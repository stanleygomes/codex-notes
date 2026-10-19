import { mkdtempSync, rmSync } from 'node:fs';
import { tmpdir } from 'node:os';
import { join } from 'node:path';
import { afterEach, beforeEach, vi } from 'vitest';
import { NoteRepository } from '../../core/repositories/NoteRepository';
import { resetVscodeMock } from './mocks/vscode';

const originalHome = process.env.HOME;
let sandbox: string;

beforeEach(() => {
  sandbox = mkdtempSync(join(tmpdir(), 'codex-notes-unit-'));
  process.env.HOME = sandbox;
  (NoteRepository as unknown as { instance?: NoteRepository }).instance =
    undefined;
  resetVscodeMock();
});

afterEach(() => {
  vi.restoreAllMocks();
  process.env.HOME = originalHome;
  rmSync(sandbox, { recursive: true, force: true });
});
