# Architecture Diagrams

This directory contains architecture diagrams and visual representations of the java-resumes system.

## � Table of Contents

1. [System Architecture](#system-architecture)
2. [Component Architecture](#component-architecture)
3. [Data Flow Diagram](#data-flow-diagram)
4. [Deployment Architecture](#deployment-architecture)
5. [Docker Compose Structure](#docker-compose-structure)
6. [Request/Response Flow](#request-response-flow)
7. [Backend Component Dependencies (UML)](#backend-component-dependencies-uml)
8. [Frontend Component Dependencies (UML)](#frontend-component-dependencies-uml)
9. [Authentication & Security](#authentication--security)
10. [Scalability Patterns](#scalability-patterns)

---

## �📊 Diagrams Included

### System Architecture

```mermaid
graph TB
    subgraph Frontend["Frontend Layer"]
        React["React 19.2.0<br/>TypeScript 5.9.3<br/>PrimeReact 10.9.7<br/>Vite 7.2.4<br/>Port: 5173/3173"]
        Components["Components<br/>- MainContentTab<br/>- AdditionalTools<br/>- FileHistory<br/>- Settings"]
    end

    subgraph Backend["Backend Layer"]
        SpringBoot["Spring Boot 3.5.1<br/>Java 17 LTS<br/>Gradle 8.7<br/>Port: 8080"]
        Controllers["Controllers<br/>Services<br/>Models<br/>API Endpoints"]
    end

    subgraph External["External Services"]
        LLM["LLM Service<br/>Ollama/LM Studio<br/>OpenAI API<br/>Port: 11434"]
        FileStorage["File Storage<br/>Local FS<br/>Upload Dir<br/>Save/Delete/List"]
    end

    Frontend <-->|REST API JSON<br/>HTTP/HTTPS| Backend
    Backend <-->|LLM Requests| LLM
    Backend <-->|File Operations| FileStorage
```

### Component Architecture

**Frontend Components**:

```mermaid
graph TD
    App["App Root"]
    Header["AppHeader"]
    Theme["ThemeToggle"]
    Content["MainContentArea"]

    Tab1["MainContentTab"]
    JobDesc["JobDescriptionInput"]
    Resume["ResumeInput"]
    PromptType["PromptTypeSelector"]
    Temp["TemperatureSlider"]
    Model["ModelSelector"]
    Process["ProcessButton"]
    Output["OutputPreview"]

    Tab2["AdditionalToolsTab"]
    MarkdownIn["MarkdownInput"]
    Convert["ConvertButton"]
    PdfPreview["PdfPreview"]

    History["FileHistory"]
    List["FileList"]
    Item["FileItem"]
    Download["DownloadBtn"]
    Delete["DeleteBtn"]
    ClearAll["ClearAllBtn"]

    App --> Header
    App --> Content
    App --> History
    Header --> Theme
    Content --> Tab1
    Content --> Tab2
    Tab1 --> JobDesc
    Tab1 --> Resume
    Tab1 --> PromptType
    Tab1 --> Temp
    Tab1 --> Model
    Tab1 --> Process
    Tab1 --> Output
    Tab2 --> MarkdownIn
    Tab2 --> Convert
    Tab2 --> PdfPreview
    History --> List
    List --> Item
    Item --> Download
    Item --> Delete
    History --> ClearAll
```

**Backend Components**:

```mermaid
graph TD
    App["RestServiceApplication<br/>Spring Boot"]

    Controller["ResumeController<br/>REST Endpoints"]
    Upload["/upload POST"]
    Files["/files GET"]
    FilesId["/files/{id}<br/>GET/DELETE"]
    Markdown["/markdownFile2PDF POST"]
    Skills["/process/skills POST"]
    Interview["Interview Endpoints:<br/>/generate/interview-hr-questions<br/>/generate/interview-job-specific<br/>/generate/interview-reverse"]
    Networking["Networking Endpoints:<br/>/generate/cold-email<br/>/generate/cold-linkedin-message<br/>/generate/thank-you-email"]

    Service["ResumeService<br/>Business Logic"]
    OptLogic["Optimization Logic"]
    FileProc["File Processing"]
    Response["Response Formatting"]

    Api["ApiService<br/>LLM Integration"]
    HttpClient["HTTP Client"]
    BuildReq["Request Building"]
    ParseResp["Response Parsing"]
    ErrorHandle["Error Handling"]

    Storage["FilesStorageService<br/>File Operations"]
    Save["Save Files"]
    Load["Load Files"]
    Delete["Delete Files"]
    List["List Files"]

    App --> Controller
    App --> Service
    App --> Api
    App --> Storage

    Controller --> Upload
    Controller --> Files
    Controller --> FilesId
    Controller --> Markdown
    Controller --> Skills
    Controller --> Interview
    Controller --> Networking

    Service --> OptLogic
    Service --> FileProc
    Service --> Response

    Api --> HttpClient
    Api --> BuildReq
    Api --> ParseResp
    Api --> ErrorHandle

    Storage --> Save
    Storage --> Load
    Storage --> Delete
    Storage --> List
```

### Data Flow Diagram

```mermaid
sequenceDiagram
    participant User
    participant Frontend
    participant Backend
    participant BackgroundThread
    participant ApiService
    participant LLMService
    participant FileStorage

    User->>Frontend: 1. Enter job description & resume
    User->>Frontend: 2. Select optimization type
    User->>Frontend: 3. Adjust parameters
    User->>Frontend: 4. Click Process

    Frontend->>Frontend: Validate inputs
    Frontend->>Frontend: Build FormData
    Frontend->>Backend: POST /upload

    Backend->>Backend: Validate files & parameters
    Backend->>FileStorage: Save uploaded files
    Backend->>BackgroundThread: Spawn async thread
    Backend->>Frontend: Return 200/202

    BackgroundThread->>ApiService: Call produceFiles()
    ApiService->>ApiService: Build LLM request
    ApiService->>LLMService: Send OpenAI-compatible request
    LLMService->>ApiService: Return optimized content

    ApiService->>ApiService: Generate PDF
    ApiService->>FileStorage: Save generated files

    Frontend->>Backend: Poll GET /files
    Backend->>Frontend: Return file list with results

    Frontend->>User: Display optimized documents
    User->>Frontend: Download PDF/Markdown
    User->>Frontend: Manage files (delete, etc.)
```

### Deployment Architecture

```mermaid
graph TB
    Host["Host Machine"]

    subgraph DockerNetwork["Docker Network: java-resumes"]
        subgraph Backend["Backend Container"]
            BuildStage["Build Stage<br/>gradle:8.7-jdk17"]
            BackendRuntime["Runtime<br/>eclipse-temurin:17-jre-alpine<br/>Port: 8080<br/>Java App: app.jar"]
            BackendEnv["Environment<br/>JAVA_OPTS<br/>Spring Profile"]
            BackendVolume["Volumes<br/>/app/uploads/"]
        end

        subgraph Frontend["Frontend Container"]
            FrontendBuild["Build<br/>node:18-alpine<br/>npm run build"]
            FrontendServer["Server<br/>nginx<br/>Port: 80"]
            FrontendPorts["Port Mapping<br/>80→5173 dev<br/>80→3173 prod"]
            FrontendEnv["Environment<br/>REACT_APP_API_URL"]
        end
    end

    BuildStage --> BackendRuntime
    BackendRuntime --> BackendEnv
    BackendRuntime --> BackendVolume

    FrontendBuild --> FrontendServer
    FrontendServer --> FrontendPorts
    FrontendServer --> FrontendEnv

    BackendRuntime <-->|REST API| FrontendServer

    Host --> DockerNetwork
    Host --> BackendVolume

    Host -.->|Port 8080| BackendRuntime
    Host -.->|Port 80| FrontendServer
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

```mermaid
sequenceDiagram
    participant Frontend as Frontend<br/>Client
    participant HTTP as HTTP POST<br/>/upload
    participant Backend as Backend<br/>Processing
    participant BG as Background<br/>Thread
    participant LLM as LLM Service<br/>Ollama/OpenAI
    participant Storage as File<br/>Storage
    participant Poll as HTTP GET<br/>/files

    Frontend->>Frontend: Build Request:<br/>{jobDescription, resume,<br/>promptType, temperature,<br/>model}

    Frontend->>HTTP: Send FormData
    HTTP->>Backend: Receive Request

    Backend->>Backend: 1. Validate files<br/>2. Extract parameters
    Backend->>Storage: 3. Save uploaded files
    Backend->>BG: 4. Spawn async thread
    Backend->>Frontend: Return 200/202:<br/>{message: "Processing started",<br/>jobId: "abc123"}

    par Background Processing
        BG->>BG: 1. Read parameters
        BG->>BG: 2. Build LLM prompt
        BG->>LLM: 3. Send request
        LLM->>BG: Return: {choices,<br/>usage: {prompt_tokens,<br/>completion_tokens}}
        BG->>BG: 4. Parse response
        BG->>Storage: 5. Generate & save PDF
    end

    Frontend->>Poll: Poll GET /files
    Storage->>Frontend: Return file list:<br/>{files: [{filename,<br/>url, size, type}]}

    Frontend->>Frontend: Display:<br/>- resume_optimized.pdf<br/>- coverletter_generated.pdf
    Frontend->>User: Ready for download
```

---

## Backend Component Dependencies (UML)

```mermaid
classDiagram
    class RestServiceApplication {
        +main(String[] args) void
        +init() void
    }

    class ResumeController {
        -FilesStorageService filesStorageService
        -ApiService apiService
        +optimizeResume(MultipartFile, String) ResponseEntity
        +file2PDF(String) ResponseEntity
        +getListFiles() ResponseEntity
        +getFile(String) ResponseEntity
        +deleteFile(String) ResponseEntity
        +healthCheck() ResponseEntity
        +processSkills(String) ResponseEntity
        +generateInterviewHrQuestions(String) ResponseEntity
        +generateInterviewJobSpecific(String) ResponseEntity
        +generateInterviewReverse(String) ResponseEntity
        +generateColdEmail(String) ResponseEntity
        +generateColdLinkedInMessage(String) ResponseEntity
        +generateThankYouEmail(String) ResponseEntity
        -validate(Optimize) boolean
        -spawn(BackgroundResume) void
        -processPromptRequest(String, Optimize, String) ResponseEntity
    }

    class BackgroundResume {
        -Optimize optimize
        -String root
        +run() void
        -processOptimization() void
        -logResults() void
    }

    class ApiService {
        -String endpoint
        -String apiKey
        -String model
        +produceFiles(String, String, Optimize) String
        -createChatBody(String, String) ChatBody
        -sendHttpRequest(ChatBody) LLMResponse
        -parseResponse(LLMResponse) String
        -handleError(Exception) void
    }

    class FilesStorageService {
        -Path rootLocation
        +save(MultipartFile) void
        +load(String) Resource
        +delete(String) void
        +loadAll() Stream<Path>
        +init() void
    }

    class HtmlToPdf {
        +convertMarkdownToPdf(String) byte[]
        +convertHtmlToPdf(String) byte[]
        -renderDocument(Document) byte[]
    }

    class Config {
        -String endpoint
        -String apikey
        -String model
        +loadFromJson(String) Config
    }

    class Optimize {
        -String resume
        -String jobDescription
        -String coverLetter
        -String outputType
        -double temperature
        -String model
        +getResume() String
        +getJobDescription() String
    }

    class FileInfo {
        -String filename
        -String url
        -long size
        -String type
        +getFilename() String
        +getUrl() String
    }

    class ResponseMessage {
        -String message
        +getMessage() String
        +setMessage(String) void
    }

    class Utility {
        +readFileAsString(String) String
        +validateFile(MultipartFile) boolean
        +sanitizeFilename(String) String
    }

    RestServiceApplication --> ResumeController
    RestServiceApplication --> FilesStorageService
    RestServiceApplication --> ApiService

    ResumeController --> BackgroundResume
    ResumeController --> FilesStorageService
    ResumeController --> ApiService
    ResumeController --> Optimize
    ResumeController --> FileInfo
    ResumeController --> ResponseMessage

    BackgroundResume --> ApiService
    BackgroundResume --> FilesStorageService
    BackgroundResume --> Optimize
    BackgroundResume --> HtmlToPdf

    ApiService --> Config
    ApiService --> HtmlToPdf

    FilesStorageService --> FileInfo

    HtmlToPdf --> Utility
```

---

## Frontend Component Dependencies (UML)

```mermaid
classDiagram
    class App {
        -useTheme()
        -useApi()
        +render() JSX
    }

    class Navbar {
        -theme: string
        -toggleTheme() void
        +render() JSX
    }

    class MainLayout {
        -activeTab: string
        -loading: boolean
        +setActiveTab(string) void
        +render() JSX
    }

    class MainContentTab {
        -resume: string
        -jobDescription: string
        -outputType: string
        -temperature: number
        -useApi()
        +handleSubmit() void
        +render() JSX
    }

    class DocumentUploadForm {
        -formData: FormData
        -loading: boolean
        -error: string
        +handleUpload() void
        +handleChange() void
        +render() JSX
    }

    class FileHistory {
        -files: FileInfo[]
        -loading: boolean
        -useApi(endpoint)
        +loadFiles() void
        +handleDelete(id) void
        +handleDownload(id) void
        +render() JSX
    }

    class AdditionalToolsTab {
        -markdown: string
        -pdfPreview: Blob
        -useApi()
        +handleConvert() void
        +render() JSX
    }

    class MarkdownToPdfForm {
        -markdown: string
        -converted: boolean
        +handleConvert() void
        +render() JSX
    }

    class ThemeToggle {
        -theme: string
        +toggleTheme() void
        +render() JSX
    }

    class useApi {
        -data: any
        -loading: boolean
        -error: string
        +execute(payload) Promise
        +reset() void
    }

    class useTheme {
        -theme: string
        +toggleTheme() void
        +setTheme(string) void
    }

    class ApiService {
        -baseURL: string
        +uploadResume(formData) Promise
        +convertToPdf(markdown) Promise
        +getFiles() Promise
        +deleteFile(id) Promise
        +downloadFile(filename) void
    }

    class AppContext {
        -files: FileInfo[]
        -loading: boolean
        +setFiles(FileInfo[]) void
        +setLoading(boolean) void
    }

    class ThemeContext {
        -theme: string
        -colors: object
        +toggleTheme() void
        +getColors() object
    }

    App --> Navbar
    App --> MainLayout
    App --> ThemeToggle
    App --> AppContext
    App --> ThemeContext

    Navbar --> ThemeToggle
    Navbar --> useTheme

    MainLayout --> MainContentTab
    MainLayout --> AdditionalToolsTab
    MainLayout --> FileHistory

    MainContentTab --> DocumentUploadForm
    MainContentTab --> useApi
    MainContentTab --> ApiService

    DocumentUploadForm --> useApi

    FileHistory --> useApi
    FileHistory --> ApiService
    FileHistory --> AppContext

    AdditionalToolsTab --> MarkdownToPdfForm
    AdditionalToolsTab --> useApi
    AdditionalToolsTab --> ApiService

    MarkdownToPdfForm --> useApi

    useApi --> ApiService
    useTheme --> ThemeContext
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

```mermaid
graph LR
    LB["Load Balancer"]

    subgraph Backend["Backend Instances"]
        B1["Backend 1<br/>Port 8080"]
        B2["Backend 2<br/>Port 8080"]
        B3["Backend 3<br/>Port 8080"]
    end

    subgraph Shared["Shared Resources"]
        FS["File Storage<br/>NFS/S3"]
        DB["Database<br/>Optional"]
        Cache["Cache<br/>Redis/Optional"]
    end

    LB --> B1
    LB --> B2
    LB --> B3

    B1 --> FS
    B2 --> FS
    B3 --> FS

    B1 --> DB
    B2 --> DB
    B3 --> DB

    B1 --> Cache
    B2 --> Cache
    B3 --> Cache
```

### Message Queue Pattern

```mermaid
graph LR
    Frontend["Frontend"]
    API["REST API"]
    Queue["Message Queue<br/>Job Queue"]

    subgraph Workers["Worker Pool"]
        W1["Worker 1<br/>Process Jobs"]
        W2["Worker 2<br/>Process Jobs"]
        W3["Worker 3<br/>Process Jobs"]
    end

    Storage["Shared Storage<br/>Results"]

    Frontend -->|Submit| API
    API -->|Enqueue| Queue
    Queue -->|Dequeue| W1
    Queue -->|Dequeue| W2
    Queue -->|Dequeue| W3

    W1 -->|Update| Storage
    W2 -->|Update| Storage
    W3 -->|Update| Storage

    Storage -->|Poll| Frontend
```

---

## 📚 References

- [Full Architecture Documentation](../Architecture.md)
- [Backend Implementation](../BACKEND_README.md)
- [Frontend Setup](../../frontend/README.md)
- [Docker Configuration](../../Dockerfile)
- [Docker Compose Setup](../../docker-compose.yml)

---
  
---  
  
**Last Updated:** February 2, 2026  
**Maintained By:** java-resumes development team 
