import { DeleteNoteService } from '../../../core/services/DeleteNoteService';
import { ViewProvider } from '../../view/ViewProvider';
import { Note } from '../../../core/dtos/Note';

export function createHandleDelete(
  deleteService: DeleteNoteService,
  provider: ViewProvider,
) {
  return async (note: Note): Promise<void> => {
    const deleted = await deleteService.confirmAndDelete([note]);
    if (deleted) {
      provider.refresh();
    }
  };
}
