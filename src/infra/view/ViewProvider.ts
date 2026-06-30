import * as vscode from 'vscode';
import { Note } from '../../core/dtos/Note';
import { NoteRepository } from '../../core/repositories/NoteRepository';
import { SearchNoteService } from '../../core/services/SearchNoteService';
import { SortNotesService } from '../../core/services/SortNotesService';
import { FilterNotesService } from '../../core/services/FilterNotesService';
import { HtmlTemplateBuilder } from './HtmlTemplateBuilder';
import { ViewState } from './ViewState';
import { ViewMessageHandler } from './ViewMessageHandler';
import { WebviewMessage } from '../../core/dtos/WebviewMessage';

export class ViewProvider implements vscode.WebviewViewProvider {
  public static readonly viewType = 'codexNotes.notesView';

  private view?: vscode.WebviewView;
  private readonly viewState: ViewState;
  private readonly messageHandler: ViewMessageHandler;

  constructor(
    private readonly extensionUri: vscode.Uri,
    repository: NoteRepository,
    searchService: SearchNoteService,
    sortService: SortNotesService,
    filterService: FilterNotesService,
    onOpenNote: (note: Note) => void,
    onCreateNote: () => void,
    onDeleteNote: (note: Note) => void,
    onRenameNote: (note: Note) => void,
    onDuplicateNote: (note: Note) => void,
    onToggleFavorite: (note: Note) => void,
    onChangeColor: (note: Note) => void,
    onOpenLocation: (note: Note) => void,
  ) {
    this.viewState = new ViewState(
      repository,
      searchService,
      sortService,
      filterService,
    );
    this.messageHandler = new ViewMessageHandler(
      repository,
      this.viewState,
      () => this.refresh(),
      onOpenNote,
      onCreateNote,
      onDeleteNote,
      onRenameNote,
      onDuplicateNote,
      onToggleFavorite,
      onChangeColor,
      onOpenLocation,
    );
  }

  public resolveWebviewView(
    webviewView: vscode.WebviewView,
    _context: vscode.WebviewViewResolveContext,
    _token: vscode.CancellationToken,
  ): void {
    this.view = webviewView;

    webviewView.webview.options = {
      enableScripts: true,
      localResourceRoots: [this.extensionUri],
    };

    webviewView.webview.html = HtmlTemplateBuilder.build(
      webviewView.webview,
      this.extensionUri,
    );

    webviewView.webview.onDidReceiveMessage((message: WebviewMessage) => {
      this.messageHandler.handle(message);
    });

    webviewView.onDidChangeVisibility(() => {
      if (webviewView.visible) {
        this.refresh();
      }
    });

    this.refresh();
  }

  public refresh(): void {
    if (this.view) {
      this.view.webview.postMessage(this.viewState.getPayload());
    }
  }
}
