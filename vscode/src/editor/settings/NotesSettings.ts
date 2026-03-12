import * as vscode from 'vscode';
import * as os from 'os';
import * as path from 'path';

export class NotesSettings {
  private static readonly SECTION = 'codexNotes';

  static getFileExtension(): string {
    return vscode.workspace.getConfiguration(NotesSettings.SECTION).get<string>('fileExtension', 'md');
  }

  static getNotesDirectory(): string {
    const config = vscode.workspace.getConfiguration(NotesSettings.SECTION);
    const dir = config.get<string>('notesDirectory', '');
    if (dir) {
      return dir;
    }
    return path.join(os.homedir(), '.codex-notes');
  }

  static getExportDirectory(): string {
    return vscode.workspace.getConfiguration(NotesSettings.SECTION).get<string>('exportDirectory', '');
  }

  static getImportDirectory(): string {
    return vscode.workspace.getConfiguration(NotesSettings.SECTION).get<string>('importDirectory', '');
  }

  static isOpenFolderEnabled(): boolean {
    return vscode.workspace.getConfiguration(NotesSettings.SECTION).get<boolean>('openFolderEnabled', true);
  }
}
