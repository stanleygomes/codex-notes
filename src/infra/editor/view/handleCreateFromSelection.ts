import * as vscode from 'vscode';
import { CreateNoteService } from '../../../core/services/CreateNoteService';
import { ViewProvider } from '../../view/ViewProvider';

export function createHandleCreateFromSelection(
  createService: CreateNoteService,
  provider: ViewProvider,
) {
  return async (): Promise<void> => {
    const editor = vscode.window.activeTextEditor;
    const selection = editor?.document.getText(editor.selection) ?? '';
    await createService.create(selection);
    provider.refresh();
  };
}
