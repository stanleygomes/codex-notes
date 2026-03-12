"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.FavoriteNoteService = void 0;
class FavoriteNoteService {
    repository;
    constructor(repository) {
        this.repository = repository;
    }
    toggleFavorite(note) {
        this.repository.toggleFavorite(note.id);
    }
}
exports.FavoriteNoteService = FavoriteNoteService;
//# sourceMappingURL=FavoriteNoteService.js.map