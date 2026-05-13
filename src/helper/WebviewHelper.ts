import * as vscode from 'vscode';
import { TemplateHelper } from './TemplateHelper';

export class WebviewHelper {
  public static getHtml(
    webview: vscode.Webview,
    extensionUri: vscode.Uri,
  ): string {
    const nonce = this.generateNonce();

    // Load CSS first
    const style = TemplateHelper.render(extensionUri, [
      'resources',
      'templates',
      'style',
      'main.css',
    ]);

    // Render HTML with nonce and the loaded CSS
    return TemplateHelper.render(
      extensionUri,
      ['resources', 'templates', 'html', 'index.html'],
      {
        nonce: nonce,
        style: style,
      },
    );
  }

  private static generateNonce(): string {
    let text = '';
    const possible =
      'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    for (let i = 0; i < 32; i++) {
      text += possible.charAt(Math.floor(Math.random() * possible.length));
    }
    return text;
  }
}
