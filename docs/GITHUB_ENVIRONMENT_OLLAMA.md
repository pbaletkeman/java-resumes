# GitHub Environment Setup for Ollama

This guide explains how to configure a GitHub environment with Ollama LLM service for automated testing and development.

## Overview

GitHub Environments allow you to configure different deployment targets with specific settings, secrets, and protection rules. This guide sets up an environment specifically for Ollama-based LLM testing.

---

## Creating a GitHub Environment

### Step 1: Navigate to Repository Settings

1. Go to your GitHub repository: `https://github.com/pbaletkeman/java-resumes`
2. Click **Settings** tab
3. In the left sidebar, click **Environments**

### Step 2: Create New Environment

1. Click **New environment** button
2. Enter environment name: `ollama-testing`
3. Click **Configure environment**

### Step 3: Configure Environment Variables

Add the following environment variables:

| Variable Name | Value | Description |
|---------------|-------|-------------|
| `OLLAMA_MODEL` | `tinyllama` | Default model to use |
| `LLM_ENDPOINT` | `http://localhost:11434/v1/chat/completions` | Ollama API endpoint |
| `LLM_APIKEY` | `ollama` | API key (not required but field needed) |

### Step 4: Add Environment Secrets (Optional)

If using external LLM services as fallback:

| Secret Name | Description |
|-------------|-------------|
| `OPENAI_API_KEY` | OpenAI API key (fallback) |
| `ANTHROPIC_API_KEY` | Claude API key (fallback) |

### Step 5: Configure Protection Rules (Optional)

**For production environments:**
- Enable "Required reviewers" (1-2 reviewers)
- Set "Wait timer" to 0 minutes
- Enable "Prevent self-review"

**For testing environments:**
- No protection rules needed

---

## Using the Environment in Workflows

### Option 1: Reference Environment in Workflow

Update `.github/workflows/ollama-service.yml`:

```yaml
name: Ollama LLM Service

on:
  workflow_dispatch:
  push:
    branches: [master, develop]

jobs:
  setup-ollama:
    runs-on: ubuntu-latest
    environment: ollama-testing  # Add this line
    
    steps:
      - name: Checkout code
        uses: actions/checkout@v4
      
      # ... rest of workflow
```

### Option 2: Use Environment Variables

Access environment variables in workflow:

```yaml
- name: Pull Ollama model
  env:
    MODEL: ${{ vars.OLLAMA_MODEL }}  # From environment variables
  run: |
    echo "Pulling model: $MODEL"
    ollama pull $MODEL
```

### Option 3: Use Secrets

Access secrets for external APIs:

```yaml
- name: Configure LLM endpoint
  env:
    OPENAI_KEY: ${{ secrets.OPENAI_API_KEY }}
    ENDPOINT: ${{ vars.LLM_ENDPOINT }}
  run: |
    # Use secrets in workflow
    echo "Configured with endpoint: $ENDPOINT"
```

---

## Workflow Configuration Examples

### Example 1: Simple Ollama Test

```yaml
name: Test with Ollama

on:
  push:
    branches: [master]

jobs:
  test:
    runs-on: ubuntu-latest
    environment: ollama-testing
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Install Ollama
        run: curl -fsSL https://ollama.com/install.sh | sh
      
      - name: Start Ollama
        run: ollama serve &
      
      - name: Pull model
        env:
          MODEL: ${{ vars.OLLAMA_MODEL }}
        run: |
          ollama pull $MODEL
          ollama list
      
      - name: Run tests
        run: |
          # Your test commands here
          ./gradlew test
```

### Example 2: Matrix Testing with Multiple Models

```yaml
name: Multi-Model Testing

on:
  workflow_dispatch:

jobs:
  test:
    runs-on: ubuntu-latest
    environment: ollama-testing
    
    strategy:
      matrix:
        model: [tinyllama, phi3:mini, gemma2:2b]
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Setup Ollama
        run: |
          curl -fsSL https://ollama.com/install.sh | sh
          ollama serve &
          sleep 10
      
      - name: Test with ${{ matrix.model }}
        run: |
          ollama pull ${{ matrix.model }}
          # Run tests
          MODEL=${{ matrix.model }} ./gradlew test
```

### Example 3: Conditional Deployment

```yaml
name: Deploy with LLM

on:
  push:
    branches: [master]

jobs:
  deploy:
    runs-on: ubuntu-latest
    environment: ollama-testing  # Requires approval if configured
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Deploy to environment
        run: |
          echo "Deploying with Ollama configuration"
          echo "Model: ${{ vars.OLLAMA_MODEL }}"
          echo "Endpoint: ${{ vars.LLM_ENDPOINT }}"
```

---

## Environment Management Best Practices

### 1. Separate Environments

Create multiple environments for different purposes:

- `ollama-development` - For PR testing
- `ollama-staging` - For pre-production testing  
- `ollama-production` - For production deployments

### 2. Model Selection Strategy

**Development:**
```yaml
OLLAMA_MODEL=tinyllama  # Fast, small
```

**Staging:**
```yaml
OLLAMA_MODEL=phi3:mini  # Balanced
```

**Production:**
```yaml
OLLAMA_MODEL=mistral    # High quality
```

### 3. Resource Management

Monitor GitHub Actions usage:
- Free tier: 2,000 minutes/month
- Team: 3,000 minutes/month
- Enterprise: 50,000 minutes/month

Use smaller models in CI to conserve minutes.

### 4. Caching Strategy

Cache Ollama models between runs:

```yaml
- name: Cache Ollama models
  uses: actions/cache@v3
  with:
    path: ~/.ollama
    key: ollama-${{ runner.os }}-${{ hashFiles('config.json') }}
```

---

## Monitoring and Debugging

### View Environment Status

1. Go to repository **Environments** page
2. Click on environment name
3. View deployment history and logs

### Debug Workflow Issues

```yaml
- name: Debug environment
  run: |
    echo "Environment: ${{ github.environment }}"
    echo "Model: ${{ vars.OLLAMA_MODEL }}"
    echo "Endpoint: ${{ vars.LLM_ENDPOINT }}"
    env | grep -i ollama || true
```

### View Ollama Logs

```yaml
- name: Show Ollama logs
  if: always()
  run: |
    cat /tmp/ollama.log || echo "No logs found"
    curl -s http://localhost:11434/api/version || echo "Service not running"
```

---

## Security Considerations

### 1. API Keys

- ✅ Store API keys as **Secrets** (not variables)
- ✅ Use environment protection for production
- ✅ Rotate keys regularly
- ❌ Never commit keys to repository

### 2. Model Downloads

- ✅ Verify model checksums
- ✅ Use official Ollama library
- ❌ Don't download from untrusted sources

### 3. Resource Limits

- Set timeout limits in workflows
- Monitor disk usage (models can be large)
- Use smaller models when possible

---

## Troubleshooting

### Issue: Environment not available

**Error:** `Environment "ollama-testing" not found`

**Solution:**
1. Check environment name spelling
2. Verify environment exists in Settings → Environments
3. Ensure you have proper repository permissions

### Issue: Variables not accessible

**Error:** `vars.OLLAMA_MODEL is empty`

**Solution:**
1. Verify variables are set in environment configuration
2. Check workflow references correct environment
3. Ensure workflow has `environment: ollama-testing` specified

### Issue: Workflow waiting indefinitely

**Cause:** Environment has required reviewers configured

**Solution:**
1. Approve the deployment in GitHub UI
2. Or remove protection rules for testing environment

---

## Complete Example Workflow

Here's a complete example combining all concepts:

```yaml
name: Complete Ollama Test

on:
  workflow_dispatch:
    inputs:
      model:
        description: 'Model to test'
        required: false
        default: 'tinyllama'
  push:
    branches: [master]

jobs:
  test:
    runs-on: ubuntu-latest
    environment: ollama-testing
    timeout-minutes: 30
    
    steps:
      - name: Checkout
        uses: actions/checkout@v4
      
      - name: Free disk space
        run: |
          df -h
          sudo rm -rf /usr/share/dotnet
          df -h
      
      - name: Cache Ollama
        uses: actions/cache@v3
        with:
          path: ~/.ollama
          key: ollama-${{ runner.os }}-${{ hashFiles('config.json') }}
      
      - name: Install Ollama
        run: |
          curl -fsSL https://ollama.com/install.sh | sh
          ollama serve > /tmp/ollama.log 2>&1 &
          sleep 10
      
      - name: Pull model
        env:
          MODEL: ${{ inputs.model || vars.OLLAMA_MODEL }}
        run: |
          echo "Pulling model: $MODEL"
          ollama pull $MODEL
          ollama list
      
      - name: Test inference
        env:
          MODEL: ${{ inputs.model || vars.OLLAMA_MODEL }}
          ENDPOINT: ${{ vars.LLM_ENDPOINT }}
        run: |
          curl -s $ENDPOINT -d "{
            \"model\": \"$MODEL\",
            \"messages\": [{\"role\": \"user\", \"content\": \"test\"}]
          }" | jq '.'
      
      - name: Run application tests
        run: ./gradlew test
      
      - name: Upload logs
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: ollama-logs
          path: /tmp/ollama.log
      
      - name: Summary
        run: |
          echo "## Test Summary" >> $GITHUB_STEP_SUMMARY
          echo "- **Model**: ${{ inputs.model || vars.OLLAMA_MODEL }}" >> $GITHUB_STEP_SUMMARY
          echo "- **Status**: ✅ Success" >> $GITHUB_STEP_SUMMARY
```

---

## Additional Resources

- [GitHub Environments Documentation](https://docs.github.com/en/actions/deployment/targeting-different-environments/using-environments-for-deployment)
- [GitHub Actions Variables](https://docs.github.com/en/actions/learn-github-actions/variables)
- [GitHub Actions Secrets](https://docs.github.com/en/actions/security-guides/encrypted-secrets)
- [Ollama GitHub Actions Guide](docs/OLLAMA_SETUP.md)

---

**Last Updated:** January 2026  
**Repository:** pbaletkeman/java-resumes
