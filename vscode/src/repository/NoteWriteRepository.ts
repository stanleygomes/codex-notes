import { eq } from 'drizzle-orm';
import { Note } from '../dto/Note';
import { NoteColorEnum } from '../enum/NoteColorEnum';
import { DateHelper } from '../helper/DateHelper';
import { notesTable } from '../database/schema';
import { BaseNoteRepository } from './BaseNoteRepository';

export class NoteWriteRepository extends BaseNoteRepository {
  addNote(title: string, filePath: string): Note {
    const now = DateHelper.nowMs();
    const note: Note = {
      id: globalThis.crypto.randomUUID(),
      title,
      filePath,
      createdAt: now,
      updatedAt: now,
      isFavorite: false,
      color: NoteColorEnum.NONE,
    };

    this.db.insert(notesTable).values({
      id: note.id,
      title: note.title,
      filePath: note.filePath,
      createdAt: note.createdAt,
      updatedAt: note.updatedAt,
      isFavorite: 0,
      color: NoteColorEnum.NONE,
    }).run();

    return note;
  }

  updateNote(id: string, title?: string, filePath?: string): void {
    const updates: Partial<typeof notesTable.$inferInsert> = {
      updatedAt: DateHelper.nowMs(),
    };
    if (title !== undefined) {
      updates.title = title;
    }
    if (filePath !== undefined) {
      updates.filePath = filePath;
    }

    this.db.update(notesTable).set(updates).where(eq(notesTable.id, id)).run();
  }

  removeNote(id: string): void {
    this.db.delete(notesTable).where(eq(notesTable.id, id)).run();
  }
}
