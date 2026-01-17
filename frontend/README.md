# Resume Optimizer Frontend

[![React](https://img.shields.io/badge/React-19.2.0-blue.svg)](https://reactjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.9.3-blue.svg)](https://www.typescriptlang.org/)
[![Vite](https://img.shields.io/badge/Vite-7.2.4-646CFF.svg)](https://vitejs.dev/)
[![PrimeReact](https://img.shields.io/badge/PrimeReact-10.9.7-007BFF.svg)](https://primereact.org/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind-4.1.18-38B2AC.svg)](https://tailwindcss.com/)

A modern, responsive React application built with PrimeReact components for AI-powered resume optimization and document processing. Features a tab-based interface, file management, theme switching, and seamless backend integration.

---

## 📋 Table of Contents

- [Overview](#-overview)
- [🚀 Quick Start in 5 Steps](#-quick-start-in-5-steps)
- [Technology Stack](#%EF%B8%8F-technology-stack)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Getting Started Without Docker](#-getting-started-without-docker)
- [Development](#-development)
- [Available Scripts](#-available-scripts)
- [Building for Production](#-building-for-production)
- [Testing](#-testing)
- [Docker](#-docker)
- [Environment Variables](#-environment-variables)
- [Project Structure](#-project-structure)
- [Features](#-features)
- [Theme Customization](#-theme-customization)
- [API Integration](#-api-integration)
- [Component Architecture](#-component-architecture)
- [State Management](#-state-management)
- [Troubleshooting](#-troubleshooting)
- [Code Style](#-code-style)
- [Contributing](#-contributing)

---

## 🌟 Overview

The Resume Optimizer Frontend is a production-ready React application that provides an intuitive user interface for resume and cover letter optimization. Built with modern web technologies, it offers a seamless experience across devices with responsive design, accessibility features, and comprehensive error handling.

### Key Features

- 📱 **Responsive Design**: Works flawlessly on mobile (320px) to desktop (1920px+)
- 🎨 **Theme Support**: Light and dark themes with persistent user preferences
- 📂 **File Management**: Upload, download, and delete documents with ease
- 🔄 **Real-time Processing**: Instant feedback with loading states and progress indicators
- ✅ **Form Validation**: Client-side validation with user-friendly error messages
- 🔔 **Toast Notifications**: Non-intrusive feedback for all user actions
- ♿ **Accessibility**: WCAG 2.1 AA compliant with keyboard navigation
- 🧪 **Tested**: Comprehensive test coverage with Vitest and React Testing Library

---

## � Quick Start in 5 Steps

Get the frontend running in just 5 minutes! Follow these quick steps to start developing:

### Prerequisites

- Node.js 24 LTS installed
- npm 10+ installed
- Backend running (http://localhost:8080)

### Steps

#### Step 1: Clone and navigate to frontend

```bash
git clone https://github.com/pbaletkeman/java-resumes.git
cd java-resumes/frontend
```

#### Step 2: Install dependencies

```bash
npm install
```

#### Step 3: Configure API endpoint (optional, if backend not on localhost:8080)

```bash
cp .env.example .env
# Edit .env if needed to match your backend URL
```

#### Step 4: Start development server

```bash
npm run dev
```

#### Step 5: Open in browser

```plaintext
http://localhost:5173
```

### Access the Application

- **Frontend**: http://localhost:5173 (or port shown in terminal)
- **Backend API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html

That's it! You now have a hot-reloading development environment. Any code changes will automatically refresh in your browser.

---

## ���️ Technology Stack

### Core Technologies

| Technology     | Version | Purpose                            |
| -------------- | ------- | ---------------------------------- |
| **React**      | 19.2.0  | UI library and component framework |
| **TypeScript** | 5.9.3   | Type-safe JavaScript superset      |
| **Vite**       | 7.2.4   | Fast build tool and dev server     |

### UI and Styling

| Technology       | Version | Purpose                           |
| ---------------- | ------- | --------------------------------- |
| **PrimeReact**   | 10.9.7  | Professional UI component library |
| **PrimeIcons**   | 7.0.0   | Icon library                      |
| **Tailwind CSS** | 4.1.18  | Utility-first CSS framework       |
| **PostCSS**      | 8.5.6   | CSS processing                    |
| **Autoprefixer** | 10.4.23 | CSS vendor prefixing              |

### HTTP and State Management

| Technology        | Version | Purpose                   |
| ----------------- | ------- | ------------------------- |
| **Axios**         | 1.13.2  | Promise-based HTTP client |
| **React Context** | 19.2.0  | Global state management   |

### Testing

| Technology                      | Version | Purpose                     |
| ------------------------------- | ------- | --------------------------- |
| **Vitest**                      | 4.0.17  | Unit testing framework      |
| **React Testing Library**       | 16.3.1  | React component testing     |
| **@testing-library/jest-dom**   | 6.9.1   | Custom DOM matchers         |
| **@testing-library/user-event** | 14.6.1  | User interaction simulation |
| **jsdom**                       | 27.4.0  | DOM implementation          |

### Development Tools

| Technology            | Version | Purpose                           |
| --------------------- | ------- | --------------------------------- |
| **ESLint**            | 9.39.1  | JavaScript/TypeScript linting     |
| **Prettier**          | latest  | Code formatting                   |
| **TypeScript ESLint** | 8.46.4  | TypeScript-specific linting rules |

### Production Server

| Technology | Version | Purpose               |
| ---------- | ------- | --------------------- |
| **Nginx**  | alpine  | Production web server |

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Node.js**: Version 24 LTS or higher ([Download Node.js](https://nodejs.org/))
- **npm**: Version 10.x or higher (comes with Node.js)
- **Backend Service**: Java backend must be running on port 8080
- **Docker** (optional): For containerized deployment

### Verify Installation

```bash
node --version  # Should be v24.x or higher
npm --version   # Should be 10.x or higher
```

---

## 💻 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/pbaletkeman/java-resumes.git
cd java-resumes/frontend
```

### 2. Install Dependencies

```bash
npm install
```

This will install all required packages listed in `package.json`.

### 3. Configure Environment Variables

```bash
cp .env.example .env
```

Edit `.env` file:

```env
# API Configuration
VITE_API_BASE_URL=http://localhost:8080
```

### 4. Verify Installation

```bash
npm run dev
```

Open http://localhost:3000 in your browser. You should see the application running.

---

## 🚀 Getting Started Without Docker

If you prefer to run the frontend without Docker, follow these native setup instructions.

### Prerequisites

- **Node.js 24 LTS** installed

  ```bash
  node --version  # Verify you see Node 24.x
  ```

- **npm 10.x+** (comes with Node.js)

  ```bash
  npm --version
  ```

- **Java backend** running (see BACKEND_README.md)

  ```bash
  curl http://localhost:8080/api/health
  ```

- **Git** for version control
- **IDE or Editor** (VS Code, WebStorm, etc.)

### Step 1: Clone the Repository

```bash
git clone https://github.com/pbaletkeman/java-resumes.git
cd java-resumes/frontend
```

### Step 2: Install Node Dependencies

```bash
# Install all dependencies
npm install

# Verify installation
npm list react  # Should show React 19.2.0
```

**Troubleshooting installation issues**:

```bash
# Clear npm cache
npm cache clean --force

# Remove node_modules and reinstall
rm -rf node_modules package-lock.json
npm install

# Use npm ci for exact versions (CI environments)
npm ci
```

### Step 3: Configure Environment Variables

Copy the example environment file:

```bash
cp .env.example .env
```

Edit `.env` to match your setup:

```env
# Backend API endpoint (adjust if backend is on different host/port)
VITE_API_BASE_URL=http://localhost:8080

# Optional: Custom configuration
# VITE_APP_TITLE=Resume Optimizer
# VITE_MAX_FILE_SIZE=500000
```

**Common configurations**:

```env
# Local development (backend on same machine)
VITE_API_BASE_URL=http://localhost:8080

# Remote backend development
VITE_API_BASE_URL=http://192.168.1.100:8080

# Production deployment
VITE_API_BASE_URL=https://api.example.com
```

### Step 4: Start Development Server

```bash
# Start the dev server with hot reload
npm run dev
```

You should see:

```shell
  VITE v7.2.4  dev server running at:
  ➜  Local:   http://localhost:5173/
  ➜  press h to show help
```

### Step 5: Open in Browser

Navigate to one of the URLs shown in the terminal:

- Local: http://localhost:5173 (or http://127.0.0.1:5173)
- Accessible from network: http://your-ip:5173

### Verify Setup

1. **Check Frontend Loads**:
   - Page should display without errors
   - See the main content area and file history panel
   - Light theme should be active by default

2. **Test Backend Connection**:
   - Try uploading a test document
   - Should see "loading" indicator
   - Should either process successfully or show error

3. **Test Theme Toggle**:
   - Click theme toggle button (top right)
   - Should switch between light and dark themes
   - Theme should persist across page reloads

### Development Setup

#### Configure IDE/Editor

**VS Code** (recommended):

1. Install extensions:
   - "ES7+ React/Redux/React-Native snippets"
   - "ESLint"
   - "Prettier - Code formatter"
   - "Tailwind CSS IntelliSense"
   - "Thunder Client" (for API testing)

2. Create `.vscode/settings.json`:

   ```json
   {
     "editor.defaultFormatter": "esbenp.prettier-vscode",
     "editor.formatOnSave": true,
     "editor.codeActionsOnSave": {
       "source.fixAll.eslint": true
     }
   }
   ```

**WebStorm/IntelliJ**:

1. Open project root
2. Enable ESLint and Prettier
3. Set Node.js interpreter to 24 LTS
4. Configure Vite as build tool

#### Enable Hot Reload Features

The development server includes:

- **Hot Module Replacement (HMR)**: Instant component updates
- **Preserve Component State**: State survives hot reloads
- **Fast Refresh**: Automatically handles React component updates
- **CSS Hot Reload**: Styling updates without refresh

Just save your files and see changes instantly in the browser!

#### Development Workflow

1. **Start Backend** (in separate terminal):

   ```bash
   cd ../
   ./gradlew bootRun
   ```

2. **Start Frontend**:

   ```bash
   npm run dev
   ```

3. **Make Code Changes**:
   - Edit files in `src/` directory
   - Changes appear instantly in browser
   - Fix any displayed errors and save again

4. **Test Your Changes**:
   - Use the application UI to test features
   - Open browser DevTools (F12) to check console
   - Use Thunder Client or Postman to test API directly

5. **Stop the Server**:
   - Press `Ctrl+C` in the terminal

### Available Development Commands

```bash
# Start development server with hot reload
npm run dev

# Check for TypeScript errors
npm run type-check

# Run linting checks
npm run lint

# Build for production
npm run build

# Preview production build locally
npm run preview

# Run unit tests
npm run test

# Run tests in watch mode (continuous)
npm run test -- --watch

# Run tests with UI
npm run test:ui

# Generate coverage report
npm run test:coverage

# Format code with Prettier
npm run format

# Format and lint code
npm run lint:fix
```

### Project Structure

```plaintext
frontend/
├── src/
│   ├── components/          # Reusable React components
│   │   ├── Layout/
│   │   ├── Tabs/
│   │   ├── Forms/
│   │   └── Common/
│   ├── hooks/               # Custom React hooks
│   │   ├── useApi.ts
│   │   ├── useTheme.ts
│   │   └── [other hooks]
│   ├── services/            # API service layer
│   │   └── api.ts           # Axios instance and requests
│   ├── context/             # Context providers
│   │   └── ThemeContext.tsx
│   ├── utils/               # Utility functions
│   ├── assets/              # Static files (images, fonts)
│   ├── App.tsx              # Root component
│   ├── main.tsx             # Application entry point
│   └── index.css            # Global styles
├── public/                  # Static files served as-is
├── .env.example             # Environment variables template
├── vite.config.ts           # Vite configuration
├── tsconfig.json            # TypeScript configuration
├── tailwind.config.js       # Tailwind CSS configuration
├── eslint.config.js         # ESLint configuration
├── vitest.config.ts         # Vitest configuration
├── package.json             # Dependencies and scripts
├── Dockerfile               # Docker configuration
├── nginx.conf               # Nginx configuration
└── README.md                # This file
```

### Environment-Specific Settings

```env
# Development
VITE_API_BASE_URL=http://localhost:8080
VITE_ENV=development

# Staging
VITE_API_BASE_URL=https://staging-api.example.com
VITE_ENV=staging

# Production
VITE_API_BASE_URL=https://api.example.com
VITE_ENV=production
```

### Common Development Issues

**Port 5173 Already in Use**:

```bash
# Use different port
npm run dev -- --port 3000

# Or kill process using port 5173
lsof -i :5173         # Unix/Mac
netstat -ano | findstr :5173  # Windows
```

**Backend Connection Error**:

```bash
# Check backend is running
curl http://localhost:8080/api/health

# Check VITE_API_BASE_URL in .env
cat .env

# Check browser console for CORS errors
# Backend might need CORS configuration
```

**Node Version Mismatch**:

```bash
# Check Node version
node --version

# Use Node Version Manager (nvm) to switch versions
nvm list          # See installed versions
nvm use 24        # Switch to Node 24
nvm install 24    # Install Node 24
```

**Module Not Found Errors**:

```bash
# Reinstall dependencies
rm -rf node_modules package-lock.json
npm install

# Clear Vite cache
rm -rf node_modules/.vite
```

**Hot Reload Not Working**:

```bash
# Restart dev server
npm run dev

# Check firewall/antivirus isn't blocking connections
# Clear browser cache (Ctrl+Shift+Delete)
# Restart browser
```

### Performance Tips

1. **Use Development Build**:
   - `npm run dev` - fast, unoptimized
   - `npm run build` - slower, production-optimized

2. **Monitor Bundle Size**:

   ```bash
   npm run build
   # Check size of dist/index.js
   ```

3. **Enable Source Maps**:
   - Vite enables by default in dev mode
   - See original source in browser DevTools

4. **Code Splitting**:
   - Vite automatically splits code
   - Monitor Network tab to see bundle requests

---

## 🚀 Development

### Start Development Server

```bash
npm run dev
```

The development server will start with:

- **URL**: http://localhost:3000
- **Hot Module Replacement (HMR)**: Enabled
- **Source Maps**: Enabled for debugging
- **Auto Reload**: On file changes

### Development Features

- ⚡ **Lightning Fast HMR**: Instant feedback on code changes
- 🔍 **Source Maps**: Full debugging support in browser DevTools
- 🎨 **Theme Hot Reload**: Instant theme changes without reload
- 📦 **Automatic Code Splitting**: Optimized bundle sizes
- 🔄 **API Proxy**: Seamless backend integration

### Development Workflow

1. **Start Backend** (in separate terminal):

   ```bash
   cd ../
   ./gradlew bootRun
   ```

2. **Start Frontend**:

   ```bash
   npm run dev
   ```

3. **Make Changes**: Edit files in `src/` directory

4. **Test Changes**: See updates instantly in browser

5. **Run Tests**:

   ```bash
   npm run test
   ```

---

## 📜 Available Scripts

### Development Scripts

```bash
# Start development server with hot reload
npm run dev
```

### Build Scripts

```bash
# Build for production (optimized and minified)
npm run build

# Preview production build locally
npm run preview
```

### Testing Scripts

```bash
# Run all tests in watch mode
npm run test

# Run tests with UI interface
npm run test:ui

# Generate test coverage report
npm run test:coverage
```

### Code Quality Scripts

```bash
# Run ESLint to check code quality
npm run lint

# Auto-fix ESLint issues
npm run lint:fix

# Format code with Prettier
npm run format

# Check code formatting
npm run format:check
```

### Package Management

```bash
# Install dependencies
npm install

# Update dependencies
npm update

# Audit for vulnerabilities
npm audit

# Fix vulnerabilities
npm audit fix
```

---

## 📦 Building for Production

### Create Production Build

```bash
npm run build
```

This command:

1. ✅ Compiles TypeScript to JavaScript
2. ✅ Optimizes and minifies code
3. ✅ Generates source maps
4. ✅ Creates static assets in `dist/` directory
5. ✅ Applies tree-shaking for smaller bundle size
6. ✅ Optimizes CSS and images

### Build Output

```shell
dist/
├── assets/
│   ├── index-[hash].js       # Main JavaScript bundle
│   ├── index-[hash].css      # Compiled CSS
│   └── [images/fonts]        # Optimized assets
├── index.html                # Entry HTML file
└── favicon.ico               # Favicon
```

### Preview Production Build

```bash
npm run preview
```

Opens the production build at http://localhost:4173

### Build Optimization

**Bundle Size Targets:**

- Main bundle: < 200KB gzipped
- CSS bundle: < 50KB gzipped
- Total bundle: < 300KB gzipped

**Optimization Techniques:**

- Code splitting for routes
- Tree shaking for unused code
- Minification and compression
- Asset optimization (images, fonts)
- Lazy loading for components

---

## 🧪 Testing

### Running Tests

**Watch Mode** (recommended for development):

```bash
npm run test
```

**Single Run**:

```bash
npm run test -- --run
```

**With UI Interface**:

```bash
npm run test:ui
```

Opens an interactive UI at http://localhost:51204

**Coverage Report**:

```bash
npm run test:coverage
```

Report location: `coverage/index.html`

### Test Structure

```shell
tests/
├── components/              # Component tests
│   ├── Layout/
│   ├── Tabs/
│   ├── Forms/
│   └── Common/
├── hooks/                   # Custom hook tests
├── services/                # API service tests
├── utils/                   # Utility function tests
└── setup.ts                 # Test configuration
```

### Writing Tests

**Component Test Example:**

```typescript
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { Button } from '@/components/Common/Button';

describe('Button Component', () => {
  it('renders button with text', () => {
    render(<Button label="Click Me" />);
    expect(screen.getByText('Click Me')).toBeInTheDocument();
  });

  it('calls onClick when clicked', () => {
    const handleClick = vi.fn();
    render(<Button label="Click Me" onClick={handleClick} />);

    fireEvent.click(screen.getByText('Click Me'));
    expect(handleClick).toHaveBeenCalledTimes(1);
  });
});
```

### Coverage Targets

- **Minimum Coverage**: 80% across all metrics
- **Statements**: 80%+
- **Branches**: 75%+
- **Functions**: 80%+
- **Lines**: 80%+

### Test Best Practices

- ✅ Test user behavior, not implementation details
- ✅ Use semantic queries (getByRole, getByLabelText)
- ✅ Mock external dependencies (API calls)
- ✅ Test edge cases and error states
- ✅ Keep tests focused and isolated

---

## 🐳 Docker

### Build Docker Image

```bash
docker build -t resume-frontend:latest .
```

### Run Docker Container

```bash
docker run -d \
  --name resume-frontend \
  -p 80:80 \
  -e VITE_API_BASE_URL=http://backend:8080 \
  resume-frontend:latest
```

### Docker Multi-Stage Build

The `Dockerfile` uses a multi-stage build for optimization:

**Stage 1: Build** (node:20-alpine)

- Installs dependencies
- Compiles TypeScript
- Builds production bundle

**Stage 2: Runtime** (nginx:alpine)

- Copies built assets
- Serves with Nginx
- Minimal image size (~20MB)

### Docker Compose

```bash
# Start full stack (backend + frontend)
docker compose up --build

# Stop all services
docker compose down

# View logs
docker compose logs -f frontend
```

### Docker Image Size

- **Build stage**: ~400MB (includes build tools)
- **Runtime image**: ~20MB (nginx + static files)
- **Optimization**: Multi-stage build reduces final size by 95%

---

## 🔐 Environment Variables

### Configuration File

**File**: `.env`

```env
# API Configuration (Required)
VITE_API_BASE_URL=http://localhost:8080

# Optional Configuration
VITE_APP_TITLE=Resume Optimizer
VITE_MAX_FILE_SIZE=524288
VITE_ENABLE_DEBUG=false
```

### Environment Variables Reference

| Variable             | Description                  | Default                 | Required |
| -------------------- | ---------------------------- | ----------------------- | -------- |
| `VITE_API_BASE_URL`  | Backend API endpoint         | `http://localhost:8080` | Yes      |
| `VITE_APP_TITLE`     | Application title            | `Resume Optimizer`      | No       |
| `VITE_MAX_FILE_SIZE` | Max file upload size (bytes) | `524288` (500KB)        | No       |
| `VITE_ENABLE_DEBUG`  | Enable debug logging         | `false`                 | No       |

### Accessing Environment Variables

In code:

```typescript
// Access environment variable
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

// Check if variable exists
if (!apiBaseUrl) {
  throw new Error('VITE_API_BASE_URL is not defined');
}
```

### Environment-Specific Configuration

**Development** (`.env.development`):

```env
VITE_API_BASE_URL=http://localhost:8080
VITE_ENABLE_DEBUG=true
```

**Production** (`.env.production`):

```env
VITE_API_BASE_URL=https://api.example.com
VITE_ENABLE_DEBUG=false
```

### Important Notes

- ⚠️ All Vite environment variables must be prefixed with `VITE_`
- ⚠️ Never commit `.env` file (use `.env.example`)
- ⚠️ Restart dev server after changing `.env` file
- ⚠️ Environment variables are embedded at build time

---

## 📁 Project Structure

```shell
frontend/
├── public/                             # Static assets
│   ├── favicon.ico
│   └── logo.png
│
├── src/                                # Source code
│   ├── components/                     # React components
│   │   ├── Layout/                     # Layout components
│   │   │   ├── Navbar.tsx              # Top navigation bar
│   │   │   ├── Sidebar.tsx             # File history sidebar
│   │   │   ├── FileHistory.tsx         # File list component
│   │   │   └── MainLayout.tsx          # Main layout wrapper
│   │   ├── Tabs/                       # Tab components
│   │   │   ├── MainContentTab.tsx      # Upload & process tab
│   │   │   └── AdditionalToolsTab.tsx  # Utilities tab
│   │   ├── Forms/                      # Form components
│   │   │   ├── DocumentUploadForm.tsx  # File upload form
│   │   │   ├── MarkdownToPdfForm.tsx   # MD to PDF form
│   │   │   └── FormInputs.tsx          # Reusable inputs
│   │   └── Common/                     # Shared components
│   │       ├── ErrorBoundary.tsx       # Error handling
│   │       ├── LoadingSpinner.tsx      # Loading indicator
│   │       ├── ConfirmDialog.tsx       # Confirmation modal
│   │       └── ThemeToggle.tsx         # Theme switcher
│   │
│   ├── pages/                          # Page components
│   │   ├── HomePage.tsx                # Main application page
│   │   └── NotFound.tsx                # 404 page
│   │
│   ├── hooks/                          # Custom React hooks
│   │   ├── useTheme.ts                 # Theme management hook
│   │   ├── useApi.ts                   # API calls hook
│   │   ├── useFileManagement.ts        # File operations hook
│   │   └── useToast.ts                 # Toast notifications hook
│   │
│   ├── services/                       # Service layer
│   │   ├── api.ts                      # Axios configuration
│   │   ├── fileService.ts              # File operations API
│   │   └── documentService.ts          # Document processing API
│   │
│   ├── context/                        # React Context providers
│   │   ├── ThemeContext.tsx            # Theme state management
│   │   └── AppContext.tsx              # Global app state
│   │
│   ├── utils/                          # Utility functions
│   │   ├── constants.ts                # App constants
│   │   ├── validators.ts               # Form validators
│   │   ├── helpers.ts                  # Helper functions
│   │   └── types.ts                    # TypeScript types
│   │
│   ├── assets/                         # Images, fonts, etc.
│   │
│   ├── App.tsx                         # Root component
│   ├── main.tsx                        # Entry point
│   ├── index.css                       # Global styles
│   └── vite-env.d.ts                   # Vite type definitions
│
├── tests/                              # Test files
│   ├── components/                     # Component tests
│   ├── hooks/                          # Hook tests
│   ├── services/                       # Service tests
│   ├── utils/                          # Utility tests
│   └── setup.ts                        # Test configuration
│
├── .env.example                        # Environment template
├── .gitignore                          # Git ignore rules
├── .prettierrc                         # Prettier configuration
├── .dockerignore                       # Docker ignore rules
├── Dockerfile                          # Docker build configuration
├── nginx.conf                          # Nginx server configuration
├── package.json                        # Dependencies and scripts
├── package-lock.json                   # Locked dependency versions
├── tsconfig.json                       # TypeScript configuration
├── tsconfig.app.json                   # App-specific TS config
├── tsconfig.node.json                  # Node-specific TS config
├── vite.config.ts                      # Vite configuration
├── vitest.config.ts                    # Vitest configuration
├── tailwind.config.js                  # Tailwind CSS configuration
├── postcss.config.js                   # PostCSS configuration
├── eslint.config.js                    # ESLint configuration
└── README.md                           # This file
```

### Directory Descriptions

| Directory        | Purpose                                    |
| ---------------- | ------------------------------------------ |
| `src/components` | Reusable React components                  |
| `src/pages`      | Top-level page components                  |
| `src/hooks`      | Custom React hooks for logic reuse         |
| `src/services`   | API integration and business logic         |
| `src/context`    | Global state management with React Context |
| `src/utils`      | Utility functions and helpers              |
| `src/assets`     | Static assets (images, fonts, etc.)        |
| `tests/`         | All test files mirror src/ structure       |
| `public/`        | Static files served as-is                  |

---

## ✨ Features

### User Interface

- **Tab-Based Navigation**: Switch between Main Content and Additional Tools
- **File History Panel**: Always visible sidebar with file operations
- **Theme Toggle**: Switch between light and dark themes
- **Responsive Layout**: Adapts from mobile (320px) to desktop (1920px+)
- **Toast Notifications**: Non-intrusive feedback messages
- **Loading States**: Visual indicators during API calls
- **Error Handling**: Graceful error display with Error Boundaries

### Document Management

- **Upload Methods**: Paste text or upload files
- **File Listing**: View all uploaded and generated files
- **Download Files**: Click to download any file
- **Delete Files**: Remove unwanted files with confirmation
- **File Metadata**: Display name, size, and upload date

### Document Processing

- **Resume Optimization**: AI-powered resume tailoring
- **Cover Letter Generation**: Automated cover letter creation
- **Markdown to PDF**: Convert markdown files to PDF
- **Real-time Processing**: Progress indicators during processing
- **Result Display**: Show generated content inline

### Form Features

- **Input Validation**: Client-side validation with error messages
- **Toggle Modes**: Switch between paste and file upload
- **Multi-line Text**: Large text areas for pasting content
- **File Upload**: Drag & drop or click to browse
- **Clear Form**: Reset all fields

### Accessibility

- **WCAG 2.1 AA**: Compliance with accessibility standards
- **Keyboard Navigation**: Full keyboard support
- **ARIA Labels**: Proper labels on all interactive elements
- **Focus Indicators**: Visible focus states
- **Screen Reader**: Compatible with screen readers
- **High Contrast**: Support for high contrast mode

---

## 🎨 Theme Customization

### Built-in Themes

The application includes two PrimeReact themes:

- **Lara Light** (Default): Clean, professional light theme
- **Lara Dark**: Modern dark theme for low-light environments

### Theme Switching

Users can switch themes using the theme toggle button in the navbar. Preferences are saved to localStorage and persist across sessions.

### Customizing Themes

**1. Modify Tailwind Configuration** (`tailwind.config.js`):

```javascript
export default {
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#f0f9ff',
          100: '#e0f2fe',
          // ... custom colors
        },
      },
    },
  },
};
```

**2. Override PrimeReact Theme** (in `index.css`):

```css
/* Custom primary color */
:root {
  --primary-color: #3b82f6;
  --primary-color-text: #ffffff;
}

/* Dark theme overrides */
.dark {
  --primary-color: #60a5fa;
  --surface-ground: #1e293b;
}
```

**3. Create Custom Theme**:

See [PrimeReact Theme Designer](https://designer.primereact.org/) for visual theme creation.

### Theme Architecture

```typescript
// ThemeContext.tsx
export interface ThemeContextType {
  theme: 'light' | 'dark';
  toggleTheme: () => void;
}

// useTheme hook
const { theme, toggleTheme } = useTheme();
```

### Theme Persistence

Themes are saved to localStorage:

```typescript
localStorage.setItem('theme', 'dark');
const savedTheme = localStorage.getItem('theme') || 'light';
```

---

## 🔌 API Integration

### API Service Configuration

**File**: `src/services/api.ts`

```typescript
import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor
api.interceptors.request.use(
  config => {
    // Add auth token if available
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

// Response interceptor
api.interceptors.response.use(
  response => response,
  error => {
    // Handle errors globally
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

export default api;
```

### API Endpoints

**File Service** (`src/services/fileService.ts`):

```typescript
import api from './api';

// List all files
export const listFiles = async () => {
  const response = await api.get('/files');
  return response.data;
};

// Download file
export const downloadFile = async (filename: string) => {
  const response = await api.get(`/files/${filename}`, {
    responseType: 'blob',
  });
  return response.data;
};

// Delete file
export const deleteFile = async (filename: string) => {
  const response = await api.delete(`/files/${filename}`);
  return response.data;
};
```

**Document Service** (`src/services/documentService.ts`):

```typescript
import api from './api';

// Upload and optimize documents
export const uploadAndOptimize = async (data: OptimizeRequest) => {
  const response = await api.post('/upload', data);
  return response.data;
};

// Convert markdown to PDF
export const markdownToPdf = async (file: File) => {
  const formData = new FormData();
  formData.append('file', file);

  const response = await api.post('/markdownFile2PDF', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
    responseType: 'blob',
  });

  return response.data;
};
```

### Using API in Components

```typescript
import { useEffect, useState } from 'react';
import { listFiles } from '@/services/fileService';

export const FileHistory = () => {
  const [files, setFiles] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchFiles = async () => {
      try {
        const data = await listFiles();
        setFiles(data);
      } catch (error) {
        console.error('Failed to fetch files:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchFiles();
  }, []);

  // ... render component
};
```

### Error Handling

```typescript
try {
  await uploadAndOptimize(data);
  toast.success('Document uploaded successfully');
} catch (error) {
  if (axios.isAxiosError(error)) {
    if (error.response?.status === 400) {
      toast.error('Invalid file format');
    } else if (error.response?.status === 500) {
      toast.error('Server error. Please try again.');
    } else {
      toast.error('An error occurred');
    }
  }
}
```

---

## 🏗️ Component Architecture

### Component Hierarchy

```shell
App
├── ThemeProvider
│   ├── MainLayout
│   │   ├── Navbar
│   │   │   └── ThemeToggle
│   │   ├── ContentArea
│   │   │   ├── TabView
│   │   │   │   ├── MainContentTab
│   │   │   │   │   ├── DocumentUploadForm
│   │   │   │   │   └── ProcessingButtons
│   │   │   │   └── AdditionalToolsTab
│   │   │   │       └── MarkdownToPdfForm
│   │   ├── Sidebar
│   │   │   └── FileHistory
│   │   │       ├── FileItem (repeat)
│   │   │       │   ├── DownloadButton
│   │   │       │   └── DeleteButton
│   │   └── Toast
│   └── ErrorBoundary
```

### Component Patterns

**1. Container/Presentational Pattern:**

```typescript
// Container (Logic)
export const FileHistoryContainer = () => {
  const { files, loading, deleteFile } = useFileManagement();

  return (
    <FileHistoryPresentation
      files={files}
      loading={loading}
      onDelete={deleteFile}
    />
  );
};

// Presentational (UI)
export const FileHistoryPresentation = ({ files, loading, onDelete }) => {
  if (loading) return <LoadingSpinner />;

  return (
    <div>
      {files.map(file => (
        <FileItem key={file.id} file={file} onDelete={onDelete} />
      ))}
    </div>
  );
};
```

**2. Custom Hooks Pattern:**

```typescript
// useFileManagement.ts
export const useFileManagement = () => {
  const [files, setFiles] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchFiles = async () => {
    const data = await listFiles();
    setFiles(data);
  };

  const deleteFile = async (filename: string) => {
    await deleteFileAPI(filename);
    setFiles(files.filter(f => f.name !== filename));
  };

  useEffect(() => {
    fetchFiles();
  }, []);

  return { files, loading, deleteFile, fetchFiles };
};
```

**3. Compound Components Pattern:**

```typescript
export const FileHistory = ({ children }) => {
  return <div className="file-history">{children}</div>;
};

FileHistory.Header = ({ title }) => {
  return <h2>{title}</h2>;
};

FileHistory.List = ({ files }) => {
  return <ul>{files.map(f => <li key={f.id}>{f.name}</li>)}</ul>;
};

// Usage
<FileHistory>
  <FileHistory.Header title="Recent Files" />
  <FileHistory.List files={files} />
</FileHistory>
```

---

## 🗃️ State Management

### Context API

**Theme Context** (`src/context/ThemeContext.tsx`):

```typescript
interface ThemeContextType {
  theme: 'light' | 'dark';
  toggleTheme: () => void;
}

export const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

export const ThemeProvider = ({ children }: { children: ReactNode }) => {
  const [theme, setTheme] = useState<'light' | 'dark'>('light');

  useEffect(() => {
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark';
    if (savedTheme) setTheme(savedTheme);
  }, []);

  const toggleTheme = () => {
    const newTheme = theme === 'light' ? 'dark' : 'light';
    setTheme(newTheme);
    localStorage.setItem('theme', newTheme);
  };

  return (
    <ThemeContext.Provider value={{ theme, toggleTheme }}>
      {children}
    </ThemeContext.Provider>
  );
};

export const useTheme = () => {
  const context = useContext(ThemeContext);
  if (!context) throw new Error('useTheme must be used within ThemeProvider');
  return context;
};
```

### Local State

**Component State** (for UI-specific state):

```typescript
const [isOpen, setIsOpen] = useState(false);
const [inputValue, setInputValue] = useState('');
```

### Server State

**React Query** (alternative for future):

```typescript
import { useQuery } from '@tanstack/react-query';

const { data, isLoading, error } = useQuery({
  queryKey: ['files'],
  queryFn: listFiles,
});
```

---

## 🐛 Troubleshooting

### Common Issues

#### Build Errors

**Issue**: TypeScript compilation errors

```bash
# Solution 1: Clean and reinstall
rm -rf node_modules package-lock.json
npm install

# Solution 2: Check TypeScript config
npx tsc --noEmit
```

**Issue**: Module not found errors

```bash
# Solution: Check imports and paths in tsconfig.json
{
  "compilerOptions": {
    "paths": {
      "@/*": ["./src/*"]
    }
  }
}
```

#### Runtime Errors

**Issue**: "Cannot connect to backend"

```bash
# Check backend is running
curl http://localhost:8080/api/health

# Verify .env configuration
cat .env | grep VITE_API_BASE_URL

# Check browser console for CORS errors
```

**Issue**: Theme not switching

```bash
# Clear localStorage
localStorage.clear()

# Check ThemeProvider is wrapping App
# In main.tsx:
<ThemeProvider>
  <App />
</ThemeProvider>
```

#### Development Issues

**Issue**: Hot reload not working

```bash
# Restart dev server
npm run dev

# Clear Vite cache
rm -rf node_modules/.vite
```

**Issue**: Styles not applying

```bash
# Rebuild Tailwind
npm run build

# Check PostCSS configuration
# Verify tailwind.config.js imports
```

### Debugging Tips

**Enable debug logging:**

```typescript
// In .env
VITE_ENABLE_DEBUG = true;

// In code
if (import.meta.env.VITE_ENABLE_DEBUG) {
  console.log('Debug info:', data);
}
```

**Check API calls:**

```typescript
// In api.ts, add interceptors
api.interceptors.request.use(request => {
  console.log('Request:', request);
  return request;
});
```

**React DevTools:**

- Install React DevTools browser extension
- Inspect component tree and props
- Profile performance issues

### Getting Help

1. Check browser console for errors
2. Review [PRD document](../PRD-PRIMEREACT-DOCKER-v2.md)
3. Check [PrimeReact documentation](https://primereact.org/)
4. Open issue on [GitHub](https://github.com/pbaletkeman/java-resumes/issues)

---

## 📝 Code Style

### ESLint Configuration

**File**: `eslint.config.js`

```javascript
export default [
  {
    rules: {
      'react/prop-types': 'off',
      'react-hooks/exhaustive-deps': 'warn',
      '@typescript-eslint/no-unused-vars': 'error',
    },
  },
];
```

### Prettier Configuration

**File**: `.prettierrc`

```json
{
  "semi": true,
  "trailingComma": "es5",
  "singleQuote": true,
  "printWidth": 100,
  "tabWidth": 2
}
```

### Coding Standards

**Naming Conventions:**

- Components: PascalCase (`MainContentTab`)
- Files: PascalCase for components (`MainContentTab.tsx`)
- Hooks: camelCase with `use` prefix (`useFileManagement`)
- Constants: UPPER_SNAKE_CASE (`API_BASE_URL`)
- Functions: camelCase (`fetchFiles`)

**Import Order:**

1. React imports
2. Third-party libraries
3. Internal components
4. Internal hooks
5. Internal utils
6. Types
7. Styles

```typescript
import { useState, useEffect } from 'react';
import axios from 'axios';
import { Button } from 'primereact/button';

import { MainLayout } from '@/components/Layout';
import { useTheme } from '@/hooks/useTheme';
import { formatDate } from '@/utils/helpers';
import type { FileInfo } from '@/utils/types';

import './styles.css';
```

**Component Structure:**

```typescript
// 1. Imports
import { ... } from '...';

// 2. Types/Interfaces
interface ComponentProps {
  prop1: string;
  prop2?: number;
}

// 3. Component
export const Component = ({ prop1, prop2 }: ComponentProps) => {
  // 4. Hooks
  const [state, setState] = useState();

  // 5. Effects
  useEffect(() => {
    // ...
  }, []);

  // 6. Handlers
  const handleClick = () => {
    // ...
  };

  // 7. Render
  return (
    <div>{/* ... */}</div>
  );
};
```

---

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

### Development Workflow

1. **Fork the repository**
2. **Create feature branch**: `git checkout -b feature/your-feature`
3. **Make changes**: Follow code style guidelines
4. **Write tests**: Ensure 80%+ coverage
5. **Run tests**: `npm run test`
6. **Run linting**: `npm run lint`
7. **Commit changes**: `git commit -m "feat: add feature"`
8. **Push to fork**: `git push origin feature/your-feature`
9. **Create Pull Request**

### Code Review Checklist

- [ ] Code follows style guidelines
- [ ] Tests added for new features
- [ ] All tests passing
- [ ] ESLint checks passing
- [ ] No console errors/warnings
- [ ] Responsive design tested
- [ ] Accessibility tested
- [ ] Documentation updated

### Commit Message Format

```plaintext
type(scope): subject

body (optional)

footer (optional)
```

**Types:**

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting)
- `refactor`: Code refactoring
- `test`: Test additions/updates
- `chore`: Maintenance tasks

**Examples:**

```plaintext
feat(upload): add drag and drop support
fix(theme): persist theme selection
docs(readme): update installation instructions
```

---

**For complete documentation, see the [Root README](../README.md) and [PRD](../PRD-PRIMEREACT-DOCKER-v2.md)**

**Made with ❤️ using React, TypeScript, and PrimeReact**
