import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import { buildNote } from '../../test/unit/fakes';
import { DateFilterEnum } from '../enums/DateFilterEnum';
import { FilterNotesService } from './FilterNotesService';

const NOW = new Date(2026, 9, 15, 12);

const notes = [
  buildNote({ id: 'today', updatedAt: new Date(2026, 9, 15, 8).getTime() }),
  buildNote({ id: 'week', updatedAt: new Date(2026, 9, 12).getTime() }),
  buildNote({ id: 'month', updatedAt: new Date(2026, 9, 2).getTime() }),
  buildNote({ id: 'old', updatedAt: new Date(2026, 8, 20).getTime() }),
];

describe('FilterNotesService', () => {
  const service = new FilterNotesService();

  beforeEach(() => {
    vi.useFakeTimers();
    vi.setSystemTime(NOW);
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it('keeps only favorites', () => {
    const list = [
      buildNote({ id: 'a', isFavorite: true }),
      buildNote({ id: 'b' }),
    ];

    expect(service.filterByFavorite(list).map((n) => n.id)).toEqual(['a']);
  });

  it.each([
    [DateFilterEnum.TODAY, ['today']],
    [DateFilterEnum.THIS_WEEK, ['today', 'week']],
    [DateFilterEnum.THIS_MONTH, ['today', 'week', 'month']],
  ])('filters by %s', (filter, expected) => {
    expect(service.filterByDateRange(notes, filter).map((n) => n.id)).toEqual(
      expected,
    );
  });
});
