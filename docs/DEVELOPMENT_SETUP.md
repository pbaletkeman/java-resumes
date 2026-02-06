# Development Setup

Complete guide for setting up the development environment for java-resumes.

- [Development Setup](#development-setup)
  - [📋 Prerequisites](#-prerequisites)
    - [System Requirements](#system-requirements)
    - [For Backend Development](#for-backend-development)
    - [For Frontend Development](#for-frontend-development)
    - [For Full Stack Development](#for-full-stack-development)
  - [⚙️ Backend Setup](#️-backend-setup)
    - [Step 1: Clone Repository](#step-1-clone-repository)
    - [Step 2: Verify Java Installation](#step-2-verify-java-installation)
    - [Step 3: Configure Gradle](#step-3-configure-gradle)
    - [Step 4: Configure LLM Service](#step-4-configure-llm-service)
    - [Step 5: Build Backend](#step-5-build-backend)
    - [Step 6: Run Backend](#step-6-run-backend)
    - [Backend Development Commands](#backend-development-commands)
  - [🎨 Frontend Setup](#-frontend-setup)
    - [Step 1: Navigate to Frontend Directory](#step-1-navigate-to-frontend-directory)
    - [Step 2: Verify Node/npm Installation](#step-2-verify-nodenpm-installation)
    - [Step 3: Install Dependencies](#step-3-install-dependencies)
    - [Step 4: Configure Environment](#step-4-configure-environment)
    - [Step 5: Start Development Server](#step-5-start-development-server)
    - [Frontend Development Commands](#frontend-development-commands)
  - [🐳 Full Stack Development with Docker](#-full-stack-development-with-docker)
    - [Quick Start (Docker Compose)](#quick-start-docker-compose)
    - [Individual Container Commands](#individual-container-commands)
  - [🔄 Development Workflow](#-development-workflow)
    - [Local Backend + Local Frontend](#local-backend--local-frontend)
    - [Debugging Backend](#debugging-backend)
    - [Debugging Frontend](#debugging-frontend)
  - [📂 Project Structure for Development](#-project-structure-for-development)
  - [🧪 Testing During Development](#-testing-during-development)
    - [Backend Tests](#backend-tests)
    - [Frontend Tests](#frontend-tests)
    - [Integration Testing](#integration-testing)
  - [🌍 Environment Variables](#-environment-variables)
    - [Backend Environment Variables](#backend-environment-variables)
    - [Frontend Environment Variables](#frontend-environment-variables)
  - [💻 IDE Configuration](#-ide-configuration)
    - [IntelliJ IDEA Setup](#intellij-idea-setup)
    - [VS Code Setup](#vs-code-setup)
  - [🐛 Common Development Issues](#-common-development-issues)
    - [Issue: Java 21 Not Found](#issue-java-21-not-found)
    - [Issue: Port Already in Use](#issue-port-already-in-use)
    - [Issue: LLM Service Connection Failed](#issue-llm-service-connection-failed)
    - [Issue: npm install Fails](#issue-npm-install-fails)
  - [Additional Resources](#additional-resources)

---

## 📋 Prerequisites

### System Requirements

- **OS**: Windows 10+, macOS 10.15+, or Linux (Ubuntu 18.04+)
- **RAM**: 8GB minimum (16GB recommended)
- **Disk**: 20GB free space (for Docker images, builds, and development)
- **CPU**: 4 cores minimum

### For Backend Development

- **Java Development Kit (JDK) 21 LTS**
  - Download: [Eclipse Temurin](https://adoptium.net/)
  - Verify: `java -version` (should show 21.x.x)

- **Gradle 8.7+**
  - Included with project (uses gradlew wrapper)
  - Verify: `./gradlew --version`

- **IDE** (choose one)
  - IntelliJ IDEA Community Edition (recommended)
  - Eclipse IDE for Java Developers
  - Visual Studio Code with Java extensions

### For Frontend Development

- **Node.js 22 LTS**
  - Download: [Node.js](https://nodejs.org/)
  - Verify: `node --version` (should show 22.x.x)
  - Verify npm: `npm --version` (should show 10.x+)

- **Code Editor**
  - Visual Studio Code (recommended)
  - WebStorm
  - Sublime Text with extensions

### For Full Stack Development

- **Docker Desktop**
  - Download: [Docker Desktop](https://www.docker.com/products/docker-desktop)
  - Includes: Docker & Docker Compose
  - Verify: `docker --version` and `docker compose --version`

- **LLM Service** (choose one for testing)
  - **Ollama**: [Download](https://ollama.ai/)
  - **LM Studio**: [Download](https://lmstudio.ai/)
  - **OpenAI API Key**: [Sign up](https://platform.openai.com/)

- **Git** (for version control)
  - Download: [Git](https://git-scm.com/)
  - Verify: `git --version`

---

## ⚙️ Backend Setup

### Step 1: Clone Repository

```bash
git clone https://github.com/pbaletkeman/java-resumes.git
cd java-resumes
```

### Step 2: Verify Java Installation

```bash
java -version
# Output should show: openjdk version "21.x.x"
```

### Step 3: Configure Gradle

No configuration needed! The project uses Gradle wrapper:

```bash
# On Linux/macOS
./gradlew --version

# On Windows
gradlew.bat --version
```

### Step 4: Configure LLM Service

Create `config.json` in project root:

```json
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "not-needed-for-local",
  "model": "gemma-3-4b-it"
}
```

**For different LLM services:**

**Ollama (recommended for dev):**

```json
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "not-needed-for-local",
  "model": "gemma-3-4b-it"
}
```

**LM Studio:**

```json
{
  "endpoint": "http://localhost:1234/v1/chat/completions",
  "apikey": "not-needed-for-local",
  "model": "your-loaded-model"
}
```

**OpenAI:**

```json
{
  "endpoint": "https://api.openai.com/v1/chat/completions",
  "apikey": "sk-your-actual-key",
  "model": "gpt-4"
}
```

### Step 5: Build Backend

```bash
# Full build with tests
./gradlew clean build

# Build without tests (faster)
./gradlew clean build -x test
```

### Step 6: Run Backend

**Development mode (hot reload):**

```bash
./gradlew bootRun
```

**From IDE (IntelliJ IDEA):**

1. Open project
2. Right-click `RestServiceApplication.java`
3. Select "Run RestServiceApplication"

**Access:**

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Health Check: http://localhost:8080/api/health

### Backend Development Commands

```bash
# Clean build
./gradlew clean

# Build without tests
./gradlew build -x test

# Run tests
./gradlew test

# Run specific test
./gradlew test --tests ResumeControllerTest

# Code quality check
./gradlew checkstyleMain checkstyleTest

# Generate test report
./gradlew test jacocoTestReport
# View: build/reports/jacoco/test/html/index.html

# Watch for changes and rebuild
./gradlew build --continuous

# View dependencies
./gradlew dependencies

# Clean everything
./gradlew clean
```

---

## 🎨 Frontend Setup

### Step 1: Navigate to Frontend Directory

```bash
cd frontend
```

### Step 2: Verify Node/npm Installation

```bash
node --version  # Should show 22.x.x
npm --version   # Should show 10.x+
```

### Step 3: Install Dependencies

```bash
npm install
```

### Step 4: Configure Environment

Copy environment template:

```bash
cp .env.example .env
```

Edit `.env`:

```env
VITE_API_BASE_URL=http://localhost:8080
VITE_APP_TITLE=Java Resumes
```

### Step 5: Start Development Server

```bash
npm run dev
```

**Access:**

- Frontend: http://localhost:3000
- Vite is configured to proxy API calls to backend

**Output should show:**

```
VITE v7.2.4  ready in xxx ms

  Local:   http://localhost:3000/
  press h + enter to show help
```

### Frontend Development Commands

```bash
# Start dev server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Run all tests
npm run test

# Run tests with UI
npm run test:ui

# Generate coverage report
npm run test:coverage
# View: frontend/coverage/index.html

# Lint code
npm run lint

# Format code
npm run format

# Type checking
npm run type-check

# Watch mode (rebuild on changes)
npm run build --watch
```

---

## 🐳 Full Stack Development with Docker

### Quick Start (Docker Compose)

```bash
# From project root
docker compose up --build

# Access:
# Frontend: http://localhost
# Backend API: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
```

### Individual Container Commands

```bash
# View logs
docker compose logs -f

# View specific service logs
docker compose logs -f backend
docker compose logs -f frontend

# Stop containers
docker compose down

# Stop and remove volumes
docker compose down -v

# Rebuild specific service
docker compose up --build backend

# Execute command in container
docker compose exec backend sh
docker compose exec frontend sh

# Check container status
docker compose ps
```

---

## 🔄 Development Workflow

### Local Backend + Local Frontend

**Terminal 1 - Backend:**

```bash
cd java-resumes
./gradlew bootRun
```

**Terminal 2 - Frontend:**

```bash
cd java-resumes/frontend
npm run dev
```

**Access:**

- Frontend: http://localhost:3000
- Backend: http://localhost:8080
- API calls proxy automatically via Vite config

### Debugging Backend

**Using IntelliJ IDEA:**

1. Set breakpoint by clicking line number
2. Click "Debug" button (next to Run)
3. Execution pauses at breakpoint
4. Use Debug panel to inspect variables

**Using VS Code:**

1. Install "Extension Pack for Java"
2. Set breakpoint by clicking line number
3. Press F5 or click "Run and Debug"
4. Select "Java" as debug environment
5. Execution pauses at breakpoint

### Debugging Frontend

**Using Chrome DevTools:**

1. Open http://localhost:3000
2. Press F12 to open DevTools
3. Go to Sources tab
4. Set breakpoints in code
5. Reload page to trigger

**Using VS Code:**

1. Install "Debugger for Chrome"
2. Create launch configuration in `.vscode/launch.json`
3. Press F5 to start debugging
4. Set breakpoints and reload page

---

## 📂 Project Structure for Development

```
java-resumes/
 src/main/java/              # Backend source code
    ca/letkeman/resumes/
        controller/         # REST endpoints
        service/            # Business logic
        model/              # Data models
        optimizer/          # LLM integration
        RestServiceApplication.java

 src/test/java/              # Backend tests
    ca/letkeman/resumes/

 frontend/                   # Frontend React app
    src/
       components/         # React components
       hooks/              # Custom hooks
       services/           # API services
       App.tsx             # Root component
       main.tsx            # Entry point
    tests/                  # Frontend tests
    package.json            # Dependencies
    vite.config.ts          # Vite config

 build.gradle                # Backend build config
 settings.gradle
 docker-compose.yml          # Docker config
 config.json                 # LLM config
 README.md
```

---

## 🧪 Testing During Development

### Backend Tests

```bash
# Run all tests
./gradlew test

# Run test class
./gradlew test --tests ResumeControllerTest

# Run single test method
./gradlew test --tests ResumeControllerTest.testUploadResume

# Run with output
./gradlew test --info

# Debug test
./gradlew test --debug
```

### Frontend Tests

```bash
# Run all tests
npm run test

# Watch mode (rerun on changes)
npm run test --watch

# Run specific test file
npm run test -- components/MainTab.test.tsx

# Debug tests
npm run test -- --inspect-brk
```

### Integration Testing

```bash
# Start both backend and frontend
# Terminal 1
./gradlew bootRun

# Terminal 2
npm run dev

# Terminal 3 - Run integration tests
npm run test:integration
```

---

## 🌍 Environment Variables

### Backend Environment Variables

**File:** `.env` or `application.properties`

```properties
# LLM Configuration
LLM_ENDPOINT=http://localhost:11434/v1/chat/completions
LLM_APIKEY=not-needed-for-local
LLM_MODEL=gemma-3-4b-it

# File Upload
UPLOAD_PATH=./files
UPLOAD_MAX_SIZE=500000

# Spring Boot
SPRING_PROFILES_ACTIVE=dev
LOGGING_LEVEL_CA_LETKEMAN=DEBUG

# External Config Path
APP_CONFIG_PATH=/path/to/config

# External Prompts
PROMPTS_DIR=/path/to/prompts
```

### Frontend Environment Variables

**File:** `frontend/.env`

```env
# API
VITE_API_BASE_URL=http://localhost:8080

# App Settings
VITE_APP_TITLE=Java Resumes
VITE_LOG_LEVEL=debug

# External Config Path
VITE_CONFIG_PATH=/path/to/config
```

---

## 💻 IDE Configuration

### IntelliJ IDEA Setup

1. **Open Project:**
   - File Open Select project root

2. **Configure JDK:**
   - Project Structure Project Set SDK to JDK 21

3. **Configure Run Configuration:**
   - Run Edit Configurations
   - Create new Spring Boot application
   - Main class: `ca.letkeman.resumes.RestServiceApplication`
   - VM options: `-Xmx512m -Dapp.config.path=./`

4. **Install Plugins:**
   - Settings Plugins Search Install:
     - Checkstyle-IDEA
     - Gradle
     - Spring Boot

### VS Code Setup

**Extensions:**

```
Extension Pack for Java
Spring Boot Extension Pack
Gradle for Java
TestRunner for Java
```

**Settings (.vscode/settings.json):**

```json
{
  "java.home": "/path/to/jdk21",
  "java.configuration.updateBuildConfiguration": "automatic",
  "[java]": {
    "editor.defaultFormatter": "redhat.java",
    "editor.formatOnSave": true
  }
}
```

---

## 🐛 Common Development Issues

### Issue: Java 21 Not Found

```bash
# Check installed versions
java -version

# Set JAVA_HOME (on Linux/macOS)
export JAVA_HOME=/path/to/jdk21

# Set JAVA_HOME (on Windows)
SET JAVA_HOME=C:\path\to\jdk21
```

### Issue: Port Already in Use

```bash
# Linux/macOS - Find process using port
lsof -i :8080
kill -9 <PID>

# Windows - Find process using port
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Or change port in application.yml
# server.port=8081
```

### Issue: LLM Service Connection Failed

```bash
# Check if Ollama is running
curl http://localhost:11434/api/version

# Start Ollama
ollama serve

# In another terminal, pull a model
ollama pull gemma-3-4b-it

# Test the model
ollama run gemma-3-4b-it "Hello"
```

### Issue: npm install Fails

```bash
# Clear npm cache
npm cache clean --force

# Remove node_modules and package-lock.json
rm -rf node_modules package-lock.json

# Reinstall
npm install
```

---

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [Gradle Documentation](https://docs.gradle.org/)
- [Vite Documentation](https://vitejs.dev/)
- [Docker Documentation](https://docs.docker.com/)

---

**See also:**

- [Quick Start](QUICK_START.md) - 5-minute setup with Docker
- [Architecture](ARCHITECTURE.md) - System design overview
- [Testing](TESTING.md) - Comprehensive testing guide
- [Production Deployment](PRODUCTION_DEPLOYMENT.md) - Production setup

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
