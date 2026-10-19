import { describe, expect, it } from 'vitest';
import { NOTE_COLOR_HEX, NoteColorEnum } from './NoteColorEnum';

describe('NoteColorEnum', () => {
  it('maps every color to a hex value, except NONE', () => {
    Object.values(NoteColorEnum).forEach((color) => {
      const hex = NOTE_COLOR_HEX[color];
      if (color === NoteColorEnum.NONE) {
        expect(hex).toBeNull();
      } else {
        expect(hex).toMatch(/^#[0-9A-F]{6}$/);
      }
    });
  });
});
