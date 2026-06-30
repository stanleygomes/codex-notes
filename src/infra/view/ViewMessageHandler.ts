import { WebviewMessage } from '../../core/dtos/WebviewMessage';
import { Note } from '../../core/dtos/Note';
import { NoteRepository } from '../../core/repositories/NoteRepository';
import { ViewState } from './ViewState';

export class ViewMessageHandler {
  constructor(
    private readonly repository: NoteRepository,
    private readonly viewState: ViewState,
    private readonly refreshCallback: () => void,
    private readonly onOpenNote: (note: Note) => void,
    private readonly onCreateNote: () => void,
    private readonly onDeleteNote: (note: Note) => void,
    private readonly onRenameNote: (note: Note) => void,
    private readonly onDuplicateNote: (note: Note) => void,
    private readonly onToggleFavorite: (note: Note) => void,
    private readonly onChangeColor: (note: Note) => void,
    private readonly onOpenLocation: (note: Note) => void,
  ) {}

  public async handle(message: WebviewMessage): Promise<void> {
    const note = message.noteId
      ? this.repository.findOne(message.noteId)
      : undefined;

    switch (message.command) {
      case 'openNote':
        if (note) {
          this.onOpenNote(note);
        }
        break;
      case 'createNote':
        this.onCreateNote();
        break;
      case 'deleteNote':
        if (note) {
          this.onDeleteNote(note);
        }
        break;
      case 'renameNote':
        if (note) {
          this.onRenameNote(note);
        }
        break;
      case 'duplicateNote':
        if (note) {
          this.onDuplicateNote(note);
        }
        break;
      case 'toggleFavorite':
        if (note) {
          this.onToggleFavorite(note);
        }
        break;
      case 'changeColor':
        if (note) {
          this.onChangeColor(note);
        }
        break;
      case 'openLocation':
        if (note) {
          this.onOpenLocation(note);
        }
        break;
      case 'search':
        this.viewState.currentQuery = message.query ?? '';
        this.refreshCallback();
        break;
      case 'sort':
        if (message.sortType) {
          this.viewState.currentSort = message.sortType;
        }
        this.refreshCallback();
        break;
      case 'filterFavorites':
        this.viewState.filterFavorites = message.filterFavorites ?? false;
        this.refreshCallback();
        break;
      case 'filterDate':
        this.viewState.activeDateFilter = message.dateFilter;
        this.refreshCallback();
        break;
    }
  }
}
