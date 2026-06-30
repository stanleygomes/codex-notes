import { Note } from '../dtos/Note';
import { NoteColorEnum } from '../enums/NoteColorEnum';
import { NoteRepository } from '../repositories/NoteRepository';
import { UserInteraction } from '../../infra/editor/UserInteraction';
import { DateHelper } from '../helpers/DateHelper';

interface ColorOption {
  label: string;
  value: NoteColorEnum;
}

export class ChangeNoteColorService {
  private readonly repository: NoteRepository;
  private readonly userInteraction: UserInteraction;

  constructor(repository: NoteRepository, userInteraction: UserInteraction) {
    this.repository = repository;
    this.userInteraction = userInteraction;
  }

  async changeColor(note: Note): Promise<void> {
    const options: ColorOption[] = [
      { label: 'None', value: NoteColorEnum.NONE },
      { label: 'Blue', value: NoteColorEnum.BLUE },
      { label: 'Green', value: NoteColorEnum.GREEN },
      { label: 'Yellow', value: NoteColorEnum.YELLOW },
      { label: 'Orange', value: NoteColorEnum.ORANGE },
      { label: 'Pink', value: NoteColorEnum.PINK },
      { label: 'Red', value: NoteColorEnum.RED },
      { label: 'Purple', value: NoteColorEnum.PURPLE },
    ];

    const selected = await this.userInteraction.showQuickPick(
      options.map((o) => ({
        label: o.label,
        description: o.value,
        value: o.value,
      })),
      { placeHolder: 'Select note color' },
    );

    if (!selected) {
      return;
    }

    const updatedNote: Note = {
      ...note,
      color: selected.value,
      updatedAt: DateHelper.nowMs(),
    };
    this.repository.save(updatedNote);
  }
}
