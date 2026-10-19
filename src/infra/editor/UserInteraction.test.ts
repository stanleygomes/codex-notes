import { describe, expect, it, vi } from 'vitest';
import {
  Uri,
  buildTextEditor,
  commands,
  quickPicks,
  window,
  workspace,
} from '../../test/unit/mocks/vscode';
import { UserInteraction } from './UserInteraction';

const ui = new UserInteraction();

describe('UserInteraction', () => {
  it('forwards input boxes', async () => {
    window.showInputBox.mockResolvedValue('typed');

    await expect(ui.showInputBox({ prompt: 'p' })).resolves.toBe('typed');
    expect(window.showInputBox).toHaveBeenCalledWith({ prompt: 'p' });
  });

  it.each([
    ['Yes', true],
    ['No', false],
    [undefined, false],
  ])('confirmation answered %o resolves %s', async (answer, expected) => {
    window.showWarningMessage.mockResolvedValue(answer);

    await expect(ui.showConfirmation('Sure?')).resolves.toBe(expected);
    expect(window.showWarningMessage).toHaveBeenCalledWith(
      'Sure?',
      'Yes',
      'No',
    );
  });

  it('forwards quick picks', async () => {
    const item = { label: 'a' };
    window.showQuickPick.mockResolvedValue(item);

    await expect(ui.showQuickPick([item], {})).resolves.toBe(item);
  });

  it('shows errors and info messages', async () => {
    window.showInformationMessage.mockResolvedValue('Ok');

    ui.showError('bad');

    expect(window.showErrorMessage).toHaveBeenCalledWith('bad');
    await expect(ui.showInfo('hi', 'Ok')).resolves.toBe('Ok');
    expect(window.showInformationMessage).toHaveBeenCalledWith('hi', 'Ok');
  });

  it('executes commands', async () => {
    commands.executeCommand.mockResolvedValue(42);

    await expect(ui.executeCommand('cmd', 1, 2)).resolves.toBe(42);
    expect(commands.executeCommand).toHaveBeenCalledWith('cmd', 1, 2);
  });

  it('forwards open and save dialogs', async () => {
    const uri = Uri.file('/a');
    window.showOpenDialog.mockResolvedValue([uri]);
    window.showSaveDialog.mockResolvedValue(uri);

    await expect(ui.showOpenDialog({})).resolves.toEqual([uri]);
    await expect(ui.showSaveDialog({})).resolves.toBe(uri);
  });

  it('opens a text document in the editor', async () => {
    await ui.openTextDocument('/notes/a.md');

    expect(workspace.openTextDocument).toHaveBeenCalledWith(
      Uri.file('/notes/a.md'),
    );
    expect(window.showTextDocument).toHaveBeenCalledWith({
      uri: Uri.file('/notes/a.md'),
    });
  });

  it.each([false, true])(
    'opens folders (new window: %s)',
    async (newWindow) => {
      await ui.openFolder('/project', newWindow);

      expect(commands.executeCommand).toHaveBeenCalledWith(
        'vscode.openFolder',
        Uri.file('/project'),
        newWindow,
      );
    },
  );

  it('opens folders in the same window by default', async () => {
    await ui.openFolder('/project');

    expect(commands.executeCommand).toHaveBeenCalledWith(
      'vscode.openFolder',
      Uri.file('/project'),
      false,
    );
  });

  it('reveals files in the OS', async () => {
    await ui.revealFileInOS('/notes/a.md');

    expect(commands.executeCommand).toHaveBeenCalledWith(
      'revealFileInOS',
      Uri.file('/notes/a.md'),
    );
  });

  describe('reopenNote', () => {
    it('closes the old editor and opens the renamed markdown file', async () => {
      window.visibleTextEditors = [
        buildTextEditor('/notes/other.md'),
        buildTextEditor('/notes/old.md'),
      ];

      await ui.reopenNote('/notes/old.md', '/notes/new.md');

      expect(commands.executeCommand).toHaveBeenCalledOnce();
      expect(commands.executeCommand).toHaveBeenCalledWith(
        'workbench.action.closeActiveEditor',
      );
      expect(workspace.openTextDocument).toHaveBeenCalledWith(
        Uri.file('/notes/new.md'),
      );
    });

    it('opens .txt files even when the old one was not visible', async () => {
      await ui.reopenNote('/notes/old.txt', '/notes/new.TXT');

      expect(commands.executeCommand).not.toHaveBeenCalled();
      expect(window.showTextDocument).toHaveBeenCalledOnce();
    });

    it('does not open other file types', async () => {
      await ui.reopenNote('/notes/old.json', '/notes/new.json');

      expect(workspace.openTextDocument).not.toHaveBeenCalled();
    });
  });

  describe('showQuickSearch', () => {
    const items = [{ label: 'one' }, { label: 'two' }];

    function open() {
      const onSearch = vi.fn(() => [items[1]]);
      const onAccept = vi.fn();
      ui.showQuickSearch('Search...', items, onSearch, onAccept);
      return { quickPick: quickPicks[0], onSearch, onAccept };
    }

    it('shows the initial items', () => {
      const { quickPick } = open();

      expect(quickPick.placeholder).toBe('Search...');
      expect(quickPick.items).toBe(items);
      expect(quickPick.show).toHaveBeenCalledOnce();
    });

    it('updates the items while typing', () => {
      const { quickPick, onSearch } = open();

      quickPick.type('tw');

      expect(onSearch).toHaveBeenCalledWith('tw');
      expect(quickPick.items).toEqual([items[1]]);
    });

    it('accepts the selection, hides and disposes', () => {
      const { quickPick, onAccept } = open();
      quickPick.selectedItems = [items[0]];

      quickPick.accept();

      expect(onAccept).toHaveBeenCalledWith(items[0]);
      expect(quickPick.hide).toHaveBeenCalledOnce();
      expect(quickPick.dispose).toHaveBeenCalledOnce();
    });

    it('hides without accepting when nothing is selected', () => {
      const { quickPick, onAccept } = open();

      quickPick.accept();

      expect(onAccept).not.toHaveBeenCalled();
      expect(quickPick.hide).toHaveBeenCalledOnce();
    });
  });
});
