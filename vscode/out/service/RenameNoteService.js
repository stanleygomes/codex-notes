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
exports.RenameNoteService = void 0;
const vscode = __importStar(require("vscode"));
const path = __importStar(require("path"));
const FileHelper_1 = require("../helper/FileHelper");
const DialogHelper_1 = require("../helper/DialogHelper");
const NotesSettings_1 = require("../editor/settings/NotesSettings");
class RenameNoteService {
    repository;
    constructor(repository) {
        this.repository = repository;
    }
    async rename(note) {
        const newTitle = await DialogHelper_1.DialogHelper.showInputBox({
            prompt: 'Enter new note title',
            value: note.title,
            validateInput: (value) => {
                if (!value.trim()) {
                    return 'Title cannot be empty';
                }
                return undefined;
            },
        });
        if (!newTitle || newTitle.trim() === note.title) {
            return undefined;
        }
        const title = newTitle.trim();
        const extension = NotesSettings_1.NotesSettings.getFileExtension();
        const dir = FileHelper_1.FileHelper.getParentPath(note.filePath);
        const newFileName = FileHelper_1.FileHelper.buildNoteFileName(title, extension);
        if (FileHelper_1.FileHelper.fileExists(dir, newFileName)) {
            DialogHelper_1.DialogHelper.showError(`A note with the filename "${newFileName}" already exists.`);
            return undefined;
        }
        const newFilePath = FileHelper_1.FileHelper.renameFile(note.filePath, newFileName);
        this.repository.updateNote(note.id, title, newFilePath);
        await this.reopenNote(note.filePath, newFilePath);
        return { ...note, title, filePath: newFilePath };
    }
    async reopenNote(oldPath, newPath) {
        const oldUri = vscode.Uri.file(oldPath);
        for (const editor of vscode.window.visibleTextEditors) {
            if (editor.document.uri.fsPath === oldUri.fsPath) {
                await vscode.commands.executeCommand('workbench.action.closeActiveEditor');
                break;
            }
        }
        const newUri = vscode.Uri.file(newPath);
        const ext = path.extname(newPath).toLowerCase();
        if (ext === '.md' || ext === '.txt') {
            const doc = await vscode.workspace.openTextDocument(newUri);
            await vscode.window.showTextDocument(doc);
        }
    }
}
exports.RenameNoteService = RenameNoteService;
//# sourceMappingURL=RenameNoteService.js.map