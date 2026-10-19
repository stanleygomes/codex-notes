import { vi } from 'vitest';

type Listener<T> = (event: T) => void;

export class Disposable {
  constructor(private readonly callOnDispose: () => void = () => {}) {}

  dispose(): void {
    this.callOnDispose();
  }
}

export class Uri {
  private constructor(public readonly fsPath: string) {}

  static file(path: string): Uri {
    return new Uri(path);
  }

  toString(): string {
    return `file://${this.fsPath}`;
  }
}

export interface QuickPickItem {
  label: string;
  description?: string;
}

export class FakeQuickPick {
  placeholder = '';
  items: QuickPickItem[] = [];
  selectedItems: QuickPickItem[] = [];
  readonly show = vi.fn();
  readonly dispose = vi.fn();
  private changeListener: Listener<string> = () => {};
  private acceptListener: Listener<void> = () => {};
  private hideListener: Listener<void> = () => {};

  readonly hide = vi.fn(() => this.hideListener());

  onDidChangeValue(listener: Listener<string>): Disposable {
    this.changeListener = listener;
    return new Disposable();
  }

  onDidAccept(listener: Listener<void>): Disposable {
    this.acceptListener = listener;
    return new Disposable();
  }

  onDidHide(listener: Listener<void>): Disposable {
    this.hideListener = listener;
    return new Disposable();
  }

  type(value: string): void {
    this.changeListener(value);
  }

  accept(): void {
    this.acceptListener();
  }
}

export const quickPicks: FakeQuickPick[] = [];

interface FakeTextEditor {
  document: { uri: Uri; getText: (selection?: unknown) => string };
  selection?: unknown;
}

export const window = {
  activeTextEditor: undefined as FakeTextEditor | undefined,
  visibleTextEditors: [] as FakeTextEditor[],
  showInputBox: vi.fn(),
  showWarningMessage: vi.fn(),
  showQuickPick: vi.fn(),
  showErrorMessage: vi.fn(),
  showInformationMessage: vi.fn(),
  showOpenDialog: vi.fn(),
  showSaveDialog: vi.fn(),
  showTextDocument: vi.fn(),
  registerWebviewViewProvider: vi.fn(
    (_viewType: string, _provider: unknown) => new Disposable(),
  ),
  createQuickPick: vi.fn(() => {
    const quickPick = new FakeQuickPick();
    quickPicks.push(quickPick);
    return quickPick;
  }),
};

export const workspace = {
  openTextDocument: vi.fn(async (uri: Uri) => ({ uri })),
};

export const commands = {
  registerCommand: vi.fn(
    (_command: string, _callback: (...args: unknown[]) => unknown) =>
      new Disposable(),
  ),
  executeCommand: vi.fn(),
};

export function buildTextEditor(path: string, text = ''): FakeTextEditor {
  return {
    document: { uri: Uri.file(path), getText: () => text },
    selection: {},
  };
}

export function resetVscodeMock(): void {
  window.activeTextEditor = undefined;
  window.visibleTextEditors = [];
  quickPicks.length = 0;
  workspace.openTextDocument.mockImplementation(async (uri: Uri) => ({ uri }));
  window.registerWebviewViewProvider.mockImplementation(() => new Disposable());
  window.createQuickPick.mockImplementation(() => {
    const quickPick = new FakeQuickPick();
    quickPicks.push(quickPick);
    return quickPick;
  });
  commands.registerCommand.mockImplementation(() => new Disposable());
}
