# Architecture Diagrams & Visual Documentation

System architecture, component diagrams, and data flow visualizations for the java-resumes application.

- [Architecture Diagrams \& Visual Documentation](#architecture-diagrams--visual-documentation)
  - [📆 Overview](#-overview)
  - [🎨 System Architecture](#-system-architecture)
    - [Overall Architecture Diagram](#overall-architecture-diagram)
  - [🔄 Data Flow Diagram](#-data-flow-diagram)
  - [📋 UML Diagrams](#-uml-diagrams)
    - [Backend Class Diagram](#backend-class-diagram)
    - [Frontend Component Diagram](#frontend-component-diagram)
  - [🔗 Component Relationships](#-component-relationships)
    - [Backend Component Interaction](#backend-component-interaction)
    - [Frontend Component Hierarchy](#frontend-component-hierarchy)
  - [🚀 Deployment Architecture](#-deployment-architecture)
    - [Docker Container Architecture](#docker-container-architecture)
    - [Docker Compose Orchestration](#docker-compose-orchestration)
  - [🌐 API Integration](#-api-integration)
    - [LLM Service Integration](#llm-service-integration)
  - [🚰 Technology Stack Visualization](#-technology-stack-visualization)
    - [Backend Stack](#backend-stack)
    - [Frontend Stack](#frontend-stack)
    - [Infrastructure Stack](#infrastructure-stack)
  - [📋 Data Models](#-data-models)
    - [Core Data Models](#core-data-models)
  - [🔗 Related Documentation](#-related-documentation)

---

## 📆 Overview

Comprehensive visual documentation of system architecture, showing how components interact and data flows through the application.

**Architecture Covers:**

- ✅ Frontend React application
- ✅ Spring Boot backend services
- ✅ LLM API integration (Ollama/OpenAI)
- ✅ File storage and processing
- ✅ Docker containerization
- ✅ Component interactions

---

## 🎨 System Architecture

### Overall Architecture Diagram

![System Architecture Overview](../backend/system-architecture-backend.png)

**Shows:**

- Spring Boot application with REST API
- Controller layer (API endpoints)
- Service layer (business logic)
- Data models and repositories
- External API connections
- File storage integration

![Frontend Architecture](../frontend/system-architecture-frontend.png)

**Shows:**

- React component structure
- State management (Context API)
- Custom hooks organization
- API communication layer
- Theme and styling system
- Component hierarchy

---

## 🔄 Data Flow Diagram

The complete flow of resume optimization from upload to download:

![Document Processing & Data Flow](./architecture/document-processing-flow.png)

**Flow Steps:**

1. **Upload Phase**
   - User uploads resume and job description
   - Frontend sends to backend via REST API
   - Backend validates files

2. **Processing Phase**
   - Backend spawns background thread
   - LLM API called with document content
   - Content optimized and enhanced

3. **Output Phase**
   - Generated files (markdown, PDF) created
   - Files saved to filesystem
   - Frontend polls for results

4. **Download Phase**
   - Frontend detects new files
   - User downloads optimized documents
   - Files available in File History

---

## 📋 UML Diagrams

### Backend Class Diagram

![Backend UML Class Diagram](../backend/backend-uml.png)

**Key Classes:**

- `ResumeController` - REST API endpoints
- `BackgroundResume` - Async processing thread
- `ApiService` - LLM integration
- `FilesStorageService` - File operations
- `Optimize` - Request model
- `FileInfo` - Response model

**Relationships:**

- Controllers use Services
- Services use Model classes
- Background threads manage processing
- External API integration points

### Frontend Component Diagram

![Frontend UML Component Diagram](../frontend/frontend-uml.png)

**Key Components:**

- `App` - Main application wrapper
- `MainContentTab` - Primary UI interface
- `FileHistoryTab` - File management
- `SettingsTab` - Configuration
- `ToolsTab` - Utilities
- Custom Hooks - `useApi`, `useTheme`

**Relationships:**

- Components use custom hooks
- Context providers manage state
- API hooks handle communication
- Theme context provides styling

---

## 🔗 Component Relationships

### Backend Component Interaction

```mermaid
graph TD
    A["ResumeController<br/>REST API Endpoints"] --> B["FilesStorageService<br/>File Upload/Download"]
    A --> C["BackgroundResume<br/>Async Processing"]
    A --> D["Model Classes<br/>DTOs & Entities"]
    C --> E["ApiService<br/>LLM Integration"]
    E --> F["External LLM API<br/>Ollama/OpenAI"]

    style A fill:#e3f2fd
    style B fill:#f3e5f5
    style C fill:#fff3e0
    style D fill:#fce4ec
    style E fill:#e0f2f1
    style F fill:#f1f8e9
```

### Frontend Component Hierarchy

```mermaid
graph TD
    A["App<br/>Main Component"] --> B["MainContentTab<br/>Resume Editor"]
    A --> C["FileHistoryTab<br/>File Management"]
    A --> D["SettingsTab<br/>Configuration"]
    A --> E["ToolsTab<br/>Utilities"]
    A --> F["Custom Hooks<br/>useApi, useTheme"]

    B --> G["API Service"]
    C --> G
    D --> G
    E --> G

    style A fill:#e3f2fd
    style B fill:#f3e5f5
    style C fill:#fff3e0
    style D fill:#fce4ec
    style E fill:#e0f2f1
    style F fill:#f1f8e9
    style G fill:#e8eaf6
```

---

## 🚀 Deployment Architecture

### Docker Container Architecture

**Backend Container:**

- Base: `eclipse-temurin:21-jdk`
- Framework: Spring Boot 3.5.1
- Build Tool: Gradle 8.10
- Port: 8080
- Features: REST API, async processing

**Frontend Container (Development):**

- Base: `node:22-alpine`
- Framework: React 19 + Vite
- Port: 3000
- Features: Hot module reload, dev server

**Frontend Container (Production):**

- Base: `nginx:alpine`
- Build: Static React build
- Port: 80
- Features: Static file serving

### Docker Compose Orchestration

```mermaid
graph TB
    subgraph compose["Docker Compose Network"]
        A["🐳 Backend Service<br/>Spring Boot 3.5<br/>Port: 8080"]
        B["🐳 Frontend Service<br/>React + Vite<br/>Port: 3000"]
        C["🔗 External APIs<br/>Ollama/OpenAI"]
    end

    B -->|HTTP Requests| A
    A -->|REST Calls| C

    style compose fill:#eceff1
    style A fill:#bbdefb
    style B fill:#c8e6c9
    style C fill:#ffccbc
```

---

## 🌐 API Integration

### LLM Service Integration

**Supported Services:**

- Ollama (local LLM, free)
- OpenAI (commercial API)
- Any OpenAI-compatible endpoint

**Request Flow:**

1. Frontend → Backend: Resume + Job Description
2. Backend → LLM Service: OpenAI-compatible request
3. LLM Service → Backend: Optimized content
4. Backend → Frontend: Generated files (markdown, PDF)

**Configuration:**

```json
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "ollama",
  "model": "mistral"
}
```

---

## 🚰 Technology Stack Visualization

### Backend Stack

```mermaid
graph TD
    A["Spring Boot 3.5.1"] --> B["Spring Web<br/>REST API"]
    A --> C["Spring Data<br/>Data Access"]
    A --> D["Build Tool<br/>Gradle 8.10"]
    A --> E["Testing<br/>JUnit 5"]
    A --> F["JSON Processing<br/>Gson"]
    A --> G["HTML Parsing<br/>jsoup"]
    A --> H["PDF Generation<br/>Flying Saucer"]

    style A fill:#bbdefb
    style B fill:#c8e6c9
    style C fill:#fff9c4
    style D fill:#f8bbd0
    style E fill:#ffccbc
    style F fill:#c5cae9
    style G fill:#b2dfdb
    style H fill:#ffe0b2
```

### Frontend Stack

```mermaid
graph TD
    A["React 19 + TypeScript 5.9"] --> B["Build Tool<br/>Vite 7.3.1"]
    A --> C["UI Components<br/>PrimeReact 10.9.7"]
    A --> D["Styling<br/>Tailwind CSS 4.1.18"]
    A --> E["Testing<br/>Vitest"]
    A --> F["HTTP Client<br/>Axios"]
    A --> G["Icons<br/>React Icons"]

    style A fill:#c8e6c9
    style B fill:#bbdefb
    style C fill:#fff9c4
    style D fill:#f8bbd0
    style E fill:#ffccbc
    style F fill:#c5cae9
    style G fill:#b2dfdb
```

### Infrastructure Stack

```mermaid
graph TD
    A["Docker & Docker Compose"] --> B["Backend Container<br/>eclipse-temurin:21-jdk"]
    A --> C["Frontend Dev<br/>node:22-alpine"]
    A --> D["Frontend Prod<br/>nginx:alpine"]
    A --> E["Network<br/>Internal DNS Resolution"]

    style A fill:#ffccbc
    style B fill:#bbdefb
    style C fill:#c8e6c9
    style D fill:#fff9c4
    style E fill:#f8bbd0
```

---

## 📋 Data Models

### Core Data Models

**Optimize Request Model:**

- Resume content
- Job description
- Target company/position
- LLM temperature setting
- Output types (Resume, Cover Letter)

**FileInfo Response Model:**

- Filename
- Download URL
- File size
- Creation timestamp

**BackgroundResume Thread:**

- Manages async processing
- Coordinates API calls
- Handles file writing
- Error handling

---

## 🔗 Related Documentation

- **[Backend README](../BACKEND_README.md)** - Backend API and service documentation
- **[Frontend README](../../frontend/README.md)** - Frontend component and hook documentation
- **[Docker Setup Guide](../DOCKER_DEV_SETUP.md)** - Docker configuration and deployment
- **[Main Architecture Document](../Architecture.md)** - Comprehensive technical documentation

---

**Last Updated:** January 22, 2026
**Status:** ✅ Complete with all architecture diagrams

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
