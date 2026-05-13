import * as vscode from 'vscode';
import { Note } from '../dto/Note';
import { NOTE_COLOR_HEX } from '../enum/NoteColorEnum';
import { SortTypeEnum } from '../enum/SortTypeEnum';
import { DateFilterEnum } from '../enum/DateFilterEnum';
import { NoteRepository } from '../repository/NoteRepository';
import { SearchNoteService } from '../service/SearchNoteService';
import { SortNotesService } from '../service/SortNotesService';
import { FilterNotesService } from '../service/FilterNotesService';
import { DateHelper } from '../helper/DateHelper';
import { FileHelper } from '../helper/FileHelper';
import { WebviewHelper } from '../helper/WebviewHelper';

interface WebviewMessage {
  command: string;
  noteId?: string;
  query?: string;
  sortType?: SortTypeEnum;
  dateFilter?: DateFilterEnum;
  filterFavorites?: boolean;
}

export class NotesViewProvider implements vscode.WebviewViewProvider {
  public static readonly viewType = 'codexNotes.notesView';

  private view?: vscode.WebviewView;
  private currentQuery = '';
  private currentSort: SortTypeEnum = SortTypeEnum.DATE;
  private filterFavorites = false;
  private activeDateFilter?: DateFilterEnum;

  constructor(
    private readonly extensionUri: vscode.Uri,
    private readonly repository: NoteRepository,
    private readonly searchService: SearchNoteService,
    private readonly sortService: SortNotesService,
    private readonly filterService: FilterNotesService,
    private readonly onOpenNote: (note: Note) => void,
    private readonly onCreateNote: () => void,
    private readonly onDeleteNote: (note: Note) => void,
    private readonly onRenameNote: (note: Note) => void,
    private readonly onDuplicateNote: (note: Note) => void,
    private readonly onToggleFavorite: (note: Note) => void,
    private readonly onChangeColor: (note: Note) => void,
    private readonly onExportNotes: () => void,
    private readonly onImportNotes: () => void,
    private readonly onOpenLocation: (note: Note) => void,
  ) {}

  resolveWebviewView(
    webviewView: vscode.WebviewView,
    _context: vscode.WebviewViewResolveContext,
    _token: vscode.CancellationToken,
  ): void {
    this.view = webviewView;

    webviewView.webview.options = {
      enableScripts: true,
      localResourceRoots: [this.extensionUri],
    };

    webviewView.webview.html = WebviewHelper.getHtml(
      webviewView.webview,
      this.extensionUri,
    );

    webviewView.webview.onDidReceiveMessage((message: WebviewMessage) => {
      this.handleMessage(message);
    });

    webviewView.onDidChangeVisibility(() => {
      if (webviewView.visible) {
        this.refresh();
      }
    });

    this.refresh();
  }

  refresh(): void {
    if (!this.view) {
      return;
    }

    const notes = this.getFilteredAndSortedNotes();
    this.view.webview.postMessage({
      command: 'updateNotes',
      notes: this.serializeNotes(notes),
    });
  }

  private getFilteredAndSortedNotes(): Note[] {
    let notes: Note[];

    if (this.currentQuery.trim()) {
      notes = this.searchService.search(this.currentQuery);
    } else {
      notes = this.repository.getAllNotes();
    }

    if (this.filterFavorites) {
      notes = this.filterService.filterByFavorite(notes);
    }

    if (this.activeDateFilter) {
      notes = this.filterService.filterByDateRange(
        notes,
        this.activeDateFilter,
      );
    }

    return this.sortService.sort(notes, this.currentSort);
  }

  private serializeNotes(notes: Note[]): object[] {
    return notes.map((note) => ({
      id: note.id,
      title: note.title,
      preview: this.getPreview(note),
      dateLabel: DateHelper.toHumanRelative(note.updatedAt),
      isFavorite: note.isFavorite,
      color: note.color,
      colorHex: NOTE_COLOR_HEX[note.color],
    }));
  }

  private getPreview(note: Note): string {
    const content = FileHelper.readText(note.filePath);
    const plainText = content
      .replace(/```[\s\S]*?```/g, '')
      .replace(/`[^`]*`/g, '')
      .replace(/!\[.*?\]\(.*?\)/g, '')
      .replace(/\[.*?\]\(.*?\)/g, '')
      .replace(/#{1,6}\s+/g, '')
      .replace(/(\*\*|__)(.*?)\1/g, '$2')
      .replace(/(\*|_)(.*?)\1/g, '$2')
      .replace(/~~(.*?)~~/g, '$1')
      .replace(/^[-*+]\s+/gm, '')
      .replace(/^\d+\.\s+/gm, '')
      .replace(/^>\s+/gm, '')
      .replace(/[-_*]{3,}/g, '')
      .replace(/\s+/g, ' ')
      .trim();
    return plainText.slice(0, 80);
  }

  private handleMessage(message: WebviewMessage): void {
    const note = message.noteId
      ? this.repository.getNoteById(message.noteId)
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
      case 'exportNotes':
        this.onExportNotes();
        break;
      case 'importNotes':
        this.onImportNotes();
        break;
      case 'openLocation':
        if (note) {
          this.onOpenLocation(note);
        }
        break;
      case 'search':
        this.currentQuery = message.query ?? '';
        this.refresh();
        break;
      case 'sort':
        if (message.sortType) {
          this.currentSort = message.sortType;
        }
        this.refresh();
        break;
      case 'filterFavorites':
        this.filterFavorites = message.filterFavorites ?? false;
        this.refresh();
        break;
      case 'filterDate':
        this.activeDateFilter = message.dateFilter;
        this.refresh();
        break;
    }
  }
}
