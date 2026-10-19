import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import { Container } from './container';
import { activate, deactivate } from './extension';
import { NoteColorEnum } from './core/enums/NoteColorEnum';
import { ViewProvider } from './infra/view/ViewProvider';
import {
  buildExtensionContext,
  buildNote,
  writeNoteFile,
} from './test/unit/fakes';
import { commands, window } from './test/unit/mocks/vscode';
import { buildWebviewView } from './test/unit/webview';

async function setup() {
  const { context, extensionContext } = buildExtensionContext();
  const container = await activate(extensionContext);
  const provider = window.registerWebviewViewProvider.mock
    .calls[0][1] as unknown as ViewProvider;
  const view = buildWebviewView();
  provider.resolveWebviewView(view.webviewView, {} as never, {} as never);
  view.fake.webview.postMessage.mockClear();
  const note = buildNote({ filePath: writeNoteFile('note-one.md', 'hello') });
  container.repository.save(note);
  return { context, container, view, note };
}

describe('extension', () => {
  beforeEach(() => {
    vi.spyOn(console, 'log').mockImplementation(() => {});
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it('returns the wired container', async () => {
    const { container } = await setup();

    expect(container).toBeInstanceOf(Container);
  });

  it('registers the sidebar provider and commands', async () => {
    const { context } = await setup();

    expect(window.registerWebviewViewProvider).toHaveBeenCalledWith(
      ViewProvider.viewType,
      expect.any(ViewProvider),
    );
    expect(commands.registerCommand).toHaveBeenCalledTimes(5);
    expect(context.subscriptions).toHaveLength(6);
  });

  it('creates notes from the webview and refreshes', async () => {
    const { container, view } = await setup();

    view.send({ command: 'createNote' });

    await vi.waitFor(() =>
      expect(container.repository.findAll()).toHaveLength(2),
    );
    await vi.waitFor(() =>
      expect(view.fake.webview.postMessage).toHaveBeenCalled(),
    );
  });

  it('toggles favorites from the webview and refreshes', async () => {
    const { container, view } = await setup();

    view.send({ command: 'toggleFavorite', noteId: 'note-1' });

    expect(container.repository.findOne('note-1')?.isFavorite).toBe(true);
    expect(view.fake.webview.postMessage).toHaveBeenCalledOnce();
  });

  it('deletes notes after confirmation', async () => {
    const { container, view } = await setup();
    window.showWarningMessage.mockResolvedValue('Yes');

    view.send({ command: 'deleteNote', noteId: 'note-1' });

    await vi.waitFor(() => expect(container.repository.findAll()).toEqual([]));
  });

  it('renames notes', async () => {
    const { container, view } = await setup();
    window.showInputBox.mockResolvedValue('Renamed');

    view.send({ command: 'renameNote', noteId: 'note-1' });

    await vi.waitFor(() =>
      expect(container.repository.findOne('note-1')?.title).toBe('Renamed'),
    );
  });

  it('duplicates notes', async () => {
    const { container, view } = await setup();

    view.send({ command: 'duplicateNote', noteId: 'note-1' });

    await vi.waitFor(() =>
      expect(container.repository.findAll().map((n) => n.title)).toContain(
        'Note One (1)',
      ),
    );
  });

  it('changes note colors', async () => {
    const { container, view } = await setup();
    window.showQuickPick.mockResolvedValue({ value: NoteColorEnum.GREEN });

    view.send({ command: 'changeColor', noteId: 'note-1' });

    await vi.waitFor(() =>
      expect(container.repository.findOne('note-1')?.color).toBe(
        NoteColorEnum.GREEN,
      ),
    );
  });

  it('opens notes and their location', async () => {
    const { view, note } = await setup();

    view.send({ command: 'openNote', noteId: 'note-1' });
    view.send({ command: 'openLocation', noteId: 'note-1' });

    await vi.waitFor(() =>
      expect(window.showTextDocument).toHaveBeenCalledOnce(),
    );
    expect(commands.executeCommand).toHaveBeenCalledWith(
      'revealFileInOS',
      expect.objectContaining({ fsPath: note.filePath }),
    );
  });

  it('deactivates without side effects', () => {
    expect(() => deactivate()).not.toThrow();
  });
});
