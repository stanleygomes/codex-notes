import * as vscode from 'vscode';

export class UserInteraction {
  public async showInputBox(
    options: vscode.InputBoxOptions,
  ): Promise<string | undefined> {
    return vscode.window.showInputBox(options);
  }

  public async showConfirmation(message: string): Promise<boolean> {
    const answer = await vscode.window.showWarningMessage(message, 'Yes', 'No');
    return answer === 'Yes';
  }

  public async showQuickPick<T extends vscode.QuickPickItem>(
    items: T[],
    options: vscode.QuickPickOptions,
  ): Promise<T | undefined> {
    return vscode.window.showQuickPick(items, options);
  }

  public showError(message: string): void {
    vscode.window.showErrorMessage(message);
  }

  public async showInfo(
    message: string,
    ...actions: string[]
  ): Promise<string | undefined> {
    return vscode.window.showInformationMessage(message, ...actions);
  }

  public async executeCommand(command: string, ...args: any[]): Promise<any> {
    return vscode.commands.executeCommand(command, ...args);
  }

  public async showOpenDialog(
    options: vscode.OpenDialogOptions,
  ): Promise<vscode.Uri[] | undefined> {
    return vscode.window.showOpenDialog(options);
  }

  public async showSaveDialog(
    options: vscode.SaveDialogOptions,
  ): Promise<vscode.Uri | undefined> {
    return vscode.window.showSaveDialog(options);
  }

  public async openTextDocument(uri: vscode.Uri): Promise<void> {
    const document = await vscode.workspace.openTextDocument(uri);
    await vscode.window.showTextDocument(document);
  }

  public async openFolder(
    path: string,
    forceNewWindow: boolean = false,
  ): Promise<void> {
    const uri = vscode.Uri.file(path);
    await vscode.commands.executeCommand(
      'vscode.openFolder',
      uri,
      forceNewWindow,
    );
  }
}
