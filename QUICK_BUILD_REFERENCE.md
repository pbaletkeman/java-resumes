# Quick Build Reference

Fast reference for common build, test, and deployment tasks.

## Prerequisites

```bash
# Check versions
node --version    # Should be v20+
npm --version     # Should be 11+
java -version     # Should be Java 21
gradle --version  # Should be 9.3+
```

## Frontend Commands

```bash
# Install dependencies
npm install

# Development server
npm run dev

# Run tests
npm test

# Build tests with coverage
npm test -- --coverage --run

# Production build
npm run build

# Linting
npm run lint

# Type checking
npm run type-check
```

## Backend Commands

```bash
# Run tests
gradle test --no-daemon

# Code quality check
gradle checkstyleMain --no-daemon

# Production build (skip tests)
gradle build --no-daemon -x test -x checkstyleMain -x checkstyleTest

# Build with all checks
gradle build --no-daemon

# Run application
java -jar build/libs/*.jar
```

## Build Scripts

### Windows

```batch
REM Frontend
scripts\build-frontend.bat

REM Backend
scripts\build-backend.bat

REM Release/Version
scripts\release.bat
```

### macOS/Linux

```bash
# Frontend
./scripts/build-frontend.sh

# Backend
./scripts/build-backend.sh

# Release/Version
./scripts/release.sh
```

## Production Deployment

### Frontend

```bash
# Build
cd frontend && npm run build

# Output: dist/
# Deploy dist/ contents to web server
```

### Backend

```bash
# Build
gradle build -x test -x checkstyleMain -x checkstyleTest

# Output: build/libs/java-resumes-*.jar
# Run: java -jar build/libs/java-resumes-*.jar
```

## Version Management

### Manual Release

```bash
# 1. Update versions
# - Edit frontend/package.json (version field)
# - Edit build.gradle (version = '...')

# 2. Commit
git add frontend/package.json build.gradle
git commit -m "Release v1.2.3"

# 3. Tag
git tag -a v1.2.3 -m "Release v1.2.3"

# 4. Push
git push origin master
git push origin v1.2.3
```

### Using Release Script

```bash
# Windows
scripts\release.bat

# macOS/Linux
./scripts/release.sh
```

## GitHub Actions

### View Workflows

- Frontend Build: `.github/workflows/frontend-build.yml`
- Backend Build: `.github/workflows/backend-build.yml`
- Release: `.github/workflows/release.yml`

### Trigger Release Workflow

1. Go to GitHub Actions tab
2. Select "Full Release Pipeline"
3. Click "Run workflow"
4. Select options and run

## Docker Commands

```bash
# Build frontend image
docker build -f frontend/Dockerfile -t myapp-frontend:latest frontend/

# Build backend image
docker build -f Dockerfile -t myapp-backend:latest .

# Run with docker-compose
docker-compose up -d

# View logs
docker-compose logs -f backend
docker-compose logs -f frontend

# Stop
docker-compose down
```

## Troubleshooting

```bash
# Clear npm cache
npm cache clean --force

# Clear Gradle cache
gradle clean

# Full rebuild
rm -rf frontend/node_modules frontend/dist
rm -rf build
npm install
gradle build
```

## CI/CD Status

Check GitHub Actions workflow status:

```bash
# View all workflows
gh workflow list

# View latest run
gh workflow view

# View specific workflow
gh workflow view frontend-build.yml
```

## Documentation

- **Full Guide**: [BUILD_AND_DEPLOYMENT.md](./BUILD_AND_DEPLOYMENT.md)
- **Architecture**: [Architecture.md](./Architecture.md)
- **README**: [../README.md](../README.md)
