# Technology Stack

Complete list of all technologies, frameworks, and tools used in the java-resumes project.

## Table of Contents

- [Backend Technologies](#-backend-technologies)
  - [Core Framework](#core-framework)
  - [Server & Web](#server--web)
  - [JSON & Serialization](#json--serialization)
  - [Document Processing](#document-processing)
  - [Testing](#testing)
  - [Code Quality](#code-quality)
  - [Additional Libraries](#additional-libraries)
- [Frontend Technologies](#-frontend-technologies)
  - [Core Framework](#core-framework-1)
  - [Build & Development](#build--development)
  - [UI & Styling](#ui--styling)
  - [HTTP & API](#http--api)
  - [Development Tools](#development-tools)
  - [Testing](#testing-1)
  - [Type Safety](#type-safety)
- [DevOps & Infrastructure](#-devops--infrastructure)
  - [Containerization](#containerization)
  - [Docker Images](#docker-images)
- [AI/LLM Integration](#-aillm-integration)
  - [Supported LLM Services](#supported-llm-services)
  - [API Integration](#api-integration)
- [Database (Future)](#-database-future)
- [Cloud & Deployment (Future)](#-cloud--deployment-future)
- [Monitoring & Observability (Future)](#-monitoring--observability-future)
- [Security (Current & Future)](#-security-current--future)
- [Documentation & Communication](#documentation--communication)
- [Testing & Quality](#testing--quality)
- [Development Tools](#development-tools-1)

---

##  Backend Technologies

### Core Framework

| Technology  | Version | Purpose                   |
| ----------- | ------- | ------------------------- |
| Spring Boot | 3.5.1   | Application framework     |
| Java        | 21 LTS  | Programming language      |
| Gradle      | 8.7     | Build tool and automation |

### Server & Web

| Technology         | Version | Purpose                     |
| ------------------ | ------- | --------------------------- |
| Spring Web         | 3.5.1   | REST API and web support    |
| Spring Boot Tomcat | 10.x    | Embedded web server         |
| SpringDoc OpenAPI  | 2.8.7   | API documentation (Swagger) |

### JSON & Serialization

| Technology | Version | Purpose      |
| ---------- | ------- | ------------ |
| Gson       | 2.13.1  | JSON parsing |

### Document Processing

| Technology    | Version | Purpose                  |
| ------------- | ------- | ------------------------ |
| Flying Saucer | 9.1.22  | HTML/CSS to PDF renderer |
| CommonMark    | 0.24.0  | Markdown parser          |
| jsoup         | 1.15.4  | HTML parser              |

### Testing

| Technology  | Version | Purpose             |
| ----------- | ------- | ------------------- |
| JUnit 5     | 5.x     | Unit testing        |
| Mockito     | Latest  | Mocking library     |
| Spring Test | 3.5.1   | Integration testing |
| AssertJ     | Latest  | Fluent assertions   |

### Code Quality

| Technology | Version | Purpose          |
| ---------- | ------- | ---------------- |
| Checkstyle | 10.14.2 | Code style check |

### Additional Libraries

| Technology     | Version | Purpose                |
| -------------- | ------- | ---------------------- |
| SLF4J          | Latest  | Logging facade         |
| Logback        | Latest  | Logging implementation |
| Apache Commons | Latest  | Common utilities       |

---

##  Frontend Technologies

### Core Framework

| Technology | Version | Purpose                |
| ---------- | ------- | ---------------------- |
| React      | 19.2.0  | UI component framework |
| TypeScript | 5.9.3   | Type-safe JavaScript   |
| Node.js    | 22 LTS  | JavaScript runtime     |
| npm        | 10+     | Package manager        |

### Build & Development

| Technology | Version | Purpose                 |
| ---------- | ------- | ----------------------- |
| Vite       | 7.2.4   | Fast build tool and dev |
| esbuild    | Latest  | JavaScript bundler      |
| Rollup     | Latest  | Module bundler          |

### UI & Styling

| Technology | Version | Purpose              |
| ---------- | ------- | -------------------- |
| PrimeReact | 10.9.7  | UI component library |
| PrimeIcons | 7.0.0   | Icon library         |
| Tailwind   | 4.1.18  | Utility-first CSS    |
| CSS 3      | Latest  | Styling              |

### HTTP & API

| Technology | Version | Purpose             |
| ---------- | ------- | ------------------- |
| Axios      | 1.13.2  | HTTP client library |

### Development Tools

| Technology | Version | Purpose            |
| ---------- | ------- | ------------------ |
| ESLint     | Latest  | Code linting       |
| Prettier   | Latest  | Code formatting    |
| PostCSS    | Latest  | CSS post-processor |

### Testing

| Technology        | Version | Purpose           |
| ----------------- | ------- | ----------------- |
| Vitest            | 4.0.17  | Unit testing      |
| React Testing Lib | 16.3.1  | Component testing |
| jsdom             | Latest  | DOM simulation    |

### Type Safety

| Technology | Version | Purpose       |
| ---------- | ------- | ------------- |
| TypeScript | 5.9.3   | Type checking |

---

##  DevOps & Infrastructure

### Containerization

| Technology     | Version | Purpose                       |
| -------------- | ------- | ----------------------------- |
| Docker         | 20.10+  | Container platform            |
| Docker Compose | 2.0+    | Multi-container orchestration |

### Docker Images

| Image           | Version   | Purpose              |
| --------------- | --------- | -------------------- |
| node            | 22-alpine | Frontend build stage |
| nginx           | alpine    | Frontend web server  |
| gradle          | 8.7-jdk21 | Backend build stage  |
| eclipse-temurin | 21-jre    | Backend runtime      |
| alpine          | latest    | Minimal base images  |

---

##  AI/LLM Integration

### Supported LLM Services

| Service   | Type  | Purpose                   |
| --------- | ----- | ------------------------- |
| Ollama    | Local | Open-source LLM inference |
| LM Studio | Local | User-friendly LLM desktop |
| OpenAI    | Cloud | GPT-4 and other models    |

### API Integration

| Technology  | Purpose                   |
| ----------- | ------------------------- |
| REST API    | Communication with LLM    |
| OpenAI API  | Standard API format       |
| HTTP Client | Request/response handling |

---

##  Database (Future)

| Technology | Version | Purpose           |
| ---------- | ------- | ----------------- |
| PostgreSQL | 17+     | Production DB     |
| MySQL      | 8.0+    | Alternative DB    |
| H2         | Latest  | In-memory testing |
| MongoDB    | 6+      | NoSQL option      |

---

##  Cloud & Deployment (Future)

| Platform     | Purpose                       |
| ------------ | ----------------------------- |
| AWS          | EC2, RDS, S3, CloudFront      |
| Azure        | App Service, Cosmos DB        |
| Google Cloud | Compute Engine, Cloud Storage |
| Kubernetes   | Container orchestration       |
| Helm         | Kubernetes package manager    |

---

##  Monitoring & Observability (Future)

| Technology | Purpose                     |
| ---------- | --------------------------- |
| Prometheus | Metrics collection          |
| Grafana    | Metrics visualization       |
| ELK Stack  | Logging and analysis        |
| Jaeger     | Distributed tracing         |
| DataDog    | Full observability platform |

---

##  Security (Current & Future)

| Technology      | Purpose                      |
| --------------- | ---------------------------- |
| Spring Security | Authentication/Authorization |
| JWT             | Token-based authentication   |
| OAuth 2.0       | Third-party auth             |
| SSL/TLS         | HTTPS encryption             |
| Vault           | Secrets management           |

---

##  Documentation & Communication

| Technology      | Purpose              |
| --------------- | -------------------- |
| Markdown        | Documentation format |
| Swagger/OpenAPI | API documentation    |
| GitHub          | Version control      |
| Docker Hub      | Image registry       |

---

##  Testing & Quality

| Category      | Technologies                   |
| ------------- | ------------------------------ |
| Unit Testing  | JUnit 5, Vitest                |
| Integration   | Spring Test, React Testing Lib |
| Code Coverage | JaCoCo (Java), Istanbul (JS)   |
| Code Quality  | Checkstyle, ESLint, Prettier   |
| Performance   | JMH (Java), Lighthouse (Web)   |
| Load Testing  | k6, Gatling, Apache JMeter     |

---

##  Development Tools

| Tool            | Purpose                |
| --------------- | ---------------------- |
| Git             | Version control        |
| GitHub          | Repository hosting     |
| IntelliJ IDEA   | Java IDE (recommended) |
| VS Code         | Code editor            |
| Bruno           | API testing            |
| Chrome DevTools | Browser debugging      |
| Docker Desktop  | Local containerization |

---

##  Version Compatibility Matrix

### Java Backend

```
java-resumes
 Spring Boot 3.5.1
    Java 21 LTS (required)
    Gradle 8.7
    JDK 21
 Dependencies
    Gson 2.13.1 (JSON)
    Flying Saucer 9.1.22 (PDF)
    CommonMark 0.24.0 (Markdown)
    jsoup 1.15.4 (HTML parsing)
 Testing
     JUnit 5 (latest)
     Spring Test 3.5.1
```

### React Frontend

```
java-resumes
 React 19.2.0
    TypeScript 5.9.3
    Node.js 22 LTS
    npm 10+
 Build Tools
    Vite 7.2.4
    esbuild (latest)
 UI Components
    PrimeReact 10.9.7
    PrimeIcons 7.0.0
    Tailwind CSS 4.1.18
 Testing
     Vitest 4.0.17
     React Testing Lib 16.3.1
```

### Infrastructure

```
Docker Compose
 Frontend Container
    Build: node:22-alpine
    Runtime: nginx:alpine
 Backend Container
    Build: gradle:8.7-jdk21
    Runtime: eclipse-temurin:21-jre
 Network: resume-app-network
```

---

##  Dependency Tree Highlights

### Backend Critical Path

```
Spring Boot 3.5.1
 spring-boot-starter-web
    spring-webmvc
    tomcat-embed-core
 spring-boot-starter-json
    jackson (JSON processing)
 spring-boot-starter-logging
    logback
    slf4j
 Flying Saucer 9.1.22
     xhtmlrenderer
     itext (PDF generation)
```

### Frontend Critical Path

```
React 19.2.0
 react-dom
 react-router-dom
 typescript
 @types/react
 @types/node
 primereact
    primeicons
 tailwindcss
    postcss
 axios
 vite
     esbuild
```

---

##  Performance Metrics by Technology

### Backend Performance

| Technology            | Metric                |
| --------------------- | --------------------- |
| Spring Boot 3.5.1     | ~1-2s startup time    |
| Gradle 8.7            | ~10-15s build time    |
| Gson JSON parsing     | <1ms for typical JSON |
| Flying Saucer PDF gen | 2-5s per document     |

### Frontend Performance

| Technology      | Metric                |
| --------------- | --------------------- |
| Vite build      | ~5-10s for production |
| React rendering | ~16ms (60fps target)  |
| Bundle size     | ~150-200KB (gzipped)  |
| Load time       | <2s on 4G connection  |

---

##  Security Considerations by Technology

| Technology  | Security Feature          |
| ----------- | ------------------------- |
| Spring Boot | Input validation, auth    |
| TypeScript  | Type safety prevents bugs |
| Docker      | Isolated containers       |
| HTTPS/TLS   | Encrypted communication   |
| Checkstyle  | Code quality enforcement  |

---

##  Upgrade Path

### Next Version Recommendations

- **Spring Boot**: 3.5.1  3.6.x (minor upgrade)
- **Java**: 21 LTS  23 (current non-LTS)
- **React**: 19.2.0  20.x (major upgrade, breaking)
- **Node.js**: 22 LTS  24 LTS (minor upgrade)
- **TypeScript**: 5.9.3  5.10+ (patch upgrade)

### Long-term Technology Evolution

- **Database**: Add PostgreSQL for data persistence
- **Caching**: Redis for response caching
- **Message Queue**: RabbitMQ for async processing
- **Kubernetes**: Move from Docker Compose
- **GraphQL**: Add GraphQL API alongside REST

---

**See also:**

- [Architecture](ARCHITECTURE.md) - How technologies fit together
- [Development Setup](DEVELOPMENT_SETUP.md) - Installation instructions
- [Production Deployment](PRODUCTION_DEPLOYMENT.md) - Production stack

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
