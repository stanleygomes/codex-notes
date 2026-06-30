import { SortTypeEnum } from '../core/enum/SortTypeEnum';
import { DateFilterEnum } from '../core/enum/DateFilterEnum';

export interface WebviewMessage {
  command: string;
  noteId?: string;
  query?: string;
  sortType?: SortTypeEnum;
  dateFilter?: DateFilterEnum;
  filterFavorites?: boolean;
}
