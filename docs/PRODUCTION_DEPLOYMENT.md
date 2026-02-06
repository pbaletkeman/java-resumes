# Production Deployment

Complete guide for deploying java-resumes to production environments.

- [Production Deployment](#production-deployment)
  - [Deployment Options](#deployment-options)
    - [Quick Comparison](#quick-comparison)
  - [Docker Compose Deployment](#docker-compose-deployment)
    - [Prerequisite: Configure Application](#prerequisite-configure-application)
    - [Step 1: Build Docker Images](#step-1-build-docker-images)
    - [Step 2: Create docker-compose.yml](#step-2-create-docker-composeyml)
    - [Step 3: Deploy](#step-3-deploy)
  - [HTTPS/SSL Configuration](#httpsssl-configuration)
    - [Using Let's Encrypt](#using-lets-encrypt)
    - [Obtain SSL Certificate](#obtain-ssl-certificate)
  - [Cloud Deployment](#cloud-deployment)
    - [AWS Deployment](#aws-deployment)
    - [Azure Deployment](#azure-deployment)
    - [Google Cloud Deployment](#google-cloud-deployment)
  - [Kubernetes Deployment](#kubernetes-deployment)
    - [Manifest Files](#manifest-files)
    - [Deploy to Kubernetes](#deploy-to-kubernetes)
  - [Security Hardening](#security-hardening)
    - [Database Security](#database-security)
    - [Secrets Management](#secrets-management)
    - [Network Security](#network-security)
  - [Monitoring \& Logging](#monitoring--logging)
    - [Docker Logs](#docker-logs)
    - [Application Monitoring (Future)](#application-monitoring-future)
    - [Health Checks](#health-checks)
  - [Continuous Deployment](#continuous-deployment)
    - [GitHub Actions CI/CD](#github-actions-cicd)
  - [Deployment Checklist](#deployment-checklist)
    - [Pre-Deployment](#pre-deployment)
    - [Deployment](#deployment)
    - [Post-Deployment](#post-deployment)
  - [Rollback Plan](#rollback-plan)
    - [Quick Rollback](#quick-rollback)
    - [Database Rollback (Future)](#database-rollback-future)
  - [Scaling Strategies](#scaling-strategies)
    - [Horizontal Scaling](#horizontal-scaling)
    - [Load Balancing](#load-balancing)
    - [Caching Strategy](#caching-strategy)

---

## 🚀 Deployment Options

### Quick Comparison

| Option         | Effort | Cost | Scalability | Recommended |
| -------------- | ------ | ---- | ----------- | ----------- |
| Docker Compose | Low    | Low  | Single Host | Dev/Small   |
| Single Server  | Low    | Low  | Limited     | Hobby       |
| Kubernetes     | High   | High | Excellent   | Enterprise  |
| PaaS (Heroku)  | Low    | High | Good        | Startup     |
| Serverless     | Medium | Low  | Auto        | API-only    |

---

## 🐳 Docker Compose Deployment

### Prerequisite: Configure Application

**Create production environment file:** `.env.production`

```env
# API Configuration
VITE_API_BASE_URL=https://api.yourdomain.com
VITE_APP_TITLE=Resume Optimizer

# Backend Configuration
SPRING_PROFILES_ACTIVE=prod
UPLOAD_PATH=/data/files
LOG_LEVEL=INFO

# LLM Configuration
LLM_ENDPOINT=https://api.openai.com/v1/chat/completions
LLM_APIKEY=sk-your-production-key
LLM_MODEL=gpt-4

# External Paths
APP_CONFIG_PATH=/config/app
PROMPTS_DIR=/config/prompts
```

### Step 1: Build Docker Images

```bash
# Build backend image
docker build -f Dockerfile -t java-resumes:latest .

# Build frontend image
docker build -f frontend/Dockerfile -t java-resumes-frontend:latest ./frontend

# Or use compose to build both
docker compose build
```

### Step 2: Create docker-compose.yml

**Production configuration** (`docker-compose.prod.yml`):

```yaml
version: "3.8"

services:
  frontend:
    image: java-resumes-frontend:latest
    container_name: java-resumes-frontend
    restart: always
    ports:
      - "80:80"
    environment:
      - VITE_API_BASE_URL=https://api.yourdomain.com
    networks:
      - resume-app-network
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:80"]
      interval: 30s
      timeout: 10s
      retries: 3

  backend:
    image: java-resumes:latest
    container_name: java-resumes-backend
    restart: always
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      UPLOAD_PATH: /data/files
      LLM_ENDPOINT: https://api.openai.com/v1/chat/completions
      LLM_APIKEY: ${LLM_APIKEY}
      APP_CONFIG_PATH: /config/app
      PROMPTS_DIR: /config/prompts
    volumes:
      - backend-files:/data/files
      - ./config:/config
    networks:
      - resume-app-network
    depends_on:
      - frontend
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/api/health"]
      interval: 30s
      timeout: 10s
      retries: 3

  nginx:
    image: nginx:alpine
    container_name: java-resumes-nginx
    restart: always
    ports:
      - "443:443"
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf
      - ./certs:/etc/nginx/certs
    networks:
      - resume-app-network
    depends_on:
      - frontend

networks:
  resume-app-network:
    driver: bridge

volumes:
  backend-files:
    driver: local
```

### Step 3: Deploy

```bash
# Production deployment
docker compose -f docker-compose.prod.yml up -d

# Check status
docker compose -f docker-compose.prod.yml ps

# View logs
docker compose -f docker-compose.prod.yml logs -f

# Stop services
docker compose -f docker-compose.prod.yml down
```

---

## 🔐 HTTPS/SSL Configuration

### Using Let's Encrypt

**Nginx configuration with SSL:**

```nginx
server {
    listen 443 ssl http2;
    server_name yourdomain.com;

    ssl_certificate /etc/letsencrypt/live/yourdomain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/yourdomain.com/privkey.pem;

    # Security headers
    add_header Strict-Transport-Security "max-age=31536000" always;
    add_header X-Content-Type-Options nosniff;
    add_header X-Frame-Options SAMEORIGIN;

    location / {
        proxy_pass http://frontend:80;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location /api {
        proxy_pass http://backend:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}

# Redirect HTTP to HTTPS
server {
    listen 80;
    server_name yourdomain.com;
    return 301 https://$server_name$request_uri;
}
```

### Obtain SSL Certificate

```bash
# Using Certbot
certbot certonly --standalone -d yourdomain.com

# Certificate location
/etc/letsencrypt/live/yourdomain.com/
```

---

## ☁️ Cloud Deployment

### AWS Deployment

**Using EC2 + RDS:**

1. **Launch EC2 Instance**
   - AMI: Ubuntu 22.04 LTS
   - Instance Type: t3.medium (minimum)
   - Storage: 100GB EBS
   - Security Group: Allow 80, 443, 8080

2. **Install Docker**

   ```bash
   curl -fsSL https://get.docker.com -o get-docker.sh
   sudo sh get-docker.sh
   ```

3. **Clone and Deploy**
   ```bash
   git clone https://github.com/pbaletkeman/java-resumes.git
   cd java-resumes
   docker compose -f docker-compose.prod.yml up -d
   ```

### Azure Deployment

**Using App Service:**

1. **Create Resource Group**

   ```bash
   az group create --name mygroup --location eastus
   ```

2. **Create App Service**

   ```bash
   az appservice plan create --name myplan --resource-group mygroup --sku B2
   ```

3. **Deploy Container**
   ```bash
   az webapp create --name myapp --plan myplan --resource-group mygroup \
     --deployment-container-image-name java-resumes:latest
   ```

### Google Cloud Deployment

**Using Cloud Run:**

1. **Build and Push Image**

   ```bash
   gcloud builds submit --tag gcr.io/PROJECT_ID/java-resumes
   ```

2. **Deploy to Cloud Run**
   ```bash
   gcloud run deploy java-resumes --image gcr.io/PROJECT_ID/java-resumes \
     --platform managed --region us-central1 \
     --set-env-vars LLM_ENDPOINT=...
   ```

---

## ⚛️ Kubernetes Deployment

### Manifest Files

**deployment.yaml:**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: java-resumes-backend
spec:
  replicas: 2
  selector:
    matchLabels:
      app: java-resumes-backend
  template:
    metadata:
      labels:
        app: java-resumes-backend
    spec:
      containers:
        - name: backend
          image: java-resumes:latest
          ports:
            - containerPort: 8080
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: prod
            - name: LLM_ENDPOINT
              valueFrom:
                secretKeyRef:
                  name: llm-config
                  key: endpoint
          livenessProbe:
            httpGet:
              path: /api/health
              port: 8080
            initialDelaySeconds: 30
            periodSeconds: 10
          readinessProbe:
            httpGet:
              path: /api/health
              port: 8080
            initialDelaySeconds: 5
            periodSeconds: 5
```

**service.yaml:**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: java-resumes-service
spec:
  type: LoadBalancer
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  selector:
    app: java-resumes-backend
```

### Deploy to Kubernetes

```bash
# Create namespace
kubectl create namespace java-resumes

# Create secrets
kubectl create secret generic llm-config \
  --from-literal=endpoint=https://api.openai.com/v1/chat/completions \
  -n java-resumes

# Apply manifests
kubectl apply -f deployment.yaml -n java-resumes
kubectl apply -f service.yaml -n java-resumes

# Check status
kubectl get pods -n java-resumes
kubectl get svc -n java-resumes

# View logs
kubectl logs -f deployment/java-resumes-backend -n java-resumes
```

---

## 🔒 Security Hardening

### Database Security

```yaml
# Docker Compose - Add PostgreSQL
db:
  image: postgres:17-alpine
  environment:
    POSTGRES_PASSWORD: ${DB_PASSWORD}
    POSTGRES_DB: java_resumes
  volumes:
    - db-data:/var/lib/postgresql/data
  networks:
    - resume-app-network
  restart: always
```

### Secrets Management

```bash
# Using Docker Secrets
docker secret create llm_key /path/to/secret

# Using Kubernetes Secrets
kubectl create secret generic llm-apikey --from-literal=key=sk-...
```

### Network Security

- Firewall rules (allow only 80, 443)
- Network policies (Kubernetes)
- VPC/Subnets (Cloud)
- CORS restrictions
- Rate limiting

---

## 📉 Monitoring \& Logging

### Docker Logs

```bash
# View logs
docker compose logs -f backend
docker compose logs -f frontend

# Log rotation
docker run ... --log-driver json-file \
  --log-opt max-size=10m \
  --log-opt max-file=3
```

### Application Monitoring (Future)

**Prometheus + Grafana:**

```yaml
services:
  prometheus:
    image: prom/prometheus:latest
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
    ports:
      - "9090:9090"

  grafana:
    image: grafana/grafana:latest
    ports:
      - "3000:3000"
```

### Health Checks

```bash
# API Health
curl http://localhost:8080/api/health

# Container Health
docker compose ps

# Kubernetes Health
kubectl get pods --field-selector=status.phase=Failed
```

---

## 🔄 Continuous Deployment

### GitHub Actions CI/CD

**.github/workflows/deploy.yml:**

```yaml
name: Deploy to Production

on:
  push:
    branches: [main]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Build Docker Images
        run: docker compose build
      - name: Push to Registry
        run: |
          docker tag java-resumes myregistry/java-resumes:latest
          docker push myregistry/java-resumes:latest
      - name: Deploy to Server
        run: |
          ssh user@server 'cd /app && docker compose pull && docker compose up -d'
```

---

## ✅ Deployment Checklist

### Pre-Deployment

- [ ] Run all tests (backend: 80%+, frontend: 80%+)
- [ ] Run code quality checks (Checkstyle, ESLint)
- [ ] Update version numbers
- [ ] Create release notes
- [ ] Backup database (if applicable)
- [ ] Test in staging environment
- [ ] Review security checklist
- [ ] Configure monitoring and alerting

### Deployment

- [ ] Deploy backend service
- [ ] Deploy frontend application
- [ ] Run smoke tests
- [ ] Verify health endpoints
- [ ] Check logs for errors
- [ ] Monitor system resources
- [ ] Verify external integrations (LLM service)

### Post-Deployment

- [ ] Verify user workflows
- [ ] Check error rates
- [ ] Monitor performance metrics
- [ ] Collect user feedback
- [ ] Document deployment details
- [ ] Update runbook
- [ ] Plan rollback strategy

---

## 🗙️ Rollback Plan

### Quick Rollback

```bash
# Stop current deployment
docker compose down

# Revert to previous image
docker tag myregistry/java-resumes:v1.0.0 myregistry/java-resumes:latest

# Start previous version
docker compose up -d
```

### Database Rollback (Future)

```bash
# Restore from backup
postgresql_restore < backup.sql

# Migrate down
./gradlew flywayUndo
```

---

## 📈 Scaling Strategies

### Horizontal Scaling

```yaml
# Docker Compose - Multiple Replicas
services:
  backend:
    deploy:
      replicas: 3
```

### Load Balancing

```nginx
upstream backend {
    server backend1:8080;
    server backend2:8080;
    server backend3:8080;
}

server {
    location /api {
        proxy_pass http://backend;
    }
}
```

### Caching Strategy

- Frontend: Browser cache (manifests, assets)
- Backend: Redis (responses, sessions)
- Database: Query caching

---

**See also:**

- [Docker Setup](QUICK_START.md) - Docker Compose quick start
- [Architecture](ARCHITECTURE.md) - System design
- [Configuration](CONFIGURATION.md) - Configuration management
- [Troubleshooting](TROUBLESHOOTING.md) - Production troubleshooting

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
