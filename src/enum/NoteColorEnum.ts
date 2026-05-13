export enum NoteColorEnum {
  NONE = 'NONE',
  BLUE = 'BLUE',
  GREEN = 'GREEN',
  YELLOW = 'YELLOW',
  ORANGE = 'ORANGE',
  PINK = 'PINK',
  RED = 'RED',
  PURPLE = 'PURPLE',
}

export const NOTE_COLOR_HEX: Record<NoteColorEnum, string | null> = {
  [NoteColorEnum.NONE]: null,
  [NoteColorEnum.BLUE]: '#ADE8E6',
  [NoteColorEnum.GREEN]: '#90EE90',
  [NoteColorEnum.YELLOW]: '#FFFF99',
  [NoteColorEnum.ORANGE]: '#FFDAB9',
  [NoteColorEnum.PINK]: '#FFB6C1',
  [NoteColorEnum.RED]: '#FF6666',
  [NoteColorEnum.PURPLE]: '#CC99FF',
};
