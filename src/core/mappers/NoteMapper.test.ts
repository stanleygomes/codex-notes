import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import { buildNote, writeNoteFile } from '../../test/unit/fakes';
import { NoteColorEnum } from '../enums/NoteColorEnum';
import { NoteMapper } from './NoteMapper';

const NOW = new Date(2026, 9, 15, 12).getTime();

describe('NoteMapper', () => {
  beforeEach(() => {
    vi.useFakeTimers();
    vi.setSystemTime(NOW);
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it('maps a note to the webview payload', () => {
    const filePath = writeNoteFile('note.md', 'Plain text');
    const note = buildNote({
      filePath,
      color: NoteColorEnum.BLUE,
      isFavorite: true,
      updatedAt: NOW - 3 * 60 * 60 * 1000,
    });

    expect(NoteMapper.toWebview(note)).toEqual({
      id: 'note-1',
      title: 'Note One',
      preview: 'Plain text',
      dateLabel: '3 hours ago',
      isFavorite: true,
      color: NoteColorEnum.BLUE,
      colorHex: '#ADE8E6',
    });
  });

  it('strips markdown syntax from the preview', () => {
    const markdown = [
      '# Title',
      '**bold** and _italic_ and ~~strike~~',
      '- item',
      '1. first',
      '> quote',
      '---',
      '`inline` [link](http://x) ![img](a.png)',
      '```',
      'code block',
      '```',
    ].join('\n');
    const note = buildNote({ filePath: writeNoteFile('md.md', markdown) });

    expect(NoteMapper.toWebview(note).preview).toBe(
      'Title bold and italic and strike item first quote',
    );
  });

  it('limits the preview to 80 characters', () => {
    const note = buildNote({
      filePath: writeNoteFile('long.md', 'a'.repeat(200)),
    });

    expect(NoteMapper.toWebview(note).preview).toHaveLength(80);
  });

  it('returns an empty preview when the file is missing', () => {
    expect(NoteMapper.toWebview(buildNote()).preview).toBe('');
  });
});
