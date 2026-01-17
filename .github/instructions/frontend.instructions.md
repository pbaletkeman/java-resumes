---
applyTo: "frontend/**/*.ts,frontend/**/*.tsx,frontend/**/*.js,frontend/**/*.jsx,frontend/**/*.json,frontend/**/*.css,frontend/**/*.yml,frontend/Dockerfile,frontend/nginx.conf"
---

# Frontend Development Instructions

## Project Overview

This is a React 19 frontend application for the Resume Optimizer platform, using PrimeReact components and TypeScript. It provides a tab-based UI for document upload, resume optimization, cover letter generation, and markdown-to-PDF conversion.

**Technology Stack:**

- React 19.2.0 with TypeScript 5.9.3
- Vite 7.2.4 (build tool)
- PrimeReact 10.9.7 (UI component library)
- Tailwind CSS 4.1.18
- Axios 1.13.2 (HTTP client)
- Vitest 4.0.17 (unit testing)
- ESLint & Prettier (code quality)

**Project Size:** 41+ component files, comprehensive test coverage

## Directory Structure

```
frontend/
├── src/
│   ├── components/
│   │   ├── Layout/                  # Layout components
│   │   │   ├── Navbar.tsx           # Top navigation with theme toggle
│   │   │   ├── MainLayout.tsx       # Main layout wrapper
│   │   │   └── Sidebar.tsx          # Left sidebar navigation
│   │   ├── Tabs/                    # Tab components
│   │   │   ├── MainContentTab.tsx   # Resume/cover letter processing
│   │   │   └── AdditionalToolsTab.tsx # Markdown to PDF conversion
│   │   ├── Forms/                   # Form components
│   │   │   ├── DocumentUploadForm.tsx
│   │   │   └── MarkdownToPdfForm.tsx
│   │   ├── Common/                  # Reusable components
│   │   │   ├── ErrorBoundary.tsx    # Error handling
│   │   │   ├── LoadingSpinner.tsx   # Loading indicator
│   │   │   ├── ConfirmDialog.tsx    # Confirmation modal
│   │   │   └── FileHistory.tsx      # File list panel
│   │   └── index.ts                 # Component exports
│   ├── hooks/                       # Custom React hooks
│   │   ├── useTheme.ts              # Theme management (light/dark)
│   │   ├── useApi.ts                # API call wrapper
│   │   └── useFileManagement.ts     # File operations
│   ├── services/
│   │   ├── api.ts                   # Axios configuration
│   │   └── fileService.ts           # File operation wrappers
│   ├── context/
│   │   ├── ThemeContext.tsx         # Theme context provider
│   │   └── AppContext.tsx           # App-level state
│   ├── utils/
│   │   ├── constants.ts             # App constants
│   │   ├── validators.ts            # Form validation
│   │   └── helpers.ts               # Utility functions
│   ├── App.tsx                      # Root component
│   ├── main.tsx                     # Entry point
│   └── index.css                    # Global styles
├── public/                          # Static assets
├── tests/                           # Test files
├── Dockerfile                       # Docker build config
├── nginx.conf                       # Nginx server config
├── vite.config.ts                   # Vite build config
├── vitest.config.ts                 # Vitest config
├── tsconfig.json                    # TypeScript config
├── tailwind.config.js               # Tailwind config
├── .eslintrc.json                   # ESLint rules
├── .prettierrc                      # Prettier formatting
├── package.json                     # Dependencies
├── .env.example                     # Environment template
└── README.md                        # Frontend documentation
```

## Build and Development Instructions

### Prerequisites

- Node.js 18+ (LTS recommended)
- npm 9+ or yarn
- Docker (for containerization)

### Setup

```bash
# Install dependencies
npm install

# Always run npm install before building or starting dev server
```

### Development Server

```bash
# Start dev server with hot reload
npm run dev

# Access application at: http://localhost:5173

# Dev server includes automatic TypeScript checking and hot module replacement
```

### Build and Test

```bash
# Build for production
npm run build

# Run unit tests
npm run test

# Run tests with UI
npm run test:ui

# Run tests with coverage report
npm run test:coverage
# Coverage report location: coverage/

# Lint code
npm run lint

# Format code with Prettier
npm run format

# Check types
npm run type-check
```

### Docker Build

```bash
# Build Docker image (runs npm build automatically)
docker build -t resume-frontend:latest .

# Run container
docker run -p 3000:80 resume-frontend:latest

# Access at: http://localhost:3000
```

## Configuration Files

- **`vite.config.ts`**: Build tool configuration, includes proxy for development API calls
- **`vitest.config.ts`**: Unit test framework configuration
- **`.eslintrc.json`**: Code linting rules
- **`.prettierrc`**: Code formatting configuration
- **`tsconfig.json`**: TypeScript compiler options (strict mode enabled)
- **`tailwind.config.js`**: Tailwind CSS theming
- **`.env.example`**: Environment variable template (copy to `.env` for development)
- **`Dockerfile`**: Multi-stage build (Node build → Nginx runtime)
- **`nginx.conf`**: Web server configuration for production

## Environment Variables

Copy `.env.example` to `.env` and configure:

```
VITE_API_URL=http://localhost:8080
VITE_APP_NAME=Resume Optimizer
# Add other variables as needed
```

## Key Development Patterns

### API Integration (useApi Hook)

```typescript
const { data, loading, error, fetch } = useApi<ResultType>(
  "/api/endpoint",
  "POST"
);

// Loading states trigger <LoadingSpinner />
// Error states trigger <ConfirmDialog /> or toast notifications
// Data changes update component state
```

### Theme Management (useTheme Hook)

```typescript
const { theme, toggleTheme } = useTheme();

// Persists to localStorage
// Updates HTML data-theme attribute
// Works with PrimeReact theme switching
```

### Form Validation (validators.ts)

```typescript
// Use centralized validators for consistency
const errors = validateDocumentUpload(resume, jobDescription);
if (errors.length > 0) {
  showErrors(errors); // Toast notifications
  return;
}
```

### Component Testing (Vitest)

```typescript
import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';

describe('ComponentName', () => {
    it('should render correctly', () => {
        render(<ComponentName />);
        expect(screen.getByText('expected text')).toBeInTheDocument();
    });
});
```

## Testing Strategy

- **Unit Tests**: Test components, hooks, utilities in isolation
- **Integration Tests**: Test component interactions and API integration
- **Component Tests**: Render components and verify behavior
- **Coverage Target**: 80%+ for all user-facing code

### Test File Structure

```
- Create `.test.tsx` files alongside component files
- Use `describe()` blocks for related tests
- Mock API calls with `vi.mock()`
- Test user interactions with `userEvent` from testing-library
```

## Common Issues and Workarounds

| Issue                  | Cause                 | Solution                                            |
| ---------------------- | --------------------- | --------------------------------------------------- |
| API connection refused | Backend not running   | Start backend: `./gradlew bootRun`                  |
| CORS errors            | API origin mismatch   | Check `VITE_API_URL` in `.env` matches backend      |
| TypeScript errors      | Type mismatches       | Run `npm run type-check` for full report            |
| Tests failing          | Outdated dependencies | Run `npm install` or `npm ci`                       |
| Build size too large   | Large dependencies    | Check bundle analysis: `npm run build -- --analyze` |
| Module not found       | Missing import        | Verify file exists and path is correct              |

## Validation Steps Before Committing

Always run these checks in order:

1. **Type check:** `npm run type-check`
2. **Lint code:** `npm run lint`
3. **Run tests:** `npm run test`
4. **Verify coverage:** Coverage should be 80%+
5. **Test in browser:** `npm run dev` and manually verify functionality
6. **Build:** `npm run build` (must succeed)
7. **Check for warnings:** Build should complete with no warnings

## Explicit Instructions

- **Always run `npm install`** before building or starting the dev server
- **Always run `npm run type-check`** before pushing—TypeScript errors must be zero
- **Always include tests** for new components or functionality
- Component import paths must use TypeScript aliases from `tsconfig.json` for consistency
- All forms must use centralized validation functions from `utils/validators.ts`
- API calls must use the `useApi()` hook for consistent error handling
- Theme persistence through localStorage is required for theme toggle
- Error boundaries must wrap all page-level components
- Loading states must show `<LoadingSpinner />` component
- File downloads must include confirmation with `<ConfirmDialog />`
- Toast notifications must be used for user feedback (success, error, warning)

## Code Quality Standards

- **Linting:** ESLint rules must pass—run `npm run lint`
- **Formatting:** Prettier formatting—run `npm run format`
- **Types:** Zero TypeScript errors—run `npm run type-check`
- **Tests:** 80%+ coverage required
- **Bundle Size:** Monitor with `npm run build` warnings
- **Performance:** Use React DevTools Profiler to identify slow renders

## Dependencies

- **React 19 & React DOM**: UI framework
- **TypeScript**: Type safety
- **PrimeReact**: Enterprise UI components (DataTable, Dialog, Toast, etc.)
- **Tailwind CSS**: Utility-first styling
- **Axios**: HTTP client
- **React Context API**: State management (no external store needed)
- **Vitest**: Unit testing framework
- **React Testing Library**: Component testing utilities
- **ESLint**: Code quality
- **Prettier**: Code formatting
- **Vite**: Build tool (fast, modern)

## References

- [React 19 Documentation](https://react.dev/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [PrimeReact Components](https://primereact.org/)
- [Tailwind CSS Utilities](https://tailwindcss.com/docs)
- [Vitest Guide](https://vitest.dev/)
- [Testing Library Best Practices](https://testing-library.com/docs/react-testing-library/intro)

**When in doubt, examine existing component files and test files for patterns. The codebase demonstrates all major patterns and best practices used in this project.**
