"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.SearchNoteService = void 0;
const FileHelper_1 = require("../helper/FileHelper");
const SearchHelper_1 = require("../helper/SearchHelper");
class SearchNoteService {
    repository;
    constructor(repository) {
        this.repository = repository;
    }
    search(query) {
        const allNotes = this.repository.getAllNotes();
        const contentMap = this.buildContentMap(allNotes);
        return SearchHelper_1.SearchHelper.search(allNotes, query, contentMap);
    }
    buildContentMap(notes) {
        const map = new Map();
        for (const note of notes) {
            map.set(note.id, FileHelper_1.FileHelper.readText(note.filePath));
        }
        return map;
    }
}
exports.SearchNoteService = SearchNoteService;
//# sourceMappingURL=SearchNoteService.js.map