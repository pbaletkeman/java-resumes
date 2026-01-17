# Copilot Instructions - java-resumes Repository

Repository-wide custom instructions for GitHub Copilot agents working on the java-resumes project.

## Project Overview

**java-resumes** is a full-stack application for AI-powered resume and cover letter optimization.

- **Backend**: Spring Boot 3.5.1, Java 25 LTS, Gradle 8.7 - REST API for document processing
- **Frontend**: React 19, TypeScript 5.9.3, Vite 7.2.4 - Tab-based UI for document management
- **AI Integration**: Ollama/LM Studio LLM endpoints for intelligent optimization
- **Code Quality**: Checkstyle 10.14.2 (100% compliance), 80%+ test coverage required

---

## Path-Specific Custom Instructions

This repository uses path-specific custom instructions for targeted guidance:

### Backend Instructions

- **File**: `.github/instructions/backend.instructions.md`
- **Applies To**: `src/main/**/*.java,src/test/**/*.java,build.gradle,**/application*.yml,**/checkstyle*.xml`
- **Content**: Java/Spring Boot patterns, build process, testing strategy, Gradle commands
- **Use When**: Working on REST APIs, services, data models, backend tests

### Frontend Instructions

- **File**: `.github/instructions/frontend.instructions.md`
- **Applies To**: `frontend/**/*.ts,frontend/**/*.tsx,frontend/**/*.js,frontend/**/*.jsx,frontend/**/*.json,frontend/**/*.css,frontend/**/*.yml,frontend/Dockerfile,frontend/nginx.conf`
- **Content**: React/TypeScript patterns, npm scripts, component structure, testing strategy
- **Use When**: Working on components, hooks, styling, frontend configuration

**When Copilot edits files**, it will automatically apply the appropriate path-specific instructions alongside these repository-wide instructions.

---

## Core Purpose & Values

When working with this codebase:

1. **Maintain Clarity**: Code should be self-explanatory with minimal comments
2. **Follow Standards**: Adhere to checkstyle (backend) and ESLint (frontend) rules
3. **Test Thoroughly**: Write unit tests for all new functionality (target: 80%+ coverage)
4. **Document Well**: Update markdown files when architecture changes
5. **Design Simply**: Prefer straightforward solutions over complex patterns

---

## Project Structure

```
src/main/java/ca/letkeman/resumes/
├── RestServiceApplication.java        # Spring Boot entry point
├── BackgroundResume.java              # Async processing thread
├── Config.java                        # LLM configuration loading
├── Utility.java                       # Shared utilities
├── controller/
│   └── ResumeController.java          # REST endpoints
├── service/
│   ├── FilesStorageService.java       # File ops interface
│   └── FilesStorageServiceImpl.java    # File ops implementation
├── model/
│   ├── Optimize.java                  # Optimization request DTO
│   └── FileInfo.java                  # File metadata DTO
├── message/
│   └── ResponseMessage.java           # API response wrapper
└── optimizer/
    ├── ApiService.java                # LLM API integration
    ├── HtmlToPdf.java                 # Markdown-to-PDF converter
    ├── ChatBody.java                  # LLM request model
    ├── Message.java                   # Chat message model
    └── responses/
        ├── LLMResponse.java           # LLM API response wrapper
        ├── Choice.java                # Response choice
        ├── Message.java               # Response message
        ├── Stats.java                 # Generation stats
        └── Usage.java                 # Token usage info

src/test/java/ca/letkeman/resumes/
├── controller/
│   └── ResumeControllerTest.java      # REST endpoint tests
├── model/
│   └── OptimizeTest.java              # Optimize model tests
└── optimizer/
    └── ApiServiceTest.java            # LLM integration tests

docs/
├── README.md                          # Detailed documentation
└── Architecture.md                    # System design & diagrams

Configuration Files:
├── config.json                        # LLM endpoint configuration
├── application.properties             # Spring properties
├── build.gradle                       # Gradle build file
└── config/checkstyle/checkstyle.xml   # Code quality rules
```

---

## Key Components

### ResumeController

**Purpose**: REST API endpoints for file operations and optimization requests

**Key Methods**:

- `file2PDF()`: Markdown/HTML to PDF conversion
- `optimizeResume()`: Process resume/job description and trigger optimization
- `getListFiles()`: List uploaded files
- `getFile()`: Download specific file
- `deleteFile()`: Remove file
- `healthCheck()`: API health status check

**HTTP Endpoints**:

```
POST   /api/upload                - Optimize resume/cover letter
POST   /api/markdownFile2PDF      - Convert markdown to PDF
GET    /api/files                 - List all files
GET    /api/files/{filename}      - Download file
DELETE /api/files/{filename}      - Delete file
GET    /api/health                - Health check
```

**Guidelines**:

- Use `@CrossOrigin(origins = "*")` for React frontend compatibility
- Always validate input files (check null, empty)
- Return appropriate HTTP status codes
- Serialize/deserialize with Gson
- Log operations with SLF4J

### BackgroundResume

**Purpose**: Async thread for resume optimization (prevents blocking)

**Key Methods**:

- `run()`: Executes optimization workflow
- `getOptimize()` / `setOptimize()`: Configuration access

**Guidelines**:

- Spawned by ResumeController after validating request
- Uses ApiService for LLM communication
- Handles file writing asynchronously
- Log completion and errors

### ApiService

**Purpose**: Integration with OpenAI-compatible LLM APIs

**Key Methods**:

- `produceFiles()`: Main optimization workflow
- `createChatBody()`: Build LLM request
- `sendHttpRequest()`: HTTP communication
- `processResponse()`: Parse LLM response

**Guidelines**:

- Support OpenAI API format
- Handle HTTP connections properly
- Parse JSON responses with Gson
- Manage model configuration via Config.json
- Implement error handling for API failures

### FilesStorageService / FilesStorageServiceImpl

**Purpose**: Abstract file system operations

**Key Methods**:

- `save()`: Store uploaded file
- `load()`: Retrieve file resource
- `delete()`: Remove file
- `loadAll()`: List all files

**Guidelines**:

- Use Spring Resource API
- Handle file paths correctly
- Implement proper error handling
- Respect configurable root directory

### Model Classes (DTOs)

**Optimize**: Request parameters for optimization

- `promptType[]`: Array of output types (Resume, CoverLetter)
- `temperature`: LLM response randomness (0.0-1.0)
- `model`: LLM model identifier
- `company`, `jobTitle`: Target job info
- `resume`, `jobDescription`: Document content

**FileInfo**: File metadata response

- `filename`: File name
- `url`: Download URL
- `size`: File size in bytes

**Guidelines**:

- Use getters/setters for properties
- Implement equals/hashCode for comparison
- Support JSON serialization

---

## Testing Standards

### Unit Test Coverage

**ResumeControllerTest**: Controller functionality

```java
@SpringBootTest
@AutoConfigureMockMvc
class ResumeControllerTest {
    // Use MockMvc for HTTP testing
    // Mock file uploads with MockMultipartFile
    // Verify response status and JSON content
}
```

**OptimizeTest**: Model validation

```java
// Test constructor
// Test getters/setters
// Test validation rules
// Test serialization
```

**ApiServiceTest**: LLM integration

```java
// Mock HTTP responses
// Test request building
// Test response parsing
// Test error handling
```

### Writing New Tests

1. **Use @SpringBootTest** for integration tests
2. **Use @AutoConfigureMockMvc** for controller tests
3. **Mock external dependencies** (LLM service, file storage)
4. **Test both success and failure cases**
5. **Verify response formats and status codes**

**Example Test Pattern**:

```java
@Test
void test_successful_operation() throws Exception {
    // Setup
    MockMultipartFile file = new MockMultipartFile(...);

    // Execute
    mockMvc.perform(multipart("/endpoint").file(file))

    // Assert
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").exists());
}
```

### Running Tests

```bash
./gradlew test                              # Run all tests
./gradlew test --tests ClassName            # Run specific class
./gradlew test --tests ClassName.methodName # Run specific test
```

---

## Code Quality Standards

### Checkstyle Compliance

The project uses **Checkstyle 10.14.2** with rules defined in `config/checkstyle/checkstyle.xml`.

**Key Rules**:

- Max line length: 120 characters
- Import organization
- Javadoc requirements
- Naming conventions
- Code structure rules

**Run Quality Checks**:

```bash
./gradlew checkstyleMain    # Main code
./gradlew checkstyleTest    # Test code
./gradlew check             # Full quality check
```

**Fix Common Issues**:

- 80+ column limit for readability
- Use meaningful variable names
- Keep methods focused and short
- Add public method documentation

### Code Comments

**Philosophy**: Write self-explanatory code; comment only when needed

**When to Comment**:

- ✅ Complex algorithms or business logic
- ✅ Non-obvious design decisions
- ✅ API contracts and usage examples
- ✅ Workarounds or hacks with explanations

**When NOT to Comment**:

- ❌ Obvious operations (counter++, getter methods)
- ❌ Repeating what the code says
- ❌ Outdated comments that don't match code

**Comment Style**:

```java
// Explain WHY, not WHAT
// Send request with exponential backoff to handle rate limits
for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
    // ...
}
```

---

## Development Workflow

### Adding a New Feature

1. **Analyze Requirements**
   - Read existing tests for similar features
   - Check controller endpoints for patterns
   - Review Architecture.md for design

2. **Design Solution**
   - Keep changes minimal and focused
   - Follow existing patterns
   - Consider error cases

3. **Implement Code**
   - Write code with clarity
   - Follow checkstyle rules
   - Add meaningful variable names

4. **Write Tests**
   - Test happy path
   - Test error conditions
   - Mock external dependencies

5. **Run Quality Checks**

   ```bash
   ./gradlew build
   ./gradlew test
   ./gradlew checkstyleMain
   ```

6. **Update Documentation**
   - Update docs/README.md if user-facing
   - Update docs/Architecture.md if design changes
   - Add code comments for complex logic

### Modifying Existing Code

**Guidelines**:

- Make minimal necessary changes
- Don't refactor unrelated code
- Run full test suite before submitting
- Update affected tests
- Check checkstyle compliance

### Git Workflow

```bash
# Create feature branch
git checkout -b feature/description

# Make changes, commit frequently
git add .
git commit -m "Brief description of change"

# Before pushing
./gradlew build test checkstyleMain

# Push to remote
git push origin feature/description
```

---

## Common Patterns

### REST Endpoint Pattern

```java
@PostMapping(path = "/endpoint")
public ResponseEntity<ResponseMessage> methodName(@RequestParam String param) {
    try {
        // Validate input
        if (param == null || param.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseMessage("Error message"));
        }

        // Process request
        String result = processData(param);

        // Return success
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseMessage("Success message"));
    } catch (Exception e) {
        LOGGER.error("Error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ResponseMessage("Server error"));
    }
}
```

### Service Method Pattern

```java
public void save(MultipartFile file) {
    // Validate
    if (file == null || file.isEmpty()) {
        throw new RuntimeException("File is empty");
    }

    // Process
    try {
        Path uploadPath = Paths.get(configRoot);
        Files.createDirectories(uploadPath);
        Files.copy(file.getInputStream(),
                   uploadPath.resolve(file.getOriginalFilename()));
    } catch (IOException e) {
        LOGGER.error("File save error: {}", e.getMessage());
        throw new RuntimeException("Failed to save file");
    }
}
```

### Background Thread Pattern

```java
public class BackgroundTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundTask.class);

    private final Data data;

    public BackgroundTask(Data data) {
        this.data = data;
    }

    @Override
    public void run() {
        try {
            // Execute long-running operation
            processData();
            LOGGER.info("Task completed successfully");
        } catch (Exception e) {
            LOGGER.error("Task failed: {}", e.getMessage());
        }
    }
}
```

### Testing Pattern

```java
@SpringBootTest
@AutoConfigureMockMvc
class ComponentTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_successful_scenario() throws Exception {
        // Setup
        MockMultipartFile file = new MockMultipartFile(
            "field", "filename.ext", "mime/type", "content".getBytes());

        // Execute & Assert
        mockMvc.perform(multipart("/endpoint").file(file))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("expected"));
    }
}
```

---

## Configuration & Setup

### Local Development Setup

**1. Install Java 25 LTS**

```bash
# Verify installation
java -version
```

**2. Clone Repository**

```bash
git clone https://github.com/pbaletkeman/java-resumes.git
cd java-resumes
```

**3. Start LLM Service** (choose one):

```bash
# Option A: Ollama
ollama serve
# In another terminal: ollama pull gemma-3-4b-it

# Option B: LM Studio
# Download and run LM Studio, load a model
```

**4. Configure LLM Endpoint**
Edit `config.json`:

```json
{
  "endpoint": "http://localhost:11434",
  "apikey": "not-needed-for-local",
  "model": "gemma-3-4b-it"
}
```

**5. Build & Run**

```bash
./gradlew build
./gradlew bootRun
```

**6. Access Application**

- Web UI: http://localhost:8080/spotlight/index.html
- API Docs: http://localhost:8080/swagger-ui/index.html

### Application Properties

Create `src/main/resources/application.properties`:

```properties
server.port=8080
upload.path=./uploads
spring.servlet.multipart.max-file-size=10MB
logging.level.ca.letkeman=DEBUG
```

---

## Dependencies & Versions

### Core Dependencies

- **Spring Boot**: 3.5.1+
- **Java**: 25 (LTS)
- **Gradle**: Latest
- **Gson**: 2.13.1
- **jsoup**: 1.15.4
- **CommonMark**: 0.24.0
- **Flying Saucer**: 9.1.22

### Test Dependencies

- **JUnit 5**: Latest
- **Spring Boot Test**: 3.5.1+
- **MockMvc**: Included with spring-test

### Build Plugins

- **Checkstyle**: 10.14.2
- **Spring Boot Plugin**: 3.5.1+

See `build.gradle` for complete dependency list.

---

## Troubleshooting

### Build Issues

**Problem**: Gradle build fails

```bash
# Clean and rebuild
./gradlew clean build

# Update Gradle
./gradlew wrapper --gradle-version=latest
```

**Problem**: Java version mismatch

```bash
# Check Java version
java -version

# Set JAVA_HOME if needed
export JAVA_HOME=/path/to/java/25
```

### Runtime Issues

**Problem**: Port 8080 already in use

```properties
# In application.properties
server.port=8081
```

**Problem**: LLM API connection fails

- Verify LLM service is running
- Check config.json endpoint URL
- Test: `curl http://localhost:11434/api/tags`

**Problem**: File upload fails

- Verify `upload.path` directory exists
- Check permissions on upload directory
- Ensure `upload.path` in config.json is correct

### Test Failures

**Problem**: Tests fail with connection errors

- Ensure LLM service is NOT running (tests should mock)
- Check mock setup in test class
- Verify @Mock annotations

**Problem**: Checkstyle failures

- Run: `./gradlew checkstyleMain`
- Check output for line length, naming, etc.
- Fix automatically where possible

---

## Documentation References

| Document         | Purpose                | Location                                                             |
| ---------------- | ---------------------- | -------------------------------------------------------------------- |
| Detailed README  | Setup, features, API   | [docs/README.md](docs/README.md)                                     |
| Architecture     | Design diagrams, flows | [docs/Architecture.md](docs/Architecture.md)                         |
| Quick Start      | Initial setup          | [README.md](README.md)                                               |
| Checkstyle Rules | Code quality           | [config/checkstyle/checkstyle.xml](config/checkstyle/checkstyle.xml) |
| Build Config     | Dependencies           | [build.gradle](build.gradle)                                         |

---

## Best Practices

### Code Organization

1. **Keep classes focused**: Single responsibility principle
2. **Use interfaces**: Abstract dependencies (like FilesStorageService)
3. **Separate concerns**: Controller → Service → Model layers
4. **Name clearly**: Variable names should be self-explanatory

### Error Handling

```java
// ✅ Good: Specific exception and logging
try {
    return processFile(file);
} catch (IOException e) {
    LOGGER.error("Failed to process file {}: {}", filename, e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ResponseMessage("File processing failed"));
}

// ❌ Bad: Generic exception, no logging
try {
    return processFile(file);
} catch (Exception e) {
    return ResponseEntity.ok(new ResponseMessage("Error"));
}
```

### Validation

```java
// ✅ Good: Clear validation with specific error
if (optimize == null || optimize.getResume().isEmpty()) {
    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
        .body(new ResponseMessage("Required property missing or invalid."));
}

// ❌ Bad: No validation
processRequest(optimize);
```

### Testing

```java
// ✅ Good: Clear test name and assertions
@Test
void test_upload_rejects_empty_file() throws Exception {
    MockMultipartFile empty = new MockMultipartFile(
        "file", "empty.pdf", "application/pdf", new byte[0]);

    mockMvc.perform(multipart("/upload").file(empty))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("No file/invalid file provided"));
}

// ❌ Bad: Unclear test
@Test
void test1() throws Exception {
    // What is this testing?
}
```

---

## Useful Commands

```bash
# Build
./gradlew build                           # Full build
./gradlew clean build                     # Clean and build

# Run
./gradlew bootRun                         # Start application
./gradlew run                             # Alternative

# Test
./gradlew test                            # Run all tests
./gradlew test --tests ClassName          # Run specific test class
./gradlew test --info                     # Verbose test output

# Quality
./gradlew checkstyleMain                  # Check code style
./gradlew checkstyleTest                  # Check test style
./gradlew check                           # All quality checks

# Clean
./gradlew clean                           # Remove build artifacts
./gradlew cleanTest                       # Remove test artifacts

# Dependencies
./gradlew dependencies                    # List dependencies
./gradlew dependencyInsight               # Analyze dependency
```

---

## Key Takeaways

When working on this project:

1. **Code Clarity**: Write obvious code; minimize complex patterns
2. **Tests First**: Consider test implications before designing
3. **Follow Standards**: Respect checkstyle and existing patterns
4. **Document Changes**: Update markdown files for architecture changes
5. **Minimal Changes**: Make only necessary modifications
6. **Error Handling**: Always handle exceptions and log appropriately
7. **Async Processing**: Use BackgroundResume for long operations
8. **API Design**: Follow REST conventions in endpoints
9. **Validation**: Check inputs early and return appropriate errors
10. **Testing**: Write tests for all public methods and edge cases

---

---

## Documentation Structure

Complete documentation is organized in the [docs/](docs/) directory:

- **[docs/INDEX.md](docs/INDEX.md)** - Complete documentation index and navigation
- **[docs/Architecture.md](docs/Architecture.md)** - System architecture and design patterns
- **[docs/BACKEND_README.md](docs/BACKEND_README.md)** - Backend development guide and setup
- **[docs/README.md](docs/README.md)** - Frontend development guide and setup
- **[docs/wip/STATUS.md](docs/wip/STATUS.md)** - Current project status and milestones
- **[docs/CHECKSTYLE_AND_OLLAMA.md](docs/CHECKSTYLE_AND_OLLAMA.md)** - Code quality and LLM configuration

---

## Validation Checklist

### Before Committing Backend Code

- [ ] All tests pass: `./gradlew clean test`
- [ ] No Checkstyle violations: `./gradlew checkstyleMain checkstyleTest`
- [ ] Build succeeds: `./gradlew clean build`
- [ ] Code coverage meets 80%+ target
- [ ] No unused imports
- [ ] All public methods have JSDoc comments
- [ ] No TODO comments without context
- [ ] Error handling implemented for all user inputs

### Before Committing Frontend Code

- [ ] All tests pass: `npm test`
- [ ] No TypeScript errors: `npm run type-check`
- [ ] No lint errors: `npm run lint`
- [ ] Build succeeds: `npm run build`
- [ ] Code coverage meets 80%+ target
- [ ] No console errors or warnings
- [ ] Responsive design verified in browser
- [ ] All components have proper TypeScript types

---

## Quick Reference

### Build Commands

**Backend:**

```bash
./gradlew clean build              # Full build
./gradlew clean test               # Run tests
./gradlew checkstyleMain           # Check code quality
./gradlew bootRun                  # Run application (port 8080)
```

**Frontend:**

```bash
npm install                        # Install dependencies
npm run dev                        # Start dev server (port 5173)
npm test                           # Run tests
npm run build                      # Build for production
npm run lint                       # Check code quality
npm run type-check                 # TypeScript type checking
```

### Project Structure

```
java-resumes/
├── .github/
│   └── instructions/              # Path-specific Copilot guidance
│       ├── backend.instructions.md
│       └── frontend.instructions.md
├── src/main/java/                 # Backend source code
├── src/test/java/                 # Backend tests
├── frontend/                      # Frontend React application
├── docs/                          # Documentation
│   ├── INDEX.md                   # Documentation index
│   ├── Architecture.md            # System architecture
│   ├── BACKEND_README.md          # Backend guide
│   ├── README.md                  # Frontend guide
│   └── wip/                       # Work-in-progress docs
├── copilot-instructions.md        # This file (repo-wide guidance)
├── .gitattributes                 # Git encoding configuration
└── README.md                      # Main project README
```

### Essential Backend Files

| File                       | Purpose             |
| -------------------------- | ------------------- |
| `ResumeController.java`    | REST API endpoints  |
| `ApiService.java`          | LLM integration     |
| `FilesStorageService.java` | File operations     |
| `BackgroundResume.java`    | Async processing    |
| `build.gradle`             | Build configuration |
| `application-*.yml`        | App configuration   |

### Essential Frontend Files

| File                 | Purpose                       |
| -------------------- | ----------------------------- |
| `App.tsx`            | Main application component    |
| `MainContentTab.tsx` | Primary UI interface          |
| `hooks/useApi.ts`    | Centralized API communication |
| `hooks/useTheme.ts`  | Theme management              |
| `vite.config.ts`     | Build configuration           |
| `tsconfig.json`      | TypeScript configuration      |

---

## Technology Stack

### Backend

- **Language**: Java 25 LTS (Eclipse Temurin)
- **Framework**: Spring Boot 3.5.1
- **Build Tool**: Gradle 8.7
- **Testing**: JUnit 5, Mockito (80%+ coverage required)
- **Code Quality**: Checkstyle 10.14.2 (100% compliance)
- **JSON**: Gson
- **PDF Generation**: Flying Saucer, CommonMark
- **Logging**: SLF4J + Logback

### Frontend

- **Framework**: React 19.2.0
- **Language**: TypeScript 5.9.3
- **Build Tool**: Vite 7.2.4
- **UI Components**: PrimeReact 10.9.7
- **Styling**: Tailwind CSS 4.1.18
- **Testing**: Vitest 4.0.17, React Testing Library
- **HTTP Client**: Axios
- **Icons**: React Icons

---

## Key Guidelines

### General

1. **Clarity Over Cleverness**: Write obvious code
2. **Test Everything**: Target 80%+ coverage
3. **Type Safety**: Use types to prevent errors
4. **Minimal Changes**: Only modify what's necessary
5. **Documentation**: Update markdown when architecture changes

### Backend

1. Use proper REST conventions for endpoints
2. Validate all inputs early
3. Return appropriate HTTP status codes
4. Log operations with SLF4J
5. Handle exceptions gracefully
6. Use service layer for business logic
7. Write tests for all public methods
8. Maintain 100% Checkstyle compliance

### Frontend

1. Use TypeScript strict mode
2. Create reusable components
3. Use custom hooks for logic
4. Manage state with React Context
5. Test components and hooks
6. Follow Tailwind CSS patterns
7. Use centralized API communication
8. Handle loading and error states

---

## Getting Help

**For questions about:**

- **Architecture**: See [docs/Architecture.md](docs/Architecture.md)
- **Backend Setup**: See [docs/BACKEND_README.md](docs/BACKEND_README.md)
- **Frontend Setup**: See [docs/README.md](docs/README.md)
- **Code Quality**: See [docs/CHECKSTYLE_AND_OLLAMA.md](docs/CHECKSTYLE_AND_OLLAMA.md)
- **Project Status**: See [docs/wip/STATUS.md](docs/wip/STATUS.md)
- **Navigation**: See [docs/INDEX.md](docs/INDEX.md)

---

## Configuration Files

Important configuration files (see path-specific instructions for details):

**Backend:**

- `.github/instructions/backend.instructions.md` - Java/Spring Boot guidance
- `build.gradle` - Build configuration
- `application-dev.yml` / `application-prod.yml` - App configuration
- `config/checkstyle/checkstyle.xml` - Code quality rules
- `.editorconfig` - Editor settings

**Frontend:**

- `.github/instructions/frontend.instructions.md` - React/TypeScript guidance
- `vite.config.ts` - Build configuration
- `tsconfig.json` - TypeScript configuration
- `vitest.config.ts` - Test configuration
- `frontend/.env.example` - Environment variables template
- `.editorconfig` - Editor settings

**Git Configuration:**

- `.gitattributes` - Encoding and line ending consistency (UTF-8, LF)
- `.gitignore` - Files to ignore

---

## Last Updated

Documentation reorganization and Copilot instructions consolidation: 2024

For the latest updates and detailed guidance on specific technologies, see the path-specific instructions or documentation files.

**Repository**: https://github.com/pbaletkeman/java-resumes
**Issues**: GitHub Issues
**Discussions**: GitHub Discussions
