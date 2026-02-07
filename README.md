# Java Resumes - Full-Stack Resume Optimization Application

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://openjdk.java.net/) | [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.1-brightgreen.svg)](https://spring.io/projects/spring-boot) | [![React](https://img.shields.io/badge/React-19.2.0-blue.svg)](https://reactjs.org/) | [![Node.js](https://img.shields.io/badge/Node.js-22%20LTS-339933.svg)](https://nodejs.org/) | [![TypeScript](https://img.shields.io/badge/TypeScript-5.9.3-blue.svg)](https://www.typescriptlang.org/) | [![Docker](https://img.shields.io/badge/Docker-Compose-2496ED.svg)](https://www.docker.com/) | [![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

AI-powered full-stack application for resume and cover letter optimization using Large Language Models. Built with Spring Boot 3.5.1, React 19, TypeScript, and Docker for seamless deployment.

**📖 [Full Documentation](docs/README.md)** | Supports external config paths, custom prompts, and multiple LLM providers

---

- [Java Resumes - Full-Stack Resume Optimization Application](#java-resumes---full-stack-resume-optimization-application)
  - [🚀 Quick Start](#-quick-start)
  - [📚 Documentation Index](#-documentation-index)
  - [✨ What It Does](#-what-it-does)
    - [Core Features](#core-features)
    - [How It Works](#how-it-works)
  - [🏗️ Architecture](#️-architecture)
  - [🛠️ Technology Stack](#️-technology-stack)
  - [📂 Project Structure](#-project-structure)
  - [📡 API Endpoints](#-api-endpoints)
    - [Core Document Processing](#core-document-processing)
    - [Interview Preparation _(New)_](#interview-preparation-new)
    - [Professional Networking _(New)_](#professional-networking-new)
    - [File Management](#file-management)
  - [🔧 Development Setup](#-development-setup)
    - [Backend](#backend)
    - [Frontend](#frontend)
  - [🧪 Testing](#-testing)
  - [🔐 Configuration](#-configuration)
    - [LLM Setup](#llm-setup)
  - [🐛 Troubleshooting](#-troubleshooting)
  - [🚀 Production Deployment](#-production-deployment)
    - [Docker Compose (Recommended)](#docker-compose-recommended)
    - [Kubernetes / Cloud](#kubernetes--cloud)
  - [🤝 Contributing](#-contributing)
  - [📄 License](#-license)
  - [📞 Support \& Resources](#-support--resources)
    - [Made with ❤️ using Java, Spring Boot, React, and AI](#made-with-️-using-java-spring-boot-react-and-ai)

---

## 🚀 Quick Start

### Docker Compose (Recommended - 3 Steps)

```bash
git clone https://github.com/pbaletkeman/java-resumes.git
cd java-resumes

# Step 1: Start all services (Frontend, Backend, Ollama LLM)
docker compose up -d

# Step 2: Pull an LLM model (e.g., tinyllama)
docker exec resume-ollama ollama pull tinyllama

# Step 3: Access application
# Frontend: http://localhost:3000
# Backend: http://localhost:8080
# Ollama: http://localhost:11434
```

**Available Compose Files:**

- `docker-compose.yml` - Main configuration with all services
- `docker-compose.frontend-backend.yml` - Alternative configuration (same services, different file structure)

**→ [Docker Compose Quick Start](docs/DOCKER_COMPOSE_QUICK_START.md) | [Detailed Setup Guide](docs/QUICK_START.md)**

---

## 📚 Documentation Index

| Topic             | Doc                                                  | Topic             | Doc                                                     |
| ----------------- | ---------------------------------------------------- | ----------------- | ------------------------------------------------------- |
| **Setup**         | [Quick Start](docs/QUICK_START.md)                   | **API**           | [API Reference](docs/API_REFERENCE.md)                  |
| **Docker**        | [Docker Compose](docs/DOCKER_COMPOSE_QUICK_START.md) | **Docker Detail** | [Docker Setup](docs/DOCKER_SETUP.md)                    |
| **Architecture**  | [System Design](docs/ARCHITECTURE.md)                | **Testing**       | [Test Guide](docs/TESTING.md)                           |
| **Configuration** | [Config Guide](docs/CONFIGURATION.md)                | **Code Quality**  | [Git Hooks](docs/git-hooks/README_GIT_HOOKS.md)         |
| **Development**   | [Dev Setup](docs/DEVELOPMENT_SETUP.md)               | **Issues**        | [Troubleshooting](docs/TROUBLESHOOTING.md)              |
| **Deployment**    | [Deploy Guide](docs/PRODUCTION_DEPLOYMENT.md)        | **Env Vars**      | [Configuration](docs/ENVIRONMENT_VARIABLES.md)          |
| **Ollama LLM**    | [Ollama Guide](docs/OLLAMA_SETUP.md)                 | **GitHub CI**     | [GitHub Environment](docs/GITHUB_ENVIRONMENT_OLLAMA.md) |

---

## ✨ What It Does

The application provides comprehensive AI-powered career optimization tools:

### Core Features

**📄 Resume & Cover Letter Optimization**

- Submit a job description + resume → Get back an AI-optimized resume and/or cover letter tailored to that specific job opening
- ATS (Applicant Tracking System) optimization
- Keyword matching and relevance scoring

**🎯 Skills Development Planning**

- Get personalized recommendations for certifications, skills, and hands-on experiences
- Receive a structured learning path tailored to your target role
- Understand time investment and priorities for career advancement

**💼 Interview Preparation** _(New)_

- **HR Interview Questions**: Get 5 general HR questions to prepare for behavioral interviews
- **Job-Specific Questions**: Receive 5 technical/functional questions based on the job description
- **Reverse Interview Questions**: Get thoughtful questions to ask your interviewers

**🤝 Professional Networking** _(New)_

- **Cold Email Templates**: Generate 5 variations of professional cold outreach emails
- **LinkedIn Messages**: Create 5 distinct LinkedIn connection request messages
- **Thank You Emails**: Get 5 variations of post-interview thank you emails

### How It Works

**Features:**

- ✅ AI-powered resume optimization
- ✅ Cover letter generation
- ✅ Skills & certifications recommendations
- ✅ Interview preparation (HR, job-specific, reverse questions) _(New)_
- ✅ Professional networking (cold emails, LinkedIn, thank you notes) _(New)_
- ✅ Markdown to PDF conversion
- ✅ File management (upload, download, delete)
- ✅ Light/dark theme support
- ✅ Docker containerized
- ✅ REST API with Swagger docs
- ✅ 83%+ test coverage

---

## 🏗️ Architecture

**Frontend:** React 19 + TypeScript + PrimeReact + Tailwind CSS
**Backend:** Spring Boot 3.5.1 + Java 21 + Gradle
**LLM:** Ollama / LM Studio / OpenAI (OpenAI-compatible API)
**DevOps:** Docker Compose for orchestration

See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) for detailed diagrams and system design.

---

## 🛠️ Technology Stack

| Component            | Technology   | Version |
| -------------------- | ------------ | ------- |
| **Frontend**         | React        | 19.2.0  |
| **Language**         | TypeScript   | 5.9.3   |
| **UI Lib**           | PrimeReact   | 10.9.7  |
| **Styling**          | Tailwind CSS | 4.1.18  |
| **Backend**          | Spring Boot  | 3.5.1   |
| **Language**         | Java         | 21 LTS  |
| **Build**            | Gradle       | 8.7     |
| **Containerization** | Docker       | 20.10+  |

See [docs/TECHNOLOGY_STACK.md](docs/TECHNOLOGY_STACK.md) for complete details.

---

## 📂 Project Structure

```
java-resumes/
├── frontend/                    # React application
│   ├── src/components/          # UI components
│   ├── src/hooks/               # Custom React hooks
│   └── package.json             # Dependencies
├── src/                         # Java source code
│   ├── main/java/.../           # Backend classes
│   └── test/java/.../           # Backend tests
├── docs/                        # Complete documentation
├── docker-compose.yml           # Docker orchestration
├── config.json                  # LLM configuration
├── build.gradle                 # Gradle configuration
└── README.md                    # This file
```

---

## 📡 API Endpoints

### Core Document Processing

| Method | Endpoint                | Purpose                      |
| ------ | ----------------------- | ---------------------------- |
| POST   | `/api/upload`           | Optimize resume/cover letter |
| POST   | `/api/markdownFile2PDF` | Convert Markdown to PDF      |
| POST   | `/api/process/skills`   | Get skills recommendations   |

### Interview Preparation _(New)_

| Method | Endpoint                               | Purpose                         |
| ------ | -------------------------------------- | ------------------------------- |
| POST   | `/api/generate/interview-hr-questions` | Generate HR interview questions |
| POST   | `/api/generate/interview-job-specific` | Generate job-specific questions |
| POST   | `/api/generate/interview-reverse`      | Questions to ask interviewers   |

### Professional Networking _(New)_

| Method | Endpoint                              | Purpose                       |
| ------ | ------------------------------------- | ----------------------------- |
| POST   | `/api/generate/cold-email`            | Generate cold outreach emails |
| POST   | `/api/generate/cold-linkedin-message` | Generate LinkedIn messages    |
| POST   | `/api/generate/thank-you-email`       | Generate thank you emails     |

### File Management

| Method | Endpoint                | Purpose       |
| ------ | ----------------------- | ------------- |
| GET    | `/api/files`            | List files    |
| GET    | `/api/files/{filename}` | Download file |
| DELETE | `/api/files/{filename}` | Delete file   |
| GET    | `/api/health`           | Health check  |

Full API docs: http://localhost:8080/swagger-ui/html (when running)

See [docs/API_REFERENCE.md](docs/API_REFERENCE.md) for complete API documentation.

---

## 🔧 Development Setup

### Backend

```bash
cd java-resumes
./gradlew build                 # Build
./gradlew bootRun              # Run (http://localhost:8080)
./gradlew test                 # Test
./gradlew checkstyleMain       # Code quality
```

### Frontend

```bash
cd frontend
npm install                     # Install
npm run dev                    # Run (http://localhost:3000)
npm test                       # Test
npm run lint                   # Code quality
```

See [docs/DEVELOPMENT_SETUP.md](docs/DEVELOPMENT_SETUP.md) for detailed instructions.

---

## 🧪 Testing

```bash
# Backend
./gradlew test                          # Run all tests
./gradlew test jacocoTestReport         # Generate coverage report

# Frontend
cd frontend
npm test                                # Run all tests
npm run test:coverage                   # Generate coverage report
```

Target coverage: **80%+** for both frontend and backend.

See [docs/TESTING.md](docs/TESTING.md) for complete testing guide.

---

## 🔐 Configuration

### LLM Setup

Edit `config.json`:

```json
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "not-needed-for-local",
  "model": "gemma-3-4b-it"
}
```

**Ollama** (recommended):

```bash
ollama serve
ollama pull gemma-3-4b-it
```

**LM Studio:** Download and run, load a model, start server at http://localhost:1234

See [docs/CONFIGURATION.md](docs/CONFIGURATION.md) for advanced configuration.

---

## 🐛 Troubleshooting

Common issues and solutions:

```bash
# Backend health check
curl http://localhost:8080/api/health

# Frontend status
curl http://localhost/

# Docker status
docker compose ps
docker compose logs -f
```

See [docs/TROUBLESHOOTING.md](docs/TROUBLESHOOTING.md) for complete troubleshooting guide.

---

## 🚀 Production Deployment

### Docker Compose (Recommended)

```bash
docker compose up --build -d
```

### Kubernetes / Cloud

See [docs/PRODUCTION_DEPLOYMENT.md](docs/PRODUCTION_DEPLOYMENT.md) for deployment options.

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Make changes and add tests
4. Run checks: `./gradlew build` and `npm run lint`
5. Commit: `git commit -m "feat: description"`
6. Push: `git push origin feature/your-feature`
7. Create Pull Request

See [docs/CODE_QUALITY.md](docs/CODE_QUALITY.md) for code standards.

---

## 📄 License

MIT License - see [LICENSE](LICENSE) file for details.

---

## 📞 Support & Resources

- **Documentation**: [docs/README.md](docs/README.md)
- **Issues**: [GitHub Issues](https://github.com/pbaletkeman/java-resumes/issues)
- **Discussions**: [GitHub Discussions](https://github.com/pbaletkeman/java-resumes/discussions)
- **Repository**: <https://github.com/pbaletkeman/java-resumes>

---

### Made with ❤️ using Java, Spring Boot, React, and AI

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
**Repository:** [github.com/pbaletkeman/java-resumes](https://github.com/pbaletkeman/java-resumes)
