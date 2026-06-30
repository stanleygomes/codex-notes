import { Note } from '../dtos/Note';
import { NoteRepository } from '../repositories/NoteRepository';
import { FileHelper } from '../helpers/FileHelper';
import { SearchHelper } from '../helpers/SearchHelper';

export class SearchNoteService {
  private readonly repository: NoteRepository;

  constructor(repository: NoteRepository) {
    this.repository = repository;
  }

  search(query: string): Note[] {
    const allNotes = this.repository.findAll();
    const contentMap = this.buildContentMap(allNotes);
    return SearchHelper.search(allNotes, query, contentMap);
  }

  private buildContentMap(notes: Note[]): Map<string, string> {
    const map = new Map<string, string>();
    for (const note of notes) {
      map.set(note.id, FileHelper.readText(note.filePath));
    }
    return map;
  }
}
