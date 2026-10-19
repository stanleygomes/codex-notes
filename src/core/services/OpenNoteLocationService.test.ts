import { describe, expect, it } from 'vitest';
import { buildNote, buildUserInteraction } from '../../test/unit/fakes';
import { OpenNoteLocationService } from './OpenNoteLocationService';

describe('OpenNoteLocationService', () => {
  it('reveals the note file in the OS', () => {
    const { fake, userInteraction } = buildUserInteraction();
    const note = buildNote();

    new OpenNoteLocationService(userInteraction).openLocation(note);

    expect(fake.revealFileInOS).toHaveBeenCalledWith(note.filePath);
  });
});
