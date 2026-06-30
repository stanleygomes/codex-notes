import * as vscode from 'vscode';
import { WebviewHelper } from '../../../core/helper/WebviewHelper';
import { FileHelper } from '../../../core/helper/FileHelper';
import { ImportNotesService } from '../../../core/service/ImportNotesService';
import { ExportNotesService } from '../../../core/service/ExportNotesService';

export function createHandleOpenBackup(
  extensionUri: vscode.Uri,
  importService: ImportNotesService,
  exportService: ExportNotesService,
  onRefresh: () => void,
) {
  let panel: vscode.WebviewPanel | undefined;

  return () => {
    if (panel) {
      panel.reveal(vscode.ViewColumn.One);
      return;
    }

    panel = vscode.window.createWebviewPanel(
      'codexNotes.backup',
      'Backup & Sync - Codex Notes',
      vscode.ViewColumn.One,
      {
        enableScripts: true,
        localResourceRoots: [extensionUri],
        retainContextWhenHidden: true,
      },
    );

    panel.webview.html = WebviewHelper.getHtml(
      panel.webview,
      extensionUri,
      'backup',
      {
        notesDir: FileHelper.getDefaultNotesDir(),
      },
    );

    panel.webview.onDidReceiveMessage(async (message) => {
      switch (message.command) {
        case 'import':
          await importService.import();
          onRefresh();
          break;
        case 'export':
          await exportService.exportAll();
          break;
      }
    });

    panel.onDidDispose(() => {
      panel = undefined;
    });
  };
}
