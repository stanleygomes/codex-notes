import { DuplicateNoteService } from '../../service/DuplicateNoteService';
import { NotesViewProvider } from '../../ui/NotesViewProvider';
import { Note } from '../../dto/Note';

export function createHandleDuplicate(
  duplicateService: DuplicateNoteService,
  provider: NotesViewProvider
) {
  return async (note: Note): Promise<void> => {
    await duplicateService.duplicate(note);
    provider.refresh();
  };
}
