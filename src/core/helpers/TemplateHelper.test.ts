import * as os from 'node:os';
import { describe, expect, it, vi } from 'vitest';
import { Uri } from '../../test/unit/mocks/vscode';
import { FileHelper } from './FileHelper';
import { TemplateHelper } from './TemplateHelper';

describe('TemplateHelper', () => {
  it('renders the template replacing every variable', () => {
    FileHelper.createFileWithContent(
      FileHelper.buildPath(os.homedir(), 'tpl'),
      'page.html',
      '<p nonce="{{nonce}}">{{body}} {{nonce}}</p>',
    );

    const html = TemplateHelper.render(
      Uri.file(os.homedir()) as never,
      ['tpl', 'page.html'],
      { nonce: 'abc', body: 'Hi' },
    );

    expect(html).toBe('<p nonce="abc">Hi abc</p>');
  });

  it('returns an html comment when the template is missing', () => {
    vi.spyOn(console, 'error').mockImplementation(() => {});

    const html = TemplateHelper.render(Uri.file('/nowhere') as never, [
      'missing.html',
    ]);

    expect(html).toBe(
      '<!-- Error: Template not found at /nowhere/missing.html -->',
    );
    expect(console.error).toHaveBeenCalledOnce();
  });
});
