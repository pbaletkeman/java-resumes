# Resume Optimizer Backend

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://openjdk.java.net/) | [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.1-brightgreen.svg)](https://spring.io/projects/spring-boot) | [![Gradle](https://img.shields.io/badge/Gradle-8.10-02303A.svg)](https://gradle.org/) | [![Checkstyle](https://img.shields.io/badge/Checkstyle-10.14.2-blue.svg)](https://checkstyle.org/)

A robust Java Spring Boot backend service for AI-powered resume and cover letter optimization. Provides RESTful APIs for document processing, file management, and LLM integration with comprehensive testing and code quality enforcement.

---

- [Resume Optimizer Backend](#resume-optimizer-backend)
  - [Overview](#overview)
    - [Key Capabilities](#key-capabilities)
  - [Quick Start in 5 Steps](#quick-start-in-5-steps)
    - [📋 Prerequisites](#prerequisites)
    - [Steps](#steps)
    - [Access the Application](#access-the-application)
  - [🛠️ Technology Stack](#technology-stack)
    - [Core Technologies](#core-technologies)
    - [Spring Boot Starters](#spring-boot-starters)
    - [Libraries and Tools](#libraries-and-tools)
    - [Testing Framework](#testing-framework)
    - [Development Tools](#development-tools)
  - [Prerequisites](#prerequisites-1)
    - [Verify Installation](#verify-installation)
  - [⚙️ Installation](#installation)
    - [1. Clone the Repository](#1-clone-the-repository)
    - [2. Build the Project](#2-build-the-project)
    - [3. Verify Build](#3-verify-build)
  - [🔧 Configuration](#configuration)
    - [LLM Service Configuration](#llm-service-configuration)
    - [Spring Boot Configuration](#spring-boot-configuration)
  - [Getting Started Without Docker](#getting-started-without-docker)
    - [Prerequisites](#prerequisites-2)
    - [Step 1: Clone the Repository](#step-1-clone-the-repository)
    - [Step 2: Set Up LLM Service](#step-2-set-up-llm-service)
      - [Option A: Ollama (Recommended - Free, Local)](#option-a-ollama-recommended---free-local)
      - [Option B: LM Studio (Free, Local)](#option-b-lm-studio-free-local)
      - [Option C: OpenAI API (Paid, Cloud)](#option-c-openai-api-paid-cloud)
    - [Step 3: Configure LLM Endpoint](#step-3-configure-llm-endpoint)
    - [Step 4: Build the Project](#step-4-build-the-project)
    - [Step 5: Run the Application](#step-5-run-the-application)
    - [Step 6: Verify Installation](#step-6-verify-installation)
    - [Development Setup](#development-setup)
      - [Configure IDE](#configure-ide)
      - [Enable Hot Reload](#enable-hot-reload)
      - [Run Tests During Development](#run-tests-during-development)
      - [Check Code Quality](#check-code-quality)
    - [Directory Structure](#directory-structure)
    - [Environment Variables](#environment-variables)
    - [Common Development Tasks](#common-development-tasks)
    - [Troubleshooting Native Setup](#troubleshooting-native-setup)
  - [Running Locally](#running-locally)
    - [Using Gradle](#using-gradle)
    - [Using JAR File](#using-jar-file)
    - [Hot Reload Development](#hot-reload-development)
    - [Development Commands](#development-commands)
  - [Testing](#testing)
    - [Running Tests](#running-tests)
    - [Test Coverage](#test-coverage)
    - [Test Structure](#test-structure)
    - [Test Categories](#test-categories)
    - [Writing Tests](#writing-tests)
  - [Docker](#docker)
    - [Build Docker Image](#build-docker-image)
    - [Run Docker Container](#run-docker-container)
    - [Docker Compose](#docker-compose)
    - [Dockerfile Overview](#dockerfile-overview)
    - [Docker Environment Variables](#docker-environment-variables)
    - [Docker Health Check](#docker-health-check)
  - [API Endpoints](#api-endpoints)
    - [File Management Endpoints](#file-management-endpoints)
      - [List All Files](#list-all-files)
      - [Download File](#download-file)
      - [Delete File](#delete-file)
    - [Document Processing Endpoints](#document-processing-endpoints)
      - [Upload and Optimize Resume](#upload-and-optimize-resume)
      - [Process Resume](#process-resume)
      - [Process Cover Letter](#process-cover-letter)
      - [Process Skills](#process-skills)
      - [Convert Markdown to PDF](#convert-markdown-to-pdf)
      - [Convert Markdown to DOCX](#convert-markdown-to-docx)
    - [Interview Preparation Endpoints](#interview-preparation-endpoints)
      - [Generate HR Interview Questions](#generate-hr-interview-questions)
      - [Generate Job-Specific Interview Questions](#generate-job-specific-interview-questions)
      - [Generate Reverse Interview Questions](#generate-reverse-interview-questions)
    - [Networking and Outreach Endpoints](#networking-and-outreach-endpoints)
      - [Generate Cold Email](#generate-cold-email)
      - [Generate Cold LinkedIn Message](#generate-cold-linkedin-message)
      - [Generate Thank You Email](#generate-thank-you-email)
    - [System Endpoints](#system-endpoints)
      - [Health Check](#health-check)
    - [Error Responses](#error-responses)
    - [API Documentation](#api-documentation)
  - [Environment Variables](#environment-variables-1)
    - [Application Configuration](#application-configuration)
    - [JVM Options](#jvm-options)
    - [Docker Environment Variables](#docker-environment-variables-1)
  - [Configuration Files](#configuration-files)
    - [application.yml](#applicationyml)
    - [config.json](#configjson)
    - [build.gradle](#buildgradle)
    - [checkstyle.xml](#checkstylexml)
  - [Logging](#logging)
    - [Logging Configuration](#logging-configuration)
    - [Logging Best Practices](#logging-best-practices)
    - [Viewing Logs](#viewing-logs)
  - [Project Structure](#project-structure)
    - [Package Organization](#package-organization)
  - [Code Quality](#code-quality)
    - [Checkstyle](#checkstyle)
    - [Code Style Standards](#code-style-standards)
    - [Static Analysis](#static-analysis)
  - [LLM Integration](#llm-integration)
    - [LLM Service Architecture](#llm-service-architecture)
    - [ApiService Implementation](#apiservice-implementation)
    - [LLM Request Format](#llm-request-format)
    - [Error Handling](#error-handling)
    - [Testing LLM Integration](#testing-llm-integration)
  - [Error Handling](#error-handling-1)
    - [Exception Hierarchy](#exception-hierarchy)
    - [Global Exception Handler](#global-exception-handler)
    - [Error Response Format](#error-response-format)
  - [Security](#security)
    - [Security Best Practices](#security-best-practices)
    - [CORS Configuration](#cors-configuration)
  - [Performance](#performance)
    - [Performance Optimization](#performance-optimization)
    - [Performance Monitoring](#performance-monitoring)
  - [Troubleshooting](#troubleshooting)
    - [Common Issues](#common-issues)
      - [Build Failures](#build-failures)
      - [Runtime Issues](#runtime-issues)
      - [Test Failures](#test-failures)
    - [Debug Mode](#debug-mode)
    - [Getting Help](#getting-help)
  - [Contributing](#contributing)
    - [Development Workflow](#development-workflow)
    - [Code Standards](#code-standards)
    - [Commit Message Format](#commit-message-format)

---

## ✅ Overview

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

## 🏃 Quick Start in 5 Steps

Get the backend running in just 5 minutes! Follow these quick steps to start developing:

### Prerequisites

- Java 21 LTS JDK installed
- Gradle 8.10+ (or use included wrapper)
- LLM service running (Ollama recommended)

### Steps

**Step 1: Clone and navigate**

```bash
git clone https://github.com/pbaletkeman/java-resumes.git
cd java-resumes
```

**Step 2: Start an LLM service** (open separate terminal)

```bash
ollama serve
# In another terminal: ollama pull gemma-3-4b-it
```

**Step 3: Configure LLM endpoint**

```bash
# Edit config.json (or leave as default for local Ollama)
cat > config.json << 'EOF'
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "not-needed-for-local",
  "model": "gemma-3-4b-it"
}
EOF
```

**Step 4: Build the application**

```bash
./gradlew build
```

**Step 5: Run the backend**

```bash
./gradlew bootRun
```

### Access the Application

- **REST API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Health Check**: http://localhost:8080/api/health

That's it! Your backend is now running. Start uploading resumes and generating optimized versions.

---

## Technology Stack

### Core Technologies

| Component           | Technology             | Version | Purpose                           |
| ------------------- | ---------------------- | ------- | --------------------------------- |
| **Language**        | Java                   | 21 LTS  | Backend programming language      |
| **Framework**       | Spring Boot            | 3.5.1   | Application framework             |
| **Build Tool**      | Gradle                 | 8.10    | Build automation and dependencies |
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
| **Gradle Wrapper**       | 8.10    | Consistent Gradle version  |
| **Spring Boot DevTools** | 3.5.1   | Hot reload and live reload |
| **Checkstyle**           | 10.14.2 | Code style validation      |

---

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK)**: Version 21 LTS ([Download Eclipse Temurin](https://adoptium.net/))
- **Gradle**: Version 8.7+ (or use included Gradle wrapper)
- **LLM Service**: Ollama, LM Studio, or OpenAI API access
- **IDE** (optional): IntelliJ IDEA, Eclipse, or VS Code with Java extensions

### Verify Installation

```bash
# Check Java version
java -version
# Output should show version 21 or higher

# Check Gradle version (if installed globally)
gradle --version

# Or use Gradle wrapper
./gradlew --version
```

---

## Installation

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

## Configuration

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

## 🚀 Getting Started Without Docker

If you prefer to run the backend without Docker, follow these native setup instructions.

### Prerequisites

- **Java 21 LTS JDK** installed
  ```bash
  java -version  # Verify you see Java 21
  ```
- **Gradle 8.10+** (or use included wrapper)
  ```bash
  ./gradlew --version  # Verify Gradle is installed
  ```
- **LLM Service**: Ollama, LM Studio, or OpenAI API
- **Git** for version control
- **IDE** (optional): IntelliJ IDEA, Eclipse, or VS Code

### Step 1: Clone the Repository

```bash
git clone https://github.com/pbaletkeman/java-resumes.git
cd java-resumes
```

### Step 2: Set Up LLM Service

Choose one of the following options:

#### Option A: Ollama (Recommended - Free, Local)

**Install Ollama**:

- Download from https://ollama.ai
- Follow installation instructions for your OS

**Start Ollama Service**:

```bash
# Start Ollama in background
ollama serve

# In another terminal, pull a model
ollama pull gemma-3-4b-it

# Or use a different model
ollama pull llama2
ollama pull mistral
```

**Verify Ollama**:

```bash
curl http://localhost:11434/api/version
```

#### Option B: LM Studio (Free, Local)

**Install LM Studio**:

- Download from https://lmstudio.ai
- Install and launch application

**Load a Model**:

1. Open LM Studio
2. Click "Browse Models"
3. Select and download a model (e.g., `Mistral 7B`)
4. Click the download icon
5. Wait for download to complete

**Start Local Server**:

1. Go to "Local Server" tab
2. Click "Start Server"
3. Default endpoint: `http://localhost:1234/v1/chat/completions`

#### Option C: OpenAI API (Paid, Cloud)

**Get API Key**:

1. Sign up at https://openai.com
2. Get your API key from settings
3. Set environment variable:
   ```bash
   export OPENAI_API_KEY="sk-your-key-here"  # Unix/Mac
   set OPENAI_API_KEY=sk-your-key-here       # Windows
   ```

### Step 3: Configure LLM Endpoint

Create or edit `config.json` in the project root:

```json
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "not-needed-for-local",
  "model": "gemma-3-4b-it"
}
```

**For different LLM services**:

- **Ollama**: `http://localhost:11434/v1/chat/completions`
- **LM Studio**: `http://localhost:1234/v1/chat/completions`
- **OpenAI**: `https://api.openai.com/v1/chat/completions`

### Step 4: Build the Project

```bash
./gradlew clean build
```

This command:

- Downloads all dependencies
- Compiles Java source code
- Runs all tests
- Generates JAR file

**Troubleshooting build issues**:

```bash
# If build fails, try:
./gradlew clean build --refresh-dependencies

# For verbose output:
./gradlew build --info

# Skip tests (faster):
./gradlew build -x test
```

### Step 5: Run the Application

**Using Gradle (with hot reload)**:

```bash
./gradlew bootRun
```

**Using JAR file**:

```bash
java -jar build/libs/resumes-0.0.1-SNAPSHOT.jar
```

**With custom JVM options**:

```bash
java -Xms512m -Xmx1024m \
  -Dlogging.level.ca.letkeman=DEBUG \
  -jar build/libs/resumes-0.0.1-SNAPSHOT.jar
```

### Step 6: Verify Installation

Open your browser and test the following:

1. **Health Check**: <http://localhost:8080/api/health>
   - Should return: `{"status":"UP"}`

2. **Swagger UI**: <http://localhost:8080/swagger-ui/index.html>
   - Should show interactive API documentation

3. **API Docs**: <http://localhost:8080/api-docs>
   - Should return JSON OpenAPI specification

### Development Setup

#### Configure IDE

**IntelliJ IDEA**:

1. Open project root
2. Select "gradle" as build tool
3. Set JDK to Java 21
4. Enable annotations processing (Settings > Build > Compiler > Annotation Processors)

**VS Code**:

1. Install "Extension Pack for Java"
2. Install "Gradle for Java"
3. Open project root folder

#### Enable Hot Reload

```bash
./gradlew bootRun --continuous
```

This watches for changes and automatically recompiles code.

#### Run Tests During Development

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests ResumeControllerTest

# Run tests in watch mode
./gradlew test --continuous

# Run with coverage report
./gradlew test jacocoTestReport
```

#### Check Code Quality

```bash
# Run Checkstyle
./gradlew checkstyleMain checkstyleTest

# View detailed report
./gradlew checkstyleMain
# Report: build/reports/checkstyle/main.html
```

### Directory Structure

After setup, you should have:

```
java-resumes/
 src/main/          # Source code
 src/test/          # Test code
 build/             # Compiled output
 files/             # File storage (created at runtime)
 config/            # Configuration files
 build.gradle       # Build configuration
 config.json        # LLM configuration
 Dockerfile         # Docker config (optional)
 docker-compose.yml # Compose config (optional)
 README.md          # This file
```

### Environment Variables

Set these for custom configuration:

```bash
# LLM Configuration
export LLM_ENDPOINT="http://localhost:11434/v1/chat/completions"
export LLM_APIKEY="your-api-key"

# Server Configuration
export SERVER_PORT=9090

# Upload Configuration
export UPLOAD_PATH="./uploads"

# Logging
export LOGGING_LEVEL_CA_LETKEMAN="DEBUG"
```

### Common Development Tasks

```bash
# Full clean build
./gradlew clean build

# Build without running tests
./gradlew build -x test

# Run application with debug logging
./gradlew bootRun --debug

# Run on different port
./gradlew bootRun --args='--server.port=9090'

# Stop the running application
Ctrl+C

# Clean build artifacts
./gradlew clean

# Check dependencies for updates
./gradlew dependencyUpdates

# Generate test coverage report
./gradlew test jacocoTestReport
# Open: build/reports/jacoco/test/html/index.html
```

### Troubleshooting Native Setup

**Java Version Mismatch**:

```bash
# Check Java version
java -version

# If wrong version, set JAVA_HOME
export JAVA_HOME=/path/to/jdk-21  # Unix/Mac
set JAVA_HOME=C:\path\to\jdk-21   # Windows
```

**LLM Connection Error**:

```bash
# Check if Ollama is running
curl http://localhost:11434/api/version

# Check if LM Studio is running
curl http://localhost:1234/v1/models

# Verify config.json endpoint
cat config.json
```

**Build Failures**:

```bash
# Clear Gradle cache
rm -rf ~/.gradle/caches

# Retry build
./gradlew clean build --refresh-dependencies
```

**Port Already in Use**:

```bash
# Find what's using port 8080
lsof -i :8080              # Unix/Mac
netstat -ano | findstr 8080  # Windows

# Use different port
./gradlew bootRun --args='--server.port=9090'
```

---

## 🏃 Running Locally

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

- **API Base URL**: <http://localhost:8080>
- **Swagger UI**: <http://localhost:8080/swagger-ui/index.html>
- **API Docs**: <http://localhost:8080/api-docs>
- **Health Check**: <http://localhost:8080/api/health>

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
 ResumeControllerTest.java   # REST endpoint tests
 OptimizeTest.java             # Model validation tests
 ApiServiceTest.java           # LLM integration tests
 [additional test files]
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

1. **Build Stage** (`gradle:8.7-jdk25`):
   - Copies source code
   - Builds application
   - Creates JAR file

2. **Runtime Stage** (`eclipse-temurin:21-jre-alpine`):
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

**Description**: Retrieves list of all uploaded and generated files with metadata

**Response**: `200 OK`

```json
[
  {
    "name": "resume-optimized.md",
    "url": "http://localhost:8080/files/resume-optimized.md",
    "date": "2025-01-16 10:30"
  },
  {
    "name": "cover-letter.pdf",
    "url": "http://localhost:8080/files/cover-letter.pdf",
    "date": "2025-01-16 10:31"
  }
]
```

**Response Fields**:

| Field | Type   | Description                           |
| ----- | ------ | ------------------------------------- |
| name  | String | File name                             |
| url   | String | Download URL for the file             |
| date  | String | Last modified date (yyyy-MM-dd HH:mm) |

#### Download File

```http
GET /files/{filename}
```

**Description**: Downloads specific file by name

**Parameters**:

| Parameter  | Type   | Required | Description              |
| ---------- | ------ | -------- | ------------------------ |
| `filename` | String | Yes      | Name of file to download |

**Response**: `200 OK` with file binary data

**Headers**:

- `Content-Type`: File MIME type
- `Content-Disposition`: `attachment; filename="..."`

**Example**:

```bash
curl -O http://localhost:8080/api/files/resume-optimized.pdf
```

#### Delete File

```http
DELETE /files/{filename}
```

**Description**: Deletes specific file

**Parameters**:

| Parameter  | Type   | Required | Description            |
| ---------- | ------ | -------- | ---------------------- |
| `filename` | String | Yes      | Name of file to delete |

**Response**: `200 OK`

```json
{
  "message": "Delete the file successfully: resume-optimized.pdf"
}
```

**Error Response** (`404 Not Found`):

```json
{
  "message": "The file does not exist!"
}
```

**Example**:

```bash
curl -X DELETE http://localhost:8080/api/files/resume-optimized.pdf
```

### Document Processing Endpoints

#### Upload and Optimize Resume

```http
POST /upload
```

**Description**: Upload resume and job description to generate optimized resume and cover letter

**Request**: `multipart/form-data`

**Parameters**:

| Parameter  | Type        | Required | Description                                  |
| ---------- | ----------- | -------- | -------------------------------------------- |
| `resume`   | File        | No\*     | Resume file to upload                        |
| `job`      | File        | No\*     | Job description file to upload               |
| `optimize` | JSON String | No       | Optimization parameters (see Optimize model) |

**Optimize JSON Parameters**:

```json
{
  "resume": "John Doe\nSenior Software Engineer with 10+ years...",
  "jobDescription": "We are seeking a Senior Java Developer...",
  "promptType": ["resume-optimization"]
}
```

**Response**: `200 OK`

```json
{
  "message": "generating"
}
```

**Example cURL**:

```bash
curl -X POST http://localhost:8080/api/upload \
  -F "resume=@resume.txt" \
  -F "job=@job-description.txt"
```

#### Process Resume

```http
POST /process/resume
```

**Description**: Process resume with job description (alias for `/upload`)

**Request**: `multipart/form-data`

**Parameters**: Same as `/upload`

**Response**: `200 OK`

```json
{
  "message": "generating"
}
```

#### Process Cover Letter

```http
POST /process/cover-letter
```

**Description**: Generate optimized cover letter based on resume and job description

**Request**: `multipart/form-data`

**Parameters**:

| Parameter     | Type        | Required | Description                    |
| ------------- | ----------- | -------- | ------------------------------ |
| `coverLetter` | File        | No\*     | Cover letter file to upload    |
| `job`         | File        | No\*     | Job description file to upload |
| `optimize`    | JSON String | No       | Optimization parameters        |

**Response**: `200 OK`

```json
{
  "message": "generating"
}
```

#### Process Skills

```http
POST /process/skills
```

**Description**: Generate skill recommendations based on job description

**Request**: `multipart/form-data`

**Parameters**:

| Parameter  | Type        | Required | Description                    |
| ---------- | ----------- | -------- | ------------------------------ |
| `job`      | File        | Yes      | Job description file to upload |
| `optimize` | JSON String | No       | Optimization parameters        |

**Response**: `202 Accepted`

```json
{
  "message": "Skills suggestion generation started"
}
```

#### Convert Markdown to PDF

```http
POST /markdownFile2PDF
```

**Description**: Converts markdown file to professionally formatted PDF

**Request**: `multipart/form-data`

**Parameters**:

| Parameter | Type | Required | Description   |
| --------- | ---- | -------- | ------------- |
| `file`    | File | Yes      | Markdown file |

**Response**: `200 OK` with PDF binary data

**Headers**:

- `Content-Type`: `application/pdf`
- `Content-Disposition`: `attachment; filename="document.pdf"`

**Example cURL**:

```bash
curl -X POST http://localhost:8080/api/markdownFile2PDF \
  -F "file=@resume.md" \
  -o resume.pdf
```

#### Convert Markdown to DOCX

```http
POST /markdownFile2DOCX
```

**Description**: Converts markdown file to Microsoft Word format (.docx)

**Request**: `multipart/form-data`

**Parameters**:

| Parameter | Type | Required | Description   |
| --------- | ---- | -------- | ------------- |
| `file`    | File | Yes      | Markdown file |

**Response**: `200 OK` with DOCX binary data

**Headers**:

- `Content-Type`: `application/vnd.openxmlformats-officedocument.wordprocessingml.document`
- `Content-Disposition`: `attachment; filename="document.docx"`

### Interview Preparation Endpoints

#### Generate HR Interview Questions

```http
POST /generate/interview-hr-questions
```

**Description**: Generate common HR interview questions and suggested answers based on resume and job description

**Request**: `multipart/form-data`

**Parameters**:

| Parameter  | Type        | Required | Description                    |
| ---------- | ----------- | -------- | ------------------------------ |
| `job`      | File        | No\*     | Job description file to upload |
| `optimize` | JSON String | No       | Optimization parameters        |

**Response**: `202 Accepted`

```json
{
  "message": "generating"
}
```

#### Generate Job-Specific Interview Questions

```http
POST /generate/interview-job-specific
```

**Description**: Generate technical and role-specific interview questions

**Request**: `multipart/form-data`

**Parameters**:

| Parameter  | Type        | Required | Description                    |
| ---------- | ----------- | -------- | ------------------------------ |
| `job`      | File        | No\*     | Job description file to upload |
| `optimize` | JSON String | No       | Optimization parameters        |

**Response**: `202 Accepted`

```json
{
  "message": "generating"
}
```

#### Generate Reverse Interview Questions

```http
POST /generate/interview-reverse
```

**Description**: Generate questions to ask the interviewer about the role and company

**Request**: `multipart/form-data`

**Parameters**:

| Parameter  | Type        | Required | Description                    |
| ---------- | ----------- | -------- | ------------------------------ |
| `job`      | File        | No\*     | Job description file to upload |
| `optimize` | JSON String | No       | Optimization parameters        |

**Response**: `202 Accepted`

```json
{
  "message": "generating"
}
```

### Networking and Outreach Endpoints

#### Generate Cold Email

```http
POST /generate/cold-email
```

**Description**: Generate personalized cold outreach email based on job description

**Request**: `multipart/form-data`

**Parameters**:

| Parameter  | Type        | Required | Description                    |
| ---------- | ----------- | -------- | ------------------------------ |
| `job`      | File        | No\*     | Job description file to upload |
| `optimize` | JSON String | No       | Optimization parameters        |

**Response**: `202 Accepted`

```json
{
  "message": "generating"
}
```

#### Generate Cold LinkedIn Message

```http
POST /generate/cold-linkedin-message
```

**Description**: Generate personalized LinkedIn outreach message

**Request**: `multipart/form-data`

**Parameters**:

| Parameter  | Type        | Required | Description                    |
| ---------- | ----------- | -------- | ------------------------------ |
| `job`      | File        | No\*     | Job description file to upload |
| `optimize` | JSON String | No       | Optimization parameters        |

**Response**: `202 Accepted`

```json
{
  "message": "generating"
}
```

#### Generate Thank You Email

```http
POST /generate/thank-you-email
```

**Description**: Generate personalized thank you email for after interviews

**Request**: `multipart/form-data`

**Parameters**:

| Parameter  | Type        | Required | Description                    |
| ---------- | ----------- | -------- | ------------------------------ |
| `job`      | File        | No\*     | Job description file to upload |
| `optimize` | JSON String | No       | Optimization parameters        |

**Response**: `202 Accepted`

```json
{
  "message": "generating"
}
```

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

**400 Bad Request** - Invalid or missing parameters:

```json
{
  "message": "No file/invalid file provided"
}
```

**400 Bad Request** - Invalid JSON in optimize parameter:

```json
{
  "message": "invalid optimize parameter"
}
```

**404 Not Found** - File not found:

```json
{
  "message": "The file does not exist!"
}
```

**417 Expectation Failed** - Missing required fields:

```json
{
  "message": "Required property missing or invalid."
}
```

**500 Internal Server Error** - Server error:

```json
{
  "message": "problem with conversion"
}
```

### API Documentation

**Swagger UI**: http://localhost:8080/swagger-ui/index.html

Interactive API documentation with try-it-out functionality.

**OpenAPI Spec**: http://localhost:8080/api-docs

JSON specification for API tools and clients.

---

## 🌍 Environment Variables

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

## ⚙️ Configuration Files

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
        languageVersion = JavaLanguageVersion.of(21)
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

## 📂 Project Structure

```
java-resumes/
 src/
    main/
       java/ca/letkeman/resumes/
          controller/              # REST Controllers
             ResumeController.java
                - REST API endpoints
                - Request/response handling
                - File operations

          service/                 # Business Logic
             FilesStorageService.java (interface)
             FilesStorageServiceImpl.java
                - File storage operations
                - File listing, upload, delete

          model/                   # Data Models
             Optimize.java
               - Request payload for optimization
             FileInfo.java
                - File metadata

          optimizer/               # LLM Integration
             ApiService.java
               - LLM API communication
             HtmlToPdf.java
               - PDF conversion
             ChatBody.java
               - LLM request structure
             Message.java
               - Chat message structure
             responses/
                 LLMResponse.java
                 Choice.java
                 Message.java
                 Usage.java
                 Stats.java

          message/                 # Response Messages
             ResponseMessage.java
                - API response wrapper

          Config.java              # Configuration
            - Reads config.json
            - LLM configuration

          Utility.java             # Utility Methods
            - Helper functions
            - Common operations

          BackgroundResume.java    # Background Tasks
            - Async processing

          RestServiceApplication.java
             - Spring Boot main class
             - Application entry point

       resources/
           application.yml          # Spring configuration
           static/                  # Static resources

    test/                            # Test Files
        java/ca/letkeman/resumes/
            ResumeControllerTest.java
            OptimizeTest.java
            ApiServiceTest.java

 config/                              # Configuration
    checkstyle/
        checkstyle.xml              # Code style rules

 gradle/                              # Gradle Wrapper
    wrapper/

 build/                               # Build Output
    classes/                        # Compiled classes
    libs/                           # JAR files
    reports/                        # Test/coverage reports

 files/                               # File Storage (runtime)

 build.gradle                         # Gradle build script
 settings.gradle                      # Gradle settings
 gradle.properties                    # Gradle properties
 gradlew                              # Gradle wrapper (Unix)
 gradlew.bat                          # Gradle wrapper (Windows)
 config.json                          # LLM configuration
 Dockerfile                           # Docker build config
 .gitignore                           # Git ignore rules
 BACKEND_README.md                    # This file
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

## ✅ Code Quality

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

ApiService.java

HTTP POST (JSON)

LLM Service (Ollama/LM Studio/OpenAI)

Response (JSON)

Parse & Process

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
 StorageException
    StorageFileNotFoundException
    StorageUploadException
 ProcessingException
    LLMServiceException
    DocumentConversionException
 ValidationException
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

## 🔐 Security

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
export JAVA_HOME=/path/to/jdk-21
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

**Made with using Java, Spring Boot, and AI**

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
