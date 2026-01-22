# Architecture Diagrams

This directory contains architecture diagrams and visual representations of the java-resumes system.

## 📊 Diagrams Included

### System Architecture

```plaintext
┌─────────────────────────────────────────────────────────────┐
│                    Java Resumes System                      │
└─────────────────────────────────────────────────────────────┘

┌──────────────────────┐        ┌──────────────────────┐
│  Frontend Layer      │        │  Backend Layer       │
│  ────────────────    │        │  ──────────────      │
│                      │        │                      │
│  React 19.2.0        │◄──────►│  Spring Boot 3.5.1   │
│  TypeScript 5.9.3    │        │  Java 17 LTS         │
│  PrimeReact 10.9.7   │        │  Gradle 8.7          │
│  Vite 7.2.4          │        │                      │
│  Port: 5173          │        │  Port: 8080          │
│  (Development)       │        │  ────────────────    │
│  3173 (Production)   │        │                      │
│                      │        │  Controllers         │
│  ────────────────    │        │  Services            │
│  Main Components:    │        │  Models              │
│  - MainContentTab    │        │  ────────────────    │
│  - AdditionalTools   │        │  API Endpoints:      │
│  - FileHistory       │        │  - POST /upload      │
│  - Settings/Theme    │        │  - GET /files        │
│  - ApiClient (Hooks) │        │  - DELETE /files     │
└──────────────────────┘        └──────────────────────┘
          │                               │
          │                               │
          └───────────────────┬───────────┘
                              │
                    REST API (JSON)
                    HTTP/HTTPS
                              │
          ┌───────────────────┴───────────────────┐
          │                                       │
    ┌─────▼──────┐                         ┌──────▼──────┐
    │  LLM Service│                         │ File Storage │
    │  ─────────  │                         │ ────────────│
    │             │                         │             │
    │ Ollama      │                         │ Local FS    │
    │ LM Studio   │                         │ Upload Dir  │
    │ OpenAI API  │                         │ /uploads    │
    │             │                         │             │
    │ Port: 11434 │                         │ Management: │
    │ (local)     │                         │ - Save      │
    │             │                         │ - Delete    │
    │ Returns:    │                         │ - List      │
    │ - Resume    │                         │             │
    │ - Cover     │                         └─────────────┘
    │ - Analysis  │
    └─────────────┘
```

### Component Architecture

**Frontend Components**:

```shell
App (Root)
├── AppHeader
│   └── ThemeToggle
├── MainContentArea
│   ├── MainContentTab
│   │   ├── JobDescriptionInput
│   │   ├── ResumeInput
│   │   ├── PromptTypeSelector
│   │   ├── TemperatureSlider
│   │   ├── ModelSelector
│   │   ├── ProcessButton
│   │   └── OutputPreview
│   └── AdditionalToolsTab
│       ├── MarkdownInput
│       ├── ConvertButton
│       └── PdfPreview
└── FileHistory
    ├── FileList
    │   └── FileItem
    │       ├── DownloadBtn
    │       └── DeleteBtn
    └── ClearAllBtn
```

**Backend Components**:

```shell
RestServiceApplication (Spring Boot)
├── ResumeController (REST Endpoints)
│   ├── /upload (POST)
│   ├── /files (GET)
│   ├── /files/{id} (GET, DELETE)
│   └── /markdownFile2PDF (POST)
├── ResumeService (Business Logic)
│   ├── Optimization Logic
│   ├── File Processing
│   └── Response Formatting
├── ApiService (LLM Integration)
│   ├── HTTP Client
│   ├── Request Building
│   ├── Response Parsing
│   └── Error Handling
└── FilesStorageService (File Operations)
    ├── Save Files
    ├── Load Files
    ├── Delete Files
    └── List Files
```

### Data Flow Diagram

```plaintext
User Interaction:
1. User enters job description (text or upload)
2. User enters resume (text or upload)
3. User selects optimization type (Resume/CoverLetter)
4. User adjusts parameters (temperature, model)
5. User clicks "Process" button
   │
   ▼
Frontend Validation & Submission:
1. Validate inputs (non-empty, valid format)
2. Build FormData with files
3. Send POST /upload request to backend
   │
   ▼
Backend Processing:
1. Receive request at ResumeController
2. Validate files and parameters
3. Save uploaded files to storage
4. Create optimization request
5. Pass to BackgroundResume thread
   │
   ▼
Async Background Processing:
1. Read optimization parameters
2. Call ApiService.produceFiles()
3. Build LLM request:
   - Select model from config
   - Build system prompt
   - Add job description context
   - Add resume content
   - Set temperature parameter
   │
   ▼
LLM Service Communication:
1. Send HTTP request to LLM endpoint
2. Format: OpenAI-compatible API
3. Wait for LLM response
4. Parse response JSON
5. Extract generated content
   │
   ▼
Output Generation:
1. Format response data
2. Generate PDF from content
3. Save generated files to storage
4. Update status/metadata
   │
   ▼
Response & Storage:
1. Store generated resume/cover letter
2. Return file location
3. Frontend polls for completion
4. Display results to user
   │
   ▼
User Download:
1. View generated documents
2. Download as PDF
3. Download as Markdown
4. Manage files (delete, etc.)
```

### Deployment Architecture

```plaintext
┌──────────────────────────────────────────────────────────┐
│              Docker Compose Environment                  │
└──────────────────────────────────────────────────────────┘

┌─────────────────────┐         ┌─────────────────────┐
│  Backend Container  │         │ Frontend Container  │
│  ─────────────────  │         │ ─────────────────   │
│                     │         │                     │
│ Base Image:         │         │ Base Image:         │
│ gradle:8.7-jdk17    │         │ node:18-alpine      │
│ (build stage)       │         │                     │
│                     │         │ Build: npm run build│
│ Runtime:            │         │                     │
│ eclipse-temurin:    │         │ Server: nginx       │
│ 17-jre-alpine       │         │                     │
│                     │         │ Exposes:            │
│ Port: 8080          │◄────────│ Port: 80 (nginx)    │
│ Java App:           │ REST API│ Maps to: 5173 (dev) │
│ app.jar             │         │ Maps to: 3173 (prod)│
│                     │         │                     │
│ Environment:        │         │ Environment:        │
│ JAVA_OPTS           │         │ REACT_APP_API_URL   │
│ Spring Profile      │         │                     │
│                     │         │                     │
│ Volumes:            │         │ Volumes:            │
│ /app/uploads/       │         │ (build output)      │
│ (file storage)      │         │                     │
└─────────────────────┘         └─────────────────────┘
         │                              │
         ▼                              ▼
    ┌────────────────────────────────────┐
    │    Docker Network: java-resumes    │
    │    (containers communicate)        │
    └────────────────────────────────────┘
         │
         ▼
    ┌─────────────────┐
    │  Host Machine   │
    │  ─────────────  │
    │                 │
    │  Ports:         │
    │  8080→8080      │
    │  (backend)      │
    │                 │
    │  80→80          │
    │  (frontend)     │
    │                 │
    │  Volumes:       │
    │  ./uploads/     │
    │  (file sync)    │
    └─────────────────┘
```

### Docker Compose Structure

```yaml
services:
  backend:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    volumes:
      - ./uploads:/app/uploads
    environment:
      - SPRING_PROFILE_ACTIVE=docker
    depends_on:
      - (LLM service - external)

  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    ports:
      - "80:80"
    depends_on:
      - backend
    environment:
      - REACT_APP_API_URL=http://backend:8080

volumes:
  uploads:
    driver: local

networks:
  default:
    name: java-resumes
```

---

## 📈 Request/Response Flow

```plaintext
┌─────────────────────────────────────────────────────────┐
│           Resume Optimization Request Flow              │
└─────────────────────────────────────────────────────────┘

CLIENT REQUEST (Frontend):
{
  "jobDescription": "We are seeking...",
  "resume": "John Doe, Senior Developer...",
  "promptType": ["Resume", "CoverLetter"],
  "temperature": 0.7,
  "model": "mistral"
}

   │
   ▼ HTTP POST /upload

BACKEND PROCESSING:
1. Validate and extract files
2. Save to /uploads/
3. Queue background job
4. Return immediate response

IMMEDIATE RESPONSE:
{
  "message": "Processing started",
  "jobId": "abc123def456"
}

   │
   ▼ Poll GET /files

BACKGROUND PROCESSING:
1. Read job parameters
2. Build LLM prompt
3. Send to LLM service
4. Parse response
5. Generate PDF
6. Save results

   │
   ▼ When complete

LLM RESPONSE (from Ollama/OpenAI):
{
  "choices": [{
    "message": {
      "content": "Optimized resume content..."
    }
  }],
  "usage": {
    "prompt_tokens": 245,
    "completion_tokens": 156
  }
}

   │
   ▼ Generate outputs

GENERATED FILES:
├── resume_optimized.md
├── resume_optimized.pdf
├── coverletter_generated.md
└── coverletter_generated.pdf

   │
   ▼ File response

FILE LIST RESPONSE:
{
  "files": [
    {
      "filename": "resume_optimized.pdf",
      "url": "/files/resume_optimized.pdf",
      "size": 245120,
      "type": "pdf"
    },
    ...
  ]
}
```

---

## 🔄 Authentication & Security

Current Implementation:

- No authentication required (local use)
- File storage on local filesystem
- Direct API access

Production Considerations:

- Add JWT authentication
- Implement rate limiting
- Add HTTPS/TLS
- File size limits
- Input validation
- CORS configuration
- API key management

---

## 🚀 Scalability Patterns

### Horizontal Scaling

```plaintext
Load Balancer
     │
     ├─► Backend Instance 1
     ├─► Backend Instance 2
     └─► Backend Instance 3

Shared:
├─► File Storage (NFS/S3)
├─► Database (if added)
└─► Cache (if added)
```

### Message Queue Pattern

```plaintext
Frontend ──► REST API ──► Message Queue
                          │
                          ├─► Worker 1
                          ├─► Worker 2
                          └─► Worker 3

Each worker processes optimization jobs
independently and updates shared storage.
```

---

## 📚 References

- [Full Architecture Documentation](../Architecture.md)
- [Backend Implementation](../BACKEND_README.md)
- [Frontend Setup](../../frontend/README.md)
- [Docker Configuration](../../Dockerfile)
- [Docker Compose Setup](../../docker-compose.yml)

---
