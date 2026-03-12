import initSqlJs, { Database as SqlJsDatabase } from 'sql.js';
import * as fs from 'fs';
import * as path from 'path';
import { FileHelper } from '../helper/FileHelper';

export class DatabaseConnection {
  private static instance: DatabaseConnection;
  private database: SqlJsDatabase;
  private readonly dbPath: string;

  private constructor(database: SqlJsDatabase, dbPath: string) {
    this.database = database;
    this.dbPath = dbPath;
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
}
