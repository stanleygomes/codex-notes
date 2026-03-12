import { Note } from '../dto/Note';
import { NoteColorEnum } from '../enum/NoteColorEnum';
import { NoteQueryRepository } from './NoteQueryRepository';
import { NoteWriteRepository } from './NoteWriteRepository';
import { NoteFavoriteRepository } from './NoteFavoriteRepository';
import { NoteColorRepository } from './NoteColorRepository';
import { NoteImportRepository } from './NoteImportRepository';
import { DatabaseConnection } from './DatabaseConnection';

export class NoteRepository {
  private static instance: NoteRepository;
  private queryRepository: NoteQueryRepository;
  private writeRepository: NoteWriteRepository;
  private favoriteRepository: NoteFavoriteRepository;
  private colorRepository: NoteColorRepository;
  private importRepository: NoteImportRepository;

  private constructor() {
    this.queryRepository = new NoteQueryRepository();
    this.writeRepository = new NoteWriteRepository();
    this.favoriteRepository = new NoteFavoriteRepository();
    this.colorRepository = new NoteColorRepository();
    this.importRepository = new NoteImportRepository();
  }

  static async initialize(): Promise<NoteRepository> {
    if (!NoteRepository.instance) {
      await DatabaseConnection.initialize();
      NoteRepository.instance = new NoteRepository();
    }
    return NoteRepository.instance;
  }

  static getInstance(): NoteRepository {
    if (!NoteRepository.instance) {
      throw new Error('NoteRepository not initialized. Call initialize() first.');
    }
    return NoteRepository.instance;
  }

  addNote(title: string, filePath: string): Note {
    return this.writeRepository.addNote(title, filePath);
  }

  updateNote(id: string, title?: string, filePath?: string): void {
    this.writeRepository.updateNote(id, title, filePath);
  }

  toggleFavorite(id: string): void {
    this.favoriteRepository.toggleFavorite(id);
  }

  changeColor(id: string, color: NoteColorEnum): void {
    this.colorRepository.changeColor(id, color);
  }

  removeNote(id: string): void {
    this.writeRepository.removeNote(id);
  }

  getAllNotes(): Note[] {
    return this.queryRepository.getAllNotes();
  }

  getNoteById(id: string): Note | undefined {
    return this.queryRepository.getNoteById(id);
  }

  importNote(note: Note): void {
    this.importRepository.importNote(note);
  }
}
