import { ImportNotesService } from '../../../core/service/ImportNotesService';
import { NotesViewProvider } from '../../../ui/NotesViewProvider';

export function createHandleImport(
  importService: ImportNotesService,
  provider: NotesViewProvider,
) {
  return async (): Promise<void> => {
    await importService.import();
    provider.refresh();
  };
}
