# Visual Documentation Navigation Guide

Quick reference for finding and using visual documentation throughout the java-resumes project.

## Quick Navigation Map

```
📚 Main Documentation Hub
└─ docs/INDEX.md
   │
   ├─ 🎨 Visual Documentation
   │  ├─ UI_SCREENSHOTS.md ...................... Dark & light UI themes
   │  ├─ screenshots/setup/README.md ........... Setup & deployment
   │  ├─ screenshots/architecture/README.md ... System diagrams
   │  └─ screenshots/api/README.md ............ REST API reference
   │
   └─ 📖 Related Guides
      ├─ DOCKER_DEV_SETUP.md .................. Development environment
      ├─ Architecture.md ...................... System design
      ├─ BACKEND_README.md ................... Backend API docs
      └─ README.md (root) .................... Project overview
```

---

## Finding Screenshots by Purpose

### "I want to see how the UI looks"

**→ Start here:** [`docs/screenshots/UI_SCREENSHOTS.md`](docs/screenshots/UI_SCREENSHOTS.md)

**You'll find:**

- Main tab (resume editor) - dark & light
- File history tab - dark & light
- Settings tab - dark & light
- Tools tab - dark & light
- Add model dialog - dark & light

**Alternative:** [`DOCKER_DEV_SETUP.md`](DOCKER_DEV_SETUP.md) has UI preview section

---

### "I need to set up the development environment"

**→ Start here:** [`docs/screenshots/setup/README.md`](docs/screenshots/setup/README.md)

**You'll find:**

- Docker Compose setup (local-dev-setup.png)
- Gradle build output (build-success.png)
- Backend startup logs (backend-docker.png)
- Frontend startup logs (frontend-docker.png)

**Alternative:** [`DOCKER_DEV_SETUP.md`](DOCKER_DEV_SETUP.md) for detailed guide

---

### "I want to understand the system architecture"

**→ Start here:** [`docs/screenshots/architecture/README.md`](docs/screenshots/architecture/README.md)

**You'll find:**

- Backend architecture diagram
- Frontend architecture diagram
- Component relationships
- UML class diagrams
- Data flow visualizations
- Deployment architecture

**Alternative:** [`Architecture.md`](Architecture.md) for detailed documentation

---

### "I need to integrate with the REST API"

**→ Start here:** [`docs/screenshots/api/README.md`](docs/screenshots/api/README.md)

**You'll find:**

- Swagger UI reference
- Endpoint documentation
- HTTP status codes
- Error response examples
- Usage examples (curl, JavaScript)

**Alternative:** [`BACKEND_README.md`](BACKEND_README.md) for complete API docs

---

### "I want a directory of all screenshots"

**→ Start here:** [`docs/screenshots/README.md`](docs/screenshots/README.md)

**You'll find:**

- Complete directory structure
- All 23 screenshots catalogued
- Quick links by category
- Usage guidelines
- Markdown best practices

---

## Screenshot Inventory

### By Location

**UI Themes:** `docs/screenshots/ui/`

- 10 files (5 dark + 5 light)
- Size: ~300 KB total
- Coverage: All major interface sections

**Setup/Deployment:** `docs/screenshots/setup/`

- 4 files (Docker, build, logs)
- Size: ~160 KB total
- Coverage: Development environment

**Architecture:** `docs/screenshots/architecture/`

- 7 files (diagrams, UML, flows)
- Size: ~200 KB total
- Coverage: System design

**API Reference:** `docs/screenshots/api/`

- 3 files (Swagger, endpoints, errors)
- Size: ~80 KB total
- Coverage: REST API

**TOTAL:** 24 image files, ~740 KB

### By Type

**Interface Screenshots:** 10 (UI variants)
**Setup Screenshots:** 4 (docker, build, logs)
**Architecture Diagrams:** 7 (UML, flows, system)
**API Documentation:** 3 (Swagger, endpoints, errors)

### By Theme

**Dark Theme:** 5 UI screenshots
**Light Theme:** 5 UI screenshots
**Neutral:** 14 (setup, architecture, api)

---

## How to Reference Screenshots

### In Your Own Documentation

**Basic format:**

```markdown
![Description](relative/path/to/image.png)
```

**Examples from this project:**

From `docs/`:

```markdown
![UI Screenshot](./screenshots/ui/main-tab-dark.png)
```

From `docs/api/`:

```markdown
![API Endpoints](../screenshots/api/api-endpoints.png)
```

From `frontend/`:

```markdown
![System Architecture](../docs/screenshots/architecture/system-architecture-backend.png)
```

### Creating Comparisons

**Side-by-side dark/light:**

```markdown
|           Dark Theme            |            Light Theme            |
| :-----------------------------: | :-------------------------------: |
| ![Dark](./ui/main-tab-dark.png) | ![Light](./ui/main-tab-light.png) |
```

---

## Documentation Structure

### Top Level (`docs/`)

| File                  | Purpose           | Screenshots            |
| --------------------- | ----------------- | ---------------------- |
| `INDEX.md`            | Documentation hub | Links to all guides    |
| `Architecture.md`     | System design     | Links to architecture/ |
| `BACKEND_README.md`   | API documentation | Links to api/          |
| `DOCKER_DEV_SETUP.md` | Development setup | UI preview section     |

### Screenshots Directory (`docs/screenshots/`)

| File                     | Purpose          | Images              |
| ------------------------ | ---------------- | ------------------- |
| `README.md`              | Screenshot index | Directory structure |
| `UI_SCREENSHOTS.md`      | UI showcase      | 10 dark/light pairs |
| `setup/README.md`        | Setup guide      | 4 deployment        |
| `architecture/README.md` | Architecture     | 7 diagrams          |
| `api/README.md`          | API reference    | 3 API docs          |

---

## Recommended Reading Order

### For New Developers

1. Start: [`docs/INDEX.md`](docs/INDEX.md)
2. Setup: [`docs/screenshots/setup/README.md`](docs/screenshots/setup/README.md)
3. UI: [`docs/screenshots/UI_SCREENSHOTS.md`](docs/screenshots/UI_SCREENSHOTS.md)
4. Architecture: [`docs/screenshots/architecture/README.md`](docs/screenshots/architecture/README.md)
5. API: [`docs/screenshots/api/README.md`](docs/screenshots/api/README.md)

### For Backend Developers

1. Start: [`docs/INDEX.md`](docs/INDEX.md)
2. API: [`docs/screenshots/api/README.md`](docs/screenshots/api/README.md)
3. Architecture: [`docs/screenshots/architecture/README.md`](docs/screenshots/architecture/README.md)
4. Setup: [`docs/screenshots/setup/README.md`](docs/screenshots/setup/README.md)

### For Frontend Developers

1. Start: [`docs/INDEX.md`](docs/INDEX.md)
2. UI: [`docs/screenshots/UI_SCREENSHOTS.md`](docs/screenshots/UI_SCREENSHOTS.md)
3. Architecture: [`docs/screenshots/architecture/README.md`](docs/screenshots/architecture/README.md)
4. Setup: [`docs/screenshots/setup/README.md`](docs/screenshots/setup/README.md)

### For DevOps/Deployment

1. Start: [`docs/INDEX.md`](docs/INDEX.md)
2. Setup: [`docs/screenshots/setup/README.md`](docs/screenshots/setup/README.md)
3. Architecture: [`docs/screenshots/architecture/README.md`](docs/screenshots/architecture/README.md)

---

## Direct Links to All Guides

### Main Documentation

- 📚 [Documentation Index](docs/INDEX.md)
- 🎨 [UI Screenshots Showcase](docs/screenshots/UI_SCREENSHOTS.md)
- 🗂️ [Screenshots Directory Index](docs/screenshots/README.md)

### Category Guides

- 🚀 [Setup & Deployment](docs/screenshots/setup/README.md)
- 🏗️ [Architecture Diagrams](docs/screenshots/architecture/README.md)
- 📡 [API Documentation](docs/screenshots/api/README.md)

### Supporting Documentation

- 🐳 [Docker Development Setup](DOCKER_DEV_SETUP.md)
- 🏛️ [System Architecture](Architecture.md)
- 🔌 [Backend API Reference](docs/BACKEND_README.md)
- 🎯 [Integration Summary](docs/SCREENSHOTS_INTEGRATION_SUMMARY.md)

---

## Visual Documentation Map

```
┌─────────────────────────────────────────────────────────┐
│          Documentation Structure & Navigation           │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  📚 docs/INDEX.md (Main Hub)                           │
│  │                                                      │
│  ├─► 🎨 Visual Documentation Section                   │
│  │   ├─► UI_SCREENSHOTS.md (10 images)                  │
│  │   ├─► setup/README.md (4 images)                     │
│  │   ├─► architecture/README.md (7 images)             │
│  │   └─► api/README.md (3 images)                      │
│  │                                                     │
│  ├─► 📖 Technical Reference Section                    │
│  │   ├─► Architecture.md                               │
│  │   ├─► BACKEND_README.md                             │
│  │   ├─► DOCKER_DEV_SETUP.md                           │
│  │   └─► README.md (root)                              │
│  │                                                     │
│  └─► 📋 Documentation Section                          │
│      ├─► screenshots/README.md (Directory Index)       │
│      └─► SCREENSHOTS_INTEGRATION_SUMMARY.md            │
│                                                        │
└────────────────────────────────────────────────────────┘
```

---

## Quick Search Guide

| Looking for...          | Go to...                                                                                  |
| ----------------------- | ----------------------------------------------------------------------------------------- |
| Dark theme UI           | [UI_SCREENSHOTS.md](docs/screenshots/UI_SCREENSHOTS.md#dark-theme)                        |
| Light theme UI          | [UI_SCREENSHOTS.md](docs/screenshots/UI_SCREENSHOTS.md#light-theme)                       |
| Docker setup            | [setup/README.md](docs/screenshots/setup/README.md)                                       |
| System diagram          | [architecture/README.md](docs/screenshots/architecture/README.md)                         |
| API endpoints           | [api/README.md](docs/screenshots/api/README.md#api-endpoints-reference)                   |
| Swagger UI              | [api/README.md](docs/screenshots/api/README.md#swagger-ui-documentation)                  |
| Error responses         | [api/README.md](docs/screenshots/api/README.md#error-responses-guide)                     |
| Component relationships | [architecture/README.md](docs/screenshots/architecture/README.md#component-relationships) |
| Data flow               | [architecture/README.md](docs/screenshots/architecture/README.md#data-flow-diagram)       |

---

## Features of Documentation

✅ **Comprehensive:** 23 screenshots covering all aspects
✅ **Well-organized:** Clear directory structure and navigation
✅ **Cross-referenced:** Links between related documents
✅ **Professional:** High-quality images and descriptions
✅ **Accessible:** Proper alt text for all images
✅ **Compliant:** Follows project markdown guidelines
✅ **Current:** Up-to-date with latest version

---

## Tips for Using Visual Documentation

1. **Start with INDEX.md** - Always start from the main documentation index
2. **Use Category Guides** - Each category has a dedicated README
3. **Follow Cross-References** - Links guide you to related content
4. **Check Dark/Light Variants** - Understand both UI themes
5. **Read Image Captions** - Context explains what you're seeing
6. **Reference Examples** - Usage examples in guides

---

## Questions?

- **"Where is feature X?"** → Check [UI_SCREENSHOTS.md](docs/screenshots/UI_SCREENSHOTS.md)
- **"How do I set up?"** → See [setup/README.md](docs/screenshots/setup/README.md)
- **"How does the system work?"** → Review [architecture/README.md](docs/screenshots/architecture/README.md)
- **"How do I use the API?"** → Check [api/README.md](docs/screenshots/api/README.md)
- **"Where are all screenshots?"** → View [screenshots/README.md](docs/screenshots/README.md)

---

**Last Updated:** January 22, 2026
**Status:** ✅ Complete visual documentation
**Total References:** 23 screenshots
**Total Documents:** 10+ guide files
