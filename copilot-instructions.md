# Copilot Instructions - java-resumes Repository

Repository-wide custom instructions for GitHub Copilot agents working on the java-resumes project.

## Project Overview

**java-resumes** is a full-stack AI-powered resume and cover letter optimization application.

- **Backend**: Spring Boot 3.5.1, Java 21 LTS, Gradle 8.10
- **Frontend**: React 19, TypeScript 5.9.3, Vite 7.2.4
- **LLM Integration**: Ollama/LM Studio with OpenAI-compatible API
- **Code Quality**: Checkstyle 10.14.2 (100% compliance), 80%+ test coverage
- **Architecture**: Async processing with background threads for long-running optimizations

---

## 🎯 Essential Architecture Pattern

**THE Core Flow** - This is the ONE critical pattern that ties everything together:

1. **User submits** via React UI (`frontend/src/components/MainContentTab.tsx`)
2. **Controller validates** (`ResumeController.optimizeResume()`) - checks file/config, returns 202 Accepted immediately
3. **Background thread spawned** (`new BackgroundResume(optimize, root)`) - runs async in separate thread
4. **LLM integration** (`ApiService.produceFiles()`) - sends to Ollama/LM Studio via OpenAI API format
5. **Files saved** (`FilesStorageService.save()`) - markdown + PDF written to filesystem
6. **UI polls** (`GET /api/files`) - frontend retrieves list and downloads results

**Why this matters**: NEVER block the HTTP response waiting for LLM completion. ALWAYS spawn BackgroundResume asynchronously. The controller returns 202 immediately; the UI polls for results.

**Config loading pattern** (`BackgroundResume` constructor):

```java
String configStr = Utility.readFileAsString("config.json");
Config c = new Gson().fromJson(configStr, Config.class);
// config.json: { "endpoint", "apikey", "model" } for LLM service
```

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

## Key Integration Points & Workflows

### Critical Backend Workflows

**REST Request → Background Processing → File Download Flow**:

- Controller receives multipart upload, validates, returns 202 immediately
- `new BackgroundResume(optimize, root)` spawned in separate thread (NEVER in controller thread)
- BackgroundResume loads config.json, calls ApiService.produceFiles()
- Files written to disk, UI polls GET /api/files until results appear

**Example from ResumeController**:

```java
// Returns 202 immediately - spawns thread for LLM work
Thread thread = new Thread(new BackgroundResume(optimize, root));
thread.start();
return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseMessage("generating"));
```

### Critical Frontend Workflows

**File Upload → Poll Results → Download Flow**:

- MainContentTab.tsx handles upload form submission
- useApi hook manages loading/error states during async operations
- Frontend polls GET /api/files every 2-3 seconds until new files appear
- User clicks download link to retrieve PDF/Markdown

**Example Pattern**:

```typescript
const { execute, loading, error } = useApi();
const onSubmit = async () => {/* Lines 108-115 omitted */};
```

### LLM Integration Points

**ApiService.produceFiles() is the gateway**:

- Reads resume + job description
- Sends OpenAI-compatible request to Ollama/LM Studio endpoint
- Parses response and extracts optimized content
- Converts to markdown, then PDF
- Saves to filesystem

**CRITICAL**: Config comes from config.json (not Spring properties). Endpoint format:

```json
{/* Lines 132-135 omitted */}
```

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
src/test/java/ca/letkeman/resumes/
├── controller/
│   └── ResumeControllerTest.java      # REST endpoint tests
├── model/
│   └── OptimizeTest.java              # Optimize model tests
└── optimizer/
    /* Lines 176-177 omitted */

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
class ResumeControllerTest {/* Lines 312-315 omitted */}
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
for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {/* Lines 423-424 omitted */}
```

---

## Development Workflow

### Adding a New Feature

1. **Analyze Requirements**
   /* Lines 434-437 omitted */

2. **Design Solution**
   /* Lines 439-442 omitted */

3. **Implement Code**
   /* Lines 444-447 omitted */

4. **Write Tests**
   /* Lines 449-452 omitted */

5. **Run Quality Checks**
/* Lines 454-460 omitted */

6. **Update Documentation**
   /* Lines 462-465 omitted */

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
public ResponseEntity<ResponseMessage> methodName(@RequestParam String param) {/* Lines 502-520 omitted */}
```

### Service Method Pattern

```java
public void save(MultipartFile file) {/* Lines 527-542 omitted */}
```

### Background Thread Pattern

```java
public class BackgroundTask implements Runnable {/* Lines 549-567 omitted */}
```

### Testing Pattern

```java
@SpringBootTest
@AutoConfigureMockMvc
class ComponentTest {/* Lines 576-590 omitted */}
```

---

## Configuration & Setup

### Local Development Setup

**1. Install Java 21 LTS**

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
{/* Lines 629-632 omitted */}
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
- **Java**: 21 (LTS)
- **Gradle**: 8.10+
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
export JAVA_HOME=/path/to/java/21
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

| Document         | Purpose                | Location                                                               |
| ---------------- | ---------------------- | ---------------------------------------------------------------------- |
| Detailed README  | Setup, features, API   | [docs/README.md](docs/README.md)                                       |
| Architecture     | Design diagrams, flows | [docs/architecture/ARCHITECTURE.md](docs/architecture/ARCHITECTURE.md) |
| Quick Start      | Initial setup          | [README.md](README.md)                                                 |
| Checkstyle Rules | Code quality           | [config/checkstyle/checkstyle.xml](config/checkstyle/checkstyle.xml)   |
| Build Config     | Dependencies           | [build.gradle](build.gradle)                                           |
```

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

// ❌ Bad: Generic exception, no logging
try {
    return processFile(file);
} catch (Exception e) {/* Lines 785-786 omitted */}
```

### Validation

```java
// ✅ Good: Clear validation with specific error
if (optimize == null || optimize.getResume().isEmpty()) {

// ❌ Bad: No validation
processRequest(optimize);
```

### Testing

```java
// ✅ Good: Clear test name and assertions
@Test
void test_upload_rejects_empty_file() throws Exception {

// ❌ Bad: Unclear test
@Test
void test1() throws Exception {/* Lines 819-820 omitted */}
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
