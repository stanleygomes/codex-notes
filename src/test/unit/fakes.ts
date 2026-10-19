import * as fs from 'node:fs';
import * as os from 'node:os';
import * as path from 'node:path';
import { vi } from 'vitest';
import type { ExtensionContext } from 'vscode';
import type { Note } from '../../core/dtos/Note';
import { NoteColorEnum } from '../../core/enums/NoteColorEnum';
import { NoteRepository } from '../../core/repositories/NoteRepository';
import type { UserInteraction } from '../../infra/editor/UserInteraction';
import type { ViewProvider } from '../../infra/view/ViewProvider';
import { Uri } from './mocks/vscode';

export function notesDir(): string {
  return path.join(os.homedir(), '.codex-notes');
}

export function notesJsonPath(): string {
  return path.join(notesDir(), 'notes.json');
}

export function readNotesJson(): { description: string; notes: Note[] } {
  return JSON.parse(fs.readFileSync(notesJsonPath(), 'utf8'));
}

export function writeNoteFile(fileName: string, content = ''): string {
  fs.mkdirSync(notesDir(), { recursive: true });
  const filePath = path.join(notesDir(), fileName);
  fs.writeFileSync(filePath, content);
  return filePath;
}

export function buildNote(overrides: Partial<Note> = {}): Note {
  return {
    id: 'note-1',
    title: 'Note One',
    filePath: path.join(notesDir(), 'note-one.md'),
    createdAt: 1_000,
    updatedAt: 1_000,
    isFavorite: false,
    color: NoteColorEnum.NONE,
    ...overrides,
  };
}

export async function buildRepository(
  initial: Note[] = [],
): Promise<NoteRepository> {
  if (!fs.existsSync(notesJsonPath())) {
    fs.mkdirSync(notesDir(), { recursive: true });
    fs.writeFileSync(notesJsonPath(), JSON.stringify({ notes: [] }));
  }
  const repository = await NoteRepository.initialize();
  initial.forEach((note) => repository.save(note));
  return repository;
}

export function buildUserInteraction() {
  const fake = {
    showInputBox: vi.fn(),
    showConfirmation: vi.fn(),
    showQuickPick: vi.fn(),
    showError: vi.fn(),
    showInfo: vi.fn(),
    executeCommand: vi.fn(),
    showOpenDialog: vi.fn(),
    showSaveDialog: vi.fn(),
    openTextDocument: vi.fn(),
    openFolder: vi.fn(),
    revealFileInOS: vi.fn(),
    reopenNote: vi.fn(),
    showQuickSearch: vi.fn(),
  };
  return { fake, userInteraction: fake as unknown as UserInteraction };
}

export function buildProvider() {
  const fake = { refresh: vi.fn() };
  return { fake, provider: fake as unknown as ViewProvider };
}

export function buildExtensionContext() {
  const context = {
    subscriptions: [] as { dispose(): void }[],
    extensionUri: Uri.file(process.cwd()),
  };
  return { context, extensionContext: context as unknown as ExtensionContext };
}
