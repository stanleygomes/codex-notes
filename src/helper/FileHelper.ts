import * as fs from 'fs';
import * as path from 'path';
import * as os from 'os';
import { FilenameHelper } from './FilenameHelper';

export class FileHelper {
  static getDefaultNotesDir(): string {
    return path.join(os.homedir(), '.codex-notes');
  }

  static getDataDir(): string {
    return path.join(FileHelper.getDefaultNotesDir(), 'data');
  }

  static getDatabasePath(): string {
    return path.join(FileHelper.getDataDir(), 'notes.db');
  }

  static ensureDirectoryExists(dirPath: string): void {
    if (!fs.existsSync(dirPath)) {
      fs.mkdirSync(dirPath, { recursive: true });
    }
  }

  static createFile(dir: string, fileName: string): string {
    FileHelper.ensureDirectoryExists(dir);
    const filePath = path.join(dir, fileName);
    fs.writeFileSync(filePath, '');
    return filePath;
  }

  static createFileWithContent(dir: string, fileName: string, content: string): string {
    FileHelper.ensureDirectoryExists(dir);
    const filePath = path.join(dir, fileName);
    fs.writeFileSync(filePath, content, 'utf8');
    return filePath;
  }

  static deleteFile(filePath: string): void {
    if (fs.existsSync(filePath)) {
      fs.unlinkSync(filePath);
    }
  }

  static renameFile(oldPath: string, newName: string): string {
    const dir = path.dirname(oldPath);
    const newPath = path.join(dir, newName);
    fs.renameSync(oldPath, newPath);
    return newPath;
  }

  static fileExists(parentDir: string, name: string): boolean {
    return fs.existsSync(path.join(parentDir, name));
  }

  static getParentPath(filePath: string): string {
    return path.dirname(filePath);
  }

  static getFileName(filePath: string): string {
    return path.basename(filePath);
  }

  static buildPath(baseDir: string, ...parts: string[]): string {
    return path.join(baseDir, ...parts);
  }

  static readText(filePath: string): string {
    if (!fs.existsSync(filePath)) {
      return '';
    }
    return fs.readFileSync(filePath, 'utf8');
  }

  static getLastModified(filePath: string): number {
    if (!fs.existsSync(filePath)) {
      return 0;
    }
    return fs.statSync(filePath).mtimeMs;
  }

  static buildNoteFileName(title: string, extension: string): string {
    const normalized = FilenameHelper.normalize(title);
    const ext = extension.startsWith('.') ? extension : `.${extension}`;
    return `${normalized}${ext}`;
  }

  static copyFile(sourcePath: string, destPath: string): void {
    fs.copyFileSync(sourcePath, destPath);
  }

  static listFiles(dirPath: string, extension: string): string[] {
    if (!fs.existsSync(dirPath)) {
      return [];
    }
    return fs
      .readdirSync(dirPath)
      .filter((f) => f.endsWith(extension))
      .map((f) => path.join(dirPath, f));
  }
}
