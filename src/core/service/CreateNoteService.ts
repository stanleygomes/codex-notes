import { Note } from '../dto/Note';
import { NoteRepository } from '../repository/NoteRepository';
import { FileHelper } from '../helper/FileHelper';
import { NotesSettings } from '../../infra/editor/settings/NotesSettings';
import { NoteColorEnum } from '../enum/NoteColorEnum';
import { DateHelper } from '../helper/DateHelper';
import { UserInteraction } from '../../infra/editor/UserInteraction';

export class CreateNoteService {
  private readonly repository: NoteRepository;
  private readonly userInteraction: UserInteraction;

  constructor(repository: NoteRepository, userInteraction: UserInteraction) {
    this.repository = repository;
    this.userInteraction = userInteraction;
  }

  async create(content?: string): Promise<Note | undefined> {
    const title = await this.generateUniqueTitle();
    if (!title) {
      return undefined;
    }
    return this.createWithTitle(title, content ?? '');
  }

  async createWithTitle(title: string, content: string = ''): Promise<Note> {
    const extension = NotesSettings.getFileExtension();
    const notesDir = NotesSettings.getNotesDirectory();
    const fileName = FileHelper.buildNoteFileName(title, extension);
    const filePath = FileHelper.createFileWithContent(
      notesDir,
      fileName,
      content,
    );
    const now = DateHelper.nowMs();
    const note: Note = {
      id: globalThis.crypto.randomUUID(),
      title,
      filePath,
      createdAt: now,
      updatedAt: now,
      isFavorite: false,
      color: NoteColorEnum.NONE,
    };
    this.repository.save(note);
    await this.userInteraction.openTextDocument(filePath);
    return note;
  }

  private async generateUniqueTitle(): Promise<string | undefined> {
    const existing = this.repository.findAll().map((n) => n.title);
    let index = 1;
    let title = 'Untitled';
    while (existing.includes(title)) {
      title = `Untitled ${index}`;
      index++;
    }
    return title;
  }
}
