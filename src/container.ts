import * as vscode from 'vscode';
import { NoteRepository } from './core/repositories/NoteRepository';
import { CreateNoteService } from './core/services/CreateNoteService';
import { DeleteNoteService } from './core/services/DeleteNoteService';
import { RenameNoteService } from './core/services/RenameNoteService';
import { SearchNoteService } from './core/services/SearchNoteService';
import { DuplicateNoteService } from './core/services/DuplicateNoteService';
import { FavoriteNoteService } from './core/services/FavoriteNoteService';
import { ChangeNoteColorService } from './core/services/ChangeNoteColorService';
import { SortNotesService } from './core/services/SortNotesService';
import { FilterNotesService } from './core/services/FilterNotesService';
import { OpenNoteLocationService } from './core/services/OpenNoteLocationService';
import { QuickSearchService } from './core/services/QuickSearchService';
import { OpenNotesFileService } from './core/services/OpenNotesFileService';
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
  public readonly openNotesFileService: OpenNotesFileService;

  constructor(context: vscode.ExtensionContext, repository: NoteRepository) {
    this.repository = repository;
    this.userInteraction = new UserInteraction();
    this.createService = new CreateNoteService(
      repository,
      this.userInteraction,
    );
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
    this.locationService = new OpenNoteLocationService(this.userInteraction);
    this.quickSearchService = new QuickSearchService(
      repository,
      this.searchService,
      openNote,
      this.userInteraction,
    );
    this.openNotesFileService = new OpenNotesFileService(
      repository,
      this.userInteraction,
    );
  }
}
