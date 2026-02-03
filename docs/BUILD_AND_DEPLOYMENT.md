# Build & Deployment Workflows

Complete guide to building, testing, and deploying the java-resumes application (frontend and backend) to production.

## 📋 Table of Contents

1. [Quick Start](#quick-start)
2. [Frontend Build Guide](#frontend-build-guide)
3. [Backend Build Guide](#backend-build-guide)
4. [Release & Version Management](#release--version-management)
5. [Deployment Strategies](#deployment-strategies)
6. [CI/CD Workflows](#cicd-workflows)
7. [Troubleshooting](#troubleshooting)

---

## Quick Start

### Prerequisites

**All systems:**

- Git (version control)
- GitHub account with repository access

**Frontend:**

- Node.js 22+ (https://nodejs.org/)
- npm 11+ (included with Node.js)

**Backend:**

- Java 21 LTS (https://adoptium.net/)
- Gradle 9.3+ (included with project via wrapper)

### Quick Build (All-in-One)

**Windows (PowerShell/CMD):**

```bash
# Frontend
cd frontend && npm run build

# Backend
gradlew build -x test -x checkstyleMain -x checkstyleTest
```

**macOS/Linux:**

```bash
# Frontend
cd frontend && npm run build

# Backend
./gradlew build -x test -x checkstyleMain -x checkstyleTest
```

---

## Frontend Build Guide

### Overview

The frontend is a React 19 application using TypeScript, built with Vite for fast development and optimized production bundles.

**Key Statistics:**

- Framework: React 19.2.0
- Language: TypeScript 5.9.3
- Build Tool: Vite 7.2.4
- Bundle Size: ~350-450KB (gzipped)
- Target: Modern browsers (ES2020+)

### Quick Start (Frontend Only)

```bash
cd frontend
npm install
npm run build
```

Output: `frontend/dist/` directory with production-ready files

### Detailed Build Process

#### Step 1: Prerequisites Check

```bash
node --version    # Should be v22.0.0 or higher
npm --version     # Should be 11.0.0 or higher
```

#### Step 2: Install Dependencies

```bash
cd frontend
npm install
```

This command:

- Downloads all dependencies from npm registry
- Creates `node_modules/` directory
- Generates `package-lock.json` for reproducible builds

#### Step 3: Run Tests

```bash
npm test
```

Or with coverage:

```bash
npm test -- --coverage --run
```

**Coverage Requirements:**

- Lines: 80%+
- Functions: 80%+
- Branches: 80%+
- Statements: 80%+

#### Step 4: Production Build

```bash
npm run build
```

This command:

- Compiles TypeScript to JavaScript
- Bundles all modules with Vite
- Optimizes and minifies code
- Generates source maps (for debugging)
- Output: `dist/` directory

#### Step 5: Verify Build

```bash
# Check build size
du -sh frontend/dist/

# List generated files
ls -la frontend/dist/
```

Expected output structure:

```
dist/
├── index.html           # Entry point
├── assets/
│   ├── index-[hash].js  # Main bundle
│   ├── react-[hash].js  # React vendor
│   └── *.css            # Stylesheets
└── favicon.ico
```

### Using Build Scripts

**Windows:**

```batch
scripts\build-frontend.bat
```

**macOS/Linux:**

```bash
scripts/build-frontend.sh
chmod +x scripts/build-frontend.sh
./scripts/build-frontend.sh
```

The script:

1. ✓ Verifies Node.js installation
2. ✓ Installs dependencies
3. ✓ Runs test suite
4. ✓ Builds production bundle
5. ✓ Verifies output
6. ✓ Displays build summary

### Configuration

**Environment Variables:**

Create `frontend/.env.production`:

```env
VITE_API_BASE_URL=https://api.yourdomain.com
VITE_APP_TITLE=Resume Optimizer
VITE_ENABLE_ANALYTICS=true
```

**Build Configuration:**

Edit `frontend/vite.config.ts`:

```typescript
export default defineConfig({
  build: {
    outDir: "dist",
    sourcemap: true,
    minify: "terser",
    rollupOptions: {
      output: {
        manualChunks: {
          "react-vendor": ["react", "react-dom"],
        },
      },
    },
  },
});
```

### Deployment

**Static Hosting (Netlify, Vercel, GitHub Pages):**

1. Build: `npm run build`
2. Deploy: Push `dist/` contents
3. Configure: Set environment variables in hosting platform

**Docker:**

```dockerfile
FROM node:22-alpine AS builder
WORKDIR /app
COPY frontend ./
RUN npm install && npm run build

FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY frontend/nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
```

**Build & Push:**

```bash
docker build -f frontend/Dockerfile -t myapp-frontend:latest .
docker push myapp-frontend:latest
```

---

## Backend Build Guide

### Overview

The backend is a Spring Boot 3.5.1 application using Java 21 LTS, packaged as a fat JAR for easy deployment.

**Key Statistics:**

- Framework: Spring Boot 3.5.1
- Language: Java 21 LTS
- Build Tool: Gradle 9.3
- JAR Size: ~80-120MB
- Runtime: Java 21 LTS

### Quick Start (Backend Only)

```bash
gradle build -x test -x checkstyleMain -x checkstyleTest
```

Output: `build/libs/java-resumes-*.jar`

### Detailed Build Process

#### Step 1: Prerequisites Check

```bash
java -version       # Should be Java 21
gradle --version    # Should be 9.3+
```

#### Step 2: Clean Previous Builds

```bash
gradle clean --no-daemon
```

#### Step 3: Run Tests

```bash
gradle test -x checkstyleMain -x checkstyleTest --no-daemon
```

**Test Coverage:**

- Target: 80%+ code coverage
- Run: `gradle test --no-daemon`
- Report: `build/reports/tests/test/index.html`

#### Step 4: Code Quality Checks

```bash
gradle checkstyleMain checkstyleTest --no-daemon
```

**Checkstyle Rules:**

- Max line length: 120 characters
- Code formatting standards
- Javadoc requirements
- Import organization

#### Step 5: Build Fat JAR

```bash
gradle build --no-daemon -x test -x checkstyleMain -x checkstyleTest
```

This command:

- Compiles all Java source files
- Resolves dependencies
- Packages application with embedded Tomcat
- Creates executable JAR
- Output: `build/libs/java-resumes-X.X.X.jar`

#### Step 6: Verify JAR

```bash
# Check JAR file
ls -lh build/libs/*.jar

# Run JAR
java -jar build/libs/java-resumes-*.jar
```

### Using Build Scripts

**Windows:**

```batch
scripts\build-backend.bat
```

**macOS/Linux:**

```bash
scripts/build-backend.sh
chmod +x scripts/build-backend.sh
./scripts/build-backend.sh
```

The script:

1. ✓ Verifies Java installation
2. ✓ Cleans previous builds
3. ✓ Runs all tests
4. ✓ Checks code quality
5. ✓ Builds fat JAR
6. ✓ Verifies JAR creation
7. ✓ Displays deployment instructions

### Configuration

**Production Properties:**

Create `src/main/resources/application-prod.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://prod-db:3306/java_resumes
    username: ${DB_USER}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate

server:
  port: 8080
  servlet:
    context-path: /api

logging:
  level:
    root: INFO
    ca.letkeman: INFO
  file:
    name: logs/application.log

upload:
  path: /data/uploads
```

**Runtime JVM Options:**

```bash
java -Xmx2g \
     -Xms512m \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -Dspring.profiles.active=prod \
     -jar java-resumes-*.jar
```

### Deployment

**Local Server:**

```bash
# Build
gradle build -x test -x checkstyleMain -x checkstyleTest

# Run
java -jar build/libs/java-resumes-*.jar
```

**Docker:**

```dockerfile
FROM eclipse-temurin:25-jdk

WORKDIR /app

# Copy JAR from build
COPY build/libs/*.jar app.jar

# Create upload directory
RUN mkdir -p /data/uploads

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD java -version

# Run application
ENTRYPOINT ["java", "-Xmx2g", "-jar", "app.jar"]
```

**Build & Push:**

```bash
docker build -f Dockerfile -t myapp-backend:latest .
docker push myapp-backend:latest
```

**Docker Compose:**

```yaml
version: "3.8"

services:
  backend:
    image: myapp-backend:latest
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/resumes
      - SPRING_DATASOURCE_USERNAME=${DB_USER}
      - SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
    volumes:
      - upload_data:/data/uploads
    depends_on:
      - db

  frontend:
    image: myapp-frontend:latest
    ports:
      - "80:80"

  db:
    image: mysql:8
    environment:
      - MYSQL_DATABASE=resumes
      - MYSQL_ROOT_PASSWORD=${DB_ROOT_PASSWORD}

volumes:
  upload_data:
```

---

## Release & Version Management

### Overview

Version management for frontend and backend independently, with coordinated GitHub releases.

**Versioning Scheme:**

- Frontend: `frontend/v1.2.3`
- Backend: `backend/v2.0.1`
- Combined: `v1.2.3-2.0.1`

### Using Release Scripts

**Windows:**

```batch
scripts\release.bat
```

**macOS/Linux:**

```bash
scripts/release.sh
chmod +x scripts/release.sh
./scripts/release.sh
```

### Manual Release Process

#### Step 1: Update Versions

**Frontend:**

```bash
# Edit frontend/package.json
{
  "version": "1.2.4"  # Bump from 1.2.3
}
```

**Backend:**

```bash
# Edit build.gradle
version = '2.0.2'  # Bump from 2.0.1
```

#### Step 2: Commit Changes

```bash
git add frontend/package.json build.gradle
git commit -m "chore: bump versions - frontend v1.2.4, backend v2.0.2"
```

#### Step 3: Create Tags

```bash
# Frontend tag
git tag -a frontend/v1.2.4 -m "Release frontend v1.2.4"

# Backend tag
git tag -a backend/v2.0.2 -m "Release backend v2.0.2"

# Combined tag
git tag -a v1.2.4-2.0.2 -m "Release v1.2.4-2.0.2"
```

#### Step 4: Push to GitHub

```bash
# Push commits
git push origin master

# Push tags
git push origin frontend/v1.2.4 backend/v2.0.2 v1.2.4-2.0.2
```

#### Step 5: Create GitHub Release

1. Go to https://github.com/pbaletkeman/java-resumes/releases
2. Click "Draft a new release"
3. Select tag: `v1.2.4-2.0.2`
4. Add release notes
5. Attach build artifacts
6. Publish

---

## Deployment Strategies

### Blue-Green Deployment

Deploy new version alongside old version, switch traffic:

```bash
# Deploy new version (green)
docker run -d --name backend-green myapp-backend:v2.0.2

# Verify health
curl http://localhost:8081/api/health

# Switch traffic (use nginx/load balancer)
# Update upstream to point to green

# Remove old version (blue)
docker stop backend-blue
docker rm backend-blue
```

### Canary Deployment

Gradually roll out to percentage of users:

```yaml
# Kubernetes example
apiVersion: fluxcd.io/v1
kind: Canary
metadata:
  name: java-resumes
spec:
  targetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: backend
  progressDeadlineSeconds: 300
  service:
    port: 8080
  analysis:
    interval: 1m
    threshold: 5
    metrics:
      - name: request-success-rate
        thresholdRange:
          min: 99
```

### Zero-Downtime Deployment

Rolling updates with health checks:

```bash
# Using Kubernetes
kubectl set image deployment/backend \
  backend=myapp-backend:v2.0.2 --record

# Monitor rollout
kubectl rollout status deployment/backend
```

---

## CI/CD Workflows

### GitHub Actions Workflows

#### Frontend Build Workflow

**Trigger:** Changes in `frontend/` or `.github/workflows/frontend-build.yml`

**Steps:**

1. Checkout code
2. Setup Node.js 22
3. Install dependencies
4. Run linting
5. Run type checking
6. Run tests
7. Build production bundle
8. Upload artifacts

**View:** `.github/workflows/frontend-build.yml`

#### Backend Build Workflow

**Trigger:** Changes in `src/` or `.github/workflows/backend-build.yml`

**Steps:**

1. Checkout code
2. Setup Java 21
3. Run tests
4. Run code quality checks
5. Build fat JAR
6. Security scanning (Trivy)
7. Docker build (dry run)
8. Upload artifacts

**View:** `.github/workflows/backend-build.yml`

#### Release Workflow

**Trigger:** Manual workflow dispatch

**Steps:**

1. Build frontend
2. Build backend
3. Create GitHub Release
4. Generate release notes
5. Upload artifacts

**View:** `.github/workflows/release.yml`

**How to Trigger:**

1. Go to Actions tab
2. Select "Full Release Pipeline"
3. Click "Run workflow"
4. Select options:
   - Release type: `patch`, `minor`, or `major`
   - Components: `frontend`, `backend`, or `both`
5. Click "Run workflow"

---

## Troubleshooting

### Frontend Build Issues

**Issue: npm ERR! code E404 (module not found)**

```bash
# Solution: Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
```

**Issue: Out of memory during build**

```bash
# Solution: Increase Node.js memory
NODE_OPTIONS=--max_old_space_size=4096 npm run build
```

**Issue: TypeScript compilation errors**

```bash
# Check types
npm run type-check

# Fix: Review error messages and fix source files
```

### Backend Build Issues

**Issue: Gradle compilation error**

```bash
# Solution: Update Gradle wrapper
gradle wrapper --gradle-version=latest --distribution-type=bin
gradlew build
```

**Issue: Test failures**

```bash
# Run specific test
gradle test --tests TestClassName

# Run with debug output
gradle test --info
```

**Issue: Out of memory (Java heap)**

```bash
# Increase Java heap
export JAVA_OPTS="-Xmx2g -Xms512m"
gradle build
```

### General Issues

**Issue: Git tag conflicts**

```bash
# Delete local tag
git tag -d v1.2.3

# Delete remote tag
git push origin --delete v1.2.3

# Recreate tag
git tag -a v1.2.3 -m "Release v1.2.3"
git push origin v1.2.3
```

**Issue: Workflow runs but artifacts missing**

```bash
# Check workflow logs
# GitHub > Actions > [Workflow Name] > [Run] > [Job]

# Common causes:
# - Build failed (check logs)
# - Wrong artifact path
# - Insufficient permissions
```

---

## Environment Variables Reference

### Frontend

```env
VITE_API_BASE_URL=http://localhost:8080
VITE_APP_TITLE=Resume Optimizer
VITE_ENABLE_ANALYTICS=false
```

### Backend

```env
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/resumes
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=password
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_PROFILES_ACTIVE=dev
UPLOAD_PATH=./uploads
```

---

## Support & Resources

- **Documentation**: [docs/architecture/ARCHITECTURE.md](../Architecture.md)
- **GitHub Issues**: https://github.com/pbaletkeman/java-resumes/issues
- **GitHub Discussions**: https://github.com/pbaletkeman/java-resumes/discussions

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
