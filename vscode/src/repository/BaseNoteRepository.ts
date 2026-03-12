import { BetterSQLite3Database } from 'drizzle-orm/better-sqlite3';
import { Note } from '../dto/Note';
import { NoteColorEnum } from '../enum/NoteColorEnum';
import { notesTable } from '../database/schema';
import { DatabaseConnection } from './DatabaseConnection';

export abstract class BaseNoteRepository {
  protected db: BetterSQLite3Database;

  constructor() {
    this.db = DatabaseConnection.getInstance().getDb();
  }

  protected rowToNote(row: typeof notesTable.$inferSelect): Note {
    return {
      id: row.id,
      title: row.title,
      filePath: row.filePath,
      createdAt: row.createdAt,
      updatedAt: row.updatedAt,
      isFavorite: row.isFavorite === 1,
      color: (row.color as NoteColorEnum) ?? NoteColorEnum.NONE,
    };
  }
}
