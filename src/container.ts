import * as vscode from 'vscode';
import { NoteRepository } from './core/repository/NoteRepository';
import { CreateNoteService } from './core/service/CreateNoteService';
import { DeleteNoteService } from './core/service/DeleteNoteService';
import { RenameNoteService } from './core/service/RenameNoteService';
import { SearchNoteService } from './core/service/SearchNoteService';
import { DuplicateNoteService } from './core/service/DuplicateNoteService';
import { FavoriteNoteService } from './core/service/FavoriteNoteService';
import { ChangeNoteColorService } from './core/service/ChangeNoteColorService';
import { SortNotesService } from './core/service/SortNotesService';
import { FilterNotesService } from './core/service/FilterNotesService';
import { OpenNoteLocationService } from './core/service/OpenNoteLocationService';
import { QuickSearchService } from './core/service/QuickSearchService';
import { openNote } from './infra/editor/view';
import { UserInteraction } from './infra/editor/UserInteraction';

export class Container {
  public readonly repository: NoteRepository;
  public readonly userInteraction: UserInteraction;
  public readonly createService: CreateNoteService;
  public readonly deleteService: DeleteNoteService;
  public readonly renameService: RenameNoteService;
  public readonly searchService: SearchNoteService;
  public readonly duplicateService: DuplicateNoteService;
  public readonly favoriteService: FavoriteNoteService;
  public readonly colorService: ChangeNoteColorService;
  public readonly sortService: SortNotesService;
  public readonly filterService: FilterNotesService;
  public readonly locationService: OpenNoteLocationService;
  public readonly quickSearchService: QuickSearchService;

  constructor(context: vscode.ExtensionContext, repository: NoteRepository) {
    this.repository = repository;
    this.userInteraction = new UserInteraction();
    this.createService = new CreateNoteService(repository);
    this.deleteService = new DeleteNoteService(
      repository,
      this.userInteraction,
    );
    this.renameService = new RenameNoteService(
      repository,
      this.userInteraction,
    );
    this.searchService = new SearchNoteService(repository);
    this.duplicateService = new DuplicateNoteService(
      repository,
      this.createService,
    );
    this.favoriteService = new FavoriteNoteService(repository);
    this.colorService = new ChangeNoteColorService(
      repository,
      this.userInteraction,
    );
    this.sortService = new SortNotesService();
    this.filterService = new FilterNotesService();
    this.locationService = new OpenNoteLocationService();
    this.quickSearchService = new QuickSearchService(
      repository,
      this.searchService,
      openNote,
    );
  }
}
