import * as path from 'path';
import { Note } from '../dto/Note';
import { NoteRepository } from '../repository/NoteRepository';
import { FileHelper } from '../helper/FileHelper';
import { DialogHelper } from '../helper/DialogHelper';
import { ZipHelper } from '../helper/ZipHelper';

export class ExportNotesService {
  private readonly repository: NoteRepository;

  constructor(repository: NoteRepository) {
    this.repository = repository;
  }

  async exportAll(): Promise<void> {
    const saveUri = await DialogHelper.showSaveDialog({
      filters: { 'ZIP Archive': ['zip'] },
      defaultUri: undefined,
    });

    if (!saveUri) {
      return;
    }

    const notes = this.repository.getAllNotes();
    const filePaths = notes
      .map((n) => n.filePath)
      .filter((fp) => !!fp);

    const outputPath = saveUri.fsPath.endsWith('.zip') ? saveUri.fsPath : `${saveUri.fsPath}.zip`;

    await ZipHelper.createZipFromFiles(filePaths, outputPath);
    DialogHelper.showInfo(`Exported ${filePaths.length} notes to ${path.basename(outputPath)}`);
  }
}
