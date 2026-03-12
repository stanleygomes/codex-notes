import * as vscode from 'vscode';
import { Note } from '../../dto/Note';

export async function openNote(note: Note): Promise<void> {
  const uri = vscode.Uri.file(note.filePath);
  const doc = await vscode.workspace.openTextDocument(uri);
  await vscode.window.showTextDocument(doc);
}
