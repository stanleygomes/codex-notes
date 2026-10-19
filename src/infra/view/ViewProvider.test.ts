import { describe, expect, it, vi } from 'vitest';
import { buildNote, buildRepository } from '../../test/unit/fakes';
import { Uri } from '../../test/unit/mocks/vscode';
import { buildWebviewView } from '../../test/unit/webview';
import { FilterNotesService } from '../../core/services/FilterNotesService';
import { SearchNoteService } from '../../core/services/SearchNoteService';
import { SortNotesService } from '../../core/services/SortNotesService';
import { ViewProvider } from './ViewProvider';

async function setup() {
  const repository = await buildRepository([buildNote()]);
  const callbacks = Array.from({ length: 8 }, () => vi.fn());
  const [open, create, remove, rename, duplicate, favorite, color, location] =
    callbacks;
  const extensionUri = Uri.file(process.cwd());
  const provider = new ViewProvider(
    extensionUri as never,
    repository,
    new SearchNoteService(repository),
    new SortNotesService(),
    new FilterNotesService(),
    open,
    create,
    remove,
    rename,
    duplicate,
    favorite,
    color,
    location,
  );
  return { provider, extensionUri, open };
}

function resolve(provider: ViewProvider) {
  const view = buildWebviewView();
  provider.resolveWebviewView(view.webviewView, {} as never, {} as never);
  return view;
}

describe('ViewProvider', () => {
  it('exposes the contributed view id', () => {
    expect(ViewProvider.viewType).toBe('codexNotes.notesView');
  });

  it('ignores refresh before the view is resolved', async () => {
    const { provider } = await setup();

    expect(() => provider.refresh()).not.toThrow();
  });

  it('configures the webview and posts the first payload', async () => {
    const { provider, extensionUri } = await setup();

    const { fake } = resolve(provider);

    expect(fake.webview.options).toEqual({
      enableScripts: true,
      localResourceRoots: [extensionUri],
    });
    expect(fake.webview.html).toContain('<!doctype html>');
    expect(fake.webview.postMessage).toHaveBeenCalledWith({
      command: 'updateNotes',
      notes: [expect.objectContaining({ id: 'note-1' })],
    });
  });

  it('routes webview messages to the handlers', async () => {
    const { provider, open } = await setup();
    const { send } = resolve(provider);

    send({ command: 'openNote', noteId: 'note-1' });

    expect(open).toHaveBeenCalledWith(buildNote());
  });

  it('refreshes after a search message', async () => {
    const { provider } = await setup();
    const { fake, send } = resolve(provider);
    fake.webview.postMessage.mockClear();

    send({ command: 'search', query: 'nothing matches' });

    expect(fake.webview.postMessage).toHaveBeenCalledWith({
      command: 'updateNotes',
      notes: [],
    });
  });

  it.each([
    [true, 1],
    [false, 0],
  ])('visible=%s refreshes %d times', async (visible, refreshes) => {
    const { provider } = await setup();
    const { fake, changeVisibility } = resolve(provider);
    fake.webview.postMessage.mockClear();

    changeVisibility(visible);

    expect(fake.webview.postMessage).toHaveBeenCalledTimes(refreshes);
  });
});
