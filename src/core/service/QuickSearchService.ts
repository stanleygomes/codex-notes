import * as vscode from 'vscode';
import { SearchNoteService } from './SearchNoteService';
import { NoteRepository } from '../repository/NoteRepository';
import { Note } from '../dto/Note';

export class QuickSearchService {
  constructor(
    private readonly repository: NoteRepository,
    private readonly searchService: SearchNoteService,
    private readonly onOpenNote: (note: Note) => void,
  ) {}

  async show(): Promise<void> {
    const quickPick = vscode.window.createQuickPick();
    quickPick.placeholder = 'Search Codex Notes...';
    quickPick.items = this.getNoteItems(this.repository.findAll());

    quickPick.onDidChangeValue((value) => {
      const filteredNotes = this.searchService.search(value);
      quickPick.items = this.getNoteItems(filteredNotes);
    });

    quickPick.onDidAccept(() => {
      const selectedItem = quickPick.selectedItems[0] as NoteQuickPickItem;
      if (selectedItem) {
        this.onOpenNote(selectedItem.note);
      }
      quickPick.hide();
    });

    quickPick.onDidHide(() => quickPick.dispose());
    quickPick.show();
  }

  private getNoteItems(notes: Note[]): NoteQuickPickItem[] {
    return notes.map((note) => ({
      label: `$(markdown) ${note.title}`,
      description: note.filePath.replace(process.env.HOME || '', '~'),
      note: note,
    }));
  }
}

interface NoteQuickPickItem extends vscode.QuickPickItem {
  note: Note;
}
