"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
exports.activate = activate;
exports.deactivate = deactivate;
const vscode = __importStar(require("vscode"));
const NoteRepository_1 = require("./repository/NoteRepository");
const CreateNoteService_1 = require("./service/CreateNoteService");
const DeleteNoteService_1 = require("./service/DeleteNoteService");
const RenameNoteService_1 = require("./service/RenameNoteService");
const SearchNoteService_1 = require("./service/SearchNoteService");
const DuplicateNoteService_1 = require("./service/DuplicateNoteService");
const ExportNotesService_1 = require("./service/ExportNotesService");
const ImportNotesService_1 = require("./service/ImportNotesService");
const FavoriteNoteService_1 = require("./service/FavoriteNoteService");
const ChangeNoteColorService_1 = require("./service/ChangeNoteColorService");
const SortNotesService_1 = require("./service/SortNotesService");
const FilterNotesService_1 = require("./service/FilterNotesService");
const OpenNoteLocationService_1 = require("./service/OpenNoteLocationService");
const NotesViewProvider_1 = require("./ui/NotesViewProvider");
function activate(context) {
    const repository = NoteRepository_1.NoteRepository.getInstance();
    const createService = new CreateNoteService_1.CreateNoteService(repository);
    const deleteService = new DeleteNoteService_1.DeleteNoteService(repository);
    const renameService = new RenameNoteService_1.RenameNoteService(repository);
    const searchService = new SearchNoteService_1.SearchNoteService(repository);
    const duplicateService = new DuplicateNoteService_1.DuplicateNoteService(repository, createService);
    const exportService = new ExportNotesService_1.ExportNotesService(repository);
    const importService = new ImportNotesService_1.ImportNotesService(repository, createService);
    const favoriteService = new FavoriteNoteService_1.FavoriteNoteService(repository);
    const colorService = new ChangeNoteColorService_1.ChangeNoteColorService(repository);
    const sortService = new SortNotesService_1.SortNotesService();
    const filterService = new FilterNotesService_1.FilterNotesService();
    const locationService = new OpenNoteLocationService_1.OpenNoteLocationService();
    const provider = new NotesViewProvider_1.NotesViewProvider(context.extensionUri, repository, searchService, sortService, filterService, (note) => openNote(note), () => handleCreate(), (note) => handleDelete(note), (note) => handleRename(note), (note) => handleDuplicate(note), (note) => { favoriteService.toggleFavorite(note); provider.refresh(); }, (note) => handleChangeColor(note), () => exportService.exportAll(), () => handleImport(), (note) => locationService.openLocation(note));
    context.subscriptions.push(vscode.window.registerWebviewViewProvider(NotesViewProvider_1.NotesViewProvider.viewType, provider));
    async function openNote(note) {
        const uri = vscode.Uri.file(note.filePath);
        const doc = await vscode.workspace.openTextDocument(uri);
        await vscode.window.showTextDocument(doc);
    }
    async function handleCreate() {
        await createService.create();
        provider.refresh();
    }
    async function handleDelete(note) {
        const deleted = await deleteService.confirmAndDelete([note]);
        if (deleted) {
            provider.refresh();
        }
    }
    async function handleRename(note) {
        const renamed = await renameService.rename(note);
        if (renamed) {
            provider.refresh();
        }
    }
    async function handleDuplicate(note) {
        await duplicateService.duplicate(note);
        provider.refresh();
    }
    async function handleChangeColor(note) {
        await colorService.changeColor(note);
        provider.refresh();
    }
    async function handleImport() {
        await importService.import();
        provider.refresh();
    }
    context.subscriptions.push(vscode.commands.registerCommand('codexNotes.createNote', () => handleCreate()), vscode.commands.registerCommand('codexNotes.createNoteFromSelection', async () => {
        const editor = vscode.window.activeTextEditor;
        const selection = editor?.document.getText(editor.selection) ?? '';
        await createService.create(selection);
        provider.refresh();
    }), vscode.commands.registerCommand('codexNotes.refreshNotes', () => provider.refresh()), vscode.commands.registerCommand('codexNotes.exportNotes', () => exportService.exportAll()), vscode.commands.registerCommand('codexNotes.importNotes', () => handleImport()));
}
function deactivate() { }
//# sourceMappingURL=extension.js.map