# Screenshots and Visual Documentation Guide

This guide explains all screenshots used in the java-resumes project documentation, how they're organized, and how to update them.

---

## 🔗 Related Documentation

This is the main reference guide. For specific tasks, see:

| Document                                                 | Purpose                               | Time   |
| -------------------------------------------------------- | ------------------------------------- | ------ |
| **[README.md](README.md)**                               | Directory overview, quick start       | 2 min  |
| **[CAPTURE_WORKFLOW.md](CAPTURE_WORKFLOW.md)**           | Complete 85-minute capture workflow   | 90 min |
| **[TOOLS_AND_RESOURCES.md](TOOLS_AND_RESOURCES.md)**     | Tools comparison and setup            | 10 min |
| **[frontend/README.md](frontend/README.md)**             | Capture guide for 6 UI screenshots    | 30 min |
| **[backend/README.md](backend/README.md)**               | Capture guide for 3 API screenshots   | 20 min |
| **[architecture/DIAGRAMS.md](architecture/DIAGRAMS.md)** | Reference for 4 architecture diagrams | 15 min |
| **[setup/README.md](setup/README.md)**                   | Capture guide for 3 setup screenshots | 20 min |

### Start Here If You Want To

- 📷 **Capture all screenshots**: Follow [CAPTURE_WORKFLOW.md](CAPTURE_WORKFLOW.md) (85 min)
- 🎨 **Choose screenshot tools**: See [TOOLS_AND_RESOURCES.md](TOOLS_AND_RESOURCES.md)
- 📝 **Update frontend screens**: Read [frontend/README.md](frontend/README.md)
- 🔌 **Capture API screenshots**: Read [backend/README.md](backend/README.md)
- 🏗️ **Create architecture diagrams**: See [architecture/DIAGRAMS.md](architecture/DIAGRAMS.md)
- ⚙️ **Capture setup process**: Read [setup/README.md](setup/README.md)

---

```shell
docs/screenshots/
├── SCREENSHOTS_GUIDE.md          # This file - comprehensive guide
├── frontend/                     # Frontend UI screenshots
│   ├── main-tab.png             # Main upload and process interface
│   ├── file-history.png         # File history panel
│   ├── tools-tab.png            # Additional tools and utilities
│   ├── light-theme.png          # Light theme demonstration
│   ├── dark-theme.png           # Dark theme demonstration
│   └── responsive-mobile.png    # Mobile responsive design
├── backend/                      # Backend API screenshots
│   ├── swagger-ui.png           # Swagger API documentation
│   ├── api-endpoints.png        # API endpoint examples
│   └── error-responses.png      # Error response examples
├── architecture/                 # Architecture and diagrams
│   ├── system-architecture.png  # Overall system architecture
│   ├── data-flow.png            # Data flow diagram
│   ├── deployment.png           # Docker deployment setup
│   └── component-diagram.png    # Component relationships
├── setup/                        # Setup and installation
│   ├── docker-compose-up.png    # Docker Compose startup
│   ├── local-dev-setup.png      # Local development environment
│   └── build-success.png        # Successful build output
└── README.md                    # Index of all screenshots
```

---

## 🎯 Screenshot Categories

### Frontend UI Screenshots

#### Main Upload & Process Tab (`main-tab.png`)

- **Location**: Frontend main interface
- **Size**: 800x600px (minimum)
- **Shows**:
  - Job description input field
  - Resume upload/text area
  - Optimization type selection (Resume/Cover Letter)
  - Temperature and model selection
  - Process button
  - Output preview area
- **How to capture**:
  1. Start frontend (`npm run dev`)
  2. Fill in sample data
  3. Capture screenshot of main content area

#### File History Panel (`file-history.png`)

- **Location**: Right sidebar of main interface
- **Size**: 350x600px (minimum)
- **Shows**:
  - List of uploaded files
  - Generated document files
  - Download button for each file
  - Delete button for each file
  - File metadata (name, size, date)
- **How to capture**:
  1. Upload several files
  2. Generate some documents
  3. Capture screenshot of history panel

#### Additional Tools Tab (`tools-tab.png`)

- **Location**: Frontend tools section
- **Size**: 800x600px (minimum)
- **Shows**:
  - Markdown to PDF converter
  - File upload area for markdown
  - Convert button
  - Output preview
- **How to capture**:
  1. Navigate to "Additional Tools" tab
  2. Prepare sample markdown
  3. Capture screenshot of tools interface

#### Light Theme (`light-theme.png`)

- **Location**: Full application interface
- **Size**: 1200x800px (minimum)
- **Shows**:
  - Clean, professional light theme
  - All UI elements clearly visible
  - Light background with dark text
  - High contrast
- **How to capture**:
  1. Enable light theme (Settings icon)
  2. Capture full application window
  3. Ensure all text is readable

#### Dark Theme (`dark-theme.png`)

- **Location**: Full application interface
- **Size**: 1200x800px (minimum)
- **Shows**:
  - Modern dark theme
  - Comfortable for low-light viewing
  - Dark background with light text
  - Accent colors visible
- **How to capture**:
  1. Enable dark theme (Settings icon)
  2. Capture full application window
  3. Ensure all details are visible

---

### Backend API Screenshots

#### Swagger UI (`swagger-ui.png`)

- **Location**: `http://localhost:8080/swagger-ui/index.html`
- **Size**: 1200x800px (minimum)
- **Shows**:
  - OpenAPI/Swagger documentation
  - All API endpoints listed
  - Request/response examples
  - Try-it-out functionality
- **How to capture**:
  1. Start backend (`./gradlew bootRun`)
  2. Open Swagger UI in browser
  3. Capture full interface

#### API Endpoints (`api-endpoints.png`)

- **Location**: Swagger UI or Bruno
- **Size**: 1000x600px (minimum)
- **Shows**:
  - POST /upload endpoint
  - GET /files endpoint
  - DELETE /files/{filename} endpoint
  - Request parameters
  - Response examples
- **How to capture**:
  1. Open Swagger UI
  2. Expand each endpoint
  3. Capture showing multiple endpoints

#### Error Responses (`error-responses.png`)

- **Location**: Bruno or API testing tool
- **Size**: 1000x600px (minimum)
- **Shows**:
  - 400 Bad Request error
  - 404 Not Found error
  - 500 Internal Server Error
  - Error message formats
  - HTTP status codes
- **How to capture**:
  1. Use Bruno or curl
  2. Trigger various errors
  3. Capture response examples

---

### Architecture & Diagrams

#### System Architecture (`system-architecture.png`)

- **Location**: Generated from docs/architecture/ARCHITECTURE.md
- **Size**: 1200x900px (minimum)
- **Shows**:
  - Frontend (React + TypeScript)
  - Backend (Spring Boot)
  - LLM Service (Ollama/OpenAI)
  - File Storage
  - Database connections
- **How to create**:
  1. Use Mermaid or draw.io
  2. Create component diagram
  3. Export as PNG (600dpi)
  4. Save to this directory

#### Data Flow (`data-flow.png`)

- **Location**: Generated from architecture documentation
- **Size**: 1000x800px (minimum)
- **Shows**:
  - Request flow from frontend
  - Backend processing
  - LLM API calls
  - Response flow back to frontend
  - File storage operations
- **How to create**:
  1. Design flowchart
  2. Show each step in sequence
  3. Include decision points
  4. Export as PNG

#### Deployment Setup (`deployment.png`)

- **Location**: Docker Compose configuration
- **Size**: 1000x700px (minimum)
- **Shows**:
  - Docker containers
  - Port mappings (8080, 5173)
  - Volume mounts
  - Network configuration
  - Environment variables
- **How to create**:
  1. Diagram Docker Compose setup
  2. Show container relationships
  3. Include port/volume details
  4. Export as PNG

#### Component Diagram (`component-diagram.png`)

- **Location**: UML or draw.io diagram
- **Size**: 1200x900px (minimum)
- **Shows**:
  - ResumeController component
  - ApiService component
  - FileStorageService
  - LLM integration
  - Component dependencies
- **How to create**:
  1. Create UML component diagram
  2. Show all Java classes
  3. Show relationships
  4. Export as PNG

---

### Setup & Installation Screenshots

#### Docker Compose Up (`docker-compose-up.png`)

- **Location**: Terminal output
- **Size**: 1000x400px (minimum)
- **Shows**:
  - `docker-compose up` command execution
  - Container startup messages
  - Port binding confirmations
  - All services starting
- **How to capture**:
  1. Run `docker-compose up`
  2. Wait for all services to start
  3. Capture terminal showing success

#### Local Development Setup (`local-dev-setup.png`)

- **Location**: Local development environment
- **Size**: 1200x600px (minimum)
- **Shows**:
  - VS Code with project open
  - Terminal with development services running
  - Both frontend and backend running
  - IDE showing key files
- **How to capture**:
  1. Set up local development environment
  2. Start both frontend and backend
  3. Capture full screen showing setup

#### Build Success (`build-success.png`)

- **Location**: Gradle build output
- **Size**: 1000x500px (minimum)
- **Shows**:
  - `./gradlew build` successful completion
  - All checks passing (Checkstyle, Tests)
  - Build artifact created
  - No errors or warnings
- **How to capture**:
  1. Run `./gradlew clean build`
  2. Wait for completion
  3. Capture showing "BUILD SUCCESSFUL"

---

## 🔧 Tools for Creating Screenshots

### For UI Screenshots

- **Built-in**: Print Screen + Paint
- **Windows**: Snip & Sketch (Win+Shift+S)
- **Chrome DevTools**: Built-in screenshot tool
- **Recommended**: ShareX (free, powerful)

### For Diagrams & Architecture

- **Mermaid**: Diagram as code (markdown compatible)
- **draw.io**: Free online diagram tool
- **Lucidchart**: Professional diagrams
- **PlantUML**: UML diagrams as code

### For Image Optimization

- **TinyPNG**: Online PNG compression
- **ImageMagick**: Command-line image processing
- **GIMP**: Free image editor

### For Screen Recording (optional)

- **OBS Studio**: Free, open-source
- **ShareX**: Screenshots + screen recording
- **FFmpeg**: Command-line video processing

---

## 📋 Screenshot Requirements

### Image Format & Size

- **Format**: PNG (lossless, better for UI)
- **Minimum width**: 800px
- **Maximum width**: 1920px (for full-screen)
- **Resolution**: 72-96 DPI (web standard)
- **File size**: Target < 500KB per image

### Naming Convention

```shell
{category}-{component}-{description}.png

Examples:
- frontend-main-upload-interface.png
- backend-swagger-ui-full.png
- architecture-system-components.png
- setup-docker-compose-success.png
```

### Alt Text (for accessibility)

Every screenshot in documentation should have:

```markdown
![Descriptive alt text explaining what the image shows](path/to/image.png)
```

### Documentation Standards

- Include context before/after each screenshot
- Explain what users should see
- Highlight important UI elements
- Include arrows or callouts for key features
- Keep screenshots current with code changes

---

## 🔄 Updating Screenshots

### When to Update Screenshots

1. **UI Changes**: Any modification to components or layout
2. **Color Scheme Changes**: Theme updates
3. **New Features**: Screenshots should show new functionality
4. **Bug Fixes**: If visual bug was fixed
5. **Documentation Review**: At least quarterly
6. **Release**: Before each release cycle

### Update Process

1. Identify which screenshots are affected
2. Capture new screenshots following guidelines
3. Optimize images (compress, resize if needed)
4. Replace old images in `docs/screenshots/{category}/`
5. Update markdown with new filenames if changed
6. Verify links work in documentation
7. Test on mobile and desktop views
8. Commit changes with descriptive message

### Version Control

- Store screenshots in Git LFS if images are large
- Or use `.gitignore` and link to external storage
- Document image sources and creation date
- Keep a changelog of screenshot updates

---

## 📸 Using Screenshots in Documentation

### Markdown Syntax

```markdown
![Alt text describing the image](docs/screenshots/{category}/{filename}.png)

![Main Upload Interface showing job description and resume inputs](docs/screenshots/frontend/main-tab-dark.png)
```

### Best Practices

1. **Center images for emphasis**:

   ```markdown
   <center>

   ![Image description](path/to/image.png)

   </center>
   ```

2. **Add captions**:

   ```markdown
   ![Image](path)

   _Caption explaining what user sees in the image_
   ```

3. **Group related images**:

   ```markdown
   ### Theme Options

   #### Light Theme

   ![Light theme](path/light.png)

   #### Dark Theme

   ![Dark theme](path/dark.png)
   ```

4. **Link to full-size images**:

   ```markdown
   [![Thumbnail](path/thumbnail.png)](path/full-size.png)
   ```

---

## ✅ Screenshot Verification Checklist

Before including a screenshot in documentation:

- [ ] Image is clear and readable (no blur or distortion)
- [ ] All text is legible at normal viewing size
- [ ] Colors are accurate and consistent with theme
- [ ] No sensitive information (API keys, passwords, tokens)
- [ ] No personal information visible
- [ ] Image resolution is appropriate for use
- [ ] File size is optimized (< 500KB)
- [ ] Alt text is descriptive and helpful
- [ ] Caption explains what user should observe
- [ ] Screenshot shows current state of application
- [ ] No outdated version numbers or deprecated features
- [ ] Links in documentation point to correct image files
- [ ] Image displays correctly on mobile devices
- [ ] Image displays correctly on desktop browsers
- [ ] Keyboard shortcuts/commands are current
- [ ] Error messages are helpful and current

---

## 🎓 Example Screenshots in README

### Current Implementation

The README.md currently has these screenshot references:

1. **Main Content Tab** - Upload interface (placeholder)
2. **File History Panel** - File management (placeholder)
3. **Additional Tools Tab** - Markdown to PDF (placeholder)
4. **Light Theme** - Theme demonstration (placeholder)
5. **Dark Theme** - Theme demonstration (placeholder)
6. **Swagger UI** - API documentation (GitHub link)
7. **Spotlight UI** - Original interface (GitHub link)

### Next Steps

1. Replace placeholder images with real screenshots
2. Update location paths in README
3. Add new sections for architecture diagrams
4. Add setup/deployment screenshots
5. Create image optimization workflow

---

## 📞 Questions & Support

For questions about screenshots:

1. Check existing screenshots in `docs/screenshots/`
2. Review this guide for standards and conventions
3. Use tools listed in the "Tools for Creating Screenshots" section
4. Refer to "Screenshot Verification Checklist"
5. Test locally before committing changes

---

## Revision History

| Date       | Changes                                                      |
| ---------- | ------------------------------------------------------------ |
| 2026-01-16 | Initial guide created with directory structure and standards |

---

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
