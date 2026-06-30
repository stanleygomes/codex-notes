import { Note } from '../dtos/Note';
import { NoteRepository } from '../repositories/NoteRepository';
import { FileHelper } from '../helpers/FileHelper';
import { UserInteraction } from '../../infra/editor/UserInteraction';

export class DeleteNoteService {
  private readonly repository: NoteRepository;
  private readonly userInteraction: UserInteraction;

  constructor(repository: NoteRepository, userInteraction: UserInteraction) {
    this.repository = repository;
    this.userInteraction = userInteraction;
  }

  async confirmAndDelete(notes: Note[]): Promise<boolean> {
    const names = notes.slice(0, 5).map((n) => `"${n.title}"`);
    const extra = notes.length > 5 ? ` and ${notes.length - 5} more` : '';
    const confirmed = await this.userInteraction.showConfirmation(
      `Delete ${names.join(', ')}${extra}? This action cannot be undone.`,
    );

    if (!confirmed) {
      return false;
    }

    for (const note of notes) {
      FileHelper.deleteFile(note.filePath);
      this.repository.delete(note.id);
    }

    return true;
  }
}
