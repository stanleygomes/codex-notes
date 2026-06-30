import * as vscode from 'vscode';
import { TemplateHelper } from '../../core/helpers/TemplateHelper';

export class HtmlTemplateBuilder {
  public static build(
    webview: vscode.Webview,
    extensionUri: vscode.Uri,
    templateName: string = 'index',
    extraVariables: Record<string, string> = {},
  ): string {
    const nonce = this.generateNonce();

    const style = TemplateHelper.render(extensionUri, [
      'src',
      'infra',
      'view',
      'style',
      'main.css',
    ]);

    return TemplateHelper.render(
      extensionUri,
      ['src', 'infra', 'view', 'html', `${templateName}.html`],
      {
        nonce: nonce,
        style: style,
        ...extraVariables,
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
