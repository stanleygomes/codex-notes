import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import {
  buildNote,
  buildRepository,
  writeNoteFile,
} from '../../test/unit/fakes';
import { DateFilterEnum } from '../../core/enums/DateFilterEnum';
import { SortTypeEnum } from '../../core/enums/SortTypeEnum';
import { FilterNotesService } from '../../core/services/FilterNotesService';
import { SearchNoteService } from '../../core/services/SearchNoteService';
import { SortNotesService } from '../../core/services/SortNotesService';
import { ViewState } from './ViewState';

const NOW = new Date(2026, 9, 15, 12).getTime();
const DAY = 24 * 60 * 60 * 1000;

async function setup() {
  const repository = await buildRepository([
    buildNote({
      id: 'recent',
      title: 'Recent work',
      updatedAt: NOW - 1000,
      filePath: writeNoteFile('recent.md', 'todo'),
    }),
    buildNote({
      id: 'fav',
      title: 'Archive',
      updatedAt: NOW - 60 * DAY,
      isFavorite: true,
      filePath: writeNoteFile('fav.md', 'work log'),
    }),
  ]);
  return new ViewState(
    repository,
    new SearchNoteService(repository),
    new SortNotesService(),
    new FilterNotesService(),
  );
}

function ids(state: ViewState): string[] {
  return state.getPayload().notes.map((n: { id: string }) => n.id);
}

describe('ViewState', () => {
  beforeEach(() => {
    vi.useFakeTimers();
    vi.setSystemTime(NOW);
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it('defaults to every note sorted by date', async () => {
    const state = await setup();

    expect(state.getPayload()).toEqual({
      command: 'updateNotes',
      notes: [
        expect.objectContaining({ id: 'recent', preview: 'todo' }),
        expect.objectContaining({ id: 'fav', preview: 'work log' }),
      ],
    });
  });

  it('applies the search query', async () => {
    const state = await setup();
    state.currentQuery = 'archive';

    expect(ids(state)).toEqual(['fav']);
  });

  it('ignores blank queries', async () => {
    const state = await setup();
    state.currentQuery = '   ';

    expect(ids(state)).toEqual(['recent', 'fav']);
  });

  it('applies the favorites filter', async () => {
    const state = await setup();
    state.filterFavorites = true;

    expect(ids(state)).toEqual(['fav']);
  });

  it('applies the date filter', async () => {
    const state = await setup();
    state.activeDateFilter = DateFilterEnum.TODAY;

    expect(ids(state)).toEqual(['recent']);
  });

  it('applies the sort type', async () => {
    const state = await setup();
    state.currentSort = SortTypeEnum.TITLE;

    expect(ids(state)).toEqual(['fav', 'recent']);
  });
});
