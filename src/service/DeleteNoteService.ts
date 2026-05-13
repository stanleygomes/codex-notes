import { Note } from '../dto/Note';
import { NoteRepository } from '../repository/NoteRepository';
import { FileHelper } from '../helper/FileHelper';
import { DialogHelper } from '../helper/DialogHelper';

export class DeleteNoteService {
  private readonly repository: NoteRepository;

  constructor(repository: NoteRepository) {
    this.repository = repository;
  }

  async confirmAndDelete(notes: Note[]): Promise<boolean> {
    const names = notes.slice(0, 5).map((n) => `"${n.title}"`);
    const extra = notes.length > 5 ? ` and ${notes.length - 5} more` : '';
    const confirmed = await DialogHelper.showConfirmation(
      `Delete ${names.join(', ')}${extra}? This action cannot be undone.`,
    );

    if (!confirmed) {
      return false;
    }

    for (const note of notes) {
      FileHelper.deleteFile(note.filePath);
      this.repository.removeNote(note.id);
    }

    return true;
  }
}
