---
applyTo: "src/main/**/*.java,src/test/**/*.java,build.gradle,**/application*.yml,**/application*.yaml,**/checkstyle*.xml"
---

# Java Backend Development Instructions

## Table of Contents

- [Project Overview](#project-overview)
- [Directory Structure](#directory-structure)
- [Setup & Build](#setup--build)
- [REST API Endpoints](#rest-api-endpoints)
- [Development Guidelines](#development-guidelines)
- [Testing Strategy](#testing-strategy)
- [Validation Checklist](#validation-checklist)

---

## Project Overview

This is a Spring Boot 3.5.1 backend service for AI-powered resume and cover letter optimization. The application provides RESTful APIs for document processing, file management, and LLM integration with comprehensive testing and code quality enforcement.

**Technology Stack:**

- Java 21 LTS (Eclipse Temurin)
- Spring Boot 3.5.1
- Gradle 8.10
- JUnit 5 & Mockito (80%+ test coverage)
- Checkstyle 10.14.2
- SLF4J with Logback

**Project Size:** 16 main source files, 5+ test files

## Directory Structure

```
src/main/java/ca/letkeman/resumes/
├── RestServiceApplication.java          # Spring Boot entry point
├── BackgroundResume.java                # Async processing thread
├── Config.java                          # LLM configuration loading
├── Utility.java                         # Shared utilities
├── WebConfig.java                       # CORS and web configuration
├── controller/
│   └── ResumeController.java            # REST API endpoints
├── service/
│   ├── FilesStorageService.java         # File operations interface
│   ├── FilesStorageServiceImpl.java      # File operations implementation
│   └── PromptService.java               # Prompt building and management
├── model/
│   ├── Optimize.java                    # Optimization request model
│   └── FileInfo.java                    # File metadata model
├── message/
│   └── ResponseMessage.java             # API response wrapper
└── optimizer/
    ├── ApiService.java                  # LLM API integration
    ├── HtmlToPdf.java                   # Markdown/HTML to PDF converter
    ├── MarkdownToDocx.java              # Markdown to DOCX converter
    ├── ChatBody.java                    # LLM request model
    ├── Message.java                     # Chat message model
    └── responses/
        ├── LLMResponse.java             # LLM response wrapper
        ├── Choice.java                  # Response choice
        ├── Message.java                 # Response message
        ├── Stats.java                   # Generation statistics
        └── Usage.java                   # Token usage statistics
```

## Build and Test Instructions

### Prerequisites

- Java 21 LTS JDK (Eclipse Temurin)
- Gradle 8.10 (included via wrapper)
- Ollama or LM Studio running on `http://127.0.0.1:11434`

### Build Project

```bash
# Clean build with all checks
./gradlew clean build

# Build without running tests (faster)
./gradlew build -x test

# Build and check Checkstyle
./gradlew clean build checkstyleMain

# Always run: ./gradlew clean build before pushing code
```

### Run Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests ResumeControllerTest
./gradlew test --tests OptimizeTest
./gradlew test --tests ApiServiceTest

# Run tests with detailed output
./gradlew test --info

# Run tests with debugging
./gradlew test --debug

# Generate coverage report
./gradlew test jacocoTestReport
# Report location: build/reports/jacoco/test/html/index.html
```

### Code Quality Tools

This project uses automated code quality tools:

### Spotless

Automatic code formatting using Google Java Format:

```bash
# Check formatting (without changes)
./gradlew spotlessCheck

# Apply formatting (auto-fixes)
./gradlew spotlessApply
```

### Checkstyle

Enforces coding standards from `config/checkstyle/checkstyle.xml`:

```bash
./gradlew checkstyleMain
./gradlew checkstyleTest
```

View report: `build/reports/checkstyle/main.html`

### SpotBugs

Analyzes code for potential bugs:

```bash
./gradlew spotbugsMain
```

View report: `build/reports/spotbugs/main.html`

### Git Hooks

Automatically run quality checks on commit and push. Set up with:

```bash
./gradlew setupGitHooks
# or
./setup-hooks.sh          # Mac/Linux
./setup-hooks.bat         # Windows
python setup-hooks.py     # Cross-platform
```

See [SETUP_GIT_HOOKS.md](../../SETUP_GIT_HOOKS.md) for details.

## Code Quality Tools

### Run Application

```bash
# Start with default profile (development)
./gradlew bootRun

# Start with specific profile
./gradlew bootRun --args='--spring.profiles.active=dev'

# Start with continuous mode (auto-reload on changes)
./gradlew bootRun --continuous

# Debug mode
./gradlew bootRun --debug

# Custom port
./gradlew bootRun --args='--server.port=9090'
```

**Access Points:**

- API Base: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- API Docs: `http://localhost:8080/api-docs`
- Health Check: `http://localhost:8080/api/health`

## Configuration Files

- **`build.gradle`**: Gradle dependencies and build configuration
- **`src/main/resources/application.yml`**: Default application configuration
- **`src/main/resources/application-dev.yml`**: Development profile
- **`src/main/resources/application-prod.yml`**: Production profile
- **`src/main/resources/application-test.yml`**: Test profile
- **`config/checkstyle/checkstyle.xml`**: Code style rules

## Key Development Patterns

### REST Controllers

```java
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")  // For React frontend compatibility
public class ResumeController {

    // Always validate input files
    if (file == null || file.isEmpty()) {
        return ResponseEntity.badRequest()
            .body(new ResponseMessage("No file/invalid file provided"));
    }

    // Return appropriate HTTP status codes
    // Use ResponseEntity for flexibility
    // Serialize/deserialize with Gson
}
```

### Service Layer

- Implement interfaces for dependency injection
- Use `@Service` annotation
- Log operations with SLF4J: `private static final Logger LOGGER = LoggerFactory.getLogger(ClassName.class);`
- Handle exceptions gracefully
- Return DTOs, not entities

### Unit Tests

- Always use `@SpringBootTest` for integration tests
- Use `MockMvc` for HTTP endpoint testing
- Use `Mockito` for service mocking
- Test both happy path and error cases
- Achieve 80%+ code coverage
- Follow naming: `test_descriptive_test_name()`

### JSON Handling

- JSON field names like `prompt_tokens`, `completion_tokens`, `finish_reason`, `system_fingerprint` must remain snake_case (Ollama API compatibility)
- Use `@SerializedName` if needed for mapping
- Do not rename these fields without API coordination

## Common Build Issues and Workarounds

| Issue                   | Cause                        | Solution                                                                       |
| ----------------------- | ---------------------------- | ------------------------------------------------------------------------------ |
| `Checkstyle violations` | Code style violations        | Run `./gradlew checkstyleMain` and fix reported violations                     |
| `Test failures`         | Changed code behavior        | Ensure code logic is preserved; update tests if behavior intentionally changed |
| `Build timeout`         | Large dependency downloads   | Run `./gradlew clean build --no-daemon`                                        |
| `Port already in use`   | Another process on port 8080 | Use `./gradlew bootRun --args='--server.port=9090'`                            |

## Validation Steps Before Committing

Always run these checks in order:

1. **Build and test:** `./gradlew clean build`
2. **Run Checkstyle:** `./gradlew checkstyleMain checkstyleTest`
3. **Check test coverage:** Verify 80%+ coverage
4. **Verify behavior:** Run application and test manually: `./gradlew bootRun`
5. **Check for obvious errors:** Review code for typos, logic errors
6. **Commit message format:** Use conventional commits: `feat:`, `fix:`, `test:`, `docs:`, `refactor:`, `chore:`

**Note:** Git hooks automatically run quality checks on commit and push (see [SETUP_GIT_HOOKS.md](../../SETUP_GIT_HOOKS.md))

## Explicit Instructions

- **Always run `./gradlew clean build`** before pushing code to ensure all checks pass
- Only explore the codebase if information in these instructions is incomplete or found to be in error
- JSON field names in optimizer responses are intentionally snake_case—do not refactor them to camelCase
- Test coverage must always be 80% or higher
- All new features must include unit tests
- Checkstyle violations (non-JSON) must be zero before code is approved

## Dependencies

- **Spring Boot Starter Web**: REST API framework
- **Spring Boot Starter Test**: Integration testing
- **JUnit 5**: Unit testing framework
- **Mockito**: Mocking framework for tests
- **SLF4J/Logback**: Logging
- **Gson**: JSON serialization
- **jsoup**: HTML parsing and manipulation
- **Flying Saucer**: HTML to PDF conversion
- **CommonMark**: Markdown parsing
- **Apache POI**: DOCX generation
- **SpringDoc OpenAPI**: API documentation
- **Checkstyle**: Code style enforcement
- **SpotBugs**: Bug detection analysis
- **Spotless**: Code formatting enforcement

## References

- [Spring Boot 3.5 Documentation](https://spring.io/projects/spring-boot)
- [JUnit 5 Guide](https://junit.org/junit5/docs/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Checkstyle Configuration](https://checkstyle.org/config.html)
- [SpotBugs Documentation](https://spotbugs.readthedocs.io/)
- [Spotless Documentation](https://github.com/diffplug/spotless)

**When in doubt, refer to existing test files for patterns and examples. The test suite demonstrates all major code patterns and best practices used in this project.**

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
