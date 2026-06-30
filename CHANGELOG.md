# Change Log

All notable changes to the "stanleygomes-codex-notes" extension will be documented in this file.

Check [Keep a Changelog](http://keepachangelog.com/) for recommendations on how to structure this file.

## [Unreleased]

## [2.5.0] - 2026-06-30

- chore: update package-lock.json dependencies
- ci: add lint, prettier, and audit jobs to the build pipeline and update build dependencies
- fix: update style render path to point to style directory instead of css
- feat: add command to open notes.json and implement migration of existing note files into notes.json upon initialization
- refactor: modularize view templates and separate frontend logic into independent script files
- refactor: replace NotesViewProvider with a modular view architecture and standardize path imports.
- refactor: reorganize project structure by relocating helpers, services, and DTOs into standardized directories
- refactor: abstract VS Code editor interactions into UserInteraction service to decouple core services from the framework
- refactor: migrate note storage from SQLite database to a local JSON file
- feat: implement infrastructure layer for workspace theme management and persistent state storage
- refactor: remove backup and import/export functionality and associated dependencies
- refactor: remove dependency on vscode configuration for note settings
- refactor: replace static DialogHelper with injectable UserInteraction service
- chore: remove zod dependency from package.json
- refactor: replace date-fns with Intl.RelativeTimeFormat in DateHelper to remove external dependency
- refactor: implement dependency injection container and modularize command registration
- chore: moving to folders infra and core
- chore: remove landing page documentation and configuration files
- refactor: remove test suite, clean up project configuration, and format HTML templates
- chore: remove the landpage directory and related deployment workflow
- docs: update readme
- Please provide the specific file changes or a description of the modifications you have made so I can generate the commit message for you.
- Merge pull request #107 from stanleygomes/release/v2.4.0


## [2.4.0] - 2026-05-13

- feat: implement QuickSearchService to enable note searching via command and quick access prefix
- feat: replace date filter chips with a dropdown and update filter layout styling
- feat: migrate import/export functionality to a dedicated Backup & Sync webview panel
- fix: correct path to WebviewMessage type definition
- refactor: encapsulate note serialization logic into a dedicated NoteMapper and extract WebviewMessage interface
- chore: configure Prettier and enforce code style across the repository
- feat: implement webview templating system and update Notes explorer UI
- style: move context menu display property to CSS and clean up select styling
- feat: add primary button to empty notes state to trigger note creation
- refactor: streamline build configuration, update dependencies, and clean up UI elements
- docs: remove installation section from README table of contents

## [2.2.4] - 2026-03-14

- docs: screenshot for plugin vscode
- docs: screenshots of plugins in readme

## [2.2.3] - 2026-03-13

- chore: release 2.2.3

## [2.2.0] - 2026-03-13

- fix: remove problematic settings vscode
- fix: open note folder correctly
- fix: refresh notes when changing view vscode
- docs: fixing readme vscode
- fix: nodemodules bundle
- fix: nodemodules bundle
- docs: fix readme vscode
- chore: add readme vscode
- chore: bump version vscode
- chore: bump version vscode
- chore: some adjustments to package vscode
- chore: extension vscode name
- chore: vscode version fix
- fix: app icon
- fix: render note colors and favorite indicator in list
- fix: build fixes for vscode extension
- fix: replace better-sqlite3 with sql.js to fix Electron NODE_MODULE_VERSION mismatch
- chore: extenion settings and other config
- chore: fix extension icon
- chore: refact file extensions
- chore: refactoring repositories
- chore: remove build artifacts from git tracking (out/ directory)
- feat: implement VS Code Codex Notes extension with full note management
- feat: starter for vscode extension

- Initial release
