import * as fs from 'node:fs';
import * as path from 'node:path';
import { describe, expect, it, vi } from 'vitest';
import {
  buildNote,
  notesDir,
  notesJsonPath,
  readNotesJson,
  writeNoteFile,
} from '../../test/unit/fakes';
import { NoteColorEnum } from '../enums/NoteColorEnum';
import { NoteRepository } from './NoteRepository';

function writeNotesJson(content: string): void {
  fs.mkdirSync(notesDir(), { recursive: true });
  fs.writeFileSync(notesJsonPath(), content);
}

describe('NoteRepository', () => {
  it('throws when used before initialization', () => {
    expect(() => NoteRepository.getInstance()).toThrow(
      'NoteRepository not initialized. Call initialize() first.',
    );
  });

  it('initializes once and reuses the same instance', async () => {
    const first = await NoteRepository.initialize();
    const second = await NoteRepository.initialize();

    expect(second).toBe(first);
    expect(NoteRepository.getInstance()).toBe(first);
  });

  it('creates an empty notes.json on first run', async () => {
    const repository = await NoteRepository.initialize();

    expect(repository.getNotesJsonPath()).toBe(notesJsonPath());
    expect(repository.findAll()).toEqual([]);
    expect(readNotesJson()).toEqual({
      description: expect.stringContaining(notesDir()),
      notes: [],
    });
  });

  it('migrates existing markdown files on first run', async () => {
    const filePath = writeNoteFile('old-note.md', 'legacy');
    writeNoteFile('ignored.txt');

    const repository = await NoteRepository.initialize();

    expect(repository.findAll()).toEqual([
      expect.objectContaining({
        title: 'old-note',
        filePath,
        isFavorite: false,
        color: NoteColorEnum.NONE,
      }),
    ]);
    expect(readNotesJson().notes).toHaveLength(1);
  });

  it('skips files that cannot be migrated', async () => {
    vi.spyOn(console, 'error').mockImplementation(() => {});
    fs.mkdirSync(notesDir(), { recursive: true });
    fs.symlinkSync(
      path.join(notesDir(), 'missing-target'),
      path.join(notesDir(), 'broken.md'),
    );

    const repository = await NoteRepository.initialize();

    expect(repository.findAll()).toEqual([]);
    expect(console.error).toHaveBeenCalledWith(
      expect.stringContaining('Failed to migrate file'),
      expect.anything(),
    );
  });

  it('loads notes from an existing notes.json', async () => {
    const note = buildNote();
    writeNotesJson(JSON.stringify({ description: '', notes: [note] }));

    const repository = await NoteRepository.initialize();

    expect(repository.findAll()).toEqual([note]);
  });

  it('treats a notes.json without notes as empty', async () => {
    writeNotesJson('{}');

    const repository = await NoteRepository.initialize();

    expect(repository.findAll()).toEqual([]);
  });

  it('starts empty when notes.json is corrupted', async () => {
    vi.spyOn(console, 'error').mockImplementation(() => {});
    writeNotesJson('{not json');

    const repository = await NoteRepository.initialize();

    expect(repository.findAll()).toEqual([]);
    expect(console.error).toHaveBeenCalledWith(
      'Error loading notes.json, initializing empty storage',
      expect.any(SyntaxError),
    );
  });

  it('inserts, updates and persists notes', async () => {
    const repository = await NoteRepository.initialize();

    repository.save(buildNote());
    repository.save(buildNote({ title: 'Renamed' }));

    expect(repository.findAll()).toHaveLength(1);
    expect(repository.findOne('note-1')?.title).toBe('Renamed');
    expect(readNotesJson().notes[0].title).toBe('Renamed');
  });

  it('returns undefined for unknown ids', async () => {
    const repository = await NoteRepository.initialize();

    expect(repository.findOne('nope')).toBeUndefined();
  });

  it('deletes notes and persists the change', async () => {
    const repository = await NoteRepository.initialize();
    repository.save(buildNote());

    repository.delete('note-1');

    expect(repository.findAll()).toEqual([]);
    expect(readNotesJson().notes).toEqual([]);
  });

  it('logs when notes.json cannot be written', async () => {
    vi.spyOn(console, 'error').mockImplementation(() => {});
    fs.mkdirSync(notesJsonPath(), { recursive: true });
    const repository = await NoteRepository.initialize();

    repository.save(buildNote());

    expect(repository.findAll()).toHaveLength(1);
    expect(console.error).toHaveBeenCalledWith(
      'Error persisting notes.json',
      expect.anything(),
    );
  });
});
