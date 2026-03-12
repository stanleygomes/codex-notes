"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.SortNotesService = void 0;
const SortTypeEnum_1 = require("../enum/SortTypeEnum");
class SortNotesService {
    sort(notes, sortType) {
        const copy = [...notes];
        switch (sortType) {
            case SortTypeEnum_1.SortTypeEnum.TITLE:
                return copy.sort((a, b) => a.title.localeCompare(b.title));
            case SortTypeEnum_1.SortTypeEnum.DATE:
                return copy.sort((a, b) => b.updatedAt - a.updatedAt);
            case SortTypeEnum_1.SortTypeEnum.FAVORITE:
                return copy.sort((a, b) => {
                    if (a.isFavorite === b.isFavorite) {
                        return b.updatedAt - a.updatedAt;
                    }
                    return a.isFavorite ? -1 : 1;
                });
        }
    }
}
exports.SortNotesService = SortNotesService;
//# sourceMappingURL=SortNotesService.js.map