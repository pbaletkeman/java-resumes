# Java Resumes - Architecture Documentation

## Table of Contents

1. [1000ft Process View](#1000ft-process-view)
2. [Sequence Diagram](#sequence-diagram)
3. [Detailed Process View](#detailed-process-view)
4. [Class Dependency Diagram](#class-dependency-diagram)
5. [Component Interactions](#component-interactions)
6. [Data Models](#data-models)
7. [API Contract](#api-contract)

---

## 1000ft Process View

High-level overview showing main system components and data flow:

```mermaid
graph LR
    User["👤 User<br/>Web Browser"]
    UI["🖥️ Web UI<br/>React + TypeScript<br/>Spotlight"]
    API["🔌 REST API<br/>Spring Boot<br/>Controller"]
    Processor["⚙️ Processor<br/>Background Thread<br/>BackgroundResume"]
    LLM["🤖 LLM Service<br/>Ollama/LM Studio<br/>OpenAI API"]
    Storage["💾 File Storage<br/>Local Filesystem"]

    User -->|interacts| UI
    UI -->|HTTP| API
    API -->|spawn| Processor
    Processor -->|HTTP| LLM
    LLM -->|JSON Response| Processor
    Processor -->|save| Storage
    API -->|read/write| Storage
    API -->|return| UI
    UI -->|display| User

    style User fill:#e1f5ff
    style UI fill:#f3e5f5
    style API fill:#fff3e0
    style Processor fill:#e8f5e9
    style LLM fill:#fce4ec
    style Storage fill:#f1f8e9
```

**Key Components**:

- **User**: Interacts via web browser
- **Web UI (Spotlight)**: React-based frontend
- **REST API**: Spring Boot controller handling HTTP requests
- **Processor**: Background thread for async optimization
- **LLM Service**: OpenAI-compatible AI model
- **File Storage**: Local filesystem for uploads/downloads

---

## Sequence Diagram

Detailed sequence of interactions during a resume optimization request:

```mermaid
sequenceDiagram
    participant User as User<br/>(Browser)
    participant UI as Web UI<br/>(React)
    participant API as AdvancedController<br/>(REST)
    participant Service as FilesStorageService<br/>(File Mgmt)
    participant BG as BackgroundResume<br/>(Async Thread)
    participant LLM as ApiService<br/>(LLM Integration)
    participant LLMSvc as LLM Service<br/>(Ollama/OpenAI)

    User->>UI: 1. Select files & options
    User->>UI: 2. Click "Optimize"

    UI->>API: 3. POST /upload<br/>(resume.pdf, job.pdf, optimize config)
    activate API

    API->>Service: 4. save(file)
    activate Service
    Service->>Service: 5. Store files in filesystem
    deactivate Service

    API->>API: 6. Validate optimize request

    alt Request Invalid
        API-->>UI: 400 Bad Request
        UI-->>User: Show error
    else Request Valid
        API->>BG: 7. new BackgroundResume(optimize)
        activate BG

        API-->>UI: 8. 202 Accepted<br/>{"message": "generating"}
        deactivate API
        UI-->>User: Show processing status

        BG->>LLM: 9. produceFiles(optimize, endpoint, apikey)
        activate LLM

        LLM->>LLM: 10. createChatBody(resume, jobDescription)
        LLM->>LLMSvc: 11. sendHttpRequest(ChatBody)
        activate LLMSvc

        LLMSvc-->>LLM: 12. LLMResponse<br/>(optimized content)
        deactivate LLMSvc

        LLM->>LLM: 13. parseResponse() &<br/>extractOptimizedContent()

        LLM->>LLM: 14. convertToMarkdown()
        LLM->>LLM: 15. convertToPDF()<br/>HtmlToPdf

        LLM->>Service: 16. save(optimized_resume.md)<br/>save(optimized_resume.pdf)
        activate Service
        Service->>Service: 17. Store to filesystem
        deactivate Service

        deactivate LLM

        BG->>BG: 18. Processing complete<br/>Log: "all done"
        deactivate BG

        Note over User,LLMSvc: User checks status or<br/>downloads files via API

        User->>UI: 19. Refresh or check files
        UI->>API: 20. GET /files
        activate API
        API->>Service: 21. loadAll()
        activate Service
        Service-->>API: 22. List of files
        deactivate Service
        API-->>UI: 23. JSON file list
        deactivate API
        UI-->>User: 24. Display files with<br/>download links

        User->>API: 25. GET /files/{optimized_resume.pdf}
        activate API
        API->>Service: 26. load(filename)
        activate Service
        Service-->>API: 27. Resource
        deactivate Service
        API-->>UI: 28. File content
        deactivate API
        UI-->>User: 29. Download PDF
    end
```

**Key Sequences**:

1. **Upload Phase**: User submits resume and job description
2. **Validation Phase**: Server validates request
3. **Async Processing**: Background thread created for optimization
4. **LLM Integration**: Communication with LLM service
5. **Output Generation**: Optimized documents created
6. **File Storage**: Results saved to filesystem
7. **Retrieval Phase**: User downloads completed files

---

## Detailed Process View

Process flow showing interaction between major layers:

```mermaid
graph TB
    subgraph Client["🖥️ Client Layer"]
        Browser["Web Browser<br/>React + TypeScript"]
    end

    subgraph REST["🔌 REST Layer<br/>Spring Boot"]
        Controller["AdvancedController<br/>@RestController"]
    end

    subgraph Service["📁 Service Layer"]
        StorageService["FilesStorageService<br/>Interface"]
        StorageImpl["FilesStorageServiceImpl<br/>Implementation"]
    end

    subgraph Model["📊 Model Layer"]
        Optimize["Optimize<br/>Request Data"]
        FileInfo["FileInfo<br/>File Metadata"]
        ResponseMsg["ResponseMessage<br/>Response Wrapper"]
    end

    subgraph Optimizer["🤖 Optimizer Layer"]
        BackgroundResume["BackgroundResume<br/>Runnable Thread"]
        ApiService["ApiService<br/>LLM Integration"]
        HtmlToPdf["HtmlToPdf<br/>Converter"]
        ChatBody["ChatBody<br/>Request Model"]
        LLMResponse["LLMResponse<br/>Response Model"]
    end

    subgraph External["🌐 External Services"]
        LLMService["LLM Service<br/>Ollama/OpenAI"]
    end

    subgraph Storage["💾 Storage Layer"]
        FileSystem["File System<br/>Uploads Directory"]
    end

    Browser -->|HTTP Request| Controller

    Controller -->|POST /upload| Model
    Controller -->|Validate| Optimize
    Controller -->|Spawn| BackgroundResume
    Controller -->|GET /files| StorageService
    Controller -->|Download| StorageImpl

    StorageService -.->|implements| StorageImpl
    StorageImpl -->|Read/Write| FileSystem

    BackgroundResume -->|produceFiles| ApiService
    BackgroundResume -->|Process| Optimize

    ApiService -->|Create Request| ChatBody
    ApiService -->|Serialize| Model
    ApiService -->|HTTP| LLMService
    ApiService -->|Parse| LLMResponse

    ApiService -->|Markdown Output| HtmlToPdf
    HtmlToPdf -->|Convert| FileSystem

    Controller -->|Build Response| ResponseMsg
    ResponseMsg -->|JSON| Browser

    style Client fill:#e1f5ff
    style REST fill:#fff3e0
    style Service fill:#f3e5f5
    style Model fill:#e8f5e9
    style Optimizer fill:#fce4ec
    style External fill:#ffe0b2
    style Storage fill:#f1f8e9
```

**Layer Responsibilities**:

| Layer         | Components                   | Purpose                          |
| ------------- | ---------------------------- | -------------------------------- |
| **Client**    | Web Browser                  | User interface                   |
| **REST**      | AdvancedController           | HTTP endpoints & request routing |
| **Service**   | FilesStorageService          | File operations abstraction      |
| **Model**     | POJO classes                 | Data transfer & validation       |
| **Optimizer** | ApiService, BackgroundResume | LLM integration & processing     |
| **External**  | LLM Service                  | AI model execution               |
| **Storage**   | File System                  | Persistent data storage          |

---

## Class Dependency Diagram

Detailed class relationships and dependencies:

```mermaid
graph TB
    subgraph Controllers["Controllers"]
        AC["AdvancedController<br/>─────────<br/>+ file2PDF()<br/>+ uploadFiles()<br/>+ getListFiles()<br/>+ downloadFile()<br/>+ deleteFile()"]
    end

    subgraph Services["Services"]
        FSS["FilesStorageService<br/>«interface»<br/>─────────<br/>+ init()<br/>+ save()<br/>+ load()<br/>+ delete()<br/>+ loadAll()"]
        FSSI["FilesStorageServiceImpl<br/>─────────<br/>+ init()<br/>+ save()<br/>+ load()<br/>+ delete()<br/>+ loadAll()"]
    end

    subgraph Models["Models"]
        Opt["Optimize<br/>─────────<br/>- promptType: String[]<br/>- temperature: double<br/>- model: String<br/>- resume: String<br/>- jobDescription: String<br/>- jobTitle: String<br/>- company: String"]
        FI["FileInfo<br/>─────────<br/>- filename: String<br/>- url: String<br/>- size: long"]
        RM["ResponseMessage<br/>─────────<br/>- message: String"]
    end

    subgraph Optimizers["Optimizers"]
        BR["BackgroundResume<br/>«Runnable»<br/>─────────<br/>- optimize: Optimize<br/>- endpoint: String<br/>- apikey: String<br/>- root: String<br/>+ run()"]
        AS["ApiService<br/>─────────<br/>- jobDescription: String<br/>- resume: String<br/>+ produceFiles()<br/>+ createChatBody()<br/>+ sendHttpRequest()<br/>+ processResponse()"]
        HTP["HtmlToPdf<br/>─────────<br/>- inputFile: String<br/>- outputFile: String<br/>- options: String<br/>+ convertFile()"]
    end

    subgraph ResponseModels["Response Models"]
        LR["LLMResponse<br/>─────────<br/>- model: String<br/>- choices: List<Choice><br/>- usage: Usage"]
        Ch["Choice<br/>─────────<br/>- message: Message<br/>- index: int"]
        Msg["Message<br/>─────────<br/>- role: String<br/>- content: String"]
        Us["Usage<br/>─────────<br/>- prompt_tokens: int<br/>- completion_tokens: int<br/>- total_tokens: int"]
    end

    subgraph RequestModels["Request Models"]
        CB["ChatBody<br/>─────────<br/>- model: String<br/>- messages: List<Message><br/>- temperature: double<br/>- stream: boolean"]
    end

    subgraph Config["Configuration"]
        Cfg["Config<br/>─────────<br/>- endpoint: String<br/>- apikey: String"]
    end

    subgraph Utilities["Utilities"]
        Util["Utility<br/>─────────<br/>+ readFileAsString()<br/>+ convertLineEndings()<br/>+ removeFileExtension()"]
    end

    AC -->|uses| FSS
    AC -->|uses| Opt
    AC -->|uses| HTP
    AC -->|creates| RM
    AC -->|returns| FI

    FSSI -.->|implements| FSS

    BR -->|contains| Opt
    BR -->|uses| AS
    BR -->|uses| Cfg
    BR -->|uses| Util

    AS -->|receives| Opt
    AS -->|creates| CB
    AS -->|receives| LR
    AS -->|uses| HTP

    CB -->|contains| Msg
    LR -->|contains| Ch
    Ch -->|contains| Msg
    LR -->|contains| Us

    HTP -->|creates| FI

    Cfg -->|reads via| Util

    style Controllers fill:#fff3e0
    style Services fill:#f3e5f5
    style Models fill:#e8f5e9
    style Optimizers fill:#fce4ec
    style ResponseModels fill:#ffe0b2
    style RequestModels fill:#f1f8e9
    style Config fill:#c8e6c9
    style Utilities fill:#d1c4e9
```

**Key Relationships**:

- **AdvancedController** → FilesStorageService: Dependency injection
- **FilesStorageServiceImpl** → FilesStorageService: Interface implementation
- **BackgroundResume** → ApiService: Delegates LLM operations
- **ApiService** → LLMResponse: Parses API responses
- **ApiService** → HtmlToPdf: Converts outputs to PDF

---

## Component Interactions

### File Upload & Processing Flow

```mermaid
sequenceDiagram
    autonumber

    actor User
    participant Controller as AdvancedController
    participant Validator as Request Validator
    participant Spawner as Thread Spawner
    participant Background as BackgroundResume
    participant Optimizer as ApiService
    participant LLMApi as LLM Service
    participant FileOps as FilesStorageService

    User->>Controller: POST /upload
    Note over Controller: Files + optimize config

    Controller->>Validator: Validate request
    alt Invalid
        Validator-->>Controller: Error
        Controller-->>User: 417 Expectation Failed
    else Valid
        Validator-->>Controller: Valid
        Controller->>FileOps: Save uploaded files
        FileOps->>FileOps: Create upload directory
        FileOps-->>Controller: Files saved

        Controller->>Spawner: Create background thread
        Spawner->>Background: new BackgroundResume(optimize)
        Background-->>Spawner: Thread created
        Spawner->>Background: thread.start()

        Controller-->>User: 202 Accepted
        Note over Controller,User: Response sent immediately

        activate Background
        Background->>Optimizer: produceFiles()

        Optimizer->>Optimizer: Extract resume & job description
        Optimizer->>LLMApi: HTTP POST /api/chat

        LLMApi->>LLMApi: Process with LLM
        LLMApi-->>Optimizer: JSON response

        Optimizer->>Optimizer: Parse LLMResponse
        Optimizer->>Optimizer: Extract content
        Optimizer->>Optimizer: Format as Markdown

        Optimizer->>FileOps: Save optimized_resume.md
        FileOps->>FileOps: Write to filesystem

        Optimizer->>FileOps: Save optimized_resume.pdf
        FileOps->>FileOps: Convert Markdown to PDF

        deactivate Background
        Background->>Background: Log completion
    end

    Note over User,FileOps: User can retrieve files via GET /files
```

---

## Data Models

### Optimize Request Model

```mermaid
graph LR
    subgraph OptimizeModel["Optimize Request"]
        PT["promptType<br/>string[]<br/>'Resume', 'CoverLetter'"]
        Temp["temperature<br/>double<br/>0.0 - 1.0"]
        Model["model<br/>string<br/>gemma-3-4b-it"]
        Resume["resume<br/>string<br/>Document content"]
        JD["jobDescription<br/>string<br/>Document content"]
        JT["jobTitle<br/>string<br/>Target position"]
        Company["company<br/>string<br/>Target company"]
    end

    style OptimizeModel fill:#e8f5e9
    style PT fill:#c8e6c9
    style Temp fill:#c8e6c9
    style Model fill:#c8e6c9
    style Resume fill:#a5d6a7
    style JD fill:#a5d6a7
    style JT fill:#c8e6c9
    style Company fill:#c8e6c9
```

### FileInfo Response Model

```mermaid
graph LR
    subgraph FileInfoModel["FileInfo Response"]
        FN["filename<br/>string"]
        URL["url<br/>string<br/>Download URL"]
        Size["size<br/>long<br/>Bytes"]
    end

    style FileInfoModel fill:#f3e5f5
    style FN fill:#e1bee7
    style URL fill:#e1bee7
    style Size fill:#e1bee7
```

### LLMResponse Model

```mermaid
graph LR
    subgraph LLMResp["LLMResponse"]
        ModelName["model<br/>string"]
        Choices["choices<br/>List<Choice>"]
        Usage["usage<br/>Usage"]
    end

    subgraph Choice["Choice"]
        Message["message<br/>Message"]
        Index["index<br/>int"]
    end

    subgraph Message["Message"]
        Role["role<br/>string<br/>assistant"]
        Content["content<br/>string<br/>Optimized content"]
    end

    subgraph Usage["Usage"]
        PromptTokens["prompt_tokens<br/>int"]
        CompletionTokens["completion_tokens<br/>int"]
        TotalTokens["total_tokens<br/>int"]
    end

    LLMResp --> Choices
    Choices --> Choice
    Choice --> Message
    Choice --> Index
    Message --> Role
    Message --> Content
    LLMResp --> Usage

    style LLMResp fill:#ffe0b2
    style Choice fill:#ffcc80
    style Message fill:#ffb74d
    style Usage fill:#ffa726
```

---

## API Contract

### REST Endpoint Specification

```mermaid
graph LR
    subgraph API["API Endpoints"]
        POST["POST /upload<br/>Resume optimization"]
        GET1["GET /files<br/>List files"]
        GET2["GET /files/{filename}<br/>Download file"]
        DELETE["DELETE /files/{filename}<br/>Delete file"]
        DELALL["POST /delete-all<br/>Delete all"]
        MD2PDF["POST /markdownFile2PDF<br/>Convert MD to PDF"]
    end

    style API fill:#fff3e0
```

### Key Endpoints

| HTTP       | Endpoint            | Payload                                     | Response                    |
| ---------- | ------------------- | ------------------------------------------- | --------------------------- |
| **POST**   | `/upload`           | Multi-part form: resume, job, optimize JSON | `{"message": "generating"}` |
| **GET**    | `/files`            | None                                        | `List<FileInfo>`            |
| **GET**    | `/files/{filename}` | Path parameter: filename                    | File content (binary)       |
| **DELETE** | `/files/{filename}` | Path parameter: filename                    | `{"message": "deleted"}`    |
| **POST**   | `/delete-all`       | None                                        | `{"message": "deleted"}`    |
| **POST**   | `/markdownFile2PDF` | Multi-part form: file                       | `{"message": "converted"}`  |

---

## Technology Mapping

```mermaid
graph TB
    subgraph Frontend["Frontend"]
        React["React<br/>JavaScript Framework"]
        TS["TypeScript<br/>Type Safety"]
        Prime["PrimeReact<br/>UI Components"]
    end

    subgraph Backend["Backend - Spring Boot 3.5"]
        Web["spring-boot-starter-web<br/>REST Support"]
        OpenAPI["springdoc-openapi<br/>Swagger/API Docs"]
    end

    subgraph Processing["Processing"]
        Gson["Gson 2.13.1<br/>JSON Processing"]
        Jsoup["jsoup 1.15.4<br/>HTML Parsing"]
        CommonMark["commonmark 0.24.0<br/>Markdown Processing"]
    end

    subgraph PDFGeneration["PDF Generation"]
        FlySaucer["flying-saucer-core<br/>HTML Rendering"]
        OpenPDF["flying-saucer-pdf-openpdf<br/>PDF Creation"]
    end

    subgraph Testing["Testing"]
        JUnit["JUnit 5<br/>Test Framework"]
        MockMvc["MockMvc<br/>Controller Testing"]
    end

    subgraph QualityTools["Quality Tools"]
        Checkstyle["Checkstyle 10.14.2<br/>Code Quality"]
    end

    style Frontend fill:#f3e5f5
    style Backend fill:#fff3e0
    style Processing fill:#e8f5e9
    style PDFGeneration fill:#fce4ec
    style Testing fill:#e1f5ff
    style QualityTools fill:#f1f8e9
```

---

## Deployment Architecture

```mermaid
graph TB
    subgraph LocalDev["Local Development"]
        LLMLocal["LLM Service<br/>Ollama/LM Studio<br/>Port 11434"]
        AppLocal["Spring Boot App<br/>Port 8080"]
    end

    subgraph Production["Production Optional"]
        LLMProd["OpenAI API<br/>or Remote LLM<br/>HTTPS"]
        AppProd["Spring Boot App<br/>Cloud Hosting<br/>Docker Container"]
    end

    subgraph Communication["Network Communication"]
        HTTP["HTTP/HTTPS<br/>REST APIs"]
    end

    AppLocal -->|HTTP| LLMLocal
    AppLocal -->|HTTP| HTTP

    AppProd -->|HTTPS| LLMProd
    HTTP -->|HTTPS| LLMProd

    style LocalDev fill:#c8e6c9
    style Production fill:#ffccbc
    style Communication fill:#bbdefb
```

---

## Summary

This architecture provides:

1. **Scalability**: Background processing prevents blocking
2. **Modularity**: Clear separation of concerns
3. **Maintainability**: Layered architecture with interfaces
4. **Flexibility**: Multiple file format support
5. **Integration**: Easy LLM service switching
6. **Testing**: Comprehensive unit test coverage

For detailed implementation, see [../docs/README.md](./README.md)
