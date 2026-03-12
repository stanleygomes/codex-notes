import * as fs from 'fs';
import * as path from 'path';
import archiver from 'archiver';

export class ZipHelper {
  static createZipFromFiles(filePaths: string[], outputPath: string): Promise<void> {
    return new Promise((resolve, reject) => {
      const output = fs.createWriteStream(outputPath);
      const archive = archiver('zip', { zlib: { level: 9 } });

      output.on('close', resolve);
      archive.on('error', reject);

      archive.pipe(output);

      for (const filePath of filePaths) {
        if (fs.existsSync(filePath)) {
          archive.file(filePath, { name: path.basename(filePath) });
        }
      }

      archive.finalize();
    });
  }
}
