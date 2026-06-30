import { SortTypeEnum } from '../enums/SortTypeEnum';
import { DateFilterEnum } from '../enums/DateFilterEnum';

export interface WebviewMessage {
  command: string;
  noteId?: string;
  query?: string;
  sortType?: SortTypeEnum;
  dateFilter?: DateFilterEnum;
  filterFavorites?: boolean;
}
