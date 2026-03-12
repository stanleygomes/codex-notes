import * as assert from 'assert';
import { SortNotesService } from '../service/SortNotesService';
import { SortTypeEnum } from '../enum/SortTypeEnum';
import { Note } from '../dto/Note';
import { NoteColorEnum } from '../enum/NoteColorEnum';

function makeNote(id: string, title: string, updatedAt: number, isFavorite: boolean = false): Note {
  return {
    id,
    title,
    filePath: `/tmp/${id}.md`,
    createdAt: updatedAt,
    updatedAt,
    isFavorite,
    color: NoteColorEnum.NONE,
  };
}

suite('SortNotesService', () => {
  const service = new SortNotesService();

  test('should sort notes by title alphabetically', () => {
    const notes = [makeNote('1', 'Zebra', 1000), makeNote('2', 'Apple', 2000)];
    const result = service.sort(notes, SortTypeEnum.TITLE);
    assert.strictEqual(result[0].title, 'Apple');
    assert.strictEqual(result[1].title, 'Zebra');
  });

  test('should sort notes by date descending (most recent first)', () => {
    const notes = [makeNote('1', 'Old', 1000), makeNote('2', 'New', 9000)];
    const result = service.sort(notes, SortTypeEnum.DATE);
    assert.strictEqual(result[0].title, 'New');
    assert.strictEqual(result[1].title, 'Old');
  });

  test('should sort favorites first when sorting by favorite', () => {
    const notes = [
      makeNote('1', 'Regular', 1000, false),
      makeNote('2', 'Favorite', 500, true),
    ];
    const result = service.sort(notes, SortTypeEnum.FAVORITE);
    assert.strictEqual(result[0].title, 'Favorite');
  });

  test('should not mutate original array when sorting', () => {
    const notes = [makeNote('1', 'Zebra', 1000), makeNote('2', 'Apple', 2000)];
    const original = [...notes];
    service.sort(notes, SortTypeEnum.TITLE);
    assert.strictEqual(notes[0].title, original[0].title);
  });
});
