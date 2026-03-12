import { ChangeNoteColorService } from '../../service/ChangeNoteColorService';
import { NotesViewProvider } from '../../ui/NotesViewProvider';
import { Note } from '../../dto/Note';

export function createHandleChangeColor(
  colorService: ChangeNoteColorService,
  provider: NotesViewProvider
) {
  return async (note: Note): Promise<void> => {
    await colorService.changeColor(note);
    provider.refresh();
  };
}
