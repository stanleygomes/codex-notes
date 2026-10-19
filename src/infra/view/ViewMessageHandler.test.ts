import { describe, expect, it, vi } from 'vitest';
import { buildNote, buildRepository } from '../../test/unit/fakes';
import { DateFilterEnum } from '../../core/enums/DateFilterEnum';
import { SortTypeEnum } from '../../core/enums/SortTypeEnum';
import type { WebviewMessage } from '../../core/dtos/WebviewMessage';
import { ViewMessageHandler } from './ViewMessageHandler';
import type { ViewState } from './ViewState';

async function setup() {
  const repository = await buildRepository([buildNote()]);
  const deps = {
    viewState: {
      currentQuery: '',
      currentSort: SortTypeEnum.DATE,
      filterFavorites: false,
      activeDateFilter: undefined as DateFilterEnum | undefined,
    },
    refresh: vi.fn(),
    openNote: vi.fn(),
    createNote: vi.fn(),
    deleteNote: vi.fn(),
    renameNote: vi.fn(),
    duplicateNote: vi.fn(),
    toggleFavorite: vi.fn(),
    changeColor: vi.fn(),
    openLocation: vi.fn(),
  };
  const handler = new ViewMessageHandler(
    repository,
    deps.viewState as unknown as ViewState,
    deps.refresh,
    deps.openNote,
    deps.createNote,
    deps.deleteNote,
    deps.renameNote,
    deps.duplicateNote,
    deps.toggleFavorite,
    deps.changeColor,
    deps.openLocation,
  );
  return { handler, deps };
}

type Deps = Awaited<ReturnType<typeof setup>>['deps'];

const noteCommands: [string, (deps: Deps) => ReturnType<typeof vi.fn>][] = [
  ['openNote', (d) => d.openNote],
  ['deleteNote', (d) => d.deleteNote],
  ['renameNote', (d) => d.renameNote],
  ['duplicateNote', (d) => d.duplicateNote],
  ['toggleFavorite', (d) => d.toggleFavorite],
  ['changeColor', (d) => d.changeColor],
  ['openLocation', (d) => d.openLocation],
];

describe('ViewMessageHandler', () => {
  describe.each(noteCommands)('%s', (command, target) => {
    it('dispatches the stored note', async () => {
      const { handler, deps } = await setup();

      await handler.handle({ command, noteId: 'note-1' });

      expect(target(deps)).toHaveBeenCalledWith(buildNote());
    });

    it.each([undefined, 'unknown'])('ignores note id %o', async (noteId) => {
      const { handler, deps } = await setup();

      await handler.handle({ command, noteId });

      expect(target(deps)).not.toHaveBeenCalled();
    });
  });

  it('creates notes', async () => {
    const { handler, deps } = await setup();

    await handler.handle({ command: 'createNote' });

    expect(deps.createNote).toHaveBeenCalledOnce();
  });

  it.each([
    [{ command: 'search', query: 'api' }, 'api'],
    [{ command: 'search' }, ''],
  ] as [WebviewMessage, string][])(
    'stores the query and refreshes for %o',
    async (message, expected) => {
      const { handler, deps } = await setup();

      await handler.handle(message);

      expect(deps.viewState.currentQuery).toBe(expected);
      expect(deps.refresh).toHaveBeenCalledOnce();
    },
  );

  it.each([
    [SortTypeEnum.TITLE, SortTypeEnum.TITLE],
    [undefined, SortTypeEnum.DATE],
  ])('sort %o keeps %s and refreshes', async (sortType, expected) => {
    const { handler, deps } = await setup();

    await handler.handle({ command: 'sort', sortType });

    expect(deps.viewState.currentSort).toBe(expected);
    expect(deps.refresh).toHaveBeenCalledOnce();
  });

  it.each([
    [true, true],
    [undefined, false],
  ])('favorites filter %o becomes %s', async (filterFavorites, expected) => {
    const { handler, deps } = await setup();
    deps.viewState.filterFavorites = true;

    await handler.handle({ command: 'filterFavorites', filterFavorites });

    expect(deps.viewState.filterFavorites).toBe(expected);
    expect(deps.refresh).toHaveBeenCalledOnce();
  });

  it.each([DateFilterEnum.TODAY, undefined])(
    'sets the date filter to %o',
    async (dateFilter) => {
      const { handler, deps } = await setup();
      deps.viewState.activeDateFilter = DateFilterEnum.THIS_MONTH;

      await handler.handle({ command: 'filterDate', dateFilter });

      expect(deps.viewState.activeDateFilter).toBe(dateFilter);
      expect(deps.refresh).toHaveBeenCalledOnce();
    },
  );

  it('ignores unknown commands', async () => {
    const { handler, deps } = await setup();

    await handler.handle({ command: 'unknown', noteId: 'note-1' });

    expect(deps.refresh).not.toHaveBeenCalled();
    noteCommands.forEach(([, target]) =>
      expect(target(deps)).not.toHaveBeenCalled(),
    );
  });
});
