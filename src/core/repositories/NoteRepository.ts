import * as fs from 'fs';
import * as path from 'path';
import { Note } from '../dtos/Note';
import { FileHelper } from '../helpers/FileHelper';
import { NotesJsonMetadata } from '../dtos/NotesJsonMetadata';
import { NotesSettings } from '../../infra/editor/settings/NotesSettings';
import { NoteColorEnum } from '../enums/NoteColorEnum';

export class NoteRepository {
  private static instance: NoteRepository;
  private notes: Note[] = [];
  private readonly notesJsonPath: string;

  private constructor() {
    const notesDir = FileHelper.getDefaultNotesDir();
    this.notesJsonPath = path.join(notesDir, 'notes.json');
  }

  static async initialize(): Promise<NoteRepository> {
    if (!NoteRepository.instance) {
      const repository = new NoteRepository();
      await repository.load();
      NoteRepository.instance = repository;
    }
    return NoteRepository.instance;
  }

  static getInstance(): NoteRepository {
    if (!NoteRepository.instance) {
      throw new Error(
        'NoteRepository not initialized. Call initialize() first.',
      );
    }
    return NoteRepository.instance;
  }

  private async load(): Promise<void> {
    const notesDir = FileHelper.getDefaultNotesDir();
    FileHelper.ensureDirectoryExists(notesDir);

    if (!fs.existsSync(this.notesJsonPath)) {
      this.notes = [];
      this.runMigrationFromFiles();
      this.persist();
      return;
    }

    try {
      const dataStr = fs.readFileSync(this.notesJsonPath, 'utf8');
      const data = JSON.parse(dataStr) as NotesJsonMetadata;
      this.notes = data.notes || [];
    } catch (error) {
      console.error(
        'Error loading notes.json, initializing empty storage',
        error,
      );
      this.notes = [];
    }
  }

  private runMigrationFromFiles(): void {
    const notesDir = FileHelper.getDefaultNotesDir();
    const extension = NotesSettings.getFileExtension();
    const filePaths = FileHelper.listFiles(notesDir, extension);

    for (const filePath of filePaths) {
      const exists = this.notes.some((n) => n.filePath === filePath);
      if (!exists) {
        try {
          const stats = fs.statSync(filePath);
          const fileName = path.basename(filePath);
          const ext = path.extname(filePath);
          const title = fileName.slice(0, -ext.length);

          const note: Note = {
            id: globalThis.crypto.randomUUID(),
            title,
            filePath,
            createdAt: stats.birthtimeMs || stats.mtimeMs,
            updatedAt: stats.mtimeMs,
            isFavorite: false,
            color: NoteColorEnum.NONE,
          };
          this.notes.push(note);
        } catch (error) {
          console.error(`Failed to migrate file ${filePath}`, error);
        }
      }
    }
  }

  private persist(): void {
    const data: NotesJsonMetadata = {
      description: `Codex Notes Metadata file. You can copy this file and your notes to another machine. Your notes are stored in the same folder: ${FileHelper.getDefaultNotesDir()}`,
      notes: this.notes,
    };
    try {
      fs.writeFileSync(
        this.notesJsonPath,
        JSON.stringify(data, null, 2),
        'utf8',
      );
    } catch (error) {
      console.error('Error persisting notes.json', error);
    }
  }

  findAll(): Note[] {
    return this.notes;
  }

  findOne(id: string): Note | undefined {
    return this.notes.find((note) => note.id === id);
  }

  save(note: Note): void {
    const index = this.notes.findIndex((n) => n.id === note.id);
    if (index >= 0) {
      this.notes[index] = note;
    } else {
      this.notes.push(note);
    }
    this.persist();
  }

  delete(id: string): void {
    this.notes = this.notes.filter((note) => note.id !== id);
    this.persist();
  }

  getNotesJsonPath(): string {
    return this.notesJsonPath;
  }
}
