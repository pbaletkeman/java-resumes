# Quick Start Reference - Java-Resumes

- [Quick Start Reference - Java-Resumes](#quick-start-reference---java-resumes)
  - [One-Minute Overview](#one-minute-overview)
  - [For Non-Technical Users](#for-non-technical-users)
    - [I just want to upload and optimize documents](#i-just-want-to-upload-and-optimize-documents)
    - [I want to add a new AI model](#i-want-to-add-a-new-ai-model)
    - [I want to share my model settings with the team](#i-want-to-share-my-model-settings-with-the-team)
    - [Something went wrong, I want to reset](#something-went-wrong-i-want-to-reset)
  - [For Technical Users / Developers](#for-technical-users--developers)
    - [Build and Run Backend](#build-and-run-backend)
    - [Build and Run Frontend](#build-and-run-frontend)
    - [Key Endpoints](#key-endpoints)
    - [Architecture](#architecture)
  - [Project Structure](#project-structure)
  - [Configuration](#configuration)
    - [Backend Configuration](#backend-configuration)
    - [Environment Variables](#environment-variables)
    - [Frontend Environment](#frontend-environment)
  - [Testing](#testing)
    - [Backend Tests](#backend-tests)
    - [Frontend Tests](#frontend-tests)
    - [Code Quality](#code-quality)
  - [Deployment](#deployment)
    - [Development](#development)
    - [Production](#production)
  - [Documentation](#documentation)
  - [Common Questions](#common-questions)
    - [Q: How do I change the AI model?](#q-how-do-i-change-the-ai-model)
    - [Q: Can I use my own prompts?](#q-can-i-use-my-own-prompts)
    - [Q: Where are my models stored?](#q-where-are-my-models-stored)
    - [Q: Can I backup my models?](#q-can-i-backup-my-models)
    - [Q: What if I break something?](#q-what-if-i-break-something)
    - [Q: How many models can I add?](#q-how-many-models-can-i-add)
    - [Q: Can I share models with team?](#q-can-i-share-models-with-team)
    - [Q: Do I need an account?](#q-do-i-need-an-account)
  - [Troubleshooting](#troubleshooting)
    - [Backend won't start](#backend-wont-start)
    - [Frontend won't build](#frontend-wont-build)
    - [Models not saving](#models-not-saving)
    - [API not responding](#api-not-responding)
  - [Performance](#performance)
  - [Security Notes](#security-notes)
  - [Tech Stack](#tech-stack)
  - [Need Help?](#need-help)
  - [Key Files to Know](#key-files-to-know)
  - [Quick Commands](#quick-commands)
  - [You're Ready!](#youre-ready)

---

> ** Location:** `docs/QUICK_START_ADVANCED.md`
> ** Audience:** Developers
> ** Related:** [Quick Reference](QUICK_REFERENCE.md) | [Technical Checklist](TECHNICAL_CHECKLIST.md) | [Index](INDEX.md)

---

## ⏱️ One-Minute Overview

**What is this?**

- Application to optimize job application materials (resume, cover letter, technical skills)
- Uses AI models to process documents
- Non-technical users can manage AI models without coding

**What can I do?**

- Optimize resumes
- Generate cover letters
- Extract skills
- Manage AI models (Settings tab)
- Export/import model configurations

---

## 🚶 For Non-Technical Users

### I just want to upload and optimize documents

1. Open the app
2. Go to **"Main Content"** tab
3. Upload file
4. Select what you want (Resume/Cover/Skills)
5. Click **"Process"**
6. Get results

### I want to add a new AI model

1. Go to **"Settings"** tab
2. Click **"Add Model"**
3. Enter model name and ID
4. Click **"Add"**
5. Now available for all uploads

### I want to share my model settings with the team

1. Go to **"Settings"** tab
2. Click **"Export Models"**
3. Send JSON file to team
4. Team goes to Settings Import Models
5. Everyone has same settings

### Something went wrong, I want to reset

1. Go to **"Settings"** tab
2. Click **"Reset to Defaults"**
3. Everything back to original

---

## 💻 For Technical Users / Developers

### Build and Run Backend

```bash
cd c:\Users\Pete\Desktop\java-resumes
./gradlew bootRun                    # Starts on localhost:8080
./gradlew test                       # Run tests (57/57 passing)
./gradlew checkstyleMain             # Check code quality
```

### Build and Run Frontend

```bash
cd c:\Users\Pete\Desktop\java-resumes\frontend
npm install                          # Install dependencies
npm run dev                          # Starts on localhost:5173
npm run build                        # Production build
npm test                             # Run tests
```

### Key Endpoints

```
POST /upload                         # Process document
POST /upload/skills                  # Extract skills
GET  /health                         # Health check
```

### Architecture

```
Frontend (React + TypeScript)

Backend (Spring Boot + Java 21)

LLM API (Configurable models)

Result (Optimized document)
```

---

## 📂 Project Structure

```
backend/src/main/java/ca/letkeman/resumes/
 controller/ResumeController.java        API endpoints
 service/ApiService.java                 LLM integration
 service/PromptService.java              Prompt loading

frontend/src/
 components/Forms/DocumentUploadForm.tsx     Main upload
 components/Settings/ModelSettings.tsx       Model management
 components/Tabs/SettingsTab.tsx            Settings UI
 pages/HomePage.tsx                         Main page

prompts/
 RESUME.md                                  Resume optimization
 COVER.md                                   Cover letter
 SKILLS.md                                  Skills extraction
```

---

## ⚙️ Configuration

### Backend Configuration

File: `src/main/resources/application.yml`

```yaml
spring:
  application:
    name: rest-service
  prompts:
    directory: ${PROMPTS_DIR} # Optional external prompts
```

### Environment Variables

```
PROMPTS_DIR=/path/to/prompts    # Optional: external prompt directory
JAVA_OPTS=-Xmx1G               # JVM memory
```

### Frontend Environment

File: `frontend/.env` (optional)

```
VITE_API_URL=http://localhost:8080
VITE_APP_NAME=Java-Resumes
```

---

## 🧪 Testing

### Backend Tests

```bash
./gradlew test                   # All 57 tests
./gradlew testResumeController   # Specific test class
```

### Frontend Tests

```bash
npm test                         # All tests
npm run test:coverage           # With coverage report
npm run test:ui                 # Interactive mode
```

### Code Quality

```bash
./gradlew checkstyleMain        # Check Java style
npm run lint                    # Check TypeScript
npm run type-check              # TypeScript strict
```

---

## 🚀 Deployment

### Development

```bash
# Terminal 1 - Backend
./gradlew bootRun               # localhost:8080

# Terminal 2 - Frontend
cd frontend && npm run dev      # localhost:5173
```

### Production

```bash
# Build everything
./gradlew build                 # Backend JAR
cd frontend && npm run build    # Frontend dist

# Deploy
java -jar build/libs/*.jar      # Backend on 8080
npm run preview                 # Frontend preview
```

---

## 📚 Documentation

| Document                        | Purpose                  |
| ------------------------------- | ------------------------ |
| **README.md**                   | Main project overview    |
| **PROJECT_STATUS.md**           | Complete status report   |
| **NONTECHNICAL_MODEL_GUIDE.md** | User guide (no coding)   |
| **IMPLEMENTATION_SUMMARY.md**   | Technical implementation |
| **TECHNICAL_CHECKLIST.md**      | Development checklist    |

---

## ❓ Common Questions

### Q: How do I change the AI model?

**A:** Go to Settings tab Add Model or select from dropdown

### Q: Can I use my own prompts?

**A:** Yes, set `PROMPTS_DIR` environment variable

### Q: Where are my models stored?

**A:** Browser localStorage (survives refresh)

### Q: Can I backup my models?

**A:** Yes, Settings Export Models JSON file

### Q: What if I break something?

**A:** Go to Settings Reset to Defaults

### Q: How many models can I add?

**A:** Unlimited, stored in localStorage (~5-10MB max)

### Q: Can I share models with team?

**A:** Yes, Export send JSON they Import

### Q: Do I need an account?

**A:** No, everything runs in browser/local

---

## 🐛 Troubleshooting

### Backend won't start

```bash
# Check Java version
java -version                   # Should be 21+

# Check port 8080
netstat -ano | find ":8080"    # Kill process if using port

# Clear Gradle cache
./gradlew clean && ./gradlew build
```

### Frontend won't build

```bash
# Clear node_modules
rm -r node_modules package-lock.json
npm install
npm run build
```

### Models not saving

```bash
# Check localStorage is enabled
Press F12  Application  Local Storage

# Try resetting
Go to Settings  Reset to Defaults
```

### API not responding

```bash
# Check backend is running
curl http://localhost:8080/health

# Check frontend API URL
Check VITE_API_URL environment
```

---

## ⚡ Performance

| Operation               | Time   |
| ----------------------- | ------ |
| Resume optimization     | 5-30s  |
| Cover letter generation | 10-45s |
| Skills extraction       | 3-15s  |
| Model operations        | <100ms |

**Times depend on LLM service, not application**

---

## 🔐 Security Notes

- Input validation on all uploads
- File type checking
- No stored passwords or credentials
- localStorage only (no external transmission)
- CORS configured for security
- Error messages don't expose internals

---

## 🛠️ Tech Stack

**Backend:**

- Java 21
- Spring Boot 6.2.8
- Gradle 8.7
- JUnit 5 / Mockito

**Frontend:**

- React 18
- TypeScript 5.9.3
- Vite 7.3.1
- PrimeReact 10.1.0
- TailwindCSS 3.4.1
- Vitest / React Testing Library

**Infrastructure:**

- Cross-platform (Windows/Mac/Linux)
- Browser localStorage
- No database required
- No external dependencies for models

---

## 💬 Need Help?

1. **User Issue?** Check NONTECHNICAL_MODEL_GUIDE.md
2. **Technical Issue?** Check IMPLEMENTATION_SUMMARY.md
3. **Development?** Check code comments and JSDoc
4. **Status?** Check PROJECT_STATUS.md

---

## 📋 Key Files to Know

```
README.md                            Main overview
PROJECT_STATUS.md                    Current status
NONTECHNICAL_MODEL_GUIDE.md          User guide
IMPLEMENTATION_SUMMARY.md            Tech details
TECHNICAL_CHECKLIST.md               Dev checklist

frontend/src/pages/HomePage.tsx      Main UI
frontend/src/components/
  /Settings/ModelSettings.tsx        Model management
  /Forms/DocumentUploadForm.tsx      Upload form

src/main/java/.../controller/
  ResumeController.java              API endpoints
src/main/java/.../service/
  ApiService.java                    LLM integration
  PromptService.java                 Prompt loading

prompts/
  RESUME.md                          Resume prompt
  COVER.md                           Cover letter
  SKILLS.md                          Skills extraction
```

---

## ⚡ Quick Commands

```bash
# Start development
./gradlew bootRun & cd frontend && npm run dev

# Run all tests
./gradlew test && cd frontend && npm test

# Build for production
./gradlew build && cd frontend && npm run build

# Check code quality
./gradlew checkstyleMain && cd frontend && npm run lint

# Clean everything
./gradlew clean && cd frontend && rm -r node_modules dist

# Format code
./gradlew spotlessApply   # Backend
npm run lint:fix          # Frontend
```

---

## ✨ You're Ready!

- Everything is built and working
- All tests passing
- Documentation complete
- Ready for production
- Non-technical users supported

**Start using it now!**

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
