import { eq } from 'drizzle-orm';
import { Note } from '../dto/Note';
import { notesTable } from '../database/schema';
import { BaseNoteRepository } from './BaseNoteRepository';

export class NoteQueryRepository extends BaseNoteRepository {
  getAllNotes(): Note[] {
    const rows = this.db.select().from(notesTable).all();
    return rows.map((row) => this.rowToNote(row));
  }

  getNoteById(id: string): Note | undefined {
    const rows = this.db.select().from(notesTable).where(eq(notesTable.id, id)).all();
    return rows.length > 0 ? this.rowToNote(rows[0]) : undefined;
  }
}
