import { describe, expect, it } from 'vitest';
import { FilenameHelper } from './FilenameHelper';

describe('FilenameHelper', () => {
  it.each([
    ['Simple Title', 'simple-title'],
    ['  Many   spaces  ', 'many-spaces'],
    ['bad/\\:*?"<>|chars', 'badchars'],
    ['Café com açúcar', 'caf-com-acar'],
    ['UPPER', 'upper'],
  ])('normalizes "%s" to "%s"', (title, expected) => {
    expect(FilenameHelper.normalize(title)).toBe(expected);
  });
});
