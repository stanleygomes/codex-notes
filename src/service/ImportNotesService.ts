import * as path from 'path';
import { NoteRepository } from '../repository/NoteRepository';
import { CreateNoteService } from './CreateNoteService';
import { FileHelper } from '../helper/FileHelper';
import { DialogHelper } from '../helper/DialogHelper';

export class ImportNotesService {
  private readonly repository: NoteRepository;
  private readonly createNoteService: CreateNoteService;

  constructor(repository: NoteRepository, createNoteService: CreateNoteService) {
    this.repository = repository;
    this.createNoteService = createNoteService;
  }

  async import(): Promise<void> {
    const uris = await DialogHelper.showOpenDialog({
      canSelectFiles: true,
      canSelectMany: true,
      filters: { 'Notes': ['md', 'txt'] },
    });

    if (!uris || uris.length === 0) {
      return;
    }

    let imported = 0;
    for (const uri of uris) {
      const filePath = uri.fsPath;
      const content = FileHelper.readText(filePath);
      const title = path.basename(filePath, path.extname(filePath));
      await this.createNoteService.createWithTitle(title, content);
      imported++;
    }

    DialogHelper.showInfo(`Imported ${imported} note(s) successfully.`);
  }
}
