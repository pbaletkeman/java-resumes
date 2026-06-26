# java-resumes Feature Inventory

## Overview
This project is a full-stack AI-powered resume and career toolkit built with:
- Backend: Java 21, Spring Boot 3.5.1
- Frontend: React 19, TypeScript, PrimeReact, Tailwind
- AI / LLM integration via HTTP-compatible endpoint
- Dockerized deployment with multiple compose options

---

## Core Application Features

### 1. Resume & Cover Letter Optimization
- Upload or paste:
  - resume content
  - job description
- Generate AI-enhanced resume optimization
- Generate AI-generated cover letters
- Supports multiple prompt output types in one form
- Handles text input and file upload modes

### 2. Skills & Certifications Recommendations
- Generate skills and certification suggestions
- Uses job description plus optional resume context
- Produces personalized learning/career growth guidance

### 3. Interview Preparation
- Generate HR interview questions
- Generate job-specific interview questions
- Generate reverse interview questions for candidates to ask interviewers

### 4. Professional Networking / Outreach
- Generate cold outreach email templates
- Generate LinkedIn connection message templates
- Generate thank-you email templates

### 5. File Processing & Conversion
- Convert uploaded Markdown files to PDF:
  - Endpoint: `/api/markdownFile2PDF`
- Convert uploaded Markdown files to DOCX:
  - Endpoint: `/api/markdownFile2DOCX`
- File uploads are saved to an upload directory
- File listing, download, delete APIs supported

### 6. File Management
- List uploaded/generated files:
  - Endpoint: `/api/files`
- Download file through controller resource mapping:
  - Endpoint: `/api/files/{filename}`
- Delete stored file:
  - Endpoint: `/api/files/{filename}` with DELETE
- Frontend includes file filtering, sorting, selection, and bulk deletion

### 7. Health & Diagnostics
- Health summary endpoint:
  - `/api/health`
- Database connectivity check:
  - `/api/health/database`
- LLM connectivity check:
  - `/api/health/llm`
- Disk health check:
  - `/api/health/disk`
- Backend health data includes overall status and component statuses

---

## Supported API Endpoints

### Document & AI Processing
- `POST /api/upload`
- `POST /api/process/resume`
- `POST /api/process/cover-letter`
- `POST /api/process/skills`

### Interview Generation
- `POST /api/generate/interview-hr-questions`
- `POST /api/generate/interview-job-specific`
- `POST /api/generate/interview-reverse`

### Networking Content Generation
- `POST /api/generate/cold-email`
- `POST /api/generate/cold-linkedin-message`
- `POST /api/generate/thank-you-email`

### File Conversion
- `POST /api/markdownFile2PDF`
- `POST /api/markdownFile2DOCX`

### File Management
- `GET /api/files`
- `GET /api/files/{filename}`
- `DELETE /api/files/{filename}`

### Health
- `GET /api/health`
- `GET /api/health/database`
- `GET /api/health/llm`
- `GET /api/health/disk`

---

## Backend Feature Details

### Prompt / LLM Integration
- AI prompt engine built around `Optimize` model
- Supports prompt types:
  - `resume`
  - `cover`
  - `skills`
  - `interview-hr-questions`
  - `interview-job-specific`
  - `interview-reverse`
  - `cold-email`
  - `cold-linkedin-message`
  - `thank-you-email`
- Prompt content is loaded from `prompts/` directory via `PromptService`
- External prompt override supported via config property `prompts.external-dir`
- `ApiService` supports:
  - HTTP LLM calls
  - mock mode for testing
  - prompt substitution variables:
    - `{resume_string}`
    - `{job_description}`
    - `{job_title}`
    - `{company}`
    - `{today}`
    - `{interviewer_name}`

### Background Processing
- Asynchronous generation via `BackgroundResume` thread
- Reads config from `config.json` or `app.config.path`
- Supports environment overrides:
  - `LLM_ENDPOINT`
  - `LLM_APIKEY`
  - `LLM_MODEL`

### File Storage & Uploads
- File storage service with:
  - upload save
  - load resource
  - delete
  - list all files
  - delete all
- Upload root configurable via `upload.path`

### Request Validation & Input Handling
- `Optimize.isValid()` enforces required fields:
  - job description
  - company
  - job title
  - model settings
  - valid prompt type
  - resume required for all outputs except skills/interview/networking
- Supports fallback of uploaded file content into optimize payload

### Health Checks
- Database type detection and connectivity test:
  - SQLite
  - PostgreSQL
  - MySQL
- LLM endpoint connectivity verification
- Disk write/read health on upload directory

---

## Frontend/User-Facing Features

### Main UI
- Tabbed interface:
  - Main Content
  - Files
  - Additional Tools
  - Settings
- Input modes:
  - Paste text
  - Upload files
- Validation for required fields
- Model selection dropdown
- Multiple output type selector
- Loading spinner and submission dialog UI

### Document Input Features
- Job description input
- Resume input
- Job title and company fields
- Optional interviewer name
- File upload support for:
  - PDF
  - DOC
  - DOCX
  - TXT
  - Markdown

### Output Actions
- Optimize Resume
- Generate Cover Letter
- Generate Skills & Certifications
- Generate HR Interview Questions
- Generate Job-Specific Interview Questions
- Generate Reverse Interview Questions
- Generate Cold Email
- Generate LinkedIn Message
- Generate Thank You Email

### File Management UI
- File listing and auto-refresh
- Filtering by type:
  - All
  - Cover letters
  - Resumes
  - Skills plans
- Sorting by date or filename
- Download files
- Delete selected files
- Bulk file selection

### Settings & Theme
- Theme support:
  - Light theme
  - Dark theme
- Persisted theme preference
- Model selection saved in UI storage
- Frontend uses environment variable `VITE_API_BASE_URL`

---

## Prompt Files Found
- `prompts/RESUME.md`
- `prompts/COVER.md`
- `prompts/SKILLS.md`
- `prompts/README.md`
- `prompts/more-prompts.md`

---

## Notable Project Capabilities
- Docker-first architecture with multiple compose configs
- Frontend and backend run separately, integrated through REST API
- Prompt customization via external directories
- Mock LLM support for local or test mode
- File generation and export workflow
- Persistent health and diagnostics endpoints
- Adaptive prompt selection via frontend output controls
