"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.FilterNotesService = void 0;
const DateFilterEnum_1 = require("../enum/DateFilterEnum");
const DateHelper_1 = require("../helper/DateHelper");
class FilterNotesService {
    filterByFavorite(notes) {
        return notes.filter((n) => n.isFavorite);
    }
    filterByDateRange(notes, dateFilter) {
        const now = new Date();
        let since;
        switch (dateFilter) {
            case DateFilterEnum_1.DateFilterEnum.TODAY:
                since = DateHelper_1.DateHelper.startOfDay(now);
                break;
            case DateFilterEnum_1.DateFilterEnum.THIS_WEEK:
                since = DateHelper_1.DateHelper.startOfWeek(now);
                break;
            case DateFilterEnum_1.DateFilterEnum.THIS_MONTH:
                since = DateHelper_1.DateHelper.startOfMonth(now);
                break;
        }
        return notes.filter((n) => n.updatedAt >= since.getTime());
    }
}
exports.FilterNotesService = FilterNotesService;
//# sourceMappingURL=FilterNotesService.js.map