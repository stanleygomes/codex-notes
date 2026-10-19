import { defineConfig } from '@vscode/test-cli';
import { mkdirSync, mkdtempSync, rmSync, writeFileSync } from 'node:fs';
import { tmpdir } from 'node:os';
import { join } from 'node:path';

const sandbox = mkdtempSync(join(tmpdir(), 'codex-notes-e2e-'));
const home = join(sandbox, 'home');
const workspaceFolder = join(sandbox, 'sample-project');

mkdirSync(home);
mkdirSync(workspaceFolder);
writeFileSync(join(workspaceFolder, 'README.md'), '# Sample project\n');
process.on('exit', () => rmSync(sandbox, { recursive: true, force: true }));

export default defineConfig({
  files: 'out/test/e2e/**/*.test.js',
  workspaceFolder,
  env: {
    HOME: home,
    USERPROFILE: home,
    CODEX_NOTES_E2E_HOME: home,
  },
  mocha: {
    ui: 'tdd',
    timeout: 60000,
  },
  launchArgs: [
    '--disable-extensions',
    '--disable-workspace-trust',
    '--skip-welcome',
    '--skip-release-notes',
  ],
});
