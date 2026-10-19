import { describe, expect, it } from 'vitest';
import {
  buildNote,
  buildRepository,
  buildUserInteraction,
  writeNoteFile,
} from '../../test/unit/fakes';
import { FileHelper } from '../helpers/FileHelper';
import { CreateNoteService } from './CreateNoteService';
import { DuplicateNoteService } from './DuplicateNoteService';

async function setup(extraTitles: string[] = []) {
  const original = buildNote({
    title: 'Plan',
    filePath: writeNoteFile('plan.md', 'original content'),
  });
  const repository = await buildRepository([
    original,
    ...extraTitles.map((title, i) => buildNote({ id: `x${i}`, title })),
  ]);
  const { userInteraction } = buildUserInteraction();
  const service = new DuplicateNoteService(
    repository,
    new CreateNoteService(repository, userInteraction),
  );
  return { service, repository, original };
}

describe('DuplicateNoteService', () => {
  it('copies the content into a "(1)" note', async () => {
    const { service, repository, original } = await setup();

    const copy = await service.duplicate(original);

    expect(copy.title).toBe('Plan (1)');
    expect(copy.id).not.toBe(original.id);
    expect(FileHelper.readText(copy.filePath)).toBe('original content');
    expect(repository.findAll()).toHaveLength(2);
  });

  it('skips titles that already exist', async () => {
    const { service, original } = await setup(['Plan (1)', 'Plan (2)']);

    const copy = await service.duplicate(original);

    expect(copy.title).toBe('Plan (3)');
  });
});
