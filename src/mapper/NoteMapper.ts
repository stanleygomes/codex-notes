import { Note } from '../core/dto/Note';
import { NOTE_COLOR_HEX } from '../core/enum/NoteColorEnum';
import { DateHelper } from '../core/helper/DateHelper';
import { FileHelper } from '../core/helper/FileHelper';

export class NoteMapper {
  /**
   * Maps a Note entity to a format suitable for the Webview.
   */
  public static toWebview(note: Note): any {
    return {
      id: note.id,
      title: note.title,
      preview: this.getPreview(note),
      dateLabel: DateHelper.toHumanRelative(note.updatedAt),
      isFavorite: note.isFavorite,
      color: note.color,
      colorHex: NOTE_COLOR_HEX[note.color],
    };
  }

  private static getPreview(note: Note): string {
    const content = FileHelper.readText(note.filePath);
    const plainText = content
      .replace(/```[\s\S]*?```/g, '')
      .replace(/`[^`]*`/g, '')
      .replace(/!\[.*?\]\(.*?\)/g, '')
      .replace(/\[.*?\]\(.*?\)/g, '')
      .replace(/#{1,6}\s+/g, '')
      .replace(/(\*\*|__)(.*?)\1/g, '$2')
      .replace(/(\*|_)(.*?)\1/g, '$2')
      .replace(/~~(.*?)~~/g, '$1')
      .replace(/^[-*+]\s+/gm, '')
      .replace(/^\d+\.\s+/gm, '')
      .replace(/^>\s+/gm, '')
      .replace(/[-_*]{3,}/g, '')
      .replace(/\s+/g, ' ')
      .trim();

    return plainText.slice(0, 80);
  }
}
