import { describe, expect, it } from 'vitest';
import { buildNote } from '../../test/unit/fakes';
import { SortTypeEnum } from '../enums/SortTypeEnum';
import { SortNotesService } from './SortNotesService';

const notes = [
  buildNote({ id: 'b', title: 'Bravo', updatedAt: 2, isFavorite: false }),
  buildNote({ id: 'a', title: 'alpha', updatedAt: 1, isFavorite: true }),
  buildNote({ id: 'c', title: 'Charlie', updatedAt: 3, isFavorite: false }),
  buildNote({ id: 'd', title: 'Delta', updatedAt: 4, isFavorite: true }),
];

describe('SortNotesService', () => {
  const service = new SortNotesService();

  it.each([
    [SortTypeEnum.TITLE, ['a', 'b', 'c', 'd']],
    [SortTypeEnum.DATE, ['d', 'c', 'b', 'a']],
    [SortTypeEnum.FAVORITE, ['d', 'a', 'c', 'b']],
  ])('sorts by %s', (sortType, expected) => {
    expect(service.sort(notes, sortType).map((n) => n.id)).toEqual(expected);
  });

  it('does not mutate the input', () => {
    const before = notes.map((n) => n.id);

    service.sort(notes, SortTypeEnum.TITLE);

    expect(notes.map((n) => n.id)).toEqual(before);
  });
});
