import initSqlJs, { Database as SqlJsDatabase } from 'sql.js';
import * as fs from 'fs';
import * as path from 'path';
import * as vscode from 'vscode';
import { FileHelper } from '../helper/FileHelper';

export class DatabaseConnection {
  private static instance: DatabaseConnection;
  private database: SqlJsDatabase;
  private readonly dbPath: string;

  private constructor(database: SqlJsDatabase, dbPath: string) {
    this.database = database;
    this.dbPath = dbPath;
    this.runMigrations();
  }

  static async initialize(): Promise<DatabaseConnection> {
    if (!DatabaseConnection.instance) {
      FileHelper.ensureDirectoryExists(FileHelper.getDataDir());
      const dbPath = FileHelper.getDatabasePath();

      const wasmBinary = fs.readFileSync(
        path.join(__dirname, 'sql-wasm.wasm')
      );
      const SQL = await initSqlJs({ wasmBinary });

      let database: SqlJsDatabase;
      if (fs.existsSync(dbPath)) {
        const buffer = fs.readFileSync(dbPath);
        database = new SQL.Database(buffer);
      } else {
        database = new SQL.Database();
      }

      DatabaseConnection.instance = new DatabaseConnection(database, dbPath);
    }
    return DatabaseConnection.instance;
  }

  static getInstance(): DatabaseConnection {
    if (!DatabaseConnection.instance) {
      throw new Error('DatabaseConnection not initialized. Call initialize() first.');
    }
    return DatabaseConnection.instance;
  }

  getDatabase(): SqlJsDatabase {
    return this.database;
  }

  persist(): void {
    const data = this.database.export();
    const buffer = Buffer.from(data);
    fs.writeFileSync(this.dbPath, buffer);
  }

  private runMigrations(): void {
    try {
      this.database.run(`
        CREATE TABLE IF NOT EXISTS notes (
          id TEXT PRIMARY KEY NOT NULL,
          title TEXT NOT NULL,
          filePath TEXT NOT NULL,
          createdAt INTEGER NOT NULL,
          updatedAt INTEGER NOT NULL,
          isFavorite INTEGER NOT NULL DEFAULT 0,
          color TEXT NOT NULL DEFAULT 'NONE'
        )
      `);
      this.persist();
    } catch (error) {
      console.error('Database migration error:', error);
      vscode.window.showErrorMessage(
        'Codex Notes: Failed to run database migrations. Please check the logs.'
      );
      throw error;
    }
  }
}
