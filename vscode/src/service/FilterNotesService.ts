import { Note } from '../dto/Note';
import { DateFilterEnum } from '../enum/DateFilterEnum';
import { DateHelper } from '../helper/DateHelper';

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
