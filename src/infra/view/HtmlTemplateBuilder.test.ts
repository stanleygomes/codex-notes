import { describe, expect, it } from 'vitest';
import { Uri } from '../../test/unit/mocks/vscode';
import { HtmlTemplateBuilder } from './HtmlTemplateBuilder';

function build(extra: Record<string, string> = {}): string {
  return HtmlTemplateBuilder.build(
    {} as never,
    Uri.file(process.cwd()) as never,
    'index',
    extra,
  );
}

describe('HtmlTemplateBuilder', () => {
  it('inlines templates, styles and scripts without leftover placeholders', () => {
    const html = build();

    expect(html).toContain('<!doctype html>');
    expect(html).toContain('.context-menu');
    expect(html).toContain('function showContextMenu');
    expect(html).toContain('function calculateMenuCoordinate');
    expect(html).not.toMatch(/\{\{\w+\}\}/);
    expect(html).not.toContain('Template not found');
  });

  it('uses a fresh 32 char nonce for every build', () => {
    const nonceOf = (html: string) => html.match(/nonce="(\w+)"/)?.[1];

    const first = nonceOf(build());
    const second = nonceOf(build());

    expect(first).toMatch(/^[A-Za-z0-9]{32}$/);
    expect(second).not.toBe(first);
  });
});
