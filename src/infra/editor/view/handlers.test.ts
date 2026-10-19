import { describe, expect, it, vi } from 'vitest';
import { buildNote, buildProvider } from '../../../test/unit/fakes';
import {
  Uri,
  buildTextEditor,
  window,
  workspace,
} from '../../../test/unit/mocks/vscode';
import {
  createHandleChangeColor,
  createHandleCreate,
  createHandleCreateFromSelection,
  createHandleDelete,
  createHandleDuplicate,
  createHandleRename,
  openNote,
} from './index';

const note = buildNote();

describe('editor view handlers', () => {
  it('creates a note and refreshes', async () => {
    const service = { create: vi.fn() };
    const { fake, provider } = buildProvider();

    await createHandleCreate(service as never, provider)();

    expect(service.create).toHaveBeenCalledWith();
    expect(fake.refresh).toHaveBeenCalledOnce();
  });

  it('creates a note from the editor selection', async () => {
    window.activeTextEditor = buildTextEditor('/a.ts', 'selected');
    const service = { create: vi.fn() };
    const { fake, provider } = buildProvider();

    await createHandleCreateFromSelection(service as never, provider)();

    expect(service.create).toHaveBeenCalledWith('selected');
    expect(fake.refresh).toHaveBeenCalledOnce();
  });

  it('creates an empty note when there is no active editor', async () => {
    const service = { create: vi.fn() };
    const { provider } = buildProvider();

    await createHandleCreateFromSelection(service as never, provider)();

    expect(service.create).toHaveBeenCalledWith('');
  });

  it.each([
    [true, 1],
    [false, 0],
  ])('delete confirmed=%s refreshes %d times', async (deleted, refreshes) => {
    const service = { confirmAndDelete: vi.fn().mockResolvedValue(deleted) };
    const { fake, provider } = buildProvider();

    await createHandleDelete(service as never, provider)(note);

    expect(service.confirmAndDelete).toHaveBeenCalledWith([note]);
    expect(fake.refresh).toHaveBeenCalledTimes(refreshes);
  });

  it.each([
    [note, 1],
    [undefined, 0],
  ])('rename result %o refreshes %d times', async (renamed, refreshes) => {
    const service = { rename: vi.fn().mockResolvedValue(renamed) };
    const { fake, provider } = buildProvider();

    await createHandleRename(service as never, provider)(note);

    expect(service.rename).toHaveBeenCalledWith(note);
    expect(fake.refresh).toHaveBeenCalledTimes(refreshes);
  });

  it('duplicates a note and refreshes', async () => {
    const service = { duplicate: vi.fn() };
    const { fake, provider } = buildProvider();

    await createHandleDuplicate(service as never, provider)(note);

    expect(service.duplicate).toHaveBeenCalledWith(note);
    expect(fake.refresh).toHaveBeenCalledOnce();
  });

  it('changes the color and refreshes', async () => {
    const service = { changeColor: vi.fn() };
    const { fake, provider } = buildProvider();

    await createHandleChangeColor(service as never, provider)(note);

    expect(service.changeColor).toHaveBeenCalledWith(note);
    expect(fake.refresh).toHaveBeenCalledOnce();
  });

  it('opens the note file in the editor', async () => {
    await openNote(note);

    expect(workspace.openTextDocument).toHaveBeenCalledWith(
      Uri.file(note.filePath),
    );
    expect(window.showTextDocument).toHaveBeenCalledWith({
      uri: Uri.file(note.filePath),
    });
  });
});
