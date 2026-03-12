export class FilenameHelper {
  static normalize(title: string): string {
    return title
      .replace(/[/\\:*?"<>|]/g, '')
      .replace(/[^\x20-\x7E]/g, '')
      .trim()
      .replace(/\s+/g, ' ')
      .replace(/ /g, '-')
      .toLowerCase();
  }
}
