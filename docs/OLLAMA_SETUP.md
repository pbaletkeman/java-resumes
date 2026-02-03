# Ollama LLM Service Setup Guide

This guide explains how to set up and use the Ollama LLM service for the Java-Resumes project, both locally and in GitHub Actions CI/CD environments.

## Table of Contents

- [Overview](#overview)
- [Quick Start](#quick-start)
- [Model Options](#model-options)
- [Local Development Setup](#local-development-setup)
- [Docker Compose Setup](#docker-compose-setup)
- [GitHub Actions Integration](#github-actions-integration)
- [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)

---

## Overview

The Java-Resumes project uses Ollama to run Large Language Models (LLMs) locally for resume optimization, cover letter generation, and interview preparation. This provides:

- **Privacy**: All data stays on your machine
- **Cost**: No API fees for usage
- **Speed**: Local processing with optimized models
- **Offline**: Works without internet connection

---

## Quick Start

### Option 1: Automated Setup Script (Recommended)

```bash
# Clone repository
git clone https://github.com/pbaletkeman/java-resumes.git
cd java-resumes

# Run setup script (pulls tinyllama by default)
./scripts/setup-ollama.sh

# Or specify a different model
./scripts/setup-ollama.sh phi3:mini
```

### Option 2: Manual Setup

```bash
# Install Ollama (macOS/Linux)
curl -fsSL https://ollama.com/install.sh | sh

# Start Ollama service
ollama serve &

# Pull a small model
ollama pull tinyllama

# Verify it's working
ollama list
```

### Option 3: Docker Compose (All-in-One)

```bash
# Start all services including Ollama
docker-compose up -d

# Pull model inside container
docker exec resume-ollama ollama pull tinyllama

# Verify
docker exec resume-ollama ollama list
```

---

## Model Options

Choose a model based on your system resources and performance requirements:

| Model | Size | Parameters | Speed | Quality | Recommended For |
|-------|------|-----------|-------|---------|-----------------|
| **qwen2.5:0.5b** | ~400MB | 0.5B | ⚡⚡⚡⚡⚡ | ⭐⭐ | CI/CD, Very limited resources |
| **tinyllama** | ~800MB | 1.1B | ⚡⚡⚡⚡ | ⭐⭐⭐ | **Default**, Fast testing |
| **gemma2:2b** | ~1.6GB | 2B | ⚡⚡⚡ | ⭐⭐⭐⭐ | Balanced, Good quality |
| **phi3:mini** | ~2.3GB | 3.8B | ⚡⚡ | ⭐⭐⭐⭐⭐ | Best quality for small model |
| **mistral** | ~4.1GB | 7B | ⚡ | ⭐⭐⭐⭐⭐ | Production, High quality |

### Switching Models

```bash
# Pull and switch to a different model
ollama pull phi3:mini

# Update config.json
cat > config.json << EOF
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "ollama",
  "model": "phi3:mini"
}
EOF
```

---

## Local Development Setup

### Prerequisites

- **Operating System**: macOS, Linux, or Windows (WSL2)
- **RAM**: Minimum 4GB (8GB+ recommended)
- **Disk Space**: 2-8GB depending on model
- **CPU**: Modern x86_64 or ARM64 processor

### Installation Steps

#### macOS

```bash
# Using Homebrew
brew install ollama

# Or download from https://ollama.com
```

#### Linux

```bash
# Install Ollama
curl -fsSL https://ollama.com/install.sh | sh

# Start service (systemd)
sudo systemctl start ollama
sudo systemctl enable ollama

# Or run manually
ollama serve &
```

#### Windows (WSL2)

```bash
# Inside WSL2 terminal
curl -fsSL https://ollama.com/install.sh | sh
ollama serve &
```

### Verify Installation

```bash
# Check version
ollama --version

# Test API
curl http://localhost:11434/api/version

# Test inference
curl http://localhost:11434/api/generate -d '{
  "model": "tinyllama",
  "prompt": "Hello, world!",
  "stream": false
}'
```

---

## Docker Compose Setup

The project includes an Ollama service in `docker-compose.yml` for containerized development.

### Start Services

```bash
# Start all services (backend, frontend, ollama)
docker-compose up -d

# Check service status
docker-compose ps

# View logs
docker-compose logs ollama
```

### Initialize Ollama in Container

```bash
# Pull model inside container
docker exec resume-ollama ollama pull tinyllama

# Verify model is available
docker exec resume-ollama ollama list

# Test inference
docker exec resume-ollama ollama run tinyllama "Hello"
```

### Environment Variables

Configure Ollama service via environment variables in `.env` file:

```env
# .env file
OLLAMA_MODEL=tinyllama
LLM_ENDPOINT=http://ollama:11434/v1/chat/completions
LLM_APIKEY=not-needed-for-local
```

### GPU Support (Optional)

For NVIDIA GPU acceleration, uncomment the GPU section in `docker-compose.yml`:

```yaml
ollama:
  image: ollama/ollama:latest
  deploy:
    resources:
      reservations:
        devices:
          - driver: nvidia
            count: 1
            capabilities: [gpu]
```

Requires: `nvidia-docker` installed on host system.

---

## GitHub Actions Integration

The project includes a GitHub Actions workflow (`.github/workflows/ollama-service.yml`) that automatically sets up Ollama for CI/CD testing.

### Workflow Features

- ✅ Installs Ollama on GitHub-hosted runners
- ✅ Pulls specified model (default: tinyllama)
- ✅ Tests model inference
- ✅ Runs integration tests with backend
- ✅ Supports model selection via workflow dispatch

### Manual Workflow Trigger

Run the workflow manually with your preferred model:

1. Go to **Actions** tab in GitHub
2. Select **Ollama LLM Service** workflow
3. Click **Run workflow**
4. Select model from dropdown
5. Click **Run workflow**

### Workflow Inputs

```yaml
model:
  - tinyllama (default)
  - phi3:mini
  - gemma2:2b
  - qwen2.5:0.5b
  - mistral
```

### CI/CD Best Practices

- Use `tinyllama` or `qwen2.5:0.5b` for fast CI builds
- Use larger models (`phi3:mini`, `mistral`) for thorough testing
- Workflow caches model data for faster subsequent runs
- Artifacts saved for debugging (model info, test results)

---

## Configuration

### config.json

Primary configuration file for LLM settings:

```json
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "ollama",
  "model": "tinyllama"
}
```

**Fields:**
- `endpoint`: Ollama API endpoint (OpenAI-compatible format)
- `apikey`: API key (not required for local Ollama, but field must exist)
- `model`: Model name to use for inference

### application.yml (Backend)

Spring Boot application configuration:

```yaml
llm:
  endpoint: ${LLM_ENDPOINT:http://localhost:11434/v1/chat/completions}
  apikey: ${LLM_APIKEY:ollama}
```

Can be overridden with environment variables:
```bash
export LLM_ENDPOINT=http://localhost:11434/v1/chat/completions
export LLM_APIKEY=ollama
```

---

## Troubleshooting

### Issue: Ollama service not starting

**Symptoms:** `curl: (7) Failed to connect to localhost port 11434`

**Solutions:**
```bash
# Check if service is running
ps aux | grep ollama

# Kill existing process
pkill ollama

# Start fresh
ollama serve &

# Wait a few seconds
sleep 5

# Test connection
curl http://localhost:11434/api/version
```

### Issue: Model not found

**Symptoms:** `Error: model "tinyllama" not found`

**Solutions:**
```bash
# List installed models
ollama list

# Pull the model
ollama pull tinyllama

# Verify installation
ollama list
```

### Issue: Out of memory during model load

**Symptoms:** Process crashes or system becomes unresponsive

**Solutions:**
1. Use a smaller model:
   ```bash
   ollama pull qwen2.5:0.5b
   ```

2. Close other applications to free RAM

3. Check available memory:
   ```bash
   free -h  # Linux
   vm_stat  # macOS
   ```

### Issue: Slow inference performance

**Solutions:**
1. Use a smaller model (tinyllama, qwen2.5:0.5b)
2. Enable GPU acceleration (if available)
3. Reduce concurrent requests
4. Increase system RAM

### Issue: Docker container cannot connect to host Ollama

**Symptoms:** Backend service in Docker cannot reach Ollama on host

**Solutions:**

**Option 1:** Use Docker's Ollama service (recommended)
```bash
# Use ollama service in docker-compose.yml
docker-compose up -d ollama
```

**Option 2:** Use host.docker.internal
```yaml
# In docker-compose.yml
environment:
  - LLM_ENDPOINT=http://host.docker.internal:11434/v1/chat/completions
```

**Option 3:** Use host network mode (Linux only)
```yaml
# In docker-compose.yml
network_mode: "host"
```

### Issue: GitHub Actions workflow fails

**Common causes:**
1. **Disk space:** Free up space before model pull
2. **Timeout:** Increase timeout for large models
3. **Network:** Retry on transient network errors

**Debug:**
```bash
# Check workflow logs in GitHub Actions tab
# Look for specific error messages
# Re-run with larger model if needed
```

---

## Performance Tips

### 1. Model Selection
- **Development/Testing:** Use `tinyllama` (fast, small)
- **CI/CD:** Use `qwen2.5:0.5b` (fastest, smallest)
- **Production:** Use `phi3:mini` or `mistral` (best quality)

### 2. System Optimization
```bash
# Linux: Increase file descriptor limits
ulimit -n 4096

# Monitor resource usage
ollama ps  # Show running models
```

### 3. Batch Processing
Process multiple requests in batches to optimize model loading time.

### 4. Model Preloading
Keep Ollama service running to avoid cold start delays:
```bash
# Keep service running
ollama serve &

# Preload model
ollama run tinyllama ""
```

---

## Additional Resources

- **Ollama Official Documentation:** https://github.com/ollama/ollama
- **Model Library:** https://ollama.com/library
- **API Documentation:** https://github.com/ollama/ollama/blob/main/docs/api.md
- **Community Discord:** https://discord.gg/ollama

---

## Quick Command Reference

```bash
# Installation
curl -fsSL https://ollama.com/install.sh | sh

# Service Management
ollama serve &                    # Start service
pkill ollama                      # Stop service

# Model Management
ollama list                       # List installed models
ollama pull tinyllama            # Pull a model
ollama rm tinyllama              # Remove a model
ollama run tinyllama "prompt"    # Test model

# API Testing
curl http://localhost:11434/api/version
curl http://localhost:11434/api/tags

# Docker Commands
docker-compose up -d ollama
docker exec resume-ollama ollama list
docker exec resume-ollama ollama pull tinyllama

# Project Setup
./scripts/setup-ollama.sh
./scripts/setup-ollama.sh phi3:mini
```

---

**Last Updated:** January 2026
**Maintained By:** Java-Resumes Development Team
  
---  
  
**Last Updated:** February 2, 2026  
**Maintained By:** java-resumes development team 
