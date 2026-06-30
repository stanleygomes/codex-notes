import { Note } from '../dtos/Note';
import { DateFilterEnum } from '../enums/DateFilterEnum';
import { DateHelper } from '../helpers/DateHelper';

export class FilterNotesService {
  filterByFavorite(notes: Note[]): Note[] {
    return notes.filter((n) => n.isFavorite);
  }

  filterByDateRange(notes: Note[], dateFilter: DateFilterEnum): Note[] {
    const now = new Date();
    let since: Date;

    switch (dateFilter) {
      case DateFilterEnum.TODAY:
        since = DateHelper.startOfDay(now);
        break;
      case DateFilterEnum.THIS_WEEK:
        since = DateHelper.startOfWeek(now);
        break;
      case DateFilterEnum.THIS_MONTH:
        since = DateHelper.startOfMonth(now);
        break;
    }

    return notes.filter((n) => n.updatedAt >= since.getTime());
  }
}
