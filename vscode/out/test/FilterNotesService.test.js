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
const assert = __importStar(require("assert"));
const FilterNotesService_1 = require("../service/FilterNotesService");
const DateFilterEnum_1 = require("../enum/DateFilterEnum");
const NoteColorEnum_1 = require("../enum/NoteColorEnum");
function makeNote(id, updatedAt, isFavorite = false) {
    return {
        id,
        title: `Note ${id}`,
        filePath: `/tmp/${id}.md`,
        createdAt: updatedAt,
        updatedAt,
        isFavorite,
        color: NoteColorEnum_1.NoteColorEnum.NONE,
    };
}
suite('FilterNotesService', () => {
    const service = new FilterNotesService_1.FilterNotesService();
    test('should filter only favorite notes', () => {
        const notes = [makeNote('1', 1000, true), makeNote('2', 2000, false), makeNote('3', 3000, true)];
        const result = service.filterByFavorite(notes);
        assert.strictEqual(result.length, 2);
        assert.ok(result.every((n) => n.isFavorite));
    });
    test('should return empty array when no favorites exist', () => {
        const notes = [makeNote('1', 1000, false), makeNote('2', 2000, false)];
        const result = service.filterByFavorite(notes);
        assert.strictEqual(result.length, 0);
    });
    test('should filter notes updated today', () => {
        const now = Date.now();
        const yesterday = now - 25 * 60 * 60 * 1000;
        const notes = [makeNote('1', now), makeNote('2', yesterday)];
        const result = service.filterByDateRange(notes, DateFilterEnum_1.DateFilterEnum.TODAY);
        assert.strictEqual(result.length, 1);
        assert.strictEqual(result[0].id, '1');
    });
    test('should include notes from the current month when filtering by month', () => {
        const now = Date.now();
        const twoMonthsAgo = now - 62 * 24 * 60 * 60 * 1000;
        const notes = [makeNote('1', now), makeNote('2', twoMonthsAgo)];
        const result = service.filterByDateRange(notes, DateFilterEnum_1.DateFilterEnum.THIS_MONTH);
        assert.strictEqual(result.length, 1);
        assert.strictEqual(result[0].id, '1');
    });
});
//# sourceMappingURL=FilterNotesService.test.js.map