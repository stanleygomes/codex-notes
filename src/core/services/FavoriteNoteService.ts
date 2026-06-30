import { Note } from '../dtos/Note';
import { NoteRepository } from '../repositories/NoteRepository';
import { DateHelper } from '../helpers/DateHelper';

export class FavoriteNoteService {
  private readonly repository: NoteRepository;

  constructor(repository: NoteRepository) {
    this.repository = repository;
  }

  toggleFavorite(note: Note): void {
    const updatedNote: Note = {
      ...note,
      isFavorite: !note.isFavorite,
      updatedAt: DateHelper.nowMs(),
    };
    this.repository.save(updatedNote);
  }
}
