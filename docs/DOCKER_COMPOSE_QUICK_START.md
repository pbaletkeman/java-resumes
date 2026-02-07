# Docker Compose Quick Start with Ollama

Get up and running with the Java-Resumes application using Docker Compose with local Ollama LLM service in 2 minutes.

## Prerequisites

- Docker and Docker Compose installed
- 8GB RAM available
- 20GB free disk space (for models)

## Quick Start (3 Steps)

### Step 1: Start All Services

```bash
# Clone repository
git clone https://github.com/pbaletkeman/java-resumes.git
cd java-resumes

# Start all services including Ollama
docker-compose up -d
```

Services starting:

- ✅ **Ollama** (port 11434) - LLM inference engine
- ✅ **Backend API** (port 8080) - Spring Boot application
- ✅ **Frontend** (port 3000) - React application

### Step 2: Pull an LLM Model

In a separate terminal, pull a lightweight model:

```bash
# Pull Tinyllama (lightweight, ~4GB)
docker exec resume-ollama ollama pull tinyllama

# Or use a different model
# docker exec resume-ollama ollama pull mistral
# docker exec resume-ollama ollama pull neural-chat
```

First-time pull typically takes 5-15 minutes depending on model size and internet speed.

### Step 3: Access the Application

Open your browser:

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **Ollama API**: http://localhost:11434

## Alternative Compose Configuration

An alternative `docker-compose.frontend-backend.yml` file is available with the same services:

```bash
docker-compose -f docker-compose.frontend-backend.yml up -d
```

**Features:**

- ✅ Complete Ollama LLM service with full documentation
- ✅ Backend Spring Boot API with LLM integration
- ✅ Frontend React development server
- ✅ Improved proxy configuration for API routing
- ✅ Proper health checks and service dependencies

Use this when you need:

- Different deployment workflows
- Alternate compose file structure
- Explicit service ordering and configuration

Both files include identical Ollama configuration with:

- Quick-start instructions in inline comments
- Model persistence using Docker volumes
- Health checks for reliable startup
- Ollama API available at `http://localhost:11434`

## Verify Everything is Running

Check service health:

```bash
# View all containers
docker-compose ps

# Expected output:
# NAME              STATUS              PORTS
# resume-ollama     Up (healthy)        0.0.0.0:11434→11434
# resume-backend    Up (healthy)        0.0.0.0:8080→8080
# resume-frontend   Up                  0.0.0.0:3000→3000

# View logs
docker-compose logs -f  # All services
docker-compose logs -f backend  # Backend only
docker-compose logs -f ollama   # Ollama only
```

## Available Models

### Lightweight (4-7GB) - Recommended for Development

```bash
docker exec resume-ollama ollama pull tinyllama
docker exec resume-ollama ollama pull mistral
docker exec resume-ollama ollama pull neural-chat
```

### Standard (13-34GB) - Better Quality

```bash
docker exec resume-ollama ollama pull llama2
docker exec resume-ollama ollama pull mistral:7b
docker exec resume-ollama ollama pull llama2:13b
```

### Large (70GB+) - Best Quality (Requires 16GB+ RAM)

```bash
docker exec resume-ollama ollama pull llama2:70b
```

View installed models:

```bash
docker exec resume-ollama ollama list
```

## Common Tasks

### Restart Services

```bash
# Restart all services
docker-compose restart

# Restart specific service
docker-compose restart backend
docker-compose restart ollama
```

### Stop Services

```bash
# Stop all (preserves data)
docker-compose down

# Stop and remove volumes (WARNING: deletes models and data)
docker-compose down -v
```

### View Real-Time Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f ollama
docker-compose logs -f backend
docker-compose logs -f frontend

# Last 100 lines
docker-compose logs --tail=100
```

### Access Shell in Container

```bash
# Backend shell
docker-compose exec backend /bin/sh

# Ollama shell
docker-compose exec ollama /bin/bash

# Check Ollama model status
docker exec resume-ollama ollama list
docker exec resume-ollama curl -s http://localhost:11434/api/version
```

### Force Rebuild

```bash
# If code changes aren't reflected
docker-compose up -d --build

# Force rebuild without cache
docker-compose build --no-cache
docker-compose up -d
```

## Troubleshooting

### Ollama Reports "Unhealthy"

Ollama shows unhealthy initially but is functional. It needs time to start.

```bash
# Wait for Ollama to be ready (should see "healthy" after ~30 seconds)
docker-compose ps

# If still unhealthy after 2 minutes, check logs
docker-compose logs ollama

# Restart if needed
docker-compose restart ollama
```

### Port Already in Use

```bash
# Find what's using the port
lsof -i :3000   # Frontend
lsof -i :8080   # Backend
lsof -i :11434  # Ollama

# Stop the conflicting process or change port in docker-compose.yml
```

### Out of Memory

```bash
# Check available memory
docker stats

# Reduce model size or increase Docker memory limit
# In Docker Desktop: Settings → Resources → Memory
```

### Models Not Persisting

Models are stored in the `ollama-data` volume. Check if it exists:

```bash
docker volume ls | grep ollama

# If missing, models were lost and need to be re-pulled
docker exec resume-ollama ollama pull tinyllama
```

### Backend Cannot Connect to Ollama

The backend uses the service name `ollama` as hostname for Docker networking.

```bash
# Test connectivity from backend
docker-compose exec backend curl -s http://ollama:11434/api/version

# If fails, check docker-compose.yml networks configuration
docker network inspect resume-app-network
```

## Performance Optimization

### GPU Support (NVIDIA)

To use GPU for faster inference:

1. Install [NVIDIA Docker Runtime](https://github.com/NVIDIA/nvidia-docker)
2. Uncomment GPU section in `docker-compose.yml`
3. Restart services: `docker-compose up -d`

### CPU Optimization

For faster CPU-only inference:

- Use smaller models (tinyllama, mistral)
- Increase available CPU cores to Docker
- Docker Desktop: Settings → Resources → CPUs

### Memory Management

Models consume significant memory:

- Tinyllama: ~4GB
- Mistral: ~7GB
- Llama2: ~13GB (7B) or ~34GB (13B)

## Environment Variables

Configure LLM endpoint in `docker-compose.yml`:

```yaml
environment:
  - LLM_ENDPOINT=http://ollama:11434/v1/chat/completions
  - LLM_APIKEY=not-needed-for-local # Optional, not used with local Ollama
```

## System Resource Requirements

| Component | Minimum  | Recommended                 |
| --------- | -------- | --------------------------- |
| **RAM**   | 8GB      | 16GB+                       |
| **CPU**   | 4 cores  | 8+ cores                    |
| **Disk**  | 20GB     | 50GB                        |
| **GPU**   | Optional | NVIDIA for faster inference |

## Next Steps

- 📖 [Full Docker Setup Guide](docs/DOCKER_SETUP.md)
- 🔧 [Configuration Guide](docs/CONFIGURATION.md)
- 🚀 [Production Deployment](docs/PRODUCTION_DEPLOYMENT.md)
- 🤖 [Ollama Setup Details](docs/OLLAMA_SETUP.md)

## Getting Help

- Check logs: `docker-compose logs <service>`
- View service status: `docker-compose ps`
- Rebuild: `docker-compose up -d --build`
- Reset everything: `docker-compose down -v && docker-compose up -d`

Happy optimizing your resume! 🚀
