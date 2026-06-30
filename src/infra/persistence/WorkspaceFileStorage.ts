import * as path from 'path';
import { Workspace } from '../../core/dtos/Workspace';
import { FileHelper } from '../../core/helpers/FileHelper';

export class WorkspaceFileStorage {
  constructor(private readonly filePath: string) {
    const dir = path.dirname(this.filePath);
    FileHelper.mkdir(dir);
  }

  public exists(): boolean {
    return FileHelper.exists(this.filePath);
  }

  public read(): Workspace[] {
    if (!this.exists()) {
      return [];
    }
    try {
      const content = FileHelper.readText(this.filePath);
      return JSON.parse(content);
    } catch (e) {
      console.error('Error reading workspaces file', e);
      return [];
    }
  }

  public write(workspaces: Workspace[]): void {
    FileHelper.writeText(this.filePath, JSON.stringify(workspaces, null, 2));
  }

  public getFilePath(): string {
    return this.filePath;
  }

  public getDirectoryPath(): string {
    return path.dirname(this.filePath);
  }
}
