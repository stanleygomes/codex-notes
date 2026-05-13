import * as assert from 'assert';
import { SearchHelper } from '../helper/SearchHelper';
import { Note } from '../dto/Note';
import { NoteColorEnum } from '../enum/NoteColorEnum';

function makeNote(id: string, title: string): Note {
  return {
    id,
    title,
    filePath: `/tmp/${id}.md`,
    createdAt: Date.now(),
    updatedAt: Date.now(),
    isFavorite: false,
    color: NoteColorEnum.NONE,
  };
}

suite('SearchHelper', () => {
  test('should return all notes when query is empty', () => {
    const notes = [makeNote('1', 'Alpha'), makeNote('2', 'Beta')];
    const result = SearchHelper.search(notes, '');
    assert.strictEqual(result.length, 2);
  });

  test('should return exact match when query matches title exactly', () => {
    const notes = [makeNote('1', 'Alpha'), makeNote('2', 'Beta'), makeNote('3', 'Gamma')];
    const result = SearchHelper.search(notes, 'Alpha');
    assert.strictEqual(result[0].title, 'Alpha');
  });

  test('should filter out notes that do not match query', () => {
    const notes = [makeNote('1', 'Shopping list'), makeNote('2', 'Meeting notes')];
    const result = SearchHelper.search(notes, 'xyz');
    assert.strictEqual(result.length, 0);
  });

  test('should rank title matches higher than content matches', () => {
    const notes = [makeNote('1', 'Something else'), makeNote('2', 'Recipe')];
    const contentMap = new Map([
      ['1', 'This has recipe in content'],
      ['2', 'Quick recipe title note'],
    ]);
    const result = SearchHelper.search(notes, 'recipe', contentMap);
    assert.strictEqual(result[0].title, 'Recipe');
  });

  test('should search case insensitively', () => {
    const notes = [makeNote('1', 'TypeScript Tips')];
    const result = SearchHelper.search(notes, 'typescript');
    assert.strictEqual(result.length, 1);
  });

  test('should match partial words in title', () => {
    const notes = [makeNote('1', 'Meeting notes'), makeNote('2', 'Shopping')];
    const result = SearchHelper.search(notes, 'meet');
    assert.strictEqual(result.length, 1);
    assert.strictEqual(result[0].title, 'Meeting notes');
  });
});
