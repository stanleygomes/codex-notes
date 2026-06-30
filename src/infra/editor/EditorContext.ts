import * as vscode from 'vscode';
import { StringHelper } from '../../core/helpers/StringHelper';

export class EditorContext {
  private static getActiveFolders():
    | readonly vscode.WorkspaceFolder[]
    | undefined {
    const folders = vscode.workspace.workspaceFolders;
    return folders && folders.length > 0 ? folders : undefined;
  }

  public static getCurrentWorkspaceId(): string | undefined {
    const folders = this.getActiveFolders();
    if (!folders) return undefined;

    const primaryFolder = folders[0];
    const workspaceFile = vscode.workspace.workspaceFile;

    return StringHelper.toBase64(
      workspaceFile?.fsPath || primaryFolder.uri.fsPath,
    );
  }

  public static getCurrentWorkspaceFolders(): string[] {
    const folders = this.getActiveFolders();
    if (!folders) return [];
    return folders.map((f) => f.uri.fsPath);
  }

  public static getCurrentWorkspaceName(): string | undefined {
    const folders = this.getActiveFolders();
    if (!folders) return undefined;

    const primaryFolder = folders[0];
    const workspaceFile = vscode.workspace.workspaceFile;

    return workspaceFile
      ? vscode.workspace.name || primaryFolder.name
      : primaryFolder.name;
  }
}
