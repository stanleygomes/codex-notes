import { Note } from '../dto/Note';
import { SortTypeEnum } from '../enum/SortTypeEnum';

export class SortNotesService {
  sort(notes: Note[], sortType: SortTypeEnum): Note[] {
    const copy = [...notes];
    switch (sortType) {
      case SortTypeEnum.TITLE:
        return copy.sort((a, b) => a.title.localeCompare(b.title));
      case SortTypeEnum.DATE:
        return copy.sort((a, b) => b.updatedAt - a.updatedAt);
      case SortTypeEnum.FAVORITE:
        return copy.sort((a, b) => {
          if (a.isFavorite === b.isFavorite) {
            return b.updatedAt - a.updatedAt;
          }
          return a.isFavorite ? -1 : 1;
        });
    }
  }
}
