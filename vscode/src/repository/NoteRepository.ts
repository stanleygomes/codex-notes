import Database from 'better-sqlite3';
import { drizzle, BetterSQLite3Database } from 'drizzle-orm/better-sqlite3';
import { sqliteTable, text, integer } from 'drizzle-orm/sqlite-core';
import { eq } from 'drizzle-orm';
import { Note } from '../dto/Note';
import { NoteColorEnum } from '../enum/NoteColorEnum';
import { FileHelper } from '../helper/FileHelper';
import { DateHelper } from '../helper/DateHelper';

const notesTable = sqliteTable('notes', {
  id: text('id').primaryKey(),
  title: text('title').notNull(),
  filePath: text('filePath').notNull(),
  createdAt: integer('createdAt').notNull(),
  updatedAt: integer('updatedAt').notNull(),
  isFavorite: integer('isFavorite').notNull().default(0),
  color: text('color').notNull().default(NoteColorEnum.NONE),
});

export class NoteRepository {
  private static instance: NoteRepository;
  private db: BetterSQLite3Database;

  private constructor() {
    FileHelper.ensureDirectoryExists(FileHelper.getDataDir());
    const sqlite = new Database(FileHelper.getDatabasePath());
    this.db = drizzle(sqlite);
    this.migrate(sqlite);
  }

  static getInstance(): NoteRepository {
    if (!NoteRepository.instance) {
      NoteRepository.instance = new NoteRepository();
    }
    return NoteRepository.instance;
  }

  private migrate(sqlite: Database.Database): void {
    sqlite.exec(`
      CREATE TABLE IF NOT EXISTS notes (
        id TEXT PRIMARY KEY,
        title TEXT NOT NULL,
        filePath TEXT NOT NULL,
        createdAt INTEGER NOT NULL,
        updatedAt INTEGER NOT NULL,
        isFavorite INTEGER NOT NULL DEFAULT 0,
        color TEXT NOT NULL DEFAULT 'NONE'
      )
    `);
  }

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

  toggleFavorite(id: string): void {
    const note = this.getNoteById(id);
    if (!note) {
      return;
    }
    this.db
      .update(notesTable)
      .set({ isFavorite: note.isFavorite ? 0 : 1, updatedAt: DateHelper.nowMs() })
      .where(eq(notesTable.id, id))
      .run();
  }

  changeColor(id: string, color: NoteColorEnum): void {
    this.db
      .update(notesTable)
      .set({ color, updatedAt: DateHelper.nowMs() })
      .where(eq(notesTable.id, id))
      .run();
  }

  removeNote(id: string): void {
    this.db.delete(notesTable).where(eq(notesTable.id, id)).run();
  }

  getAllNotes(): Note[] {
    const rows = this.db.select().from(notesTable).all();
    return rows.map((row) => this.rowToNote(row));
  }

  getNoteById(id: string): Note | undefined {
    const rows = this.db.select().from(notesTable).where(eq(notesTable.id, id)).all();
    return rows.length > 0 ? this.rowToNote(rows[0]) : undefined;
  }

  importNote(note: Note): void {
    const existing = this.getNoteById(note.id);
    if (existing) {
      return;
    }
    this.db.insert(notesTable).values({
      id: note.id,
      title: note.title,
      filePath: note.filePath,
      createdAt: note.createdAt,
      updatedAt: note.updatedAt,
      isFavorite: note.isFavorite ? 1 : 0,
      color: note.color,
    }).run();
  }

  private rowToNote(row: typeof notesTable.$inferSelect): Note {
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
