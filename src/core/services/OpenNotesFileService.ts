import { NoteRepository } from '../repositories/NoteRepository';
import { UserInteraction } from '../../infra/editor/UserInteraction';

export class OpenNotesFileService {
  constructor(
    private readonly repository: NoteRepository,
    private readonly userInteraction: UserInteraction,
  ) {}

  public async open(): Promise<void> {
    try {
      const filePath = this.repository.getNotesJsonPath();
      await this.userInteraction.openTextDocument(filePath);
    } catch (e: any) {
      this.userInteraction.showError(
        `Failed to open notes.json: ${e.message || e}`,
      );
    }
  }
}
