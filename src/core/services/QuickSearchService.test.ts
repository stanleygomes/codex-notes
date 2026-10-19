import { describe, expect, it, vi } from 'vitest';
import {
  buildNote,
  buildRepository,
  buildUserInteraction,
} from '../../test/unit/fakes';
import { QuickSearchService } from './QuickSearchService';
import { SearchNoteService } from './SearchNoteService';

async function setup() {
  const notes = [
    buildNote({ id: 'a', title: 'Alpha' }),
    buildNote({ id: 'b', title: 'Beta' }),
  ];
  const repository = await buildRepository(notes);
  const { fake, userInteraction } = buildUserInteraction();
  const onOpenNote = vi.fn();
  const service = new QuickSearchService(
    repository,
    new SearchNoteService(repository),
    onOpenNote,
    userInteraction,
  );
  await service.show();
  const [placeholder, initialItems, onSearch, onAccept] =
    fake.showQuickSearch.mock.calls[0];
  return { placeholder, initialItems, onSearch, onAccept, onOpenNote, notes };
}

describe('QuickSearchService', () => {
  it('lists every note with a home-relative path', async () => {
    const { placeholder, initialItems, notes } = await setup();

    expect(placeholder).toBe('Search Codex Notes...');
    expect(initialItems).toEqual([
      {
        label: '$(markdown) Alpha',
        description: '~/.codex-notes/note-one.md',
        note: notes[0],
      },
      expect.objectContaining({ label: '$(markdown) Beta' }),
    ]);
  });

  it('narrows the items while typing', async () => {
    const { onSearch } = await setup();

    expect(onSearch('beta').map((i: { label: string }) => i.label)).toEqual([
      '$(markdown) Beta',
    ]);
  });

  it('opens the accepted note', async () => {
    const { initialItems, onAccept, onOpenNote, notes } = await setup();

    onAccept(initialItems[1]);

    expect(onOpenNote).toHaveBeenCalledWith(notes[1]);
  });

  it('keeps absolute paths when HOME is not set', async () => {
    const { onSearch, notes } = await setup();
    vi.stubEnv('HOME', '');

    expect(onSearch('')[0].description).toBe(notes[0].filePath);
  });

  it('keeps paths that only share a prefix with HOME', async () => {
    const { onSearch, notes } = await setup();
    vi.stubEnv('HOME', `${process.env.HOME}/.codex`);

    expect(onSearch('')[0].description).toBe(notes[0].filePath);
  });
});
