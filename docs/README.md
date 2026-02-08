# Java Resumes - Documentation

Welcome to Java Resumes - an AI-powered full-stack resume and cover letter optimization application.

This documentation is organized into focused sections to make it easy to find what you need.

- [Java Resumes - Documentation](#java-resumes---documentation)
  - [📚 Documentation Index](#-documentation-index)
    - [Getting Started](#getting-started)
    - [Core Documentation](#core-documentation)
    - [Development \& Deployment](#development--deployment)
    - [Testing \& Quality](#testing--quality)
    - [Troubleshooting \& Reference](#troubleshooting--reference)
  - [🔍 Quick Navigation](#-quick-navigation)
  - [📋 Project Overview](#-project-overview)
    - [Key Features](#key-features)
    - [Technology Stack](#technology-stack)
  - [🚀 Getting Started](#-getting-started)
    - [Fastest Way (Docker)](#fastest-way-docker)
    - [Development Setup](#development-setup)
  - [⚙️ External Configuration](#️-external-configuration)
    - [External Config Path](#external-config-path)
    - [External Prompts Directory](#external-prompts-directory)
  - [📂 Documentation Structure](#-documentation-structure)
  - [📖 How to Use This Documentation](#-how-to-use-this-documentation)
  - [🔗 Important Links](#-important-links)
    - [Technology Documentation](#technology-documentation)
  - [💡 Tips for Success](#-tips-for-success)
    - [For Docker Users](#for-docker-users)
    - [For Development](#for-development)
    - [For Production](#for-production)
  - [📝 Contributing to Documentation](#-contributing-to-documentation)
  - [🆘 Need Help?](#-need-help)

---

## 📚 Documentation Index

### Getting Started

- **[QUICK_START.md](QUICK_START.md)** - Get up and running in minutes with Docker Compose
- **[CONFIGURATION.md](CONFIGURATION.md)** - Configure external config paths and LLM services

### Core Documentation

- **[ARCHITECTURE.md](ARCHITECTURE.md)** - System design, component overview, and data flow
- **[FEATURES.md](FEATURES.md)** - Complete feature list and capabilities
- **[TECHNOLOGY_STACK.md](TECHNOLOGY_STACK.md)** - Technologies used and versions

### Development & Deployment

- **[DEVELOPMENT_SETUP.md](DEVELOPMENT_SETUP.md)** - Backend and frontend development setup
- **[PRODUCTION_DEPLOYMENT.md](PRODUCTION_DEPLOYMENT.md)** - Production deployment and environment configuration
- **[API_REFERENCE.md](API_REFERENCE.md)** - Complete REST API documentation and endpoints

### Testing & Quality

- **[TESTING.md](TESTING.md)** - Testing strategies and running tests
- **[CODE_QUALITY.md](CODE_QUALITY.md)** - Code quality standards and checks
- **[Git Hooks Setup](git-hooks/README_GIT_HOOKS.md)** - Automated code quality enforcement with Spotless, Checkstyle, and SpotBugs

### Troubleshooting & Reference

- **[TROUBLESHOOTING.md](TROUBLESHOOTING.md)** - Common issues and solutions
- **[ENVIRONMENT_VARIABLES.md](ENVIRONMENT_VARIABLES.md)** - Complete environment variable reference

---

## 🔍 Quick Navigation

| Need                               | Go To                                                |
| ---------------------------------- | ---------------------------------------------------- |
| Start the app immediately          | [QUICK_START.md](QUICK_START.md)                     |
| Configure external config/prompts  | [CONFIGURATION.md](CONFIGURATION.md)                 |
| Understand the system architecture | [ARCHITECTURE.md](ARCHITECTURE.md)                   |
| Setup development environment      | [DEVELOPMENT_SETUP.md](DEVELOPMENT_SETUP.md)         |
| Deploy to production               | [PRODUCTION_DEPLOYMENT.md](PRODUCTION_DEPLOYMENT.md) |
| See all API endpoints              | [API_REFERENCE.md](API_REFERENCE.md)                 |
| Fix a problem                      | [TROUBLESHOOTING.md](TROUBLESHOOTING.md)             |
| Find environment variable          | [ENVIRONMENT_VARIABLES.md](ENVIRONMENT_VARIABLES.md) |
| Learn testing approach             | [TESTING.md](TESTING.md)                             |
| Setup code quality hooks           | [Git Hooks](git-hooks/README_GIT_HOOKS.md)           |

---

## 📋 Project Overview

**Java Resumes** helps job seekers create optimized, tailored resumes and cover letters using AI-powered Large Language Models.

### Key Features

- AI-powered resume optimization for specific job descriptions
- Automated cover letter generation
- Markdown to PDF/DOCX conversion
- File management and download
- Light and dark theme support
- Full Docker containerization
- External configuration support

### Technology Stack

- **Frontend:** React 19, TypeScript, Vite, Tailwind CSS, PrimeReact
- **Backend:** Spring Boot 3.5.1, Java 21 LTS, Gradle
- **LLM Integration:** Ollama, LM Studio, or OpenAI API
- **Deployment:** Docker, Docker Compose, Nginx

---

## 🚀 Getting Started

### Fastest Way (Docker)

```bash
# 1. Clone the repository
git clone https://github.com/pbaletkeman/java-resumes.git
cd java-resumes

# 2. Configure (optional - see CONFIGURATION.md)
# Edit config.json for LLM settings

# 3. Start everything
docker compose up --build

# 4. Access the application
# Frontend: http://localhost
# API Docs: http://localhost:8080/swagger-ui/index.html
```

**For detailed setup:** [QUICK_START.md](QUICK_START.md)

### Development Setup

For backend or frontend development, see:

- Backend: [DEVELOPMENT_SETUP.md#backend-development](DEVELOPMENT_SETUP.md#-backend-development)
- Frontend: [DEVELOPMENT_SETUP.md#frontend-development](DEVELOPMENT_SETUP.md#-frontend-development)

---

## ⚙️ External Configuration

The application supports external configuration for production deployments.

### External Config Path

Set the config.json location via system property:

```bash
java -jar app.jar -Dapp.config.path=/etc/java-resumes
```

Or as environment variable:

```bash
export CONFIG_PATH=/etc/java-resumes
java -jar app.jar
```

### External Prompts Directory

Set custom prompt templates:

```bash
# In application.yml
prompts:
  external-dir: /etc/java-resumes/prompts

# Or via environment variable
export PROMPTS_DIR=/etc/java-resumes/prompts
```

**Learn more:** [CONFIGURATION.md](CONFIGURATION.md)

---

## 📂 Documentation Structure

```plaintext
docs/
 README.md                          # This file (navigation hub)
 QUICK_START.md                     # Get started in minutes
 CONFIGURATION.md                   # External config & LLM setup
 ARCHITECTURE.md                    # System design & diagrams
 FEATURES.md                        # Complete feature list
 TECHNOLOGY_STACK.md                # Technologies & versions
 DEVELOPMENT_SETUP.md               # Dev environment setup
 PRODUCTION_DEPLOYMENT.md           # Deployment guide
 API_REFERENCE.md                   # REST API docs
 TESTING.md                         # Testing strategy
 CODE_QUALITY.md                    # Quality standards
 TROUBLESHOOTING.md                 # Problem solving
 ENVIRONMENT_VARIABLES.md           # All environment vars
 screenshots/                       # Visual documentation
     frontend/                      # UI screenshots
     backend/                       # API & architecture diagrams
     SCREENSHOTS_GUIDE.md           # How to capture screenshots
```

---

## 📖 How to Use This Documentation

1. **New to the project?** Start with [QUICK_START.md](QUICK_START.md)
2. **Need to configure something?** Check [CONFIGURATION.md](CONFIGURATION.md)
3. **Understanding the system?** Read [ARCHITECTURE.md](ARCHITECTURE.md)
4. **Setting up for development?** Follow [DEVELOPMENT_SETUP.md](DEVELOPMENT_SETUP.md)
5. **Deploying to production?** See [PRODUCTION_DEPLOYMENT.md](PRODUCTION_DEPLOYMENT.md)
6. **Running into issues?** Check [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

---

## 🔗 Important Links

- **GitHub Repository:** <https://github.com/pbaletkeman/java-resumes>
- **Issues & Support:** <https://github.com/pbaletkeman/java-resumes/issues>
- **Discussions:** <https://github.com/pbaletkeman/java-resumes/discussions>

### Technology Documentation

- [React Documentation](https://react.dev)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Vite Documentation](https://vitejs.dev)
- [Docker Documentation](https://docs.docker.com)
- [Ollama Documentation](https://ollama.ai)

---

## 💡 Tips for Success

### For Docker Users

- Use `docker compose logs -f` to troubleshoot
- Use `-v` flag to preserve volumes between restarts
- Set `CONFIG_PATH` before startup for external config

### For Development

- Backend changes require rebuild: `./gradlew build`
- Frontend hot reload works with `npm run dev`
- Run tests frequently: `./gradlew test` and `npm test`

### For Production

- Always use environment-specific config
- Set external `CONFIG_PATH` for config management
- Use external `PROMPTS_DIR` to customize LLM prompts
- Enable HTTPS/TLS in production
- Monitor logs regularly

---

## 📝 Contributing to Documentation

When updating documentation:

1. Keep files under 750 lines
2. Link to related documents
3. Use consistent formatting
4. Include examples and code snippets
5. Update this README if adding new sections

---

## 🆘 Need Help?

- **Documentation Issues?** Check [TROUBLESHOOTING.md](TROUBLESHOOTING.md)
- **Bug Reports?** Open a GitHub issue
- **Questions?** Use GitHub Discussions
- **Configuration Help?** See [CONFIGURATION.md](CONFIGURATION.md)

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
**Version:** 2.0 (External Configuration Support)
