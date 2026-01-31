# PRD Review Summary: Interview & Networking Prompts Feature

**Document**: `PRD-NEW-PROMPTS-FEATURE.md`
**Status**: ✅ COMPLETE & READY FOR IMPLEMENTATION
**Total Lines**: 3,248
**Last Updated**: January 30, 2026

---

## Overview

This comprehensive PRD defines the implementation of 6 expanded interview and networking prompts with full database tracking, REST API endpoints, and enhanced frontend capabilities. The feature supports both **SQLite (v1 - default)** and **PostgreSQL (v2+ - optional)** databases.

---

## Key Features Documented

### 1. **6 New Prompt Types** ✅

| Prompt Type            | Purpose                               | Output                 |
| ---------------------- | ------------------------------------- | ---------------------- |
| interview-hr-questions | 5 common HR questions                 | Markdown list          |
| interview-job-specific | Job-specific interview Qs             | Markdown list          |
| interview-reverse      | Questions to ask employer             | Markdown with sections |
| cold-email             | 5 cold outreach variations            | Email templates        |
| cold-linkedin-message  | 5 LinkedIn message variations         | Message variations     |
| thank-you-email        | 5 post-interview thank-you variations | Email templates        |

### 2. **REST API Endpoints** ✅

```
POST /api/generate/interview-hr-questions
POST /api/generate/interview-job-specific
POST /api/generate/interview-reverse
POST /api/generate/cold-email
POST /api/generate/cold-linkedin-message
POST /api/generate/thank-you-email
```

All endpoints:

- Accept consistent request format
- Return 202 Accepted with requestId
- Spawn async BackgroundResume thread
- Support polling for results via GET /api/files

### 3. **Request/Response Format** ✅

**Standardized Request Body**:

```json
{
  "promptType": "interview-hr-questions",
  "jobDescription": "string",
  "company": "string",
  "jobTitle": "string",
  "interviewerName": "string",
  "temperature": 0.7,
  "model": "mistral"
}
```

**Standardized Response** (202 Accepted):

```json
{
  "message": "generating",
  "promptType": "interview-hr-questions",
  "requestId": "uuid-1234",
  "timestamp": "2026-01-30T10:30:00Z"
}
```

### 4. **Database Implementation** ✅

#### Schema (Same for SQLite & PostgreSQL)

**PromptHistory Table**:

- `id` (Primary Key)
- `request_id` (Unique tracking ID)
- `prompt_type` (Generated content type)
- `job_description`, `company`, `job_title` (Context)
- `expanded_prompt_json` (Full prompt sent to LLM)
- `generated_content` (LLM response)
- `generated_file_path` (Output file location)
- Tracking: `created_at`, `updated_at`, `status`, `error_message`
- Performance: `llm_response_time_ms`, `token_usage_estimate`

#### Database Options

| Aspect          | SQLite                      | PostgreSQL        |
| --------------- | --------------------------- | ----------------- |
| **Default**     | ✅ v1                       | v2+               |
| **Setup**       | Zero config                 | Server required   |
| **Use Case**    | Single-user, local          | Multi-user, cloud |
| **Scalability** | Limited                     | Unlimited         |
| **Migration**   | Export/import tool provided | N/A               |

#### Configuration

**SQLite (Default)**:

```properties
spring.datasource.url=jdbc:sqlite:./data/prompts.db
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.flyway.locations=classpath:db/migration/sqlite
```

**PostgreSQL (Optional)**:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/prompts
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

### 5. **Architecture Decision** ✅

**ADR-001: Database Selection - SQLite Default with PostgreSQL Option**

- **v1**: Ships with SQLite for zero-setup single-user deployment
- **v2+**: Adds PostgreSQL support for multi-user/cloud scenarios
- **Abstraction**: Flyway + Hibernate manage both via configuration
- **Migration Path**: Seamless upgrade from SQLite → PostgreSQL
- **Philosophy**: Start simple, scale when needed

### 6. **Entity & Repository Design** ✅

**Entity**: `PromptHistory`

- Fully documented with Spring Data JPA annotations
- Includes 20+ properties for comprehensive tracking
- Supports JSON serialization for prompt/response storage

**Repository**: `PromptHistoryRepository`

- JpaRepository<PromptHistory, Long>
- Methods: findByPromptType, findByCreatedAtBetween, findByRequestId

**Service**: `PromptService`

- `expandPrompt()` - Variable substitution
- `savePromptToHistory()` - Database persistence
- `getAllHistory()`, `getHistoryByType()`, `getHistoryById()`
- `deleteHistoryById()`, `downloadExpandedPrompt()`

### 7. **Dependencies & Build** ✅

**Required**:

- `spring-boot-starter-data-jpa` - ORM/database abstraction
- `flyway-core` - Database migrations

**SQLite (Default)**:

- `sqlite-jdbc:3.45.0` - SQLite driver
- `hibernate-community-dialects:6.4.0` - Hibernate SQLite support

**PostgreSQL (Optional)**:

- `postgresql:42.7.1` - PostgreSQL driver
- `HikariCP:5.1.0` - Connection pooling

### 8. **Configuration & Setup** ✅

**Application Properties**:

- Default profiles: SQLite
- PostgreSQL profile: `postgres` (activate via SPRING_PROFILES_ACTIVE)
- Flyway: Auto-migration on startup
- JPA: Validation mode (manual migrations via Flyway)

### 9. **Testing Strategy** ✅

**Unit Tests**:

- PromptServiceTest (expand, save, retrieve operations)
- PromptHistoryRepositoryTest (CRUD operations)

**Integration Tests**:

- ResumeControllerTest (new endpoints)
- BackgroundResumeTest (async processing)

**Test Coverage Target**: 80%+

### 10. **Frontend Enhancements** ✅

Documented additions:

- New component: `PromptSelector.tsx` (radio buttons for 6 prompt types)
- New component: `PromptHistoryPanel.tsx` (view/download history)
- Update: `MainContentTab.tsx` (integrate new prompts)
- Update: Styling for new UI elements

### 11. **Implementation Plan (9 Phases)** ✅

1. **Database Setup**: Create migrations + entities
2. **Service Layer**: PromptService implementation
3. **Repository**: Data access patterns
4. **Controller Endpoints**: REST API implementation
5. **Background Processing**: BackgroundResume updates
6. **Frontend Components**: React/TypeScript components
7. **Testing**: Unit + integration tests
8. **Documentation**: README updates
9. **Deployment**: Build & verification

### 12. **Acceptance Criteria** ✅

**Backend**:

- ✅ All 6 endpoints functional
- ✅ Async processing working
- ✅ Database persistence correct
- ✅ 80%+ test coverage
- ✅ 100% Checkstyle compliance
- ✅ Follow community best practices and standards
- ✅ Code review reduces complexity
- ✅ Simple multiple-step code preferred
- ✅ Error handling for all scenarios

**Frontend**:

- ✅ Prompt selector UI responsive
- ✅ History panel functional
- ✅ File downloads working
- ✅ Loading/error states correct
- ✅ Mobile responsive
- ✅ ESLint compliance
- ✅ Airbnb style guide adherence

**Code Quality**:

- ✅ Cyclomatic complexity <10 per method
- ✅ Method length <50 lines average
- ✅ 80%+ test coverage
- ✅ Code understandable in <2 minutes
- ✅ Break complex logic into simple steps
- ✅ Formal code review by 2+ reviewers

**Integration**:

- ✅ End-to-end workflow functional
- ✅ File generation verified
- ✅ Database audit trail complete
- ✅ Performance acceptable

---

## Architecture Highlights

### Request Flow

1. **User submits** via React UI → MainContentTab.tsx
2. **Controller validates** → ResumeController.generatePrompt()
3. **Async thread spawned** → new BackgroundResume(promptRequest, root)
4. **Config loaded** → Config.json (endpoint, API key, model)
5. **LLM integration** → ApiService.produceFiles()
6. **Files saved** → FilesStorageService.save()
7. **DB tracked** → PromptService.savePromptToHistory()
8. **UI polls** → GET /api/files (retrieves results)
9. **Download** → User downloads markdown/PDF/JSON

### Database Design

**Schema Pattern**:

- SQLite and PostgreSQL share identical logical schema
- Flyway manages migrations separately for each database
- Hibernate abstracts database-specific SQL

**Scalability Path**:

- v1: Single SQLite file (./data/prompts.db)
- v1→v2: Export SQLite, import to PostgreSQL
- v2: Multi-user PostgreSQL deployment
- v2+: Connection pooling, replication, monitoring

---

## Implementation Readiness

### ✅ Fully Documented

- Executive summary
- Background & context
- 6 prompt definitions with frameworks
- API specifications
- Database schema (both SQLite & PostgreSQL)
- Entity/repository design
- Service patterns
- Controller implementation patterns
- Testing strategy
- Frontend component patterns
- Implementation phases
- Acceptance criteria
- ADR for database selection

### ✅ Ready to Implement

1. **Prompt templates** - Defined and ready
2. **REST endpoints** - Specification complete
3. **Database schema** - Both SQLite & PostgreSQL defined
4. **Architecture** - Clear async processing pattern
5. **Code patterns** - Example implementations provided
6. **Testing approach** - Coverage targets set

### ✅ Integration Points Clear

- BackgroundResume enhancement documented
- PromptService API defined
- Database integration pattern established
- Frontend hooks identified
- File management workflow specified

---

## Key Decisions

### 1. Community Best Practices & Standards

- **Rationale**: Ensure code quality, maintainability, and team alignment
- **Implementation**:
  - Java: Google Style Guide + Spring Boot patterns
  - TypeScript: Airbnb style guide
  - All: Clear, self-explanatory code with minimal comments

### 2. Code Review for Complexity Reduction

- **Rationale**: Peer review identifies and reduces unnecessary complexity before merge
- **Implementation**:
  - Minimum 2 reviewers for all code changes
  - Reviewers explicitly assess and reduce complexity
  - All review comments resolved before merge
  - Complexity assessment documented

### 3. Simple Multiple-Step Code Over Complex Single-Step

- **Rationale**: Code that is easy to understand is easier to maintain and debug
- **Implementation**:
  - Break complex operations into logical steps
  - Each step performs one clear action
  - Intermediate results clearly named
  - Complex conditionals extracted to helper methods
  - Long methods split into smaller focused methods
  - Target: Any method understandable in <2 minutes

### 4. SQLite Default (v1) + PostgreSQL Optional (v2+)

- **Rationale**: Zero-setup for single-user, scalable when needed
- **Trade-off**: Requires abstraction layer (already designed with Flyway + Hibernate)

### 5. Async Processing for All Prompts

- **Rationale**: Consistent with existing resume optimization pattern
- **Benefit**: Non-blocking UI, better user experience

### 6. Standardized Request Format

- **Rationale**: Easy for frontend, flexible for future extensions
- **Benefit**: Single endpoint pattern for all prompt types

### 7. Audit Trail via PromptHistory

- **Rationale**: Track usage, performance, errors
- **Benefit**: Debugging, analytics, compliance

### 8. Separate Database Migration Files

- **Rationale**: Support both SQLite and PostgreSQL without conflicts
- **Structure**: /db/migration/sqlite, /db/migration/postgres

---

## Quality Metrics

| Metric              | Target        | Status                 |
| ------------------- | ------------- | ---------------------- |
| Test Coverage       | 80%+          | ✅ Planned             |
| Checkstyle          | 100%          | ✅ Planned             |
| API Documentation   | Complete      | ✅ Complete            |
| Database Schema     | Defined       | ✅ Complete (both DBs) |
| Implementation Plan | Detailed      | ✅ Complete (9 phases) |
| Acceptance Criteria | Clear         | ✅ Complete            |
| Async Pattern       | Consistent    | ✅ Design aligned      |
| Error Handling      | Comprehensive | ✅ Designed            |

---

## Next Steps

### Immediate (Ready Now)

1. ✅ Read complete PRD
2. ✅ Review architecture decisions
3. ✅ Validate acceptance criteria
4. ✅ Confirm database choice (SQLite v1 + PostgreSQL v2+)

### Implementation Phase 1 (Database)

1. Create Flyway migration files (SQLite + PostgreSQL)
2. Create PromptHistory entity
3. Create PromptHistoryRepository
4. Run migrations and verify

### Implementation Phase 2 (Service)

1. Create PromptService
2. Implement expand/save/retrieve methods
3. Add unit tests

### Implementation Phase 3 (API)

1. Create new REST endpoints
2. Update ResumeController
3. Update BackgroundResume
4. Add integration tests

### Implementation Phase 4 (Frontend)

1. Create PromptSelector component
2. Create PromptHistoryPanel component
3. Update MainContentTab
4. Add styling

### Implementation Phase 5 (Testing & Deployment)

1. Run full test suite
2. Verify Checkstyle compliance
3. Manual end-to-end testing
4. Deploy

---

## Document Statistics

| Section                 | Lines     | Status                 |
| ----------------------- | --------- | ---------------------- |
| Executive Summary       | ~50       | ✅ Complete            |
| Background & Context    | ~100      | ✅ Complete            |
| Functional Requirements | ~200      | ✅ Complete            |
| Database Schema         | ~150      | ✅ Complete (both DBs) |
| Architecture Decisions  | ~100      | ✅ Complete            |
| Implementation Details  | ~1,000    | ✅ Complete            |
| Testing Strategy        | ~200      | ✅ Complete            |
| Frontend Enhancements   | ~150      | ✅ Complete            |
| Configuration           | ~200      | ✅ Complete            |
| Acceptance Criteria     | ~100      | ✅ Complete            |
| **TOTAL**               | **3,248** | ✅ COMPLETE            |

---

## Final Checklist

- [x] Executive summary clear
- [x] Background/context documented
- [x] 6 prompts fully defined
- [x] REST API endpoints specified
- [x] Request/response formats standardized
- [x] Database schema defined (SQLite + PostgreSQL)
- [x] Architecture decision documented (ADR)
- [x] Entity/repository design complete
- [x] Service layer patterns provided
- [x] Controller patterns provided
- [x] Testing strategy outlined
- [x] Frontend components designed
- [x] Implementation phases detailed (9 phases)
- [x] Acceptance criteria clear
- [x] Dependencies listed (both SQLite & PostgreSQL)
- [x] Configuration examples provided
- [x] Error handling documented
- [x] Performance considerations noted
- [x] Deployment instructions clear
- [x] Ready for development team

---

## Conclusion

The **Expand Interview & Networking Prompts Feature PRD is complete and ready for implementation**. The document provides:

✅ **Clear requirements** - 6 new prompt types with full specifications
✅ **Detailed architecture** - Async processing, database design, API contracts
✅ **Implementation guidance** - Code patterns, configuration examples, testing approach
✅ **Database flexibility** - SQLite for v1 (default), PostgreSQL for v2+ (optional)
✅ **Quality standards** - 80%+ test coverage, 100% Checkstyle compliance
✅ **Phase-based plan** - 9 sequential implementation phases
✅ **Acceptance criteria** - Clear success metrics for backend, frontend, integration

**Recommended team capacity**: 2-3 developers
**Estimated duration**: 2-3 week sprint
**Priority**: Medium
**Risk level**: Low (builds on existing patterns)

---

_Document prepared January 30, 2026_
_Status: Ready for Sprint Planning_
