import { DeleteNoteService } from '../../service/DeleteNoteService';
import { NotesViewProvider } from '../../ui/NotesViewProvider';
import { Note } from '../../dto/Note';

export function createHandleDelete(
  deleteService: DeleteNoteService,
  provider: NotesViewProvider
) {
  return async (note: Note): Promise<void> => {
    const deleted = await deleteService.confirmAndDelete([note]);
    if (deleted) {
      provider.refresh();
    }
  };
}
