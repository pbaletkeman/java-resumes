# Documentation Index

Complete documentation and reference guide for the java-resumes project.

- [Documentation Index](#documentation-index)
  - [📗 Quick Navigation](#quick-navigation)
    - [By Role](#by-role)
  - [🚀 Getting Started](#getting-started)
  - [📂 Project Overview](#project-overview)
  - [💻 Development Guides](#development-guides)
  - [🔕 Technical Reference](#technical-reference)
  - [🔬 Session \& Testing](#session--testing)
  - [📋 Issue Tracking \& Fixes](#issue-tracking--fixes)
  - [🇽️ Visual Documentation](#visual-documentation)
  - [📝 Related Documentation](#related-documentation)
  - [📄 Project Structure](#project-structure)
  - [🛠️ Technology Stack](#technology-stack)
    - [Backend](#backend)
    - [Frontend](#frontend)
  - [📅 Documentation Categories](#documentation-categories)
    - [Strategic Documents](#strategic-documents)
    - [Development Guides](#development-guides-1)
    - [⚙️ Configuration \& Quality](#configuration--quality)
    - [🛱 Work-in-Progress](#work-in-progress)
  - [📓 How to Use This Documentation](#how-to-use-this-documentation)
  - [🖫 Important Files](#important-files)
    - [Configuration Files](#configuration-files)
    - [Root Documentation](#root-documentation)
  - [🖯 Navigation](#navigation)

---

## 🔍 Quick Navigation

### By Role

**Project Managers**

- Start [Project Status](PROJECT_STATUS.md)
- Then [Job Completion Summary](JOB_COMPLETION_SUMMARY.md)
- Check [Outstanding Issues](OUTSTANDING_ISSUES.md)

**Developers**

- Start [Quick Start Advanced](QUICK_START_ADVANCED.md)
- Reference [Quick Reference](QUICK_REFERENCE.md)
- Study [AI Agent Guidelines](AGENTS.md)
- Check [Technical Checklist](TECHNICAL_CHECKLIST.md)

**AI Agents (Copilot, Claude)**

- Study [Copilot Instructions](copilot-instructions.md)
- Reference [AI Agent Guidelines](AGENTS.md)
- Use [Quick Reference](QUICK_REFERENCE.md)

**QA/Testers**

- Review [Testing Guide](OUTPUT_TYPE_TESTING_GUIDE.md)
- Reference [Screenshot Capture Summary](SCREENSHOT_CAPTURE_SUMMARY.md)

---

## 🚀 Getting Started

Start here if you're new to the project:

- **[Quick Start Advanced](QUICK_START_ADVANCED.md)** - Setup and configuration guide for development environment
- **[Quick Reference](QUICK_REFERENCE.md)** - Quick lookup for common tasks, commands, and workflows
- **[Root README](../README.md)** - Main project README (in repository root)

---

## 📋 Project Overview

Understand the project structure, status, and architecture:

- **[Project Status](PROJECT_STATUS.md)** - Current project status, milestones, and progress tracking
- **[Implementation Summary](IMPLEMENTATION_SUMMARY.md)** - Overview of completed implementations and features
- **[Implementation Complete](IMPLEMENTATION_COMPLETE.md)** - Final implementation details and completion status
- **[Job Completion Summary](JOB_COMPLETION_SUMMARY.md)** - Summary of all completed work and deliverables
- **[AI Agent Guidelines](AGENTS.md)** - Guidelines and best practices for AI agents and assistants
- **[Non-Technical Model Guide](NONTECHNICAL_MODEL_GUIDE.md)** - Guide for non-technical stakeholders and AI models

---

## 📚 Development Guides

Detailed guides for development and feature implementation:

- **[Frontend Enhancements](FRONTEND_ENHANCEMENTS.md)** - Frontend improvements, new features, and component documentation
- **[Copilot Instructions](copilot-instructions.md)** - Repository-wide instructions and guidelines for GitHub Copilot

---

## 📖 Technical Reference

Technical documentation and reference materials:

- **[Technical Checklist](TECHNICAL_CHECKLIST.md)** - Technical requirements, acceptance criteria, and verification checklist
- **[Screenshot Capture Summary](SCREENSHOT_CAPTURE_SUMMARY.md)** - Documentation screenshot capture results and metadata

---

## 💬 Session & Testing

Session logs, testing guides, and quality assurance documentation:

- **[Testing Guide](OUTPUT_TYPE_TESTING_GUIDE.md)** - Comprehensive testing procedures and validation guidelines
- **[Session Log](SESSION_LOG.md)** - Development session log, activities, and timeline

---

## 🐛 Issue Tracking & Fixes

Documentation related to bug fixes, issues, and resolutions:

- **[Outstanding Issues](OUTSTANDING_ISSUES.md)** - Current unresolved issues, blockers, and next action items
- **[Bug Fix: Output Type](BUG_FIX_OUTPUT_TYPE.md)** - Detailed documentation of output type dropdown bug fix
- **[Output Type Fix Quick Reference](OUTPUT_TYPE_FIX_QUICK_REFERENCE.md)** - Quick reference guide for output type fixes and implementations

---

## 📸 Visual Documentation

Screenshots and visual guides for the application:

- **[UI Screenshots - Theme Showcase](screenshots/UI_SCREENSHOTS.md)** - Dark and light theme UI variants for all application tabs
- **[Setup Screenshots Guide](screenshots/setup/README.md)** - Docker setup, build process, and deployment screenshots
- **[Architecture Diagrams](screenshots/architecture/)** - System architecture, UML diagrams, and data flow visualizations
- **[API Documentation Screenshots](screenshots/api/)** - Swagger UI, API endpoints, and error response examples

---

## 🔗 Related Documentation

Additional resources and directories:

- **[Repository Root](../README.md)** - Main project README and overview
- **[Screenshots Directory](screenshots/)** - All captured screenshots for visual documentation
- **[Backend README](./BACKEND_README.md)** - Backend setup and API documentation
- **[Frontend README](../frontend/README.md)** - Frontend setup and component documentation
- **[Backend Custom Instructions](../.github/instructions/backend.instructions.md)** - Copilot backend guidance
- **[Frontend Custom Instructions](../.github/instructions/frontend.instructions.md)** - Copilot frontend guidance

---

## 📂 Project Structure

```
java-resumes/
 .github/
    instructions/
        backend.instructions.md     # Backend development guidance
        frontend.instructions.md    # Frontend development guidance
 src/main/java/                     # Backend source code
 src/test/java/                     # Backend tests
 frontend/                          # Frontend React application
 docs/                              # Documentation
    INDEX.md                        # This file
    Architecture.md                 # System architecture
    README.md                       # Frontend documentation
    BACKEND_README.md               # Backend documentation
    wip/                            # Work-in-progress documentation
       STATUS.md                    # Current status
       IMPLEMENTATION_SUMMARY.md    # Implementation overview
       ...                          # Other WIP documents
    ...                             # Other documentation files
 copilot-instructions.md            # Repository-wide Copilot guidance
 README.md                          # Main project README

```

## 🛠️ Technology Stack

### Backend

- **Language**: Java 21 LTS (Eclipse Temurin)
- **Framework**: Spring Boot 3.5.1
- **Build Tool**: Gradle 8.7
- **Testing**: JUnit 5, Mockito
- **Code Quality**: Checkstyle 10.14.2

### Frontend

- **Framework**: React 19.2.0
- **Language**: TypeScript 5.9.3
- **Build Tool**: Vite 7.2.4
- **UI Components**: PrimeReact 10.9.7
- **Styling**: Tailwind CSS 4.1.18
- **Testing**: Vitest 4.0.17

## 📑 Documentation Categories

### Strategic Documents

- **Architecture** - System design and component relationships
- **Product Requirements** - Features and requirements (PRD)
- **Root README** - Project overview and quick start

### Development Guides

- **Backend README** - Backend development setup and patterns
- **Frontend README** - Frontend development setup and patterns
- **Custom Instructions** - Path-specific guidance for Copilot agents

### Configuration & Quality

- **Checkstyle** - Code quality standards and compliance
- **Git Configuration** - Encoding and line ending consistency (`.gitattributes`)

### Work-in-Progress

- **Status** - Current project status and milestones
- **Implementation Summaries** - Backend and frontend implementation details
- **Progress Tracking** - Code quality and setup progress

## 📖 How to Use This Documentation

1. **Getting Started?** Start with [README](../README.md) and [Architecture](./Architecture.md)
2. **Backend Development?** Read [Backend README](./BACKEND_README.md) and check [Backend Custom Instructions](../.github/instructions/backend.instructions.md)
3. **Frontend Development?** Read [Frontend README](./README.md) and check [Frontend Custom Instructions](../.github/instructions/frontend.instructions.md)
4. **Checking Status?** See [STATUS](./wip/STATUS.md) and implementation summaries
5. **Understanding Code Quality?** Review [Checkstyle Configuration](./CHECKSTYLE_AND_OLLAMA.md)

## 📋 Important Files

### Configuration Files

- `.gitattributes` - Git encoding and line ending configuration
- `.github/instructions/backend.instructions.md` - Backend Copilot guidance
- `.github/instructions/frontend.instructions.md` - Frontend Copilot guidance

### Root Documentation

- `README.md` - Main project readme
- `copilot-instructions.md` - Repository-wide Copilot guidance

## 🧭 Navigation

- [Up to Root](../README.md)
- [Backend Instructions](../.github/instructions/backend.instructions.md)
- [Frontend Instructions](../.github/instructions/frontend.instructions.md)
- [Architecture Details](./Architecture.md)

---

For questions or updates, refer to specific documentation sections or contact the development team.

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
