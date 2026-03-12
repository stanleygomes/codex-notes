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
exports.CreateNoteService = void 0;
const vscode = __importStar(require("vscode"));
const FileHelper_1 = require("../helper/FileHelper");
const NotesSettings_1 = require("../editor/settings/NotesSettings");
class CreateNoteService {
    repository;
    constructor(repository) {
        this.repository = repository;
    }
    async create(content) {
        const title = await this.generateUniqueTitle();
        if (!title) {
            return undefined;
        }
        return this.createWithTitle(title, content ?? '');
    }
    async createWithTitle(title, content = '') {
        const extension = NotesSettings_1.NotesSettings.getFileExtension();
        const notesDir = NotesSettings_1.NotesSettings.getNotesDirectory();
        const fileName = FileHelper_1.FileHelper.buildNoteFileName(title, extension);
        const filePath = FileHelper_1.FileHelper.createFileWithContent(notesDir, fileName, content);
        const note = this.repository.addNote(title, filePath);
        await this.openNote(filePath);
        return note;
    }
    async generateUniqueTitle() {
        const existing = this.repository.getAllNotes().map((n) => n.title);
        let index = 1;
        let title = 'Untitled';
        while (existing.includes(title)) {
            title = `Untitled ${index}`;
            index++;
        }
        return title;
    }
    async openNote(filePath) {
        const uri = vscode.Uri.file(filePath);
        const doc = await vscode.workspace.openTextDocument(uri);
        await vscode.window.showTextDocument(doc);
    }
}
exports.CreateNoteService = CreateNoteService;
//# sourceMappingURL=CreateNoteService.js.map