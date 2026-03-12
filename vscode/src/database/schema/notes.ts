import { sqliteTable, text, integer } from 'drizzle-orm/sqlite-core';

export const notesTable = sqliteTable('notes', {
  id: text('id').primaryKey(),
  title: text('title').notNull(),
  filePath: text('filePath').notNull(),
  createdAt: integer('createdAt').notNull(),
  updatedAt: integer('updatedAt').notNull(),
  isFavorite: integer('isFavorite').notNull().default(0),
  color: text('color').notNull().default('NONE'),
});

export type NoteRow = typeof notesTable.$inferSelect;
export type NoteInsert = typeof notesTable.$inferInsert;
