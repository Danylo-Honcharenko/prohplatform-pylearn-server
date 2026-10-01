# Repository Guidelines

## Project Structure

This repository is a Java 21 / Spring Boot service built with Maven. Application code is under `src/main/java/org/ua/fkrkm/progplatform`, organized into controllers, services, converters, configuration, filters, and utilities. Runtime settings and Flyway SQL migrations/seeds are in `src/main/resources` (notably `application.properties` and `db/`). Tests follow the same package layout under `src/test/java`; test-specific settings are in `src/test/resources`. The Maven wrapper (`mvnw`, `mvnw.cmd`) is included.

## Build, Test, and Development

- `./mvnw test` compiles the project and runs the JUnit test suite; JaCoCo generates a report during the test phase.
- `./mvnw package` builds the executable JAR in `target/`.
- `./mvnw spring-boot:run` starts the service locally. Configure database and other environment-specific settings before running; never commit credentials.
- `./mvnw -Dtest=CourseServiceTest test` runs one test class (replace with the desired class).

Use Java 21. The project depends on the separately versioned `ProgPlatformDAO` and `ProgPlatformClientLib` artifacts, which must be available to Maven.

## Coding Style and Naming

Follow the existing Java style: four spaces for indentation, package names in lowercase, types in `PascalCase`, methods and fields in `camelCase`, and constants in uppercase with underscores. Keep classes in the existing domain-oriented packages. Use constructor injection (Lombok annotations are already used in some classes), Spring stereotypes, and the existing request/response converter pattern. Match nearby formatting and comment language; no formatter or linter is configured in `pom.xml`.

## Testing

Tests use JUnit 5 through `spring-boot-starter-test`, with Mockito for isolated service tests and Spring Security test support. Name test files `*Test.java` and keep them under the corresponding `src/test/java` package. Cover changed behavior and relevant error/authorization paths; no repository-wide coverage threshold is configured.

## Commits and Pull Requests

Recent commits use short, lowercase summaries, often with prefixes such as `update:` or `refactoring:`. Keep commits focused and describe the change directly (for example, `update: validate module completion`). PRs should explain the behavior change, link related work when available, note configuration or migration impacts, and include test results. Add screenshots only when a change has a user-visible interface; this repository is a backend service.
