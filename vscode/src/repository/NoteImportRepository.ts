import { Note } from '../dto/Note';
import { BaseNoteRepository } from './BaseNoteRepository';
import { NoteQueryRepository } from './NoteQueryRepository';

export class NoteImportRepository extends BaseNoteRepository {
  private queryRepository: NoteQueryRepository;

  constructor() {
    super();
    this.queryRepository = new NoteQueryRepository();
  }

  importNote(note: Note): void {
    const existing = this.queryRepository.getNoteById(note.id);
    if (existing) {
      return;
    }
    this.db.run(
      'INSERT INTO notes (id, title, filePath, createdAt, updatedAt, isFavorite, color) VALUES (?, ?, ?, ?, ?, ?, ?)',
      [note.id, note.title, note.filePath, note.createdAt, note.updatedAt, note.isFavorite ? 1 : 0, note.color]
    );
    this.persist();
  }
}
