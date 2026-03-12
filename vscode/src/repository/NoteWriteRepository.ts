import { Note } from '../dto/Note';
import { NoteColorEnum } from '../enum/NoteColorEnum';
import { DateHelper } from '../helper/DateHelper';
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

    this.db.run(
      'INSERT INTO notes (id, title, filePath, createdAt, updatedAt, isFavorite, color) VALUES (?, ?, ?, ?, ?, ?, ?)',
      [note.id, note.title, note.filePath, note.createdAt, note.updatedAt, note.isFavorite ? 1 : 0, note.color]
    );
    this.persist();

    return note;
  }

  updateNote(id: string, title?: string, filePath?: string): void {
    const sets: string[] = ['updatedAt = ?'];
    const params: unknown[] = [DateHelper.nowMs()];

    if (title !== undefined) {
      sets.push('title = ?');
      params.push(title);
    }
    if (filePath !== undefined) {
      sets.push('filePath = ?');
      params.push(filePath);
    }

    params.push(id);
    this.db.run(`UPDATE notes SET ${sets.join(', ')} WHERE id = ?`, params);
    this.persist();
  }

  removeNote(id: string): void {
    this.db.run('DELETE FROM notes WHERE id = ?', [id]);
    this.persist();
  }
}
