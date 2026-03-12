import { Note } from '../dto/Note';
import { notesTable } from '../database/schema';
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
    this.db.insert(notesTable).values({
      id: note.id,
      title: note.title,
      filePath: note.filePath,
      createdAt: note.createdAt,
      updatedAt: note.updatedAt,
      isFavorite: note.isFavorite ? 1 : 0,
      color: note.color,
    }).run();
  }
}
