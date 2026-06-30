import { RenameNoteService } from '../../../core/services/RenameNoteService';
import { ViewProvider } from '../../view/ViewProvider';
import { Note } from '../../../core/dtos/Note';

export function createHandleRename(
  renameService: RenameNoteService,
  provider: ViewProvider,
) {
  return async (note: Note): Promise<void> => {
    const renamed = await renameService.rename(note);
    if (renamed) {
      provider.refresh();
    }
  };
}
