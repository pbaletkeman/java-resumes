# Resume Optimizer Backend

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.7-02303A.svg)](https://gradle.org/)
[![Checkstyle](https://img.shields.io/badge/Checkstyle-10.14.2-blue.svg)](https://checkstyle.org/)

A robust Java Spring Boot backend service for AI-powered resume and cover letter optimization. Provides RESTful APIs for document processing, file management, and LLM integration with comprehensive testing and code quality enforcement.

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Technology Stack](#%EF%B8%8F-technology-stack)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Running Locally](#-running-locally)
- [Testing](#-testing)
- [Docker](#-docker)
- [API Endpoints](#-api-endpoints)
- [Environment Variables](#-environment-variables)
- [Configuration Files](#-configuration-files)
- [Logging](#-logging)
- [Project Structure](#-project-structure)
- [Code Quality](#-code-quality)
- [LLM Integration](#-llm-integration)
- [Error Handling](#-error-handling)
- [Security](#-security)
- [Performance](#-performance)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)

---

## 🌟 Overview

The Resume Optimizer Backend is a production-ready Spring Boot application that processes resumes and cover letters using Large Language Models (LLMs). It provides a comprehensive REST API for document upload, AI-powered optimization, file management, and document conversion.

### Key Capabilities

- **Resume Optimization**: AI-powered resume tailoring based on job descriptions
- **Cover Letter Generation**: Automated cover letter creation
- **Document Conversion**: Markdown to PDF conversion with professional formatting
- **File Management**: Upload, download, list, and delete documents
- **LLM Integration**: Flexible integration with Ollama, LM Studio, and OpenAI
- **API Documentation**: Interactive Swagger/OpenAPI documentation
- **Health Checks**: Monitoring endpoints for container orchestration
- **Comprehensive Testing**: 80%+ code coverage with JUnit 5

---

## 🛠️ Technology Stack

### Core Technologies

| Component           | Technology             | Version | Purpose                           |
| ------------------- | ---------------------- | ------- | --------------------------------- |
| **Language**        | Java                   | 17 LTS  | Backend programming language      |
| **Framework**       | Spring Boot            | 3.5.1   | Application framework             |
| **Build Tool**      | Gradle                 | 8.7     | Build automation and dependencies |
| **Dependency Mgmt** | Spring Dependency Mgmt | 1.1.7   | Consistent dependency versions    |

### Spring Boot Starters

| Component                  | Purpose                                  |
| -------------------------- | ---------------------------------------- |
| `spring-boot-starter-web`  | RESTful web services and embedded Tomcat |
| `spring-boot-starter-test` | Testing framework with JUnit 5           |

### Libraries and Tools

| Library                | Version | Purpose                            |
| ---------------------- | ------- | ---------------------------------- |
| **SpringDoc OpenAPI**  | 2.8.7   | Swagger/OpenAPI documentation      |
| **Gson**               | 2.13.1  | JSON serialization/deserialization |
| **Jsoup**              | 1.15.4  | HTML parsing and cleaning          |
| **Flying Saucer Core** | 9.1.22  | XHTML rendering                    |
| **Flying Saucer PDF**  | 9.1.22  | PDF generation from XHTML          |
| **CommonMark**         | 0.24.0  | Markdown parsing and processing    |
| **Checkstyle**         | 10.14.2 | Code quality enforcement           |

### Testing Framework

| Component       | Version | Purpose                    |
| --------------- | ------- | -------------------------- |
| **JUnit 5**     | 5.x     | Unit testing framework     |
| **Mockito**     | 5.x     | Mocking framework          |
| **Spring Test** | 6.x     | Spring integration testing |
| **AssertJ**     | 3.x     | Fluent assertions          |

### Development Tools

| Tool                     | Version | Purpose                    |
| ------------------------ | ------- | -------------------------- |
| **Gradle Wrapper**       | 8.7     | Consistent Gradle version  |
| **Spring Boot DevTools** | 3.5.1   | Hot reload and live reload |
| **Checkstyle**           | 10.14.2 | Code style validation      |

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK)**: Version 17 or higher ([Download OpenJDK](https://adoptium.net/))
- **Gradle**: Version 8.7+ (or use included Gradle wrapper)
- **LLM Service**: Ollama, LM Studio, or OpenAI API access
- **IDE** (optional): IntelliJ IDEA, Eclipse, or VS Code with Java extensions

### Verify Installation

```bash
# Check Java version
java -version
# Output should show version 17 or higher

# Check Gradle version (if installed globally)
gradle --version

# Or use Gradle wrapper
./gradlew --version
```

---

## 💻 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/pbaletkeman/java-resumes.git
cd java-resumes
```

### 2. Build the Project

```bash
# Using Gradle wrapper (recommended)
./gradlew build

# Or using system Gradle
gradle build
```

This command will:

- Download all dependencies
- Compile Java source code
- Run all tests
- Generate JAR file in `build/libs/`

### 3. Verify Build

```bash
# Check build output
ls -lh build/libs/
# Should show: resumes-0.0.1-SNAPSHOT.jar (or similar)
```

---

## ⚙️ Configuration

### LLM Service Configuration

**File**: `config.json` (root directory)

```json
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "not-needed-for-local",
  "model": "gemma-3-4b-it"
}
```

**Configuration Options:**

| Field      | Description           | Example Values                               |
| ---------- | --------------------- | -------------------------------------------- |
| `endpoint` | LLM API endpoint URL  | `http://localhost:11434/v1/chat/completions` |
| `apikey`   | API key (if required) | `not-needed-for-local` or actual API key     |
| `model`    | Model name to use     | `gemma-3-4b-it`, `llama-3-8b`, `gpt-4`       |

**LLM Service Options:**

1. **Ollama** (Local, Free):

   ```bash
   ollama serve
   ollama pull gemma-3-4b-it
   ```

   Endpoint: `http://localhost:11434/v1/chat/completions`

2. **LM Studio** (Local, Free):
   - Download and install LM Studio
   - Load a model (e.g., llama-3-8b)
   - Start server (default port: 1234)

   Endpoint: `http://localhost:1234/v1/chat/completions`

3. **OpenAI API** (Cloud, Paid):
   ```json
   {
     "endpoint": "https://api.openai.com/v1/chat/completions",
     "apikey": "sk-your-api-key-here",
     "model": "gpt-4"
   }
   ```

### Spring Boot Configuration

**File**: `src/main/resources/application.yml`

```yaml
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    enabled: true

spring:
  servlet:
    multipart:
      max-file-size: 500KB
      max-request-size: 500KB

upload:
  path: files

llm:
  endpoint: http://127.0.0.1:11434/v1/chat/completions
  apikey: 1234567890
```

**Configuration Properties:**

| Property                                 | Description            | Default Value   |
| ---------------------------------------- | ---------------------- | --------------- |
| `spring.servlet.multipart.max-file-size` | Max file upload size   | `500KB`         |
| `upload.path`                            | File storage directory | `files`         |
| `llm.endpoint`                           | LLM service endpoint   | See config.json |
| `llm.apikey`                             | LLM API key            | See config.json |

---

## 🚀 Running Locally

### Using Gradle

**Start the application:**

```bash
./gradlew bootRun
```

**With specific profile:**

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

**Access the application:**

- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **API Docs**: http://localhost:8080/api-docs
- **Health Check**: http://localhost:8080/api/health

### Using JAR File

**Build JAR:**

```bash
./gradlew build
```

**Run JAR:**

```bash
java -jar build/libs/resumes-0.0.1-SNAPSHOT.jar
```

**With JVM options:**

```bash
java -Xms256m -Xmx512m -jar build/libs/resumes-0.0.1-SNAPSHOT.jar
```

### Hot Reload Development

**Enable continuous build:**

```bash
./gradlew bootRun --continuous
```

This watches for file changes and automatically recompiles.

### Development Commands

```bash
# Clean build
./gradlew clean build

# Build without tests (faster)
./gradlew build -x test

# Run with debug logging
./gradlew bootRun --debug

# Run on different port
./gradlew bootRun --args='--server.port=9090'
```

---

## 🧪 Testing

### Running Tests

**Run all tests:**

```bash
./gradlew test
```

**Run specific test class:**

```bash
./gradlew test --tests ResumeControllerTest
./gradlew test --tests OptimizeTest
./gradlew test --tests ApiServiceTest
```

**Run tests with output:**

```bash
./gradlew test --info
```

**Run tests with debug:**

```bash
./gradlew test --debug
```

### Test Coverage

**Generate coverage report:**

```bash
./gradlew test jacocoTestReport
```

**View coverage report:**

```bash
open build/reports/jacoco/test/html/index.html  # macOS
xdg-open build/reports/jacoco/test/html/index.html  # Linux
start build/reports/jacoco/test/html/index.html  # Windows
```

**Coverage targets:**

- **Minimum**: 80% overall coverage
- **Statements**: 80%+
- **Branches**: 75%+
- **Methods**: 80%+

### Test Structure

```
src/test/java/ca/letkeman/resumes/
├── ResumeControllerTest.java   # REST endpoint tests
├── OptimizeTest.java             # Model validation tests
├── ApiServiceTest.java           # LLM integration tests
└── [additional test files]
```

### Test Categories

**1. Controller Tests** (`ResumeControllerTest.java`):

- REST endpoint validation
- Request/response handling
- File upload/download
- Error responses
- Status codes

**2. Model Tests** (`OptimizeTest.java`):

- Data validation
- Getter/setter tests
- Serialization/deserialization
- Business logic validation

**3. Integration Tests** (`ApiServiceTest.java`):

- LLM API integration
- External service mocking
- End-to-end workflows

### Writing Tests

**Controller Test Example:**

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testUploadEndpoint() {
        Optimize optimize = new Optimize();
        optimize.setJobDescription("Test job");
        optimize.setResume("Test resume");

        ResponseEntity<ResponseMessage> response = restTemplate
            .postForEntity("/upload", optimize, ResponseMessage.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
```

**Service Test Example:**

```java
@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private FilesStorageService storageService;

    @InjectMocks
    private ResumeController controller;

    @Test
    public void testFileList() {
        when(storageService.loadAll()).thenReturn(Stream.empty());

        ResponseEntity<List<FileInfo>> response = controller.getListFiles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(storageService).loadAll();
    }
}
```

---

## 🐳 Docker

### Build Docker Image

```bash
# From project root
docker build -t resume-backend:latest .
```

### Run Docker Container

```bash
docker run -d \
  --name resume-backend \
  -p 8080:8080 \
  -v $(pwd)/files:/app/files \
  -v $(pwd)/config.json:/app/config.json:ro \
  -e SPRING_PROFILES_ACTIVE=prod \
  resume-backend:latest
```

### Docker Compose

**Start full stack:**

```bash
docker compose up --build
```

**View logs:**

```bash
docker compose logs -f backend
```

**Stop services:**

```bash
docker compose down
```

### Dockerfile Overview

**Multi-stage build:**

1. **Build Stage** (`gradle:8.7-jdk17`):
   - Copies source code
   - Builds application
   - Creates JAR file

2. **Runtime Stage** (`eclipse-temurin:17-jre-alpine`):
   - Minimal JRE image
   - Copies JAR from build stage
   - Non-root user for security
   - Health check configuration

**Image sizes:**

- Build stage: ~700MB
- Final image: ~200MB

### Docker Environment Variables

```yaml
environment:
  - SPRING_PROFILES_ACTIVE=prod
  - UPLOAD_PATH=files
  - LLM_ENDPOINT=http://host.docker.internal:11434/v1/chat/completions
  - LLM_APIKEY=not-needed-for-local
```

### Docker Health Check

```dockerfile
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/health || exit 1
```

---

## 📡 API Endpoints

### File Management Endpoints

#### List All Files

```http
GET /files
```

**Description**: Retrieves list of all uploaded and generated files

**Response**: `200 OK`

```json
[
  {
    "name": "resume-optimized.md",
    "size": 2048,
    "uploadDate": "2025-01-16T10:30:00Z",
    "type": "text/markdown"
  }
]
```

#### Download File

```http
GET /files/{filename}
```

**Description**: Downloads specific file

**Parameters**:

- `filename` (path): Name of file to download

**Response**: `200 OK` with file binary data

**Headers**:

- `Content-Type`: File MIME type
- `Content-Disposition`: `attachment; filename="..."`

#### Delete File

```http
DELETE /files/{filename}
```

**Description**: Deletes specific file

**Parameters**:

- `filename` (path): Name of file to delete

**Response**: `200 OK`

```json
{
  "message": "File deleted successfully: resume-old.pdf"
}
```

### Document Processing Endpoints

#### Upload and Optimize Documents

```http
POST /upload
```

**Description**: Upload job description and resume, generate optimized versions

**Request Body**: `application/json`

```json
{
  "jobDescription": "We are seeking a Senior Java Developer...",
  "resume": "John Doe\nSenior Software Engineer with 10+ years...",
  "jobDescriptionFile": null,
  "resumeFile": null,
  "createOptimizedResume": true,
  "createOptimizedCoverLetter": true
}
```

**Request Parameters**:

| Field                        | Type    | Required | Description               |
| ---------------------------- | ------- | -------- | ------------------------- |
| `jobDescription`             | String  | Yes\*    | Job description text      |
| `resume`                     | String  | Yes\*    | Resume text               |
| `jobDescriptionFile`         | String  | Yes\*    | Job description filename  |
| `resumeFile`                 | String  | Yes\*    | Resume filename           |
| `createOptimizedResume`      | Boolean | No       | Generate optimized resume |
| `createOptimizedCoverLetter` | Boolean | No       | Generate cover letter     |

\*Either text fields or file fields must be provided

**Response**: `200 OK`

```json
{
  "message": "Documents uploaded and processed successfully. Check /files for results."
}
```

**Generated Files**:

- `resume-optimized.md` - Optimized resume (Markdown)
- `resume-optimized.pdf` - Optimized resume (PDF)
- `cover-letter.md` - Cover letter (Markdown)
- `cover-letter.pdf` - Cover letter (PDF)

#### Convert Markdown to PDF

```http
POST /markdownFile2PDF
```

**Description**: Converts markdown file to professionally formatted PDF

**Request**: `multipart/form-data`

**Parameters**:

- `file` (file): Markdown file to convert

**Response**: `200 OK` with PDF binary data

**Headers**:

- `Content-Type`: `application/pdf`
- `Content-Disposition`: `attachment; filename="document.pdf"`

### System Endpoints

#### Health Check

```http
GET /api/health
```

**Description**: Health check endpoint for monitoring

**Response**: `200 OK`

```json
{
  "status": "UP",
  "timestamp": "2025-01-16T10:30:00Z"
}
```

### Error Responses

All endpoints return consistent error responses:

**400 Bad Request**:

```json
{
  "error": "VALIDATION_ERROR",
  "message": "Job description is required",
  "timestamp": "2025-01-16T10:30:00Z"
}
```

**404 Not Found**:

```json
{
  "error": "FILE_NOT_FOUND",
  "message": "File not found: resume.pdf",
  "timestamp": "2025-01-16T10:30:00Z"
}
```

**500 Internal Server Error**:

```json
{
  "error": "INTERNAL_SERVER_ERROR",
  "message": "An unexpected error occurred",
  "timestamp": "2025-01-16T10:30:00Z"
}
```

### API Documentation

**Swagger UI**: http://localhost:8080/swagger-ui/index.html

Interactive API documentation with try-it-out functionality.

**OpenAPI Spec**: http://localhost:8080/api-docs

JSON specification for API tools and clients.

---

## 🔐 Environment Variables

### Application Configuration

| Variable                 | Description            | Default            | Required |
| ------------------------ | ---------------------- | ------------------ | -------- |
| `SPRING_PROFILES_ACTIVE` | Active Spring profile  | `default`          | No       |
| `SERVER_PORT`            | Server port            | `8080`             | No       |
| `UPLOAD_PATH`            | File storage directory | `files`            | No       |
| `LLM_ENDPOINT`           | LLM service endpoint   | From `config.json` | Yes      |
| `LLM_APIKEY`             | LLM API key            | From `config.json` | Yes      |

### JVM Options

| Option                     | Description                | Recommended Value |
| -------------------------- | -------------------------- | ----------------- |
| `-Xms`                     | Initial heap size          | `256m`            |
| `-Xmx`                     | Maximum heap size          | `512m`            |
| `-XX:MaxRAMPercentage`     | Max RAM for container      | `75.0`            |
| `-XX:+UseContainerSupport` | Enable container awareness | `true`            |

**Example:**

```bash
java -Xms256m -Xmx512m -XX:+UseContainerSupport \
  -jar build/libs/resumes-0.0.1-SNAPSHOT.jar
```

### Docker Environment Variables

```yaml
# docker-compose.yml
environment:
  - SPRING_PROFILES_ACTIVE=prod
  - UPLOAD_PATH=files
  - LLM_ENDPOINT=${LLM_ENDPOINT:-http://host.docker.internal:11434/v1/chat/completions}
  - LLM_APIKEY=${LLM_APIKEY:-not-needed-for-local}
```

---

## 📋 Configuration Files

### application.yml

**Location**: `src/main/resources/application.yml`

```yaml
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    enabled: true

spring:
  servlet:
    multipart:
      max-file-size: 500KB
      max-request-size: 500KB

upload:
  path: files

llm:
  endpoint: http://127.0.0.1:11434/v1/chat/completions
  apikey: 1234567890
```

### config.json

**Location**: Root directory

```json
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "not-needed-for-local",
  "model": "gemma-3-4b-it"
}
```

**Purpose**: LLM service configuration read at runtime

### build.gradle

**Location**: Root directory

```gradle
plugins {
    id 'org.springframework.boot' version '3.5.1'
    id 'io.spring.dependency-management' version '1.1.7'
    id 'java'
    id 'checkstyle'
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-api:2.8.7'
    // ... additional dependencies
}
```

### checkstyle.xml

**Location**: `config/checkstyle/checkstyle.xml`

Defines code style rules based on Google Java Style Guide.

---

## 📝 Logging

### Logging Configuration

**Default Logging**:

- Level: INFO
- Output: Console
- Format: Spring Boot default

**Enable Debug Logging**:

```yaml
# application.yml
logging:
  level:
    ca.letkeman.resumes: DEBUG
    org.springframework.web: DEBUG
```

**Log Files** (optional):

```yaml
logging:
  file:
    name: logs/application.log
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
```

### Logging Best Practices

**1. Log Levels:**

- `ERROR`: System errors, exceptions
- `WARN`: Warnings, deprecated usage
- `INFO`: Important events, startup info
- `DEBUG`: Detailed debugging information
- `TRACE`: Very detailed debugging

**2. Structured Logging:**

```java
log.info("Processing resume for user: {}", userId);
log.error("Failed to process document", exception);
```

**3. Sensitive Data:**

- Never log passwords or API keys
- Mask sensitive information
- Use appropriate log levels

**4. Performance:**

- Use parameterized logging
- Avoid expensive operations in logs
- Use appropriate log levels

### Viewing Logs

**Console output:**

```bash
./gradlew bootRun
```

**Docker logs:**

```bash
docker compose logs -f backend
```

**Log file:**

```bash
tail -f logs/application.log
```

---

## 📁 Project Structure

```
java-resumes/
├── src/
│   ├── main/
│   │   ├── java/ca/letkeman/resumes/
│   │   │   ├── controller/              # REST Controllers
│   │   │   │   └── ResumeController.java
│   │   │   │       - REST API endpoints
│   │   │   │       - Request/response handling
│   │   │   │       - File operations
│   │   │   │
│   │   │   ├── service/                 # Business Logic
│   │   │   │   ├── FilesStorageService.java (interface)
│   │   │   │   └── FilesStorageServiceImpl.java
│   │   │   │       - File storage operations
│   │   │   │       - File listing, upload, delete
│   │   │   │
│   │   │   ├── model/                   # Data Models
│   │   │   │   ├── Optimize.java
│   │   │   │   │   - Request payload for optimization
│   │   │   │   └── FileInfo.java
│   │   │   │       - File metadata
│   │   │   │
│   │   │   ├── optimizer/               # LLM Integration
│   │   │   │   ├── ApiService.java
│   │   │   │   │   - LLM API communication
│   │   │   │   ├── HtmlToPdf.java
│   │   │   │   │   - PDF conversion
│   │   │   │   ├── ChatBody.java
│   │   │   │   │   - LLM request structure
│   │   │   │   ├── Message.java
│   │   │   │   │   - Chat message structure
│   │   │   │   └── responses/
│   │   │   │       ├── LLMResponse.java
│   │   │   │       ├── Choice.java
│   │   │   │       ├── Message.java
│   │   │   │       ├── Usage.java
│   │   │   │       └── Stats.java
│   │   │   │
│   │   │   ├── message/                 # Response Messages
│   │   │   │   └── ResponseMessage.java
│   │   │   │       - API response wrapper
│   │   │   │
│   │   │   ├── Config.java              # Configuration
│   │   │   │   - Reads config.json
│   │   │   │   - LLM configuration
│   │   │   │
│   │   │   ├── Utility.java             # Utility Methods
│   │   │   │   - Helper functions
│   │   │   │   - Common operations
│   │   │   │
│   │   │   ├── BackgroundResume.java    # Background Tasks
│   │   │   │   - Async processing
│   │   │   │
│   │   │   └── RestServiceApplication.java
│   │   │       - Spring Boot main class
│   │   │       - Application entry point
│   │   │
│   │   └── resources/
│   │       ├── application.yml          # Spring configuration
│   │       └── static/                  # Static resources
│   │
│   └── test/                            # Test Files
│       └── java/ca/letkeman/resumes/
│           ├── ResumeControllerTest.java
│           ├── OptimizeTest.java
│           └── ApiServiceTest.java
│
├── config/                              # Configuration
│   └── checkstyle/
│       └── checkstyle.xml              # Code style rules
│
├── gradle/                              # Gradle Wrapper
│   └── wrapper/
│
├── build/                               # Build Output
│   ├── classes/                        # Compiled classes
│   ├── libs/                           # JAR files
│   └── reports/                        # Test/coverage reports
│
├── files/                               # File Storage (runtime)
│
├── build.gradle                         # Gradle build script
├── settings.gradle                      # Gradle settings
├── gradle.properties                    # Gradle properties
├── gradlew                              # Gradle wrapper (Unix)
├── gradlew.bat                          # Gradle wrapper (Windows)
├── config.json                          # LLM configuration
├── Dockerfile                           # Docker build config
├── .gitignore                           # Git ignore rules
└── BACKEND_README.md                    # This file
```

### Package Organization

**Controller Layer** (`controller/`):

- REST API endpoints
- Request validation
- Response formatting
- Exception handling

**Service Layer** (`service/`):

- Business logic
- File operations
- Transaction management

**Model Layer** (`model/`):

- Data transfer objects (DTOs)
- Request/response models
- Validation annotations

**Optimizer Layer** (`optimizer/`):

- LLM integration
- Document processing
- PDF generation

**Configuration** (`Config.java`):

- Application configuration
- External service config
- Bean definitions

---

## 🔍 Code Quality

### Checkstyle

**Run Checkstyle:**

```bash
# Check main code
./gradlew checkstyleMain

# Check test code
./gradlew checkstyleTest

# Check all code
./gradlew check
```

**View reports:**

```bash
open build/reports/checkstyle/main.html  # macOS
xdg-open build/reports/checkstyle/main.html  # Linux
start build/reports/checkstyle/main.html  # Windows
```

### Code Style Standards

Based on Google Java Style Guide:

**Naming Conventions:**

- Classes: PascalCase (`ResumeController`)
- Methods: camelCase (`uploadDocument`)
- Constants: UPPER_SNAKE_CASE (`MAX_FILE_SIZE`)
- Variables: camelCase (`fileName`)

**Formatting:**

- Indentation: 2 spaces (not tabs)
- Line length: Maximum 100 characters
- Braces: K&R style (opening brace on same line)
- Imports: No wildcard imports

**Documentation:**

- Public APIs: Javadoc required
- Complex logic: Inline comments
- Package: package-info.java with description

**Example:**

```java
/**
 * REST controller for resume optimization operations.
 * Provides endpoints for file upload, processing, and management.
 */
@RestController
public class ResumeController {

  private static final int MAX_FILE_SIZE = 500_000; // 500KB

  private final FilesStorageService storageService;

  /**
   * Uploads and processes resume and job description.
   *
   * @param optimize the optimization request containing documents
   * @return response message with processing status
   */
  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFiles(
      @RequestBody Optimize optimize) {
    // Implementation
  }
}
```

### Static Analysis

**Build with checks:**

```bash
./gradlew clean build
```

This runs:

- Compilation
- Checkstyle
- Tests
- Test coverage

**Fix common issues:**

```bash
# Remove unused imports
# Organize imports
# Format code according to style guide
```

---

## 🤖 LLM Integration

### LLM Service Architecture

```
Application
    ↓
ApiService.java
    ↓
HTTP POST (JSON)
    ↓
LLM Service (Ollama/LM Studio/OpenAI)
    ↓
Response (JSON)
    ↓
Parse & Process
    ↓
Generate Documents
```

### ApiService Implementation

**Location**: `src/main/java/ca/letkeman/resumes/optimizer/ApiService.java`

**Key Methods:**

```java
public class ApiService {

  /**
   * Sends prompt to LLM and returns response.
   *
   * @param prompt the text prompt for LLM
   * @return LLM response text
   * @throws IOException if communication fails
   */
  public String callApi(String prompt) throws IOException {
    // Create HTTP request
    // Send to LLM endpoint
    // Parse response
    // Return content
  }

  /**
   * Optimizes resume based on job description.
   *
   * @param jobDescription target job description
   * @param resume current resume text
   * @return optimized resume content
   */
  public String optimizeResume(String jobDescription, String resume) {
    String prompt = buildResumePrompt(jobDescription, resume);
    return callApi(prompt);
  }
}
```

### LLM Request Format

```java
ChatBody chatBody = new ChatBody();
chatBody.setModel(config.model);
chatBody.setMessages(Arrays.asList(
    new Message("system", "You are a professional resume optimizer."),
    new Message("user", userPrompt)
));
chatBody.setTemperature(0.7);
chatBody.setMax_tokens(2000);
```

### Error Handling

```java
try {
  String response = apiService.callApi(prompt);
  // Process response
} catch (IOException e) {
  log.error("Failed to communicate with LLM service", e);
  throw new RuntimeException("LLM service unavailable", e);
}
```

### Testing LLM Integration

**Mock LLM responses in tests:**

```java
@Test
public void testOptimizeResume() {
  String mockResponse = "Optimized resume content...";
  when(apiService.callApi(any())).thenReturn(mockResponse);

  String result = controller.optimizeResume(jobDesc, resume);

  assertNotNull(result);
  assertTrue(result.contains("optimized"));
}
```

---

## ⚠️ Error Handling

### Exception Hierarchy

```
RuntimeException
├── StorageException
│   ├── StorageFileNotFoundException
│   └── StorageUploadException
├── ProcessingException
│   ├── LLMServiceException
│   └── DocumentConversionException
└── ValidationException
```

### Global Exception Handler

```java
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(StorageFileNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleFileNotFound(
      StorageFileNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(
        "FILE_NOT_FOUND",
        ex.getMessage(),
        LocalDateTime.now()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericError(
      Exception ex) {
    ErrorResponse error = new ErrorResponse(
        "INTERNAL_SERVER_ERROR",
        "An unexpected error occurred",
        LocalDateTime.now()
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
```

### Error Response Format

```json
{
  "error": "FILE_NOT_FOUND",
  "message": "File not found: resume.pdf",
  "timestamp": "2025-01-16T10:30:00Z",
  "details": {
    "requestedFile": "resume.pdf",
    "availableFiles": ["resume-optimized.md", "cover-letter.pdf"]
  }
}
```

---

## 🔒 Security

### Security Best Practices

**1. Input Validation:**

- Validate all user inputs
- Sanitize file names
- Check file sizes and types
- Prevent path traversal

```java
if (!isValidFilename(filename)) {
  throw new IllegalArgumentException("Invalid filename");
}
```

**2. File Upload Security:**

- Whitelist allowed file types
- Enforce size limits (500KB default)
- Store files outside web root
- Generate unique filenames

**3. API Security:**

- CORS configuration
- Rate limiting (future)
- Authentication/authorization (future)
- HTTPS in production

**4. Dependency Security:**

```bash
# Check for vulnerabilities
./gradlew dependencyCheckAnalyze

# Update dependencies
./gradlew dependencyUpdates
```

**5. Secrets Management:**

- No secrets in code
- Use environment variables
- Use external configuration
- Rotate API keys regularly

### CORS Configuration

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:3000")
        .allowedMethods("GET", "POST", "PUT", "DELETE")
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}
```

---

## ⚡ Performance

### Performance Optimization

**1. File I/O:**

- Buffered streams for large files
- Async processing for long operations
- Efficient file storage structure

**2. LLM API Calls:**

- Connection pooling
- Timeout configuration (30s)
- Retry logic with backoff

**3. Memory Management:**

- Stream processing for large files
- Proper resource cleanup
- JVM tuning for containers

**4. Caching** (future enhancement):

- Cache common LLM responses
- File metadata caching
- Spring Cache abstraction

### Performance Monitoring

**JVM Metrics:**

```bash
# Enable JMX
java -Dcom.sun.management.jmxremote -jar app.jar

# Monitor with JConsole or VisualVM
```

**Spring Boot Actuator** (optional):

```gradle
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```

**Endpoints:**

- `/actuator/health` - Health check
- `/actuator/metrics` - Application metrics
- `/actuator/info` - Application info

---

## 🐛 Troubleshooting

### Common Issues

#### Build Failures

**Issue**: Java version mismatch

```bash
# Check Java version
java -version

# Set JAVA_HOME
export JAVA_HOME=/path/to/jdk-17
```

**Issue**: Gradle build fails

```bash
# Clean and rebuild
./gradlew clean build --refresh-dependencies

# Use Gradle daemon
./gradlew --daemon build
```

#### Runtime Issues

**Issue**: Application won't start

```bash
# Check port availability
lsof -i :8080  # Unix
netstat -ano | findstr :8080  # Windows

# Start on different port
./gradlew bootRun --args='--server.port=9090'
```

**Issue**: LLM service connection failed

```bash
# Check LLM service is running
curl http://localhost:11434/api/version  # Ollama
curl http://localhost:1234/v1/models     # LM Studio

# Verify config.json
cat config.json

# Check application logs
tail -f logs/application.log
```

**Issue**: File upload fails

```bash
# Check file size limits
# Verify files/ directory exists and is writable
mkdir -p files
chmod 755 files

# Check disk space
df -h
```

#### Test Failures

**Issue**: Tests fail on CI/CD

```bash
# Run tests with full output
./gradlew test --info

# Run specific test with stacktrace
./gradlew test --tests ResumeControllerTest --stacktrace

# Skip flaky tests (temporary)
./gradlew build -x test
```

### Debug Mode

**Enable debug logging:**

```yaml
# application.yml
logging:
  level:
    ca.letkeman.resumes: DEBUG
    org.springframework.web: DEBUG
    org.hibernate: DEBUG
```

**Run with debug:**

```bash
./gradlew bootRun --debug
```

### Getting Help

1. Check application logs
2. Review [documentation](docs/README.md)
3. Check [Swagger UI](http://localhost:8080/swagger-ui/index.html)
4. Open issue on [GitHub](https://github.com/pbaletkeman/java-resumes/issues)

---

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

### Development Workflow

1. **Fork the repository**
2. **Create feature branch**: `git checkout -b feature/backend-feature`
3. **Make changes**: Follow code style guidelines
4. **Write tests**: Ensure 80%+ coverage
5. **Run tests**: `./gradlew test`
6. **Run Checkstyle**: `./gradlew checkstyleMain`
7. **Commit changes**: `git commit -m "feat(backend): add feature"`
8. **Push to fork**: `git push origin feature/backend-feature`
9. **Create Pull Request**

### Code Standards

- Follow Google Java Style Guide
- Pass all Checkstyle checks
- Maintain 80%+ test coverage
- Document public APIs with Javadoc
- Use meaningful variable names
- Keep methods focused and small

### Commit Message Format

```
type(scope): subject

body (optional)

footer (optional)
```

**Types:**

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation
- `style`: Code style (formatting)
- `refactor`: Code refactoring
- `test`: Test additions/updates
- `chore`: Maintenance tasks

**Example:**

```
feat(api): add resume optimization endpoint

Implement POST /optimize endpoint for AI-powered resume optimization.
Uses LLM service to tailor resumes based on job descriptions.

Closes #123
```

---

**For complete documentation, see the [Root README](README.md) and [PRD](PRD-PRIMEREACT-DOCKER-v2.md)**

**Made with ☕ using Java, Spring Boot, and AI**
