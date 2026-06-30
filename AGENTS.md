## Project Overview

**Codex Notes** is a VS Code extension for managing notes directly within the IDE. The extension provides a sidebar view with features for creating, managing, searching, and organizing notes with markdown support.

### Technology Stack

- **Language**: TypeScript
- **Build Tool**: npm
- **Platform**: VS Code
- **Code Quality**: ESLint, Prettier

### Project Structure

```
src/
├── extension.ts           # Main extension entry point
```

### VS Code Extension Architecture

### Development Workflow

1. Make code changes following the coding rules
2. Run `npm run format` to ensure code style
3. Run `npm run lint` to validate code style
4. Run `npm run package` to build the extension
5. Test in Extension Development Host

## Code Rules

### Clean Code

- Write extremely concise and objective code
- Never put comments in the code - prefer clear names and method/class extraction

### SOLID Principles

- Total priority for Single Responsibility (SRP) and Open/Closed (OCP)
- Separate responsibilities into reusable classes

### Project-Specific Rules

**Internationalization**:

- Use VS Code's localization API for strings
- Store localized strings in `package.nls.json` or locale-specific files

**Language**:

- Code must be written in English
- All text strings must be in English (localized via i18n)

**Package Structure**:

- Follow the existing structure under `src/`
- Core functionality in `notes/`
- UI components in `ui/`
- Utilities in `utils/`

**VS Code APIs**:

- Use VS Code extension APIs correctly
- Register commands, views, and configurations in `package.json`
- Follow VS Code extension best practices
- Use workspace and global state appropriately
