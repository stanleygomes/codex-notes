import { Note } from '../dtos/Note';
import { NoteRepository } from '../repositories/NoteRepository';
import { CreateNoteService } from './CreateNoteService';
import { FileHelper } from '../helpers/FileHelper';

export class DuplicateNoteService {
  private readonly repository: NoteRepository;
  private readonly createNoteService: CreateNoteService;

  constructor(
    repository: NoteRepository,
    createNoteService: CreateNoteService,
  ) {
    this.repository = repository;
    this.createNoteService = createNoteService;
  }

  async duplicate(note: Note): Promise<Note> {
    const content = FileHelper.readText(note.filePath);
    const newTitle = this.generateDuplicateTitle(note.title);
    return this.createNoteService.createWithTitle(newTitle, content);
  }

  private generateDuplicateTitle(originalTitle: string): string {
    const existing = this.repository.findAll().map((n) => n.title);
    let index = 1;
    let title = `${originalTitle} (${index})`;
    while (existing.includes(title)) {
      index++;
      title = `${originalTitle} (${index})`;
    }
    return title;
  }
}
