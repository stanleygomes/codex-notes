import * as vscode from 'vscode';
import { WorkspaceRepository } from '../../core/repositories/WorkspaceRepository';
import { EditorContext } from './EditorContext';

export class EditorTheme {
  constructor(private readonly repository: WorkspaceRepository) {}

  public async applyCurrentWorkspaceColor(): Promise<void> {
    const currentId = EditorContext.getCurrentWorkspaceId();
    if (!currentId) return;

    const workspace = this.repository.findOne(currentId);
    if (workspace && workspace.color) {
      await this.applyColor(workspace.color, workspace.textColor);
    }
  }

  public async applyColor(
    color: string | undefined,
    textColor: string | undefined,
  ): Promise<void> {
    const config = vscode.workspace.getConfiguration('workbench');
    const customizations = {
      ...(config.get<any>('colorCustomizations') || {}),
    };

    delete customizations['activityBar.background'];
    delete customizations['activityBar.foreground'];
    delete customizations['activityBar.activeBorder'];
    delete customizations['activityBarBadge.background'];
    delete customizations['titleBar.activeBackground'];
    delete customizations['titleBar.activeForeground'];
    delete customizations['titleBar.activeBorder'];
    delete customizations['statusBar.background'];
    delete customizations['statusBar.foreground'];
    delete customizations['statusBarItem.remoteBackground'];
    delete customizations['statusBarItem.remoteForeground'];
    delete customizations['tab.activeBorder'];

    if (color) {
      customizations['statusBar.background'] = color;
      customizations['statusBar.foreground'] = textColor || '#ffffff';
      customizations['statusBarItem.remoteBackground'] = color;
      customizations['statusBarItem.remoteForeground'] = textColor || '#ffffff';
    }

    await config.update(
      'colorCustomizations',
      customizations,
      vscode.ConfigurationTarget.Workspace,
    );
  }
}
