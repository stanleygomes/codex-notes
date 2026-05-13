import * as vscode from 'vscode';
import * as path from 'path';
import { Note } from '../dto/Note';
import { NoteRepository } from '../repository/NoteRepository';
import { FileHelper } from '../helper/FileHelper';
import { DialogHelper } from '../helper/DialogHelper';
import { NotesSettings } from '../editor/settings/NotesSettings';

export class RenameNoteService {
  private readonly repository: NoteRepository;

  constructor(repository: NoteRepository) {
    this.repository = repository;
  }

  async rename(note: Note): Promise<Note | undefined> {
    const newTitle = await DialogHelper.showInputBox({
      prompt: 'Enter new note title',
      value: note.title,
      validateInput: (value) => {
        if (!value.trim()) {
          return 'Title cannot be empty';
        }
        return undefined;
      },
    });

    if (!newTitle || newTitle.trim() === note.title) {
      return undefined;
    }

    const title = newTitle.trim();
    const extension = NotesSettings.getFileExtension();
    const dir = FileHelper.getParentPath(note.filePath);
    const newFileName = FileHelper.buildNoteFileName(title, extension);

    if (FileHelper.fileExists(dir, newFileName)) {
      DialogHelper.showError(`A note with the filename "${newFileName}" already exists.`);
      return undefined;
    }

    const newFilePath = FileHelper.renameFile(note.filePath, newFileName);
    this.repository.updateNote(note.id, title, newFilePath);

    await this.reopenNote(note.filePath, newFilePath);

    return { ...note, title, filePath: newFilePath };
  }

  private async reopenNote(oldPath: string, newPath: string): Promise<void> {
    const oldUri = vscode.Uri.file(oldPath);
    for (const editor of vscode.window.visibleTextEditors) {
      if (editor.document.uri.fsPath === oldUri.fsPath) {
        await vscode.commands.executeCommand('workbench.action.closeActiveEditor');
        break;
      }
    }
    const newUri = vscode.Uri.file(newPath);
    const ext = path.extname(newPath).toLowerCase();
    if (ext === '.md' || ext === '.txt') {
      const doc = await vscode.workspace.openTextDocument(newUri);
      await vscode.window.showTextDocument(doc);
    }
  }
}
