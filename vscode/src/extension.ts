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
import {
  openNote,
  createHandleCreate,
  createHandleDelete,
  createHandleRename,
  createHandleDuplicate,
  createHandleChangeColor,
  createHandleImport,
  createHandleCreateFromSelection,
} from './editor/view';

export function activate(context: vscode.ExtensionContext): void {
  // Initialize repository and run database migrations automatically on startup
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

  let provider: NotesViewProvider;

  const handleCreate = () => createHandleCreate(createService, provider)();
  const handleDelete = (note: Note) => createHandleDelete(deleteService, provider)(note);
  const handleRename = (note: Note) => createHandleRename(renameService, provider)(note);
  const handleDuplicate = (note: Note) => createHandleDuplicate(duplicateService, provider)(note);
  const handleChangeColor = (note: Note) => createHandleChangeColor(colorService, provider)(note);
  const handleImport = () => createHandleImport(importService, provider)();

  provider = new NotesViewProvider(
    context.extensionUri,
    repository,
    searchService,
    sortService,
    filterService,
    openNote,
    handleCreate,
    handleDelete,
    handleRename,
    handleDuplicate,
    (note) => { favoriteService.toggleFavorite(note); provider.refresh(); },
    handleChangeColor,
    () => exportService.exportAll(),
    handleImport,
    (note) => locationService.openLocation(note),
  );

  context.subscriptions.push(
    vscode.window.registerWebviewViewProvider(NotesViewProvider.viewType, provider),
  );

  context.subscriptions.push(
    vscode.commands.registerCommand('codexNotes.createNote', handleCreate),
    vscode.commands.registerCommand('codexNotes.createNoteFromSelection', () => createHandleCreateFromSelection(createService, provider)()),
    vscode.commands.registerCommand('codexNotes.refreshNotes', () => provider.refresh()),
    vscode.commands.registerCommand('codexNotes.exportNotes', () => exportService.exportAll()),
    vscode.commands.registerCommand('codexNotes.importNotes', handleImport),
  );
}

export function deactivate(): void {}

