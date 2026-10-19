import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import { buildNote, buildRepository } from '../../test/unit/fakes';
import { FavoriteNoteService } from './FavoriteNoteService';

describe('FavoriteNoteService', () => {
  beforeEach(() => {
    vi.useFakeTimers();
    vi.setSystemTime(9_000);
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it.each([false, true])('toggles isFavorite from %s', async (isFavorite) => {
    const repository = await buildRepository([buildNote({ isFavorite })]);
    const service = new FavoriteNoteService(repository);

    service.toggleFavorite(buildNote({ isFavorite }));

    expect(repository.findOne('note-1')).toMatchObject({
      isFavorite: !isFavorite,
      updatedAt: 9_000,
    });
  });
});
