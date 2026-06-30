import * as os from 'os';
import * as path from 'path';

export class NotesSettings {
  static getFileExtension(): string {
    return 'md';
  }

  static getNotesDirectory(): string {
    return path.join(os.homedir(), '.codex-notes');
  }
}
