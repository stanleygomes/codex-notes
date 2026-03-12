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
const SortNotesService_1 = require("../service/SortNotesService");
const SortTypeEnum_1 = require("../enum/SortTypeEnum");
const NoteColorEnum_1 = require("../enum/NoteColorEnum");
function makeNote(id, title, updatedAt, isFavorite = false) {
    return {
        id,
        title,
        filePath: `/tmp/${id}.md`,
        createdAt: updatedAt,
        updatedAt,
        isFavorite,
        color: NoteColorEnum_1.NoteColorEnum.NONE,
    };
}
suite('SortNotesService', () => {
    const service = new SortNotesService_1.SortNotesService();
    test('should sort notes by title alphabetically', () => {
        const notes = [makeNote('1', 'Zebra', 1000), makeNote('2', 'Apple', 2000)];
        const result = service.sort(notes, SortTypeEnum_1.SortTypeEnum.TITLE);
        assert.strictEqual(result[0].title, 'Apple');
        assert.strictEqual(result[1].title, 'Zebra');
    });
    test('should sort notes by date descending (most recent first)', () => {
        const notes = [makeNote('1', 'Old', 1000), makeNote('2', 'New', 9000)];
        const result = service.sort(notes, SortTypeEnum_1.SortTypeEnum.DATE);
        assert.strictEqual(result[0].title, 'New');
        assert.strictEqual(result[1].title, 'Old');
    });
    test('should sort favorites first when sorting by favorite', () => {
        const notes = [
            makeNote('1', 'Regular', 1000, false),
            makeNote('2', 'Favorite', 500, true),
        ];
        const result = service.sort(notes, SortTypeEnum_1.SortTypeEnum.FAVORITE);
        assert.strictEqual(result[0].title, 'Favorite');
    });
    test('should not mutate original array when sorting', () => {
        const notes = [makeNote('1', 'Zebra', 1000), makeNote('2', 'Apple', 2000)];
        const original = [...notes];
        service.sort(notes, SortTypeEnum_1.SortTypeEnum.TITLE);
        assert.strictEqual(notes[0].title, original[0].title);
    });
});
//# sourceMappingURL=SortNotesService.test.js.map