import * as vscode from 'vscode';
import { FileHelper } from './FileHelper';

export class TemplateHelper {
  /**
   * Renders a template by reading its content and replacing placeholders.
   *
   * @param extensionUri The URI of the extension.
   * @param templatePath Array of path parts relative to the extension root (e.g., ['resources', 'templates', 'html', 'index.html']).
   * @param variables A map of placeholders to their values (e.g., { 'nonce': 'abc' }).
   * @returns The rendered template string.
   */
  public static render(
    extensionUri: vscode.Uri,
    templatePath: string[],
    variables: Record<string, string> = {},
  ): string {
    const fullPath = FileHelper.buildPath(extensionUri.fsPath, ...templatePath);
    let content = FileHelper.readText(fullPath);

    if (!content) {
      console.error(`Template not found or empty: ${fullPath}`);
      return `<!-- Error: Template not found at ${fullPath} -->`;
    }

    for (const [key, value] of Object.entries(variables)) {
      const regex = new RegExp(`{{${key}}}`, 'g');
      content = content.replace(regex, value);
    }

    return content;
  }
}
