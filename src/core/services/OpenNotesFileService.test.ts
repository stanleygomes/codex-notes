import { describe, expect, it } from 'vitest';
import {
  buildRepository,
  buildUserInteraction,
  notesJsonPath,
} from '../../test/unit/fakes';
import { OpenNotesFileService } from './OpenNotesFileService';

async function setup() {
  const repository = await buildRepository();
  const { fake, userInteraction } = buildUserInteraction();
  return {
    service: new OpenNotesFileService(repository, userInteraction),
    fake,
  };
}

describe('OpenNotesFileService', () => {
  it('opens notes.json in the editor', async () => {
    const { service, fake } = await setup();

    await service.open();

    expect(fake.openTextDocument).toHaveBeenCalledWith(notesJsonPath());
    expect(fake.showError).not.toHaveBeenCalled();
  });

  it.each([
    [new Error('boom'), 'Failed to open notes.json: boom'],
    ['raw failure', 'Failed to open notes.json: raw failure'],
  ])('shows an error when opening fails with %o', async (error, message) => {
    const { service, fake } = await setup();
    fake.openTextDocument.mockRejectedValue(error);

    await service.open();

    expect(fake.showError).toHaveBeenCalledWith(message);
  });
});
