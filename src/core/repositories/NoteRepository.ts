import * as fs from 'fs';
import * as path from 'path';
import { Note } from '../dtos/Note';
import { FileHelper } from '../helpers/FileHelper';
import { NotesJsonMetadata } from '../dtos/NotesJsonMetadata';

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
}
