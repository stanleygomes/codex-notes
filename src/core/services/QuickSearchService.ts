import * as os from 'os';
import * as path from 'path';
import { SearchNoteService } from './SearchNoteService';
import { NoteRepository } from '../repositories/NoteRepository';
import { Note } from '../dtos/Note';
import { UserInteraction } from '../../infra/editor/UserInteraction';

export class QuickSearchService {
  constructor(
    private readonly repository: NoteRepository,
    private readonly searchService: SearchNoteService,
    private readonly onOpenNote: (note: Note) => void,
    private readonly userInteraction: UserInteraction,
  ) {}

  async show(): Promise<void> {
    this.userInteraction.showQuickSearch(
      'Search Codex Notes...',
      this.getNoteItems(this.repository.findAll()),
      (value) => this.getNoteItems(this.searchService.search(value)),
      (selectedItem) => {
        const item = selectedItem as NoteQuickPickItem;
        this.onOpenNote(item.note);
      },
    );
  }

  private getNoteItems(notes: Note[]): NoteQuickPickItem[] {
    return notes.map((note) => ({
      label: `$(markdown) ${note.title}`,
      description: this.toDisplayPath(note.filePath),
      note: note,
    }));
  }

  private toDisplayPath(filePath: string): string {
    const home = os.homedir();
    const isInsideHome =
      home !== '' &&
      (filePath === home || filePath.startsWith(`${home}${path.sep}`));
    return isInsideHome ? `~${filePath.slice(home.length)}` : filePath;
  }
}

interface NoteQuickPickItem {
  label: string;
  description: string;
  note: Note;
}
