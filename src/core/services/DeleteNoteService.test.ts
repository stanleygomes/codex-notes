import * as fs from 'node:fs';
import { describe, expect, it } from 'vitest';
import {
  buildNote,
  buildRepository,
  buildUserInteraction,
  writeNoteFile,
} from '../../test/unit/fakes';
import { DeleteNoteService } from './DeleteNoteService';

async function setup(count = 1) {
  const notes = Array.from({ length: count }, (_, i) =>
    buildNote({
      id: `n${i}`,
      title: `Note ${i}`,
      filePath: writeNoteFile(`note-${i}.md`),
    }),
  );
  const repository = await buildRepository(notes);
  const { fake, userInteraction } = buildUserInteraction();
  const service = new DeleteNoteService(repository, userInteraction);
  return { service, repository, fake, notes };
}

describe('DeleteNoteService', () => {
  it('deletes files and metadata after confirmation', async () => {
    const { service, repository, fake, notes } = await setup();
    fake.showConfirmation.mockResolvedValue(true);

    await expect(service.confirmAndDelete(notes)).resolves.toBe(true);

    expect(fake.showConfirmation).toHaveBeenCalledWith(
      'Delete "Note 0"? This action cannot be undone.',
    );
    expect(repository.findAll()).toEqual([]);
    expect(fs.existsSync(notes[0].filePath)).toBe(false);
  });

  it('keeps everything when the user declines', async () => {
    const { service, repository, fake, notes } = await setup();
    fake.showConfirmation.mockResolvedValue(false);

    await expect(service.confirmAndDelete(notes)).resolves.toBe(false);

    expect(repository.findAll()).toHaveLength(1);
    expect(fs.existsSync(notes[0].filePath)).toBe(true);
  });

  it('lists only the first five titles in the confirmation', async () => {
    const { service, fake, notes } = await setup(7);
    fake.showConfirmation.mockResolvedValue(false);

    await service.confirmAndDelete(notes);

    expect(fake.showConfirmation).toHaveBeenCalledWith(
      'Delete "Note 0", "Note 1", "Note 2", "Note 3", "Note 4" and 2 more? This action cannot be undone.',
    );
  });
});
