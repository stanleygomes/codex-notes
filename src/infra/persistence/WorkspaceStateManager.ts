import * as vscode from 'vscode';
import * as os from 'os';
import { Workspace } from '../../core/dtos/Workspace';
import { FileHelper } from '../../core/helpers/FileHelper';
import { OSHelper } from '../../core/helpers/OSHelper';
import { WorkspaceFileStorage } from './WorkspaceFileStorage';

export class WorkspaceStateManager {
  private static readonly STORAGE_KEY = 'savedProjects';
  private readonly fileStorage: WorkspaceFileStorage;

  constructor(private readonly context: vscode.ExtensionContext) {
    const targetPath = OSHelper.getDefaultConfigPath();
    this.fileStorage = new WorkspaceFileStorage(targetPath);

    if (context.globalState.setKeysForSync) {
      context.globalState.setKeysForSync([WorkspaceStateManager.STORAGE_KEY]);
    }

    this.initializeStorage(targetPath);
  }

  private initializeStorage(targetPath: string): void {
    if (!this.fileStorage.exists()) {
      const interimPath = FileHelper.buildPath(
        os.homedir(),
        '.config',
        'parable-workspaces',
        'workspaces.json',
      );
      const legacyPath = FileHelper.buildPath(
        this.context.globalStorageUri.fsPath,
        'workspaces.json',
      );

      if (FileHelper.exists(interimPath) && interimPath !== targetPath) {
        try {
          FileHelper.writeText(targetPath, FileHelper.readText(interimPath));
        } catch (e) {
          console.error('Error migrating from interim path', e);
        }
      } else if (FileHelper.exists(legacyPath) && legacyPath !== targetPath) {
        try {
          FileHelper.writeText(targetPath, FileHelper.readText(legacyPath));
        } catch (e) {
          console.error('Error migrating from legacy path', e);
        }
      } else {
        this.syncFromFileSystem();
      }
    }
  }

  private syncFromFileSystem(): void {
    const stateData = this.readState();
    if (stateData.length > 0 && !this.fileStorage.exists()) {
      this.fileStorage.write(stateData);
    }
  }

  getStoragePath(): string {
    return this.fileStorage.getDirectoryPath();
  }

  getStorageUri(): vscode.Uri {
    return vscode.Uri.file(this.fileStorage.getFilePath());
  }

  read(): Workspace[] {
    if (this.fileStorage.exists()) {
      const workspaces = this.fileStorage.read();
      if (workspaces.length > 0) {
        return workspaces;
      }
    }
    return this.readState();
  }

  async write(workspaces: Workspace[]): Promise<void> {
    this.fileStorage.write(workspaces);
    await this.context.globalState.update(
      WorkspaceStateManager.STORAGE_KEY,
      workspaces,
    );
  }

  private readState(): Workspace[] {
    return this.context.globalState.get<Workspace[]>(
      WorkspaceStateManager.STORAGE_KEY,
      [],
    );
  }
}
