import * as vscode from 'vscode';
import { Container } from './container';
import { ViewProvider } from './infra/view/ViewProvider';
import { createHandleCreateFromSelection } from './infra/editor/view';

export function registerCommands(
  context: vscode.ExtensionContext,
  container: Container,
  provider: ViewProvider,
  handleCreate: () => void,
): void {
  context.subscriptions.push(
    vscode.commands.registerCommand('codexNotes.createNote', handleCreate),
    vscode.commands.registerCommand('codexNotes.createNoteFromSelection', () =>
      createHandleCreateFromSelection(container.createService, provider)(),
    ),
    vscode.commands.registerCommand('codexNotes.refreshNotes', () =>
      provider.refresh(),
    ),
    vscode.commands.registerCommand('codexNotes.openNotesFile', () =>
      container.openNotesFileService.open(),
    ),
    vscode.commands.registerCommand('codexNotes.searchNotes', () =>
      container.quickSearchService.show(),
    ),
  );
}
