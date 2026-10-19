import * as os from 'node:os';
import * as path from 'node:path';
import { describe, expect, it } from 'vitest';
import { NotesSettings } from './NotesSettings';

describe('NotesSettings', () => {
  it('uses markdown files inside ~/.codex-notes', () => {
    expect(NotesSettings.getFileExtension()).toBe('md');
    expect(NotesSettings.getNotesDirectory()).toBe(
      path.join(os.homedir(), '.codex-notes'),
    );
  });
});
