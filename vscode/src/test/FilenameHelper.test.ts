import * as assert from 'assert';
import { FilenameHelper } from '../helper/FilenameHelper';

suite('FilenameHelper', () => {
  test('should normalize a simple title to lowercase with hyphens', () => {
    const result = FilenameHelper.normalize('My Note Title');
    assert.strictEqual(result, 'my-note-title');
  });

  test('should remove special characters from title', () => {
    const result = FilenameHelper.normalize('Note: "Hello" / World');
    assert.strictEqual(result, 'note-hello--world');
  });

  test('should trim whitespace from title', () => {
    const result = FilenameHelper.normalize('  Trimmed  ');
    assert.strictEqual(result, 'trimmed');
  });

  test('should collapse multiple spaces into single hyphen', () => {
    const result = FilenameHelper.normalize('multiple   spaces');
    assert.strictEqual(result, 'multiple-spaces');
  });

  test('should handle empty string', () => {
    const result = FilenameHelper.normalize('');
    assert.strictEqual(result, '');
  });
});
