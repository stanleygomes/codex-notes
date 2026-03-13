import { Database as SqlJsDatabase } from 'sql.js';
import { Note } from '../dto/Note';
import { NoteColorEnum } from '../enum/NoteColorEnum';
import { DatabaseConnection } from './DatabaseConnection';

export abstract class BaseNoteRepository {
  private static readonly validColors = new Set<string>(Object.values(NoteColorEnum));

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
      createdAt: Number(row['createdAt']),
      updatedAt: Number(row['updatedAt']),
      isFavorite: Number(row['isFavorite']) === 1,
      color: this.normalizeColor(row['color']),
    };
  }

  private normalizeColor(value: unknown): NoteColorEnum {
    const normalizedColor = String(value ?? NoteColorEnum.NONE).toUpperCase();
    if (!BaseNoteRepository.validColors.has(normalizedColor)) {
      return NoteColorEnum.NONE;
    }
    return normalizedColor as NoteColorEnum;
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
