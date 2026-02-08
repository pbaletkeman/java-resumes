# GitHub Actions Workflows for java-resumes

This directory contains all CI/CD and automation workflows for the java-resumes project. Each workflow is designed to support a specific part of the development, testing, security, deployment, and release lifecycle for both the backend (Spring Boot/Java) and frontend (React/TypeScript) components, as well as LLM integration and full release management.

- [GitHub Actions Workflows for java-resumes](#github-actions-workflows-for-java-resumes)
  - [Workflow Overview](#workflow-overview)
  - [Workflow Details](#workflow-details)
    - [1. `backend-build.yml`](#1-backend-buildyml)
    - [2. `frontend-build.yml`](#2-frontend-buildyml)
    - [3. `ollama-service.yml`](#3-ollama-serviceyml)
    - [4. `release.yml`](#4-releaseyml)
  - [Usage Notes](#usage-notes)
  - [Adding/Modifying Workflows](#addingmodifying-workflows)
  - [References](#references)

---

## Workflow Overview

| Workflow File        | Purpose / Triggers                                                       | Key Jobs & Steps                                                                              |
| -------------------- | ------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------- |
| `backend-build.yml`  | Build, test, checkstyle, security scan, and Docker build for backend.    | - Build/test with Gradle<br>- Checkstyle<br>- Trivy security scan<br>- Docker build (dry run) |
| `frontend-build.yml` | Build, lint, type-check, test, and deploy frontend.                      | - npm build/lint/type-check/test<br>- Archive and upload build<br>- Deploy (customizable)     |
| `ollama-service.yml` | Provision and test Ollama LLM service for integration and backend tests. | - Install/start Ollama<br>- Pull/test model<br>- Integration test with backend                |
| `release.yml`        | Manual full release pipeline for frontend/backend or both.               | - Build artifacts<br>- Create GitHub release<br>- Summarize release                           |

---

## Workflow Details

### 1. `backend-build.yml`

- **Triggers**: On push/pull_request to `master`/`develop` (backend files), or manual dispatch.
- **Jobs**:
  - **build**: Checks out code, sets up Java 21 (Corretto), runs Gradle tests, checkstyle, builds fat JAR, uploads artifacts and test reports.
  - **security-scan**: Runs Trivy vulnerability scan on the repo, uploads SARIF results to GitHub Security tab.
  - **docker-build**: (Push to master only) Builds Docker image using the backend JAR (dry run, not pushed).
- **Artifacts**: Backend JARs, test reports, Trivy scan results.

### 2. `frontend-build.yml`

- **Triggers**: On push/pull_request to `master`/`develop` (frontend files), or manual dispatch.
- **Jobs**:
  - **build**: Checks out code, sets up Node.js 20.x, installs dependencies, runs lint/type-check/tests, builds production bundle, uploads artifacts, archives build for deployment.
  - **deploy**: (Push to master only) Downloads build artifact and runs a placeholder deploy step (customize for your platform).
- **Artifacts**: Frontend build output (`dist/`), production bundle tarball.

### 3. `ollama-service.yml`

- **Triggers**: Manual dispatch (with model selection), or on workflow file changes.
- **Jobs**:
  - **setup-ollama**: Installs and starts Ollama LLM service, pulls a model, tests both native and OpenAI-compatible endpoints, uploads model info artifact.
  - **integration-test**: (Needs setup-ollama) Installs Ollama, pulls model, updates backend `config.json`, builds backend, starts backend service, tests health endpoint, summarizes results.
- **Artifacts**: Ollama model info, integration test summary.

### 4. `release.yml`

- **Triggers**: Manual dispatch only (with release type and component selection).
- **Jobs**:
  - **frontend-build**: (If selected) Builds frontend, uploads versioned artifact.
  - **backend-build**: (If selected) Builds backend, uploads versioned artifact.
  - **create-release**: (Needs build jobs) Generates release notes, creates GitHub release with combined version tag.
  - **notify**: Summarizes the release pipeline results in the GitHub Actions summary.
- **Artifacts**: Versioned frontend and backend builds, GitHub release notes.

---

## Usage Notes

- **Artifacts**: All workflows upload build/test artifacts for traceability and deployment.
- **Branch Policy**: Most workflows run on `master` and `develop` branches, with Docker/deploy/release steps restricted to `master`.
- **Manual Triggers**: Use the GitHub Actions UI to manually trigger `release.yml` or `ollama-service.yml` with custom parameters.
- **Security**: Trivy scans backend for vulnerabilities; results are uploaded to the Security tab.
- **LLM Integration**: `ollama-service.yml` provisions a local Ollama service for backend integration and testing.
- **Customization**: The `deploy` step in `frontend-build.yml` is a placeholder—replace with your actual deployment logic.

---

## Adding/Modifying Workflows

- Place new workflow YAML files in this `.github/workflows/` directory.
- Follow the existing structure for job naming, artifact handling, and environment setup.
- Reference this README for guidance on triggers, job structure, and artifact conventions.

---

## References

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Gradle Build Tool](https://docs.gradle.org/)
- [Node.js](https://nodejs.org/)
- [Ollama LLM](https://ollama.com/)
- [Trivy Security Scanner](https://aquasecurity.github.io/trivy/)
