# Configuration Guide

Learn how to configure Java Resumes for different environments and customize external paths.

- [Configuration Guide](#configuration-guide)
  - [Overview](#overview)
  - [LLM Configuration (config.json)](#llm-configuration-configjson)
    - [File Location](#file-location)
    - [Configuration Structure](#configuration-structure)
    - [Configuration Parameters](#configuration-parameters)
    - [LLM Service Examples](#llm-service-examples)
      - [Ollama (Local)](#ollama-local)
      - [LM Studio (Local)](#lm-studio-local)
      - [OpenAI (Cloud)](#openai-cloud)
      - [Azure OpenAI](#azure-openai)
  - [External Configuration Path](#external-configuration-path)
    - [Setting External Config Location](#setting-external-config-location)
      - [Option 1: System Property](#option-1-system-property)
      - [Option 2: Environment Variable](#option-2-environment-variable)
      - [Option 3: Docker Compose](#option-3-docker-compose)
    - [How External Config Works](#how-external-config-works)
    - [Production Deployment Example](#production-deployment-example)
  - [External Prompts Directory](#external-prompts-directory)
    - [Setting Custom Prompts](#setting-custom-prompts)
      - [In application.yml](#in-applicationyml)
      - [Via Environment Variable](#via-environment-variable)
      - [In docker-compose.yml](#in-docker-composeyml)
    - [Prompt Files](#prompt-files)
    - [Prompt Loading Behavior](#prompt-loading-behavior)
    - [Customizing Prompts](#customizing-prompts)
  - [Spring Boot Configuration (application.yml)](#spring-boot-configuration-applicationyml)
    - [File Location](#file-location-1)
    - [Common Settings](#common-settings)
    - [Spring Profiles](#spring-profiles)
  - [Environment Variables](#environment-variables)
    - [Complete List](#complete-list)
    - [Setting Environment Variables](#setting-environment-variables)
      - [Linux/Mac](#linuxmac)
      - [Windows (PowerShell)](#windows-powershell)
      - [Windows (Command Prompt)](#windows-command-prompt)
  - [Docker Configuration](#docker-configuration)
    - [docker-compose.yml with External Config](#docker-composeyml-with-external-config)
    - [.env File for Docker Compose](#env-file-for-docker-compose)
  - [Configuration Priority](#configuration-priority)
  - [Troubleshooting Configuration](#troubleshooting-configuration)
    - ["Could not load config"](#could-not-load-config)
    - ["Cannot connect to LLM"](#cannot-connect-to-llm)
    - ["Prompts not loading"](#prompts-not-loading)
    - [Config Changes Not Applied](#config-changes-not-applied)
  - [Best Practices](#best-practices)
    - [Development](#development)
    - [Production](#production)

---

## 🌍 Overview

The application supports flexible configuration through:

1. **config.json** - LLM service settings
2. **application.yml** - Spring Boot settings
3. **Environment variables** - Runtime overrides
4. **External configuration paths** - For production deployments

## 🤖 LLM Configuration (config.json)

### File Location

`config.json` in the project root directory. Docker Compose looks here by default.

### Configuration Structure

```json
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "your-api-key",
  "model": "mistral:latest"
}
```

### Configuration Parameters

| Parameter  | Type   | Description              | Examples                                     |
| ---------- | ------ | ------------------------ | -------------------------------------------- |
| `endpoint` | string | LLM service API endpoint | `http://localhost:11434/v1/chat/completions` |
| `apikey`   | string | API authentication key   | `sk-xxx` for OpenAI, `not-needed` for local  |
| `model`    | string | LLM model identifier     | `mistral`, `gpt-4`, `llama2`                 |

### LLM Service Examples

#### Ollama (Local)

```json
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "not-needed-for-local",
  "model": "mistral:latest"
}
```

**Docker environment:**

```json
{
  "endpoint": "http://host.docker.internal:11434/v1/chat/completions",
  "apikey": "not-needed-for-local",
  "model": "mistral:latest"
}
```

#### LM Studio (Local)

```json
{
  "endpoint": "http://localhost:1234/v1/chat/completions",
  "apikey": "not-needed",
  "model": "your-loaded-model"
}
```

#### OpenAI (Cloud)

```json
{
  "endpoint": "https://api.openai.com/v1/chat/completions",
  "apikey": "sk-your-api-key-here",
  "model": "gpt-4"
}
```

#### Azure OpenAI

```json
{
  "endpoint": "https://{resource}.openai.azure.com/openai/deployments/{deployment}/chat/completions?api-version=2024-02-01",
  "apikey": "your-azure-api-key",
  "model": "gpt-4"
}
```

## 💿 External Configuration Path

### Setting External Config Location

#### Option 1: System Property

```bash
java -jar app.jar -Dapp.config.path=/etc/java-resumes
```

#### Option 2: Environment Variable

```bash
export CONFIG_PATH=/etc/java-resumes
java -jar app.jar
```

#### Option 3: Docker Compose

In `docker-compose.yml`:

```yaml
backend:
  environment:
    - app.config.path=/etc/java-resumes
  volumes:
    - /etc/java-resumes:/etc/java-resumes:ro
```

### How External Config Works

1. **On startup,** the application checks the `app.config.path` system property
2. **If set,** it looks for `config.json` in that directory
3. **If found,** uses that configuration
4. **If not found,** falls back to current working directory
5. **If still not found,** returns empty config (may cause errors)

### Production Deployment Example

```bash
# Directory structure
/opt/java-resumes/
 config.json           # External config file
 prompts/              # External prompts directory
 files/                # File storage directory

# Start with external paths
java -jar app.jar \
  -Dapp.config.path=/opt/java-resumes \
  --prompts.external-dir=/opt/java-resumes/prompts
```

## 📑 External Prompts Directory

### Setting Custom Prompts

The application includes bundled prompts but supports external prompt overrides.

#### In application.yml

```yaml
prompts:
  external-dir: /opt/java-resumes/prompts
```

#### Via Environment Variable

```bash
export PROMPTS_DIR=/opt/java-resumes/prompts
```

#### In docker-compose.yml

```yaml
backend:
  environment:
    - PROMPTS_DIR=/custom/prompts
  volumes:
    - /opt/prompts:/custom/prompts:ro
```

### Prompt Files

Required prompt files in external directory:

```
/opt/java-resumes/prompts/
 RESUME.md           # Resume optimization prompt
 COVER.md            # Cover letter generation prompt
 SKILLS.md           # Skills analysis prompt
 README.md           # Prompt documentation
```

### Prompt Loading Behavior

1. **If external directory is configured** and prompt file exists there, use it
2. **Otherwise,** use bundled prompt from application JAR
3. **If neither exists,** log error and use empty prompt

This allows gradual customization - start with bundled prompts, override what you need.

### Customizing Prompts

1. **Copy bundled prompts to external directory:**

```bash
mkdir -p /opt/java-resumes/prompts
cp -r src/main/resources/prompts/* /opt/java-resumes/prompts/
```

2. **Edit prompts as needed** (RESUME.md, COVER.md, SKILLS.md)
3. **Restart application** to load new prompts
4. **Set PROMPTS_DIR environment variable**

## 🎄 Spring Boot Configuration (application.yml)

### File Location

`src/main/resources/application.yml`

### Common Settings

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 500KB
      max-request-size: 500KB

upload:
  path: files

llm:
  endpoint: http://127.0.0.1:11434/v1/chat/completions
  apikey: 1234567890

prompts:
  external-dir: ${PROMPTS_DIR:}
```

### Spring Profiles

Create environment-specific configurations:

**application-prod.yml:**

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB

upload:
  path: /data/files

llm:
  endpoint: https://api.openai.com/v1/chat/completions
  apikey: ${LLM_APIKEY}

prompts:
  external-dir: /etc/java-resumes/prompts
```

**application-dev.yml:**

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 500KB

upload:
  path: ./files

llm:
  endpoint: http://localhost:11434/v1/chat/completions
  apikey: not-needed
```

**Activate profile:**

```bash
export SPRING_PROFILES_ACTIVE=prod
java -jar app.jar
```

## 🌍 Environment Variables

### Complete List

| Variable                 | Type   | Default                      | Description              |
| ------------------------ | ------ | ---------------------------- | ------------------------ |
| `app.config.path`        | string | (current directory)          | External config location |
| `CONFIG_PATH`            | string | (current directory)          | Alternative to above     |
| `PROMPTS_DIR`            | string | (bundled prompts)            | External prompts dir     |
| `SPRING_PROFILES_ACTIVE` | string | `dev`                        | Spring Boot profile      |
| `UPLOAD_PATH`            | string | `files`                      | File storage directory   |
| `LLM_ENDPOINT`           | string | `http://127.0.0.1:11434/...` | LLM service endpoint     |
| `LLM_APIKEY`             | string | `1234567890`                 | LLM API key              |
| `VITE_API_BASE_URL`      | string | `http://localhost:8080`      | Frontend API base URL    |

### Setting Environment Variables

#### Linux/Mac

```bash
export PROMPTS_DIR=/opt/java-resumes/prompts
export CONFIG_PATH=/opt/java-resumes
export SPRING_PROFILES_ACTIVE=prod
java -jar app.jar
```

#### Windows (PowerShell)

```powershell
$env:PROMPTS_DIR = "C:\java-resumes\prompts"
$env:CONFIG_PATH = "C:\java-resumes"
$env:SPRING_PROFILES_ACTIVE = "prod"
java -jar app.jar
```

#### Windows (Command Prompt)

```cmd
set PROMPTS_DIR=C:\java-resumes\prompts
set CONFIG_PATH=C:\java-resumes
set SPRING_PROFILES_ACTIVE=prod
java -jar app.jar
```

## 🐳 Docker Configuration

### docker-compose.yml with External Config

```yaml
version: "3.8"

services:
  backend:
    build: .
    ports:
      - "8080:8080"
    environment:
      # Config paths
      - app.config.path=/etc/java-resumes
      - PROMPTS_DIR=/etc/java-resumes/prompts

      # Spring Boot
      - SPRING_PROFILES_ACTIVE=prod
      - UPLOAD_PATH=/data/files

      # LLM (from .env file or directly here)
      - LLM_ENDPOINT=${LLM_ENDPOINT:-http://host.docker.internal:11434/v1/chat/completions}
      - LLM_APIKEY=${LLM_APIKEY:-not-needed}

    volumes:
      # Mount external config
      - /opt/java-resumes/config.json:/etc/java-resumes/config.json:ro
      - /opt/java-resumes/prompts:/etc/java-resumes/prompts:ro

      # Mount file storage
      - backend-files:/data/files

    networks:
      - resume-app-network

  frontend:
    build:
      context: .
      dockerfile: frontend/Dockerfile
    ports:
      - "80:80"
    environment:
      - VITE_API_BASE_URL=http://backend:8080
    depends_on:
      - backend
    networks:
      - resume-app-network

volumes:
  backend-files:

networks:
  resume-app-network:
```

### .env File for Docker Compose

Create `.env` in project root:

```env
# LLM Configuration
LLM_ENDPOINT=http://host.docker.internal:11434/v1/chat/completions
LLM_APIKEY=not-needed
LLM_MODEL=mistral:latest

# Paths (for Docker volumes)
CONFIG_PATH=/opt/java-resumes
PROMPTS_DIR=/opt/java-resumes/prompts

# Spring Boot
SPRING_PROFILES_ACTIVE=prod
UPLOAD_PATH=/data/files
```

## Configuration Priority

When the same setting is specified in multiple places, priority is:

1. **System property** (`-Dapp.config.path=...`)
2. **Environment variable** (`export CONFIG_PATH=...`)
3. **application.yml** (Spring configuration file)
4. **config.json** (embedded configuration)
5. **Default values**

## 🐛 Troubleshooting Configuration

### "Could not load config"

```
Error: Could not read file: config.json
```

**Solution:** Ensure `config.json` exists in the current directory or set `app.config.path`

### "Cannot connect to LLM"

**For Docker:** Use `host.docker.internal` instead of `localhost`

```json
{
  "endpoint": "http://host.docker.internal:11434/v1/chat/completions"
}
```

### "Prompts not loading"

1. Verify external prompts directory path is correct
2. Ensure prompt files (RESUME.md, COVER.md, SKILLS.md) exist
3. Check file permissions (must be readable)
4. Review application logs for path issues

### Config Changes Not Applied

**Restart the application** - configuration is loaded at startup, not reloaded at runtime.

## ✅ Best Practices

### Development

- Use bundled prompts (don't set `PROMPTS_DIR`)
- Use local LLM (Ollama or LM Studio)
- Keep config.json in project root
- Use Docker Compose for consistency

### Production

- Use external `CONFIG_PATH` for config management
- Use external `PROMPTS_DIR` for prompt customization
- Set `SPRING_PROFILES_ACTIVE=prod`
- Use environment variables for sensitive data (API keys)
- Mount volumes read-only where possible
- Use secrets management for API keys
- Keep configuration separate from deployment

---

See also: [ENVIRONMENT_VARIABLES.md](ENVIRONMENT_VARIABLES.md) for complete reference

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
