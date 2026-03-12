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
exports.FileHelper = void 0;
const fs = __importStar(require("fs"));
const path = __importStar(require("path"));
const os = __importStar(require("os"));
const FilenameHelper_1 = require("./FilenameHelper");
class FileHelper {
    static getDefaultNotesDir() {
        return path.join(os.homedir(), '.codex-notes');
    }
    static getDataDir() {
        return path.join(FileHelper.getDefaultNotesDir(), 'data');
    }
    static getDatabasePath() {
        return path.join(FileHelper.getDataDir(), 'notes.db');
    }
    static ensureDirectoryExists(dirPath) {
        if (!fs.existsSync(dirPath)) {
            fs.mkdirSync(dirPath, { recursive: true });
        }
    }
    static createFile(dir, fileName) {
        FileHelper.ensureDirectoryExists(dir);
        const filePath = path.join(dir, fileName);
        fs.writeFileSync(filePath, '');
        return filePath;
    }
    static createFileWithContent(dir, fileName, content) {
        FileHelper.ensureDirectoryExists(dir);
        const filePath = path.join(dir, fileName);
        fs.writeFileSync(filePath, content, 'utf8');
        return filePath;
    }
    static deleteFile(filePath) {
        if (fs.existsSync(filePath)) {
            fs.unlinkSync(filePath);
        }
    }
    static renameFile(oldPath, newName) {
        const dir = path.dirname(oldPath);
        const newPath = path.join(dir, newName);
        fs.renameSync(oldPath, newPath);
        return newPath;
    }
    static fileExists(parentDir, name) {
        return fs.existsSync(path.join(parentDir, name));
    }
    static getParentPath(filePath) {
        return path.dirname(filePath);
    }
    static getFileName(filePath) {
        return path.basename(filePath);
    }
    static buildPath(baseDir, ...parts) {
        return path.join(baseDir, ...parts);
    }
    static readText(filePath) {
        if (!fs.existsSync(filePath)) {
            return '';
        }
        return fs.readFileSync(filePath, 'utf8');
    }
    static getLastModified(filePath) {
        if (!fs.existsSync(filePath)) {
            return 0;
        }
        return fs.statSync(filePath).mtimeMs;
    }
    static buildNoteFileName(title, extension) {
        const normalized = FilenameHelper_1.FilenameHelper.normalize(title);
        const ext = extension.startsWith('.') ? extension : `.${extension}`;
        return `${normalized}${ext}`;
    }
    static copyFile(sourcePath, destPath) {
        fs.copyFileSync(sourcePath, destPath);
    }
    static listFiles(dirPath, extension) {
        if (!fs.existsSync(dirPath)) {
            return [];
        }
        return fs
            .readdirSync(dirPath)
            .filter((f) => f.endsWith(extension))
            .map((f) => path.join(dirPath, f));
    }
}
exports.FileHelper = FileHelper;
//# sourceMappingURL=FileHelper.js.map