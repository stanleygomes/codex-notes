import * as os from 'node:os';
import * as path from 'node:path';
import { afterEach, describe, expect, it, vi } from 'vitest';
import { OSHelper } from './OSHelper';

const originalPlatform = process.platform;

function setPlatform(platform: NodeJS.Platform): void {
  Object.defineProperty(process, 'platform', { value: platform });
}

describe('OSHelper', () => {
  afterEach(() => {
    setPlatform(originalPlatform);
    vi.unstubAllEnvs();
  });

  it('uses APPDATA on windows', () => {
    setPlatform('win32');
    vi.stubEnv('APPDATA', 'C:\\AppData');

    expect(OSHelper.getBaseConfigDir()).toBe('C:\\AppData');
  });

  it('falls back to AppData/Roaming on windows', () => {
    setPlatform('win32');
    vi.stubEnv('APPDATA', '');

    expect(OSHelper.getBaseConfigDir()).toBe(
      path.join(os.homedir(), 'AppData', 'Roaming'),
    );
  });

  it('uses Application Support on macOS', () => {
    setPlatform('darwin');

    expect(OSHelper.getBaseConfigDir()).toBe(
      path.join(os.homedir(), 'Library', 'Application Support'),
    );
  });

  it('uses XDG_CONFIG_HOME on linux', () => {
    setPlatform('linux');
    vi.stubEnv('XDG_CONFIG_HOME', '/xdg');

    expect(OSHelper.getBaseConfigDir()).toBe('/xdg');
  });

  it('falls back to ~/.config on linux', () => {
    setPlatform('linux');
    vi.stubEnv('XDG_CONFIG_HOME', '');

    expect(OSHelper.getBaseConfigDir()).toBe(
      path.join(os.homedir(), '.config'),
    );
  });

  it('builds the default config path', () => {
    setPlatform('linux');
    vi.stubEnv('XDG_CONFIG_HOME', '/xdg');

    expect(OSHelper.getDefaultConfigPath()).toBe(
      '/xdg/parable-workspaces/workspaces.json',
    );
  });
});
