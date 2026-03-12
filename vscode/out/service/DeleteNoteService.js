"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.DeleteNoteService = void 0;
const FileHelper_1 = require("../helper/FileHelper");
const DialogHelper_1 = require("../helper/DialogHelper");
class DeleteNoteService {
    repository;
    constructor(repository) {
        this.repository = repository;
    }
    async confirmAndDelete(notes) {
        const names = notes.slice(0, 5).map((n) => `"${n.title}"`);
        const extra = notes.length > 5 ? ` and ${notes.length - 5} more` : '';
        const confirmed = await DialogHelper_1.DialogHelper.showConfirmation(`Delete ${names.join(', ')}${extra}? This action cannot be undone.`);
        if (!confirmed) {
            return false;
        }
        for (const note of notes) {
            FileHelper_1.FileHelper.deleteFile(note.filePath);
            this.repository.removeNote(note.id);
        }
        return true;
    }
}
exports.DeleteNoteService = DeleteNoteService;
//# sourceMappingURL=DeleteNoteService.js.map