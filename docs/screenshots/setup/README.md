# Setup & Deployment Screenshots Guide

Visual documentation for installation, setup, and deployment processes.

## 📸 Setup Screenshots

| Screenshot        | File                    | Size       | Purpose           | Status    |
| ----------------- | ----------------------- | ---------- | ----------------- | --------- |
| Docker Compose Up | `docker-compose-up.png` | 1000x400px | Container startup | 📌 Needed |
| Local Dev Setup   | `local-dev-setup.png`   | 1200x600px | Local development | 📌 Needed |
| Build Success     | `build-success.png`     | 1000x500px | Successful build  | 📌 Needed |

---

## 1. Docker Compose Up

### What to Show

Terminal output showing successful Docker Compose startup with all services running.

### Output Should Include

```bash
$ docker-compose up

Creating network "java-resumes_default" with the default driver
Creating java-resumes-backend-1  ... done
Creating java-resumes-frontend-1 ... done

backend-1   |
backend-1   |   .   ____          _            __ _ _
backend-1   |  /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
backend-1   | ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
backend-1   |  \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
backend-1   |   '  |____| .__|_| |_|_| |_\__, | / / / /
backend-1   |  =========|_|==============|___/=/_/_/_/
backend-1   | :: Spring Boot ::                (v3.5.1)
backend-1   |
backend-1   | 2026-01-16 10:30:00.123  INFO 1 --- [main] c.l.resumes.RestServiceApplication...
backend-1   | 2026-01-16 10:30:05.456  INFO 1 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer
backend-1   | 2026-01-16 10:30:05.789  INFO 1 --- [main] Tomcat initialized with port(s): 8080 (http)
backend-1   | 2026-01-16 10:30:06.012  INFO 1 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer
backend-1   | Tomcat started on port(s): 8080 (http) with context path ''
backend-1   | 2026-01-16 10:30:06.234  INFO 1 --- [main] c.l.resumes.RestServiceApplication
backend-1   | Started RestServiceApplication in 5.234 seconds (JVM running for 6.345s)
backend-1   |

frontend-1  |
frontend-1  |  VITE v7.2.4  ready in 234 ms
frontend-1  |
frontend-1  |  ➜  Local:   http://localhost:80
frontend-1  |  ➜  press h + enter to show help
```

### Capture Instructions

1. **Verify Docker is Running**

   ```bash
   docker --version
   docker-compose --version
   ```

2. **Navigate to Project Directory**

   ```bash
   cd c:\Users\Pete\Desktop\java-resumes
   ```

3. **Build Images (if needed)**

   ```bash
   docker-compose build
   ```

4. **Start Services**

   ```bash
   docker-compose up
   ```

   Wait for both services to show as running

5. **Capture Terminal Output**
   - Take screenshot showing both containers started
   - Include port mappings (8080, 80)
   - Show success messages
   - Size: 1000x400px minimum

6. **Save File**
   ```
   docs/screenshots/setup/docker-compose-up.png
   ```

### Verification Checklist

- [ ] Both containers show as "done"
- [ ] Backend port 8080 confirmed
- [ ] Frontend running on port 80
- [ ] No error messages visible
- [ ] "Spring Boot" ASCII art visible
- [ ] Tomcat startup message visible
- [ ] Vite startup message visible
- [ ] Terminal text is readable
- [ ] File size < 200KB

---

## 2. Local Development Setup

### What to Show

Complete local development environment with both services running in IDE/terminals.

### Setup Should Show

```
┌─────────────────────────────────────────────────────────┐
│  VS Code - java-resumes [master]                        │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  [File Explorer]                                        │
│  📁 java-resumes                                        │
│  ├─ 📁 src/main/java                                    │
│  ├─ 📁 frontend                                         │
│  ├─ 📁 docs                                             │
│  ├─ 📄 build.gradle                                     │
│  └─ 📄 Dockerfile                                       │
│                                                         │
│  [Editor Tab]                                           │
│  ResumeController.java                                  │
│  [Code visible]                                         │
│                                                         │
│  [Terminal 1 - Frontend]                                │
│  $ npm run dev                                          │
│  ...                                                    │
│  ➜  Local:   http://localhost:5173                      │
│                                                         │
│  [Terminal 2 - Backend]                                 │
│  $ ./gradlew bootRun                                    │
│  ...                                                    │
│  Tomcat started on port(s): 8080 (http)                 │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### Capture Instructions

1. **Set Up Terminal Splits**
   - Open VS Code
   - Open integrated terminal (Ctrl+`)
   - Split terminal (Ctrl+Shift+5)
   - Create two terminal panes

2. **Start Frontend (Terminal 1)**

   ```bash
   cd frontend
   npm run dev
   ```

   Wait for: "➜ Local: http://localhost:5173"

3. **Start Backend (Terminal 2)**

   ```bash
   ./gradlew bootRun
   ```

   Wait for: "Tomcat started on port(s): 8080 (http)"

4. **Arrange VS Code**
   - Open a source file in editor (e.g., ResumeController.java)
   - Show file explorer on left
   - Show terminals at bottom
   - Maximize window (1200x600px)

5. **Take Screenshot**
   - Capture full VS Code window
   - Show both terminals with status
   - Show project structure
   - Show open code file

6. **Save File**
   ```
   docs/screenshots/setup/local-dev-setup.png
   ```

### Verification Checklist

- [ ] VS Code window clear and visible
- [ ] Project structure visible in explorer
- [ ] Code file open in editor
- [ ] Both terminals visible
- [ ] Frontend running (port 5173)
- [ ] Backend running (port 8080)
- [ ] No error messages
- [ ] Window size 1200x600px minimum
- [ ] File size < 400KB

---

## 3. Build Success

### What to Show

Terminal output showing successful Gradle build completion.

### Build Output Should Include

```bash
$ ./gradlew clean build

> Task :clean
> Task :checkstyleMain
> Task :compileJava
> Task :processResources
> Task :classes
> Task :checkstyleTest
> Task :compileTestJava
> Task :processTestResources
> Task :testClasses
> Task :test

> Task :jar
> Task :bootJar
> Task :assemble
> Task :check
> Task :build

BUILD SUCCESSFUL in 45s
47 actionable tasks, 47 executed

✓ All checks passed
✓ All tests passed
✓ Checkstyle validation passed
✓ Build artifact created: build/libs/resumes-0.0.1-SNAPSHOT.jar
```

### Capture Instructions

1. **Clean Build**

   ```bash
   cd c:\Users\Pete\Desktop\java-resumes
   ./gradlew clean build
   ```

   This will take 30-60 seconds depending on system

2. **Wait for Completion**
   - Watch for "BUILD SUCCESSFUL"
   - Ensure no "BUILD FAILED" messages
   - Note the time taken

3. **Capture Terminal Output**
   - Take screenshot showing full build output
   - Include starting task list
   - Include final summary
   - Include success message
   - Size: 1000x500px

4. **Optional: Build with Checkstyle**

   ```bash
   ./gradlew checkstyleMain
   ```

   Shows code quality validation passed

5. **Save File**
   ```
   docs/screenshots/setup/build-success.png
   ```

### Verification Checklist

- [ ] "BUILD SUCCESSFUL" message visible
- [ ] Build time shown (should be <60 seconds)
- [ ] Task list shows all major tasks
- [ ] No failed tasks
- [ ] No error or warning messages
- [ ] JAR file location shown
- [ ] Terminal text readable
- [ ] File size < 200KB

---

## 📋 Setup Verification Steps

### Prerequisites Verification

Before taking screenshots, verify:

```bash
# Check Java version
java -version
# Should show: openjdk version "17..." or java version "17..."

# Check Gradle
./gradlew --version
# Should show: Gradle 8.7

# Check Node.js
node --version
# Should show: v18.x.x or higher

# Check npm
npm --version
# Should show: v9.x.x or higher

# Check Docker
docker --version
# Should show: Docker version ...

# Check Docker Compose
docker-compose --version
# Should show: Docker Compose version ...
```

### LLM Service Verification

```bash
# Check if Ollama is running
curl http://localhost:11434/api/tags

# Should return JSON with available models
# Example response:
# {"models":[{"name":"mistral:latest","...":"..."}]}
```

### Application Verification

```bash
# Check backend is accessible
curl http://localhost:8080/swagger-ui/index.html

# Check frontend is accessible
curl http://localhost:5173
# or open in browser: http://localhost:5173
```

---

## 🔄 Build & Test Workflow

### Full Development Cycle

```bash
# 1. Clean previous build
./gradlew clean

# 2. Build project
./gradlew build

# 3. Run tests with coverage
./gradlew test jacocoTestReport

# 4. Check code quality
./gradlew checkstyleMain

# 5. Run application
./gradlew bootRun

# Or in frontend:
cd frontend
npm install
npm run dev
```

### Docker Workflow

```bash
# 1. Build images
docker-compose build

# 2. Start services
docker-compose up

# 3. Check logs
docker-compose logs -f

# 4. Stop services
docker-compose down

# 5. Clean up
docker-compose down -v
```

---

## 📸 Additional Setup Screenshots

### Gradle Wrapper Installation

```bash
$ ./gradlew --version

------------------------------------------------------------
Gradle 8.7
------------------------------------------------------------

Build time:   2024-11-01 19:46:21 UTC
Revision:     fb35f4bf2b1e6cc0e71e0fa7c6e3ee5c68ea55fa

Kotlin:       1.9.24
Groovy:       3.0.21
Ant:           1.10.14
JVM:           17.0.11 (Eclipse Adoptium 17.0.11+9)
OS:            Windows 11 10.0 x86_64
```

### Node Package Installation

```bash
$ cd frontend
$ npm install

added 324 packages, and audited 324 packages in 12s

found 0 vulnerabilities
```

### Environment Configuration

```bash
# Backend configuration
cat src/main/resources/application.properties

server.port=8080
upload.path=./uploads
spring.servlet.multipart.max-file-size=10MB
logging.level.ca.letkeman=DEBUG

# Frontend configuration
cat frontend/.env.development

VITE_API_BASE_URL=http://localhost:8080
```

---

## ✅ Setup Checklist

Before considering setup complete:

- [ ] Java 17 installed and verified
- [ ] Gradle 8.7 installed and working
- [ ] Node.js 18+ installed
- [ ] npm installed
- [ ] Docker installed
- [ ] Docker Compose installed
- [ ] Ollama/LM Studio running (for LLM)
- [ ] Backend builds successfully
- [ ] Frontend installs and runs
- [ ] Both services accessible
- [ ] Database (if needed) initialized
- [ ] Environment variables configured
- [ ] No startup errors or warnings
- [ ] All tests passing

---

## 📞 Troubleshooting Setup Issues

### Backend Won't Build

```bash
# Clear cache and rebuild
./gradlew clean
./gradlew build -x test

# Check Java version
java -version

# Check Gradle wrapper
./gradlew --version
```

### Frontend Won't Start

```bash
# Clear node modules and reinstall
rm -rf node_modules
npm install

# Check Node version
node --version

# Start dev server
npm run dev
```

### Docker Issues

```bash
# Check Docker daemon
docker ps

# Build images
docker-compose build --no-cache

# View logs
docker-compose logs -f
```

### LLM Connection Error

```bash
# Verify Ollama is running
curl http://localhost:11434/api/tags

# Start Ollama
ollama serve

# Pull model
ollama pull mistral
```

---

## 📚 References

- [Main README](../../README.md)
- [Backend Setup Guide](../../docs/BACKEND_README.md)
- [Frontend Setup Guide](../../frontend/README.md)
- [Docker Documentation](../../Dockerfile)
- [Docker Compose Setup](../../docker-compose.yml)
- [Screenshots Guide](../SCREENSHOTS_GUIDE.md)

---

Last Updated: 2026-01-16
