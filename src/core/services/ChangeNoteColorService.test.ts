import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import {
  buildNote,
  buildRepository,
  buildUserInteraction,
} from '../../test/unit/fakes';
import { NoteColorEnum } from '../enums/NoteColorEnum';
import { ChangeNoteColorService } from './ChangeNoteColorService';

async function setup() {
  const repository = await buildRepository([buildNote()]);
  const { fake, userInteraction } = buildUserInteraction();
  const service = new ChangeNoteColorService(repository, userInteraction);
  return { service, repository, fake };
}

describe('ChangeNoteColorService', () => {
  beforeEach(() => {
    vi.useFakeTimers();
    vi.setSystemTime(5_000);
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it('offers every color', async () => {
    const { service, fake } = await setup();

    await service.changeColor(buildNote());

    const [items, options] = fake.showQuickPick.mock.calls[0];
    expect(items.map((i: { value: string }) => i.value)).toEqual(
      Object.values(NoteColorEnum),
    );
    expect(options).toEqual({ placeHolder: 'Select note color' });
  });

  it('saves the selected color', async () => {
    const { service, repository, fake } = await setup();
    fake.showQuickPick.mockResolvedValue({ value: NoteColorEnum.RED });

    await service.changeColor(buildNote());

    expect(repository.findOne('note-1')).toMatchObject({
      color: NoteColorEnum.RED,
      updatedAt: 5_000,
    });
  });

  it('keeps the note when the picker is dismissed', async () => {
    const { service, repository, fake } = await setup();
    fake.showQuickPick.mockResolvedValue(undefined);

    await service.changeColor(buildNote());

    expect(repository.findOne('note-1')).toEqual(buildNote());
  });
});
