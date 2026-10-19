import { vi } from 'vitest';
import type { WebviewView } from 'vscode';

export function buildWebviewView() {
  const listeners = {
    message: (_message: unknown) => {},
    visibility: () => {},
  };
  const fake = {
    visible: true,
    webview: {
      options: {},
      html: '',
      postMessage: vi.fn(),
      onDidReceiveMessage: vi.fn((listener: (message: unknown) => void) => {
        listeners.message = listener;
      }),
    },
    onDidChangeVisibility: vi.fn((listener: () => void) => {
      listeners.visibility = listener;
    }),
  };
  return {
    fake,
    webviewView: fake as unknown as WebviewView,
    send: (message: unknown) => listeners.message(message),
    changeVisibility: (visible: boolean) => {
      fake.visible = visible;
      listeners.visibility();
    },
  };
}
