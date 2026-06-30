import * as vscode from 'vscode';

export class EditorStatusBar {
  private readonly statusBarItem: vscode.StatusBarItem;

  constructor() {
    this.statusBarItem = vscode.window.createStatusBarItem(
      vscode.StatusBarAlignment.Left,
      100,
    );
  }

  public update(text: string, tooltip: string, command: string): void {
    this.statusBarItem.text = text;
    this.statusBarItem.tooltip = tooltip;
    this.statusBarItem.command = command;
    this.statusBarItem.show();
  }

  public hide(): void {
    this.statusBarItem.hide();
  }

  public dispose(): void {
    this.statusBarItem.dispose();
  }

  public onDidChangeWorkspaceFolders(callback: () => void): vscode.Disposable {
    return vscode.workspace.onDidChangeWorkspaceFolders(callback);
  }
}
