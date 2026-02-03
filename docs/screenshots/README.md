# Screenshots Directory - Complete Index

Central hub for all visual documentation and screenshots in the java-resumes project.

## Table of Contents

- [Overview](#overview)
- [Directory Structure](#directory-structure)
- [Screenshot Categories](#screenshot-categories)
- [Theme Variants](#theme-variants)
- [Quick Links](#quick-links)
- [How to Use Screenshots](#how-to-use-screenshots)
- [Markdown Guidelines](#markdown-guidelines-for-screenshots)

---

## Overview

This directory contains all visual documentation for the java-resumes application, organized by category for easy navigation and reference.

**Contents:**

- ✅ UI screenshots (dark & light themes)
- ✅ Setup and deployment guides
- ✅ Architecture diagrams and visualizations
- ✅ API documentation with Swagger examples
- ✅ Complete visual reference for documentation

**Total Screenshots:** 23 files
**Formats:** PNG (lossless compression)
**Quality:** High resolution, suitable for documentation and presentations

---

## Directory Structure

```
docs/screenshots/
│
├── README.md                          # This file - directory index
│
├── UI_SCREENSHOTS.md                 # UI theme showcase (dark & light)
│
├── ui/                               # User interface screenshots
│   ├── main-tab-dark.png            # Resume editor - dark theme
│   ├── main-tab-light.png           # Resume editor - light theme
│   ├── file-history-dark.png        # File history - dark theme
│   ├── file-history-light.png       # File history - light theme
│   ├── settings-tab-dark.png        # Settings - dark theme
│   ├── settings-tab-light.png       # Settings - light theme
│   ├── tools-tab-dark.png           # Tools - dark theme
│   ├── tools-tab-light.png          # Tools - light theme
│   ├── add-model-dark.png           # Add model dialog - dark theme
│   └── add-model-light.png          # Add model dialog - light theme
│
├── setup/                            # Setup and deployment documentation
│   ├── README.md                    # Setup guide with screenshots
│   ├── local-dev-setup.png         # Docker compose up output
│   ├── build-success.png           # Gradle build completion
│   ├── backend-docker.png          # Backend container logs
│   └── frontend-docker.png         # Frontend container logs
│
├── architecture/                     # System architecture and diagrams
│   ├── README.md                    # Architecture guide
│   ├── system-architecture-backend.png   # Backend architecture
│   ├── system-architecture-frontend.png  # Frontend architecture
│   ├── backend-uml.png              # Backend class diagram
│   ├── frontend-uml.png             # Frontend component diagram
│   ├── data-flow.png                # Document processing flow
│   └── document-processing-flow.png # Full process visualization
│
└── api/                             # API documentation
    ├── README.md                    # API documentation guide
    ├── swagger-ui.png               # Swagger UI interface
    ├── api-endpoints.png            # Endpoint reference chart
    └── error-responses.png          # Error response examples
```

---

## Screenshot Categories

### 1. User Interface Screenshots

**Location:** `ui/`
**Contents:** 10 images (5 dark + 5 light theme pairs)

| Component    |           Dark           |           Light           | Status |
| ------------ | :----------------------: | :-----------------------: | :----: |
| Main Tab     |   ✅ main-tab-dark.png   |   ✅ main-tab-light.png   | Ready  |
| File History | ✅ file-history-dark.png | ✅ file-history-light.png | Ready  |
| Settings     | ✅ settings-tab-dark.png | ✅ settings-tab-light.png | Ready  |
| Tools        |  ✅ tools-tab-dark.png   |  ✅ tools-tab-light.png   | Ready  |
| Add Model    |  ✅ add-model-dark.png   |  ✅ add-model-light.png   | Ready  |

**See:** [UI Screenshots - Theme Showcase](./UI_SCREENSHOTS.md)

### 2. Setup & Deployment Screenshots

**Location:** `setup/`
**Contents:** 4 images

| Screenshot          |  Status  | Purpose                          |
| ------------------- | :------: | -------------------------------- |
| local-dev-setup.png | ✅ Ready | Docker Compose environment setup |
| build-success.png   | ✅ Ready | Gradle build completion          |
| backend-docker.png  | ✅ Ready | Backend container startup logs   |
| frontend-docker.png | ✅ Ready | Frontend Vite dev server logs    |

**See:** [Setup Screenshots Guide](./setup/README.md)

### 3. Architecture Diagrams

**Location:** `architecture/`
**Contents:** 7 images

| Diagram                          |  Status  | Purpose                         |
| -------------------------------- | :------: | ------------------------------- |
| system-architecture-backend.png  | ✅ Ready | Spring Boot system architecture |
| system-architecture-frontend.png | ✅ Ready | React frontend structure        |
| backend-uml.png                  | ✅ Ready | Backend class diagram           |
| frontend-uml.png                 | ✅ Ready | Frontend component diagram      |
| data-flow.png                    | ✅ Ready | Resume optimization flow        |
| document-processing-flow.png     | ✅ Ready | Document pipeline               |

**See:** [Architecture Diagrams Guide](./architecture/README.md)

### 4. API Documentation

**Location:** `api/`
**Contents:** 3 images

| Reference           |  Status  | Purpose                       |
| ------------------- | :------: | ----------------------------- |
| swagger-ui.png      | ✅ Ready | Swagger/OpenAPI documentation |
| api-endpoints.png   | ✅ Ready | REST endpoint reference       |
| error-responses.png | ✅ Ready | Error handling examples       |

**See:** [API Documentation Screenshots](./api/README.md)

---

## Theme Variants

### Dark Theme (10 screenshots)

- **Background:** Dark gray/black
- **Text:** White/light gray
- **Accents:** Bright blue/cyan
- **Use Case:** Low-light environments, reduced eye strain
- **Files:** All `*-dark.png` variants

### Light Theme (10 screenshots)

- **Background:** White/light gray
- **Text:** Dark gray/black
- **Accents:** Blue/teal
- **Use Case:** Professional environments, daytime use
- **Files:** All `*-light.png` variants

---

## Quick Links

### By Category

| Category        | Guide                                              | Images |
| --------------- | -------------------------------------------------- | ------ |
| 🎨 UI Themes    | [UI_SCREENSHOTS.md](./UI_SCREENSHOTS.md)           | 10     |
| 🚀 Setup        | [setup/README.md](./setup/README.md)               | 4      |
| 🏗️ Architecture | [architecture/README.md](./architecture/README.md) | 7      |
| 📡 API          | [api/README.md](./api/README.md)                   | 3      |

### By Purpose

- **For New Developers:** Start with [Setup Guide](./setup/README.md)
- **For UI/UX Reference:** See [UI Screenshots](./UI_SCREENSHOTS.md)
- **For Architecture Understanding:** View [Architecture Diagrams](./architecture/README.md)
- **For API Integration:** Check [API Documentation](./api/README.md)

---

## How to Use Screenshots

### In Markdown Documentation

**Basic Image Reference:**

```markdown
![Alt text describing image](path/to/image.png)
```

**With Relative Path:**

```markdown
# From docs/ directory:

![UI Screenshot](./screenshots/frontend/main-tab-dark.png)

# From docs/api/ directory:

![Setup Guide](../setup/local-dev-setup.png)

# From docs/screenshots/api/ directory:

![Dark Theme](../frontend/main-tab-dark.png)
```

### Creating Side-by-Side Comparisons

**Table Format (Dark vs Light):**

```markdown
|               Dark Theme               |               Light Theme                |
| :------------------------------------: | :--------------------------------------: |
| ![Dark](../frontend/main-tab-dark.png) | ![Light](../frontend/main-tab-light.png) |
```

---

## Markdown Guidelines for Screenshots

### File Naming Conventions

✅ **Good Examples:**

- `main-tab-dark.png` - Clear, descriptive, lowercase
- `build-success.png` - Specific purpose
- `api-endpoints.png` - Feature-focused

### Image Link Format

```markdown
![Descriptive Alt Text](relative/path/to/image.png)
```

**Requirements:**

- Alt text: Describe image content
- Path: Relative with forward slashes
- Format: Lowercase, hyphens for spaces

### Alt Text Guidelines

✅ **Descriptive Alt Text:**

- "Main tab interface with dark theme"
- "Gradle build success output"
- "API endpoints reference"

### Image Placement

**Best Practice - Image Below Heading:**

```markdown
## Feature Name

Brief description.

![Feature Screenshot](./path/to/image.png)

Explanation of what to see...
```

---

## Documentation Integration

**Where Screenshots Are Used:**

| File                     | Category           | Purpose                   |
| ------------------------ | ------------------ | ------------------------- |
| `DOCKER_DEV_SETUP.md`    | Setup, UI          | Development environment   |
| `docs/architecture/ARCHITECTURE.md`   | Architecture, API  | System design reference   |
| `docs/BACKEND_README.md` | API, architecture  | Backend API documentation |
| `README.md` (root)       | Setup, UI overview | Project introduction      |

---

**Last Updated:** January 22, 2026
**Total Screenshots:** 23
**Status:** ✅ Complete visual documentation suite

- [ ] deployment.png - Docker deployment setup
- [ ] component-diagram.png - Component relationships

### Setup & Deployment

- [ ] docker-compose-up.png - Docker startup output
- [ ] local-dev-setup.png - Local development environment
- [ ] build-success.png - Build completion

## 🔗 Usage in Documentation

Screenshots are referenced in:

- **README.md** - Main project overview and features
- **docs/README.md** - Comprehensive documentation
- **docs/BACKEND_README.md** - Backend setup guide
- **frontend/README.md** - Frontend development guide
- **docs/architecture/ARCHITECTURE.md** - System architecture details

## 💾 Storage & Optimization

- **Format**: PNG for UI, can use SVG for diagrams
- **Target size**: < 500KB per image
- **Resolution**: 72-96 DPI
- **Minimum width**: 800px
- **Naming**: `{category}-{component}-{description}.png`

## 🔄 Maintenance

Screenshots should be reviewed and updated:

- ✅ When features change
- ✅ When UI is redesigned
- ✅ When new versions are released
- ✅ When bugs affecting visuals are fixed
- ✅ Quarterly comprehensive review

## 📚 Related Documentation

- [SCREENSHOTS_GUIDE.md](SCREENSHOTS_GUIDE.md) - Complete guide
- [../../README.md](../../README.md) - Project README with screenshot references
- [../../docs/architecture/ARCHITECTURE.md](../../docs/architecture/ARCHITECTURE.md) - Architecture documentation

## 🎨 Tools & Resources

### Recommended Tools

- **Capturing**: Windows Snip & Sketch, ShareX
- **Diagrams**: Mermaid, draw.io, PlantUML
- **Optimization**: TinyPNG, ImageMagick
- **Editing**: GIMP, Photoshop

### Links

- [Mermaid Diagram Syntax](https://mermaid-js.github.io/)
- [draw.io Online Editor](https://app.diagrams.net/)
- [TinyPNG Image Compression](https://tinypng.com/)
- [ShareX - Free Screenshot Tool](https://getsharex.com/)

## 📝 Notes

- All screenshots should include meaningful alt text for accessibility
- Keep screenshots free of sensitive information (API keys, tokens, passwords)
- Test screenshots on multiple devices (desktop, tablet, mobile)
- Document screenshot creation date and tool used
- Version control screenshots appropriately (Git LFS for large files)

---

Last updated: 2026-01-16

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
