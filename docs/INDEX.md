# Documentation Index

Complete documentation and reference guide for the java-resumes project.

## Quick Links

### Getting Started

- [Root README](../README.md) - Project overview and quick start
- [Architecture](./Architecture.md) - System architecture and design

### Backend Development

- [Backend README](./BACKEND_README.md) - Backend development guide
- [Backend Custom Instructions](../.github/instructions/backend.instructions.md) - Copilot backend guidance

### Frontend Development

- [Frontend README](./README.md) - Frontend development guide
- [Frontend Custom Instructions](../.github/instructions/frontend.instructions.md) - Copilot frontend guidance

### Product & Requirements

- [PRD v2 (Latest)](./PRD-PRIMEREACT-DOCKER-v2.md) - Product requirements document (latest version)
- [PRD v1](./PRD-PRIMEREACT-DOCKER.md) - Product requirements document (previous version)

### Configuration & Infrastructure

- [Checkstyle Configuration](./CHECKSTYLE_AND_OLLAMA.md) - Code quality and Ollama integration
- [Checkstyle Compliance Report](./CHECKSTYLE_COMPLIANCE_REPORT.md) - Code quality compliance status

### Work in Progress

- [STATUS](./wip/STATUS.md) - Current project status
- [Implementation Summary](./wip/IMPLEMENTATION_SUMMARY.md) - Frontend/backend implementation overview
- [Frontend Implementation](./wip/FRONTEND_IMPLEMENTATION_SUMMARY.md) - Frontend implementation details
- [Java 17 Setup](./wip/JAVA17_SETUP.md) - Java 17 LTS setup and configuration
- [Checkstyle Progress](./wip/CHECKSTYLE_PROGRESS.md) - Code quality progress tracking
- [Migration Summary](./MIGRATION_SUMMARY.md) - Migration and refactoring summary

### Reference

- [Documentation Summary](./DOCUMENTATION_SUMMARY.md) - Summary of all documentation
- [Documentation Index](./DOCUMENTATION_INDEX.md) - Alternative documentation index

## Project Structure

```
java-resumes/
├── .github/
│   └── instructions/
│       ├── backend.instructions.md    # Backend development guidance
│       └── frontend.instructions.md   # Frontend development guidance
├── src/main/java/                     # Backend source code
├── src/test/java/                     # Backend tests
├── frontend/                          # Frontend React application
├── docs/                              # Documentation
│   ├── INDEX.md                       # This file
│   ├── Architecture.md                # System architecture
│   ├── README.md                      # Frontend documentation
│   ├── BACKEND_README.md              # Backend documentation
│   ├── wip/                           # Work-in-progress documentation
│   │   ├── STATUS.md                  # Current status
│   │   ├── IMPLEMENTATION_SUMMARY.md  # Implementation overview
│   │   └── ...                        # Other WIP documents
│   └── ...                            # Other documentation files
├── copilot-instructions.md            # Repository-wide Copilot guidance
└── README.md                          # Main project README

```

## Technology Stack

### Backend

- **Language**: Java 17 LTS (Corretto)
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

## Documentation Categories

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

## How to Use This Documentation

1. **Getting Started?** Start with [README](../README.md) and [Architecture](./Architecture.md)
2. **Backend Development?** Read [Backend README](./BACKEND_README.md) and check [Backend Custom Instructions](../.github/instructions/backend.instructions.md)
3. **Frontend Development?** Read [Frontend README](./README.md) and check [Frontend Custom Instructions](../.github/instructions/frontend.instructions.md)
4. **Checking Status?** See [STATUS](./wip/STATUS.md) and implementation summaries
5. **Understanding Code Quality?** Review [Checkstyle Configuration](./CHECKSTYLE_AND_OLLAMA.md) and [Compliance Report](./CHECKSTYLE_COMPLIANCE_REPORT.md)

## Important Files

### Configuration Files

- `.gitattributes` - Git encoding and line ending configuration
- `.github/instructions/backend.instructions.md` - Backend Copilot guidance
- `.github/instructions/frontend.instructions.md` - Frontend Copilot guidance

### Root Documentation

- `README.md` - Main project readme
- `copilot-instructions.md` - Repository-wide Copilot guidance

## Navigation

- [Up to Root](../README.md)
- [Backend Instructions](../.github/instructions/backend.instructions.md)
- [Frontend Instructions](../.github/instructions/frontend.instructions.md)
- [Architecture Details](./Architecture.md)

---

For questions or updates, refer to specific documentation sections or contact the development team.

Last Updated: 2026 (documentation organization completed)
