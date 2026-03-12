import * as vscode from 'vscode';
import { Note } from '../dto/Note';
import { FileHelper } from '../helper/FileHelper';

export class OpenNoteLocationService {
  openLocation(note: Note): void {
    const dir = FileHelper.getParentPath(note.filePath);
    vscode.commands.executeCommand('revealFileInOS', vscode.Uri.file(dir));
  }
}
