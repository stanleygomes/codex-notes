import { eq } from 'drizzle-orm';
import { DateHelper } from '../helper/DateHelper';
import { notesTable } from '../database/schema';
import { BaseNoteRepository } from './BaseNoteRepository';
import { NoteQueryRepository } from './NoteQueryRepository';

export class NoteFavoriteRepository extends BaseNoteRepository {
  private queryRepository: NoteQueryRepository;

  constructor() {
    super();
    this.queryRepository = new NoteQueryRepository();
  }

  toggleFavorite(id: string): void {
    const note = this.queryRepository.getNoteById(id);
    if (!note) {
      return;
    }
    this.db
      .update(notesTable)
      .set({ isFavorite: note.isFavorite ? 0 : 1, updatedAt: DateHelper.nowMs() })
      .where(eq(notesTable.id, id))
      .run();
  }
}
