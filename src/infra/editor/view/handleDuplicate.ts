import { DuplicateNoteService } from '../../../core/service/DuplicateNoteService';
import { NotesViewProvider } from '../../../ui/NotesViewProvider';
import { Note } from '../../../core/dto/Note';

export function createHandleDuplicate(
  duplicateService: DuplicateNoteService,
  provider: NotesViewProvider,
) {
  return async (note: Note): Promise<void> => {
    await duplicateService.duplicate(note);
    provider.refresh();
  };
}
