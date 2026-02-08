# Quick Start Guide

Get Java Resumes up and running in minutes using Docker Compose.

- [Quick Start Guide](#quick-start-guide)
  - [📋 Prerequisites](#-prerequisites)
  - [📝 Step-by-Step Setup](#-step-by-step-setup)
    - [Step 1: Clone the Repository](#step-1-clone-the-repository)
    - [Step 2: Configure LLM Service](#step-2-configure-llm-service)
      - [Option A: Ollama (Recommended for Local Development)](#option-a-ollama-recommended-for-local-development)
      - [Option B: LM Studio](#option-b-lm-studio)
      - [Option C: OpenAI API](#option-c-openai-api)
    - [Step 3: Choose Your Deployment Configuration](#step-3-choose-your-deployment-configuration)
      - [Quick Start Commands](#quick-start-commands)
    - [Step 4: Start the Application](#step-4-start-the-application)
    - [Step 5: Access the Application](#step-5-access-the-application)
    - [Step 6: Test It Out](#step-6-test-it-out)
  - [🔨 Common Commands](#-common-commands)
    - [View Logs](#view-logs)
    - [Stop the Application](#stop-the-application)
    - [Rebuild and Restart](#rebuild-and-restart)
    - [Check Container Status](#check-container-status)
    - [Execute Commands Inside Container](#execute-commands-inside-container)
  - [🐛 Troubleshooting](#-troubleshooting)
    - [Port Already in Use](#port-already-in-use)
    - [LLM Connection Failed](#llm-connection-failed)
    - [Docker Image Won't Build](#docker-image-wont-build)
    - [Files Not Persisting](#files-not-persisting)
  - [➡️ Next Steps](#️-next-steps)
    - [For Production Deployment](#for-production-deployment)
    - [For Development](#for-development)
    - [For Configuration](#for-configuration)
  - [🌍 Environment Files](#-environment-files)
    - [`.env` for Backend Environment](#env-for-backend-environment)
    - [`.env` for Frontend Environment](#env-for-frontend-environment)
  - [⚡ Performance Tips](#-performance-tips)
  - [🐳 Docker Compose Reference](#-docker-compose-reference)
    - [Complete docker-compose.yml Structure](#complete-docker-composeyml-structure)
  - [💬 Getting Help](#-getting-help)

---

## 📋 Prerequisites

- **Docker** 20.10+ - [Get Docker](https://docs.docker.com/get-docker/)
- **Docker Compose** 2.0+ - (included with Docker Desktop)
- **LLM Service** running locally or API key for cloud service
- **Git** for cloning the repository

## 📝 Step-by-Step Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/pbaletkeman/java-resumes.git
cd java-resumes
```

### Step 2: Configure LLM Service

The application needs an LLM service to function. Choose one option:

#### Option A: Ollama (Recommended for Local Development)

1. **Install Ollama:** <https://ollama.ai>
2. **Start Ollama server:**

```bash
ollama serve
```

3. **In another terminal, pull a model:**

```bash
ollama pull mistral:latest
```

4. **Edit `config.json` in project root:**

```json
{
  "endpoint": "http://host.docker.internal:11434/v1/chat/completions",
  "apikey": "not-needed-for-local",
  "model": "mistral:latest"
}
```

#### Option B: LM Studio

1. **Download LM Studio:** <https://lmstudio.ai>
2. **Load a model and start the server**
3. **Note the server endpoint** (usually `http://localhost:1234`)
4. **Edit `config.json`:**

```json
{
  "endpoint": "http://host.docker.internal:1234/v1/chat/completions",
  "apikey": "not-needed",
  "model": "your-model-name"
}
```

#### Option C: OpenAI API

1. **Get your API key:** <https://platform.openai.com/api-keys>
2. **Edit `config.json`:**

```json
{
  "endpoint": "https://api.openai.com/v1/chat/completions",
  "apikey": "sk-your-api-key-here",
  "model": "gpt-4"
}
```

### Step 3: Choose Your Deployment Configuration

The project includes **4 different docker-compose files** optimized for different use cases:

| Configuration         | Use Case             | Database            | LLM Service | Startup Time |
| --------------------- | -------------------- | ------------------- | ----------- | ------------ |
| **frontend-backend**  | CI/CD, quick testing | In-memory           | No          | ~30s         |
| **sqlite**            | Development          | SQLite (persistent) | No          | ~40s         |
| **postgresql**        | Production-like      | PostgreSQL 17       | No          | ~60s         |
| **ollama-postgresql** | Full-stack local     | PostgreSQL 17       | Ollama      | ~90s+        |

#### Quick Start Commands

**Option 1: Lightweight (Frontend + Backend only)**

```bash
docker compose -f docker-compose.frontend-backend.yml up --build
```

Best for: CI/CD pipelines, quick testing, environments with minimal resources

**Option 2: Development with SQLite**

```bash
docker compose -f docker-compose.sqlite.yml up --build
```

Best for: Local development with persistent storage, simpler setup

**Option 3: Production-like with PostgreSQL**

```bash
docker compose -f docker-compose.postgresql.yml up --build
```

Best for: Testing production configuration, scale testing, team development

**Option 4: Full Stack with Ollama + PostgreSQL** _(Recommended)_

```bash
docker compose -f docker-compose.ollama-postgresql.yml up --build

# In another terminal, pull an LLM model:
docker exec resume-ollama ollama pull mistral
```

Best for: Complete local development with all services, realistic production simulation

For detailed information about each configuration, databases, health checks, and troubleshooting, see **[DOCKER_SETUP.md](DOCKER_SETUP.md)**.

### Step 4: Start the Application

If using the default configuration (frontend-backend):

```bash
docker compose up --build
```

Or choose one of the configurations above.

This will:

- Build the frontend Docker image (~5 minutes)
- Build the backend Docker image (~8 minutes)
- Start both containers
- Create a Docker network for inter-container communication
- Set up persistent file storage volume

First-time builds take longer; subsequent starts are faster.

### Step 5: Access the Application

Once all services are running, access:

- **Frontend UI:** <http://localhost> (or <http://localhost:3000>)
- **Backend API:** <http://localhost:8080>
- **API Documentation:** <http://localhost:8080/swagger-ui/index.html>
- **Health Check:** <http://localhost:8080/api/health>

### Step 6: Test It Out

1. **Open the web interface** at <http://localhost>
2. **Enter a job description** or upload a job posting
3. **Paste or upload your resume**
4. **Select optimization types** (Resume, Cover Letter, or both)
5. **Click "Process"** and wait for optimization
6. **Download the results** from the file history panel

## 🔨 Common Commands

### View Logs

```bash
# All containers
docker compose logs -f

# Specific service
docker compose logs -f frontend
docker compose logs -f backend
```

### Stop the Application

```bash
# Stop containers (preserves data)
docker compose down

# Stop and remove volumes (deletes all data)
docker compose down -v
```

### Rebuild and Restart

```bash
docker compose up --build --force-recreate
```

### Check Container Status

```bash
docker compose ps
```

### Execute Commands Inside Container

```bash
# In backend container
docker compose exec backend sh

# In frontend container
docker compose exec frontend sh
```

## 🐛 Troubleshooting

### Port Already in Use

If port 8080 or 80 is already in use:

1. **Find what's using the port:**

```bash
# Linux/Mac
lsof -i :8080

# Windows
netstat -ano | findstr :8080
```

2. **Stop the conflicting service or change the port** in `docker-compose.yml`:

```yaml
services:
  backend:
    ports:
      - "8081:8080" # Changed from 8080:8080
```

### LLM Connection Failed

**Error:** "Cannot connect to LLM service"

1. **Verify LLM service is running**
2. **Check `config.json` endpoint is correct**
3. **For local services, use `host.docker.internal` not `localhost`**
4. **Test connection manually:**

```bash
curl -X POST http://host.docker.internal:11434/api/chat \
  -H "Content-Type: application/json" \
  -d '{"model":"mistral","messages":[{"role":"user","content":"test"}]}'
```

### Docker Image Won't Build

**Solution:** Clean and rebuild

```bash
docker compose down -v
docker system prune -a
docker compose up --build
```

### Files Not Persisting

Ensure the volume is configured in `docker-compose.yml`:

```yaml
volumes:
  backend-files:
    driver: local
```

And backend service has the volume mount:

```yaml
volumes:
  - backend-files:/app/files
```

## ➡️ Next Steps

### For Production Deployment

See **[PRODUCTION_DEPLOYMENT.md](PRODUCTION_DEPLOYMENT.md)** for:

- HTTPS/TLS configuration
- External configuration setup
- Environment-specific settings
- Security considerations

### For Development

See **[DEVELOPMENT_SETUP.md](DEVELOPMENT_SETUP.md)** for:

- Backend development setup
- Frontend development setup
- Running without Docker
- Hot reload and debugging

### For Configuration

See **[CONFIGURATION.md](CONFIGURATION.md)** for:

- External config paths
- External prompts directory
- Environment variable setup
- Advanced configuration options

## 🌍 Environment Files

### `.env` for Backend Environment

Create a `.env` file in the project root (optional):

```env
SPRING_PROFILES_ACTIVE=prod
UPLOAD_PATH=files
LLM_ENDPOINT=http://host.docker.internal:11434/v1/chat/completions
LLM_APIKEY=not-needed
PROMPTS_DIR=/custom/prompts
CONFIG_PATH=/custom/config
```

### `.env` for Frontend Environment

Create `frontend/.env` (optional):

```env
VITE_API_BASE_URL=http://localhost:8080
```

## ⚡ Performance Tips

1. **Allocate more memory** to Docker if you have it available
2. **Use lightweight models** initially (mistral-7b vs mistral-large)
3. **Monitor container logs** to identify bottlenecks
4. **For frequent use,** keep models loaded in memory

## 🐳 Docker Compose Reference

### Complete docker-compose.yml Structure

```yaml
version: "3.8"

services:
  frontend:
    build:
      context: .
      dockerfile: frontend/Dockerfile
    ports:
      - "80:80"
    depends_on:
      - backend
    networks:
      - resume-app-network

  backend:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - UPLOAD_PATH=files
      - LLM_ENDPOINT=${LLM_ENDPOINT}
      - LLM_APIKEY=${LLM_APIKEY}
      - PROMPTS_DIR=${PROMPTS_DIR}
    volumes:
      - backend-files:/app/files
    networks:
      - resume-app-network

volumes:
  backend-files:
    driver: local

networks:
  resume-app-network:
    driver: bridge
```

## 💬 Getting Help

- **Configuration issues?** [CONFIGURATION.md](CONFIGURATION.md)
- **Specific problem?** [TROUBLESHOOTING.md](TROUBLESHOOTING.md)
- **Need more setup info?** [DEVELOPMENT_SETUP.md](DEVELOPMENT_SETUP.md)
- **Bug or feature request?** [GitHub Issues](https://github.com/pbaletkeman/java-resumes/issues)

---

**Ready?** Start with `docker compose up --build` and follow the steps above!

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
