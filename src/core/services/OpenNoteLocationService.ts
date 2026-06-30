import { Note } from '../dto/Note';
import { UserInteraction } from '../../infra/editor/UserInteraction';

export class OpenNoteLocationService {
  private readonly userInteraction: UserInteraction;

  constructor(userInteraction: UserInteraction) {
    this.userInteraction = userInteraction;
  }

  openLocation(note: Note): void {
    this.userInteraction.revealFileInOS(note.filePath);
  }
}
