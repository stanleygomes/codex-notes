import Database from 'better-sqlite3';
import { drizzle, BetterSQLite3Database } from 'drizzle-orm/better-sqlite3';
import { migrate } from 'drizzle-orm/better-sqlite3/migrator';
import * as vscode from 'vscode';
import { FileHelper } from '../helper/FileHelper';
import * as path from 'path';

export class DatabaseConnection {
  private static instance: DatabaseConnection;
  private db: BetterSQLite3Database;
  private sqlite: Database.Database;

  private constructor() {
    FileHelper.ensureDirectoryExists(FileHelper.getDataDir());
    this.sqlite = new Database(FileHelper.getDatabasePath());
    this.db = drizzle(this.sqlite);
    this.runMigrations();
  }

  static getInstance(): DatabaseConnection {
    if (!DatabaseConnection.instance) {
      DatabaseConnection.instance = new DatabaseConnection();
    }
    return DatabaseConnection.instance;
  }

  getDb(): BetterSQLite3Database {
    return this.db;
  }

  getSqlite(): Database.Database {
    return this.sqlite;
  }

  private runMigrations(): void {
    try {
      console.log('🔄 Running database migrations...');
      const migrationsFolder = path.join(__dirname, 'database/migrations');
      migrate(this.db, { migrationsFolder });
      console.log('✅ Database migrations completed successfully');
    } catch (error) {
      console.error('❌ Migration error:', error);
      vscode.window.showErrorMessage(
        'Codex Notes: Failed to run database migrations. Please check the logs.'
      );
      throw error;
    }
  }
}
