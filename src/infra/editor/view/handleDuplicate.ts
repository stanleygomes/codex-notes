import { DuplicateNoteService } from '../../../core/services/DuplicateNoteService';
import { ViewProvider } from '../../view/ViewProvider';
import { Note } from '../../../core/dtos/Note';

export function createHandleDuplicate(
  duplicateService: DuplicateNoteService,
  provider: ViewProvider,
) {
  return async (note: Note): Promise<void> => {
    await duplicateService.duplicate(note);
    provider.refresh();
  };
}
