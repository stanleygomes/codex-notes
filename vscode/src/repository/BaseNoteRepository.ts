import { Database as SqlJsDatabase } from 'sql.js';
import { Note } from '../dto/Note';
import { NoteColorEnum } from '../enum/NoteColorEnum';
import { DatabaseConnection } from './DatabaseConnection';

export abstract class BaseNoteRepository {
  protected get db(): SqlJsDatabase {
    return DatabaseConnection.getInstance().getDatabase();
  }

  protected persist(): void {
    DatabaseConnection.getInstance().persist();
  }

  protected rowToNote(row: Record<string, unknown>): Note {
    return {
      id: row['id'] as string,
      title: row['title'] as string,
      filePath: row['filePath'] as string,
      createdAt: row['createdAt'] as number,
      updatedAt: row['updatedAt'] as number,
      isFavorite: (row['isFavorite'] as number) === 1,
      color: (row['color'] as NoteColorEnum) ?? NoteColorEnum.NONE,
    };
  }

  protected queryAll(sql: string, params: unknown[] = []): Note[] {
    const stmt = this.db.prepare(sql);
    try {
      stmt.bind(params);
      const results: Note[] = [];
      while (stmt.step()) {
        const row = stmt.getAsObject();
        results.push(this.rowToNote(row));
      }
      return results;
    } finally {
      stmt.free();
    }
  }
}
