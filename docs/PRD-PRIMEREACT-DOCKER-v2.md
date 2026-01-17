# Product Requirements Document (PRD)

## ReactJS Frontend with PrimeReact and Java 25 Backend Containerization

**Document Version:** 2.0
**Date Created:** January 16, 2026
**Last Updated:** January 16, 2026
**Project Status:** Ready for Development

---

## Table of Contents

1. [Executive Summary](#1-executive-summary)
2. [Project Objectives](#2-project-objectives)
3. [Technical Requirements](#3-technical-requirements)
4. [Detailed Specifications](#4-detailed-specifications)
5. [UI Layout and Features](#5-ui-layout-and-features)
6. [API Integration Requirements](#6-api-integration-requirements)
7. [Acceptance Criteria](#7-acceptance-criteria)
8. [Non-Functional Requirements](#8-non-functional-requirements)
9. [Deliverables](#9-deliverables)
10. [Success Metrics](#10-success-metrics)
11. [Timeline and Milestones](#11-timeline-and-milestones)
12. [Constraints and Assumptions](#12-constraints-and-assumptions)
13. [Notes for the Developer](#13-notes-for-the-developer)
14. [Approval and Sign-off](#14-approval-and-sign-off)

---

## 1. Executive Summary

Create a modern, production-ready full-stack application with a **ReactJS frontend using PrimeReact** component library and a **Java 25 backend** with Spring Boot. Both services will be containerized with Docker for consistent deployment across environments. The application enables users to upload, manage, and optimize resume and cover letter documents with AI-powered processing capabilities.

---

## 2. Project Objectives

- Develop a responsive, professional ReactJS frontend application with PrimeReact UI components
- Implement a modular, tab-based UI with file management, document processing, and conversion tools
- Create a robust Java 25 backend API for document processing and file storage
- Implement modern React patterns (hooks, functional components, context API)
- Support both light and dark themes throughout the application with persistent preferences
- Containerize both frontend and backend with Docker for consistent deployment
- Ensure the application is production-ready with optimized builds
- Establish best practices for full-stack development, testing, and deployment workflows
- Enable seamless communication between frontend and backend services via REST API

---

## 3. Technical Requirements

### 3.1 Frontend Technology Stack

- **Runtime:** Node.js 18 LTS or higher
- **Framework:** React 18.x or latest stable
- **UI Component Library:** PrimeReact (latest version)
- **Build Tool:** Vite (recommended) or Create React App
- **Package Manager:** npm
- **Language:** TypeScript (recommended)
- **CSS/Styling:** Tailwind CSS or PrimeReact built-in styling
- **State Management:** Context API or Zustand
- **HTTP Client:** Axios
- **Theme Support:** PrimeReact themes (lara-light, lara-dark) with toggle functionality
- **Testing:** Vitest or Jest with React Testing Library (minimum 80% UI coverage)
- **Linting:** ESLint with React and Prettier plugins

### 3.2 Backend Technology Stack

- **Runtime:** Java 25 LTS
- **Framework:** Spring Boot 3.3+ or Spring Framework 6+
- **Build Tool:** Gradle 8+
- **Database:** None, but if required SQLite, try without first by simply listing files on the server
- **API:** RESTful API with OpenAPI/Swagger documentation
- **Testing:** JUnit 5, Mockito (minimum 80% code coverage)
- **Logging:** SLF4J with Logback
- **Configuration:** Spring Cloud Config or application.yml
- **File Processing:** Apache Tika or similar for document handling
- **PDF Generation:** iText or Apache PDFBox for markdown-to-PDF conversion

### 3.3 Docker & Containerization

- **Frontend Container:**
  - Build Base: Node.js 18-alpine
  - Runtime Base: Nginx latest-alpine (multi-stage build)
  - Port Exposure: 3000 (development) / 80 (production)
  - Health Check: HTTP GET on port 80

- **Backend Container:**
  - Build Base: maven:3.9-eclipse-temurin-25
  - Runtime Base: Eclipse Temurin Java 25-alpine (multi-stage build)
  - Port Exposure: 8080 (internal service communication)
  - Health Check: HTTP GET on `/api/health`

- **Docker Compose:**
  - Orchestrate frontend, backend, and optional database services
  - Internal bridge network for secure service communication
  - Volume management for database persistence
  - Environment variable configuration per environment

### 3.4 Development Environment

- **Code Quality:** ESLint configuration for both frontend and backend
- **Code Formatting:** Prettier configuration (frontend), Spotless/Checkstyle (backend)
- **Git:** .gitignore file with Node.js, Java, Docker artifacts excluded
- **Environment Variables:** Support for .env files (development and production)
- **Dev Containers:** Optional DevContainer configuration for VS Code

---

## 4. Detailed Specifications

### 4.1 Application Structure

**Frontend Directory Structure:**

```shell
frontend/
├── public/
│   ├── index.html
│   └── favicon.ico
├── src/
│   ├── components/
│   │   ├── Layout/
│   │   │   ├── Navbar.jsx
│   │   │   ├── Sidebar.jsx
│   │   │   ├── FileHistory.jsx
│   │   │   └── MainLayout.jsx
│   │   ├── Tabs/
│   │   │   ├── MainContentTab.jsx
│   │   │   └── AdditionalToolsTab.jsx
│   │   ├── Forms/
│   │   │   ├── DocumentUploadForm.jsx
│   │   │   ├── MarkdownToPdfForm.jsx
│   │   │   └── FormInputs.jsx
│   │   ├── Common/
│   │   │   ├── ErrorBoundary.jsx
│   │   │   ├── LoadingSpinner.jsx
│   │   │   └── ConfirmDialog.jsx
│   │   └── index.js
│   ├── pages/
│   │   ├── HomePage.jsx
│   │   └── 404.jsx
│   ├── hooks/
│   │   ├── useTheme.js
│   │   ├── useApi.js
│   │   └── useFileManagement.js
│   ├── services/
│   │   ├── api.js
│   │   └── fileService.js
│   ├── context/
│   │   ├── ThemeContext.jsx
│   │   └── AppContext.jsx
│   ├── utils/
│   │   ├── constants.js
│   │   ├── validators.js
│   │   └── helpers.js
│   ├── App.jsx
│   ├── main.jsx
│   ├── App.css
│   └── index.css
├── tests/
│   ├── components/
│   ├── hooks/
│   ├── services/
│   └── setup.js
├── .gitignore
├── .env.example
├── .dockerignore
├── .eslintrc.json
├── .prettierrc
├── package.json
├── vite.config.js
├── Dockerfile
├── docker-compose.yml
└── README.md
```

**Backend Directory Structure:**

```shell
backend/
├── src/
│   ├── main/
│   │   ├── java/com/resumeapp/
│   │   │   ├── ResumeApplication.java
│   │   │   ├── config/
│   │   │   │   ├── CorsConfig.java
│   │   │   │   ├── SwaggerConfig.java
│   │   │   │   └── AppConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── FileController.java
│   │   │   │   ├── DocumentController.java
│   │   │   │   └── UtilityController.java
│   │   │   ├── service/
│   │   │   │   ├── FileService.java
│   │   │   │   ├── ResumeProcessingService.java
│   │   │   │   ├── CoverLetterService.java
│   │   │   │   └── DocumentConversionService.java
│   │   │   ├── repository/
│   │   │   │   ├── FileRepository.java
│   │   │   │   └── DocumentRepository.java
│   │   │   ├── entity/
│   │   │   │   ├── FileEntity.java
│   │   │   │   └── DocumentEntity.java
│   │   │   ├── dto/
│   │   │   │   ├── FileDto.java
│   │   │   │   ├── ProcessingResultDto.java
│   │   │   │   └── ErrorResponseDto.java
│   │   │   ├── exception/
│   │   │   │   ├── ApplicationException.java
│   │   │   │   └── FileNotFoundException.java
│   │   │   └── util/
│   │   │       ├── FileValidator.java
│   │   │       └── Constants.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       └── application-test.yml
│   └── test/java/com/resumeapp/
│       ├── controller/
│       ├── service/
│       ├── repository/
│       └── TestUtil.java
├── .gitignore
├── .dockerignore
├── pom.xml
├── Dockerfile
├── docker-compose.yml
└── README.md
```

### 4.2 PrimeReact Integration

- Install PrimeReact and PrimeIcons packages
- Configure PrimeReact theme with tailwind integration
- Create reusable PrimeReact component wrappers (Button, Input, Dialog, etc.)
- Implement responsive design using PrimeReact's grid and layout components
- Use PrimeReact components throughout: Button, Card, Tabs, DataTable, Dialog, InputText, FileUpload, Toast, etc.
- Ensure theme consistency across all components
- Theme switching with localStorage persistence

### 4.3 Application Features and UI Layout

#### **Main Layout Structure:**

```
┌──────────────────────────────────────────────────────────────────────────────────────────┐
│ Header / Navbar (Theme toggle, branding, logo)                                          │
├────────────────────────────────────────────────────────────────────────────┬─────────────┤
│ [Main Content Tab] [Additional Tools Tab]                                 │ File History│
├──────────────────────────────────────────────────────────────────────────┬─┤ (Always    │
│                                                                          │ │ Visible)   │
│ Main Content / Additional Tools Panel                                  │ │ ┌─────────┐│
│ (Mutually exclusive - only one visible at a time)                     │ │ │ Files:  ││
│                                                                        │ │ │ [↓][X] ││
│                                                                        │ │ │ [↓][X] ││
│                                                                        │ │ │ [↓][X] ││
│                                                                        │ │ └─────────┘│
│                                                                        │ │ (Scrollable)
│                                                                        │ │
└────────────────────────────────────────────────────────────────────────┴─┴─────────────┘
```

#### **1. File History UI (Always Visible Right Panel)**

Displays all uploaded/processed files with actions:

```
┌─────────────────────────────────────────┐
│ File History                            │
├─────────────────────────────────────────┤
│ [resume-v4.pdf]     [↓] [×]            │
│ [cover-letter-v2]   [↓] [×]            │
│ [resume-v3.docx]    [↓] [×]            │
│ [interview-notes]   [↓] [×]            │
├─────────────────────────────────────────┤
│        (Vertical scrollable list)       │
└─────────────────────────────────────────┘
```

**Features:**

- File listing with metadata (name, upload date, size)
- Download button (icon) - retrieves file via GET endpoint
- Delete button (icon) - shows confirmation dialog, then calls DELETE
- Vertical scrollable list for many files
- Loading skeleton while fetching file list
- Toast notification on success/error

**API Endpoints:**

- `GET /api/files` - List all files
- `GET /api/files/{filename}` - Download file
- `DELETE /api/files/{filename}` - Delete file

#### **2. Main Content Tab (Tab 1) - Document Upload & Processing**

```
┌────────────────────────────────────────────────────┐
│ Main Content Tab                                   │
├────────────────────────────────────────────────────┤
│ ☀ Light  🌙 Dark (Theme Toggle)                  │
├────────────────────────────────────────────────────┤
│ SECTION A: Document Source                        │
├────────────────────────────────────────────────────┤
│ [● Paste Text  ○ Upload Files]                   │
│ ────────────────────────────────────────────────  │
│ Job Description:                                  │
│ ┌──────────────────────────────────────────────┐ │
│ │ [Paste multi-line text input or upload file] │ │
│ └──────────────────────────────────────────────┘ │
│ Source Resume:                                   │
│ ┌──────────────────────────────────────────────┐ │
│ │ [Paste multi-line text input or upload file] │ │
│ └──────────────────────────────────────────────┘ │
├────────────────────────────────────────────────────┤
│ SECTION B: Processing Actions                     │
├────────────────────────────────────────────────────┤
│ [Create Optimized Resume] [Create Cover Letter]  │
│ [Submit]                                          │
│                                                   │
│ [Loading spinner when processing...]             │
│ [Success/Error message toast]                    │
└────────────────────────────────────────────────────┘
```

**Features:**

- Toggle between "Paste" and "Upload" modes
- Two multi-line text inputs (Job Description, Resume) for paste mode
- Two file upload areas for file mode
- Input validation with error messages
- "Create Optimized Resume" button (triggers API call)
- "Create Optimized Cover Letter" button (triggers API call)
- "Submit" button (uploads documents)
- Progress indicators during API calls
- Loading spinner component
- Success/error toast notifications
- Clear form button

**API Endpoints:**

- `POST /api/upload` - Upload documents
- `POST /api/process/resume` - Generate optimized resume
- `POST /api/process/cover-letter` - Generate optimized cover letter

#### **3. Additional Tools Tab (Tab 2) - Utilities**

```
┌────────────────────────────────────────────────────┐
│ Additional Tools Tab                               │
├────────────────────────────────────────────────────┤
│ Markdown to PDF Converter                         │
├────────────────────────────────────────────────────┤
│ Select Markdown File:                            │
│ ┌──────────────────────────────────────────────┐ │
│ │ [File Upload Input]                          │ │
│ └──────────────────────────────────────────────┘ │
│                                                   │
│ [Convert] [Download]                             │
│                                                   │
│ [Status message]                                 │
└────────────────────────────────────────────────────┘
```

**Features:**

- Single file upload (markdown file)
- "Convert" button to trigger markdown-to-PDF conversion
- "Download" button to download converted PDF
- Loading state during conversion
- Success/error messages
- Progress indicator

**API Endpoints:**

- `POST /api/markdownFile2PDF` - Convert markdown to PDF

#### **Core UI Features:**

- **Theme Support:** Dark and light theme toggle with persistent user preference in localStorage
- **Responsive Design:** Mobile-first approach (320px - 1920px)
- **Tab Management:** Mutually exclusive tabs (only one visible at a time)
- **File Management:** Upload, download, delete files with confirmation dialogs
- **Form Validation:** Input validation with user-friendly error messages
- **Error Handling:** Error boundaries and graceful error display
- **Loading States:** Spinner/progress indicators during API calls
- **Toast Notifications:** PrimeReact Toast for success/error/info messages
- **Keyboard Navigation:** Full keyboard support for accessibility
- **Tab ordering:** Logical tab order for keyboard users
- **ARIA labels:** Proper ARIA labels on all interactive elements
- **Accessibility:** WCAG 2.1 AA compliance

---

## 5. UI Layout and Features

(Detailed layout specifications provided in Section 4.3)

---

## 6. API Integration Requirements

### 6.1 Backend API Endpoints

All endpoints must return JSON responses with proper HTTP status codes and error handling:

#### **File Management Endpoints:**

**GET /api/files** - List all uploaded files with metadata

```json
{
  "files": [
    {
      "id": "uuid",
      "name": "resume-v4.pdf",
      "type": "application/pdf",
      "uploadedDate": "2024-01-16T10:30:00Z",
      "size": 245000
    }
  ]
}
```

- Status: 200 OK, 500 Server Error

**GET /api/files/{filename}** - Download specific file

- Response: Binary file content with proper Content-Type header
- Status: 200 OK, 404 Not Found, 500 Server Error

**DELETE /api/files/{filename}** - Delete file

```json
{
  "success": true,
  "message": "File deleted successfully"
}
```

- Status: 200 OK, 404 Not Found, 500 Server Error

#### **Document Processing Endpoints:**

**POST /api/upload** - Upload job description and resume

```json
{
  "jobDescription": "Job description text or filename",
  "resume": "Resume text or filename",
  "uploadId": "uuid"
}
```

- Request: Form data or JSON with file uploads
- Response: 201 Created, 400 Bad Request, 500 Server Error

**POST /api/process/resume** - Generate optimized resume

```json
Request:
{
  "uploadId": "uuid",
  "jobDescription": "text",
  "resume": "text"
}

Response:
{
  "optimizedResume": "generated resume text",
  "suggestions": ["suggestion 1", "suggestion 2"],
  "matchPercentage": 85
}
```

- Status: 200 OK, 400 Bad Request, 500 Server Error

**POST /api/process/cover-letter** - Generate optimized cover letter

```json
Request:
{
  "uploadId": "uuid",
  "jobDescription": "text",
  "resume": "text"
}

Response:
{
  "optimizedCoverLetter": "generated cover letter text",
  "suggestions": ["suggestion 1", "suggestion 2"]
}
```

- Status: 200 OK, 400 Bad Request, 500 Server Error

#### **Utility Endpoints:**

**POST /api/markdownFile2PDF** - Convert markdown to PDF

- Request: Form data with markdown file upload
- Response: Binary PDF file with Content-Type: application/pdf
- Status: 200 OK, 400 Bad Request, 500 Server Error

**GET /api/health** - Health check endpoint

```json
{
  "status": "UP",
  "timestamp": "2024-01-16T10:30:00Z"
}
```

- Status: 200 OK, 503 Service Unavailable

### 6.2 API Error Handling

All error responses must follow consistent format:

```json
{
  "error": "ERROR_CODE",
  "message": "Human-readable error message",
  "timestamp": "2024-01-16T10:30:00Z",
  "details": {
    "field": "error details"
  }
}
```

**Common Error Codes:**

- `INVALID_FILE_FORMAT` - File format not supported
- `FILE_SIZE_EXCEEDED` - File exceeds size limit
- `FILE_NOT_FOUND` - Requested file not found
- `PROCESSING_ERROR` - Error during document processing
- `VALIDATION_ERROR` - Input validation failed
- `INTERNAL_SERVER_ERROR` - Unexpected server error

### 6.3 CORS Configuration

- Backend must allow requests from frontend origin (e.g., http://localhost:3000)
- Frontend must configure Axios with proper CORS headers
- Nginx must proxy requests to backend with proper headers
- Production: Configure CORS for production domain

### 6.4 Request/Response Configuration

- All requests: Content-Type: application/json (or multipart/form-data for file uploads)
- All responses: Content-Type: application/json (except file downloads)
- Proper Content-Disposition headers for file downloads
- Cache-Control headers for appropriate resources
- HSTS headers (production only)

### 6.5 Authentication (Optional - Future Scope)

- JWT token-based authentication
- Token refresh mechanism
- Secure token storage in frontend (HttpOnly cookies)
- Authorization header validation

---

## 7. Acceptance Criteria

### 7.1 Frontend Acceptance Criteria

- [ ] React project created with Vite
- [ ] PrimeReact installed and properly configured with Tailwind CSS
- [ ] Tab-based UI implemented: Main Content Tab, Additional Tools Tab, File History Panel
- [ ] Light theme (lara-light) and dark theme (lara-dark) fully implemented
- [ ] Theme toggle button functional with localStorage persistence
- [ ] Main Content Tab: Paste/File upload mode toggle working
- [ ] Main Content Tab: Multi-line inputs for job description and resume
- [ ] Main Content Tab: File upload areas for documents
- [ ] Main Content Tab: "Create Optimized Resume" button with API integration
- [ ] Main Content Tab: "Create Optimized Cover Letter" button with API integration
- [ ] Main Content Tab: "Submit" button to upload documents
- [ ] Additional Tools Tab: Markdown to PDF file upload area
- [ ] Additional Tools Tab: "Convert" button with API integration
- [ ] File History Panel: Display list of uploaded files
- [ ] File History Panel: Download buttons (GET /api/files/{filename})
- [ ] File History Panel: Delete buttons with confirmation dialog (DELETE /api/files/{filename})
- [ ] File History Panel: Vertical scrollable list
- [ ] All PrimeReact components styled with selected theme
- [ ] Loading spinners during API calls
- [ ] Toast notifications for success/error/info messages
- [ ] Error boundaries to catch React errors
- [ ] Responsive design tested: mobile (320px), tablet (768px), desktop (1920px)
- [ ] ESLint passing with zero violations
- [ ] Prettier formatting applied consistently
- [ ] Unit tests with minimum 80% UI coverage
- [ ] Integration tests for API calls
- [ ] No console errors or warnings
- [ ] Application builds without warnings
- [ ] Axios configured for backend communication
- [ ] Environment variables for API endpoint (.env.example provided)
- [ ] README.md with comprehensive setup and deployment instructions
- [ ] Dev Container configuration (optional)

### 7.2 Backend Acceptance Criteria

- [ ] Java 25 Spring Boot application created and running
- [ ] All REST API endpoints implemented: /api/files, /api/upload, /api/process/\*, /api/markdown2pdf, /api/health
- [ ] Swagger/OpenAPI documentation generated and accessible (/swagger-ui.html)
- [ ] GET /api/files endpoint returning file list correctly
- [ ] GET /api/files/{filename} endpoint with proper file download headers
- [ ] DELETE /api/files/{filename} endpoint with confirmation handling
- [ ] POST /api/upload endpoint with file/text input handling
- [ ] POST /api/process/resume endpoint with resume optimization logic
- [ ] POST /api/process/cover-letter endpoint with cover letter generation logic
- [ ] POST /api/markdownFile2PDF endpoint with conversion functionality
- [ ] GET /api/health endpoint returning service status
- [ ] CORS configuration allowing frontend origin
- [ ] Proper error handling with consistent JSON responses
- [ ] Input validation on all endpoints
- [ ] File upload validation (size limits, type checking)
- [ ] File storage configuration and working
- [ ] Database configuration (H2 for dev, PostgreSQL for prod)
- [ ] Database schema and migrations set up
- [ ] Unit tests with minimum 80% code coverage
- [ ] Integration tests for API endpoints
- [ ] Service layer for business logic
- [ ] Repository layer for data access
- [ ] Proper exception handling and logging
- [ ] SLF4J logging configured with Logback
- [ ] Application properties for dev/prod/test environments
- [ ] Spring profiles (dev, prod, test) configured
- [ ] Maven/Gradle build successful without warnings
- [ ] Executable JAR created and runnable
- [ ] README.md with setup and deployment instructions
- [ ] API documentation complete and clear

### 7.3 Docker Acceptance Criteria

**Frontend Container:**

- [ ] Dockerfile created with multi-stage build (Node build + Nginx runtime)
- [ ] .dockerignore properly configured (node_modules, .git, etc.)
- [ ] Frontend Docker image builds successfully
- [ ] Frontend image size < 100MB
- [ ] Frontend container runs without errors
- [ ] Application accessible at localhost:3000 (development)
- [ ] Application accessible at localhost:80 (production)
- [ ] Nginx configured to serve React app correctly
- [ ] Nginx configured to proxy API requests to backend service
- [ ] Static assets served with proper cache headers
- [ ] CORS headers configured in Nginx
- [ ] Health check implemented in Dockerfile
- [ ] Health check returning proper response

**Backend Container:**

- [ ] Dockerfile created with multi-stage build (Maven build + Java runtime)
- [ ] .dockerignore properly configured
- [ ] Backend Docker image builds successfully
- [ ] Backend image size < 200MB
- [ ] Backend container runs without errors
- [ ] API accessible at localhost:8080 (development)
- [ ] /api/health endpoint returns UP status
- [ ] JVM parameters optimized for container (memory, GC)
- [ ] Non-root user configured for security
- [ ] Health check implemented in Dockerfile
- [ ] Environment variables properly passed to container
- [ ] Database connection string configurable via environment

**Docker Compose:**

- [ ] docker-compose.yml created for full-stack setup
- [ ] Frontend service configured with port mapping and environment
- [ ] Backend service configured with port mapping and environment
- [ ] Database service configured (optional PostgreSQL)
- [ ] Internal bridge network created for service communication
- [ ] Services can communicate using service names (frontend, backend, database)
- [ ] Volume persistence for database data
- [ ] All services start with `docker compose up --build`
- [ ] All services start with `docker compose up` (images pre-built)
- [ ] All services stop cleanly with `docker compose down`
- [ ] Health checks passing for all services
- [ ] Logs accessible via `docker compose logs`
- [ ] Environment files (.env.development, .env.production)

### 7.4 Code Quality Acceptance Criteria

**Frontend:**

- [ ] No console errors or warnings
- [ ] ESLint passing (zero violations)
- [ ] Prettier formatting applied throughout
- [ ] Components modular and reusable
- [ ] Self-explanatory code (comments explain WHY, not WHAT)
- [ ] Comments only on non-obvious business logic
- [ ] Proper naming conventions (camelCase for variables/functions, PascalCase for components)
- [ ] No unused variables or imports
- [ ] No hardcoded values (use constants)
- [ ] No sensitive data in code or logs
- [ ] .gitignore configured properly
- [ ] .env.example provided with templates

**Backend:**

- [ ] No compiler warnings
- [ ] Proper Java naming conventions
- [ ] No unused code or imports
- [ ] Comments on complex business logic only
- [ ] Self-explanatory method and variable names
- [ ] Proper exception handling
- [ ] No hardcoded values (use configuration)
- [ ] Logging appropriate events
- [ ] No sensitive data in logs
- [ ] .gitignore configured properly
- [ ] application.yml templates for all profiles

### 7.5 Documentation Acceptance Criteria

**Frontend README.md (must include):**

- [ ] Project overview and purpose
- [ ] Technology stack with specific versions
- [ ] Prerequisites (Node.js 18+, Docker, npm/yarn)
- [ ] Installation instructions (`npm install`)
- [ ] Development setup (`npm run dev`)
- [ ] All available npm scripts with descriptions
- [ ] Build instructions (`npm run build`)
- [ ] Testing instructions and coverage reporting
- [ ] Docker build and run instructions
- [ ] Environment variables configuration and .env setup
- [ ] Project directory structure explanation
- [ ] How to connect to backend API
- [ ] Theme customization instructions
- [ ] Troubleshooting common issues
- [ ] Contributing guidelines
- [ ] Table of Contents (documentation > 100 lines)

**Backend README.md (must include):**

- [ ] Project overview and purpose
- [ ] Technology stack with versions (Java 25, Spring Boot version)
- [ ] Prerequisites (JDK 25, Maven 3.8+, Docker, SQLite)
- [ ] Installation and build instructions
- [ ] Running application locally
- [ ] Testing instructions with coverage reporting
- [ ] Docker build and run instructions
- [ ] Database setup and migration instructions
- [ ] API documentation link (Swagger endpoint)
- [ ] Environment variables and profiles
- [ ] Configuration file examples
- [ ] Logging configuration
- [ ] Project structure explanation
- [ ] Common troubleshooting issues
- [ ] Contributing guidelines
- [ ] Table of Contents (documentation > 100 lines)

**Root README.md (must include):**

- [ ] Full-stack project overview
- [ ] Architecture and system design diagram (ASCII and Mermaid Markdown **both**)
- [ ] Complete technology stack overview
- [ ] Quick start guide (`docker compose up --build`)
- [ ] Directory structure for frontend and backend
- [ ] Development environment setup instructions
- [ ] Production deployment instructions
- [ ] API endpoint overview
- [ ] Database setup instructions
- [ ] Known issues and workarounds
- [ ] Contributing guidelines
- [ ] License information
- [ ] Table of Contents (documentation > 100 lines)

---

## 8. Non-Functional Requirements

### 8.1 Performance

**Frontend:**

- Initial page load time < 3 seconds on 4G networks
- Lighthouse Performance score > 85
- Bundle size optimized (target < 200KB gzipped)
- Tab switching transitions smooth (< 100ms)
- API calls with timeout (e.g., 30 seconds)
- Debounced form inputs

**Backend:**

- API response time < 500ms for file operations
- Document processing response time < 5 seconds
- Database query optimization with proper indexing
- Connection pooling configured (20-30 connections)
- Request timeout: 30 seconds
- Concurrent request handling

**Docker:**

- Frontend image < 100MB
- Backend image < 200MB
- Container startup time < 10 seconds
- Memory usage < 512MB per service (development)
- Memory usage < 1GB per service (production recommended)

### 8.2 Accessibility

- WCAG 2.1 AA compliance
- Proper ARIA labels on all interactive elements
- Keyboard navigation support for all features
- Color contrast ratios meet accessibility standards (4.5:1 for normal text)
- Tab ordering logical and intuitive
- Screen reader support for file operations and notifications
- Focus indicators visible on all interactive elements
- Error messages linked to form fields
- Instructions clear and accessible

### 8.3 Security

**Frontend:**

- No console errors related to security
- Sensitive data in environment variables only
- Secure API communication (HTTPS in production)
- XSS prevention through React's built-in escaping
- Input sanitization on all user inputs
- No localStorage for sensitive data (use HttpOnly cookies for auth)
- Content Security Policy (CSP) headers

**Backend:**

- Input validation on all endpoints
- File upload validation (size < 50MB, type whitelist)
- SQL injection prevention (parameterized queries)
- Authentication/authorization if required
- Rate limiting on API endpoints (e.g., 100 requests/minute)
- Proper logging of security events
- No sensitive data in logs
- CORS properly configured
- HTTPS in production

**Docker:**

- Non-root user for all container processes
- No secrets in Docker images
- Security scanning of base images
- Regular base image updates
- Minimal attack surface (alpine images)
- Health checks properly configured

### 8.4 Compatibility

- Browser: Chrome, Firefox, Safari, Edge (latest two versions)
- Mobile browsers: iOS Safari 15+, Chrome Mobile
- Responsive design: 320px to 1920px width
- Java 25 compatibility on Linux, Windows, macOS
- Docker 20.10+ and Docker Compose 2.0+

---

## 9. Deliverables

### 9.1 Frontend Deliverables

1. **Source Code**
   - Complete React source code (src/ directory)
   - All components, hooks, services, and utilities
   - Configuration files (vite.config.js, .eslintrc, .prettierrc)
   - Test files with 80%+ coverage
   - .dockerignore and Dockerfile

2. **Configuration & Build Files**
   - package.json with all scripts and dependencies
   - .env.example with required variables
   - .gitignore properly configured
   - Build artifacts (dist/ directory)

3. **Documentation**
   - README.md with setup and deployment instructions
   - Component documentation
   - API integration guide
   - Theme customization guide

### 9.2 Backend Deliverables

1. **Source Code**
   - Complete Java/Spring Boot source code
   - All controllers, services, repositories, entities
   - Configuration classes and application properties
   - Test files with 80%+ coverage
   - .dockerignore and Dockerfile

2. **Build Artifacts**
   - pom.xml or build.gradle with all dependencies
   - Executable JAR file
   - Database migration scripts (if applicable)

3. **Documentation**
   - README.md with setup and deployment instructions
   - API documentation (Swagger/OpenAPI)
   - Database schema documentation
   - Configuration guide

### 9.3 Docker & DevOps Deliverables

1. **Docker Configuration**
   - Frontend Dockerfile (multi-stage)
   - Backend Dockerfile (multi-stage)
   - docker-compose.yml
   - .dockerignore files for both services
   - Docker build optimization documentation

2. **Environment Configuration**
   - .env.development example
   - .env.production example
   - Environment variable documentation

3. **Deployment Documentation**
   - Docker setup guide
   - Deployment checklist
   - Scaling guide
   - Troubleshooting guide

### 9.4 Complete Documentation

1. **Root README.md** - Full-stack project overview
2. **Frontend/README.md** - Frontend-specific setup
3. **Backend/README.md** - Backend-specific setup
4. **API documentation** - Swagger endpoint live
5. **Architecture documentation** - System design overview

### 9.5 Version Control

- All code committed with descriptive commit messages
- Clean git history without merge commits
- Ready for production deployment
- All tests passing
- All linting checks passing

---

## 10. Success Metrics

**Frontend Success:**

- ✅ Application loads without errors
- ✅ All tabs and panels accessible
- ✅ File operations (upload/download/delete) working end-to-end
- ✅ Both themes functional and user-selectable
- ✅ Application responsive (320px - 1920px)
- ✅ All linting checks passing
- ✅ Unit tests > 80% coverage
- ✅ Lighthouse score > 85
- ✅ Backend API communication working

**Backend Success:**

- ✅ All REST endpoints functional
- ✅ File operations working correctly
- ✅ Document processing endpoints functional
- ✅ Error handling comprehensive
- ✅ Unit tests > 80% coverage
- ✅ Integration tests passing
- ✅ /api/health endpoint responding
- ✅ Swagger documentation complete

**Docker Success:**

- ✅ Frontend image < 100MB
- ✅ Backend image < 200MB
- ✅ `docker compose up --build` successful
- ✅ All services communicating correctly
- ✅ Health checks passing
- ✅ Application fully accessible
- ✅ Logs accessible and readable

**Overall Project Success:**

- ✅ Full-stack application fully functional
- ✅ All acceptance criteria met
- ✅ Code quality excellent (linting, formatting, tests)
- ✅ Documentation complete and clear
- ✅ Ready for immediate production deployment

---

## 11. Timeline and Milestones

- **Phase 1 (Day 1):** Project setup (frontend, backend, Docker base)
- **Phase 2 (Day 1-2):** Core component development, API endpoint scaffolding
- **Phase 3 (Day 2-3):** Feature implementation (tabs, file management, theme)
- **Phase 4 (Day 3-4):** Frontend-backend integration and testing
- **Phase 5 (Day 4-5):** Docker configuration and optimization
- **Phase 6 (Day 5):** Full-stack testing and refinement
- **Phase 7 (Day 5-6):** Documentation and deployment readiness

---

## 12. Constraints and Assumptions

**Hard Constraints:**

- Must use PrimeReact for UI components
- Must use Java 25 for backend
- Must be containerized with Docker Compose
- Tab interface must be mutually exclusive (only one visible)
- File History panel must always be visible
- Minimum 80% test coverage (frontend and backend)

**Technical Constraints:**

- Node.js 18+ required for frontend
- Java 25 required for backend
- Docker 20.10+ required
- All services must communicate over HTTP/REST
- No legacy browser support

**Assumptions:**

- Development environment has Node.js 18+, Java 25 JDK, Docker installed
- Backend runs on localhost:8080
- Frontend runs on localhost:3000 (dev) or :80 (prod)
- Internet access available for package downloads
- No user authentication in MVP (future enhancement)

---

## 13. Notes for the Developer (Copilot Agent)

### Development Standards

- Follow instruction files:
  - Performance optimization (frontend < 3s, API < 500ms)
  - Self-explanatory code commenting (WHY not WHAT)
  - ReactJS best practices (hooks, functional components)
  - Spring Boot best practices
  - Docker optimization guidelines
  - Spec-driven workflow methodology

### Code Quality

- Clean, maintainable code over clever solutions
- Modular, reusable components
- Consistent naming conventions (camelCase/PascalCase)
- No console errors or warnings
- All linting checks passing
- Comprehensive error handling

### Testing

- 80%+ coverage for both frontend and backend
- Unit tests for components/services
- Integration tests for API endpoints
- Test cross-browser compatibility

### Documentation

- Table of Contents for all docs > 100 lines
- Comments on non-obvious business logic
- API documentation complete
- Environment variable documentation

### Git Workflow

- Clear, descriptive commit messages
- Logical commit structure
- Clean history (no merge commits)
- All tests passing before commits

---

## 14. Approval and Sign-off

**Project Owner:** Pete Letkeman
**Technical Lead:** Pete Letkeman
**Date Created:** January 16, 2026
**Status:** Ready for Implementation

**Sign-off:**

- [x ] Product Owner approved
- [x ] Technical requirements reviewed
- [x ] Architecture reviewed
- [x ] Timeline agreed upon

---

**Questions or clarifications?** Refer to this PRD before development. All acceptance criteria must be met before delivery.

**Last Review:** January 16, 2026
**Next Review:** After Phase 3 completion
