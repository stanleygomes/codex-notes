import { describe, expect, it, vi } from 'vitest';
import { registerCommands } from './commands';
import type { Container } from './container';
import { buildExtensionContext, buildProvider } from './test/unit/fakes';
import { buildTextEditor, commands, window } from './test/unit/mocks/vscode';

function setup() {
  const container = {
    createService: { create: vi.fn() },
    openNotesFileService: { open: vi.fn() },
    quickSearchService: { show: vi.fn() },
  };
  const { fake: provider, provider: viewProvider } = buildProvider();
  const handleCreate = vi.fn();
  const { context, extensionContext } = buildExtensionContext();

  registerCommands(
    extensionContext,
    container as unknown as Container,
    viewProvider,
    handleCreate,
  );

  const run = (id: string) => {
    const call = commands.registerCommand.mock.calls.find(([c]) => c === id);
    return call![1]();
  };
  return { container, provider, handleCreate, context, run };
}

describe('registerCommands', () => {
  it('registers every command as a subscription', () => {
    const { context } = setup();

    expect(commands.registerCommand.mock.calls.map(([id]) => id)).toEqual([
      'codexNotes.createNote',
      'codexNotes.createNoteFromSelection',
      'codexNotes.refreshNotes',
      'codexNotes.openNotesFile',
      'codexNotes.searchNotes',
    ]);
    expect(context.subscriptions).toHaveLength(5);
  });

  it('creates notes', () => {
    const { handleCreate, run } = setup();

    run('codexNotes.createNote');

    expect(handleCreate).toHaveBeenCalledOnce();
  });

  it('creates notes from the selection', async () => {
    window.activeTextEditor = buildTextEditor('/a.ts', 'snippet');
    const { container, provider, run } = setup();

    await run('codexNotes.createNoteFromSelection');

    expect(container.createService.create).toHaveBeenCalledWith('snippet');
    expect(provider.refresh).toHaveBeenCalledOnce();
  });

  it('refreshes the view', () => {
    const { provider, run } = setup();

    run('codexNotes.refreshNotes');

    expect(provider.refresh).toHaveBeenCalledOnce();
  });

  it('opens notes.json', () => {
    const { container, run } = setup();

    run('codexNotes.openNotesFile');

    expect(container.openNotesFileService.open).toHaveBeenCalledOnce();
  });

  it('opens the quick search', () => {
    const { container, run } = setup();

    run('codexNotes.searchNotes');

    expect(container.quickSearchService.show).toHaveBeenCalledOnce();
  });
});
