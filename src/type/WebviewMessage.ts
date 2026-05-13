import { SortTypeEnum } from '../enum/SortTypeEnum';
import { DateFilterEnum } from '../enum/DateFilterEnum';

export interface WebviewMessage {
  command: string;
  noteId?: string;
  query?: string;
  sortType?: SortTypeEnum;
  dateFilter?: DateFilterEnum;
  filterFavorites?: boolean;
}
