# Code Quality Standards

Comprehensive guide for maintaining code quality in java-resumes.

## Table of Contents

- [Quality Metrics](#-quality-metrics)
- [Java Code Quality](#-java-code-quality)
- [Frontend Code Quality](#-frontend-code-quality)
- [Testing Requirements](#-testing-requirements)
- [Best Practices](#-best-practices)

---

## 📊 Quality Metrics

### Coverage Targets

| Metric        | Target | Method                   |
| ------------- | ------ | ------------------------ |
| Code Coverage | 80%+   | Line and branch coverage |
| Complexity    | Low    | Methods under 10 lines   |
| Duplication   | <3%    | No copy-paste code       |
| Security      | A      | No vulnerabilities       |
| Style         | 100%   | Checkstyle compliance    |

---

## ☕ Java Code Quality

### Checkstyle Configuration

**Tool:** Checkstyle 10.14.2
**Config:** `config/checkstyle/checkstyle.xml`
**Compliance:** 100% required

### Running Checkstyle

```bash
# Check main code
./gradlew checkstyleMain

# Check test code
./gradlew checkstyleTest

# Full check (both + build)
./gradlew check

# Generate report
./gradlew checkstyleMain -q
# Report at: build/reports/checkstyle/
```

### Java Style Guide

Based on **Google Java Style Guide**

#### Naming Conventions

| Type      | Convention           | Example               |
| --------- | -------------------- | --------------------- |
| Package   | lowercase            | `ca.letkeman.resumes` |
| Class     | PascalCase           | `ResumeController`    |
| Interface | PascalCase           | `FilesStorageService` |
| Method    | camelCase            | `processResume()`     |
| Variable  | camelCase            | `jobDescription`      |
| Constant  | UPPER_SNAKE_CASE     | `MAX_FILE_SIZE`       |
| Enum      | PascalCase (members) | `RESUME_OPTIMIZATION` |

#### Code Structure

**File Organization:**

```java
package ca.letkeman.resumes.controller;

// Imports (organized alphabetically)
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

// Class-level Javadoc
/**
 * REST API controller for resume operations.
 * Handles file uploads, downloads, and optimization requests.
 */
@RestController
@RequestMapping("/api")
public class ResumeController {

    // Constants
    private static final int MAX_FILE_SIZE = 500_000;

    // Dependencies
    private final FilesStorageService storageService;

    // Constructor
    public ResumeController(FilesStorageService storageService) {
        this.storageService = storageService;
    }

    // Public methods
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestBody Optimize optimize) {
        // Implementation
    }

    // Private helper methods
    private void validate(Optimize optimize) {
        // Validation logic
    }
}
```

#### Formatting Rules

- **Line length:** Max 120 characters
- **Indentation:** 4 spaces (not tabs)
- **Braces:** Allman style
  ```java
  if (condition)
  {
      // code
  }
  ```
- **Javadoc:** Required for public methods
  ```java
  /**
   * Processes resume with AI optimization.
   *
   * @param optimize the optimization request
   * @return optimized resume content
   * @throws IOException if file operations fail
   */
  public String processResume(Optimize optimize) throws IOException {
      // Implementation
  }
  ```

#### Comments & Documentation

**When to Comment:**

- ✅ Complex business logic
- ✅ Workarounds and hacks
- ✅ Non-obvious algorithm decisions
- ✅ Public API contracts

**When NOT to Comment:**

- ❌ Obvious operations (counter++, getter)
- ❌ Repeating what code says
- ❌ Outdated comments

**Example:**

```java
// Good: Explains WHY
// Apply progressive tax brackets: 10% for first $10k, 20% above
BigDecimal tax = calculateProgressiveTax(income);

// Bad: Explains WHAT (code already does this)
// Increment counter by 1
counter++;
```

---

## 🎨 TypeScript/React Code Quality

### ESLint Configuration

**Tool:** ESLint (latest)
**Config:** `frontend/.eslintrc.json`

### Running ESLint

```bash
cd frontend

# Lint all files
npm run lint

# Fix auto-fixable issues
npm run lint -- --fix

# Check specific file
npm run lint -- src/components/MainTab.tsx

# Generate report
npm run lint -- --format json > lint-report.json
```

### TypeScript Strict Mode

**tsconfig.json:**

```json
{
  "compilerOptions": {
    "strict": true,
    "noImplicitAny": true,
    "strictNullChecks": true,
    "strictFunctionTypes": true,
    "noUnusedLocals": true,
    "noUnusedParameters": true,
    "noImplicitReturns": true
  }
}
```

### React Best Practices

#### Component Structure

```tsx
// Good: Functional component with hooks
import React, { useState, useEffect } from "react";

interface MainTabProps {
  onSubmit: (data: FormData) => void;
}

export const MainTab: React.FC<MainTabProps> = ({ onSubmit }) => {
  // Hooks
  const [formData, setFormData] = useState<FormData>({});
  const [loading, setLoading] = useState(false);

  // Effects
  useEffect(() => {
    // Cleanup
    return () => {
      // Cleanup code
    };
  }, []);

  // Event handlers
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  // Render
  return <form onSubmit={handleSubmit}>{/* JSX */}</form>;
};
```

#### Naming Conventions

| Type           | Convention | Example          |
| -------------- | ---------- | ---------------- |
| Component      | PascalCase | `MainContentTab` |
| Hook           | camelCase  | `useTheme`       |
| Function       | camelCase  | `getFileList`    |
| Variable       | camelCase  | `isLoading`      |
| Type/Interface | PascalCase | `FileInfo`       |
| Constant       | UPPER_CASE | `API_TIMEOUT`    |

#### Code Organization

```tsx
// 1. Imports
import React, { useState } from "react";
import { useApi } from "../hooks/useApi";

// 2. Type definitions
interface Props {
  title: string;
}

// 3. Component
export const MyComponent: React.FC<Props> = ({ title }) => {
  // 3a. State
  const [data, setData] = useState(null);

  // 3b. Hooks
  const api = useApi();

  // 3c. Event handlers
  const handleClick = () => {
    // Handler logic
  };

  // 3d. Render
  return <div>{title}</div>;
};

// 4. Exports
export default MyComponent;
```

---

## 🧪 Testing Standards

### Coverage Requirements

**Backend:**

- Line coverage: 80%+
- Branch coverage: 75%+
- Method coverage: 80%+

**Frontend:**

- Line coverage: 80%+
- Function coverage: 80%+
- Branch coverage: 75%+

### Test Structure

```java
// Backend Test
@DisplayName("ResumeController")
class ResumeControllerTest {
    @Test
    @DisplayName("should upload resume successfully")
    void testUploadResume_Success() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file", "resume.pdf", "application/pdf", "content".getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/api/upload").file(file))
            .andExpect(status().isOk());
    }
}
```

```tsx
// Frontend Test
describe("MainTab", () => {
  it("should render upload form", () => {
    render(<MainTab onSubmit={jest.fn()} />);
    expect(screen.getByText(/Upload/i)).toBeInTheDocument();
  });
});
```

---

## 📐 Architecture Guidelines

### Layered Architecture

```
┌─────────────────────┐
│  Presentation Layer │  (Controllers, UI Components)
├─────────────────────┤
│   Business Logic    │  (Services, Hooks, Utils)
├─────────────────────┤
│  Data Access Layer  │  (Repositories, API Calls)
├─────────────────────┤
│   External Services │  (LLM, File Storage)
└─────────────────────┘
```

### Dependency Injection

**Backend (Spring):**

```java
@Service
public class OptimizationService {
    private final ApiService apiService;
    private final FilesStorageService storageService;

    public OptimizationService(
        ApiService apiService,
        FilesStorageService storageService) {
        this.apiService = apiService;
        this.storageService = storageService;
    }
}
```

**Frontend (React Context):**

```tsx
import { createContext, useContext } from "react";

const ApiContext = createContext<ApiService | undefined>(undefined);

export const useApi = () => {
  const context = useContext(ApiContext);
  if (!context) {
    throw new Error("useApi must be used within ApiProvider");
  }
  return context;
};
```

---

## 🔐 Security Best Practices

### Input Validation

```java
// Java
if (optimize == null || optimize.getResume().isEmpty()) {
    throw new IllegalArgumentException("Resume cannot be empty");
}

// TypeScript
if (!resume || resume.trim().length === 0) {
    throw new Error('Resume cannot be empty');
}
```

### Error Handling

```java
// Java - Good
try {
    processFile(file);
} catch (IOException e) {
    logger.error("File processing failed: {}", e.getMessage());
    throw new ApplicationException("Could not process file", e);
}
```

```tsx
// TypeScript - Good
try {
  const result = await api.uploadFile(file);
  return result;
} catch (error) {
  logger.error("Upload failed:", error);
  throw new UserFacingError("File upload failed. Please try again.");
}
```

### SQL Injection Prevention

**Use parameterized queries:**

```java
// Bad
String query = "SELECT * FROM users WHERE id = " + userId;

// Good
String query = "SELECT * FROM users WHERE id = ?";
PreparedStatement stmt = connection.prepareStatement(query);
stmt.setInt(1, userId);
```

---

## 📝 Documentation Standards

### Code Comments

```java
// Good: Explains WHY
// Exponential backoff: max delay 30s after 5 retries
private long calculateBackoffDelay(int attemptNumber) {
    return Math.min(1000 * (1 << attemptNumber), 30_000);
}

// Bad: Repeats code
// Add 1 to counter
counter++;
```

### Javadoc Standards

```java
/**
 * Processes the given resume and job description through AI optimization.
 *
 * <p>This method handles the complete optimization pipeline including
 * validation, LLM communication, and file generation. Processing occurs
 * asynchronously in a background thread.
 *
 * @param optimize the optimization request containing resume and job description
 * @return a response message indicating processing has started
 * @throws IllegalArgumentException if required fields are missing or invalid
 * @throws IOException if file operations fail
 * @see OptimizeRequest
 * @since 1.0
 */
public ResponseEntity<ResponseMessage> processResume(Optimize optimize)
    throws IOException {
    // Implementation
}
```

### README & Markdown

- Clear, concise explanations
- Code examples for complex topics
- Proper heading hierarchy
- Bullet points for lists
- Links to related documentation

---

## 🔄 Review Checklist

### Code Review Standards

**Before Commit:**

- [ ] Code compiles/builds
- [ ] All tests pass
- [ ] No compile warnings
- [ ] Code style passes checks
- [ ] No dead code
- [ ] Comments are clear
- [ ] No hardcoded values

**Pull Request:**

- [ ] 80%+ test coverage
- [ ] Code quality checks pass
- [ ] Follows architecture patterns
- [ ] Security best practices followed
- [ ] Documentation updated
- [ ] No performance regressions

**Review Process:**

1. Automated checks (build, tests, linting)
2. Code review by peer
3. Approval by maintainer
4. Merge to main branch

---

## 🚀 Continuous Quality

### Pre-commit Hook (Future)

**.git/hooks/pre-commit:**

```bash
#!/bin/bash

# Run linting
npm run lint || exit 1

# Run tests
npm run test || exit 1

# Run build
./gradlew build || exit 1

echo "✓ All quality checks passed"
```

### GitHub Actions (Future)

Automated quality checks on every push:

- ✅ Build verification
- ✅ Test execution
- ✅ Coverage reporting
- ✅ Code quality scanning
- ✅ Security analysis

---

## 📈 Refactoring Guidelines

### When to Refactor

- ✅ Code is duplicated (DRY principle)
- ✅ Methods are too long (>20 lines)
- ✅ Complex conditional logic
- ✅ Low test coverage
- ✅ High complexity (cyclomatic complexity >10)

### Refactoring Process

1. **Write tests** first to ensure coverage
2. **Make small changes** (one at a time)
3. **Run tests** after each change
4. **Commit frequently** with clear messages
5. **Review changes** for correctness

---

**See also:**

- [Testing](TESTING.md) - Testing strategies
- [Development Setup](DEVELOPMENT_SETUP.md) - Environment setup
- [Architecture](ARCHITECTURE.md) - Design patterns

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
  
---  
  
**Last Updated:** February 2, 2026  
**Maintained By:** java-resumes development team 
