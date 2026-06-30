import * as os from 'os';
import { FileHelper } from './FileHelper';

export class OSHelper {
  public static getBaseConfigDir(): string {
    const home = os.homedir();
    switch (process.platform) {
      case 'win32':
        return (
          process.env.APPDATA ||
          FileHelper.buildPath(home, 'AppData', 'Roaming')
        );
      case 'darwin':
        return FileHelper.buildPath(home, 'Library', 'Application Support');
      default:
        return (
          process.env.XDG_CONFIG_HOME || FileHelper.buildPath(home, '.config')
        );
    }
  }

  public static getDefaultConfigPath(): string {
    return FileHelper.buildPath(
      this.getBaseConfigDir(),
      'parable-workspaces',
      'workspaces.json',
    );
  }
}
