import * as vscode from 'vscode';
import { DatabaseConnection } from './DatabaseConnection';

interface Migration {
  version: number;
  name: string;
  up: (database: any) => void;
  down: (database: any) => void;
}

export class DatabaseMigration {
  private static migrations: Migration[] = [
    {
      version: 1,
      name: 'create_notes_table',
      up: (database: any) => {
        database.run(`
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
      },
      down: (database: any) => {
        database.run('DROP TABLE IF EXISTS notes');
      },
    },
  ];

  static run(): void {
    try {
      const connection = DatabaseConnection.getInstance();
      const database = connection.getDatabase();

      // Cria tabela de controle de migrations
      database.run(`
        CREATE TABLE IF NOT EXISTS migrations (
          version INTEGER PRIMARY KEY NOT NULL,
          name TEXT NOT NULL,
          executed_at INTEGER NOT NULL
        )
      `);

      // Busca migrations já executadas
      const executedMigrations = new Set<number>();
      const result = database.exec('SELECT version FROM migrations');
      if (result.length > 0 && result[0].values.length > 0) {
        result[0].values.forEach((row: any) => {
          executedMigrations.add(row[0] as number);
        });
      }

      // Executa migrations pendentes
      const pendingMigrations = this.migrations.filter(
        m => !executedMigrations.has(m.version)
      );

      if (pendingMigrations.length > 0) {
        console.log(`Running ${pendingMigrations.length} pending migration(s)...`);
        
        pendingMigrations.forEach(migration => {
          console.log(`Running migration ${migration.version}: ${migration.name}`);
          migration.up(database);
          
          database.run(
            'INSERT INTO migrations (version, name, executed_at) VALUES (?, ?, ?)',
            [migration.version, migration.name, Date.now()]
          );
        });

        connection.persist();
        console.log('Migrations completed successfully');
      } else {
        console.log('No pending migrations');
      }
    } catch (error) {
      console.error('Database migration error:', error);
      vscode.window.showErrorMessage(
        'Codex Notes: Failed to run database migrations. Please check the logs.'
      );
      throw error;
    }
  }
}
