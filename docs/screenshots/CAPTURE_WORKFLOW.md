# Screenshot Capture Workflow

Complete step-by-step instructions for capturing, verifying, and documenting all 16 required screenshots.

## 📊 Capture Timeline

| Phase       | Task                      | Est. Time  | Status       |
| ----------- | ------------------------- | ---------- | ------------ |
| **Phase 1** | Frontend Startup          | 5 min      | ⏳ Ready     |
| **Phase 1** | Frontend Screenshots (6)  | 15 min     | ⏳ Ready     |
| **Phase 2** | Backend Startup           | 5 min      | ⏳ Ready     |
| **Phase 2** | Backend Screenshots (3)   | 10 min     | ⏳ Ready     |
| **Phase 3** | Architecture Diagrams (4) | 20 min     | ⏳ Ready     |
| **Phase 4** | Setup Screenshots (3)     | 15 min     | ⏳ Ready     |
| **Phase 5** | Verification & Links      | 15 min     | ⏳ Ready     |
| **TOTAL**   | All Screenshots           | **85 min** | **⏳ Ready** |

---

## ✅ Pre-Capture Checklist

Before starting, verify your environment:

### System Requirements

- [ ] Windows 11 with screen resolution 1920x1080 or higher
- [ ] Java 17 LTS installed: `java -version`
- [ ] Gradle 8.7 working: `./gradlew --version`
- [ ] Node.js 18+ installed: `node --version`
- [ ] npm 9+ installed: `npm --version`
- [ ] Docker installed: `docker --version`
- [ ] Docker Compose installed: `docker-compose --version`

### Tools Installed

- [ ] Screenshot tool (ShareX recommended for region capture)
- [ ] Image editor (Paint, Photoshop, or online tool)
- [ ] File compression tool (TinyPNG or similar)

### Project Ready

- [ ] Repository cloned: `c:\Users\Pete\Desktop\java-resumes`
- [ ] Git branch: `main` or `master`
- [ ] No uncommitted changes
- [ ] `.gitignore` ignores screenshots directory

### Directory Structure

```bash
# Verify directories exist
docs/screenshots/
├── frontend/          ✓ Created
├── backend/           ✓ Created
├── architecture/      ✓ Created
└── setup/             ✓ Created
```

---

## 🎬 Phase 1: Frontend Screenshots (6 images)

### Prerequisites

```bash
# Navigate to project
cd c:\Users\Pete\Desktop\java-resumes

# Start backend (in Terminal 1)
./gradlew bootRun
# Wait for: "Tomcat started on port(s): 8080"

# Start frontend (in Terminal 2)
cd frontend
npm install  # Only if not done
npm run dev
# Wait for: "VITE v7.2.4 ready in XXX ms"
```

### Backend Startup Checklist

- [ ] Spring Boot loaded
- [ ] Tomcat listening on port 8080
- [ ] No errors in console
- [ ] Server ready for requests

### Frontend Startup Checklist

- [ ] Vite development server running
- [ ] Port 5173 accessible
- [ ] Application loads at <http://localhost:5173>
- [ ] UI renders without errors

### Screenshot 1: Main Upload Tab

**File:** `docs/screenshots/frontend/main-tab.png`

**Steps:**

1. Open browser: <http://localhost:5173>
2. Verify Main tab is active
3. Resize window to 800x600px
4. Ensure full form visible:
   - Resume file selector
   - Job description text area
   - File history list
   - Optimization settings
5. Take screenshot: 800x600px
6. Save with naming: `main-tab.png`

**Verification:**

- [ ] All form elements visible
- [ ] No scroll bars needed
- [ ] Text readable
- [ ] Buttons clickable regions visible
- [ ] File size < 500KB

### Screenshot 2: File History Panel

**File:** `docs/screenshots/frontend/file-history.png`

**Steps:**

1. In main tab, upload a dummy file (or look for test files)
2. Right panel shows file history
3. Resize to 350x600px width (sidebar only)
4. Capture file history entries:
   - File names
   - Upload timestamps
   - Download/delete buttons
5. Take screenshot: 350x600px
6. Save as: `file-history.png`

**Verification:**

- [ ] File list visible
- [ ] Timestamps readable
- [ ] Action buttons visible
- [ ] Scrollable if many files
- [ ] File size < 300KB

### Screenshot 3: Additional Tools Tab

**File:** `docs/screenshots/frontend/tools-tab.png`

**Steps:**

1. Click on "Tools" tab
2. Show tools available:
   - File management
   - Settings
   - Help/Documentation
3. Resize to 800x600px
4. Capture full tools interface
5. Take screenshot: 800x600px
6. Save as: `tools-tab.png`

**Verification:**

- [ ] Tools tab highlighted
- [ ] All tool options visible
- [ ] Clear descriptions
- [ ] Icons display correctly
- [ ] File size < 500KB

### Screenshot 4: Light Theme

**File:** `docs/screenshots/frontend/light-theme.png`

**Steps:**

1. Ensure light theme is active
2. Verify UI shows light colors:
   - Light background
   - Dark text
   - Light panels
3. Maximize window (1200x800px)
4. Show complete interface
5. Take screenshot: 1200x800px
6. Save as: `light-theme.png`

**Verification:**

- [ ] Light theme colors visible
- [ ] No dark mode applied
- [ ] All UI elements visible
- [ ] Professional appearance
- [ ] File size < 500KB

### Screenshot 5: Dark Theme

**File:** `docs/screenshots/frontend/dark-theme.png`

**Steps:**

1. Toggle to dark theme (theme selector)
2. Verify UI shows dark colors:
   - Dark background
   - Light text
   - Dark panels
3. Maximize window (1200x800px)
4. Show complete interface
5. Take screenshot: 1200x800px
6. Save as: `dark-theme.png`

**Verification:**

- [ ] Dark theme colors visible
- [ ] Text readable on dark background
- [ ] All UI elements visible
- [ ] Professional appearance
- [ ] File size < 500KB

### Screenshot 6: Mobile Responsive

**File:** `docs/screenshots/frontend/responsive-mobile.png`

**Steps:**

1. Open browser DevTools: F12
2. Toggle device toolbar: Ctrl+Shift+M
3. Select device: iPhone 12 Pro (390x844px)
4. Or manual resize to 375x812px
5. Scroll to show:
   - Header
   - Form elements
   - File list
6. Take screenshot: 375x812px
7. Save as: `responsive-mobile.png`

**Verification:**

- [ ] Mobile viewport active
- [ ] UI responsive and usable
- [ ] Touch targets appropriately sized
- [ ] Text readable on small screen
- [ ] File size < 300KB

---

## 🔧 Phase 2: Backend API Screenshots (3 images)

### Prerequisites - Phase 2

```bash
# Ensure backend is running
# From Phase 1, backend should still be running on port 8080
```

### Backend Running Checklist

- [ ] Spring Boot application running
- [ ] Port 8080 accessible
- [ ] Swagger UI available
- [ ] API endpoints responding

### Screenshot 1: Swagger UI

**File:** `docs/screenshots/backend/swagger-ui.png`

**Steps:**

1. Open browser: <http://localhost:8080/swagger-ui/index.html>
2. Swagger interface loads
3. Resize window to 1200x800px
4. Capture interface showing:
   - Swagger branding
   - API endpoints list
   - Try-it-out feature visible
5. Take screenshot: 1200x800px
6. Save as: `swagger-ui.png`

**Verification:**

- [ ] Swagger UI fully loaded
- [ ] Endpoints listed (/upload, /files, etc.)
- [ ] Expandable sections visible
- [ ] Professional appearance
- [ ] File size < 500KB

### Screenshot 2: API Endpoints

**File:** `docs/screenshots/backend/api-endpoints.png`

**Steps:**

1. Open Postman or similar tool
2. Show API endpoints:
   - POST /upload
   - GET /files
   - GET /files/{filename}
   - DELETE /files/{filename}
3. Resize to 1000x600px
4. Capture request/response format
5. Take screenshot: 1000x600px
6. Save as: `api-endpoints.png`

**Verification:**

- [ ] Endpoint methods clear
- [ ] Request format visible
- [ ] Response format visible
- [ ] HTTP status codes shown
- [ ] File size < 400KB

### Screenshot 3: Error Responses

**File:** `docs/screenshots/backend/error-responses.png`

**Steps:**

1. Trigger error scenarios:
   - Missing required field (400)
   - File not found (404)
   - Server error (500)
2. Capture error response in Postman:
   - HTTP status code
   - Error message
   - Response body
3. Resize to 1000x600px
4. Capture multiple error types
5. Take screenshot: 1000x600px
6. Save as: `error-responses.png`

**Verification:**

- [ ] Error codes visible
- [ ] Error messages clear
- [ ] Response format consistent
- [ ] HTTP headers shown
- [ ] File size < 400KB

---

## 🏗️ Phase 3: Architecture Diagrams (4 images)

### Tools Required

- Mermaid, draw.io, PlantUML, or Visio

### Screenshot 1: System Architecture

**File:** `docs/screenshots/architecture/system-architecture.png`

**Using draw.io or Mermaid:**

1. Create diagram showing:
   - Frontend (React)
   - Backend (Spring Boot)
   - Database (File storage)
   - LLM Service (Ollama)
2. Show connections/arrows
3. Include component labels
4. Export as PNG: 1200x800px
5. Save as: `system-architecture.png`

**Diagram Should Show:**

```plaintext
┌─────────────────┐
│  React Frontend │
└────────┬────────┘
         │ HTTP
         ▼
┌─────────────────┐      ┌──────────────┐
│  Spring Boot    │◄────►│   Ollama     │
│   REST API      │      │   LLM API    │
└────────┬────────┘      └──────────────┘
         │
         ▼
┌─────────────────┐
│  File Storage   │
└─────────────────┘
```

**Verification:**

- [ ] All components present
- [ ] Connections clear
- [ ] Labels readable
- [ ] Professional appearance
- [ ] File size < 500KB

### Screenshot 2: Data Flow Diagram

**File:** `docs/screenshots/architecture/data-flow.png`

**Steps:**

1. Create flow showing:
   - User uploads resume
   - Backend processes
   - Sends to LLM
   - Returns optimized content
   - User downloads
2. Show step numbers
3. Include data transformations
4. Export as PNG: 1200x800px
5. Save as: `data-flow.png`

**Diagram Should Show:**

```plainttext
User Input
   ↓
Resume Upload
   ↓
Validation
   ↓
LLM Processing
   ↓
Response Parsing
   ↓
PDF Generation
   ↓
Download Ready
```

**Verification:**

- [ ] Flow direction clear
- [ ] Steps numbered
- [ ] Data transformations shown
- [ ] Professional appearance
- [ ] File size < 500KB

### Screenshot 3: Deployment Architecture

**File:** `docs/screenshots/architecture/deployment.png`

**Steps:**

1. Create diagram showing Docker setup:
   - Docker Compose network
   - Frontend container
   - Backend container
   - Volume mounts
   - Port mappings
2. Show container relationships
3. Export as PNG: 1200x800px
4. Save as: `deployment.png`

**Diagram Should Show:**

```shell
docker-compose
├─ Frontend Container
│  ├─ Nginx (port 80)
│  └─ React App
├─ Backend Container
│  ├─ Spring Boot (port 8080)
│  ├─ Gradle
│  └─ Java 17
└─ Volumes
   └─ ./uploads:/app/uploads
```

**Verification:**

- [ ] Container structure clear
- [ ] Port mappings labeled
- [ ] Volume mounts shown
- [ ] Professional appearance
- [ ] File size < 500KB

### Screenshot 4: Component Diagram

**File:** `docs/screenshots/architecture/component-diagram.png`

**Steps:**

1. Create diagram showing:
   - Frontend components
   - Backend services
   - Controller layers
   - Model classes
2. Show dependencies
3. Include communication paths
4. Export as PNG: 1200x800px
5. Save as: `component-diagram.png`

**Diagram Should Show:**

```shell
Frontend
├─ App.tsx
├─ MainContentTab.tsx
└─ hooks/
   ├─ useApi.ts
   └─ useTheme.ts

Backend
├─ ResumeController
├─ ApiService
├─ FilesStorageService
└─ Model Classes
```

**Verification:**

- [ ] Component hierarchy clear
- [ ] Dependencies shown
- [ ] Interfaces labeled
- [ ] Professional appearance
- [ ] File size < 500KB

---

## 📦 Phase 4: Setup & Deployment Screenshots (3 images)

### See: [setup/README.md](setup/README.md)

Complete instructions for:

1. **docker-compose-up.png** - Container startup
2. **local-dev-setup.png** - Local development
3. **build-success.png** - Build completion

---

## ✅ Phase 5: Verification & Finalization

### Screenshot Verification Checklist

For each screenshot, verify:

#### Technical Requirements

- [ ] Format: PNG
- [ ] File size: < 500KB (< 300KB preferred)
- [ ] Minimum width: 800px
- [ ] Maximum width: 1200px
- [ ] Resolution: 72-96 DPI
- [ ] Color mode: RGB
- [ ] No corruption or artifacts

#### Content Requirements

- [ ] Relevant content clearly visible
- [ ] No personal/sensitive information
- [ ] No debug console output (unless intentional)
- [ ] Clean, professional appearance
- [ ] Proper naming convention used
- [ ] Saved to correct directory

#### Visual Quality

- [ ] Sharp and clear (no blur)
- [ ] Properly centered
- [ ] Good contrast
- [ ] Professional color scheme
- [ ] No watermarks (unless approved)
- [ ] Consistent styling across screenshots

### File Organization Verification

```bash
# Verify all screenshots present
ls -la docs/screenshots/frontend/
# Should show 6 PNG files

ls -la docs/screenshots/backend/
# Should show 3 PNG files

ls -la docs/screenshots/architecture/
# Should show 4 PNG files

ls -la docs/screenshots/setup/
# Should show 3 PNG files
```

### Documentation Link Verification

Verify all references in:

- [ ] `README.md` - Screenshot section links working
- [ ] `docs/README.md` - References to screenshot docs
- [ ] `frontend/README.md` - Frontend guides
- [ ] `backend/README.md` - Backend guides
- [ ] `architecture/DIAGRAMS.md` - Architecture references

### Final Checklist

- [ ] All 16 screenshots captured
- [ ] All files properly named
- [ ] All files in correct directories
- [ ] File sizes optimized
- [ ] Documentation updated with links
- [ ] No dead links in markdown
- [ ] Screenshots display correctly in markdown
- [ ] All verification checklists passed
- [ ] Changes committed to git

---

## 🔄 Update Schedule

Screenshots should be updated:

### Quarterly Review

- [ ] Visual appearance (UI changes, theme updates)
- [ ] Feature additions
- [ ] Documentation accuracy

### On Major Release

- [ ] Version update in screenshots
- [ ] New features showcase
- [ ] API changes

### When Requested

- [ ] Bug fixes visibility
- [ ] Workflow improvements
- [ ] New deployment process

---

## 📚 References

- [SCREENSHOTS_GUIDE.md](../SCREENSHOTS_GUIDE.md) - Comprehensive guide
- [frontend/README.md](../frontend/README.md) - Frontend capture guide
- [backend/README.md](../backend/README.md) - Backend capture guide
- [architecture/DIAGRAMS.md](../architecture/DIAGRAMS.md) - Architecture guide
- [setup/README.md](../setup/README.md) - Setup capture guide

---

Last Updated: 2026-01-16
