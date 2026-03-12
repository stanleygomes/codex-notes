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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.NoteRepository = void 0;
const better_sqlite3_1 = __importDefault(require("better-sqlite3"));
const better_sqlite3_2 = require("drizzle-orm/better-sqlite3");
const sqlite_core_1 = require("drizzle-orm/sqlite-core");
const drizzle_orm_1 = require("drizzle-orm");
const crypto = __importStar(require("crypto"));
const NoteColorEnum_1 = require("../enum/NoteColorEnum");
const FileHelper_1 = require("../helper/FileHelper");
const DateHelper_1 = require("../helper/DateHelper");
const notesTable = (0, sqlite_core_1.sqliteTable)('notes', {
    id: (0, sqlite_core_1.text)('id').primaryKey(),
    title: (0, sqlite_core_1.text)('title').notNull(),
    filePath: (0, sqlite_core_1.text)('filePath').notNull(),
    createdAt: (0, sqlite_core_1.integer)('createdAt').notNull(),
    updatedAt: (0, sqlite_core_1.integer)('updatedAt').notNull(),
    isFavorite: (0, sqlite_core_1.integer)('isFavorite').notNull().default(0),
    color: (0, sqlite_core_1.text)('color').notNull().default(NoteColorEnum_1.NoteColorEnum.NONE),
});
class NoteRepository {
    static instance;
    db;
    constructor() {
        FileHelper_1.FileHelper.ensureDirectoryExists(FileHelper_1.FileHelper.getDataDir());
        const sqlite = new better_sqlite3_1.default(FileHelper_1.FileHelper.getDatabasePath());
        this.db = (0, better_sqlite3_2.drizzle)(sqlite);
        this.migrate(sqlite);
    }
    static getInstance() {
        if (!NoteRepository.instance) {
            NoteRepository.instance = new NoteRepository();
        }
        return NoteRepository.instance;
    }
    migrate(sqlite) {
        sqlite.exec(`
      CREATE TABLE IF NOT EXISTS notes (
        id TEXT PRIMARY KEY,
        title TEXT NOT NULL,
        filePath TEXT NOT NULL,
        createdAt INTEGER NOT NULL,
        updatedAt INTEGER NOT NULL,
        isFavorite INTEGER NOT NULL DEFAULT 0,
        color TEXT NOT NULL DEFAULT 'NONE'
      )
    `);
    }
    addNote(title, filePath) {
        const now = DateHelper_1.DateHelper.nowMs();
        const note = {
            id: crypto.randomUUID(),
            title,
            filePath,
            createdAt: now,
            updatedAt: now,
            isFavorite: false,
            color: NoteColorEnum_1.NoteColorEnum.NONE,
        };
        this.db.insert(notesTable).values({
            id: note.id,
            title: note.title,
            filePath: note.filePath,
            createdAt: note.createdAt,
            updatedAt: note.updatedAt,
            isFavorite: 0,
            color: NoteColorEnum_1.NoteColorEnum.NONE,
        }).run();
        return note;
    }
    updateNote(id, title, filePath) {
        const updates = {
            updatedAt: DateHelper_1.DateHelper.nowMs(),
        };
        if (title !== undefined) {
            updates.title = title;
        }
        if (filePath !== undefined) {
            updates.filePath = filePath;
        }
        this.db.update(notesTable).set(updates).where((0, drizzle_orm_1.eq)(notesTable.id, id)).run();
    }
    toggleFavorite(id) {
        const note = this.getNoteById(id);
        if (!note) {
            return;
        }
        this.db
            .update(notesTable)
            .set({ isFavorite: note.isFavorite ? 0 : 1, updatedAt: DateHelper_1.DateHelper.nowMs() })
            .where((0, drizzle_orm_1.eq)(notesTable.id, id))
            .run();
    }
    changeColor(id, color) {
        this.db
            .update(notesTable)
            .set({ color, updatedAt: DateHelper_1.DateHelper.nowMs() })
            .where((0, drizzle_orm_1.eq)(notesTable.id, id))
            .run();
    }
    removeNote(id) {
        this.db.delete(notesTable).where((0, drizzle_orm_1.eq)(notesTable.id, id)).run();
    }
    getAllNotes() {
        const rows = this.db.select().from(notesTable).all();
        return rows.map((row) => this.rowToNote(row));
    }
    getNoteById(id) {
        const rows = this.db.select().from(notesTable).where((0, drizzle_orm_1.eq)(notesTable.id, id)).all();
        return rows.length > 0 ? this.rowToNote(rows[0]) : undefined;
    }
    importNote(note) {
        const existing = this.getNoteById(note.id);
        if (existing) {
            return;
        }
        this.db.insert(notesTable).values({
            id: note.id,
            title: note.title,
            filePath: note.filePath,
            createdAt: note.createdAt,
            updatedAt: note.updatedAt,
            isFavorite: note.isFavorite ? 1 : 0,
            color: note.color,
        }).run();
    }
    rowToNote(row) {
        return {
            id: row.id,
            title: row.title,
            filePath: row.filePath,
            createdAt: row.createdAt,
            updatedAt: row.updatedAt,
            isFavorite: row.isFavorite === 1,
            color: row.color ?? NoteColorEnum_1.NoteColorEnum.NONE,
        };
    }
}
exports.NoteRepository = NoteRepository;
//# sourceMappingURL=NoteRepository.js.map