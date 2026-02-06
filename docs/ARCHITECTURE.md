# System Architecture

The java-resumes application is built with a modern full-stack architecture that separates concerns across frontend, backend, and external services.

- [System Architecture](#system-architecture)
  - [High-Level Architecture](#high-level-architecture)
  - [Request Flow Architecture](#request-flow-architecture)
  - [Container Network Architecture](#container-network-architecture)
  - [Component Architecture](#component-architecture)
    - [Frontend Layer](#frontend-layer)
    - [Backend Layer](#backend-layer)
    - [LLM Service Layer](#llm-service-layer)
  - [Data Flow Diagrams](#data-flow-diagrams)
    - [Resume Optimization Flow](#resume-optimization-flow)
    - [File Management Flow](#file-management-flow)
  - [External Configuration Integration](#external-configuration-integration)
    - [Configuration Path Resolution](#configuration-path-resolution)
    - [External Prompts Directory](#external-prompts-directory)
  - [Security Considerations](#security-considerations)
    - [Input Validation](#input-validation)
    - [File Handling](#file-handling)
    - [API Security](#api-security)
  - [Deployment Architecture](#deployment-architecture)
    - [Docker Compose Deployment](#docker-compose-deployment)
    - [Kubernetes Deployment (Future)](#kubernetes-deployment-future)
  - [Extensibility Points](#extensibility-points)
    - [Adding New LLM Providers](#adding-new-llm-providers)
    - [Adding New Document Types](#adding-new-document-types)
    - [Custom Prompt Templates](#custom-prompt-templates)
  - [Performance Considerations](#performance-considerations)
    - [Async Processing](#async-processing)
    - [Caching](#caching)
    - [Resource Optimization](#resource-optimization)
  - [Testing Architecture](#testing-architecture)
    - [Backend Testing Strategy](#backend-testing-strategy)
    - [Frontend Testing Strategy](#frontend-testing-strategy)
  - [Key Design Patterns](#key-design-patterns)
    - [MVC Pattern](#mvc-pattern)
    - [Service Layer Pattern](#service-layer-pattern)
    - [Repository Pattern (Future)](#repository-pattern-future)
    - [Strategy Pattern (LLM Integration)](#strategy-pattern-llm-integration)

---

## 🏗️ High-Level Architecture

```mermaid
graph TD
    subgraph Browser["User Browser"]
        User["Chrome, Firefox, Safari"]
    end

    subgraph Frontend["Frontend - Port 80/3000"]
        React["React 19 + TypeScript<br/>PrimeReact + Tailwind CSS"]
        Components["Main Content Tab<br/>Additional Tools Tab<br/>File History Panel<br/>Theme Toggle"]
        Nginx["Nginx Reverse Proxy"]
        React --> Components
        Components --> Nginx
    end

    subgraph Backend["Backend - Port 8080"]
        SpringBoot["Spring Boot 3.5.1 + Java 21 + Gradle"]
        Controller["Controller Layer<br/>ResumeController<br/>/upload, /files/*, /markdownFile2PDF<br/>/process/skills<br/>/generate/interview-*<br/>/generate/cold-*, /generate/thank-you-*"]
        Service["Service Layer<br/>FilesStorageService<br/>ApiService - LLM Integration"]
        Utilities["Utilities<br/>HtmlToPdf - Document Conversion<br/>File Management"]
        SpringBoot --> Controller
        Controller --> Service
        Service --> Utilities
    end

    subgraph LLM["LLM Service - Port 11434/1234"]
        LLMProvider["Ollama / LM Studio / OpenAI"]
        Models["Models: gemma-3-4b-it, llama3, gpt-4"]
        Capabilities["Resume Optimization<br/>Cover Letter Generation<br/>Skills & Certifications<br/>Interview Preparation<br/>Professional Networking"]
        LLMProvider --> Models
        LLMProvider --> Capabilities
    end

    subgraph Storage["File Storage"]
        Files["Uploaded Documents<br/>Generated Documents<br/>Converted PDFs"]
    end

    User -->|HTTP/HTTPS| Nginx
    Nginx -->|REST API JSON| Controller
    Service -->|HTTP REST API| LLMProvider
    Utilities -->|Read/Write| Files

    style Browser fill:#e1f5ff
    style Frontend fill:#e3f2fd
    style Backend fill:#e8f5e9
    style LLM fill:#fff3e0
    style Storage fill:#fce4ec
```

## 🔄 Request Flow Architecture

```mermaid
graph TB
    subgraph "Client Layer"
        Browser[Web Browser]
    end

    subgraph "Frontend Container - Port 80/3000"
        React[React 19 + TypeScript]
        PrimeReact[PrimeReact Components]
        Axios[Axios HTTP Client]
        Nginx[Nginx Web Server]

        React --> PrimeReact
        React --> Axios
        Nginx --> React
    end

    subgraph "Backend Container - Port 8080"
        SpringBoot[Spring Boot 3.5.1 + Java 21]
        Controller[REST Controllers]
        Service[Service Layer]
        Storage[File Storage Service]
        ApiService[LLM API Service]
        Utils[Utilities]

        SpringBoot --> Controller
        Controller --> Service
        Service --> Storage
        Service --> ApiService
        Service --> Utils
    end

    subgraph "LLM Service - Port 11434"
        LLM[Ollama/LM Studio/OpenAI]
        Model[AI Models]

        LLM --> Model
    end

    subgraph "Storage"
        Files[(File System Volume)]
    end

    Browser -->|HTTP/HTTPS| Nginx
    Axios -->|REST API JSON| Controller
    ApiService -->|HTTP POST| LLM
    Storage -->|Read/Write| Files

    style Browser fill:#e1f5ff
    style React fill:#61dafb
    style SpringBoot fill:#6db33f
    style LLM fill:#ff6b6b
    style Files fill:#ffd93d
```

## 🌐 Container Network Architecture

```mermaid
graph LR
    subgraph "Docker Network: resume-app-network"
        Frontend[Frontend Container<br/>nginx:alpine<br/>Port 80]
        Backend[Backend Container<br/>eclipse-temurin:21-jre<br/>Port 8080]
        Volume[(Volume:<br/>backend-files)]

        Frontend -->|HTTP API Calls| Backend
        Backend -->|Store/Retrieve| Volume
    end

    User[User Browser] -->|Port 80| Frontend
    Backend -->|External API| LLM[LLM Service<br/>localhost:11434]

    style Frontend fill:#61dafb
    style Backend fill:#6db33f
    style Volume fill:#ffd93d
    style LLM fill:#ff6b6b
```

## Component Architecture

### Frontend Layer

**Purpose:** User interface and client-side logic

**Technology Stack:**

- React 19 - Component framework
- TypeScript 5.9.3 - Type-safe development
- PrimeReact 10.9.7 - UI components
- Tailwind CSS 4.1.18 - Styling
- Axios - HTTP client

**Key Components:**

- `MainContentTab` - Primary resume optimization interface
- `FileHistoryPanel` - File management and download
- `ToolsTab` - Markdown to PDF conversion
- `ThemeToggle` - Light/dark theme switcher

**Responsibilities:**

- Collect user input (job descriptions, resumes)
- Display processing status
- Show generated documents
- Manage file downloads
- Theme management

### Backend Layer

**Purpose:** Business logic and API endpoints

**Technology Stack:**

- Spring Boot 3.5.1 - Application framework
- Java 21 - Programming language
- Gradle 8.7 - Build tool
- Gson - JSON serialization

**Key Components:**

- `ResumeController` - REST API endpoints
- `ApiService` - LLM integration
- `FilesStorageService` - File operations
- `BackgroundResume` - Async processing thread

**Responsibilities:**

- Validate API requests
- Process resume optimization
- Manage file uploads/downloads
- Coordinate with LLM service
- Handle errors and logging

### LLM Service Layer

**Purpose:** AI-powered document optimization

**Supported Services:**

- Ollama (local, free)
- LM Studio (local, free)
- OpenAI API (cloud-based)

**Capabilities:**

- Resume optimization
- Cover letter generation
- Skills & certifications recommendations
- Interview preparation questions (HR, job-specific, reverse)
- Professional networking (cold emails, LinkedIn messages, thank you notes)
- Content restructuring

**Configuration:**

- Endpoint URL (configurable via `config.json` or external path)
- API key (if required)
- Model selection (configurable)

## Data Flow Diagrams

### Resume Optimization Flow

```mermaid
graph TD
    A[User Uploads Resume & Job Description] -->|POST /api/upload| B[ResumeController]
    B --> C{Validate Input}
    C -->|Invalid| D[Return 400 Error]
    C -->|Valid| E[Returns 202 Accepted]
    E --> F[Main Thread Returns to User]

    F -.->|Background| G[New BackgroundResume Thread]
    G --> H[Load config.json]
    H --> I[Prepare LLM Request]
    I --> J[Send to LLM Service]
    J --> K[LLM Processes & Returns]
    K --> L[Parse & Format Response]
    L --> M[Save Files]
    M --> N[Resume Ready for Download]

    O[User Polls GET /api/files] -->|Polls| P{New Files Available?}
    P -->|Yes| Q[Display Results]
    P -->|No| R[Wait & Poll Again]

    style A fill:#e1f5ff
    style E fill:#c8e6c9
    style G fill:#fff9c4
    style N fill:#c8e6c9
    style Q fill:#c8e6c9
```

### File Management Flow

```mermaid
graph TD
    A[User Action] -->|Upload| B[POST /api/upload]
    A -->|Download| C[GET /api/files/filename]
    A -->|Delete| D[DELETE /api/files/filename]
    A -->|List| E[GET /api/files]

    B --> B1[Validate File]
    B1 --> B2[Save to Disk]
    B2 --> B3[Return File Info]

    C --> C1[Retrieve File]
    C1 --> C2[Stream to Browser]

    D --> D1[Delete File]
    D1 --> D2[Confirm Deletion]

    E --> E1[List All Files]
    E1 --> E2[Return File Array]

    style A fill:#e1f5ff
    style B3 fill:#c8e6c9
    style C2 fill:#c8e6c9
    style D2 fill:#c8e6c9
    style E2 fill:#c8e6c9
```

## External Configuration Integration

The architecture supports external configuration paths for enhanced flexibility:

### Configuration Path Resolution

```mermaid
graph TD
    A["Check System Property<br/>-Dapp.config.path=/external/path"] -->|Found| D["Use System Property Config"]
    A -->|Not Found| B["Check Environment Variable<br/>APP_CONFIG_PATH=/external/path"]
    B -->|Found| E["Use Environment Variable Config"]
    B -->|Not Found| C["Fall back to Current Directory<br/>./config.json"]
    C -->|Found| F["Use Local config.json"]
    C -->|Not Found| G["Use Default Configuration"]

    style A fill:#e3f2fd
    style B fill:#e3f2fd
    style C fill:#e3f2fd
    style D fill:#c8e6c9
    style E fill:#c8e6c9
    style F fill:#c8e6c9
    style G fill:#fff9c4
```

### External Prompts Directory

```mermaid
graph TD
    A["Check Environment Variable<br/>PROMPTS_DIR=/external/prompts"] -->|Found| D["Use External Prompts Directory"]
    A -->|Not Found| B["Fall back to Application Resource<br/>classpath:prompts/"]
    B -->|Available| E["Use Application Resource Prompts"]
    B -->|Not Available| F["Use Default Prompts"]

    style A fill:#e3f2fd
    style B fill:#e3f2fd
    style D fill:#c8e6c9
    style E fill:#c8e6c9
    style F fill:#fff9c4
```

## Security Considerations

### Input Validation

- All user inputs validated on backend
- File size limits enforced
- CORS properly configured for frontend

### File Handling

- Files stored outside web root
- Temporary files cleaned up after processing
- Filename sanitization to prevent directory traversal

### API Security

- No authentication required for demo (add for production)
- CORS headers properly configured
- HTTPS recommended for production

## Deployment Architecture

### Docker Compose Deployment

```yaml
Services:
  - Frontend: nginx:alpine (Port 80)
  - Backend: eclipse-temurin:21-jre (Port 8080)
  - Network: resume-app-network (shared)
  - Volume: backend-files (persistent)

External Services:
  - LLM Service: localhost:11434 (Ollama) or 1234 (LM Studio)
```

### Kubernetes Deployment (Future)

```plaintext
Namespace: java-resumes
Deployments:
  - frontend-deployment (replicas: 2)
  - backend-deployment (replicas: 2)
Services:
  - frontend-service (ClusterIP)
  - backend-service (ClusterIP)
ConfigMaps:
  - app-config (config.json)
Persistent Volumes:
  - file-storage
```

## Extensibility Points

### Adding New LLM Providers

1. Create new provider class implementing `LLMProvider` interface
2. Add configuration in `config.json`
3. Update `ApiService` with provider detection logic

### Adding New Document Types

1. Create new prompt template in `prompts/` directory
2. Add new endpoint to `ResumeController` (e.g., `/api/generate/new-type`)
3. Use `processPromptRequest()` helper method with prompt type identifier
4. Update frontend UI if user-facing (or use API directly)
5. Document in API_REFERENCE.md and update README.md

**Example**: Interview and networking prompts added in this release:

- Interview preparation endpoints (hr-questions, job-specific, reverse)
- Networking endpoints (cold-email, linkedin-message, thank-you-email)

### Custom Prompt Templates

1. Create prompt file in `prompts/` directory (e.g., `NEW_TYPE.md`)
2. Follow existing prompt structure (clear instructions, input parameters, output format)
3. Load via `PromptService.loadPrompt("NEW_TYPE")`
4. Add controller endpoint using `processPromptRequest()`
5. Test with sample data to validate outputs

**Current Prompt Templates**:

- `RESUME.md` - Resume optimization
- `COVER.md` - Cover letter generation
- `SKILLS.md` - Skills & certifications recommendations
- Interview preparation prompts (hr-questions, job-specific, reverse)
- Networking prompts (cold-email, linkedin-message, thank-you-email)

## Performance Considerations

### Async Processing

- Long-running LLM requests run in background threads
- Frontend polls for results instead of blocking
- Prevents server timeout for large documents

### Caching

- Configuration cached on application startup
- File listings cached in memory
- Consider Redis for distributed caching

### Resource Optimization

- File streaming for large uploads
- Temporary file cleanup
- Connection pooling for external services

## Testing Architecture

### Backend Testing Strategy

- Unit tests: Individual method functionality
- Integration tests: API endpoint behavior
- Mock external services (LLM, file storage)

### Frontend Testing Strategy

- Component tests: React component behavior
- Hook tests: Custom React hooks
- Service tests: API integration mocking

## Key Design Patterns

### MVC Pattern

- **Model**: Data models (Optimize, FileInfo)
- **View**: React components
- **Controller**: Spring REST controllers

### Service Layer Pattern

- Separates business logic from controllers
- Facilitates testing through mocking
- Promotes code reusability

### Repository Pattern (Future)

- Abstract data access
- Support multiple storage backends
- Facilitate database addition

### Strategy Pattern (LLM Integration)

- Different LLM providers (Ollama, OpenAI)
- Pluggable LLM selection
- Easy to add new providers

---

**See also:**

- [Configuration Guide](CONFIGURATION.md) - External config setup
- [API Reference](API_REFERENCE.md) - Endpoint documentation
- [Development Setup](DEVELOPMENT_SETUP.md) - Development environment
