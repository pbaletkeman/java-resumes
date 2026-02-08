# Troubleshooting Guide

Common issues and their solutions for java-resumes.

- [Troubleshooting Guide](#troubleshooting-guide)
  - [🏗️ Build Issues](#️-build-issues)
    - [Gradle Build Fails](#gradle-build-fails)
    - [Checkstyle Failures](#checkstyle-failures)
    - [Dependency Resolution Fails](#dependency-resolution-fails)
  - [⚙️ Runtime Issues](#️-runtime-issues)
    - [Application Won't Start](#application-wont-start)
    - [LLM API Connection Fails](#llm-api-connection-fails)
    - [File Upload Fails](#file-upload-fails)
    - [Port Already in Use](#port-already-in-use)
  - [💻 Frontend Issues](#-frontend-issues)
    - [npm Build Fails](#npm-build-fails)
    - [TypeScript Errors](#typescript-errors)
    - [Port 5173 Already in Use](#port-5173-already-in-use)
    - [CSS/Tailwind Not Applying](#csstailwind-not-applying)
  - [💾 Database Issues](#-database-issues)
    - [File Storage Errors](#file-storage-errors)
    - [File Retrieval Fails](#file-retrieval-fails)
  - [🧪 Test Issues](#-test-issues)
    - [Tests Fail to Run](#tests-fail-to-run)
    - [Tests Time Out](#tests-time-out)
    - [Coverage Report Fails](#coverage-report-fails)
  - [🔌 API Issues](#-api-issues)
    - [CORS Errors](#cors-errors)
    - [Request/Response Format Issues](#requestresponse-format-issues)
    - [Large File Upload Issues](#large-file-upload-issues)
  - [📈 Performance Issues](#-performance-issues)
    - [Slow Build Times](#slow-build-times)
    - [Slow API Responses](#slow-api-responses)
    - [High Memory Usage](#high-memory-usage)
  - [⚙️ Configuration Issues](#️-configuration-issues)
    - [External config.json Not Found](#external-configjson-not-found)
    - [External Prompt Directory Not Found](#external-prompt-directory-not-found)
  - [🔀 Git Issues](#-git-issues)
    - [Uncommitted Changes](#uncommitted-changes)
    - [Merge Conflicts](#merge-conflicts)
  - [🔍 Diagnostic Commands](#-diagnostic-commands)
    - [System Information](#system-information)
    - [Application Logs](#application-logs)
    - [Network Diagnostics](#network-diagnostics)
  - [🆘 Getting Help](#-getting-help)
    - [Debug Mode](#debug-mode)
    - [Collect Information](#collect-information)
  - [💡 Tips \& Tricks](#-tips--tricks)
    - [Faster Builds](#faster-builds)
    - [Development Shortcuts](#development-shortcuts)
    - [Cleanup Commands](#cleanup-commands)
  - [✅ Health Checks](#-health-checks)
    - [Quick Verification](#quick-verification)
    - [Full Health Check](#full-health-check)

---

## 🏗️ Build Issues

### Gradle Build Fails

**Symptom:** `./gradlew build` fails with errors

**Solutions:**

```bash
# Clear cache and rebuild
./gradlew clean build

# Check Java version
java -version
# Expected: Java 21 LTS

# Update Gradle wrapper
./gradlew wrapper --gradle-version=latest

# Check disk space
df -h /  # At least 500MB free

# Increase memory
export GRADLE_OPTS="-Xmx2g"
./gradlew build
```

### Checkstyle Failures

**Symptom:** `./gradlew checkstyleMain` fails with violations

**Solution:**

```bash
# View violations
./gradlew checkstyleMain

# Common issues:
# - Line too long (max 120 chars)  Break into multiple lines
# - Import order  Use IDE organize imports (Ctrl+Shift+O)
# - Naming convention  Follow Java style guide
# - Javadoc missing  Add public method documentation

# Run build without checkstyle (temporary only!)
./gradlew build -x checkstyleMain
```

### Dependency Resolution Fails

**Symptom:** Maven/Gradle can't find dependency

```bash
# Clear gradle cache
rm -rf ~/.gradle/caches

# Try build with refresh
./gradlew build --refresh-dependencies

# Check internet connection
ping repository.maven.apache.org

# View dependency tree
./gradlew dependencies
```

---

## ⚙️ Runtime Issues

### Application Won't Start

**Symptom:** `./gradlew bootRun` fails to start

**Check:**

```bash
# Verify port not in use
netstat -tulpn | grep 8080
lsof -i :8080

# Kill process using port
lsof -ti:8080 | xargs kill -9

# Use different port
export SERVER_PORT=8081
./gradlew bootRun
```

### LLM API Connection Fails

**Symptom:** "Cannot connect to LLM service"

**Solutions:**

```bash
# 1. Verify LLM is running
curl http://localhost:11434/api/tags

# 2. Check config.json
cat config.json
# Should have: endpoint, apikey, model

# 3. Verify endpoint format (no trailing slash)
# Correct:   http://localhost:11434
# Wrong:     http://localhost:11434/

# 4. Check firewall
# Ollama default ports: 11434 (HTTP), 11435 (gRPC)

# 5. Test with curl
curl -X POST http://localhost:11434/api/chat \
  -H "Content-Type: application/json" \
  -d '{"model":"mistral","messages":[{"role":"user","content":"hi"}]}'
```

### File Upload Fails

**Symptom:** Upload returns 500 error

```bash
# Check upload directory exists
ls -la ./uploads

# Create if missing
mkdir -p ./uploads

# Check permissions
chmod 755 ./uploads

# Check disk space
df -h ./uploads

# Check file size limit
# In application.properties:
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### Port Already in Use

**Symptom:** "Address already in use"

```bash
# Find process using port 8080
lsof -i :8080
# or
netstat -tulpn | grep 8080

# Kill process
kill -9 <PID>

# or use different port
export SERVER_PORT=9090
./gradlew bootRun
```

---

## 💻 Frontend Issues

### npm Build Fails

**Symptom:** `npm run build` or `npm run dev` fails

```bash
# Clear cache
npm cache clean --force
rm -rf node_modules package-lock.json

# Reinstall
npm install

# Try build again
npm run build

# Check Node version
node --version
# Expected: v18+ (for React 19)

# Check npm version
npm --version
# Expected: v9+
```

### TypeScript Errors

**Symptom:** "Type 'x' is not assignable to type 'y'"

```bash
# Run type check
npm run type-check

# Common fixes:
# - Add type annotations
# - Use 'as' for type assertion (temporary)
# - Fix import paths
# - Update tsconfig.json strict mode

# Check tsconfig
cat frontend/tsconfig.json
```

### Port 5173 Already in Use

```bash
# Use different port
npm run dev -- --port 3000

# or kill process using 5173
lsof -i :5173 | grep -v COMMAND | awk '{print $2}' | xargs kill -9
```

### CSS/Tailwind Not Applying

```bash
# Check tailwind config
cat frontend/tailwind.config.ts

# Verify content paths include all template files:
content: [
  "./index.html",
  "./src/**/*.{js,ts,jsx,tsx}",
]

# Rebuild CSS
npm run dev  # Or npm run build
```

---

## 💾 Database Issues

### File Storage Errors

**Symptom:** "Failed to save file" or "File not found"

```bash
# Check config.json path setting
cat config.json

# Verify upload directory
ls -la ./uploads/

# Check file permissions
chmod -R 755 ./uploads/

# Verify Spring config
grep -i "upload.path" application.properties

# Check Java path handling
# On Windows: use forward slashes or double backslashes
# upload.path=uploads
# upload.path=uploads\\
```

### File Retrieval Fails

**Symptom:** "File not found" when downloading

```bash
# List available files
ls ./uploads/

# Check file actually exists
test -f ./uploads/filename.pdf && echo "exists" || echo "not found"

# Check filename encoding
# Make sure no special characters in filename

# Verify endpoint returns file list
curl http://localhost:8080/api/files
```

---

## 🧪 Test Issues

### Tests Fail to Run

**Symptom:** `./gradlew test` fails

```bash
# Run with verbose output
./gradlew test --info

# Run specific test
./gradlew test --tests ClassName

# Check test file exists
find . -name "*Test.java"

# Run with debug
./gradlew test --debug

# View test output
# Check: build/reports/tests/test/index.html
```

### Tests Time Out

**Symptom:** Tests hang or time out

```bash
# Increase timeout
export GRADLE_OPTS="-Xmx2g"

# Mock slow services
// Use @Mock annotation
@Mock
private SlowService slowService;

// Use Mockito to configure response
when(slowService.call()).thenReturn(result);

# Check for blocking I/O
// Use async/await instead of blocking calls
```

### Coverage Report Fails

**Symptom:** Coverage report not generated

```bash
# Generate coverage report
./gradlew test jacocoTestReport

# View report
open build/reports/jacoco/test/html/index.html

# Check if Jacoco plugin configured
grep -i jacoco build.gradle
```

---

## 🔌 API Issues

### CORS Errors

**Symptom:** "No 'Access-Control-Allow-Origin' header"

```java
// Solution: Add CORS configuration
@RestController
@CrossOrigin(origins = "*")
public class ResumeController {
    // ...
}

// or in application.properties
cors.allowed-origins=http://localhost:5173
cors.allowed-methods=GET,POST,PUT,DELETE
```

### Request/Response Format Issues

**Symptom:** JSON parsing errors

```bash
# Check Content-Type header
curl -H "Content-Type: application/json"

# Verify JSON format
echo '{"key":"value"}' | jq .

# Check API response
curl http://localhost:8080/api/files | jq .

# View request body in logs
export LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_WEB=DEBUG
```

### Large File Upload Issues

**Symptom:** "413 Payload Too Large"

```properties
# In application.properties
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

# Note: Max recommended 50MB
```

---

## 📈 Performance Issues

### Slow Build Times

**Symptom:** `./gradlew build` takes 5+ minutes

```bash
# Use Gradle daemon (enabled by default)
./gradlew build  # 2nd build will be faster

# Skip tests for faster build
./gradlew build -x test

# Build with parallel
./gradlew build --parallel

# Enable build cache
./gradlew build --build-cache

# Check what's slow
./gradlew build --profile
# Check: build/reports/profile/profile-*.html
```

### Slow API Responses

**Symptom:** LLM optimization takes too long

```bash
# Check LLM service performance
time curl http://localhost:11434/api/chat -d '{...}'

# Monitor system resources
top -b -n 1 | head -20

# Check network latency
ping localhost

# Profile Java application
./gradlew bootRun
# Use JProfiler or Visual VM to attach

# Monitor in real-time
jps  # List Java processes
jstat -gc <PID> 1000  # Monitor garbage collection
```

### High Memory Usage

**Symptom:** Java process uses too much memory

```bash
# Check heap usage
jstat -gccapacity <PID>

# Increase heap size
export GRADLE_OPTS="-Xmx4g -Xms2g"

# Reduce heap if needed
export GRADLE_OPTS="-Xmx1g"

# Monitor with VisualVM
jvisualvm &  # Opens GUI
```

---

## ⚙️ Configuration Issues

### External config.json Not Found

**Symptom:** "config.json not found" error

```bash
# Check current directory
pwd

# Create config.json in right location
cat > config.json << 'EOF'
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "ollama",
  "model": "mistral"
}
EOF

# Verify location
ls -la config.json

# Or specify config path
export CONFIG_PATH=/path/to/config.json
./gradlew bootRun
```

### External Prompt Directory Not Found

**Symptom:** "Prompt directory not found" error

```bash
# Create prompt directory
mkdir -p ./prompts

# Add prompt files
touch ./prompts/resume_optimizer.txt
touch ./prompts/cover_letter_optimizer.txt

# Verify structure
ls -la ./prompts/

# Or specify prompt directory path
export PROMPT_DIR=/path/to/prompts
./gradlew bootRun
```

---

## 🔀 Git Issues

### Uncommitted Changes

**Symptom:** Can't switch branches due to uncommitted changes

```bash
# Stash changes
git stash

# Or commit changes
git add .
git commit -m "WIP: work in progress"

# Check status
git status
```

### Merge Conflicts

**Symptom:** Merge fails with conflicts

```bash
# Check which files have conflicts
git status

# Resolve conflicts in editor
# Look for: <<<<<<<, =======, >>>>>>>

# Mark as resolved
git add .
git commit -m "Resolve merge conflicts"
```

---

## 🔍 Diagnostic Commands

### System Information

```bash
# Java version
java -version

# Node version
node --version && npm --version

# Git version
git --version

# Check disk space
df -h

# Check memory
free -h  # Linux/Mac
wmic os get totalvisiblememorysize,freevisiblememorysize  # Windows
```

### Application Logs

```bash
# View recent logs
./gradlew bootRun 2>&1 | tail -50

# View with timestamps
./gradlew bootRun 2>&1 | grep -E "ERROR|WARN"

# Save logs to file
./gradlew bootRun > app.log 2>&1 &

# Monitor log file
tail -f app.log
```

### Network Diagnostics

```bash
# Check connectivity to localhost
nc -zv localhost 8080
nc -zv localhost 5173
nc -zv localhost 11434

# Test endpoint
curl -v http://localhost:8080/api/health
curl -v http://localhost:11434/api/tags

# Check port listeners
lsof -i -P -n | grep LISTEN
```

---

## 🆘 Getting Help

### Debug Mode

```bash
# Enable debug logging
export LOG_LEVEL=DEBUG

# Run with debug
./gradlew bootRun --debug

# Attach debugger
# Use IDE: Run  Attach to Process
```

### Collect Information

When reporting issues, provide:

1. **System Info**

   ```bash
   java -version
   node --version
   npm --version
   ./gradlew --version
   ```

2. **Error Log**

   ```bash
   # Run failing command and save output
   ./gradlew build 2>&1 | tee error.log
   ```

3. **Configuration**

   ```bash
   # Show config files (sanitize sensitive data)
   cat config.json | head -5
   cat application.properties
   ```

4. **File Structure**
   ```bash
   # Show relevant files
   ls -la
   ls -la ./uploads/
   ls -la ./prompts/
   ```

---

## 💡 Tips & Tricks

### Faster Builds

```bash
# Use daemon for caching
export GRADLE_DAEMON=true

# Skip unnecessary tasks
./gradlew build -x test -x checkstyleMain

# Only build changed files
./gradlew build --up-to-date
```

### Development Shortcuts

```bash
# Watch mode for tests
./gradlew test --continuous

# Run app in background
./gradlew bootRun &
fg  # Bring to foreground
jobs  # List background processes

# Quick TypeScript check
npm run type-check --watch
```

### Cleanup Commands

```bash
# Remove all build artifacts
./gradlew clean
rm -rf node_modules build dist

# Clear caches
./gradlew clean --build-cache
npm cache clean --force

# Remove logs
rm -f app.log error.log
```

---

## ✅ Health Checks

### Quick Verification

```bash
# 1. Check Java
java -version  # Should be Java 21

# 2. Check build
./gradlew --version

# 3. Check Node
node --version  # Should be v18+

# 4. Check npm
npm --version  # Should be v9+

# 5. Check LLM
curl http://localhost:11434/api/tags  # Should respond

# 6. Check app
curl http://localhost:8080/api/health  # Should respond

# 7. Check frontend
curl http://localhost:5173  # Should respond
```

### Full Health Check

```bash
#!/bin/bash
echo "=== System Check ==="
echo "Java: $(java -version 2>&1 | head -1)"
echo "Node: $(node --version)"
echo "npm: $(npm --version)"
echo ""
echo "=== Services Check ==="
echo -n "LLM API: "
curl -s http://localhost:11434/api/tags > /dev/null && echo "✓ Running" || echo "✗ Not running"
echo -n "Backend: "
curl -s http://localhost:8080/api/health > /dev/null && echo "✓ Running" || echo "✗ Not running"
echo -n "Frontend: "
curl -s http://localhost:5173 > /dev/null && echo "✓ Running" || echo "✗ Not running"
echo ""
echo "=== File Check ==="
test -f config.json && echo "✓ config.json" || echo "✗ config.json missing"
test -d uploads && echo "✓ uploads/" || echo "✗ uploads/ missing"
test -d prompts && echo "✓ prompts/" || echo "✗ prompts/ missing"
```

---

**See also:**

- [Development Setup](DEVELOPMENT_SETUP.md) - Environment setup guide
- [Code Quality](CODE_QUALITY.md) - Quality standards
- [ARCHITECTURE.md](ARCHITECTURE.md) - System design
