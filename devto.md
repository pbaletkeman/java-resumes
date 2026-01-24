---
title: "Building an AI-Powered Resume Generator with Java 21, React, and OpenAI/Ollama"
description: "A full-stack deep dive into integrating LLMs for document generation"
tags: java, react, ai, llm, fullstack, openai, ollama, spring-boot
published: true
---

# Building an AI-Powered Resume Generator: Architecture & Implementation

## Overview

I've been working on a full-stack application that leverages LLMs to generate polished, professional resume content. This post is a technical walkthrough of the architecture, integration points, and key implementation details.

**Tech Stack:**

- **Backend:** Java 21, Spring Boot 3.x, Gradle
- **Frontend:** React 19, TypeScript, Vite
- **LLM Integration:** OpenAI API / Ollama (OpenAI-compatible endpoint)
- **Data Format:** JSON-driven resume model
- **Build Tooling:** Gradle (backend), Node (frontend)

Repository: https://github.com/pbaletkeman/java-resumes

---

## Architecture Overview

```
┌─────────────┐
│   React UI  │
│ (TypeScript)│
└──────┬──────┘
       │ HTTP/REST
       ↓
┌─────────────────────────────────┐
│   Spring Boot REST API          │
│  (Java 21, Gradle 8.10)         │
│                                 │
│  ├─ ResumeController            │
│  ├─ FilesStorageService         │
│  └─ ApiService (LLM gateway)    │
└──────────┬──────────────────────┘
           │
      ┌────┴─────┐
      ↓          ↓
  ┌────────┐  ┌────────────┐
  │ Ollama │  │ OpenAI API │
  │(local) │  │  (cloud)   │
  └────────┘  └────────────┘
```

---

## Key Components & Design Decisions

### 1. **REST API Layer (Spring Boot)**

The backend exposes endpoints for:

- **File uploads** (multipart/form-data)
- **Resume optimization** (async background processing)
- **File retrieval** (results polling)
- **File management** (list, download, delete)

**Key Endpoint Pattern:**

```java
@PostMapping(path = "/api/upload")
public ResponseEntity<ResponseMessage> optimizeResume(
    @RequestParam("optimize") String optimizeJson,
    @RequestParam("resume") MultipartFile resume,
    @RequestParam("job") MultipartFile job) {

    // Validate inputs
    if (resume.isEmpty() || job.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ResponseMessage("No file/invalid file provided"));
    }

    // Spawn background thread for LLM processing (non-blocking)
    Thread thread = new Thread(new BackgroundResume(optimize, root));
    thread.start();

    // Return 202 Accepted immediately
    return ResponseEntity.status(HttpStatus.ACCEPTED)
        .body(new ResponseMessage("generating"));
}
```

**Why this pattern?**

- LLM API calls are slow (2-30+ seconds)
- HTTP connections timeout if we wait for LLM
- `202 Accepted` signals async processing to the client
- Frontend polls `/api/files` until results appear

### 2. **Async Background Processing**

The `BackgroundResume` class handles long-running operations:

```java
public class BackgroundResume implements Runnable {
    private final Optimize optimize;
    private final String root;

    @Override
    public void run() {
        try {
            // 1. Load LLM configuration
            String configStr = Utility.readFileAsString("config.json");
            Config config = new Gson().fromJson(configStr, Config.class);

            // 2. Build LLM request
            ChatBody chatBody = ApiService.createChatBody(optimize);

            // 3. Call LLM (OpenAI-compatible API)
            LLMResponse response = ApiService.produceFiles(
                optimize,
                config.getEndpoint(),
                config.getApikey(),
                config.getModel()
            );

            // 4. Save results (Markdown + PDF)
            FilesStorageService.save(response.getContent());

            LOGGER.info("Resume optimization completed");
        } catch (Exception e) {
            LOGGER.error("Background task failed: {}", e.getMessage());
        }
    }
}
```

**Why background threads instead of async/await?**

- Simple, synchronous model
- No need for reactive framework overhead
- Easy to reason about error handling
- Works well for moderate concurrency

### 3. **LLM Integration (OpenAI-Compatible API)**

The `ApiService` class abstracts LLM provider differences:

```java
public class ApiService {
    public static LLMResponse produceFiles(
        Optimize optimize,
        String endpoint,
        String apiKey,
        String model) throws Exception {

        // Build OpenAI-compatible request
        ChatBody chatBody = new ChatBody();
        chatBody.setModel(model);
        chatBody.setMessages(createPrompt(optimize));
        chatBody.setTemperature(optimize.getTemperature());

        // Send to LLM
        HttpClient client = HttpClient.newHttpClient();
        String jsonRequest = new Gson().toJson(chatBody);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(endpoint + "/v1/chat/completions"))
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
            .build();

        HttpResponse<String> response = client.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        );

        // Parse response
        LLMResponse llmResponse = new Gson().fromJson(
            response.body(),
            LLMResponse.class
        );

        return llmResponse;
    }
}
```

**Why OpenAI-compatible format?**

- Works with Ollama (local models)
- Works with OpenAI (cloud models)
- Works with Azure OpenAI, Together.ai, etc.
- Single integration code path
- Easy to swap providers

**Configuration (config.json):**

```json
{
  "endpoint": "http://localhost:11434",
  "apikey": "ollama",
  "model": "mistral:7b"
}
```

---

## Frontend Architecture (React + TypeScript)

### Core Hook: `useApi`

Centralized API communication:

```typescript
export function useApi() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const execute = async (fn: () => Promise<any>) => {
    setLoading(true);
    setError(null);
    try {
      await fn();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return { execute, loading, error };
}
```

### File Upload & Polling Pattern

```typescript
function MainContentTab() {
  const { execute, loading } = useApi();
  const [generatedFiles, setGeneratedFiles] = useState<File[]>([]);

  const handleSubmit = async (formData: FormData) => {
    await execute(async () => {
      // 1. Upload resume + job description
      await fileService.uploadForOptimization(formData);

      // 2. Start polling for results
      let attempts = 0;
      while (attempts < 60) { // 5 minutes max
        await new Promise(r => setTimeout(r, 5000)); // Poll every 5s

        const files = await fileService.listFiles();
        const newFiles = files.filter(f =>
          f.name.endsWith('.pdf') &&
          f.timestamp > formData.get('uploadTime')
        );

        if (newFiles.length > 0) {
          setGeneratedFiles(newFiles);
          break;
        }
        attempts++;
      }
    });
  };

  return (
    // UI for upload and display results
  );
}
```

**Why polling instead of WebSockets?**

- Simpler client/server contract
- Works through corporate proxies/firewalls
- No need for persistent connection
- Acceptable for batch processing workflows

---

## Data Model: Optimize DTO

```java
public class Optimize {
    private String[] promptType;      // ["Resume", "CoverLetter", "Skills"]
    private double temperature;        // 0.0-1.0 (creativity level)
    private String model;              // Model identifier from config
    private String company;            // Target company name
    private String jobTitle;           // Target job title
    private String jobDescription;     // Full job posting text
    private String resume;             // User's current resume

    // Getters/setters...
}
```

This DTO drives:

1. **Prompt construction** - What content to generate
2. **LLM parameters** - Temperature, model selection
3. **Output filtering** - Which sections to include

---

## Key Implementation Challenges & Solutions

### Challenge 1: LLM Response Time

**Problem:** API calls can take 10-30+ seconds
**Solution:**

- Return `202 Accepted` immediately
- Process async in background thread
- Frontend polls for completion

### Challenge 2: File Format Conversion

**Problem:** LLM outputs plain text; need PDF with formatting
**Solution:**

- Convert Markdown → HTML (CommonMark parser)
- Convert HTML → PDF (Flying Saucer library)
- Save both Markdown + PDF for flexibility

### Challenge 3: Local vs Cloud LLM

**Problem:** Different APIs for Ollama vs OpenAI
**Solution:**

- Use OpenAI-compatible format (both support it)
- Config-driven endpoint selection
- Single integration point

### Challenge 4: Test Isolation

**Problem:** Tests failing due to state dependencies (file existence)
**Solution:**

```java
@BeforeEach
void setUp() throws IOException {
    Path uploadsPath = Paths.get("uploads");
    Files.createDirectories(uploadsPath);
    // Create dummy files for delete tests, etc.
    Files.write(uploadsPath.resolve("resume.pdf"),
        "dummy".getBytes());
}
```

---

## Deployment Considerations

### Local Development

```bash
# Terminal 1: Start Ollama
ollama serve
ollama pull mistral:7b

# Terminal 2: Run backend
./gradlew bootRun  # Listens on :8080

# Terminal 3: Run frontend
cd frontend && npm run dev  # Listens on :5173
```

### Cloud Deployment

```properties
# application.properties
server.port=8080
upload.path=/data/uploads
# Spring will detect OpenAI config from environment
```

---

## Testing Strategy

**80%+ Coverage Target:**

1. **Controller Tests** - HTTP layer with MockMvc
2. **Service Tests** - Business logic, mocked LLM
3. **Integration Tests** - Full request flow
4. **Model Tests** - DTO serialization/validation

```bash
./gradlew test                    # Run all tests
./gradlew test --tests ClassName  # Run specific test
./gradlew checkstyleMain          # Code quality (100% compliance)
```

---

## Performance & Scalability Notes

1. **Horizontal Scaling:** Add more backend instances behind load balancer
2. **Rate Limiting:** Implement per-user quotas for LLM API costs
3. **Caching:** Cache LLM responses for identical inputs
4. **Async Queue:** For high volume, use message queue (RabbitMQ, Kafka)
5. **File Storage:** Consider cloud storage (S3, Azure Blob) vs local filesystem

---

## ⚠️ Important Considerations

### LLM Hallucination Risk

**Critical:** LLMs can generate plausible-sounding but inaccurate content. This includes:

- Fabricated job experiences
- Incorrect technical skills
- Made-up company names or achievements
- Dates and timelines that don't align with reality

**Mitigation:**

- **Always proofread generated content** before using it
- Cross-check facts against source documents
- Verify all claims in the resume
- Consider this tool as a **content enhancement tool**, not a replacement for human review
- Use it to refine and polish verified information, not to generate unverified content

### Processing Time

**Important:** File generation is NOT instant:

- **Local models (Ollama):** 30 seconds to 5+ minutes depending on model size (7B models are faster, 13B+ models take longer)
- **Cloud models (OpenAI):** 5-30 seconds typically, but can vary with load
- **Large job descriptions:** Processing time increases with input size
- **Network latency:** Slower connections add to total time

**Frontend Polling:**

```typescript
// Default: polls every 5 seconds for up to 5 minutes (60 attempts)
// For longer processing, increase attempts or polling interval
let attempts = 0;
while (attempts < 60) {
  // Adjust this for longer waits
  await new Promise((r) => setTimeout(r, 5000)); // 5 seconds
  // ... check for files
  attempts++;
}
```

**User Experience:**

- Display a progress indicator during processing
- Show estimated wait time based on model selection
- Allow users to check back later via job ID
- Consider implementing email notifications when complete

---

## Code Quality Standards

- **Checkstyle:** 100% compliance (120 char line limit)
- **Test Coverage:** 80%+ target
- **Java Version:** Java 21 LTS with modern features
- **Spring Boot:** Version 3.5.1 with latest practices

---

## What's Next?

Potential improvements:

- [ ] WebSocket support for real-time updates
- [ ] Template system for different resume formats
- [ ] Batch processing for multiple candidates
- [ ] Integration with LinkedIn/job boards
- [ ] A/B testing for LLM prompt optimization
- [ ] Cost analytics for OpenAI usage

---

## Lessons Learned

1. **Async by Default** - HTTP endpoints should never block on slow operations
2. **Embrace Standards** - OpenAI-compatible API is a superpower
3. **Simple Patterns > Complex Frameworks** - Background threads work great for this use case
4. **Test Independence** - Always set up required state in @BeforeEach
5. **Config Over Code** - Keep LLM provider flexible via configuration

---

## Get Started

**Repository:** <https://github.com/pbaletkeman/java-resumes>

**Quick Start:**

```bash
git clone https://github.com/pbaletkeman/java-resumes
cd java-resumes
./gradlew clean build
./gradlew bootRun
# Visit http://localhost:8080/spotlight/index.html
```

---

## Credits

Special thanks to **Shaw Talebi** for his excellent [tutorial on building resume optimization tools](https://www.youtube.com/watch?v=R5WXaxmb6m4), which served as the inspiration and starter foundation for this project.

---

Have you built LLM integrations in Java? What patterns did you use? Drop a comment!

**Discussion Topics:**

- Async patterns for LLM integrations
- Local vs cloud LLM trade-offs
- Resume optimization strategies
- Full-stack Java + React workflows

---

_Tags: #Java #LLM #AI #React #SpringBoot #OpenAI #Ollama #FullStack #Architecture_
