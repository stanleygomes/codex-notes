import { Note } from '../dto/Note';
import { NoteRepository } from '../repository/NoteRepository';
import { DateHelper } from '../helper/DateHelper';

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
