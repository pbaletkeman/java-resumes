# Java Resumes - Comprehensive Documentation

## Table of Contents

1. [Overview](#overview)
2. [Problem Statement](#problem-statement)
3. [Solution Architecture](#solution-architecture)
4. [Features](#features)
5. [Technology Stack](#technology-stack)
6. [Project Structure](#project-structure)
7. [Getting Started](#getting-started)
8. [API Endpoints](#api-endpoints)
9. [Core Components](#core-components)
10. [Testing](#testing)
11. [Configuration](#configuration)
12. [Related Documentation](#related-documentation)

---

## Overview

**Java Resumes** is an AI-powered resume and cover letter optimization system that leverages Large Language Models (LLMs) to create tailored job application documents. The application accepts a job description and existing resume, then generates optimized versions in both Markdown and PDF formats using an OpenAI-compatible API service.

The solution helps job seekers:

- Save time on creating bespoke resumes for each job
- Ensure all relevant skills are highlighted
- Identify skill gaps and learning opportunities
- Generate ATS-friendly documents
- Create professional cover letters

---

## Problem Statement

### The Challenge

Every job posting requires a uniquely tailored resume and cover letter, which can take an hour or more to create manually. During this process, applicants face several critical risks:

- **Missed Details**: May overlook important requirements in the job description
- **Unneeded Content**: Accidentally include irrelevant experience that turns off employers
- **Skill Omissions**: Forget to include vital skills that employers are seeking
- **No Guidance**: Lack of insight into certifications or technologies to learn

### The Impact

Manual resume customization is time-consuming and error-prone, potentially costing applicants interviews and job opportunities.

---

## Solution Architecture

Java Resumes provides an intelligent, automated solution that:

1. **Accepts Input**: Job description and current resume (PDF format)
2. **Processes with AI**: Sends content to LLM for optimization
3. **Generates Output**: Creates tailored resume and/or cover letter
4. **Exports Multiple Formats**: Provides both Markdown and PDF versions
5. **Identifies Opportunities**: Suggests certifications and skills to acquire

### Architecture Overview

See [Architecture.md](./Architecture.md) for detailed architecture diagrams including:

- 1000ft process view
- Sequence diagrams
- Detailed process flow
- Class dependency diagrams

---

## Features

### Core Functionality

- **Resume Optimization**: AI-tailored resumes matching job descriptions
- **Cover Letter Generation**: Professional cover letters aligned with job requirements
- **Dual Format Export**: Both Markdown and PDF outputs
- **Skills Gap Analysis**: Recommendations for learning/certifications
- **Markdown to PDF**: Standalone conversion tool for any Markdown file
- **File Management**: Upload, list, and delete documents via REST API
- **ATS Optimization**: Documents formatted for Applicant Tracking System success

### User Interfaces

1. **Swagger UI**: Interactive API documentation

   - Endpoint: `http://localhost:8080/swagger-ui/index.html`
   - Full API exploration and testing

2. **Web UI (Spotlight)**: User-friendly React frontend

   - Endpoint: `http://localhost:8080/spotlight/index.html`
   - File uploads and optimization workflows
   - Built with React, TypeScript, and PrimeReact

3. **REST API**: Programmatic access
   - Comprehensive endpoints for all operations
   - JSON request/response format

---

## Technology Stack

### Backend

| Component               | Technology        | Version  |
| ----------------------- | ----------------- | -------- |
| **Framework**           | Spring Boot       | 3.5+     |
| **Language**            | Java              | 25 (LTS) |
| **Build Tool**          | Gradle            | Latest   |
| **API Documentation**   | SpringDoc OpenAPI | 2.8.7    |
| **JSON Processing**     | Gson              | 2.13.1   |
| **HTML/PDF Processing** | Flying Saucer     | 9.1.22   |
| **Markdown Processing** | CommonMark        | 0.24.0   |
| **Testing**             | JUnit 5           | Latest   |
| **Code Quality**        | Checkstyle        | 10.14.2  |

### Frontend

| Component             | Technology             |
| --------------------- | ---------------------- |
| **UI Framework**      | React                  |
| **Language**          | TypeScript             |
| **Component Library** | PrimeReact             |
| **Build System**      | Standard React tooling |

### External Services

- **LLM Service**: OpenAI-compatible API
  - Supports Ollama (local)
  - Supports LM Studio (local)
  - Supports OpenAI API (cloud)
- **Recommended Models**:
  - gemma-3-4b-it-Q4_K_M
  - Hermes-3-Llama-3.1-8B.Q4_K_M

---

## Project Structure

```
java-resumes/
├── src/
│   ├── main/java/ca/letkeman/resumes/
│   │   ├── RestServiceApplication.java          # Spring Boot entry point
│   │   ├── BackgroundResume.java                # Background processing thread
│   │   ├── Config.java                          # Configuration management
│   │   ├── Utility.java                         # Utility functions
│   │   ├── controller/
│   │   │   └── AdvancedController.java          # REST endpoints
│   │   ├── service/
│   │   │   ├── FilesStorageService.java         # File service interface
│   │   │   └── FilesStorageServiceImpl.java      # File service implementation
│   │   ├── model/
│   │   │   ├── Optimize.java                    # Optimization request model
│   │   │   └── FileInfo.java                    # File metadata
│   │   ├── message/
│   │   │   └── ResponseMessage.java             # API response wrapper
│   │   └── optimizer/
│   │       ├── ApiService.java                  # LLM API integration
│   │       ├── HtmlToPdf.java                   # Markdown/HTML to PDF converter
│   │       ├── ChatBody.java                    # LLM request model
│   │       ├── Message.java                     # Chat message model
│   │       └── responses/
│   │           ├── LLMResponse.java             # LLM response wrapper
│   │           ├── Choice.java                  # Response choice
│   │           ├── Message.java                 # Response message
│   │           ├── Stats.java                   # Generation statistics
│   │           └── Usage.java                   # Token usage info
│   └── test/java/ca/letkeman/resumes/
│       ├── controller/AdvancedControllerTest.java
│       ├── model/OptimizeTest.java
│       └── optimizer/ApiServiceTest.java
├── config/
│   └── checkstyle/checkstyle.xml               # Code quality rules
├── build.gradle                                 # Build configuration
├── README.md                                    # Quick start guide
├── docs/
│   ├── README.md                                # This file
│   ├── Architecture.md                          # Architecture diagrams
│   └── ...
└── config.json                                  # LLM configuration

```

---

## Getting Started

### Prerequisites

- **Java 25 LTS** (or compatible JDK)
- **Gradle** build tool
- **LLM Service** running locally:
  - Ollama: https://ollama.ai
  - LM Studio: https://lmstudio.ai
- **Git** for version control

### Installation

1. **Clone the repository**:

   ```bash
   git clone https://github.com/pbaletkeman/java-resumes.git
   cd java-resumes
   ```

2. **Start LLM Service** (choose one):

   **Option A: Ollama**

   ```bash
   ollama serve
   # In another terminal:
   ollama pull gemma-3-4b-it
   ```

   **Option B: LM Studio**

   - Download LM Studio
   - Load model: gemma-3-4b-it or Hermes-3-Llama-3.1-8B
   - Start local server on default port

3. **Configure LLM endpoint** in `config.json`:

   ```json
   {
     "endpoint": "http://localhost:11434",
     "apikey": "not-needed-for-local",
     "model": "gemma-3-4b-it"
   }
   ```

4. **Build the project**:

   ```bash
   ./gradlew build
   ```

5. **Run the application**:

   ```bash
   ./gradlew bootRun
   ```

6. **Access the UI**:
   - Web UI: http://localhost:8080/spotlight/index.html
   - API Docs: http://localhost:8080/swagger-ui/index.html

---

## API Endpoints

### File Management

| Method     | Endpoint            | Description             |
| ---------- | ------------------- | ----------------------- |
| **GET**    | `/files`            | List all uploaded files |
| **GET**    | `/files/{filename}` | Download specific file  |
| **DELETE** | `/files/{filename}` | Delete specific file    |
| **POST**   | `/delete-all`       | Delete all files        |

### Resume/Cover Letter Generation

| Method   | Endpoint            | Description                                 |
| -------- | ------------------- | ------------------------------------------- |
| **POST** | `/upload`           | Upload files and generate optimized content |
| **POST** | `/markdownFile2PDF` | Convert Markdown to PDF                     |

### Request/Response Models

**POST /upload** Request:

```json
{
  "optimize": {
    "company": "string",
    "jobTitle": "string",
    "model": "gemma-3-4b-it",
    "temperature": 0.15,
    "promptType": ["Resume", "CoverLetter"],
    "resume": "file content",
    "jobDescription": "file content"
  },
  "files": {
    "resume": "file",
    "job": "file"
  }
}
```

**Response**:

```json
{
  "message": "generating"
}
```

---

## Core Components

### Controller Layer (`AdvancedController`)

**Responsibilities**:

- Handles HTTP requests for file operations
- Manages file uploads and downloads
- Coordinates optimization requests
- Returns REST responses

**Key Methods**:

- `file2PDF()`: Converts Markdown/HTML to PDF
- `uploadFiles()`: Processes resume and job description
- `getListFiles()`: Lists available files
- `downloadFile()`: Downloads file to client
- `deleteFile()`: Removes file from storage

### Service Layer (`FilesStorageService`)

**Responsibilities**:

- File persistence operations
- Directory management
- Resource loading

**Implementations**:

- `FilesStorageServiceImpl`: File system storage

### Model Layer

**`Optimize`**: Encapsulates optimization request parameters

- `company`: Target company name
- `jobTitle`: Target job position
- `model`: LLM model identifier
- `temperature`: Response randomness (0.0-1.0)
- `promptType`: Array of output types (Resume, CoverLetter)
- `resume`: Current resume content
- `jobDescription`: Job posting content

**`FileInfo`**: Metadata for uploaded files

- `filename`: File identifier
- `url`: Download URL
- `size`: File size in bytes

### Optimizer Layer (`ApiService`)

**Responsibilities**:

- Communicates with LLM service
- Processes optimization requests
- Generates optimized documents
- Handles PDF conversion

**Key Methods**:

- `produceFiles()`: Main optimization workflow
- `createChatBody()`: Builds LLM request
- `sendHttpRequest()`: Communicates with LLM API
- `processResponse()`: Extracts and formats LLM output

### Supporting Classes

**`BackgroundResume`**: Thread-based async processing

- Runs optimization in background thread
- Prevents blocking of API responses

**`HtmlToPdf`**: PDF conversion utility

- Converts Markdown/HTML to PDF
- Uses Flying Saucer rendering engine

**`Utility`**: Common utility functions

- File operations
- String manipulation
- Configuration reading

---

## Testing

### Test Coverage

The project includes comprehensive unit tests for:

#### AdvancedControllerTest

```java
// File: src/test/java/ca/letkeman/resumes/controller/AdvancedControllerTest.java
- test_successful_markdown_to_pdf_conversion()
- test_unsuccessful_markdown_to_pdf_conversion()
- test_handles_null_file_parameter()
- test_invalidOptimize()
- test_optimizeResume_with_valid_files()
- test_getListFiles_with_files()
```

**Purpose**: Verifies REST endpoint functionality

- Request validation
- Response formatting
- File handling
- Error scenarios

#### OptimizeTest

```java
// File: src/test/java/ca/letkeman/resumes/model/OptimizeTest.java
```

**Purpose**: Tests the Optimize request model

- Parameter validation
- Serialization/deserialization

#### ApiServiceTest

```java
// File: src/test/java/ca/letkeman/resumes/optimizer/ApiServiceTest.java
```

**Purpose**: Tests LLM API integration

- HTTP communication
- Response parsing
- Error handling

### Running Tests

**Run all tests**:

```bash
./gradlew test
```

**Run specific test class**:

```bash
./gradlew test --tests AdvancedControllerTest
```

**Run with coverage**:

```bash
./gradlew test --info
```

### Test Best Practices

1. **Use MockMvc** for controller testing
2. **Mock external services** (LLM API, file storage)
3. **Test error conditions** and edge cases
4. **Verify response formats** and status codes
5. **Use meaningful assertions** for clarity

---

## Configuration

### Application Properties

Create `application.properties` or `application.yml`:

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/

# File Upload Configuration
upload.path=./uploads
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging
logging.level.ca.letkeman=DEBUG
logging.level.org.springframework.web=INFO
```

### LLM Configuration (`config.json`)

```json
{
  "endpoint": "http://localhost:11434",
  "apikey": "your-api-key-if-using-openai",
  "model": "gemma-3-4b-it"
}
```

**Supported Models**:

- `gemma-3-4b-it` (4GB, good for CPU)
- `Hermes-3-Llama-3.1-8B` (8GB, better quality)
- Any OpenAI-compatible LLM

### Code Quality Configuration

Checkstyle rules are defined in `config/checkstyle/checkstyle.xml`.

**Run quality checks**:

```bash
./gradlew checkstyleMain checkstyleTest
```

---

## Related Documentation

| Document                                                                 | Purpose                                             |
| ------------------------------------------------------------------------ | --------------------------------------------------- |
| [Architecture.md](./Architecture.md)                                     | System design, diagrams, and component interactions |
| [../README.md](../README.md)                                             | Quick start and overview                            |
| [../MIGRATION_SUMMARY.md](../MIGRATION_SUMMARY.md)                       | Migration history and changes                       |
| [../CHECKSTYLE_COMPLIANCE_REPORT.md](../CHECKSTYLE_COMPLIANCE_REPORT.md) | Code quality metrics                                |

---

## Troubleshooting

### Application Won't Start

**Issue**: Port 8080 already in use

```bash
# Change port in application.properties
server.port=8081
```

### LLM API Connection Failed

**Issue**: Cannot connect to LLM service

- Verify LLM service is running
- Check `config.json` endpoint URL
- Test connection: `curl http://localhost:11434/api/tags`

### Files Not Saving

**Issue**: File upload directory not found

- Ensure `upload.path` directory exists
- Check directory permissions
- Verify `upload.path` in `config.json`

### PDF Conversion Fails

**Issue**: Markdown to PDF conversion errors

- Verify Flying Saucer dependencies installed
- Check Markdown syntax
- Ensure temporary directory exists

---

## Contributing

1. Follow checkstyle rules defined in `config/checkstyle/checkstyle.xml`
2. Write unit tests for new features
3. Run `./gradlew build` before submitting changes
4. Update documentation as needed

---

## License

See repository for license information.

---

## Support

For issues and questions:

- Check existing issues on GitHub
- Review [Architecture.md](./Architecture.md) for design details
- Consult unit tests for usage examples
