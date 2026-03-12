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
const SearchHelper_1 = require("../helper/SearchHelper");
const NoteColorEnum_1 = require("../enum/NoteColorEnum");
function makeNote(id, title) {
    return {
        id,
        title,
        filePath: `/tmp/${id}.md`,
        createdAt: Date.now(),
        updatedAt: Date.now(),
        isFavorite: false,
        color: NoteColorEnum_1.NoteColorEnum.NONE,
    };
}
suite('SearchHelper', () => {
    test('should return all notes when query is empty', () => {
        const notes = [makeNote('1', 'Alpha'), makeNote('2', 'Beta')];
        const result = SearchHelper_1.SearchHelper.search(notes, '');
        assert.strictEqual(result.length, 2);
    });
    test('should return exact match when query matches title exactly', () => {
        const notes = [makeNote('1', 'Alpha'), makeNote('2', 'Beta'), makeNote('3', 'Gamma')];
        const result = SearchHelper_1.SearchHelper.search(notes, 'Alpha');
        assert.strictEqual(result[0].title, 'Alpha');
    });
    test('should filter out notes that do not match query', () => {
        const notes = [makeNote('1', 'Shopping list'), makeNote('2', 'Meeting notes')];
        const result = SearchHelper_1.SearchHelper.search(notes, 'xyz');
        assert.strictEqual(result.length, 0);
    });
    test('should rank title matches higher than content matches', () => {
        const notes = [makeNote('1', 'Something else'), makeNote('2', 'Recipe')];
        const contentMap = new Map([
            ['1', 'This has recipe in content'],
            ['2', 'Quick recipe title note'],
        ]);
        const result = SearchHelper_1.SearchHelper.search(notes, 'recipe', contentMap);
        assert.strictEqual(result[0].title, 'Recipe');
    });
    test('should search case insensitively', () => {
        const notes = [makeNote('1', 'TypeScript Tips')];
        const result = SearchHelper_1.SearchHelper.search(notes, 'typescript');
        assert.strictEqual(result.length, 1);
    });
    test('should match partial words in title', () => {
        const notes = [makeNote('1', 'Meeting notes'), makeNote('2', 'Shopping')];
        const result = SearchHelper_1.SearchHelper.search(notes, 'meet');
        assert.strictEqual(result.length, 1);
        assert.strictEqual(result[0].title, 'Meeting notes');
    });
});
//# sourceMappingURL=SearchHelper.test.js.map