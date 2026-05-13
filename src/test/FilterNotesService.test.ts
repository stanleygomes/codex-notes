import * as assert from 'assert';
import { FilterNotesService } from '../service/FilterNotesService';
import { DateFilterEnum } from '../enum/DateFilterEnum';
import { Note } from '../dto/Note';
import { NoteColorEnum } from '../enum/NoteColorEnum';

function makeNote(id: string, updatedAt: number, isFavorite: boolean = false): Note {
  return {
    id,
    title: `Note ${id}`,
    filePath: `/tmp/${id}.md`,
    createdAt: updatedAt,
    updatedAt,
    isFavorite,
    color: NoteColorEnum.NONE,
  };
}

suite('FilterNotesService', () => {
  const service = new FilterNotesService();

  test('should filter only favorite notes', () => {
    const notes = [makeNote('1', 1000, true), makeNote('2', 2000, false), makeNote('3', 3000, true)];
    const result = service.filterByFavorite(notes);
    assert.strictEqual(result.length, 2);
    assert.ok(result.every((n: Note) => n.isFavorite));
  });

  test('should return empty array when no favorites exist', () => {
    const notes = [makeNote('1', 1000, false), makeNote('2', 2000, false)];
    const result = service.filterByFavorite(notes);
    assert.strictEqual(result.length, 0);
  });

  test('should filter notes updated today', () => {
    const now = Date.now();
    const yesterday = now - 25 * 60 * 60 * 1000;
    const notes = [makeNote('1', now), makeNote('2', yesterday)];
    const result = service.filterByDateRange(notes, DateFilterEnum.TODAY);
    assert.strictEqual(result.length, 1);
    assert.strictEqual(result[0].id, '1');
  });

  test('should include notes from the current month when filtering by month', () => {
    const now = Date.now();
    const twoMonthsAgo = now - 62 * 24 * 60 * 60 * 1000;
    const notes = [makeNote('1', now), makeNote('2', twoMonthsAgo)];
    const result = service.filterByDateRange(notes, DateFilterEnum.THIS_MONTH);
    assert.strictEqual(result.length, 1);
    assert.strictEqual(result[0].id, '1');
  });
});
