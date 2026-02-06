# Ollama GitHub Environment - Quick Reference

Quick reference for setting up and using Ollama with the java-resumes project.

---

- [Ollama GitHub Environment - Quick Reference](#ollama-github-environment---quick-reference)
  - [🚀 Quick Setup Commands](#-quick-setup-commands)
    - [Local Development](#local-development)
    - [Docker Compose](#docker-compose)
    - [GitHub Actions](#github-actions)
  - [📋 Configuration Files](#-configuration-files)
  - [🎯 Model Recommendations](#-model-recommendations)
  - [⚡ Common Tasks](#-common-tasks)
    - [Change Model](#change-model)
    - [Test Ollama](#test-ollama)
    - [Troubleshooting](#troubleshooting)
  - [🔗 Quick Links](#-quick-links)
  - [📊 Resource Usage](#-resource-usage)
  - [✅ Checklist](#-checklist)

---

## 🚀 Quick Setup Commands

### Local Development

```bash
# Automated setup (recommended)
./scripts/setup-ollama.sh

# Or with specific model
./scripts/setup-ollama.sh phi3:mini

# Manual setup
ollama serve &
ollama pull tinyllama
```

### Docker Compose

```bash
# Start with Ollama service
docker-compose up -d

# Pull model in container
docker exec resume-ollama ollama pull tinyllama

# Check status
docker-compose ps
docker exec resume-ollama ollama list
```

### GitHub Actions

```bash
# Workflow triggers automatically on push to master/develop
# Or manually trigger from Actions tab with model selection

# Models available:
- tinyllama (default, fastest)
- phi3:mini (best quality for size)
- gemma2:2b (Google model)
- qwen2.5:0.5b (smallest)
- mistral (largest, best quality)
```

## 📋 Configuration Files

| File                                   | Purpose                    |
| -------------------------------------- | -------------------------- |
| `.github/workflows/ollama-service.yml` | CI/CD workflow with Ollama |
| `docker-compose.yml`                   | Ollama service definition  |
| `config.json`                          | LLM endpoint configuration |
| `scripts/setup-ollama.sh`              | Automated setup script     |
| `docs/OLLAMA_SETUP.md`                 | Complete setup guide       |
| `docs/GITHUB_ENVIRONMENT_OLLAMA.md`    | GitHub environment guide   |

## 🎯 Model Recommendations

| Use Case      | Model                         | Size   | Why                     |
| ------------- | ----------------------------- | ------ | ----------------------- |
| CI/CD Testing | `tinyllama` or `qwen2.5:0.5b` | ~400MB | Fast, minimal resources |
| Development   | `tinyllama`                   | ~800MB | Quick iteration         |
| Staging       | `phi3:mini`                   | ~2.3GB | Good quality            |
| Production    | `mistral`                     | ~4.1GB | Best quality            |

## ⚡ Common Tasks

### Change Model

```bash
# Update config.json
cat > config.json << EOF
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "ollama",
  "model": "phi3:mini"
}
EOF

# Pull new model
ollama pull phi3:mini
```

### Test Ollama

```bash
# Check service
curl http://localhost:11434/api/version

# Test inference
curl http://localhost:11434/api/generate -d '{
  "model": "tinyllama",
  "prompt": "Hello",
  "stream": false
}'

# Test OpenAI API
curl http://localhost:11434/v1/chat/completions -d '{
  "model": "tinyllama",
  "messages": [{"role": "user", "content": "Hi"}]
}'
```

### Troubleshooting

```bash
# Check if running
ps aux | grep ollama

# Restart service
pkill ollama
ollama serve &

# View logs (Docker)
docker-compose logs ollama

# Check disk space
df -h
ollama list  # See installed models
```

## 🔗 Quick Links

- **Full Setup Guide**: [docs/OLLAMA_SETUP.md](docs/OLLAMA_SETUP.md)
- **GitHub Environment**: [docs/GITHUB_ENVIRONMENT_OLLAMA.md](docs/GITHUB_ENVIRONMENT_OLLAMA.md)
- **Ollama Docs**: <https://github.com/ollama/ollama>
- **Model Library**: <https://ollama.com/library>

## 📊 Resource Usage

| Model        | RAM  | Disk   | Speed      |
| ------------ | ---- | ------ | ---------- |
| qwen2.5:0.5b | ~1GB | ~400MB | ⚡⚡⚡⚡⚡ |
| tinyllama    | ~2GB | ~800MB | ⚡⚡⚡⚡   |
| gemma2:2b    | ~3GB | ~1.6GB | ⚡⚡⚡     |
| phi3:mini    | ~4GB | ~2.3GB | ⚡⚡       |
| mistral      | ~8GB | ~4.1GB | ⚡         |

## ✅ Checklist

- [ ] Ollama installed (`ollama --version`)
- [ ] Service running (`curl localhost:11434/api/version`)
- [ ] Model pulled (`ollama list`)
- [ ] Config updated (`cat config.json`)
- [ ] Application tested (`gradle bootRun`)
- [ ] Docker working (`docker-compose up -d`)
- [ ] GitHub workflow configured (`.github/workflows/ollama-service.yml`)

---

**Last Updated:** February 2, 2026
**Status:** Ready for use ✅
**Maintained By:** java-resumes development team
