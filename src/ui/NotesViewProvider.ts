import * as vscode from 'vscode';
import { Note } from '../dto/Note';
import { NoteColorEnum, NOTE_COLOR_HEX } from '../enum/NoteColorEnum';
import { SortTypeEnum } from '../enum/SortTypeEnum';
import { DateFilterEnum } from '../enum/DateFilterEnum';
import { NoteRepository } from '../repository/NoteRepository';
import { SearchNoteService } from '../service/SearchNoteService';
import { SortNotesService } from '../service/SortNotesService';
import { FilterNotesService } from '../service/FilterNotesService';
import { DateHelper } from '../helper/DateHelper';
import { FileHelper } from '../helper/FileHelper';

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

    webviewView.webview.html = this.getHtml(webviewView.webview);

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
    this.view.webview.postMessage({ command: 'updateNotes', notes: this.serializeNotes(notes) });
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
      notes = this.filterService.filterByDateRange(notes, this.activeDateFilter);
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
    const note = message.noteId ? this.repository.getNoteById(message.noteId) : undefined;

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

  private getHtml(webview: vscode.Webview): string {
    const nonce = this.generateNonce();
    return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="Content-Security-Policy" content="default-src 'none'; style-src 'nonce-${nonce}'; script-src 'nonce-${nonce}';">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Codex Notes</title>
  <style nonce="${nonce}">
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body {
      font-family: var(--vscode-font-family);
      font-size: var(--vscode-font-size);
      color: var(--vscode-foreground);
      background: var(--vscode-sideBar-background);
      padding: 0;
    }
    .toolbar {
      display: flex;
      align-items: center;
      gap: 4px;
      padding: 6px 8px;
      border-bottom: 1px solid var(--vscode-sideBarSectionHeader-border);
      flex-wrap: wrap;
    }
    .search-box {
      flex: 1;
      min-width: 80px;
      background: var(--vscode-input-background);
      color: var(--vscode-input-foreground);
      border: 1px solid var(--vscode-input-border);
      border-radius: 3px;
      padding: 3px 6px;
      font-size: 12px;
      outline: none;
    }
    .search-box:focus {
      border-color: var(--vscode-focusBorder);
    }
    .btn {
      background: transparent;
      border: none;
      cursor: pointer;
      color: var(--vscode-icon-foreground);
      padding: 3px 5px;
      border-radius: 3px;
      font-size: 13px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .btn:hover {
      background: var(--vscode-toolbar-hoverBackground);
    }
    .btn.active {
      color: var(--vscode-button-background);
    }
    .filter-row {
      display: flex;
      gap: 4px;
      padding: 4px 8px;
      border-bottom: 1px solid var(--vscode-sideBarSectionHeader-border);
      flex-wrap: wrap;
    }
    .filter-chip {
      font-size: 11px;
      padding: 2px 7px;
      border-radius: 10px;
      border: 1px solid var(--vscode-button-border, #aaa);
      background: transparent;
      color: var(--vscode-foreground);
      cursor: pointer;
    }
    .filter-chip.active {
      background: var(--vscode-button-background);
      color: var(--vscode-button-foreground);
      border-color: var(--vscode-button-background);
    }
    .notes-list {
      overflow-y: auto;
    }
    .note-item {
      display: flex;
      align-items: stretch;
      cursor: pointer;
      border-bottom: 1px solid var(--vscode-sideBarSectionHeader-border);
      position: relative;
    }
    .note-item:hover {
      background: var(--vscode-list-hoverBackground);
    }
    .note-color-bar {
      width: 4px;
      flex-shrink: 0;
    }
    .note-color-none {
      background: transparent;
    }
    .note-color-blue {
      background: #ADE8E6;
    }
    .note-color-green {
      background: #90EE90;
    }
    .note-color-yellow {
      background: #FFFF99;
    }
    .note-color-orange {
      background: #FFDAB9;
    }
    .note-color-pink {
      background: #FFB6C1;
    }
    .note-color-red {
      background: #FF6666;
    }
    .note-color-purple {
      background: #CC99FF;
    }
    .note-body {
      flex: 1;
      padding: 8px 8px 8px 6px;
      min-width: 0;
    }
    .note-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 4px;
    }
    .note-title {
      font-weight: 600;
      font-size: 13px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      flex: 1;
    }
    .note-meta {
      display: flex;
      align-items: center;
      gap: 4px;
      flex-shrink: 0;
    }
    .note-date {
      font-size: 11px;
      color: var(--vscode-descriptionForeground);
      white-space: nowrap;
    }
    .note-favorite {
      font-size: 12px;
      color: var(--vscode-charts-yellow, #e5c07b);
    }
    .note-preview {
      font-size: 12px;
      color: var(--vscode-descriptionForeground);
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      margin-top: 2px;
    }
    .context-menu {
      position: fixed;
      background: var(--vscode-menu-background);
      border: 1px solid var(--vscode-menu-border);
      border-radius: 4px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.3);
      z-index: 1000;
      min-width: 160px;
    }
    .context-menu-item {
      padding: 6px 14px;
      cursor: pointer;
      font-size: 13px;
      color: var(--vscode-menu-foreground);
    }
    .context-menu-item:hover {
      background: var(--vscode-menu-selectionBackground);
      color: var(--vscode-menu-selectionForeground);
    }
    .context-menu-separator {
      height: 1px;
      background: var(--vscode-menu-separatorBackground);
      margin: 3px 0;
    }
    .empty-state {
      padding: 24px 16px;
      text-align: center;
      color: var(--vscode-descriptionForeground);
      font-size: 13px;
    }
    .sort-select {
      background: var(--vscode-input-background);
      color: var(--vscode-input-foreground);
      border: 1px solid var(--vscode-input-border);
      border-radius: 3px;
      padding: 2px 4px;
      font-size: 11px;
      outline: none;
      cursor: pointer;
    }
  </style>
</head>
<body>
  <div class="toolbar">
    <input class="search-box" id="searchBox" type="text" placeholder="Search notes..." />
    <button class="btn" id="btnCreate" title="New Note">＋</button>
    <button class="btn" id="btnImport" title="Import Notes">⬆</button>
    <button class="btn" id="btnExport" title="Export Notes">⬇</button>
  </div>
  <div class="filter-row">
    <select class="sort-select" id="sortSelect">
      <option value="DATE">Date</option>
      <option value="TITLE">Title</option>
      <option value="FAVORITE">Favorite</option>
    </select>
    <button class="filter-chip" id="filterFav" data-active="false">★ Favorites</button>
    <button class="filter-chip" id="filterToday" data-filter="TODAY">Today</button>
    <button class="filter-chip" id="filterWeek" data-filter="THIS_WEEK">Week</button>
    <button class="filter-chip" id="filterMonth" data-filter="THIS_MONTH">Month</button>
  </div>
  <div class="notes-list" id="notesList"></div>
  <div class="context-menu" id="contextMenu" style="display:none"></div>
  <script nonce="${nonce}">
    const vscode = acquireVsCodeApi();
    let currentNoteId = null;
    let activeDateFilter = null;
    let filterFavorites = false;

    const searchBox = document.getElementById('searchBox');
    const notesList = document.getElementById('notesList');
    const contextMenu = document.getElementById('contextMenu');
    const sortSelect = document.getElementById('sortSelect');
    const filterFavBtn = document.getElementById('filterFav');

    searchBox.addEventListener('input', () => {
      vscode.postMessage({ command: 'search', query: searchBox.value });
    });

    document.getElementById('btnCreate').addEventListener('click', () => {
      vscode.postMessage({ command: 'createNote' });
    });

    document.getElementById('btnImport').addEventListener('click', () => {
      vscode.postMessage({ command: 'importNotes' });
    });

    document.getElementById('btnExport').addEventListener('click', () => {
      vscode.postMessage({ command: 'exportNotes' });
    });

    sortSelect.addEventListener('change', () => {
      vscode.postMessage({ command: 'sort', sortType: sortSelect.value });
    });

    filterFavBtn.addEventListener('click', () => {
      filterFavorites = !filterFavorites;
      filterFavBtn.classList.toggle('active', filterFavorites);
      vscode.postMessage({ command: 'filterFavorites', filterFavorites });
    });

    document.querySelectorAll('.filter-chip[data-filter]').forEach(btn => {
      btn.addEventListener('click', () => {
        const filter = btn.getAttribute('data-filter');
        if (activeDateFilter === filter) {
          activeDateFilter = null;
          btn.classList.remove('active');
          vscode.postMessage({ command: 'filterDate', dateFilter: null });
        } else {
          document.querySelectorAll('.filter-chip[data-filter]').forEach(b => b.classList.remove('active'));
          activeDateFilter = filter;
          btn.classList.add('active');
          vscode.postMessage({ command: 'filterDate', dateFilter: filter });
        }
      });
    });

    document.addEventListener('click', (e) => {
      if (!contextMenu.contains(e.target)) {
        contextMenu.style.display = 'none';
      }
    });

    function renderNotes(notes) {
      if (notes.length === 0) {
        notesList.innerHTML = '<div class="empty-state">No notes yet.<br>Click + to create one.</div>';
        return;
      }
      notesList.innerHTML = notes.map(note => {
        const colorClass = \`note-color-\${String(note.color || 'NONE').toLowerCase()}\`;
        const colorBar = \`<div class="note-color-bar \${escapeHtml(colorClass)}"></div>\`;
        const favIcon = note.isFavorite ? '<span class="note-favorite" title="Favorite">★</span>' : '';
        return \`
          <div class="note-item" data-id="\${note.id}">
            \${colorBar}
            <div class="note-body">
              <div class="note-header">
                <span class="note-title">\${escapeHtml(note.title)}</span>
                <div class="note-meta">
                  \${favIcon}
                  <span class="note-date">\${note.dateLabel}</span>
                </div>
              </div>
              \${note.preview ? \`<div class="note-preview">\${escapeHtml(note.preview)}</div>\` : ''}
            </div>
          </div>
        \`;
      }).join('');

      notesList.querySelectorAll('.note-item').forEach(item => {
        item.addEventListener('click', (e) => {
          vscode.postMessage({ command: 'openNote', noteId: item.dataset.id });
        });
        item.addEventListener('contextmenu', (e) => {
          e.preventDefault();
          showContextMenu(e.clientX, e.clientY, item.dataset.id);
        });
      });
    }

    function showContextMenu(x, y, noteId) {
      currentNoteId = noteId;
      contextMenu.innerHTML = \`
        <div class="context-menu-item" data-action="openNote">Open Note</div>
        <div class="context-menu-separator"></div>
        <div class="context-menu-item" data-action="renameNote">Rename</div>
        <div class="context-menu-item" data-action="duplicateNote">Duplicate</div>
        <div class="context-menu-item" data-action="toggleFavorite">Toggle Favorite</div>
        <div class="context-menu-item" data-action="changeColor">Change Color</div>
        <div class="context-menu-separator"></div>
        <div class="context-menu-item" data-action="openLocation">Open File Location</div>
        <div class="context-menu-separator"></div>
        <div class="context-menu-item" data-action="deleteNote">Delete</div>
      \`;
      contextMenu.style.left = x + 'px';
      contextMenu.style.top = y + 'px';
      contextMenu.style.display = 'block';

      contextMenu.querySelectorAll('.context-menu-item').forEach(item => {
        item.addEventListener('click', () => {
          const action = item.getAttribute('data-action');
          vscode.postMessage({ command: action, noteId: currentNoteId });
          contextMenu.style.display = 'none';
        });
      });
    }

    function escapeHtml(str) {
      return str.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/"/g,'&quot;');
    }

    window.addEventListener('message', event => {
      const message = event.data;
      if (message.command === 'updateNotes') {
        renderNotes(message.notes);
      }
    });
  </script>
</body>
</html>`;
  }

  private generateNonce(): string {
    let text = '';
    const possible = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    for (let i = 0; i < 32; i++) {
      text += possible.charAt(Math.floor(Math.random() * possible.length));
    }
    return text;
  }
}
