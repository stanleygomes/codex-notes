import { RenameNoteService } from '../../service/RenameNoteService';
import { NotesViewProvider } from '../../ui/NotesViewProvider';
import { Note } from '../../dto/Note';

export function createHandleRename(
  renameService: RenameNoteService,
  provider: NotesViewProvider
) {
  return async (note: Note): Promise<void> => {
    const renamed = await renameService.rename(note);
    if (renamed) {
      provider.refresh();
    }
  };
}
