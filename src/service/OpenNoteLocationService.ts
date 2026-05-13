import * as vscode from 'vscode';
import { Note } from '../dto/Note';

export class OpenNoteLocationService {
  openLocation(note: Note): void {
    vscode.commands.executeCommand('revealFileInOS', vscode.Uri.file(note.filePath));
  }
}