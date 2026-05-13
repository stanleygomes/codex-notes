import * as vscode from 'vscode';
import * as os from 'os';
import * as path from 'path';

export class NotesSettings {
  private static readonly SECTION = 'codexNotes';

  static getFileExtension(): string {
    return vscode.workspace.getConfiguration(NotesSettings.SECTION).get<string>('fileExtension', 'md');
  }

  static getNotesDirectory(): string {
    return path.join(os.homedir(), '.codex-notes');
  }

  static isOpenFolderEnabled(): boolean {
    return vscode.workspace.getConfiguration(NotesSettings.SECTION).get<boolean>('openFolderEnabled', true);
  }
}
