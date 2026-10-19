import * as path from 'node:path';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import {
  buildNote,
  buildRepository,
  buildUserInteraction,
  notesDir,
  readNotesJson,
} from '../../test/unit/fakes';
import { FileHelper } from '../helpers/FileHelper';
import { NoteColorEnum } from '../enums/NoteColorEnum';
import { CreateNoteService } from './CreateNoteService';

async function setup(titles: string[] = []) {
  const repository = await buildRepository(
    titles.map((title, i) => buildNote({ id: `n${i}`, title })),
  );
  const { fake, userInteraction } = buildUserInteraction();
  const service = new CreateNoteService(repository, userInteraction);
  return { service, repository, fake };
}

describe('CreateNoteService', () => {
  beforeEach(() => {
    vi.useFakeTimers({ toFake: ['Date'] });
    vi.setSystemTime(7_000);
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it('creates an empty "Untitled" note and opens it', async () => {
    const { service, repository, fake } = await setup();

    const note = await service.create();

    const filePath = path.join(notesDir(), 'untitled.md');
    expect(note).toEqual({
      id: expect.any(String),
      title: 'Untitled',
      filePath,
      createdAt: 7_000,
      updatedAt: 7_000,
      isFavorite: false,
      color: NoteColorEnum.NONE,
    });
    expect(FileHelper.readText(filePath)).toBe('');
    expect(repository.findAll()).toEqual([note]);
    expect(readNotesJson().notes).toEqual([note]);
    expect(fake.openTextDocument).toHaveBeenCalledWith(filePath);
  });

  it('writes the given content', async () => {
    const { service } = await setup();

    const note = await service.create('selected text');

    expect(FileHelper.readText(note!.filePath)).toBe('selected text');
  });

  it('picks the next free "Untitled N" title', async () => {
    const { service } = await setup(['Untitled', 'Untitled 1']);

    const note = await service.create();

    expect(note?.title).toBe('Untitled 2');
  });

  it('creates a note with an explicit title', async () => {
    const { service } = await setup();

    const note = await service.createWithTitle('My Plan', 'body');

    expect(note.filePath).toBe(path.join(notesDir(), 'my-plan.md'));
    expect(FileHelper.readText(note.filePath)).toBe('body');
  });

  it('defaults to empty content for explicit titles', async () => {
    const { service } = await setup();

    const note = await service.createWithTitle('Empty');

    expect(FileHelper.readText(note.filePath)).toBe('');
  });
});
