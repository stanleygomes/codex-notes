---
applyTo: 'vscode/**'
---

## Project Overview

**Codex Notes** is a VS Code extension for managing notes directly within the IDE. The extension provides a sidebar view with features for creating, managing, searching, and organizing notes with markdown support.

### Technology Stack

- **Language**: TypeScript
- **Build Tool**: npm
- **Platform**: VS Code
- **Testing**: Jest, Mocha
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
3. Run `npm test` to verify tests pass
4. Run `npm run lint` to validate code style
5. Run `npm run package` to build the extension
6. Test in Extension Development Host

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

## Test Rules

### Test Pattern
- **AAA Pattern**: Arrange → Act → Assert
- **Title Convention**: Use the pattern `"should [behavior] when [condition]"`
- **Test Type**: Unit tests with integration tests where needed
- **Coverage Goal**: 80% minimum

### Test Coverage
- Create 1 test for the ideal scenario (Happy Path)
- Create 1 test for each alternative/error branch
- Keep tests short and focused

### Test Structure
- Package structure must match the tested class
- Test file naming: `[ClassName].test.ts`
- Mocks in a separate folder by entity called `mocks`
- Entity mocks should be extracted to auxiliary methods/classes (reuse)

### Jest Configuration
- Use Jest for testing
- Mock VS Code APIs using `@vscode/test-electron`
- Use `jest.mock()` for external dependencies
- **Never use `any()` in mocks** - prefer concrete types
- **Never use argument captors** - validate with concrete values

### Mock Annotations
- Use `jest.mock()` for modules
- Use `jest.fn()` for functions

### Mock Strategy
- Create mocks for VS Code APIs
- Avoid mocks for primitive types (string, number, boolean)
- Before creating tests, check if a reusable mock already exists
- Place reusable mocks in separate directory, organized by entity

### Assertions and Verification
- Abuse of `expect()` and assertions to ensure everything is validated
- Tests must be independent - do not share state
- Verify all expected interactions with mocked dependencies