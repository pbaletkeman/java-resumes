# Docker Setup Guide

Complete guide for running the java-resumes application using Docker with multiple configuration options.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Available Docker Compose Configurations](#available-docker-compose-configurations)
- [Service Architecture](#service-architecture)
- [Service Details](#service-details)
- [Health Check Endpoints](#health-check-endpoints)
- [Database Configuration](#database-configuration)
- [Building and Running](#building-and-running)
- [LLM Model Management](#llm-model-management)

---

## 📋 Prerequisites

- Docker 20.10+ installed
- Docker Compose v2+ (or Docker with Compose plugin)
- At least 4GB RAM available for containers
- 10GB free disk space (20GB+ recommended for PostgreSQL + Ollama)

## 🐳 Available Docker Compose Configurations

This project provides 4 pre-configured docker-compose files for different deployment scenarios:

| File                                   | Services                                 | Database            | Best For                       |
| -------------------------------------- | ---------------------------------------- | ------------------- | ------------------------------ |
| `docker-compose.frontend-backend.yml`  | Frontend, Backend                        | SQLite (in-memory)  | Lightweight testing, CI/CD     |
| `docker-compose.sqlite.yml`            | Frontend, Backend                        | SQLite (persistent) | Local development, small-scale |
| `docker-compose.postgresql.yml`        | Frontend, Backend, PostgreSQL 17         | PostgreSQL          | Production-like environment    |
| `docker-compose.ollama-postgresql.yml` | Frontend, Backend, PostgreSQL 17, Ollama | PostgreSQL          | Full stack with local LLM      |

### Quick Start - Choose Your Setup

#### Option 1: Lightweight Frontend + Backend (No Database)

```bash
docker compose -f docker-compose.frontend-backend.yml up -d
```

- **Access**: Frontend at http://localhost:3000
- **Best for**: Quick testing, minimal resources
- **Database**: None (in-memory only)
- **Startup time**: ~30 seconds

#### Option 2: Frontend + Backend with SQLite

```bash
docker compose -f docker-compose.sqlite.yml up -d
```

- **Access**: Frontend at http://localhost:3000
- **Best for**: Local development, persistent data
- **Database**: SQLite file-based (persistent volume)
- **Startup time**: ~40 seconds

#### Option 3: Frontend + Backend with PostgreSQL 17

```bash
docker compose -f docker-compose.postgresql.yml up -d
```

- **Access**: Frontend at http://localhost:3000
- **Database**: PostgreSQL 17 running in container
- **Connection**: localhost:5432 (username: resume_user, password: resume_password)
- **Best for**: Production-like testing, larger datasets
- **Startup time**: ~60 seconds

#### Option 4: Full Stack with PostgreSQL + Ollama

```bash
docker compose -f docker-compose.ollama-postgresql.yml up -d

# Pull an LLM model (required for first run)
docker exec resume-ollama ollama pull tinyllama
```

- **Access**: Frontend at http://localhost:3000, Ollama at http://localhost:11434
- **Database**: PostgreSQL 17
- **LLM**: Ollama with local models
- **Best for**: Complete testing with local LLM service
- **Startup time**: ~90 seconds
- **Note**: First model pull can take 5-15 minutes depending on model size

### Stopping Services

For any configuration:

```bash
# Stop all services
docker compose -f <compose-file> down

# Stop and remove volumes (WARNING: deletes data)
docker compose -f <compose-file> down -v
```

## 🏗️ Service Architecture

### Standard Setup (3 Services)

```

   Frontend (React)
   Port: 3000

              HTTP

  Backend (Spring Boot)
   Port: 8080

              JDBC

   Database
   (SQLite/PostgreSQL)

```

### Full Stack Setup (4 Services)

```

   Frontend (React)
   Port: 3000

              HTTP

  Backend (Spring Boot)
   Port: 8080


       JDBC         HTTP


PostgreSQL     Ollama
Port 5432     Port 11434

```

## Service Details

### Frontend Service

- **Image**: Built from `./frontend`
- **Port**: 3000
- **Technology**: React 19, TypeScript, Vite
- **Dockerfile**: `./frontend/Dockerfile.dev` (for development)
- **Environment**:
  - `VITE_API_BASE_URL`: Backend API URL (default: http://backend:8080)
  - `NODE_ENV`: development or production

### Backend Service

- **Image**: Built from `./Dockerfile`
- **Port**: 8080
- **Technology**: Java 21, Spring Boot 3.5.1
- **Dockerfile**: `./Dockerfile`
- **Environment Variables**:
  - `SPRING_PROFILES_ACTIVE`: Application profile (prod, postgresql, etc)
  - `UPLOAD_PATH`: File upload directory
  - Database-specific variables (see Database Configuration section)
  - LLM endpoint configuration

### PostgreSQL Service (PostgreSQL configs only)

- **Image**: `postgres:17-alpine`
- **Port**: 5432
- **Default Credentials**:
  - Database: `resume_db`
  - Username: `resume_user`
  - Password: `resume_password`
- **Volume**: Persistent data storage at `resume-*-postgres-data`
- **Health Check**: pg_isready (30s interval)

### Ollama Service (Full-stack only)

- **Image**: `ollama/ollama:latest`
- **Port**: 11434
- **Purpose**: Local LLM inference
- **Volume**: Model storage at `resume-ollama-data`
- **Health Check**: ollama list command (30s interval)
- **Optional**: GPU support available (see LLM Model Management)

## Health Check Endpoints

The backend includes comprehensive health check endpoints for monitoring system status.

### Overall System Health

```bash
curl http://localhost:8080/api/health
```

Returns combined status of all system components with overall_status field.

### Individual Health Checks

Database connectivity:

```bash
curl http://localhost:8080/api/health/database
```

LLM service connectivity:

```bash
curl http://localhost:8080/api/health/llm
```

Disk space availability:

```bash
curl http://localhost:8080/api/health/disk
```

### Example Health Response

```json
{
  "status": "UP",
  "timestamp": "2024-02-02T10:30:00Z",
  "service": "Resume Optimization API",
  "database": {
    "type": "PostgreSQL",
    "status": "UP",
    "message": "Database connection successful",
    "url": "jdbc:postgresql://postgres:5432/resume_db"
  },
  "llm": {
    "status": "UP",
    "message": "LLM service is reachable",
    "endpoint": "http://ollama:11434/v1/chat/completions",
    "response_code": 200
  },
  "disk": {
    "status": "UP",
    "message": "Disk space is available",
    "usable_mb": 2048,
    "total_mb": 10240,
    "free_mb": 3072,
    "usage_percent": 70
  },
  "overall_status": "UP"
}
```

### Health Check Status Meanings

- **UP**: Component is working correctly
- **DOWN**: Component is not responding or has errors
- **DEGRADED**: System is functioning but with issues
- **DISABLED**: Component is not configured
- **WARNING**: Disk space below 500MB
- **CRITICAL**: Disk space below 100MB

## Database Configuration

### SQLite Configuration

Used in: `docker-compose.sqlite.yml`

Configuration file: `src/main/resources/application.yml`

- **URL**: `jdbc:sqlite:/app/data/prompts.db`
- **Migrations**: `classpath:db/migration/sqlite`
- **Advantages**:
  - Simple setup, no external database required
  - Good for development and testing
  - Persistent data stored in Docker volume
  - File-based, can be backed up easily
- **Limitations**:
  - Not suitable for concurrent heavy access
  - Limited to local single-server scenarios
  - No advanced features like replication

SQLite Database Operations:

```bash
# Access SQLite database
docker compose -f docker-compose.sqlite.yml exec backend sh
cd /app/data
sqlite3 prompts.db

# Useful SQLite commands (in sqlite3)
.tables                                    # List tables
.schema prompt_history                     # View table structure
SELECT COUNT(*) FROM prompt_history;       # Count rows
SELECT * FROM prompt_history LIMIT 5;      # View sample data
.quit                                      # Exit

# Backup database
docker compose -f docker-compose.sqlite.yml exec backend \
  tar czf /tmp/db-backup.tar.gz /app/data

# Copy backup to host
docker compose -f docker-compose.sqlite.yml cp \
  backend:/tmp/db-backup.tar.gz ./backup/
```

### PostgreSQL Configuration

Used in: `docker-compose.postgresql.yml` and `docker-compose.ollama-postgresql.yml`

Configuration file: `src/main/resources/application-postgresql.yml`

**Default Environment Variables**:

```yaml
SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/resume_db
SPRING_DATASOURCE_USERNAME: resume_user
SPRING_DATASOURCE_PASSWORD: resume_password
SPRING_DATASOURCE_DRIVER_CLASS_NAME: org.postgresql.Driver
SPRING_JPA_DATABASE_PLATFORM: org.hibernate.dialect.PostgreSQLDialect
SPRING_JPA_HIBERNATE_DDL_AUTO: validate
SPRING_FLYWAY_LOCATIONS: classpath:db/migration/postgresql
```

**Custom Credentials**: Edit the postgres service in docker-compose file:

```yaml
postgres:
  environment:
    POSTGRES_DB: your_db_name
    POSTGRES_USER: your_username
    POSTGRES_PASSWORD: your_password
```

Then update backend environment variables to match.

**PostgreSQL Database Operations**:

```bash
# Connect to PostgreSQL container
docker compose -f docker-compose.postgresql.yml exec postgres \
  psql -U resume_user -d resume_db

# Useful PostgreSQL commands (in psql)
\dt                                  # List tables
\d prompt_history                    # View table structure
SELECT COUNT(*) FROM prompt_history; # Count rows
SELECT * FROM prompt_history LIMIT 5;# View sample data
\q                                   # Exit psql

# Backup PostgreSQL database
docker compose -f docker-compose.postgresql.yml exec postgres \
  pg_dump -U resume_user resume_db > backup-$(date +%Y%m%d).sql

# Restore from backup
cat backup-2024-02-02.sql | \
  docker compose -f docker-compose.postgresql.yml exec -T postgres \
  psql -U resume_user resume_db

# Export table as CSV
docker compose -f docker-compose.postgresql.yml exec postgres \
  psql -U resume_user -d resume_db \
  -c "COPY prompt_history TO STDOUT WITH CSV HEADER" > export.csv

# Check database size
docker compose -f docker-compose.postgresql.yml exec postgres \
  psql -U resume_user -d resume_db \
  -c "SELECT pg_size_pretty(pg_database_size('resume_db'));"
```

### Database Initialization

Flyway handles automatic schema migration on startup:

- **SQLite migrations**: `src/main/resources/db/migration/sqlite/`
- **PostgreSQL migrations**: `src/main/resources/db/migration/postgresql/`

Create a new migration:

```bash
# Create migration file
touch src/main/resources/db/migration/postgresql/V2__add_new_column.sql

# Add SQL statements
cat > src/main/resources/db/migration/postgresql/V2__add_new_column.sql << 'EOF'
ALTER TABLE prompt_history ADD COLUMN new_column TEXT;
CREATE INDEX idx_new_column ON prompt_history(new_column);
EOF

# Restart backend to trigger migration
docker compose -f docker-compose.postgresql.yml up -d --build backend

# Verify migration
docker compose -f docker-compose.postgresql.yml logs backend | grep Flyway
```

## Building and Running

### Build Specific Services

```bash
# Build all services in a compose file
docker compose -f docker-compose.postgresql.yml build

# Build specific service only
docker compose -f docker-compose.postgresql.yml build backend

# Build with no cache (forces full rebuild)
docker compose -f docker-compose.postgresql.yml build --no-cache

# Build in parallel (faster)
export DOCKER_BUILDKIT=1
docker compose -f docker-compose.postgresql.yml build --parallel
```

### Running Services

```bash
# Start services in background
docker compose -f docker-compose.postgresql.yml up -d

# Start with automatic rebuild
docker compose -f docker-compose.postgresql.yml up -d --build

# Start specific service
docker compose -f docker-compose.postgresql.yml up -d backend

# Run in foreground (see logs in real-time)
docker compose -f docker-compose.postgresql.yml up

# Restart all services
docker compose -f docker-compose.postgresql.yml restart

# Restart specific service
docker compose -f docker-compose.postgresql.yml restart backend
```

## LLM Model Management

Available for: `docker-compose.ollama-postgresql.yml`

### Available Models

Small models suitable for resume optimization:

| Model        | Size  | RAM  | Speed | Quality | Notes                   |
| ------------ | ----- | ---- | ----- | ------- | ----------------------- |
| qwen2.5:0.5b | 400MB | ~1GB |       |         | Fastest, lowest memory  |
| tinyllama    | 800MB | ~2GB |       |         | Recommended for testing |
| gemma2:2b    | 1.6GB | ~3GB |       |         | Good balance            |
| phi3:mini    | 2.3GB | ~4GB |       |         | High quality            |
| mistral      | 4.1GB | ~8GB |       |         | Best quality, slowest   |

### Model Operations

For any compose file with Ollama:

```bash
# Pull a specific model (downloads model from Ollama registry)
docker compose -f docker-compose.ollama-postgresql.yml exec ollama \
  ollama pull tinyllama

# List installed models
docker compose -f docker-compose.ollama-postgresql.yml exec ollama \
  ollama list

# Remove a model (frees disk space)
docker compose -f docker-compose.ollama-postgresql.yml exec ollama \
  ollama rm tinyllama

# Test model interactively
docker compose -f docker-compose.ollama-postgresql.yml exec ollama \
  ollama run tinyllama "Hello, how are you?"

# Show detailed model information
docker compose -f docker-compose.ollama-postgresql.yml exec ollama \
  ollama show tinyllama

# View model performance details
docker compose -f docker-compose.ollama-postgresql.yml exec ollama \
  ollama show --verbose tinyllama
```

### GPU Support

To enable GPU acceleration for Ollama (significantly faster inference):

**System Requirements**:

- NVIDIA GPU with CUDA support
- NVIDIA Docker runtime installed

**Installation Steps**:

1. **Install NVIDIA Docker runtime** (Linux):

```bash
# Ubuntu/Debian
sudo apt-get install -y nvidia-docker2
sudo systemctl restart docker

# Verify installation
docker run --rm --gpus all nvidia/cuda:11.0.3-base-ubuntu20.04 nvidia-smi
```

2. **Uncomment GPU configuration** in docker-compose file:

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

3. **Restart Ollama**:

```bash
docker compose -f docker-compose.ollama-postgresql.yml up -d ollama
```

4. **Verify GPU access**:

```bash
docker compose -f docker-compose.ollama-postgresql.yml exec ollama \
  nvidia-smi
```

## Monitoring and Logging

### View Service Status

```bash
# Check all services and their status
docker compose -f docker-compose.ollama-postgresql.yml ps

# Example output:
# NAME                            STATUS           PORTS
# resume-frontend                 Up 2 minutes     0.0.0.0:3000->3000/tcp
# resume-backend                  Up 2 minutes     0.0.0.0:8080->8080/tcp
# resume-postgres-ollama          Up 2 minutes     0.0.0.0:5432->5432/tcp
# resume-ollama                   Up 2 minutes     0.0.0.0:11434->11434/tcp
```

### View Logs

```bash
# View all logs for all services
docker compose -f docker-compose.postgresql.yml logs

# View logs for specific service
docker compose -f docker-compose.postgresql.yml logs backend

# Follow logs in real-time (like tail -f)
docker compose -f docker-compose.postgresql.yml logs -f frontend

# Show last 100 lines
docker compose -f docker-compose.postgresql.yml logs --tail=100 backend

# Show logs from last 30 minutes
docker compose -f docker-compose.postgresql.yml logs --since=30m backend

# Show logs with timestamps
docker compose -f docker-compose.postgresql.yml logs -t backend
```

### Container Health and Resources

```bash
# Monitor real-time resource usage (CPU, memory, I/O)
docker stats --no-stream

# Watch continuously
watch docker stats

# Inspect container details
docker inspect resume-backend

# View environment variables in container
docker compose -f docker-compose.postgresql.yml exec backend env

# View mounted volumes
docker inspect -f '{{json .Mounts}}' resume-backend | python -m json.tool

# Get container IP address
docker inspect -f '{{.NetworkSettings.IPAddress}}' resume-backend
```

## Troubleshooting

### Backend Fails to Start

**Issue**: Backend container exits immediately or fails health check

**Solution**:

```bash
# Check logs for specific errors
docker compose -f docker-compose.postgresql.yml logs backend

# Verify database is running and healthy
docker compose -f docker-compose.postgresql.yml ps postgres
docker compose -f docker-compose.postgresql.yml logs postgres

# Check database health endpoint
curl http://localhost:8080/api/health/database

# Check application logs
docker compose -f docker-compose.postgresql.yml exec backend cat /app/logs/application.log

# Restart backend
docker compose -f docker-compose.postgresql.yml restart backend
```

### Database Connection Errors

**Issue**: "Connection refused", "database does not exist", or migration fails

**Solution**:

```bash
# Verify database is ready
docker compose -f docker-compose.postgresql.yml exec postgres \
  pg_isready -U resume_user -d resume_db

# Check database logs for errors
docker compose -f docker-compose.postgresql.yml logs postgres

# Verify database credentials
docker compose -f docker-compose.postgresql.yml exec postgres \
  psql -U resume_user -d resume_db -c "SELECT 1;"

# Restart database
docker compose -f docker-compose.postgresql.yml restart postgres

# Check Flyway migration status
docker compose -f docker-compose.postgresql.yml logs backend | grep -i flyway
```

### Frontend Cannot Connect to Backend

**Issue**: API calls fail with network error or CORS error

**Solution**:

```bash
# Test backend connectivity from host
curl http://localhost:8080/api/health

# Test backend connectivity from frontend container
docker compose -f docker-compose.postgresql.yml exec frontend \
  curl http://backend:8080/api/health

# Check frontend environment variables
docker compose -f docker-compose.postgresql.yml exec frontend \
  env | grep VITE_API

# Check network connectivity
docker network inspect resume-postgres-network

# Verify CORS configuration in backend
curl -H "Origin: http://localhost:3000" http://localhost:8080/api/health -v
```

### Out of Memory

**Issue**: Containers crash, performance degrades, or "OutOfMemoryError"

**Solution**:

1. Increase Docker memory limit:
   - Docker Desktop: Preferences Resources Memory
   - Docker on Linux: Edit `/etc/docker/daemon.json`

2. Use smaller LLM model:

```bash
docker compose -f docker-compose.ollama-postgresql.yml exec ollama \
  ollama pull qwen2.5:0.5b
```

3. Reduce Java heap size in docker-compose:

```yaml
backend:
  environment:
    - JAVA_OPTS=-Xms128m -Xmx512m
```

4. Monitor memory usage:

```bash
docker stats --no-stream | grep backend
```

### Port Conflicts

**Issue**: "Port already in use" or "address already in use"

**Solution**:

```bash
# Find process using port (Linux/Mac)
lsof -i :8080

# Find process using port (Windows)
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Change port in docker-compose
# Update host port (first number in port mapping)
ports:
  - "8081:8080"  # Use 8081 instead of 8080

# Restart services
docker compose -f docker-compose.postgresql.yml up -d
```

### Ollama Not Responding

**Issue**: LLM health check fails, model pull times out

**Solution**:

```bash
# Check Ollama container status
docker compose -f docker-compose.ollama-postgresql.yml ps ollama

# View Ollama logs
docker compose -f docker-compose.ollama-postgresql.yml logs ollama

# Restart Ollama
docker compose -f docker-compose.ollama-postgresql.yml restart ollama

# Check Ollama API endpoint
curl http://localhost:11434/api/tags

# Pull model with verbose output
docker compose -f docker-compose.ollama-postgresql.yml exec ollama \
  ollama pull tinyllama --verbose
```

### Disk Space Issues

**Issue**: Container fails to start, disk full error

**Solution**:

```bash
# Check disk usage
docker system df

# Check specific container logs
docker compose -f docker-compose.postgresql.yml logs backend | tail -20

# Clean up unused Docker resources
docker system prune -a

# Remove unused volumes
docker volume prune

# Check available disk space
df -h

# Remove old images
docker image prune -a
```

## Production Deployment

### Security Best Practices

1. **Change default database password**:

```yaml
postgres:
  environment:
    POSTGRES_PASSWORD: $(openssl rand -base64 32)
```

2. **Use HTTPS/TLS**:
   - Add reverse proxy (nginx, Traefik, Caddy)
   - Configure SSL/TLS certificates
   - Update CORS settings for production domain

3. **Enable authentication**:
   - Implement Spring Security
   - Add JWT token validation
   - Configure OAuth2 if needed

4. **Set resource limits** to prevent resource exhaustion:

```yaml
backend:
  deploy:
    resources:
      limits:
        cpus: "2"
        memory: 2G
      reservations:
        cpus: "1"
        memory: 1G
```

5. **Use environment variables** for sensitive data (never commit to git):

```bash
# Use .env file (add to .gitignore)
docker compose -f docker-compose.postgresql.yml --env-file .env up -d
```

### Backup Strategy

```bash
# Backup PostgreSQL database (compressed)
docker compose -f docker-compose.postgresql.yml exec postgres \
  pg_dump -U resume_user resume_db | gzip > backup-$(date +%Y%m%d-%H%M%S).sql.gz

# Backup all volumes
docker run --rm \
  -v resume-postgres-backend-files:/source \
  -v $(pwd)/backup:/backup \
  alpine tar czf /backup/backend-files-$(date +%Y%m%d).tar.gz /source

# Restore database
gunzip < backup-2024-02-02.sql.gz | \
  docker compose -f docker-compose.postgresql.yml exec -T postgres \
  psql -U resume_user resume_db

# Restore volumes
tar xzf /backup/backend-files-2024-02-02.tar.gz -C /
```

### Monitoring and Observability

```bash
# Monitor real-time resource usage
watch 'docker stats --no-stream'

# Export logs for analysis
docker compose -f docker-compose.postgresql.yml logs > app-logs.txt

# Health check monitoring
watch 'curl -s http://localhost:8080/api/health | python -m json.tool'

# Database monitoring
docker compose -f docker-compose.postgresql.yml exec postgres \
  psql -U resume_user -d resume_db \
  -c "SELECT datname, pg_size_pretty(pg_database_size(datname)) FROM pg_database;"
```

## Performance Optimization

### Build Optimization

```bash
# Enable BuildKit for faster, more efficient builds
export DOCKER_BUILDKIT=1
docker compose -f docker-compose.postgresql.yml build --parallel

# Use layer caching (order Dockerfile commands from least to most frequently changing)
# Use .dockerignore to exclude unnecessary files
```

### Runtime Optimization

```bash
# Use specific image tags (not 'latest') for reproducibility
# Prevents unnecessary pulls and improves caching

# Prune unused resources
docker system prune -a          # Remove unused images, containers, networks
docker volume prune             # Remove unused volumes
docker image prune -a           # Remove dangling images

# Monitor resource usage and optimize
docker stats --no-stream
```

## Useful Commands Summary

```bash
# Service management
docker compose -f <file> up -d              # Start services
docker compose -f <file> down               # Stop services
docker compose -f <file> restart backend    # Restart service
docker compose -f <file> ps                 # List services

# Build and images
docker compose -f <file> build              # Build all images
docker compose -f <file> build --no-cache   # Force rebuild
docker compose -f <file> pull               # Update images

# Logs and debugging
docker compose -f <file> logs               # View logs
docker compose -f <file> logs -f            # Follow logs
docker compose -f <file> exec backend sh    # Shell access

# Data management
docker compose -f <file> exec postgres psql -U resume_user -d resume_db
docker compose -f <file> cp backend:/app/data ./backup/

# Cleanup
docker compose -f <file> down -v            # Remove volumes
docker system prune -a --volumes            # Full cleanup
```

## Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Ollama Documentation](https://ollama.ai/docs)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Spring Boot Docker Guide](https://spring.io/guides/topicals/spring-boot-docker/)

## Support and Issues

For troubleshooting:

1. Check health endpoints: `curl http://localhost:8080/api/health`
2. Review logs: `docker compose -f <file> logs -f`
3. Verify all services are running: `docker compose -f <file> ps`
4. Check resource usage: `docker stats --no-stream`
5. Open an issue on GitHub with logs and configuration details

For configuration help, refer to:

- Specific docker-compose file for environment variables
- `src/main/resources/application*.yml` for application configuration
- This guide's Database Configuration and Health Check sections

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
