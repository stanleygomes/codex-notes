"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.DateHelper = void 0;
const date_fns_1 = require("date-fns");
class DateHelper {
    static toHumanRelative(timestampMs) {
        return (0, date_fns_1.formatDistanceToNow)(new Date(timestampMs), { addSuffix: true });
    }
    static startOfDay(date) {
        const d = new Date(date);
        d.setHours(0, 0, 0, 0);
        return d;
    }
    static startOfWeek(date) {
        const d = new Date(date);
        const day = d.getDay();
        d.setDate(d.getDate() - day);
        d.setHours(0, 0, 0, 0);
        return d;
    }
    static startOfMonth(date) {
        const d = new Date(date);
        d.setDate(1);
        d.setHours(0, 0, 0, 0);
        return d;
    }
    static nowMs() {
        return Date.now();
    }
}
exports.DateHelper = DateHelper;
//# sourceMappingURL=DateHelper.js.map