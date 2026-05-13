import { Note } from '../dto/Note';
import { BaseNoteRepository } from './BaseNoteRepository';

export class NoteQueryRepository extends BaseNoteRepository {
  getAllNotes(): Note[] {
    return this.queryAll('SELECT * FROM notes');
  }

  getNoteById(id: string): Note | undefined {
    const results = this.queryAll('SELECT * FROM notes WHERE id = ?', [id]);
    return results.length > 0 ? results[0] : undefined;
  }
}
