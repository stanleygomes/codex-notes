import { CreateNoteService } from '../../service/CreateNoteService';
import { NotesViewProvider } from '../../ui/NotesViewProvider';

export function createHandleCreate(
  createService: CreateNoteService,
  provider: NotesViewProvider
) {
  return async (): Promise<void> => {
    await createService.create();
    provider.refresh();
  };
}
