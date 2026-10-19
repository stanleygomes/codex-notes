![Build](https://github.com/stanleygomes/codex-notes/workflows/Build/badge.svg)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/stanleygomes/codex-notes.svg?style=flat&logo=github)](https://github.com/stanleygomes/codex-notes/network)
[![GitHub issues](https://img.shields.io/github/issues/stanleygomes/codex-notes.svg?style=flat&logo=github)](https://github.com/stanleygomes/codex-notes/issues)
[![TypeScript](https://img.shields.io/badge/TypeScript-007ACC?style=flat&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)

# 🗒️ Codex Notes

![screenshot](https://github.com/stanleygomes/codex-notes/raw/HEAD/resources/screenshot.png)

<!-- Plugin description -->

**Codex Notes** is a powerful plugin for managing notes directly within your IDE. Keep your thoughts, code snippets, and documentation organized without leaving your development environment.

- **Create, edit, delete, rename, and search notes** with full markdown support
- **Favorite and assign colors to notes** for quick identification and access
- **Sort notes** by title, creation date, or favorite status
- **Duplicate notes** for easy replication
- **Import notes** from external files
- **Export notes** to external files
- **Open note file locations** in the file system
- **Filter notes** by favorites
- **Create notes from selected text** in the editor
- **Integrated search** via IntelliJ's Search Everywhere
- **Customizable settings** for file extension, notes directory, export options, import options, and folder access
<!-- Plugin description end -->

## 🌐 Plugin Marketplace Links

| Platform             | Link                                                                                                      |
| -------------------- | --------------------------------------------------------------------------------------------------------- |
| VS Code              | [Visual Studio Marketplace](https://marketplace.visualstudio.com/items?itemName=StanleyGomes.codex-notes) |
| Antigravity / Cursor | [Open VSX Registry](https://open-vsx.org/extension/stanleygomes/codex-notes)                              |

## 📋 Table of Contents

- [Usage](#-usage)
- [Keyboard Shortcuts](#-keyboard-shortcuts)
- [Configuration](#-configuration)
- [Development](#-development)
  - [Requirements](#requirements)
  - [Building from source](#building-from-source)
  - [Running tests](#running-tests)
  - [Running code inspections](#running-code-inspections)
- [CI/CD](#-cicd)
- [Contributing](#-contributing)
- [License](#-license)

## 🚀 Usage

### Opening the Tool Window

- Click on the **Codex Notes** icon in the left sidebar of your editor;

## ⌨️ Keyboard Shortcuts

| Action          | Shortcut                  |
| --------------- | ------------------------- |
| Open note       | `Double-click` or `Enter` |
| Rename note     | `F2`                      |
| Toggle favorite | `F`                       |
| Delete note     | `Delete`                  |
| Duplicate note  | `Ctrl+D`                  |
| Export note     | `Ctrl+E`                  |
| Open location   | `Ctrl+Shift+E`            |

## 🛠️ Development

**Requirements**

```
- Node.js 22.x+
- VS Code 1.85.0+
```

**Running and Debugging**

1.  Open the project in Visual Studio Code.
2.  Install dependencies:
    ```bash
    npm install
    ```
3.  Press `F5` to start a new VS Code instance with the extension enabled.
4.  To debug the **Webview**:
    - Open the Codex Notes view.
    - Run the command: `Developer: Open Webview Developer Tools` from the Command Palette.
5.  To debug the **Extension Host**:
    - Check the `Debug Console` in the main VS Code window for logs and errors.
    - Set breakpoints directly in the TypeScript files.

**Available Commands**

| Command                      | Description                                           |
| ---------------------------- | ----------------------------------------------------- |
| `npm install`                | Installs dependencies                                 |
| `npm run compile`            | Compiles the extension                                |
| `npm run typecheck`          | Type-checks sources and tests                         |
| `npm run lint`               | Runs ESLint checks                                    |
| `npm run format:check`       | Checks formatting with Prettier                       |
| `npm run test`               | Runs unit tests then e2e tests                        |
| `npm run test:unit`          | Runs Vitest unit tests                                |
| `npm run test:unit:coverage` | Runs unit tests and writes `coverage/unit`            |
| `npm run test:e2e`           | Compiles and runs Mocha e2e tests in a VS Code window |

### Testing

- **Unit tests** (`src/**/*.test.ts`) run on Vitest with a mocked `vscode`
  module (`src/test/unit/mocks/vscode.ts`). Each test gets a temporary `HOME`,
  so notes are written to a throwaway `~/.codex-notes`. Coverage must stay
  above 95% lines/functions and 90% branches.
- **E2E tests** (`src/test/e2e/**`) run inside a real VS Code Extension Host
  via `@vscode/test-cli`. `HOME` points to a temporary sandbox and the suite
  refuses to run if the notes folder resolves outside of it. On Linux without
  a display, use `xvfb-run -a npm run test:e2e`.

## 🚀 CI/CD

This project uses GitHub Actions for continuous integration and deployment. The following workflows are configured:

### Build Workflow (`build.yml`)

- **Trigger**: Push to `master` branch or pull requests
- **Actions**:
  - Validates conventional commits
  - Builds and validates the VS Code extension (lint, test, package)

### Release Workflow (`release.yml`)

- **Trigger**: Manual dispatch (`workflow_dispatch`)
- **Version auto-detection**: The workflow reads commits since the last `v*` tag and applies semantic versioning rules (breaking change → major, `feat:` → minor, everything else → patch).
- **Jobs**:
  1. `prepare_release` — auto-calculates versions, bumps files, updates changelogs, opens a PR targeting `master`, and creates the `v<version>` tag
  2. `publish_vscode` _(conditional)_ — packages and publishes to Visual Studio Marketplace and OpenVSX
  3. `create_release` _(conditional)_ — creates separate GitHub Releases per plugin and attaches built artifacts

## 📦 How to Release

1. Go to **Actions → Release** in the GitHub repository.
2. Click **Run workflow**.
3. The workflow will automatically:
   - Detect the new version from commits since the last release tag using semantic versioning.
   - Bump versions in `package.json`.
   - Update the `CHANGELOG.md` with commit entries.
   - Open a release PR with the generated changes targeting `master`.
   - Create the `v<version>` git tag.
   - Publish to the selected marketplaces.
   - Create separate GitHub Releases per plugin with built artifacts.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🔗 Links

- [Issue Tracker](https://github.com/stanleygomes/codex-notes/issues)
