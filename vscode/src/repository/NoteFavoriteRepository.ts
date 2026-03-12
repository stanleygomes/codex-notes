import { DateHelper } from '../helper/DateHelper';
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
    this.db.run(
      'UPDATE notes SET isFavorite = ?, updatedAt = ? WHERE id = ?',
      [note.isFavorite ? 0 : 1, DateHelper.nowMs(), id]
    );
    this.persist();
  }
}
