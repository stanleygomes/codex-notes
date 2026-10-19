import * as fs from 'node:fs';
import * as path from 'node:path';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import {
  buildNote,
  buildRepository,
  buildUserInteraction,
  notesDir,
  writeNoteFile,
} from '../../test/unit/fakes';
import { RenameNoteService } from './RenameNoteService';

async function setup() {
  const note = buildNote({
    title: 'Old Name',
    filePath: writeNoteFile('old-name.md', 'content'),
  });
  const repository = await buildRepository([note]);
  const { fake, userInteraction } = buildUserInteraction();
  const service = new RenameNoteService(repository, userInteraction);
  return { service, repository, fake, note };
}

describe('RenameNoteService', () => {
  beforeEach(() => {
    vi.useFakeTimers({ toFake: ['Date'] });
    vi.setSystemTime(11_000);
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it('renames the file and metadata and reopens the editor', async () => {
    const { service, repository, fake, note } = await setup();
    fake.showInputBox.mockResolvedValue('  New Name  ');

    const renamed = await service.rename(note);

    const newPath = path.join(notesDir(), 'new-name.md');
    expect(renamed).toEqual({
      ...note,
      title: 'New Name',
      filePath: newPath,
      updatedAt: 11_000,
    });
    expect(repository.findOne(note.id)).toEqual(renamed);
    expect(fs.readFileSync(newPath, 'utf8')).toBe('content');
    expect(fs.existsSync(note.filePath)).toBe(false);
    expect(fake.reopenNote).toHaveBeenCalledWith(note.filePath, newPath);
  });

  it('prefills the input and rejects blank titles', async () => {
    const { service, fake, note } = await setup();

    await service.rename(note);

    const options = fake.showInputBox.mock.calls[0][0];
    expect(options).toMatchObject({
      prompt: 'Enter new note title',
      value: 'Old Name',
    });
    expect(options.validateInput('   ')).toBe('Title cannot be empty');
    expect(options.validateInput('ok')).toBeUndefined();
  });

  it.each([undefined, '', 'Old Name', ' Old Name '])(
    'does nothing when the input is %o',
    async (input) => {
      const { service, repository, fake, note } = await setup();
      fake.showInputBox.mockResolvedValue(input);

      await expect(service.rename(note)).resolves.toBeUndefined();

      expect(repository.findOne(note.id)).toEqual(note);
      expect(fake.reopenNote).not.toHaveBeenCalled();
    },
  );

  it('refuses to overwrite an existing file', async () => {
    const { service, repository, fake, note } = await setup();
    writeNoteFile('taken.md');
    fake.showInputBox.mockResolvedValue('Taken');

    await expect(service.rename(note)).resolves.toBeUndefined();

    expect(fake.showError).toHaveBeenCalledWith(
      'A note with the filename "taken.md" already exists.',
    );
    expect(repository.findOne(note.id)).toEqual(note);
    expect(fs.existsSync(note.filePath)).toBe(true);
  });
});
