import * as assert from 'assert';
import { NoteColorEnum } from '../enum/NoteColorEnum';
import { BaseNoteRepository } from '../repository/BaseNoteRepository';

class TestNoteRepository extends BaseNoteRepository {
  mapRow(row: Record<string, unknown>) {
    return this.rowToNote(row);
  }
}

suite('BaseNoteRepository', () => {
  const repository = new TestNoteRepository();

  test('should map favorite and color when sqlite row uses string values', () => {
    const note = repository.mapRow({
      id: '1',
      title: 'Test',
      filePath: '/tmp/test.md',
      createdAt: '1000',
      updatedAt: '2000',
      isFavorite: '1',
      color: 'blue',
    });

    assert.strictEqual(note.createdAt, 1000);
    assert.strictEqual(note.updatedAt, 2000);
    assert.strictEqual(note.isFavorite, true);
    assert.strictEqual(note.color, NoteColorEnum.BLUE);
  });

  test('should fallback to NONE color when sqlite row has invalid value', () => {
    const note = repository.mapRow({
      id: '1',
      title: 'Test',
      filePath: '/tmp/test.md',
      createdAt: 1000,
      updatedAt: 2000,
      isFavorite: 0,
      color: 'UNKNOWN',
    });

    assert.strictEqual(note.isFavorite, false);
    assert.strictEqual(note.color, NoteColorEnum.NONE);
  });
});
