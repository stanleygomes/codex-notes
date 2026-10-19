import { describe, expect, it } from 'vitest';
import {
  buildNote,
  buildRepository,
  writeNoteFile,
} from '../../test/unit/fakes';
import { SearchNoteService } from './SearchNoteService';

describe('SearchNoteService', () => {
  it('searches titles and file contents', async () => {
    const repository = await buildRepository([
      buildNote({
        id: 'title',
        title: 'Kubernetes',
        filePath: writeNoteFile('k.md'),
      }),
      buildNote({
        id: 'content',
        title: 'Infra',
        filePath: writeNoteFile('i.md', 'deploy on kubernetes'),
      }),
      buildNote({
        id: 'none',
        title: 'Recipes',
        filePath: writeNoteFile('r.md'),
      }),
    ]);

    const result = new SearchNoteService(repository).search('kubernetes');

    expect(result.map((n) => n.id)).toEqual(['title', 'content']);
  });

  it('returns every note for an empty query', async () => {
    const repository = await buildRepository([buildNote()]);

    expect(new SearchNoteService(repository).search('')).toHaveLength(1);
  });
});
