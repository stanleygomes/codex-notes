import { eq } from 'drizzle-orm';
import { NoteColorEnum } from '../enum/NoteColorEnum';
import { DateHelper } from '../helper/DateHelper';
import { notesTable } from '../database/schema';
import { BaseNoteRepository } from './BaseNoteRepository';

export class NoteColorRepository extends BaseNoteRepository {
  changeColor(id: string, color: NoteColorEnum): void {
    this.db
      .update(notesTable)
      .set({ color, updatedAt: DateHelper.nowMs() })
      .where(eq(notesTable.id, id))
      .run();
  }
}
