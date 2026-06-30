import * as vscode from 'vscode';
import * as path from 'path';

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

  public async openTextDocument(filePath: string): Promise<void> {
    const uri = vscode.Uri.file(filePath);
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

  public async revealFileInOS(filePath: string): Promise<void> {
    const uri = vscode.Uri.file(filePath);
    await vscode.commands.executeCommand('revealFileInOS', uri);
  }

  public async reopenNote(oldPath: string, newPath: string): Promise<void> {
    const oldUri = vscode.Uri.file(oldPath);
    for (const editor of vscode.window.visibleTextEditors) {
      if (editor.document.uri.fsPath === oldUri.fsPath) {
        await vscode.commands.executeCommand(
          'workbench.action.closeActiveEditor',
        );
        break;
      }
    }
    const ext = path.extname(newPath).toLowerCase();
    if (ext === '.md' || ext === '.txt') {
      const newUri = vscode.Uri.file(newPath);
      const doc = await vscode.workspace.openTextDocument(newUri);
      await vscode.window.showTextDocument(doc);
    }
  }

  public showQuickSearch(
    placeholder: string,
    initialItems: vscode.QuickPickItem[],
    onSearch: (value: string) => vscode.QuickPickItem[],
    onAccept: (selectedItem: vscode.QuickPickItem) => void,
  ): void {
    const quickPick = vscode.window.createQuickPick();
    quickPick.placeholder = placeholder;
    quickPick.items = initialItems;
    quickPick.onDidChangeValue((value) => {
      quickPick.items = onSearch(value);
    });
    quickPick.onDidAccept(() => {
      const selected = quickPick.selectedItems[0];
      if (selected) {
        onAccept(selected);
      }
      quickPick.hide();
    });
    quickPick.onDidHide(() => quickPick.dispose());
    quickPick.show();
  }
}
