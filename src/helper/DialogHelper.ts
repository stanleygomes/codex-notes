import * as vscode from 'vscode';

export class DialogHelper {
  static async showInputBox(options: vscode.InputBoxOptions): Promise<string | undefined> {
    return vscode.window.showInputBox(options);
  }

  static async showConfirmation(message: string): Promise<boolean> {
    const answer = await vscode.window.showWarningMessage(message, 'Yes', 'No');
    return answer === 'Yes';
  }

  static async showQuickPick<T extends vscode.QuickPickItem>(
    items: T[],
    options: vscode.QuickPickOptions,
  ): Promise<T | undefined> {
    return vscode.window.showQuickPick(items, options);
  }

  static showError(message: string): void {
    vscode.window.showErrorMessage(message);
  }

  static showInfo(message: string): void {
    vscode.window.showInformationMessage(message);
  }

  static async showOpenDialog(options: vscode.OpenDialogOptions): Promise<vscode.Uri[] | undefined> {
    return vscode.window.showOpenDialog(options);
  }

  static async showSaveDialog(options: vscode.SaveDialogOptions): Promise<vscode.Uri | undefined> {
    return vscode.window.showSaveDialog(options);
  }
}
