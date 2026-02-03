# Environment Variables & Configuration

Complete reference for environment variables and configuration in java-resumes.

## Table of Contents

- [Overview](#-overview)
- [Backend Environment Variables](#-backend-environment-variables)
- [Frontend Environment Variables](#-frontend-environment-variables)
- [config.json](#-configjson)
- [Docker Environment Configuration](#-docker-environment-configuration)
- [Production Environment Setup](#-production-environment-setup)
- [Verification and Debugging](#-verification-and-debugging)

---

## 📋 Overview

The java-resumes project supports configuration through:

1. **Environment Variables** - System-level settings
2. **config.json** - LLM service configuration (can be external)
3. **application.properties** - Spring Boot settings
4. **Frontend .env** - React/frontend settings

**Priority Order (highest to lowest):**

1. Environment Variables
2. External configuration files (config.json, .env)
3. application.properties
4. Default values in code

---

## 🔧 Backend Environment Variables

### Core Configuration

| Variable      | Type   | Required | Default         | Description                       |
| ------------- | ------ | -------- | --------------- | --------------------------------- |
| `SERVER_PORT` | int    | No       | 8080            | HTTP server port                  |
| `CONFIG_PATH` | string | No       | `./config.json` | Path to config.json file          |
| `UPLOAD_PATH` | string | No       | `./uploads`     | Directory for uploaded files      |
| `PROMPT_DIR`  | string | No       | `./prompts`     | Directory for prompt templates    |
| `LOG_LEVEL`   | string | No       | `INFO`          | Logging level (DEBUG, INFO, WARN) |
| `JAVA_HOME`   | string | Maybe    | Auto-detected   | Java installation directory       |
| `GRADLE_OPTS` | string | No       | `-Xmx2g`        | JVM options for Gradle            |

### LLM Service Configuration

**Note:** Primary configuration is in `config.json` (external or local)

```json
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "ollama",
  "model": "mistral"
}
```

**Environment Variable Overrides (future):**

```bash
LLM_ENDPOINT=http://localhost:11434/v1/chat/completions
LLM_API_KEY=ollama
LLM_MODEL=mistral
```

### Spring Boot Properties

**File:** `src/main/resources/application.properties`

```properties
# Server
server.port=8080
server.servlet.context-path=/

# File Upload
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
upload.path=./uploads

# Logging
logging.level.root=INFO
logging.level.ca.letkeman.resumes=DEBUG
logging.pattern.console=%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n

# Jackson JSON
spring.jackson.serialization.indent-output=true
spring.jackson.default-property-inclusion=non_null

# Actuator (if enabled)
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always
```

---

## 🔧 Frontend Environment Variables

**Location:** `frontend/.env` (create from `.env.example`)

### Development

```bash
# API Configuration
VITE_API_URL=http://localhost:8080
VITE_API_TIMEOUT=30000

# Debug Mode
VITE_DEBUG=false
VITE_LOG_LEVEL=info

# Feature Flags
VITE_FEATURE_DARK_MODE=true
VITE_FEATURE_EXPORT_PDF=true
```

### Production

```bash
# Build optimization
NODE_ENV=production

# API Configuration
VITE_API_URL=https://api.example.com
VITE_API_TIMEOUT=30000

# Debug Disabled
VITE_DEBUG=false
VITE_LOG_LEVEL=warn

# Features
VITE_FEATURE_DARK_MODE=true
VITE_FEATURE_EXPORT_PDF=true
```

### Using in Frontend Code

```typescript
// TypeScript
const API_URL = import.meta.env.VITE_API_URL;
const DEBUG = import.meta.env.VITE_DEBUG === "true";
const LOG_LEVEL = import.meta.env.VITE_LOG_LEVEL;

// Access in Vite config
export default defineConfig({
  define: {
    __API_URL__: JSON.stringify(process.env.VITE_API_URL),
    __DEBUG__: process.env.VITE_DEBUG === "true",
  },
});
```

---

## 📝 config.json

**Purpose:** LLM service connection configuration

**Location Options:**

- Default: `./config.json` (same directory as application)
- Custom: Set `CONFIG_PATH` environment variable
- External: Use `CONFIG_PATH` for external location

### Format

```json
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "ollama",
  "model": "mistral"
}
```

### Parameters

| Field    | Type   | Required | Description                                  |
| -------- | ------ | -------- | -------------------------------------------- |
| endpoint | string | ✅       | LLM API endpoint URL (OpenAI-compatible)     |
| apikey   | string | ✅       | API key for authentication (can be blank)    |
| model    | string | ✅       | Model identifier (e.g., "mistral", "gemini") |

### Examples

**Ollama Local:**

```json
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "ollama",
  "model": "mistral:latest"
}
```

**LM Studio Local:**

```json
{
  "endpoint": "http://localhost:1234/v1/chat/completions",
  "apikey": "not-used",
  "model": "local-model"
}
```

**OpenAI Remote:**

```json
{
  "endpoint": "https://api.openai.com/v1/chat/completions",
  "apikey": "sk-xxxxxxx...",
  "model": "gpt-4"
}
```

**Azure OpenAI:**

```json
{
  "endpoint": "https://xxxxx.openai.azure.com/openai/deployments/xxxxx/chat/completions?api-version=2024-02-15-preview",
  "apikey": "your-azure-key",
  "model": "gpt-4"
}
```

---

## 🗂️ Prompt Directory

**Purpose:** External prompt templates for optimization

**Location Options:**

- Default: `./prompts` (same directory as application)
- Custom: Set `PROMPT_DIR` environment variable

### Structure

```
prompts/
├── resume_optimization.txt
├── cover_letter_optimization.txt
├── resume_detailed.txt
└── industry_specific/
    ├── tech.txt
    ├── finance.txt
    └── healthcare.txt
```

### File Format

Each `.txt` file contains:

- **First line:** Prompt purpose/description
- **Remaining lines:** Prompt template with variables

**Example: `prompts/resume_optimization.txt`**

```
Resume optimization prompt for job matching

You are an expert resume writer and career coach.
Analyze the following resume and job description.
Provide specific improvements to match the job requirements.

Resume:
{RESUME_CONTENT}

Job Description:
{JOB_DESCRIPTION}

Optimization Focus:
{OPTIMIZATION_TYPE}

Please provide:
1. Key improvements to highlight
2. Keywords to emphasize
3. Experience to reframe
4. Specific examples to add
```

### Variables

| Variable              | Description                |
| --------------------- | -------------------------- |
| `{RESUME_CONTENT}`    | Uploaded resume text       |
| `{JOB_DESCRIPTION}`   | Job description text       |
| `{OPTIMIZATION_TYPE}` | Type (resume/cover_letter) |
| `{company}`           | Target company             |
| `{JOB_TITLE}`         | Target job title           |

---

## 🌐 Setting Environment Variables

### Linux/Mac

**Temporary (Current Session):**

```bash
export SERVER_PORT=9090
export CONFIG_PATH=/etc/java-resumes/config.json
export UPLOAD_PATH=/var/uploads
export PROMPT_DIR=/etc/java-resumes/prompts
```

**Permanent (Bash Profile):**

```bash
# Add to ~/.bashrc or ~/.zshrc
export SERVER_PORT=9090
export CONFIG_PATH=/etc/java-resumes/config.json

# Reload shell
source ~/.bashrc
```

**In .env File:**

```bash
# Create .env in project root
echo "SERVER_PORT=9090" > .env

# Load in shell
set -a
source .env
set +a
```

### Windows

**Temporary (Command Prompt):**

```batch
set SERVER_PORT=9090
set CONFIG_PATH=C:\config\config.json
set UPLOAD_PATH=C:\uploads
set PROMPT_DIR=C:\prompts
```

**Temporary (PowerShell):**

```powershell
$env:SERVER_PORT = "9090"
$env:CONFIG_PATH = "C:\config\config.json"
$env:UPLOAD_PATH = "C:\uploads"
$env:PROMPT_DIR = "C:\prompts"
```

**Permanent (System Properties):**

1. Open "Environment Variables"
2. Click "New" under "User variables"
3. Add: Variable name = `SERVER_PORT`, Value = `9090`
4. Click OK and restart application

**Permanent (.env File):**

```batch
# Create .env
type nul > .env

# Add variables
echo SERVER_PORT=9090 >> .env
echo CONFIG_PATH=C:\config\config.json >> .env
```

### Docker

**Dockerfile:**

```dockerfile
ENV SERVER_PORT=8080
ENV CONFIG_PATH=/etc/config/config.json
ENV UPLOAD_PATH=/uploads
ENV PROMPT_DIR=/etc/prompts
```

**docker-compose.yml:**

```yaml
environment:
  SERVER_PORT: 8080
  CONFIG_PATH: /etc/config/config.json
  UPLOAD_PATH: /uploads
  PROMPT_DIR: /etc/prompts
```

**Command Line:**

```bash
docker run -e SERVER_PORT=8080 \
           -e CONFIG_PATH=/etc/config/config.json \
           java-resumes:latest
```

---

## 🚀 Startup Configuration

### Standard Startup

```bash
# Default configuration (uses ./config.json, ./uploads, ./prompts)
./gradlew bootRun

# Custom config location
CONFIG_PATH=/etc/java-resumes/config.json ./gradlew bootRun

# Custom upload and prompt directories
UPLOAD_PATH=/var/uploads PROMPT_DIR=/etc/prompts ./gradlew bootRun
```

### Production Startup

```bash
# Full external configuration
CONFIG_PATH=/etc/java-resumes/config.json \
UPLOAD_PATH=/var/uploads \
PROMPT_DIR=/etc/java-resumes/prompts \
SERVER_PORT=8080 \
LOG_LEVEL=WARN \
GRADLE_OPTS="-Xmx4g -XX:+UseG1GC" \
./gradlew bootRun
```

### Development Startup

```bash
# Development with debug logging
CONFIG_PATH=./config.dev.json \
UPLOAD_PATH=./uploads-dev \
PROMPT_DIR=./prompts-dev \
SERVER_PORT=8080 \
LOG_LEVEL=DEBUG \
./gradlew bootRun
```

---

## 🔍 Verifying Configuration

### Check Java Environment

```bash
# Java version
java -version

# Java location
which java
java -XshowSettings:properties -version 2>&1 | grep java.home

# JVM options
echo $JAVA_OPTS
echo $GRADLE_OPTS
```

### Check Spring Configuration

```bash
# View effective configuration
curl http://localhost:8080/actuator/env | jq .

# Check specific property
curl http://localhost:8080/actuator/env/server.port | jq .
```

### Check File Paths

```bash
# Verify config.json exists
test -f config.json && echo "✅ config.json found" || echo "❌ not found"
cat config.json

# Verify upload directory
test -d ./uploads && echo "✅ uploads exists" || echo "❌ not found"
ls -la ./uploads/

# Verify prompt directory
test -d ./prompts && echo "✅ prompts exists" || echo "❌ not found"
ls -la ./prompts/
```

### Check API Connectivity

```bash
# Test backend health
curl http://localhost:8080/api/health | jq .

# Test LLM connection
curl http://localhost:11434/api/tags | jq .

# Test file upload endpoint
curl http://localhost:8080/api/files | jq .
```

---

## 🔐 Security Best Practices

### Sensitive Data

**Never commit to Git:**

- `config.json` with API keys
- `.env` files with secrets
- Application properties with passwords

**Safe Practices:**

```bash
# Add to .gitignore
echo "config.json" >> .gitignore
echo "frontend/.env" >> .gitignore
echo "frontend/.env.local" >> .gitignore

# Use environment variables for secrets
export LLM_API_KEY="your-secret-key"

# Use .env files (add to .gitignore)
# And load in startup script
```

### File Permissions

```bash
# Restrict config.json access
chmod 600 config.json

# Restrict upload directory
chmod 700 ./uploads

# Restrict prompt directory
chmod 700 ./prompts
```

### Environment Variable Safety

```bash
# Don't log sensitive variables
echo $CONFIG_PATH  # Safe: file path
echo $LLM_API_KEY  # Unsafe: exposes key

# Use in code safely
String apiKey = System.getenv("LLM_API_KEY");
// Don't log: logger.info("API Key: " + apiKey);
```

---

## 🔄 Configuration Reload

### Without Restart (Development)

```bash
# Watch for changes and rebuild
./gradlew bootRun --continuous

# Frontend hot reload
cd frontend && npm run dev

# Changes to application.properties require restart
```

### After Changes

```bash
# Stop running process
Ctrl+C

# Rebuild with new config
./gradlew clean build

# Start with new configuration
./gradlew bootRun
```

---

## 📊 Common Configurations

### Local Development

```bash
# config.json
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "ollama",
  "model": "mistral"
}

# application.properties
server.port=8080
upload.path=./uploads
logging.level.ca.letkeman.resumes=DEBUG
```

### Staging Environment

```bash
# .env (external location)
CONFIG_PATH=/etc/java-resumes/config.staging.json
UPLOAD_PATH=/var/uploads-staging
PROMPT_DIR=/etc/java-resumes/prompts
SERVER_PORT=8080
LOG_LEVEL=INFO
```

### Production Environment

```bash
# .env (external location)
CONFIG_PATH=/etc/java-resumes/config.prod.json
UPLOAD_PATH=/var/uploads
PROMPT_DIR=/etc/java-resumes/prompts
SERVER_PORT=443
LOG_LEVEL=WARN
GRADLE_OPTS="-Xmx8g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

---

## 🆘 Troubleshooting Configuration

### config.json Not Found

```bash
# Check default location
ls -la config.json

# Check custom location
ls -la $CONFIG_PATH

# Create with defaults
cat > config.json << 'EOF'
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "ollama",
  "model": "mistral"
}
EOF
```

### Prompt Directory Not Found

```bash
# Create directory
mkdir -p ./prompts

# Create sample prompt
cat > prompts/resume_optimization.txt << 'EOF'
Resume optimization
Optimize the resume for the job description.
EOF

# Verify
ls -la ./prompts/
```

### Upload Path Permission Denied

```bash
# Check permissions
ls -ld ./uploads

# Fix permissions
chmod 755 ./uploads

# Or create new directory with proper permissions
mkdir -p ./uploads
chmod 755 ./uploads
```

---

## 📞 Support

**See also:**

- [Development Setup](DEVELOPMENT_SETUP.md) - Initial setup guide
- [Troubleshooting](TROUBLESHOOTING.md) - Common issues
- [ARCHITECTURE.md](ARCHITECTURE.md) - Design patterns

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
