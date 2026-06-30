import { Note } from '../../core/dtos/Note';
import { NoteRepository } from '../../core/repositories/NoteRepository';
import { SearchNoteService } from '../../core/services/SearchNoteService';
import { SortNotesService } from '../../core/services/SortNotesService';
import { FilterNotesService } from '../../core/services/FilterNotesService';
import { SortTypeEnum } from '../../core/enums/SortTypeEnum';
import { DateFilterEnum } from '../../core/enums/DateFilterEnum';
import { NoteMapper } from '../../core/mappers/NoteMapper';

export class ViewState {
  public currentQuery = '';
  public currentSort: SortTypeEnum = SortTypeEnum.DATE;
  public filterFavorites = false;
  public activeDateFilter?: DateFilterEnum;

  constructor(
    private readonly repository: NoteRepository,
    private readonly searchService: SearchNoteService,
    private readonly sortService: SortNotesService,
    private readonly filterService: FilterNotesService,
  ) {}

  public getPayload() {
    let notes = this.currentQuery.trim()
      ? this.searchService.search(this.currentQuery)
      : this.repository.findAll();

    if (this.filterFavorites) {
      notes = this.filterService.filterByFavorite(notes);
    }

    if (this.activeDateFilter) {
      notes = this.filterService.filterByDateRange(
        notes,
        this.activeDateFilter,
      );
    }

    notes = this.sortService.sort(notes, this.currentSort);

    return {
      command: 'updateNotes',
      notes: notes.map((n: Note) => NoteMapper.toWebview(n)),
    };
  }
}
