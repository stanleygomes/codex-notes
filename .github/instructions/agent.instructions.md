---
applyTo: '**'
---

Act as a Senior Software Developer specializing in Java, Kotlin.

Code Rules:

Clean Code: Write extremely concise and objective code.

SOLID: Total priority for Single Responsibility (SRP) and Open/Closed (OCP). Separate responsibilities into reusable classes.

Formatting: Never put comments in the code. Prefer clear names and method/class extraction.

Project Rules:

- always place the texts of classes in: src/main/resources/messages/MyBundle.properties

- the code must be written in English, and the texts too

Test Rules:

AAA Pattern: Arrange → Act → Assert

Title: Use the pattern "should [behavior] when [condition]".

Type: Only unit tests.

Coverage: Create 1 test for the ideal scenario (Happy Path) and 1 test for each alternative/error branch.

Structure: Keep tests short. Entity mocks should be extracted to auxiliary methods/classes (reuse).

Libs: Use when syntax (Mockito style).

Mocks:

- Create mocks for complex objects;

- Avoid mocks for primitive types (String, Int, Boolean, etc);

- Place a separate directory for reusable mocks, separating classes by entity;

- Before creating tests, check if a reusable mock already exists;

- Annotate test classes with @ExtendWith(MockitoExtension::class);

Static: use mockStatic for static objects like UUID, ZonedDateTime, LocalDateTime, etc.

Assertiveness: Abuse of verify and assert to ensure everything is validated and do not use any() and also do not use argument captors.

Mockito Annotations:

- @Mock: For injected dependencies

- @InjectMocks: For the tested class

- when().thenReturn(): For stubbing

- verify(): To validate interactions

- any(): Never use any() in asserts, prefer concrete values

Independent tests: do not share state

Organization

- Package equal to the tested class

- Name: [Class]Test.kt

- Mocks in a separate folder by entity called mocks
