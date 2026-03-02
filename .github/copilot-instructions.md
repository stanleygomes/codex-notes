---
applyTo: '**'
---

## Project Overview

**Codex Notes** is an IntelliJ IDEA plugin for managing notes directly within the IDE. The plugin provides a tool window with features for creating, managing, searching, and organizing notes with markdown support.

### Technology Stack

- **Language**: Kotlin 2.3.0
- **Build Tool**: Gradle 9.3.1
- **JVM**: Java 21
- **Platform**: IntelliJ IDEA 2025.2.5+
- **Testing**: JUnit 4.13.2, Mockito 4.11.0
- **Code Quality**: Qodana, Ktlint, Kover (coverage)

### Project Structure

```
src/main/kotlin/com/nazarethlabs/codex/
├── service/           # Business logic services (note operations, settings)
│   ├── note/         # Note-related services (create, delete, rename, search, etc)
│   └── settings/     # Settings and configuration services
├── repository/       # Data persistence layer
├── ui/               # User interface components
│   ├── toolbar/      # Toolbar buttons and actions
│   ├── popup/        # Context menus and popup actions
│   ├── noteslist/    # Notes list view
│   ├── search/       # Search UI components
│   └── settings/     # Settings UI
├── state/            # Application state management
├── listener/         # Event listeners
├── dto/              # Data transfer objects
├── enum/             # Enumerations
├── helper/           # Utility classes
└── configurable/     # IntelliJ settings configurables

src/main/resources/
├── messages/         # Internationalization bundles
├── icons/            # Plugin icons
└── META-INF/         # Plugin configuration (plugin.xml)
```

### IntelliJ Plugin Architecture

- **Tool Window**: `AppWindowFactory` creates the main tool window
- **Services**: Registered in `plugin.xml` as application/project services
- **Resource Bundle**: All text strings in `messages/MyBundle.properties`
- **State Management**: Persistent storage using IntelliJ state services
- **Event System**: Listeners for file and project events

### Build and Test Commands

Use Makefile commands for common tasks:
- `make build` - Build the project
- `make build-plugin` - Build the IntelliJ plugin distribution
- `make test` - Run unit tests
- `make test-coverage` - Generate coverage report (target: 90%)
- `make check` - Run Ktlint code style checks
- `make format` - Auto-format code with Ktlint
- `make inspections` - Run Qodana code inspections
- `make run` - Launch IntelliJ with the plugin for manual testing
- `make verify-plugin` - Verify plugin compatibility

### Development Workflow

1. Make code changes following the coding rules
2. Run `make format` to ensure code style
3. Run `make test` to verify tests pass
4. Run `make check` to validate code style
5. Run `make build-plugin` to build the plugin
6. Run `make run` to test in a sandboxed IDE

## Code Rules

### Clean Code
- Write extremely concise and objective code
- Never put comments in the code - prefer clear names and method/class extraction

### SOLID Principles
- Total priority for Single Responsibility (SRP) and Open/Closed (OCP)
- Separate responsibilities into reusable classes

### Project-Specific Rules

**Internationalization**:
- Always place text strings in: `src/main/resources/messages/MyBundle.properties`
- Access via `MyBundle.message("key")`

**Language**:
- Code must be written in English
- All text strings (in properties file) must be in English

**Package Structure**:
- Follow the existing structure under `com.nazarethlabs.codex`
- Services: Place in `service/note/` or `service/settings/`
- UI components: Place under `ui/` with appropriate subdirectories
- DTOs: Place in `dto/`
- Enums: Place in `enum/`
- Helpers: Place in `helper/`

**IntelliJ Platform APIs**:
- Register services in `src/main/resources/META-INF/plugin.xml`
- Use IntelliJ Platform services correctly (ApplicationService, ProjectService)
- Follow IntelliJ state management patterns
- Use IntelliJ notification system for user feedback

## Test Rules

### Test Pattern
- **AAA Pattern**: Arrange → Act → Assert
- **Title Convention**: Use the pattern `"should [behavior] when [condition]"`
- **Test Type**: Only unit tests
- **Coverage Goal**: 90% minimum (validated by Kover)

### Test Coverage
- Create 1 test for the ideal scenario (Happy Path)
- Create 1 test for each alternative/error branch
- Keep tests short and focused

### Test Structure
- Package structure must match the tested class
- Test file naming: `[ClassName]Test.kt`
- Mocks in a separate folder by entity called `mocks`
- Entity mocks should be extracted to auxiliary methods/classes (reuse)

### Mockito Configuration
- Annotate test classes with `@ExtendWith(MockitoExtension::class)`
- Use `when().thenReturn()` syntax for stubbing
- **Never use `any()` in asserts** - prefer concrete values
- **Never use argument captors** - validate with concrete values
- Use `mockStatic` for static objects like UUID, ZonedDateTime, LocalDateTime

### Mockito Annotations
- `@Mock`: For injected dependencies
- `@InjectMocks`: For the tested class

### Mock Strategy
- Create mocks for complex objects
- Avoid mocks for primitive types (String, Int, Boolean, etc)
- Before creating tests, check if a reusable mock already exists
- Place reusable mocks in separate directory, organized by entity

### Assertions and Verification
- Abuse of `verify()` and `assert` to ensure everything is validated
- Tests must be independent - do not share state
- Verify all expected interactions with mocked dependencies
