import * as assert from 'assert';
import * as vscode from 'vscode';
import { EXTENSION_ID, activateExtension, sandboxedNotesDir } from './helpers';

const CONTRIBUTED_COMMANDS = [
  'codexNotes.createNote',
  'codexNotes.createNoteFromSelection',
  'codexNotes.refreshNotes',
  'codexNotes.openNotesFile',
  'codexNotes.searchNotes',
];

suite('Extension activation', () => {
  test('activates and exposes the service container', async () => {
    const container = await activateExtension();

    assert.strictEqual(
      vscode.extensions.getExtension(EXTENSION_ID)?.isActive,
      true,
    );
    assert.ok(container.repository);
  });

  test('stores notes inside the e2e sandbox', () => {
    assert.ok(sandboxedNotesDir());
  });

  test('registers every command', async () => {
    await activateExtension();
    const commands = await vscode.commands.getCommands(true);

    for (const command of CONTRIBUTED_COMMANDS) {
      assert.ok(commands.includes(command), `${command} is not registered`);
    }
  });

  test('contributes commands, sidebar view, keybinding and quick access', () => {
    const contributes =
      vscode.extensions.getExtension(EXTENSION_ID)?.packageJSON.contributes;

    assert.deepStrictEqual(
      contributes.commands.map((c: { command: string }) => c.command),
      CONTRIBUTED_COMMANDS,
    );
    assert.strictEqual(
      contributes.views.codexNotesContainer[0].id,
      'codexNotes.notesView',
    );
    assert.strictEqual(
      contributes.keybindings[0].command,
      'codexNotes.createNote',
    );
    assert.strictEqual(contributes.quickAccess[0].prefix, 'n');
  });

  test('opens the sidebar view', async () => {
    await activateExtension();

    await vscode.commands.executeCommand(
      'workbench.view.extension.codexNotesContainer',
    );
    await vscode.commands.executeCommand('codexNotes.refreshNotes');
  });
});
