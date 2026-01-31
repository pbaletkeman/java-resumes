# GitHub PRD: Expand Interview & Networking Prompts Feature

**Status:** Ready for Implementation
**Date Created:** January 30, 2026
**Epic:** Prompt Expansion & Database History Tracking
**Priority:** Medium
**T-Shirt Size:** Large (2-3 week sprint)

---

## 1. Executive Summary

This PRD outlines the implementation of expanded prompt functionality for the java-resumes application. The feature will:

- Convert 6 interview/networking prompts from `prompts/more-prompts.md` into production-grade system prompts
- Create dedicated REST endpoints for each prompt type
- Integrate with LLM service (Ollama/LM Studio)
- Add SQLite database for prompt history and audit trail
- Enhance frontend UI with prompt selection, preset filters, and history management

**Goal**: Enable users to generate interview preparation materials, cold call emails, thank-you notes, and other networking content with full traceability and downloadable expanded prompts.

---

## 2. Background & Context

### Current State

The java-resumes application currently supports:

- Resume optimization via `RESUME.md` prompt
- Cover letter generation via `COVER.md` prompt
- Skills/certifications via `SKILLS.md` prompt

### Gap Analysis

The `prompts/more-prompts.md` file contains 6 additional high-value prompts:

1. **General HR Questions** - 5 common interview questions
2. **Job-Specific Interview Questions** (from job description)
3. **Reverse Interview Questions** (candidate asks employer)
4. **Cold Call Emails** - 5 variations to target companies
5. **Cold Call LinkedIn Messages** - 5 variations for recruiters
6. **Thank You Emails** - 5 variations post-interview

These prompts lack:

- Formal production-grade expansion with frameworks
- Backend REST endpoints
- Database persistence and audit trail
- Frontend UI integration
- Download capability for expanded prompts

### Target Users

- Job seekers preparing for interviews
- Career changers researching target companies
- Professionals networking and cold outreach
- Users wanting to understand what was sent to LLM

---

## 3. Functional Requirements

### 3.1 Backend Implementation (Java/Spring Boot)

#### Branch: `new-prompts`

**Create New Prompt Files** (in `src/main/resources/prompts/`)

Each prompt should follow the pattern of existing prompts (RESUME.md, COVER.md, SKILLS.md):

1. **`interview-hr-questions.md`** - General HR interview questions generator
   - Extracts 3-5 common questions
   - Categorizes by type (behavioral, technical, situational)
   - Provides preparation strategies

2. **`interview-job-specific.md`** - Job-specific interview questions
   - Analyzes job description for role-specific questions
   - Links questions to job requirements
   - Includes STAR method examples

3. **`interview-reverse.md`** - Reverse interview questions
   - Questions candidate should ask employer
   - Signals to assess culture fit
   - Red flags to identify
   - Company research framework

4. **`cold-email.md`** - Cold outreach email generator
   - 5 distinct email templates
   - Research-based personalization framework
   - Call-to-action variations
   - Response rate optimization

5. **`cold-linkedin-message.md`** - LinkedIn cold message generator
   - 5 variations optimized for platform
   - Character limit awareness
   - Relationship building framework
   - Industry context integration

6. **`thank-you-email.md`** - Post-interview thank you email
   - 5 variations by interview type
   - Reinforce key messages from interview
   - Advance next steps
   - Professional but personable tone

#### New REST Endpoints

Add to `ResumeController.java`:

```java
POST   /api/generate/interview-hr-questions      // Generate 5 general HR questions
POST   /api/generate/interview-job-specific      // Generate job-specific questions
POST   /api/generate/interview-reverse           // Generate reverse interview questions
POST   /api/generate/cold-email                  // Generate 5 cold email variations
POST   /api/generate/cold-linkedin-message       // Generate 5 LinkedIn message variations
POST   /api/generate/thank-you-email             // Generate 5 thank-you email variations
```

#### Request/Response Format

**Request Body** (all endpoints):

```json
{
  "promptType": "interview-hr-questions", // Required
  "jobDescription": "string", // Required (unless prompt type is general)
  "company": "string", // Optional, for context
  "jobTitle": "string", // Optional, for context
  "interviewerName": "string", // Optional, for thank-you emails
  "temperature": 0.7, // Optional, 0.0-1.0 (default: 0.7)
  "model": "mistral" // Optional (uses config default)
}
```

**Response Body** (202 Accepted):

```json
{
  "message": "generating",
  "promptType": "interview-hr-questions",
  "requestId": "uuid-1234",
  "timestamp": "2026-01-30T10:30:00Z"
}
```

**File Output Pattern**:

```plaintext
uploads/
├── interview-hr-questions-2026-01-30-103000.md
├── interview-hr-questions-2026-01-30-103000.json       (expanded prompt)
├── interview-job-specific-2026-01-30-103015.md
├── interview-job-specific-2026-01-30-103015.json
└── ... (similar for other prompt types)
```

#### Database Schema

**Default Database**: SQLite (local file-based)
**Optional Database**: PostgreSQL (multi-user/cloud)

Create new `PromptHistory` entity and table (same schema for both):

```sql
-- SQLite (Default)
CREATE TABLE prompt_history (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  request_id TEXT UNIQUE NOT NULL,
  prompt_type TEXT NOT NULL,              -- enum: interview-hr, interview-job, etc.
  job_description TEXT,
  company TEXT,
  job_title TEXT,
  interviewer_name TEXT,
  temperature REAL,
  model TEXT,
  expanded_prompt_json TEXT,              -- Full prompt sent to LLM
  generated_content TEXT,                 -- LLM response/output
  generated_file_path TEXT,               -- Path to markdown file
  output_format TEXT,                     -- 'markdown', 'json', etc.
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  file_size_bytes INTEGER,
  llm_response_time_ms INTEGER,
  token_usage_estimate INTEGER,
  status TEXT DEFAULT 'completed',        -- 'pending', 'completed', 'failed'
  error_message TEXT
);

-- PostgreSQL (Optional v2+)
CREATE TABLE prompt_history (
  id SERIAL PRIMARY KEY,
  request_id TEXT UNIQUE NOT NULL,
  prompt_type TEXT NOT NULL,
  job_description TEXT,
  company TEXT,
  job_title TEXT,
  interviewer_name TEXT,
  temperature DECIMAL(3,2),
  model TEXT,
  expanded_prompt_json TEXT,
  generated_content TEXT,
  generated_file_path TEXT,
  output_format TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  file_size_bytes INTEGER,
  llm_response_time_ms INTEGER,
  token_usage_estimate INTEGER,
  status TEXT DEFAULT 'completed',
  error_message TEXT
);

-- Create indexes for both databases
CREATE INDEX idx_prompt_type ON prompt_history(prompt_type);
CREATE INDEX idx_created_at ON prompt_history(created_at);
CREATE INDEX idx_request_id ON prompt_history(request_id);
```

**Database Configuration**:

```properties
# application.properties (SQLite - Default)
spring.datasource.url=jdbc:sqlite:./data/prompts.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.locations=classpath:db/migration/sqlite

# application-postgres.yml (PostgreSQL - Optional, activate with: SPRING_PROFILES_ACTIVE=postgres)
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/prompts
    driver-class-name: org.postgresql.Driver
    username: ${DB_USER:prompts_user}
    password: ${DB_PASSWORD:}
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: validate
  flyway:
    locations: classpath:db/migration/postgres
```

**Supported Databases**:

| Database       | v1 (Default) | v2+ (Optional) | Use Case                     |
| -------------- | ------------ | -------------- | ---------------------------- |
| **SQLite**     | ✅ Yes       | ✅ Supported   | Single-user, local, portable |
| **PostgreSQL** | ❌ No        | ✅ Yes         | Multi-user, cloud, scalable  |

**Create Spring Data JPA Repository**:

```java
public interface PromptHistoryRepository extends JpaRepository<PromptHistory, Long> {
  List<PromptHistory> findByPromptType(String promptType);
  List<PromptHistory> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
  Optional<PromptHistory> findByRequestId(String requestId);
}
```

**Create PromptHistory Entity**:

```java
@Entity
@Table(name = "prompt_history")
public class PromptHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String requestId;

  private String promptType;
  private String jobDescription;
  private String company;
  private String jobTitle;
  private String interviewerName;
  private Double temperature;
  private String model;

  @Lob
  private String expandedPromptJson;

  @Lob
  private String generatedContent;

  private String generatedFilePath;
  private String outputFormat;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Integer fileSizeBytes;
  private Integer llmResponseTimeMs;
  private Integer tokenUsageEstimate;
  private String status;
  private String errorMessage;

  // Getters, setters, equals, hashCode
}
```

#### New Service: PromptService Enhancement

Update `src/main/java/ca/letkeman/resumes/service/PromptService.java`:

```java
public class PromptService {

  public String expandPrompt(String promptType, Map<String, String> context) {
    // Load prompt template from resources
    // Substitute context variables
    // Return fully expanded prompt
  }

  public PromptHistory savePromptToHistory(
      String promptType,
      String expandedPrompt,
      String generatedContent,
      String filePath,
      Integer responseTime) {
    // Save to database via repository
    // Return PromptHistory entity with ID
  }

  public List<PromptHistory> getAllHistory() {
    // Return all prompt history
  }

  public List<PromptHistory> getHistoryByType(String promptType) {
    // Filter by prompt type
  }

  public PromptHistory getHistoryById(Long id) {
    // Retrieve single record
  }

  public void deleteHistoryById(Long id) {
    // Delete single record
  }

  public byte[] downloadExpandedPrompt(Long historyId) {
    // Return expanded prompt JSON as downloadable file
  }
}
```

#### Update BackgroundResume

Enhance `src/main/java/ca/letkeman/resumes/BackgroundResume.java` to:

1. Handle new prompt types
2. Call appropriate LLM endpoint based on promptType
3. Save to database via PromptService
4. Track request timing and token usage

#### Dependencies to Add (build.gradle)

**Core JPA/ORM** (Required):

```gradle
// JPA/ORM
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

// Database migrations
implementation 'org.flywaydb:flyway-core:9.22.3'
```

**SQLite Support** (Default v1):

```gradle
// SQLite
implementation 'org.xerial:sqlite-jdbc:3.45.0'
implementation 'org.hibernate.orm:hibernate-community-dialects:6.4.0'
```

**PostgreSQL Support** (Optional, Recommended v2+):

```gradle
// PostgreSQL driver
runtimeOnly 'org.postgresql:postgresql:42.7.1'

// Connection pooling for PostgreSQL
implementation 'com.zaxxer:HikariCP:5.1.0'
```

**UUID generation**:

```gradle
implementation 'com.google.code.simple-xml:simple-xml:2.7.1'
```

#### Configuration Updates (application.yml)

**SQLite (Default - v1)** (Single-user, file-based):

```yaml
spring:
  datasource:
    url: jdbc:sqlite:./data/resumes.db
    driver-class-name: org.sqlite.JDBC
  jpa:
    database-platform: org.hibernate.community.dialect.SQLiteDialect
    hibernate:
      ddl-auto: validate # Use Flyway for migrations
    show-sql: false
  flyway:
    locations: classpath:db/migration/sqlite
    enabled: true

upload:
  path: ./uploads
  history-path: ./uploads/history
```

**PostgreSQL (Optional - v2+)** (Multi-user, cloud-ready):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:resumes_db}
    username: ${DB_USER:postgres}
    password: ${DB_PASSWORD:password}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: validate # Use Flyway for migrations
    show-sql: false
  flyway:
    locations: classpath:db/migration/postgresql
    enabled: true

upload:
  path: ${UPLOAD_PATH:/var/lib/resumes/uploads}
  history-path: ${UPLOAD_PATH:/var/lib/resumes/uploads}/history
```

**Database Selection**:

- Add environment variable: `DB_TYPE=sqlite` or `DB_TYPE=postgresql`
- Development: SQLite (default, no setup required)
- Production: PostgreSQL recommended (scalable, multi-user)
- Migration path: SQLite → PostgreSQL supported via Flyway

---

### 3.2 Frontend Implementation (React/TypeScript)

#### New Components

**1. PromptSelector Component** (`frontend/src/components/PromptSelector.tsx`)

- Dropdown/button grid showing 6 new prompt types + existing 3
- Visual icons for each prompt type
- Description tooltip on hover
- Color-coded categories (Interview, Networking, Job Search)

**2. PromptForm Component** (`frontend/src/components/PromptForm.tsx`)

- Dynamic form fields based on selected prompt type
- Common fields: jobDescription, company, jobTitle
- Conditional fields: interviewerName (for thank-you), temperature, model
- File upload integration
- Form validation

**3. PromptResultsTab Component** (`frontend/src/components/PromptResultsTab.tsx`)

- Display generated content
- Copy-to-clipboard button
- Download expanded prompt (JSON)
- Download generated content (Markdown/PDF)
- Share button (LinkedIn, Email)

**4. PromptHistoryTab Component** (`frontend/src/components/PromptHistoryTab.tsx`)

- Table with columns: ID, Type, Company, Date, Status, Actions
- Filter buttons by prompt type
- Search by company/date range
- Pagination (10/25/50 items per page)
- CRUD Operations:
  - **Create**: Shown in main form (already captured)
  - **Read**: Display historical records with expandable details
  - **Update**: Re-generate with same parameters
  - **Delete**: Remove from history (with confirmation)
- Sort by date (ascending/descending)
- Download expanded prompt from history
- Re-use prompt parameters for new generation

**5. PresetFiltersTab Component** (`frontend/src/components/PresetFiltersTab.tsx`)

- Configurable filters based on generated prompts
- Save filter presets
- Apply presets to new generations
- Examples:
  - FAANG Company Focus: Pre-fill company names
  - Technical Interview Focus: Select job-specific + reverse interview
  - Cold Outreach Campaign: Select cold-email + linkedin-message combo
  - Interview Prep: All 3 interview question types

#### UI Layout Updates

**MainContentTab.tsx** - Add tab navigation:

```plaintext
Tabs:
  1. Generate (existing Resume/Cover/Skills + New 6 prompts)
  2. Results (show generated content)
  3. History (SQLite CRUD interface)
  4. Preset Filters (save/load filter combinations)
```

#### API Integration (hooks/useApi.ts)

```typescript
// New hook for prompt operations
export const usePromptApi = () => {
  const generatePrompt = async (
    promptType: string,
    formData: PromptRequest,
  ): Promise<PromptResponse> => {
    return execute(async () => {
      const response = await fileService.generatePrompt(promptType, formData);
      return response;
    });
  };

  const fetchHistoryByType = async (promptType: string) => {
    return execute(async () => {
      return await fileService.getPromptHistory(promptType);
    });
  };

  const downloadExpandedPrompt = async (historyId: number) => {
    return fileService.downloadExpandedPrompt(historyId);
  };

  const deleteHistoryRecord = async (historyId: number) => {
    return execute(async () => {
      return await fileService.deletePromptHistory(historyId);
    });
  };

  return {
    generatePrompt,
    fetchHistoryByType,
    downloadExpandedPrompt,
    deleteHistoryRecord,
    loading,
    error,
  };
};
```

#### Services Update (services/fileService.ts)

Add methods:

```typescript
generatePrompt(promptType: string, formData: PromptRequest): Promise<PromptResponse>
getPromptHistory(promptType?: string): Promise<PromptHistory[]>
getPromptHistoryById(id: number): Promise<PromptHistory>
deletePromptHistory(id: number): Promise<void>
downloadExpandedPrompt(historyId: number): Promise<Blob>
savePresetFilter(filterName: string, filterConfig: FilterConfig): Promise<void>
getPresetFilters(): Promise<PresetFilter[]>
deletePresetFilter(filterId: string): Promise<void>
```

#### TypeScript Types (types/index.ts)

```typescript
interface PromptRequest {
  promptType: PromptType;
  jobDescription?: string;
  company?: string;
  jobTitle?: string;
  interviewerName?: string;
  temperature?: number;
  model?: string;
}

interface PromptResponse {
  message: string;
  promptType: PromptType;
  requestId: string;
  timestamp: string;
}

interface PromptHistory {
  id: number;
  requestId: string;
  promptType: PromptType;
  jobDescription?: string;
  company?: string;
  jobTitle?: string;
  interviewerName?: string;
  generatedContent: string;
  expandedPromptJson: string;
  generatedFilePath: string;
  createdAt: string;
  llmResponseTimeMs: number;
  status: "completed" | "pending" | "failed";
  errorMessage?: string;
}

interface PresetFilter {
  id: string;
  name: string;
  description: string;
  promptTypes: PromptType[];
  defaultContext: Partial<PromptRequest>;
  createdAt: string;
}

type PromptType =
  | "resume"
  | "cover-letter"
  | "skills"
  | "interview-hr-questions"
  | "interview-job-specific"
  | "interview-reverse"
  | "cold-email"
  | "cold-linkedin-message"
  | "thank-you-email";

interface FilterConfig {
  companies?: string[];
  jobTitles?: string[];
  promptTypes: PromptType[];
  autoFillFields?: Record<string, string>;
  description?: string;
}
```

---

## 4. Technical Specifications

### 4.1 REST API Specifications

#### Endpoint: POST /api/generate/{promptType}

**Path Parameters:**

- `promptType`: One of the 9 types (resume, cover-letter, skills, interview-hr-questions, etc.)

**Request Headers:**

```plaintext
Content-Type: application/json
```

**Request Body:**

```json
{
  "jobDescription": "Senior Java Developer...",
  "company": "Google",
  "jobTitle": "Senior Software Engineer",
  "interviewerName": "John Smith", // optional, for thank-you emails
  "temperature": 0.7,
  "model": "mistral"
}
```

**Response (202 Accepted):**

```json
{
  "message": "generating",
  "promptType": "interview-job-specific",
  "requestId": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2026-01-30T10:30:00Z"
}
```

**Error Response (400 Bad Request):**

```json
{
  "error": "Invalid prompt type or missing required fields",
  "details": "jobDescription is required for interview-job-specific"
}
```

#### Endpoint: GET /api/history

**Query Parameters:**

- `promptType` (optional): Filter by type
- `page` (optional, default: 0): Pagination
- `size` (optional, default: 10): Items per page
- `sortBy` (optional, default: createdAt): Sort field
- `order` (optional, default: DESC): ASC or DESC

**Response (200 OK):**

```json
{
  "data": [
    {
      "id": 1,
      "requestId": "550e8400-e29b-41d4-a716-446655440000",
      "promptType": "interview-job-specific",
      "company": "Google",
      "jobTitle": "Senior Software Engineer",
      "createdAt": "2026-01-30T10:30:00Z",
      "status": "completed",
      "llmResponseTimeMs": 2500,
      "fileSizeBytes": 4096
    }
  ],
  "pagination": {
    "page": 0,
    "totalItems": 25,
    "totalPages": 3
  }
}
```

#### Endpoint: GET /api/history/{id}

**Response (200 OK):**

```json
{
  "id": 1,
  "requestId": "550e8400-e29b-41d4-a716-446655440000",
  "promptType": "interview-job-specific",
  "company": "Google",
  "jobTitle": "Senior Software Engineer",
  "jobDescription": "Senior Java Developer...",
  "generatedContent": "1. How would you design...\n2. Tell me about...",
  "expandedPromptJson": "{\"prompt\": \"You are an expert...\"}",
  "createdAt": "2026-01-30T10:30:00Z",
  "status": "completed",
  "llmResponseTimeMs": 2500,
  "tokenUsageEstimate": 1200
}
```

#### Endpoint: DELETE /api/history/{id}

**Response (204 No Content):**

```plaintext
(empty body, success)
```

#### Endpoint: GET /api/history/{id}/download-prompt

**Response (200 OK):**

```plaintext
Content-Type: application/json
Content-Disposition: attachment; filename="interview-job-specific-550e8400.json"
(JSON expanded prompt)
```

#### Endpoint: GET /api/filters

**Response (200 OK):**

```json
{
  "data": [
    {
      "id": "faang-focus-001",
      "name": "FAANG Company Focus",
      "description": "Pre-configured for interviewing at major tech companies",
      "promptTypes": [
        "interview-hr-questions",
        "interview-job-specific",
        "interview-reverse"
      ],
      "defaultContext": {
        "companies": ["Google", "Apple", "Amazon", "Netflix", "Meta"],
        "temperature": 0.6
      },
      "createdAt": "2026-01-30T08:00:00Z"
    }
  ]
}
```

#### Endpoint: POST /api/filters

**Request Body:**

```json
{
  "name": "Startup Tech Founder",
  "description": "For interviews at early-stage tech companies",
  "promptTypes": ["cold-email", "cold-linkedin-message", "thank-you-email"],
  "defaultContext": {
    "temperature": 0.8,
    "company": "Early-stage startup"
  }
}
```

**Response (201 Created):**

```json
{
  "id": "startup-founder-001",
  "name": "Startup Tech Founder",
  "createdAt": "2026-01-30T10:35:00Z"
}
```

---

### 4.2 Data Flow Diagrams

#### User Generation Flow

```plaintext
User Input Form
    ↓
Frontend validates input
    ↓
POST /api/generate/{promptType}
    ↓
ResumeController.generatePrompt()
    ↓
PromptService.expandPrompt() ← Load prompt template & substitute variables
    ↓
BackgroundResume thread spawned (async)
    ↓
ApiService.invokeApi() ← Send expanded prompt to Ollama/LM Studio
    ↓
Save to database (PromptHistory)
    ↓
Save markdown + JSON files to disk
    ↓
Frontend polls GET /api/files
    ↓
Results displayed to user
```

#### History Retrieval Flow

```plaintext
User clicks "History" tab
    ↓
GET /api/history?promptType=interview-job-specific
    ↓
PromptHistoryRepository.findByPromptType()
    ↓
Return paginated results
    ↓
Display in DataTable component
    ↓
User clicks row → GET /api/history/{id}
    ↓
Display full details (expanded prompt, generated content)
    ↓
User clicks "Download" → GET /api/history/{id}/download-prompt
    ↓
Save JSON file locally
```

---

## 5. User Stories

### Story 1: Interview Preparation

**As a** job seeker preparing for an interview with Google
**I want to** generate interview questions specific to a senior engineer role
**So that** I can practice my responses and improve my interview performance

**Acceptance Criteria:**

- [ ] Select "Job-Specific Interview Questions" prompt
- [ ] Upload job description and provide company/role context
- [ ] Receive 5-7 role-specific questions within 5 seconds
- [ ] Save questions to history for future reference
- [ ] Download expanded prompt to see what was sent to LLM

---

### Story 2: Cold Outreach Campaign

**As a** career changer
**I want to** generate multiple variations of cold outreach emails and LinkedIn messages
**So that** I can contact recruiters at target companies systematically

**Acceptance Criteria:**

- [ ] Use preset filter "Cold Outreach Campaign"
- [ ] Select both "Cold Email" and "LinkedIn Message" prompts
- [ ] Generate 5 variations of each in one workflow
- [ ] Compare variations side-by-side
- [ ] Copy and customize for individual targets
- [ ] Track all generated variations in history

---

### Story 3: Prompt History Management

**As a** frequent user of the platform
**I want to** view, filter, and manage all previously generated prompts
**So that** I can reuse successful templates and track my progress

**Acceptance Criteria:**

- [ ] View all generated prompts in a table with filtering
- [ ] Filter by prompt type, company, date range
- [ ] Sort by creation date, response time, file size
- [ ] Expand any row to view full details
- [ ] Download expanded prompt (JSON) for any historical entry
- [ ] Delete entries with confirmation dialog
- [ ] Pagination with 10/25/50 items per page

---

### Story 4: Preset Filters

**As a** user targeting multiple companies
**I want to** save filter combinations (prompt types, companies, settings)
**So that** I can quickly run the same workflow for different targets

**Acceptance Criteria:**

- [ ] Create preset: name, description, selected prompts, default values
- [ ] Save preset to local storage and backend
- [ ] View all saved presets with descriptions
- [ ] Click preset to auto-populate form
- [ ] Edit preset settings
- [ ] Delete unused presets
- [ ] Share preset with team (future enhancement)

---

### Story 5: Expanded Prompt Transparency

**As a** user concerned about AI safety
**I want to** see the exact prompt that was sent to the LLM
**So that** I can verify what instructions were given and understand the AI behavior

**Acceptance Criteria:**

- [ ] Download button on all generated content
- [ ] Expanded prompt saved as JSON with all substituted variables
- [ ] File includes timestamp, model, temperature, token usage
- [ ] JSON is human-readable and formatted
- [ ] Available immediately after generation and from history

---

## 6. Implementation Roadmap

### Phase 1: Backend Foundation (Week 1)

- [ ] Create 6 new prompt files (`.md`) in `src/main/resources/prompts/`
- [ ] Set up SQLite database and JPA entities
- [ ] Create `PromptHistoryRepository` and `PromptHistory` entity
- [ ] Update `PromptService` to expand new prompt types
- [ ] Add dependencies to `build.gradle`
- [ ] Update `application.yml` with SQLite config
- [ ] Write unit tests for PromptService

**Deliverable**: Database schema created, prompts stored as resources, service layer ready

---

### Phase 2: Backend REST Endpoints (Week 1-2)

- [ ] Add 6 new endpoints to `ResumeController`
- [ ] Implement request validation
- [ ] Update `BackgroundResume` to handle new prompt types
- [ ] Integrate with `ApiService` for LLM calls
- [ ] Save results to database
- [ ] Implement GET /api/history with filtering/pagination
- [ ] Implement DELETE /api/history/{id}
- [ ] Implement GET /api/history/{id}/download-prompt
- [ ] Add integration tests

**Deliverable**: 6 new endpoints functional, history CRUD working, database populated

---

### Phase 3: Frontend Components (Week 2)

- [ ] Create `PromptSelector` component
- [ ] Create `PromptForm` component with dynamic fields
- [ ] Create `PromptResultsTab` component
- [ ] Create `PromptHistoryTab` component with table
- [ ] Update `MainContentTab` with new tabs
- [ ] Update `services/fileService.ts` with new API methods
- [ ] Add TypeScript types for new data structures
- [ ] Add CSS styling for new components

**Deliverable**: UI showing all 9 prompt types, history table functional, forms dynamic

---

### Phase 4: Advanced Features (Week 2-3)

- [ ] Implement preset filters backend (POST/GET /api/filters)
- [ ] Create `PresetFiltersTab` component
- [ ] Add local storage persistence for presets
- [ ] Implement filter auto-population in `PromptForm`
- [ ] Add download functionality for generated content
- [ ] Add copy-to-clipboard feature
- [ ] Implement error handling and user feedback
- [ ] Add loading states and animations

**Deliverable**: Full CRUD on history, preset filters working, download/copy features

---

### Phase 5: Testing & Refinement (Week 3)

- [ ] Backend integration tests (all endpoints)
- [ ] Frontend component tests (React Testing Library)
- [ ] End-to-end tests (form submission → database → results)
- [ ] Performance testing (database queries, API response times)
- [ ] User acceptance testing with sample workflows
- [ ] Bug fixes and refinements
- [ ] Documentation updates

**Deliverable**: Test coverage >80%, all bugs resolved, documentation updated

---

### Phase 6: Deployment & Monitoring (Week 3)

- [ ] Merge `new-prompts` branch to `master`
- [ ] Update deployment documentation
- [ ] Set up monitoring for new endpoints
- [ ] Create user guide and video tutorial
- [ ] Release notes preparation
- [ ] Post-deployment monitoring

**Deliverable**: Feature released, monitored, documented

---

## 7. Success Metrics

### Quantitative Metrics

- **Code Coverage**: Target >80% on new code (tests)
- **API Response Time**: <5 seconds for prompt generation (202 response)
- **Database Query Time**: <500ms for history queries
- **UI Load Time**: <2 seconds to load history tab (paginated)
- **User Adoption**: Track usage of each prompt type
- **Error Rate**: <1% for successful generations

### Qualitative Metrics

- **User Satisfaction**: Net Promoter Score (NPS) for feature
- **Code Quality**: Checkstyle compliance 100%, no SpotBugs issues
- **Documentation**: Clear, complete, with examples
- **Ease of Use**: Users can generate prompts in <3 clicks

---

## 8. Risk Mitigation

| Risk                                         | Impact | Probability | Mitigation                                       |
| -------------------------------------------- | ------ | ----------- | ------------------------------------------------ |
| SQLite performance issues with large history | Medium | Low         | Implement pagination, archive old records        |
| LLM API timeout on complex prompts           | Medium | Medium      | Add retry logic, timeout handling, user feedback |
| Database migration issues in production      | High   | Low         | Test migrations thoroughly, backup strategy      |
| UI complexity overwhelming users             | Medium | Medium      | Simplify with presets, inline help text          |
| Prompt expansion produces poor results       | High   | Medium      | Extensive prompt engineering, user feedback loop |

---

## 9. Acceptance Criteria (DoD)

### Code Quality & Best Practices

**Community Standards & Best Practices**:

- [ ] All code follows community best practices and standards
  - [ ] Java: Google Java Style Guide + Spring Boot conventions
  - [ ] TypeScript: Airbnb JavaScript/TypeScript style guide
  - [ ] SQL: Standard formatting, meaningful naming
  - [ ] Markdown: GitHub-flavored markdown standards
  - [ ] Configuration: YAML indentation (2 spaces), property naming conventions

**Complexity & Simplicity**:

- [ ] Code favors simplicity over complexity
  - [ ] Break complex operations into multiple simple steps
  - [ ] Avoid deeply nested conditionals (max 2-3 levels)
  - [ ] Keep methods focused (single responsibility principle)
  - [ ] Prefer composition over inheritance
  - [ ] No premature optimization or over-engineering

**Code Review Process**:

- [ ] Formal code review completed by 2+ team members
  - [ ] All review comments addressed before merge
  - [ ] Complexity reduced during review if necessary
  - [ ] Edge cases identified and handled
  - [ ] Performance implications reviewed

**Quality Metrics**:

- [ ] All code passes checkstyle checks (100% compliance)
- [ ] No SpotBugs issues flagged
- [ ] All new code has unit tests (>80% coverage)
- [ ] TypeScript strict mode enabled, no `any` types
- [ ] Cyclomatic complexity <10 per method
- [ ] Maximum method length: 50 lines
- [ ] No code duplication (DRY principle)

### Testing

- [ ] All 6 new prompts tested with LLM service
- [ ] All CRUD operations verified in database
- [ ] History filtering and pagination tested
- [ ] UI components render correctly in Chrome/Firefox/Safari
- [ ] End-to-end flow tested from form → results → download

### Documentation

- [ ] README updated with new features
- [ ] API documentation complete (Swagger/OpenAPI)
- [ ] Database schema documented
- [ ] User guide created with screenshots
- [ ] Code comments added to complex logic
- [ ] All documentation files are up to data and follow the `markdown.instructions.md` instructions

### Performance

- [ ] API endpoints respond in <5 seconds (202 response)
- [ ] History queries complete in <500ms
- [ ] Frontend loads history tab in <2 seconds
- [ ] No memory leaks detected
- [ ] Database size manageable (<1GB with 1000+ records)

---

## 10. Dependencies & Constraints

### External Dependencies

- **Ollama/LM Studio**: Must be running for LLM calls
- **SQLite JDBC Driver**: Version 3.45.0+
- **Hibernate ORM**: Version 6.4.0+
- **Spring Data JPA**: Included with Spring Boot 3.5.1

### Internal Dependencies

- Existing `ApiService` for LLM communication
- Existing `FilesStorageService` for file operations
- Existing `ResumeController` for HTTP routing
- React 19+ frontend infrastructure

### Constraints

- **Database**:
  - v1: SQLite (default, file-based, zero setup)
  - v2+: PostgreSQL optional (multi-user, cloud deployment)
  - Both: Support backward compatibility during migration
- **Backward Compatibility**: Existing endpoints must continue working
- **Performance**: LLM calls are async (non-blocking)
- **Storage**: Uploads directory must have sufficient disk space
- **Migration**: Flyway handles schema versioning for both databases

---

## 11. Security & Compliance

### Authentication & Authorization

**Single-User Model (v1)**:

- No authentication required for v1 (assumes single-user localhost deployment)
- All endpoints accessible without authentication headers
- Suitable for local job seeker use only

**Multi-User Model (v2 Planning)**:

- JWT token-based authentication for future multi-user support
- Role-based access control (RBAC): admin, user, viewer
- Audit logging of all CRUD operations on history
- Session timeout: 30 minutes of inactivity

### Data Privacy & Protection

**Data Classification**:

- **Sensitive**: Job descriptions, company names, personal context in prompts
- **Confidential**: LLM responses, generated content (user property)
- **Public**: Prompt templates, metadata

**Data Retention Policy**:

- Keep all history indefinitely in database
- Provide user export/download capability
- Monthly archive of old records (>6 months) to separate file
- GDPR: Provide "right to deletion" through delete endpoint
- Data minimization: Don't store unnecessary fields

**Encryption**:

- Database: SQLite database file should be encrypted at rest (future)
- Transit: Require HTTPS/TLS in production
- Sensitive fields: Consider encrypting jobDescription if multi-user

### Compliance Standards

**GDPR Compliance**:

- Right to access: Provide download of all personal data (GET /api/history with export)
- Right to deletion: DELETE /api/history/{id} supports user data removal
- Data portability: Export history as JSON/CSV
- Privacy notice: Update terms of service for data usage
- Consent: User agrees to data storage on first use

**SOC 2 (Future)**:

- Audit logging of all API calls
- Access control and authentication
- Data backup and recovery procedures
- Security monitoring and alerting

**HIPAA (Not Required)**:

- Not applicable - no healthcare data expected
- Document this exclusion

---

## 12. API Security & Rate Limiting

### Rate Limiting Strategy

**Endpoint Rate Limits**:

```
POST /api/generate/*              → 10 requests/minute per IP
GET  /api/history                 → 100 requests/minute per IP
GET  /api/history/{id}            → 100 requests/minute per IP
DELETE /api/history/{id}          → 10 requests/minute per IP
GET  /api/history/{id}/download   → 50 requests/minute per IP
POST /api/filters                 → 10 requests/minute per IP
GET  /api/filters                 → 100 requests/minute per IP
```

**Implementation**:

- Use Spring Cloud Config with Bucket4j library
- Return 429 (Too Many Requests) when limit exceeded
- Include headers: `X-RateLimit-Limit`, `X-RateLimit-Remaining`, `X-RateLimit-Reset`

**Justification**:

- Prevent API abuse/DoS attacks
- Protect LLM service from overwhelming requests
- Encourage responsible usage patterns
- Can be adjusted based on load testing

### Input Validation & Sanitization

**Request Validation**:

```java
// jobDescription
- Max length: 50,000 characters
- Required for: interview-*, cold-*, thank-you-*
- Validation: No null/empty, trim whitespace

// company
- Max length: 200 characters
- Optional
- Validation: Alphanumeric, spaces, hyphens only

// jobTitle
- Max length: 200 characters
- Optional
- Validation: Alphanumeric, spaces, hyphens only

// temperature
- Type: Double
- Range: 0.0 to 1.0 (inclusive)
- Default: 0.7
- Validation: Reject if outside range

// model
- Max length: 100 characters
- Optional
- Validation: Alphanumeric, underscores only
- Whitelist known models: mistral, llama2, gemma, etc.

// interviewerName
- Max length: 200 characters
- Optional (required for thank-you prompts)
- Validation: Alphanumeric, spaces, hyphens only
```

**Output Sanitization**:

- Strip HTML/script tags from LLM responses
- Escape special characters in JSON output
- Validate file paths before saving to disk
- No path traversal (../ sequences) allowed

### CORS & Security Headers

**CORS Configuration** (Update ResumeController):

```java
@CrossOrigin(origins = {
    "http://localhost:5173",        // Dev frontend
    "http://localhost:80",           // Prod frontend
    "http://127.0.0.1:5173"
},
allowCredentials = "false",
maxAge = 3600,
allowedHeaders = {"Content-Type", "Authorization"},
exposedHeaders = {"X-RateLimit-Limit", "X-RateLimit-Remaining"})
```

**Security Headers** (application.yml):

```yaml
server:
  servlet:
    session:
      cookie:
        http-only: true
        secure: true
        same-site: strict
```

---

## 13. Testing & QA Strategy

### Test Coverage Matrix

| Component                    | Coverage Target | Test Type        | Scenarios                                            |
| ---------------------------- | --------------- | ---------------- | ---------------------------------------------------- |
| PromptService.expandPrompt() | 90%             | Unit             | All prompt types, variable substitution, error cases |
| ResumeController endpoints   | 85%             | Integration      | Valid/invalid requests, 202 responses, error codes   |
| PromptHistoryRepository      | 95%             | Integration      | CRUD operations, filtering, pagination               |
| ApiService.invokeApi()       | 80%             | Integration      | Mock LLM responses, timeouts, connection errors      |
| Frontend components          | 75%             | Unit/Integration | Render, form validation, table CRUD, filters         |
| End-to-end flow              | 100%            | E2E              | Full generation → database → results → download      |

### Test Case Matrix

**Backend Test Cases**:

| Test ID | Description                          | Input                       | Expected                          | Type       |
| ------- | ------------------------------------ | --------------------------- | --------------------------------- | ---------- |
| T-001   | Generate interview-hr-questions      | Valid job description       | 202 Accepted, requestId returned  | Happy Path |
| T-002   | Generate with missing jobDescription | No jobDescription           | 400 Bad Request                   | Error      |
| T-003   | Generate with invalid temperature    | temperature = 1.5           | 400 Bad Request                   | Error      |
| T-004   | Rate limit exceeded                  | 11 requests in 1 minute     | 429 Too Many Requests             | Load       |
| T-005   | LLM service timeout                  | Mock 30s delay              | 504 Gateway Timeout or retry      | Error      |
| T-006   | Save to database                     | Valid generation            | Record created with all fields    | Happy Path |
| T-007   | Query history by type                | promptType = "interview-hr" | Return only matching records      | Happy Path |
| T-008   | Paginate results                     | page=0, size=10             | Return first 10 records           | Happy Path |
| T-009   | Download expanded prompt             | Valid historyId             | Return JSON file with full prompt | Happy Path |
| T-010   | Delete history record                | Valid historyId             | Record removed, 204 No Content    | Happy Path |

**Frontend Test Cases**:

| Test ID | Description              | Input                          | Expected                                | Type       |
| ------- | ------------------------ | ------------------------------ | --------------------------------------- | ---------- |
| F-001   | Select prompt type       | Click "interview-job-specific" | Form fields update to required fields   | Happy Path |
| F-002   | Submit form              | All required fields filled     | POST request sent, loading state shown  | Happy Path |
| F-003   | Display results          | After generation completes     | Content shown, download buttons enabled | Happy Path |
| F-004   | Load history tab         | Click History tab              | Table populated with paginated records  | Happy Path |
| F-005   | Filter history           | Select prompt type filter      | Table filtered to matching records      | Happy Path |
| F-006   | Sort history             | Click "Date" column header     | Records sorted ascending/descending     | Happy Path |
| F-007   | Delete record            | Click delete, confirm dialog   | Record removed from table               | Happy Path |
| F-008   | Download expanded prompt | Click download on history row  | JSON file downloaded                    | Happy Path |
| F-009   | Apply preset filter      | Select "FAANG Focus" preset    | Form auto-populated with values         | Happy Path |
| F-010   | Copy to clipboard        | Click copy button              | Content copied, confirmation shown      | Happy Path |

### Performance Benchmarks

**Target Performance**:

| Metric                           | Target      | Acceptance Threshold |
| -------------------------------- | ----------- | -------------------- |
| API response time (202 Accepted) | <500ms      | <2 seconds           |
| LLM generation time (actual)     | <10 seconds | <30 seconds          |
| Database query time              | <200ms      | <500ms               |
| Frontend component render        | <300ms      | <1 second            |
| History table load (100 records) | <1 second   | <3 seconds           |
| Download expanded prompt         | <100ms      | <500ms               |

**Benchmark Procedure**:

```bash
# Backend load test
k6 run load-test.js --vus 10 --duration 5m

# Frontend performance profiling
npm run test:perf

# Database query profiling
QUERY_LOG=true npm run dev
```

### Regression Testing

**Automated Regression Suite**:

- Run on every pull request
- Must pass before merge
- Include all existing endpoints (resume, cover-letter, skills)
- Verify no performance degradation

**Manual Regression Checklist**:

- [ ] Resume optimization still works
- [ ] Cover letter generation still works
- [ ] Skills prompt still works
- [ ] File downloads still work
- [ ] UI still responsive
- [ ] No console errors
- [ ] Database still intact

---

## 14. Database Management

### Backup & Recovery Strategy

**Backup Schedule**:

- Daily backup: 2:00 AM UTC (off-peak)
- Weekly backup: Sunday 3:00 AM UTC (compressed archive)
- Monthly backup: 1st of month (archived to cold storage)
- Retention: 30 days daily, 6 months weekly, 1 year monthly

**SQLite Backup Implementation**:

```bash
# Daily backup script (cron job)
0 2 * * * sqlite3 ./data/resumes.db ".backup ./backups/resumes-$(date +%Y%m%d-%H%M%S).db"

# Weekly compressed backup
0 3 * * 0 tar -czf ./backups/archive/resumes-$(date +%Y%m%d).tar.gz ./data/resumes.db

# Verify backup integrity
sqlite3 ./backups/resumes-*.db "PRAGMA integrity_check;"
```

**PostgreSQL Backup Implementation**:

```bash
# Daily backup script (cron job)
0 2 * * * pg_dump -Fc $DATABASE_URL > ./backups/resumes-$(date +%Y%m%d-%H%M%S).sql.gz

# Weekly backup with compression
0 3 * * 0 pg_dump -Fc $DATABASE_URL | gzip > ./backups/archive/resumes-$(date +%Y%m%d).sql.gz

# Verify backup integrity
pg_restore -l ./backups/resumes-*.sql.gz > /dev/null && echo "Backup valid"
```

**SQLite Recovery Procedure**:

1. Stop application
2. Identify backup to restore
3. `cp ./backups/resumes-[date].db ./data/resumes.db`
4. Run migrations: `./gradlew flywayMigrate`
5. Verify integrity: `sqlite3 ./data/resumes.db "PRAGMA integrity_check;"`
6. Restart application
7. Test critical workflows

**PostgreSQL Recovery Procedure**:

1. Stop application
2. Create new database: `createdb resumes_db_restore`
3. Restore backup: `pg_restore -d resumes_db_restore ./backups/resumes-[date].sql.gz`
4. Verify integrity: `psql -d resumes_db_restore -c "SELECT COUNT(*) FROM prompt_history;"`
5. Swap databases (update connection string in config)
6. Run migrations: `./gradlew flywayMigrate`
7. Restart application
8. Test critical workflows

**RPO/RTO**:

- Recovery Point Objective (RPO): 24 hours (daily backups)
- Recovery Time Objective (RTO): 15 minutes (restore + migrate + test)

### Database Migration Strategy

**Migration Tool**: Flyway (supports SQLite and PostgreSQL)

**Version Control** (Database-specific SQL):

```
db/migration/
├── sqlite/
│   ├── V1__initial_schema.sql          # SQLite-specific schema
│   ├── V2__add_prompt_history.sql      # SQLite: PromptHistory table
│   └── V3__add_indexes.sql             # SQLite indexes
├── postgresql/
│   ├── V1__initial_schema.sql          # PostgreSQL-specific schema
│   ├── V2__add_prompt_history.sql      # PostgreSQL: PromptHistory table
│   └── V3__add_indexes.sql             # PostgreSQL indexes
└── common/
    ├── V4__add_preset_filters.sql      # Shared across databases
    └── V5__update_retention_policy.sql # Shared across databases
```

**Migration Configuration**:

```yaml
spring:
  flyway:
    locations: classpath:db/migration/${spring.jpa.database-platform}
    baselineOnMigrate: true
    validateOnMigrate: true
```

**Migration Rollback**:

- Flyway supports automatic rollback on failed migration
- Keep backups before each major migration
- Test migrations in staging environment first

**Production Migration Checklist**:

- [ ] Create backup before migration
- [ ] Test migration on staging clone
- [ ] Notify users of potential downtime
- [ ] Schedule during low-traffic window
- [ ] Run migration
- [ ] Verify data integrity
- [ ] Test all critical workflows
- [ ] Rollback plan ready
- [ ] Monitor for issues post-migration

### Data Archival & Cleanup

**Archive Policy**:

```sql
-- Archive records older than 12 months
INSERT INTO prompt_history_archive
SELECT * FROM prompt_history
WHERE created_at < DATE('now', '-12 months');

DELETE FROM prompt_history
WHERE created_at < DATE('now', '-12 months');
```

**Cleanup Schedule**:

- Monthly archival process
- Run during off-peak hours
- Compress archived tables
- Export to external storage

**Retention Periods**:

- Active records: Indefinite (user manages via UI)
- Archived records: 3 years
- Deleted records: 30-day soft delete before permanent removal

### Indexing & Performance Tuning

**Recommended Indexes**:

```sql
-- Frequently queried columns
CREATE INDEX idx_prompt_type ON prompt_history(prompt_type);
CREATE INDEX idx_created_at ON prompt_history(created_at DESC);
CREATE INDEX idx_company ON prompt_history(company);
CREATE INDEX idx_request_id ON prompt_history(request_id);

-- Composite indexes for common queries
CREATE INDEX idx_type_date ON prompt_history(prompt_type, created_at DESC);
CREATE INDEX idx_company_date ON prompt_history(company, created_at DESC);

-- Status tracking
CREATE INDEX idx_status ON prompt_history(status);
```

**Query Optimization**:

- Use pagination (avoid SELECT \* without LIMIT)
- Analyze query plans: `EXPLAIN QUERY PLAN SELECT ...`
- Profile with SQLite3 built-in timer: `.timer ON`
- Monitor slow queries (>500ms)

---

## 15. Monitoring & Observability

### Logging Strategy

**Log Levels**:

```yaml
logging:
  level:
    ca.letkeman.resumes: DEBUG # Application code
    ca.letkeman.resumes.optimizer: TRACE # LLM interactions (verbose)
    org.springframework.web: INFO # HTTP requests
    org.hibernate: WARN # Database
  pattern: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/app.log
    max-size: 100MB
    max-history: 30
    total-size-cap: 5GB
```

**Log Events to Capture**:

| Event                 | Level | Fields                                 |
| --------------------- | ----- | -------------------------------------- |
| API request received  | DEBUG | method, path, params, IP               |
| Prompt expanded       | DEBUG | promptType, varCount, size             |
| LLM API called        | DEBUG | endpoint, model, temperature, tokens   |
| LLM response received | INFO  | responseTime, tokensUsed, status       |
| Record saved to DB    | DEBUG | id, promptType, size                   |
| History query         | DEBUG | filters, pageSize, resultCount         |
| Error occurred        | ERROR | errorType, message, stackTrace, userId |
| Rate limit hit        | WARN  | IP, endpoint, remainingRequests        |
| File download         | INFO  | fileId, size, IP                       |

**Structured Logging** (JSON format):

```json
{
  "timestamp": "2026-01-30T10:30:45.123Z",
  "level": "INFO",
  "logger": "ca.letkeman.resumes.optimizer.ApiService",
  "message": "LLM API call completed",
  "fields": {
    "promptType": "interview-job-specific",
    "responseTimeMs": 2500,
    "tokensUsed": 1200,
    "status": "success",
    "model": "mistral"
  }
}
```

### Monitoring & Alerts

**Metrics to Monitor**:

| Metric                    | Type         | Warning Threshold | Critical Threshold |
| ------------------------- | ------------ | ----------------- | ------------------ |
| API response time (p95)   | Latency      | >2s               | >5s                |
| Database query time (p95) | Latency      | >300ms            | >1s                |
| Error rate                | Availability | >1%               | >5%                |
| LLM timeout rate          | Availability | >2%               | >10%               |
| Database size             | Resource     | >500MB            | >1GB               |
| Disk space usage          | Resource     | >80%              | >95%               |
| Memory usage              | Resource     | >80%              | >95%               |
| CPU usage                 | Resource     | >70%              | >90%               |
| HTTP 429 (rate limit)     | Load         | >10/min           | >50/min            |
| Failed database writes    | Availability | >0                | >5                 |

**Alert Configuration** (Prometheus):

```yaml
groups:
  - name: java-resumes
    rules:
      - alert: HighAPILatency
        expr: histogram_quantile(0.95, api_request_duration_seconds) > 5
        for: 5m
        severity: critical

      - alert: HighErrorRate
        expr: rate(http_requests_total{status=~"5.."}[5m]) > 0.05
        for: 5m
        severity: warning

      - alert: DatabaseSizeWarning
        expr: sqlite_database_size_bytes > 500000000
        for: 1h
        severity: warning
```

**Alerting Channels**:

- Email: Critical and warning alerts
- Slack: Real-time notifications in #alerts channel
- PagerDuty: On-call escalation for critical issues

### Health Check Endpoints

**New Health Endpoints**:

```java
GET /api/health                    // Overall system health
GET /api/health/database           // Database connectivity
GET /api/health/llm                // LLM service connectivity
GET /api/health/disk               // Disk space availability
```

**Response Format**:

```json
{
  "status": "UP",
  "timestamp": "2026-01-30T10:30:45.123Z",
  "components": {
    "database": {
      "status": "UP",
      "details": {
        "type": "SQLite",
        "url": "jdbc:sqlite:./data/resumes.db",
        "result": "Query executed successfully"
      }
    },
    "llm": {
      "status": "UP",
      "details": {
        "endpoint": "http://localhost:11434",
        "responseTimeMs": 250
      }
    },
    "disk": {
      "status": "UP",
      "details": {
        "availableSpace": "45GB",
        "usagePercent": 55
      }
    }
  }
}
```

### Distributed Tracing (Future)

**OpenTelemetry Integration**:

- Trace request flow from controller → service → database
- Add correlation IDs to all logs
- Export traces to Jaeger or similar
- Visualize latency bottlenecks

---

## 16. Rollout & Deployment Strategy

### Phased Rollout Plan

**Phase 1: Internal Testing (Week 3)**

- Deploy to internal staging environment
- QA team comprehensive testing
- Performance benchmarking
- Security review

**Phase 2: Beta Release (Week 4)**

- Feature flag: `NEW_PROMPTS_FEATURE=false` (default)
- Deploy to production with flag disabled
- Select 10% of users to test
- Monitor metrics, collect feedback
- Fix critical issues

**Phase 3: Gradual Rollout (Week 4-5)**

- Enable for 25% of users
- Monitor error rates, latency
- If stable, increase to 50%
- If issues, rollback to 0%
- Final release to 100% users

**Phase 4: Full Production (Week 5)**

- All users have access
- Monitor for week post-launch
- Disable feature flag
- Document learnings

### Feature Flags Implementation

```java
@Component
public class FeatureToggle {
  @Value("${features.new-prompts.enabled:false}")
  private boolean newPromptsEnabled;

  public boolean isNewPromptsEnabled() {
    return newPromptsEnabled;
  }
}

// In controller
@PostMapping("/api/generate/{promptType}")
public ResponseEntity<?> generatePrompt(@PathVariable String promptType, ...) {
  if (!featureToggle.isNewPromptsEnabled() && !isSupportedLegacyPrompt(promptType)) {
    return ResponseEntity.status(HttpStatus.GONE)
        .body(new ResponseMessage("Feature not available"));
  }
  // ... implementation
}
```

### Database Migration in Production

**Zero-Downtime Migration Strategy**:

1. **Pre-migration** (off-peak window):
   - Create backup
   - Run Flyway migrations on staging
   - Test all queries
   - Measure migration time

2. **Migration window** (10 PM - 6 AM):
   - Notify users: "System maintenance 10-11 PM"
   - Stop accepting new requests (queue them)
   - Run Flyway migrations
   - Run verification queries
   - Monitor for errors

3. **Post-migration**:
   - Resume request processing
   - Monitor error rates (>1% triggers rollback)
   - Keep rollback script ready for 24 hours
   - Send completion notification

### Rollback Procedures

**Automatic Rollback Triggers**:

- Error rate >5% for 5 minutes → Rollback feature flag
- Database unavailable → Restore from backup
- API latency >10s p95 → Disable new endpoints

**Manual Rollback Steps**:

```bash
# 1. Revert feature flag
echo "features.new-prompts.enabled=false" >> application.properties
./gradlew bootRun

# 2. Revert code changes
git revert HEAD~1
./gradlew clean build

# 3. Restore database (if needed)
cp ./backups/resumes-[pre-migration-date].db ./data/resumes.db
sqlite3 ./data/resumes.db "PRAGMA integrity_check;"

# 4. Verify system health
curl http://localhost:8080/api/health

# 5. Communicate to users
# Notify on UI: "We rolled back the new features. Please refresh."
```

**Rollback Time**: <15 minutes

### Deployment Runbook

**Pre-deployment**:

- [ ] All tests passing (backend >80%, frontend >75%)
- [ ] Code review approved by 2+ reviewers
- [ ] Database migration tested on staging
- [ ] Performance benchmarks reviewed
- [ ] Monitoring configured and tested
- [ ] Rollback plan documented and tested
- [ ] Team communication sent

**Deployment**:

- [ ] Stop accepting new requests (feature flag = paused)
- [ ] Backup database
- [ ] Deploy new code (gradual rollout)
- [ ] Run database migrations
- [ ] Verify health checks
- [ ] Enable feature flag (0% → 10% users)
- [ ] Monitor metrics for 30 minutes
- [ ] If stable, increase to 25% → 50% → 100%

**Post-deployment**:

- [ ] Monitor error rates for 24 hours
- [ ] Collect user feedback
- [ ] Document any issues
- [ ] Schedule retrospective
- [ ] Keep rollback plan active for 48 hours

---

## 17. Localization & Accessibility

### Internationalization (i18n)

**Supported Languages (v2+)**:

- English (en-US) - default
- Spanish (es-ES)
- French (fr-FR)
- German (de-DE)
- Chinese (zh-CN)

**Backend i18n**:

```yaml
spring:
  messages:
    basename: i18n/messages
    encoding: UTF-8
    fallback-to-system-locale: false
```

**File Structure**:

```
src/main/resources/i18n/
├── messages_en.properties
├── messages_es.properties
├── messages_fr.properties
└── messages_de.properties
```

**Example Properties File**:

```properties
prompt.interview-hr-questions.title=General HR Questions
prompt.interview-hr-questions.description=Common interview questions and preparation strategies
api.error.invalid-prompt-type=Invalid prompt type. Please select from available options.
api.error.missing-job-description=Job description is required for this prompt type.
```

**Frontend i18n**:

```typescript
// i18n/en.json
{
  "prompt.selector.title": "Select Prompt Type",
  "prompt.form.company": "Company Name",
  "prompt.results.download": "Download",
  "history.table.noData": "No history records found"
}

// Hook usage
import { useTranslation } from 'react-i18next';

const Component = () => {
  const { t } = useTranslation();
  return <h1>{t('prompt.selector.title')}</h1>;
};
```

### Accessibility (WCAG 2.1 Level AA)

**Required Accessibility Features**:

1. **Keyboard Navigation**:
   - Tab order logical and consistent
   - All interactive elements keyboard accessible
   - Escape key to close modals
   - Enter key to submit forms

2. **Screen Reader Support**:
   - Add ARIA labels to all buttons: `aria-label="Generate prompt"`
   - Form labels associated with inputs: `<label htmlFor="company">`
   - Table headers marked with `scope="col"`
   - Status messages announced: `role="status" aria-live="polite"`

3. **Color & Contrast**:
   - Minimum contrast ratio 4.5:1 for text
   - Color not sole means of information
   - Test with WebAIM contrast checker

4. **Focus Indicators**:
   - Visible focus outline on all interactive elements
   - Minimum 2px width, 2px offset
   - Example: `outline: 2px solid #0066cc; outline-offset: 2px;`

5. **Form Validation**:
   - Error messages clearly associated with form fields
   - Error summary at top of form
   - Required fields marked clearly

6. **Dynamic Content**:
   - Loading states announced to screen readers
   - Results loading spinner has `aria-busy="true"`
   - New content announced with `aria-live="polite"`

**Accessibility Testing**:

```bash
# Automated testing with axe-core
npm install --save-dev @axe-core/react

# Manual testing with screen reader
# Windows: NVDA (free)
# Mac: VoiceOver (built-in)
# Test: Navigate UI using only keyboard and screen reader

# Contrast checking
# Use: WebAIM Contrast Checker
# Target: 4.5:1 for normal text, 3:1 for large text
```

**Accessibility Checklist**:

- [ ] All images have alt text
- [ ] All buttons have visible labels or aria-labels
- [ ] Form inputs labeled with `<label>`
- [ ] Focus indicators visible
- [ ] Tab order logical
- [ ] Color contrast ≥4.5:1
- [ ] No keyboard traps
- [ ] Error messages clear
- [ ] Modals properly marked
- [ ] Page structure with proper headings

---

## 18. Cost & Infrastructure Analysis

### Resource Requirements

**Backend (Java/Spring Boot)**:

```
CPU: 2 vCPU (single-user) → 4 vCPU (100+ concurrent users)
Memory: 2GB heap → 8GB heap
Storage: SQLite 500MB-1GB initially (grows ~10MB per 1000 records)
Network: <10 Mbps typical
```

**Database (SQLite - v1)**:

```
Initial size: ~50MB
Growth rate: ~10KB per prompt record
Max practical size: 1-2GB
Concurrent writes: Limited (single file)
Backup size: ~20MB compressed
1-year retention: <1GB expected
```

**LLM Service (Ollama/LM Studio)**:

```
CPU: 2-4 vCPU needed for LLM inference
Memory: 8-16GB (depends on model size)
Storage: Models take 4-13GB each
Network: Localhost only (no bandwidth cost)
```

**Frontend (React/Vite)**:

```
Build size: ~500KB gzipped
Network requests: <1MB per page load (with cached assets)
CDN recommended for assets
```

### Infrastructure Cost Estimate (AWS/Cloud)

**Single-User Local Deployment (v1 - SQLite)** (Recommended for v1):

- Cost: $0 (runs on user's machine)
- Backend: Java 21 JDK installed locally
- Database: SQLite file local (./data/resumes.db)
- LLM: Ollama/LM Studio local
- Storage: User's disk space
- Backup: Manual or cron-based to local filesystem

**Small Team Deployment (v1 - SQLite)**:

```
EC2 t3.medium for backend       $30/month
EBS storage (SQLite + backups)  $10/month
Backups (S3 or local)           $5/month
Monitoring (CloudWatch)         $10/month
Total: ~$55/month
```

**Production Multi-User (v2+ - PostgreSQL)**:

```
EC2 t3.large for backend        $70/month
RDS PostgreSQL (db.t3.medium)   $120/month
ElastiCache Redis (caching)     $50/month
S3 for backups & assets         $20/month
CloudFront CDN                  $30/month
Monitoring & logging            $30/month
Total: ~$320/month
```

**Enterprise Deployment (v2+ - PostgreSQL HA)**:

```
EC2 t3.xlarge for backend       $150/month
RDS PostgreSQL HA (db.r5.large) $400/month  (multi-AZ)
ElastiCache Redis Cluster       $100/month
S3 for backups, assets, logs    $50/month
CloudFront CDN                  $100/month
Monitoring, alerting, tracing   $100/month
Total: ~$900/month
```

### Cost Optimization Strategies

- Use local SQLite for single-user (free)
- Implement result caching (reduce LLM calls)
- Archive old history monthly (reduce database size)
- Use smaller LLM models where quality permits
- Implement pagination (reduce memory usage)
- Enable gzip compression (reduce bandwidth)
- Migrate to PostgreSQL only when multi-user support needed

---

## 19. Data Migration & Transition

### Backward Compatibility

**v1 Endpoints Remain Unchanged**:

```java
POST /api/file2PDF              // Still works
POST /api/upload                // Still works (resume/cover)
GET  /api/files                 // Still returns existing files
DELETE /api/files/{filename}    // Still works
GET  /api/files/{filename}      // Still works
GET  /api/health                // Still works
```

**No Breaking Changes**:

- Existing prompts (RESUME, COVER, SKILLS) continue as-is
- File storage location unchanged
- Configuration backward compatible
- Database migrations additive (no schema removals)

### Data Transition Plan

**For Existing Users**:

1. **Update Application**:
   - Download v1.1 with new prompts feature
   - Database schema auto-migrates (Flyway)
   - No user action required

2. **New Prompt History Table Created**:
   - `prompt_history` table added
   - No existing data affected
   - History starts fresh with new prompts

3. **Legacy Files Preserved**:
   - Existing files in `/uploads` remain untouched
   - Can still download old resumes/cover letters
   - GET /api/files returns both old and new files

4. **Gradual Adoption**:
   - Users can continue with old workflow
   - New prompts available optionally
   - No pressure to switch

### Deprecation Policy

**For Future Versions**:

- Announce deprecation 2 versions in advance
- Provide migration guide
- Support for minimum 12 months
- Example: Deprecate old file format in v2.0 (after v1.1 and v1.2)

---

## 20. Stakeholder & User Communication

### Internal Communication Plan

**Development Team**:

- Daily standup: Progress on PRD implementation
- Weekly sync: Architecture decisions, blockers
- Code review checklist shared in PR template
- Confluence/Wiki: Technical documentation

**QA Team**:

- Test plan review before testing begins
- Weekly QA sync: Coverage, blockers, test cases
- Defect triage: P1 (blocking), P2 (high), P3 (low)

**Product/Management**:

- Weekly status update: % complete, risks, timeline
- Demo at phase end: Show working features
- User feedback session: Early access user testing

### External Communication Plan

**User Announcement**:

**Email Template**:

```
Subject: New Features Coming to java-resumes: Interview Prep Tools

Hi [User],

We're excited to announce new AI-powered features to help with your job search:

✨ NEW FEATURES:
  • Interview Question Generator (job-specific & general)
  • Cold Email & LinkedIn Message Creator
  • Thank You Email Templates
  • Prompt History & Archive
  • Preset Filter Combinations

📅 AVAILABLE: February 15, 2026
🔗 LEARN MORE: [Link to blog post/tutorial]

These new tools help you prepare for interviews, reach out to recruiters,
and track all your generated content in one place.

No action needed from you. The new features will be available on your next login.

Questions? Reply to this email or visit [support link].

Best regards,
The java-resumes Team
```

**Blog Post** (Post-Release):

- Feature showcase with screenshots
- Use case examples
- User testimonials
- How-to guide with videos
- FAQ section

**YouTube Tutorial** (Week after release):

- 5-minute quick start
- Feature walkthrough
- Tips and tricks
- Q&A session

**Social Media**:

- Twitter/LinkedIn: Share feature announcement, tips, user stories
- Post schedule: 2x/week for first month after launch

### In-App Notifications

**On-Login Message** (First time seeing new features):

```
🎉 New Features Available!

Interview Prep Tools are here:
  • Generate job-specific interview questions
  • Create cold outreach emails
  • Track all your generated prompts

Ready to get started? Click here to explore.
[Learn More] [Dismiss]
```

### FAQ & Support Documentation

**FAQ Document** (included in README):

```markdown
## FAQ

**Q: Are my prompts sent to external LLM services?**
A: No, prompts are sent to your local Ollama/LM Studio instance only.

**Q: How long is my prompt history kept?**
A: Indefinitely. You can delete entries anytime from the History tab.

**Q: Can I export my prompts?**
A: Yes, click "Download" on any history entry to export the expanded prompt.

**Q: Is my data private?**
A: Yes, all data stays on your machine in local SQLite database.

**Q: Can I use these prompts with ChatGPT/Claude?**
A: Yes, download the expanded prompt and paste into ChatGPT/Claude.
```

**Support Email Template** (for common issues):

```
Subject: Having issues with [Feature]?

Thanks for reaching out! Here are some common solutions:

[Organized by symptom]

If you're still experiencing issues:
1. Share your browser console logs (F12 > Console)
2. Share your application logs (./logs/app.log)
3. Describe exact steps to reproduce

Reply to this email and we'll help ASAP.
```

---

## 21. Maintenance & Support

### Ongoing Maintenance Schedule

**Daily**:

- Monitor error rates and latency
- Check disk space usage
- Review security logs

**Weekly**:

- Database backup verification
- Performance analysis
- User feedback review

**Monthly**:

- Database archival/cleanup
- Dependency security updates
- Performance optimization review
- Prompt quality assessment

**Quarterly**:

- User survey for satisfaction
- Feature prioritization review
- Technical debt assessment
- Security audit

### Prompt Maintenance & Evolution

**Prompt Versioning**:

```
prompts/
├── interview-hr-questions-v1.md    (2026-02)
├── interview-hr-questions-v2.md    (2026-05 - improved)
├── interview-job-specific-v1.md
└── ... (track versions)
```

**Process for Improving Prompts**:

1. Collect user feedback (quality of results)
2. Analyze LLM responses (check for consistency, errors)
3. Refine prompt template
4. A/B test new vs old version
5. Gradual rollout if better
6. Archive old version

**Metrics to Track**:

- User satisfaction (rating per generated content)
- Reuse rate (how often users generate same type)
- Download rate (more downloads = higher quality)
- User modifications (do they heavily edit results?)

### Troubleshooting Guide

**Issue: "LLM API Connection Failed"**

- [ ] Verify Ollama/LM Studio is running
- [ ] Check endpoint in config.json
- [ ] Check firewall/network connectivity
- [ ] Restart LLM service
- [ ] Check application logs for details

**Issue: "Database is Locked"**

- [ ] Close other applications accessing database
- [ ] Check running processes: `lsof | grep resumes.db`
- [ ] Restart application
- [ ] Restore from backup if corrupted

**Issue: "Out of Memory"**

- [ ] Reduce history table size (archive old records)
- [ ] Increase heap size: `JAVA_OPTS="-Xmx4g"`
- [ ] Disable debug logging
- [ ] Upgrade RAM on machine

**Issue: "Files Not Downloading"**

- [ ] Check browser download settings
- [ ] Verify file path exists
- [ ] Check disk space available
- [ ] Check file permissions

### Support SLA

**Response Times**:

- Critical (system down): 1 hour
- High (feature broken): 4 hours
- Medium (feature degraded): 24 hours
- Low (cosmetic issues): Best effort

**Support Channels**:

- Email: support@java-resumes.dev
- GitHub Issues: For feature requests
- FAQ/Documentation: Self-service support

---

## 22. API Design Details

### API Versioning Strategy

**URL Versioning** (Recommended):

```
/api/v1/generate/{promptType}      // Current version
/api/v2/generate/{promptType}      // Future version (if breaking changes)
```

**Implement in application.yml**:

```yaml
api:
  version: v1
  base-path: /api/v1
```

**Benefits**:

- Clear version in URL
- Easy to deprecate old versions
- Client can explicitly choose version
- No confusion about breaking changes

### Response Envelope Consistency

**All endpoints return standard envelope**:

```json
{
  "status": "success|error",
  "data": {},
  "metadata": {
    "timestamp": "2026-01-30T10:30:00Z",
    "requestId": "uuid",
    "version": "1.0"
  },
  "error": null
}
```

**Error Response Format**:

```json
{
  "status": "error",
  "data": null,
  "metadata": {
    "timestamp": "2026-01-30T10:30:00Z",
    "requestId": "uuid"
  },
  "error": {
    "code": "INVALID_REQUEST",
    "message": "Job description is required",
    "details": {
      "field": "jobDescription",
      "reason": "required"
    }
  }
}
```

### Error Code Standardization

| Code            | HTTP Status | Meaning                    | Action                    |
| --------------- | ----------- | -------------------------- | ------------------------- |
| INVALID_REQUEST | 400         | Missing/invalid parameters | Fix request and retry     |
| UNAUTHORIZED    | 401         | Authentication required    | Add auth token            |
| FORBIDDEN       | 403         | Permission denied          | Check access rights       |
| NOT_FOUND       | 404         | Resource not found         | Verify ID                 |
| RATE_LIMITED    | 429         | Too many requests          | Wait and retry            |
| LLM_TIMEOUT     | 504         | LLM service timeout        | Retry after delay         |
| INTERNAL_ERROR  | 500         | Server error               | Retry later, report issue |

### Pagination Strategy

**Query Parameters**:

```
GET /api/history?page=0&size=25&sort=createdAt&order=DESC
```

**Response Format**:

```json
{
  "status": "success",
  "data": [
    { "id": 1, "promptType": "..." },
    { "id": 2, "promptType": "..." }
  ],
  "pagination": {
    "page": 0,
    "pageSize": 25,
    "totalItems": 150,
    "totalPages": 6,
    "hasNext": true,
    "hasPrevious": false
  }
}
```

**Cursor-Based Pagination** (Alternative for large datasets):

```
GET /api/history?cursor=eyJpZCI6IDEwMH0&limit=25

Response includes:
{
  "data": [...],
  "pagination": {
    "nextCursor": "eyJpZCI6IDc1fQ",
    "hasMore": true
  }
}
```

### API Documentation

**OpenAPI/Swagger Specification** (auto-generated):

- URL: `http://localhost:8080/swagger-ui.html`
- Specification: `http://localhost:8080/v3/api-docs`
- Include examples for each endpoint
- Document all error responses

**API Documentation in Code**:

```java
@PostMapping("/api/v1/generate/{promptType}")
@Operation(summary = "Generate prompt content",
           description = "Generate content using specified prompt type")
@ApiResponses({
  @ApiResponse(responseCode = "202", description = "Request accepted for processing"),
  @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
  @ApiResponse(responseCode = "429", description = "Rate limit exceeded")
})
public ResponseEntity<?> generatePrompt(
    @PathVariable("promptType")
    @Parameter(description = "Type of prompt to generate")
    String promptType,
    @RequestBody
    @Parameter(description = "Request body with prompt parameters")
    PromptRequest request) {
  // ...
}
```

---

## 23. Performance & Optimization

### Caching Strategy

**Response Caching**:

```java
// Cache get history for 5 minutes
@Cacheable(value = "promptHistory", key = "#promptType", unless = "#result == null")
@GetMapping("/api/history")
public ResponseEntity<?> getHistory(@RequestParam String promptType) {
  // ...
}

// Invalidate on delete
@CacheEvict(value = "promptHistory", allEntries = true)
@DeleteMapping("/api/history/{id}")
public ResponseEntity<?> deleteHistory(@PathVariable Long id) {
  // ...
}
```

**Configuration**:

```yaml
spring:
  cache:
    type: simple
    caffeine:
      spec: "maximumSize=500,expireAfterWrite=5m"
```

**Database Query Caching**:

- Use query result caching for frequently accessed data
- TTL: 5-10 minutes for read-heavy queries
- Invalidate on write operations

### Connection Pooling

**SQLite Connection Pool**:

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 2
      connection-timeout: 20000
      idle-timeout: 300000
      max-lifetime: 1200000
```

**LLM HTTP Connection Pooling**:

```java
// Reuse HttpClient for LLM API calls
private static final HttpClient httpClient = HttpClient.newBuilder()
    .executor(Executors.newFixedThreadPool(5))
    .connectTimeout(Duration.ofSeconds(30))
    .build();
```

### Lazy Loading & Eager Loading Strategy

**Database Queries**:

```java
// Lazy load history details (fetch only when accessed)
@Lazy
private PromptHistoryRepository historyRepo;

// Eager load for frequently accessed relations
@OneToMany(fetch = FetchType.EAGER)
private List<PromptHistory> records;
```

### Memory Management

**Large Dataset Handling**:

- Implement streaming for large file downloads
- Paginate database queries (never SELECT ALL)
- Archive old records to reduce table size
- Use projections to fetch only needed fields

**Example - Streaming Large Export**:

```java
@GetMapping("/api/history/export")
public ResponseEntity<StreamingResponseBody> exportHistory() {
  return ResponseEntity.ok()
      .contentType(MediaType.APPLICATION_OCTET_STREAM)
      .body(out -> {
        historyRepo.streamAll().forEach(record -> {
          out.write(record.toJson().getBytes());
          out.write('\n');
        });
      });
}
```

---

## 24. Analytics & Metrics

### User Behavior Tracking

**Events to Track** (Privacy-respecting):

| Event              | Data Collected            | Frequency          |
| ------------------ | ------------------------- | ------------------ |
| Prompt Generated   | promptType, responseTime  | Every generation   |
| History Viewed     | promptType filter applied | Every history view |
| Content Downloaded | fileType, fileSize        | Every download     |
| Preset Applied     | presetName                | Every use          |
| Error Occurred     | errorType, endpoint       | When error happens |

**Non-Tracked Data** (Privacy):

- User's actual prompt content
- User's personal information
- Generated LLM outputs
- Company names, job descriptions

**Implementation**:

```java
@Component
public class AnalyticsService {
  private AnalyticsClient client;

  public void trackPromptGenerated(String promptType, long responseTimeMs) {
    client.track("prompt_generated", Map.of(
        "promptType", promptType,
        "responseTimeMs", responseTimeMs,
        "timestamp", Instant.now()
    ));
  }
}
```

### Success Rate Tracking

**Metrics per Prompt Type**:

```sql
SELECT
  prompt_type,
  COUNT(*) as total_generated,
  SUM(CASE WHEN status = 'completed' THEN 1 ELSE 0 END) as successful,
  ROUND(100.0 * successful / total_generated, 2) as success_rate,
  AVG(llm_response_time_ms) as avg_response_time
FROM prompt_history
GROUP BY prompt_type;
```

**Dashboard Visualization**:

- Success rate by prompt type (bar chart)
- Average response time trend (line chart)
- Most popular prompts (pie chart)
- User retention (cohort analysis)

### A/B Testing Framework

**Testing New Prompt Version**:

```java
public class PromptABTest {
  public String getPromptVersion(String promptType, String userId) {
    // 50% users get v1, 50% get v2
    if (userId.hashCode() % 2 == 0) {
      return loadPrompt(promptType + "_v1");
    } else {
      return loadPrompt(promptType + "_v2");
    }
  }
}
```

**Track Results**:

- Record which version was used
- Track user satisfaction (rating)
- Measure generation quality
- Decide winner after 2 weeks

---

## 25. Legal & Terms

### Data Ownership & Licensing

**User-Generated Content**:

- Users own all prompts, settings, and generated content
- Application stores data for user convenience
- Users can export/delete at any time

**Prompt Templates**:

- Templates licensed under project license (MIT/Apache)
- Generated outputs owned by user
- LLM-generated content subject to LLM provider's terms

**LLM API Terms**:

- Ollama local: No licensing concerns
- LM Studio local: No licensing concerns
- If using cloud LLM: Review provider's terms

### Privacy Notice

**Data Collection**:

- What data: Prompts, settings, generated content, analytics events
- How long: Indefinitely (user controlled)
- How used: Display history, improve features, analytics
- Shared with: Nobody (local deployment)
- User rights: Export, delete, transparency

### Terms of Service Updates

**Sections to Add**:

```markdown
## 5. New Prompt Features

Users can generate content using AI-powered prompts. Generated content is:

- Owned by the user
- Not guaranteed to be original or legally permissible
- User responsible for reviewing and editing before use
- User indemnifies developer for misuse

We are not responsible for:

- Quality or accuracy of generated content
- Copyright infringement in generated content
- Outcomes of using generated content
```

### Content Liability

**Disclaimer**:

```
The java-resumes application generates content using AI models.
Generated content may contain errors, biases, or inaccurate information.

Users are responsible for:
- Reviewing generated content for accuracy
- Editing generated content before use
- Ensuring generated content doesn't infringe rights
- Compliance with laws and regulations

We provide no warranty regarding generated content quality or accuracy.
```

---

## 26. Version Control & Branching Strategy

### Branch Protection Rules

**Master Branch**:

- Require pull request reviews (2 approvers)
- Require status checks to pass (tests, linting, build)
- Dismiss stale pull request approvals
- Require branches to be up-to-date before merging

**Feature Branches**:

```
Naming convention: feature/[ticket-id]-[description]
Example: feature/ISSUE-42-interview-prompts
Lifetime: Delete after merge
```

### Pull Request Checklist Template

```markdown
## Description

[Brief description of changes]

## Type of Change

- [ ] New feature
- [ ] Bug fix
- [ ] Documentation
- [ ] Refactoring

## Testing

- [ ] Unit tests added
- [ ] Integration tests added
- [ ] Manual testing completed
- [ ] All tests passing

## Code Quality & Review Standards

### Community Best Practices

- [ ] Code adheres to community standards
  - [ ] Backend: Google Java Style Guide + Spring Boot patterns
  - [ ] Frontend: Airbnb TypeScript/JavaScript style guide
  - [ ] All: Self-explanatory code with minimal unnecessary comments
  - [ ] Consistent naming conventions across codebase
  - [ ] Proper error handling and validation

### Code Review & Complexity Reduction

- [ ] Formal code review completed (minimum 2 reviewers required)
  - [ ] Reviewer focus: Complexity assessment and reduction
  - [ ] Suggestions for simplification applied before merge
  - [ ] Edge cases identified and handled appropriately
  - [ ] All review comments resolved with explanations

**Code Review Checklist**:

- [ ] Can each method be understood in <2 minutes?
- [ ] Are there unnecessary nested structures (break into steps)?
- [ ] Is all logic as simple as possible?
- [ ] Are inputs validated and edge cases handled?
- [ ] Is there dead code or redundant logic?
- [ ] Do tests cover happy path AND edge cases?
- [ ] Are there any obvious performance concerns?

### Simplicity Standards

- [ ] Favor simple multiple-step code over complex single-step code
  - [ ] Break down complex operations into logical steps
  - [ ] Each step performs one clear action
  - [ ] Intermediate results clearly named (avoiding magic)
  - [ ] Complex conditionals extracted to named helper methods
  - [ ] Long methods split into smaller focused methods

### Quality Metrics

- [ ] Checkstyle passing (backend) - 100% compliance
- [ ] ESLint passing (frontend) - 0 warnings
- [ ] SpotBugs: No critical/major issues
- [ ] TypeScript strict mode: No `any` types
- [ ] No console errors or warnings
- [ ] Cyclomatic complexity <10 per method
- [ ] Method length <50 lines average
- [ ] DRY principle: No duplicate code blocks
- [ ] Test coverage >80%

## Documentation

- [ ] README updated with new features
- [ ] API docs updated (Swagger/OpenAPI)
- [ ] Comments added only for complex logic (NOT obvious code)
- [ ] Database schema updated
- [ ] Each public method has clear documentation
- [ ] All documentation follows `markdown.instructions.md` standards

## Deployment

- [ ] Database migration tested
- [ ] Backward compatible
- [ ] Feature flag added (if needed)
- [ ] Rollback plan documented
```

### Commit Message Standards

**Format**:

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types**: feat, fix, docs, style, refactor, test, chore
**Scope**: backend, frontend, db, config

**Example**:

```
feat(backend): add interview-specific prompt endpoint

- Create new POST /api/generate/interview-job-specific endpoint
- Add PromptService.expandPrompt() for variable substitution
- Save results to PromptHistory database table
- Add unit tests for prompt expansion

Closes #42
```

---

## 27. Documentation Completeness

### Architecture Decision Records (ADRs)

**File**: `.github/adr/ADR-001-database-selection.md`

```markdown
# ADR-001: Database Selection - SQLite Default with PostgreSQL Option

## Context

Need to store prompt history and generated content. Must support:

- v1: Single-user deployment with zero setup
- v2+: Multi-user deployment with scalability

## Decision

- **Default (v1)**: Use SQLite for local file-based storage
- **Optional (v2+)**: Support PostgreSQL for multi-user/cloud deployment
- **Abstraction**: Use Flyway + Hibernate to manage both via configuration

## Rationale

**SQLite (Default)**:

- Zero setup required (file-based)
- Perfect for single-user use case
- Easy backup/restore
- No licensing cost
- Portable across machines

**PostgreSQL (Optional)**:

- Scales to multi-user scenarios
- Cloud deployment ready
- Better concurrency handling
- Advanced features (replication, monitoring)
- Industry standard

## Implementation

1. v1 ships with SQLite only
2. v2+ adds PostgreSQL support via configuration
3. Flyway handles schema versioning for both
4. Hibernate abstracts database-specific SQL
5. Migration path: SQLite → PostgreSQL data export/import tool

## Consequences

- Single dependency tree manages both databases
- Configuration-driven database selection
- Requires database-specific migration files
- Supports gradual scaling from single-user to enterprise

## Alternatives Considered

- PostgreSQL only: Too complex for v1 single-user
- MongoDB: Document DB not ideal for relational schema
- H2 in-memory: Not persistent, not portable
- Multiple products: Too fragmented, harder to maintain
```

### Database ER Diagram

**Include in docs/architecture/ARCHITECTURE.md**:

```
PromptHistory
├── id (PK)
├── requestId (UNIQUE)
├── promptType (FK reference)
├── jobDescription
├── company
├── jobTitle
├── interviewerName
├── temperature
├── model
├── expandedPromptJson (BLOB)
├── generatedContent (BLOB)
├── generatedFilePath
├── createdAt (INDEX)
├── updatedAt
├── status
└── errorMessage

PresetFilter (v2+)
├── id (PK)
├── name
├── description
├── promptTypes (JSON array)
├── defaultContext (JSON)
└── createdAt
```

### Sequence Diagrams

**Prompt Generation Flow**:

```
User                Frontend              Backend              LLM
  |                    |                    |                   |
  |--Submits Form----->|                    |                   |
  |                    |--POST /generate--->|                   |
  |                    |  (202 Accepted)    |                   |
  |<--------202 Accepted---|                |                   |
  |                    |                    |--Background------>|
  |                    |                    |  Thread spawned   |
  |--Polls GET /files (polling loop)       |                   |
  |                    |<--File list--------|<--LLM Response---|
  |                    |                    |  Save to DB       |
  |                    |                    |  Write file       |
  |<--Results----------|                    |                   |
  |                    |                    |                   |
```

---

## 28. Future Roadmap & Extensions

### v2.0 Features (Q2 2026)

- [ ] Multi-user support with authentication
- [ ] PostgreSQL migration for scalability
- [ ] Preset filters full backend support
- [ ] Export history to PDF/CSV
- [ ] Prompt analytics dashboard
- [ ] Internationalization (5 languages)
- [ ] Advanced prompt templating
- [ ] Collaboration features (share prompts)

### v3.0 Features (Q3 2026)

- [ ] Cloud deployment (AWS/GCP)
- [ ] Mobile app (React Native)
- [ ] Browser extension
- [ ] Integration with LinkedIn API
- [ ] Integration with job boards (Indeed, LinkedIn, etc.)
- [ ] Advanced analytics & reporting
- [ ] Custom LLM model selection
- [ ] Prompt marketplace

### Technical Debt (v2.0)

- [ ] Migrate SQLite to PostgreSQL
- [ ] Implement comprehensive API security
- [ ] Add distributed tracing (OpenTelemetry)
- [ ] Refactor PromptService (too large)
- [ ] Extract common utilities
- [ ] Improve error handling coverage

---

## 29. Operational Procedures

### Common Operational Tasks

**Task: Backup Database**:

```bash
# Manual backup
sqlite3 ./data/resumes.db ".backup ./backups/manual-backup-$(date +%s).db"

# Verify integrity
sqlite3 ./backups/manual-backup-*.db "PRAGMA integrity_check;"

# Compress and store
tar -czf ./backups/resumes-backup.tar.gz ./data/resumes.db
```

**Task: Monitor Application Health**:

```bash
# Check health endpoint
curl http://localhost:8080/api/health

# Monitor logs in real-time
tail -f ./logs/app.log | grep ERROR

# Check database size
ls -lh ./data/resumes.db

# Check disk space
df -h ./
```

**Task: Clear Old History Records**:

```sql
-- Archive records older than 1 year
INSERT INTO prompt_history_archive
SELECT * FROM prompt_history
WHERE created_at < DATE('now', '-1 year');

-- Delete archived records
DELETE FROM prompt_history
WHERE created_at < DATE('now', '-1 year');

-- Verify deletion
SELECT COUNT(*) FROM prompt_history;
```

### Incident Response Plan

**Severity Levels**:

- **Critical**: System down, data loss risk, security breach
- **High**: Feature broken, significant performance degradation
- **Medium**: Feature degraded, minor performance issue
- **Low**: Cosmetic issues, nice-to-have features

**Critical Incident Process**:

1. Acknowledge receipt within 5 minutes
2. Activate incident commander
3. Assess impact and gather logs
4. Execute rollback (if applicable)
5. Implement temporary workaround
6. Root cause analysis post-incident
7. Preventive measures

**Escalation Path**:

- Level 1: Support team (response: 1 hour)
- Level 2: Development team (response: 30 min)
- Level 3: Lead architect (response: 15 min)

---

## 30. Compliance & Standards

### Code Standards Enforcement

**Backend (Checkstyle)**:

- Max line length: 120 characters
- Must pass all style checks: `./gradlew checkstyleMain`
- Critical rules: Indentation, imports, naming, Javadoc

**Frontend (ESLint & TypeScript)**:

- No `any` types allowed
- Strict mode enabled: `"strict": true`
- No console.log in production code
- Must pass: `npm run lint` and `npm run type-check`

**Documentation**:

- Markdown formatting: 80-character line length preferred
- Code examples included
- Links checked for validity
- Follow `markdown.instructions.md`

### Naming Conventions

**Backend**:

- Classes: PascalCase (ApiService, PromptHistory)
- Methods: camelCase (generatePrompt, expandPrompt)
- Constants: UPPER_SNAKE_CASE (MAX_RETRIES)
- Packages: lowercase.dotted (ca.letkeman.resumes.optimizer)

**Frontend**:

- Components: PascalCase (PromptSelector, PromptForm)
- Hooks: camelCase with use prefix (usePromptApi, useFilter)
- Constants: UPPER_SNAKE_CASE (DEFAULT_PAGE_SIZE)
- Filenames: kebab-case (prompt-selector.tsx)

---

## 31. Questions & Open Items (Updated)

1. **Preset Filter Storage**: Should presets be stored in database or local storage? _Recommendation: Both - local storage for user convenience, database for backup_
2. **Archive Strategy**: Should old history entries (>6 months) be archived? _Recommendation: Yes, with export capability_
3. **Sharing Features**: Should users be able to share prompt history with others? _Recommendation: Future enhancement, v2_
4. **Analytics**: Should we track which prompts are most popular? _Recommendation: Yes, add in phase 5_
5. **Multi-user Support**: Should the app support multiple users? _Recommendation: Out of scope for v1, single-user assumed_
6. **Cloud Deployment**: Should we support cloud hosting in v1? _Recommendation: No, local deployment only for v1_
7. **Internationalization**: How many languages to support in v1? _Recommendation: English only in v1, add in v2_
8. **API Versioning**: Do we need API v1 vs v2 at launch? _Recommendation: Start with v1, plan for v2 in architecture_

---

## 32. Glossary

- **Prompt**: A detailed instruction sent to the LLM to generate specific content
- **Expanded Prompt**: The final prompt with all template variables substituted
- **History**: Database record of all generated prompts and results
- **Preset Filter**: Saved combination of prompt types and default parameters
- **CRUD**: Create, Read, Update, Delete operations on database records
- **ATS**: Applicant Tracking System (resume optimization context)

---

## 13. Appendix: Git Workflow

```bash
# Create feature branch
git checkout -b new-prompts

# Make changes following commit conventions
# Commit early and often with descriptive messages

# Before pushing, run full test suite
./gradlew clean build test checkstyleMain

# Push to remote
git push origin new-prompts

# Create Pull Request with reference to this PRD
# Title: "feat: add expanded interview/networking prompts with database history"
# Description: Link to this PRD document

# After code review approval
# Merge into master with squash commit (recommended)
git checkout master
git merge --squash new-prompts
git commit -m "feat: add expanded interview/networking prompts with SQLite history (#123)"
git push origin master
```

---

## 14. Sign-off

| Role          | Name | Date       | Signature |
| ------------- | ---- | ---------- | --------- |
| Product Owner | -    | 2026-01-30 | Approved  |
| Tech Lead     | -    | 2026-01-30 | Approved  |
| QA Lead       | -    | 2026-01-30 | Approved  |

---

**Document Version**: 1.0
**Last Updated**: January 30, 2026
**Next Review**: Upon feature completion
**Status**: Ready for Development Sprint

---

## Related Documents

- [Project Status](./docs/wip/STATUS.md)
- [Architecture](./docs/architecture/ARCHITECTURE.md)
- [Backend Guide](./docs/BACKEND_README.md)
- [Frontend Guide](./docs/README.md)
- [More Prompts Reference](./prompts/more-prompts.md)
