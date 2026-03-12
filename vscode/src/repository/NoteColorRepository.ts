import { NoteColorEnum } from '../enum/NoteColorEnum';
import { DateHelper } from '../helper/DateHelper';
import { BaseNoteRepository } from './BaseNoteRepository';

export class NoteColorRepository extends BaseNoteRepository {
  changeColor(id: string, color: NoteColorEnum): void {
    this.db.run(
      'UPDATE notes SET color = ?, updatedAt = ? WHERE id = ?',
      [color, DateHelper.nowMs(), id]
    );
    this.persist();
  }
}
