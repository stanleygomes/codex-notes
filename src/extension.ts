import * as vscode from 'vscode';
import { NoteRepository } from './core/repository/NoteRepository';
import { Container } from './container';
import { NotesViewProvider } from './ui/NotesViewProvider';
import { registerCommands } from './commands';
import { Note } from './core/dto/Note';
import {
  openNote,
  createHandleCreate,
  createHandleDelete,
  createHandleRename,
  createHandleDuplicate,
  createHandleChangeColor,
  createHandleImport,
  createHandleOpenBackup,
} from './infra/editor/view';

export async function activate(
  context: vscode.ExtensionContext,
): Promise<void> {
  console.log('Activating Codex Notes extension...');
  const repository = await NoteRepository.initialize();

  console.log('Codex Notes extension activated successfully');
  const container = new Container(context, repository);

  console.log('Services initialized successfully');

  let provider: NotesViewProvider;

  const handleCreate = () =>
    createHandleCreate(container.createService, provider)();
  const handleDelete = (note: Note) =>
    createHandleDelete(container.deleteService, provider)(note);
  const handleRename = (note: Note) =>
    createHandleRename(container.renameService, provider)(note);
  const handleDuplicate = (note: Note) =>
    createHandleDuplicate(container.duplicateService, provider)(note);
  const handleChangeColor = (note: Note) =>
    createHandleChangeColor(container.colorService, provider)(note);
  const handleImport = () =>
    createHandleImport(container.importService, provider)();
  const handleOpenBackup = createHandleOpenBackup(
    context.extensionUri,
    container.importService,
    container.exportService,
    () => provider.refresh(),
  );

  console.log('Handlers created successfully');

  provider = new NotesViewProvider(
    context.extensionUri,
    container.repository,
    container.searchService,
    container.sortService,
    container.filterService,
    openNote,
    handleCreate,
    handleDelete,
    handleRename,
    handleDuplicate,
    (note) => {
      container.favoriteService.toggleFavorite(note);
      provider.refresh();
    },
    handleChangeColor,
    () => container.exportService.exportAll(),
    handleImport,
    (note) => container.locationService.openLocation(note),
  );

  context.subscriptions.push(
    vscode.window.registerWebviewViewProvider(
      NotesViewProvider.viewType,
      provider,
    ),
  );

  registerCommands(
    context,
    container,
    provider,
    handleCreate,
    handleImport,
    handleOpenBackup,
  );
}

export function deactivate(): void {}
