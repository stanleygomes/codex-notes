import { Note } from '../dto/Note';
import { NoteRepository } from '../repository/NoteRepository';
import { FileHelper } from '../helper/FileHelper';
import { SearchHelper } from '../helper/SearchHelper';

export class SearchNoteService {
  private readonly repository: NoteRepository;

  constructor(repository: NoteRepository) {
    this.repository = repository;
  }

  search(query: string): Note[] {
    const allNotes = this.repository.getAllNotes();
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
