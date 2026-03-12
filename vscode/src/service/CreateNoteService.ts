import * as vscode from 'vscode';
import { Note } from '../dto/Note';
import { NoteRepository } from '../repository/NoteRepository';
import { FileHelper } from '../helper/FileHelper';
import { NotesSettings } from '../editor/settings/NotesSettings';

export class CreateNoteService {
  private readonly repository: NoteRepository;

  constructor(repository: NoteRepository) {
    this.repository = repository;
  }

  async create(content?: string): Promise<Note | undefined> {
    const title = await this.generateUniqueTitle();
    if (!title) {
      return undefined;
    }
    return this.createWithTitle(title, content ?? '');
  }

  async createWithTitle(title: string, content: string = ''): Promise<Note> {
    const extension = NotesSettings.getFileExtension();
    const notesDir = NotesSettings.getNotesDirectory();
    const fileName = FileHelper.buildNoteFileName(title, extension);
    const filePath = FileHelper.createFileWithContent(notesDir, fileName, content);
    const note = this.repository.addNote(title, filePath);
    await this.openNote(filePath);
    return note;
  }

  private async generateUniqueTitle(): Promise<string | undefined> {
    const existing = this.repository.getAllNotes().map((n) => n.title);
    let index = 1;
    let title = 'Untitled';
    while (existing.includes(title)) {
      title = `Untitled ${index}`;
      index++;
    }
    return title;
  }

  private async openNote(filePath: string): Promise<void> {
    const uri = vscode.Uri.file(filePath);
    const doc = await vscode.workspace.openTextDocument(uri);
    await vscode.window.showTextDocument(doc);
  }
}
