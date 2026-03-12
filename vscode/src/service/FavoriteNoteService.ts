import { Note } from '../dto/Note';
import { NoteRepository } from '../repository/NoteRepository';

export class FavoriteNoteService {
  private readonly repository: NoteRepository;

  constructor(repository: NoteRepository) {
    this.repository = repository;
  }

  toggleFavorite(note: Note): void {
    this.repository.toggleFavorite(note.id);
  }
}
