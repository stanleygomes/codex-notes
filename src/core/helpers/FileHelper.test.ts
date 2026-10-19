import * as fs from 'node:fs';
import * as os from 'node:os';
import * as path from 'node:path';
import { describe, expect, it } from 'vitest';
import { FileHelper } from './FileHelper';

function tempDir(): string {
  return path.join(os.homedir(), 'files');
}

describe('FileHelper', () => {
  it('resolves the default directories inside the home folder', () => {
    const base = path.join(os.homedir(), '.codex-notes');

    expect(FileHelper.getDefaultNotesDir()).toBe(base);
    expect(FileHelper.getDataDir()).toBe(path.join(base, 'data'));
    expect(FileHelper.getDatabasePath()).toBe(
      path.join(base, 'data', 'notes.db'),
    );
  });

  it('creates missing directories recursively and ignores existing ones', () => {
    const dir = path.join(tempDir(), 'a', 'b');

    FileHelper.ensureDirectoryExists(dir);
    FileHelper.ensureDirectoryExists(dir);

    expect(fs.statSync(dir).isDirectory()).toBe(true);
  });

  it('creates an empty file', () => {
    const filePath = FileHelper.createFile(tempDir(), 'empty.md');

    expect(fs.readFileSync(filePath, 'utf8')).toBe('');
  });

  it('creates a file with content', () => {
    const filePath = FileHelper.createFileWithContent(
      tempDir(),
      'note.md',
      '# Hello',
    );

    expect(filePath).toBe(path.join(tempDir(), 'note.md'));
    expect(FileHelper.readText(filePath)).toBe('# Hello');
  });

  it('deletes existing files and ignores missing ones', () => {
    const filePath = FileHelper.createFile(tempDir(), 'gone.md');

    FileHelper.deleteFile(filePath);
    FileHelper.deleteFile(filePath);

    expect(fs.existsSync(filePath)).toBe(false);
  });

  it('renames a file inside the same folder', () => {
    const filePath = FileHelper.createFileWithContent(tempDir(), 'old.md', 'x');

    const newPath = FileHelper.renameFile(filePath, 'new.md');

    expect(newPath).toBe(path.join(tempDir(), 'new.md'));
    expect(FileHelper.fileExists(tempDir(), 'new.md')).toBe(true);
    expect(FileHelper.fileExists(tempDir(), 'old.md')).toBe(false);
  });

  it('splits paths into parent and file name', () => {
    expect(FileHelper.getParentPath('/a/b/c.md')).toBe('/a/b');
    expect(FileHelper.getFileName('/a/b/c.md')).toBe('c.md');
    expect(FileHelper.buildPath('/a', 'b', 'c.md')).toBe('/a/b/c.md');
  });

  it('returns empty text and zero mtime for missing files', () => {
    const missing = path.join(tempDir(), 'missing.md');

    expect(FileHelper.readText(missing)).toBe('');
    expect(FileHelper.getLastModified(missing)).toBe(0);
  });

  it('returns the last modified time of a file', () => {
    const filePath = FileHelper.createFile(tempDir(), 'mtime.md');

    expect(FileHelper.getLastModified(filePath)).toBe(
      fs.statSync(filePath).mtimeMs,
    );
  });

  it.each([
    ['My Note', 'md', 'my-note.md'],
    ['My Note', '.txt', 'my-note.txt'],
  ])('builds the file name for "%s" with "%s"', (title, ext, expected) => {
    expect(FileHelper.buildNoteFileName(title, ext)).toBe(expected);
  });

  it('copies a file', () => {
    const source = FileHelper.createFileWithContent(tempDir(), 'a.md', 'copy');
    const dest = path.join(tempDir(), 'b.md');

    FileHelper.copyFile(source, dest);

    expect(FileHelper.readText(dest)).toBe('copy');
  });

  it('lists files by extension', () => {
    FileHelper.createFile(tempDir(), 'a.md');
    FileHelper.createFile(tempDir(), 'b.txt');

    expect(FileHelper.listFiles(tempDir(), '.md')).toEqual([
      path.join(tempDir(), 'a.md'),
    ]);
  });

  it('lists nothing for a missing directory', () => {
    expect(FileHelper.listFiles(path.join(tempDir(), 'nope'), '.md')).toEqual(
      [],
    );
  });
});
