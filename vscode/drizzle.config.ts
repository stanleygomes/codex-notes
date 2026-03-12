import { defineConfig } from 'drizzle-kit';
import * as path from 'path';
import * as os from 'os';

const dataDir = process.env.CODEX_NOTES_DATA_DIR || path.join(os.homedir(), '.codex-notes');

export default defineConfig({
  schema: './src/database/schema',
  out: './src/database/migrations',
  dialect: 'sqlite',
  dbCredentials: {
    url: path.join(dataDir, 'notes.db'),
  },
});
