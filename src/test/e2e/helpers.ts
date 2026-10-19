import * as assert from 'assert';
import * as fs from 'fs';
import * as path from 'path';
import * as vscode from 'vscode';
import type { Container } from '../../container';
import type { Note } from '../../core/dtos/Note';
import { FileHelper } from '../../core/helpers/FileHelper';

export const EXTENSION_ID = 'stanleygomes.codex-notes';

export async function activateExtension(): Promise<Container> {
  const extension = vscode.extensions.getExtension<Container>(EXTENSION_ID);
  assert.ok(extension, 'Extension should be present');
  return extension.activate();
}

export function sandboxedNotesDir(): string {
  const home = process.env.CODEX_NOTES_E2E_HOME;
  const notesDir = FileHelper.getDefaultNotesDir();
  assert.ok(
    home && notesDir.startsWith(home),
    `Refusing to run: notes dir ${notesDir} is outside the e2e sandbox`,
  );
  return notesDir;
}

export async function resetNotes(container: Container): Promise<void> {
  sandboxedNotesDir();
  await vscode.commands.executeCommand('workbench.action.closeAllEditors');
  for (const note of [...container.repository.findAll()]) {
    FileHelper.deleteFile(note.filePath);
    container.repository.delete(note.id);
  }
}

export function readNotesJson(container: Container): Note[] {
  sandboxedNotesDir();
  const content = fs.readFileSync(
    container.repository.getNotesJsonPath(),
    'utf8',
  );
  return JSON.parse(content).notes;
}

export function notePath(fileName: string): string {
  return path.join(sandboxedNotesDir(), fileName);
}

export function activeEditorPath(): string | undefined {
  return vscode.window.activeTextEditor?.document.uri.fsPath;
}

export async function waitFor(
  condition: () => boolean,
  timeoutMs = 5000,
): Promise<void> {
  const start = Date.now();
  while (!condition()) {
    if (Date.now() - start > timeoutMs) {
      throw new Error('Timed out waiting for condition');
    }
    await new Promise((resolve) => setTimeout(resolve, 50));
  }
}
