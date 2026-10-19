import { describe, expect, it } from 'vitest';
import { StringHelper } from './StringHelper';

describe('StringHelper', () => {
  it('replaces every placeholder occurrence', () => {
    expect(
      StringHelper.replace('{{a}} and {{a}} or {{b}}', { a: '1', b: '2' }),
    ).toBe('1 and 1 or 2');
  });

  it('keeps unknown placeholders', () => {
    expect(StringHelper.replace('{{missing}}', {})).toBe('{{missing}}');
  });

  it('encodes to base64', () => {
    expect(StringHelper.toBase64('/home/notes')).toBe('L2hvbWUvbm90ZXM=');
  });
});
