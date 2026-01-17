---
applyTo: "src/main/**/*.java,src/test/**/*.java,build.gradle,**/application*.yml,**/application*.yaml,**/checkstyle*.xml"
---

# Java Backend Development Instructions

## Project Overview

This is a Spring Boot 3.5.1 backend service for AI-powered resume and cover letter optimization. The application provides RESTful APIs for document processing, file management, and LLM integration with comprehensive testing and code quality enforcement.

**Technology Stack:**

- Java 17 (Corretto LTS)
- Spring Boot 3.5.1
- Gradle 8.7
- JUnit 5 & Mockito (80%+ test coverage)
- Checkstyle 10.14.2
- SLF4J with Logback

**Project Size:** 14 main source files, 13+ test files

## Directory Structure

```
src/main/java/ca/letkeman/resumes/
├── RestServiceApplication.java          # Spring Boot entry point
├── BackgroundResume.java                # Async processing thread
├── Config.java                          # LLM configuration loading
├── Utility.java                         # Shared utilities
├── controller/
│   └── ResumeController.java            # REST API endpoints
├── service/
│   ├── FilesStorageService.java         # File operations interface
│   └── FilesStorageServiceImpl.java      # File operations implementation
├── model/
│   ├── Optimize.java                    # Optimization request model
│   └── FileInfo.java                    # File metadata model
├── message/
│   └── ResponseMessage.java             # API response wrapper
└── optimizer/
    ├── ApiService.java                  # LLM API integration
    ├── HtmlToPdf.java                   # Markdown/HTML to PDF converter
    ├── ChatBody.java                    # LLM request model
    ├── Message.java                     # Chat message model
    └── responses/
        ├── LLMResponse.java             # LLM response wrapper
        ├── Choice.java                  # Response choice
        ├── Message.java                 # Response message
        └── Usage.java                   # Token usage statistics
```

## Build and Test Instructions

### Prerequisites

- Java 17 JDK (Corretto): `C:\Users\Pete\java\corretto17`
- Gradle 8.7 (included via wrapper)
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

### Code Quality

```bash
# Run Checkstyle on main code
./gradlew checkstyleMain

# Run Checkstyle on test code
./gradlew checkstyleTest

# Run all checks
./gradlew check

# Expected result: 0 non-JSON violations (28 JSON-related violations are intentional for API compatibility)
```

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
- **Apache Tika**: Document parsing
- **Flying Saucer**: HTML to PDF conversion
- **CommonMark**: Markdown parsing
- **Checkstyle**: Code quality enforcement

## References

- [Spring Boot 3.5 Documentation](https://spring.io/projects/spring-boot)
- [JUnit 5 Guide](https://junit.org/junit5/docs/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Checkstyle Configuration](https://checkstyle.org/config.html)

**When in doubt, refer to existing test files for patterns and examples. The test suite demonstrates all major code patterns and best practices used in this project.**
