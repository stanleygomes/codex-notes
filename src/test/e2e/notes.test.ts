import * as assert from 'assert';
import * as fs from 'fs';
import * as vscode from 'vscode';
import type { Container } from '../../container';
import { NoteColorEnum } from '../../core/enums/NoteColorEnum';
import {
  activateExtension,
  activeEditorPath,
  notePath,
  readNotesJson,
  resetNotes,
  waitFor,
} from './helpers';

suite('Note management', () => {
  let container: Container;

  suiteSetup(async () => {
    container = await activateExtension();
  });

  setup(async () => {
    await resetNotes(container);
  });

  suiteTeardown(async () => {
    await resetNotes(container);
  });

  test('creates a note file, persists metadata and opens it', async () => {
    await vscode.commands.executeCommand('codexNotes.createNote');

    const filePath = notePath('untitled.md');
    assert.ok(fs.existsSync(filePath));
    assert.deepStrictEqual(
      readNotesJson(container).map((n) => n.title),
      ['Untitled'],
    );
    await waitFor(() => activeEditorPath() === filePath);
  });

  test('numbers new notes to keep titles unique', async () => {
    await vscode.commands.executeCommand('codexNotes.createNote');
    await vscode.commands.executeCommand('codexNotes.createNote');

    assert.deepStrictEqual(
      container.repository.findAll().map((n) => n.title),
      ['Untitled', 'Untitled 1'],
    );
    assert.ok(fs.existsSync(notePath('untitled-1.md')));
  });

  test('creates a note from the editor selection', async () => {
    const document = await vscode.workspace.openTextDocument({
      content: 'snippet worth keeping',
    });
    const editor = await vscode.window.showTextDocument(document);
    editor.selection = new vscode.Selection(0, 0, 0, 7);

    await vscode.commands.executeCommand('codexNotes.createNoteFromSelection');

    assert.strictEqual(
      fs.readFileSync(notePath('untitled.md'), 'utf8'),
      'snippet',
    );
  });

  test('opens notes.json in the editor', async () => {
    await vscode.commands.executeCommand('codexNotes.openNotesFile');

    await waitFor(
      () => activeEditorPath() === container.repository.getNotesJsonPath(),
    );
  });

  test('shows a quick search that can be dismissed', async () => {
    await container.createService.createWithTitle('Searchable');

    await vscode.commands.executeCommand('codexNotes.searchNotes');
    await new Promise((resolve) => setTimeout(resolve, 300));
    await vscode.commands.executeCommand('workbench.action.closeQuickOpen');

    assert.strictEqual(container.repository.findAll().length, 1);
  });

  test('searches titles and contents', async () => {
    await container.createService.createWithTitle('Groceries', 'milk');
    await container.createService.createWithTitle('Infra', 'deploy k8s');

    assert.deepStrictEqual(
      container.searchService.search('k8s').map((n) => n.title),
      ['Infra'],
    );
  });

  test('renames the file and reopens it in the editor', async () => {
    const note = await container.createService.createWithTitle('Draft', 'x');
    const original = container.userInteraction.showInputBox;
    container.userInteraction.showInputBox = async () => 'Final Version';

    try {
      await container.renameService.rename(note);
    } finally {
      container.userInteraction.showInputBox = original;
    }

    const newPath = notePath('final-version.md');
    assert.ok(fs.existsSync(newPath));
    assert.ok(!fs.existsSync(note.filePath));
    assert.strictEqual(readNotesJson(container)[0].title, 'Final Version');
    await waitFor(() => activeEditorPath() === newPath);
  });

  test('duplicates a note with its content', async () => {
    const note = await container.createService.createWithTitle('Plan', 'v1');

    const copy = await container.duplicateService.duplicate(note);

    assert.strictEqual(copy.title, 'Plan (1)');
    assert.strictEqual(fs.readFileSync(copy.filePath, 'utf8'), 'v1');
    assert.strictEqual(readNotesJson(container).length, 2);
  });

  test('persists favorites and colors to notes.json', async () => {
    const note = await container.createService.createWithTitle('Pinned');

    container.favoriteService.toggleFavorite(note);
    const original = container.userInteraction.showQuickPick;
    container.userInteraction.showQuickPick = (async () => ({
      value: NoteColorEnum.PURPLE,
    })) as typeof original;
    try {
      await container.colorService.changeColor(
        container.repository.findOne(note.id)!,
      );
    } finally {
      container.userInteraction.showQuickPick = original;
    }

    const [stored] = readNotesJson(container);
    assert.strictEqual(stored.isFavorite, true);
    assert.strictEqual(stored.color, NoteColorEnum.PURPLE);
  });

  test('deletes the file and metadata after confirmation', async () => {
    const note = await container.createService.createWithTitle('Trash');
    const original = container.userInteraction.showConfirmation;
    container.userInteraction.showConfirmation = async () => true;

    try {
      await container.deleteService.confirmAndDelete([note]);
    } finally {
      container.userInteraction.showConfirmation = original;
    }

    assert.ok(!fs.existsSync(note.filePath));
    assert.deepStrictEqual(readNotesJson(container), []);
  });
});
