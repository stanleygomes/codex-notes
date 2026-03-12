"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.DuplicateNoteService = void 0;
const FileHelper_1 = require("../helper/FileHelper");
class DuplicateNoteService {
    repository;
    createNoteService;
    constructor(repository, createNoteService) {
        this.repository = repository;
        this.createNoteService = createNoteService;
    }
    async duplicate(note) {
        const content = FileHelper_1.FileHelper.readText(note.filePath);
        const newTitle = this.generateDuplicateTitle(note.title);
        return this.createNoteService.createWithTitle(newTitle, content);
    }
    generateDuplicateTitle(originalTitle) {
        const existing = this.repository.getAllNotes().map((n) => n.title);
        let index = 1;
        let title = `${originalTitle} (${index})`;
        while (existing.includes(title)) {
            index++;
            title = `${originalTitle} (${index})`;
        }
        return title;
    }
}
exports.DuplicateNoteService = DuplicateNoteService;
//# sourceMappingURL=DuplicateNoteService.js.map