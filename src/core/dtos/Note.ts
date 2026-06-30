import { NoteColorEnum } from '../enums/NoteColorEnum';

export interface Note {
  id: string;
  title: string;
  filePath: string;
  createdAt: number;
  updatedAt: number;
  isFavorite: boolean;
  color: NoteColorEnum;
}
