# Docker Setup Guide

Complete guide for running the java-resumes application using Docker.

## Prerequisites

- Docker 20.10+ installed
- Docker Compose v2+ (or Docker with Compose plugin)
- At least 4GB RAM available for containers
- 10GB free disk space

## Quick Start

```bash
# Start all services (backend, frontend, Ollama)
docker compose up -d

# Pull an LLM model (required for first run)
docker exec resume-ollama ollama pull tinyllama

# Check service status
docker compose ps

# View logs
docker compose logs -f

# Stop all services
docker compose down
```

Access the application:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- API Documentation: http://localhost:8080/swagger-ui/index.html
- Ollama API: http://localhost:11434

## Architecture

The application consists of 3 services:

### 1. Ollama (LLM Service)
- **Image**: `ollama/ollama:latest`
- **Port**: 11434
- **Purpose**: Provides local LLM inference
- **Volume**: `ollama-data` (persistent model storage)
- **Optional**: Can be disabled if using external LLM service

### 2. Backend (Spring Boot API)
- **Built from**: `./Dockerfile`
- **Port**: 8080
- **Technology**: Java 21, Spring Boot 3.5.1, Gradle 8.10
- **Volume**: `backend-files` (uploaded files storage)
- **Dependencies**: Ollama service

### 3. Frontend (React UI)
- **Built from**: `./frontend/Dockerfile` (production) or `./frontend/Dockerfile.dev` (development)
- **Port**: 3000 (dev) or 80 (production)
- **Technology**: React 19, TypeScript, Vite
- **Dependencies**: Backend service

## Configuration

### Environment Variables

Create a `.env` file in the project root:

```env
# LLM Configuration
LLM_ENDPOINT=http://ollama:11434/v1/chat/completions
LLM_APIKEY=not-needed-for-local

# Spring Profile
SPRING_PROFILES_ACTIVE=prod

# Upload Configuration
UPLOAD_PATH=files

# Frontend Configuration
VITE_API_BASE_URL=http://backend:8080
NODE_ENV=production
```

### Using External LLM Service

To use OpenAI or another external service instead of Ollama:

1. Update `.env`:
```env
LLM_ENDPOINT=https://api.openai.com/v1/chat/completions
LLM_APIKEY=your-api-key-here
```

2. Comment out Ollama service in `docker-compose.yml`:
```yaml
services:
  # ollama:
  #   image: ollama/ollama:latest
  #   ...
```

3. Update backend depends_on to remove Ollama:
```yaml
  backend:
    # ...
    depends_on:
      # ollama:
      #   condition: service_healthy
```

## Building Images

### Backend Image

```bash
# Build backend image
docker build -t java-resumes-backend:latest .

# Build with specific Java version
docker build --build-arg JAVA_VERSION=21 -t java-resumes-backend:latest .

# Build without cache
docker build --no-cache -t java-resumes-backend:latest .
```

### Frontend Image

```bash
# Build production frontend
cd frontend
docker build -f Dockerfile -t java-resumes-frontend:latest .

# Build development frontend (with hot reload)
docker build -f Dockerfile.dev -t java-resumes-frontend:dev .
```

## Development Workflow

### Development Mode (Hot Reload)

For frontend development with hot module reload:

1. Update `docker-compose.yml` to use dev Dockerfile:
```yaml
frontend:
  build:
    context: ./frontend
    dockerfile: Dockerfile.dev  # Change from Dockerfile
  ports:
    - "5173:5173"  # Vite dev server port
```

2. Mount source code as volume:
```yaml
  volumes:
    - ./frontend/src:/app/src
    - ./frontend/public:/app/public
```

3. Restart frontend service:
```bash
docker compose up -d --build frontend
```

### Backend Development

For backend development:

```bash
# Rebuild backend after code changes
docker compose build backend
docker compose up -d backend

# View backend logs
docker compose logs -f backend

# Execute commands in backend container
docker compose exec backend sh
```

### Database Management

The application uses SQLite for prompt history:

```bash
# Access SQLite database
docker compose exec backend sh
cd /app/data
sqlite3 prompts.db

# Backup database
docker compose exec backend tar czf /tmp/db-backup.tar.gz /app/data
docker compose cp backend:/tmp/db-backup.tar.gz ./backup/
```

## LLM Model Management

### Available Models

Small models suitable for resume optimization:

| Model | Size | RAM | Speed | Quality |
|-------|------|-----|-------|---------|
| qwen2.5:0.5b | 400MB | ~1GB | ⚡⚡⚡⚡⚡ | ⭐⭐ |
| tinyllama | 800MB | ~2GB | ⚡⚡⚡⚡ | ⭐⭐⭐ |
| gemma2:2b | 1.6GB | ~3GB | ⚡⚡⚡ | ⭐⭐⭐⭐ |
| phi3:mini | 2.3GB | ~4GB | ⚡⚡ | ⭐⭐⭐⭐⭐ |
| mistral | 4.1GB | ~8GB | ⚡ | ⭐⭐⭐⭐⭐ |

### Model Operations

```bash
# Pull a specific model
docker exec resume-ollama ollama pull tinyllama

# List installed models
docker exec resume-ollama ollama list

# Remove a model
docker exec resume-ollama ollama rm tinyllama

# Test model
docker exec resume-ollama ollama run tinyllama "Hello, how are you?"
```

### GPU Support

To enable GPU acceleration for Ollama:

1. Install NVIDIA Docker runtime
2. Uncomment GPU configuration in `docker-compose.yml`:
```yaml
ollama:
  deploy:
    resources:
      reservations:
        devices:
          - driver: nvidia
            count: 1
            capabilities: [gpu]
```

3. Restart Ollama service:
```bash
docker compose up -d ollama
```

## Health Checks

All services include health checks:

```bash
# Check all service health
docker compose ps

# Check backend health endpoint
curl http://localhost:8080/api/health

# Check Ollama API
curl http://localhost:11434/api/tags

# Check frontend
curl http://localhost:3000
```

## Troubleshooting

### Backend Fails to Start

**Issue**: Backend exits with connection error

**Solution**:
1. Check Ollama is running:
```bash
docker compose ps ollama
```

2. Verify Ollama health:
```bash
docker exec resume-ollama ollama list
```

3. Check backend logs:
```bash
docker compose logs backend
```

### Frontend Cannot Connect to Backend

**Issue**: API calls fail with network error

**Solution**:
1. Verify backend is running:
```bash
curl http://localhost:8080/api/health
```

2. Check network configuration:
```bash
docker network inspect resume-app-network
```

3. Verify environment variables:
```bash
docker compose exec frontend env | grep VITE_API
```

### Out of Memory

**Issue**: Containers crash or become unresponsive

**Solution**:
1. Increase Docker memory limit (Docker Desktop settings)
2. Use smaller LLM model (qwen2.5:0.5b or tinyllama)
3. Adjust Java heap size in `docker-compose.yml`:
```yaml
backend:
  environment:
    - JAVA_OPTS=-Xms256m -Xmx512m
```

### Port Conflicts

**Issue**: Port already in use

**Solution**:
1. Change port mapping in `docker-compose.yml`:
```yaml
services:
  backend:
    ports:
      - "8081:8080"  # Change host port
```

2. Update frontend API URL:
```yaml
  frontend:
    environment:
      - VITE_API_BASE_URL=http://backend:8080  # Container port stays same
```

### Database Migration Errors

**Issue**: Flyway migration fails

**Solution**:
1. Check if database volume has old schema:
```bash
docker volume rm resume-backend-files
```

2. Restart backend:
```bash
docker compose up -d backend
```

## Production Deployment

### Security Considerations

1. **Change default API keys**:
```env
LLM_APIKEY=secure-random-key-here
```

2. **Use HTTPS**:
   - Add reverse proxy (nginx, Caddy)
   - Configure SSL certificates
   - Update CORS settings

3. **Enable authentication**:
   - Implement Spring Security
   - Add JWT tokens
   - Configure OAuth2

4. **Resource limits**:
```yaml
services:
  backend:
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
```

### Backup Strategy

```bash
# Backup volumes
docker run --rm \
  -v resume-backend-files:/source \
  -v $(pwd)/backup:/backup \
  alpine tar czf /backup/backend-files.tar.gz /source

docker run --rm \
  -v resume-ollama-data:/source \
  -v $(pwd)/backup:/backup \
  alpine tar czf /backup/ollama-data.tar.gz /source
```

### Monitoring

Add monitoring services:

```yaml
services:
  prometheus:
    image: prom/prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml

  grafana:
    image: grafana/grafana
    ports:
      - "3001:3000"
    depends_on:
      - prometheus
```

## Performance Optimization

### Build Optimization

1. **Multi-stage builds**: Already implemented
2. **Layer caching**: Order Dockerfile commands from least to most frequently changing
3. **Parallel builds**:
```bash
docker compose build --parallel
```

### Runtime Optimization

1. **Use specific image tags** (not `latest`)
2. **Enable BuildKit**:
```bash
export DOCKER_BUILDKIT=1
docker compose build
```

3. **Prune unused resources**:
```bash
docker system prune -a
```

## CI/CD Integration

### GitHub Actions Example

```yaml
name: Docker Build

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Build images
        run: docker compose build

      - name: Run tests
        run: |
          docker compose up -d
          docker compose exec backend gradle test

      - name: Push to registry
        run: |
          echo ${{ secrets.DOCKER_PASSWORD }} | docker login -u ${{ secrets.DOCKER_USERNAME }} --password-stdin
          docker compose push
```

## Useful Commands

```bash
# View container resource usage
docker stats

# Inspect service configuration
docker compose config

# Recreate containers (useful after compose file changes)
docker compose up -d --force-recreate

# Scale services (if stateless)
docker compose up -d --scale backend=3

# Export logs to file
docker compose logs > docker-logs.txt

# Clean everything (including volumes)
docker compose down -v
docker system prune -a --volumes

# Update images to latest
docker compose pull
docker compose up -d
```

## Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Ollama Documentation](https://ollama.ai/docs)
- [Spring Boot Docker Guide](https://spring.io/guides/topicals/spring-boot-docker/)

## Support

For issues or questions:
1. Check logs: `docker compose logs`
2. Review health checks: `docker compose ps`
3. See troubleshooting section above
4. Open an issue on GitHub
