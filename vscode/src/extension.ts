import * as vscode from 'vscode';
import { NoteRepository } from './repository/NoteRepository';
import { CreateNoteService } from './service/CreateNoteService';
import { DeleteNoteService } from './service/DeleteNoteService';
import { RenameNoteService } from './service/RenameNoteService';
import { SearchNoteService } from './service/SearchNoteService';
import { DuplicateNoteService } from './service/DuplicateNoteService';
import { ExportNotesService } from './service/ExportNotesService';
import { ImportNotesService } from './service/ImportNotesService';
import { FavoriteNoteService } from './service/FavoriteNoteService';
import { ChangeNoteColorService } from './service/ChangeNoteColorService';
import { SortNotesService } from './service/SortNotesService';
import { FilterNotesService } from './service/FilterNotesService';
import { OpenNoteLocationService } from './service/OpenNoteLocationService';
import { NotesViewProvider } from './ui/NotesViewProvider';
import { Note } from './dto/Note';

export function activate(context: vscode.ExtensionContext): void {
  const repository = NoteRepository.getInstance();

  const createService = new CreateNoteService(repository);
  const deleteService = new DeleteNoteService(repository);
  const renameService = new RenameNoteService(repository);
  const searchService = new SearchNoteService(repository);
  const duplicateService = new DuplicateNoteService(repository, createService);
  const exportService = new ExportNotesService(repository);
  const importService = new ImportNotesService(repository, createService);
  const favoriteService = new FavoriteNoteService(repository);
  const colorService = new ChangeNoteColorService(repository);
  const sortService = new SortNotesService();
  const filterService = new FilterNotesService();
  const locationService = new OpenNoteLocationService();

  const provider = new NotesViewProvider(
    context.extensionUri,
    repository,
    searchService,
    sortService,
    filterService,
    (note: Note) => openNote(note),
    () => handleCreate(),
    (note: Note) => handleDelete(note),
    (note: Note) => handleRename(note),
    (note: Note) => handleDuplicate(note),
    (note: Note) => { favoriteService.toggleFavorite(note); provider.refresh(); },
    (note: Note) => handleChangeColor(note),
    () => exportService.exportAll(),
    () => handleImport(),
    (note: Note) => locationService.openLocation(note),
  );

  context.subscriptions.push(
    vscode.window.registerWebviewViewProvider(NotesViewProvider.viewType, provider),
  );

  async function openNote(note: Note): Promise<void> {
    const uri = vscode.Uri.file(note.filePath);
    const doc = await vscode.workspace.openTextDocument(uri);
    await vscode.window.showTextDocument(doc);
  }

  async function handleCreate(): Promise<void> {
    await createService.create();
    provider.refresh();
  }

  async function handleDelete(note: Note): Promise<void> {
    const deleted = await deleteService.confirmAndDelete([note]);
    if (deleted) {
      provider.refresh();
    }
  }

  async function handleRename(note: Note): Promise<void> {
    const renamed = await renameService.rename(note);
    if (renamed) {
      provider.refresh();
    }
  }

  async function handleDuplicate(note: Note): Promise<void> {
    await duplicateService.duplicate(note);
    provider.refresh();
  }

  async function handleChangeColor(note: Note): Promise<void> {
    await colorService.changeColor(note);
    provider.refresh();
  }

  async function handleImport(): Promise<void> {
    await importService.import();
    provider.refresh();
  }

  context.subscriptions.push(
    vscode.commands.registerCommand('codexNotes.createNote', () => handleCreate()),
    vscode.commands.registerCommand('codexNotes.createNoteFromSelection', async () => {
      const editor = vscode.window.activeTextEditor;
      const selection = editor?.document.getText(editor.selection) ?? '';
      await createService.create(selection);
      provider.refresh();
    }),
    vscode.commands.registerCommand('codexNotes.refreshNotes', () => provider.refresh()),
    vscode.commands.registerCommand('codexNotes.exportNotes', () => exportService.exportAll()),
    vscode.commands.registerCommand('codexNotes.importNotes', () => handleImport()),
  );
}

export function deactivate(): void {}
