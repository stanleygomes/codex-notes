import { describe, expect, it } from 'vitest';
import { Container } from './container';
import { NoteRepository } from './core/repositories/NoteRepository';
import { UserInteraction } from './infra/editor/UserInteraction';
import { buildExtensionContext, buildRepository } from './test/unit/fakes';

describe('Container', () => {
  it('wires every service with the shared repository', async () => {
    const repository = await buildRepository();
    const { extensionContext } = buildExtensionContext();

    const container = new Container(extensionContext, repository);

    expect(container.repository).toBeInstanceOf(NoteRepository);
    expect(container.repository).toBe(repository);
    expect(container.userInteraction).toBeInstanceOf(UserInteraction);
    [
      container.createService,
      container.deleteService,
      container.renameService,
      container.searchService,
      container.duplicateService,
      container.favoriteService,
      container.colorService,
      container.sortService,
      container.filterService,
      container.locationService,
      container.quickSearchService,
      container.openNotesFileService,
    ].forEach((service) => expect(service).toBeDefined());
  });
});
