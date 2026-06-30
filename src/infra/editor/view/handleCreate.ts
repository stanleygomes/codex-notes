import { CreateNoteService } from '../../../core/services/CreateNoteService';
import { ViewProvider } from '../../view/ViewProvider';

export function createHandleCreate(
  createService: CreateNoteService,
  provider: ViewProvider,
) {
  return async (): Promise<void> => {
    await createService.create();
    provider.refresh();
  };
}
