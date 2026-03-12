import * as vscode from 'vscode';
import { CreateNoteService } from '../../service/CreateNoteService';
import { NotesViewProvider } from '../../ui/NotesViewProvider';

export function createHandleCreateFromSelection(
  createService: CreateNoteService,
  provider: NotesViewProvider
) {
  return async (): Promise<void> => {
    const editor = vscode.window.activeTextEditor;
    const selection = editor?.document.getText(editor.selection) ?? '';
    await createService.create(selection);
    provider.refresh();
  };
}
