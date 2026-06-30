import { ChangeNoteColorService } from '../../../core/services/ChangeNoteColorService';
import { ViewProvider } from '../../view/ViewProvider';
import { Note } from '../../../core/dtos/Note';

export function createHandleChangeColor(
  colorService: ChangeNoteColorService,
  provider: ViewProvider,
) {
  return async (note: Note): Promise<void> => {
    await colorService.changeColor(note);
    provider.refresh();
  };
}
