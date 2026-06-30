import * as path from 'path';
import { Note } from '../dto/Note';
import { NoteRepository } from '../repository/NoteRepository';
import { FileHelper } from '../helper/FileHelper';
import { UserInteraction } from '../../infra/editor/UserInteraction';
import { ZipHelper } from '../helper/ZipHelper';

export class ExportNotesService {
  private readonly repository: NoteRepository;
  private readonly userInteraction: UserInteraction;

  constructor(repository: NoteRepository, userInteraction: UserInteraction) {
    this.repository = repository;
    this.userInteraction = userInteraction;
  }

  async exportAll(): Promise<void> {
    const saveUri = await this.userInteraction.showSaveDialog({
      filters: { 'ZIP Archive': ['zip'] },
      defaultUri: undefined,
    });

    if (!saveUri) {
      return;
    }

    const notes = this.repository.getAllNotes();
    const filePaths = notes.map((n) => n.filePath).filter((fp) => !!fp);

    const outputPath = saveUri.fsPath.endsWith('.zip')
      ? saveUri.fsPath
      : `${saveUri.fsPath}.zip`;

    await ZipHelper.createZipFromFiles(filePaths, outputPath);
    this.userInteraction.showInfo(
      `Exported ${filePaths.length} notes to ${path.basename(outputPath)}`,
    );
  }
}
