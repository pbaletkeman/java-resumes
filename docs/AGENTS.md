# AGENTS.md - AI Agent Guidelines for java-resumes

> **📍 Location:** `docs/AGENTS.md`
> **👥 Audience:** AI Agents, Developers
> **🔗 Related:** [Copilot Instructions](copilot-instructions.md) | [Quick Reference](QUICK_REFERENCE.md) | [Index](INDEX.md)

---

Comprehensive guidelines for AI agents (GitHub Copilot, Claude, etc.) working on java-resumes project tasks.

---

## Table of Contents

- [Overview](#overview)
- [Task Completion Workflow](#task-completion-workflow)
- [Backend Task Guidelines](#backend-task-guidelines)
- [Frontend Task Guidelines](#frontend-task-guidelines)
- [Documentation Standards](#documentation-standards)
- [Code Generation Guidelines](#code-generation-guidelines)
- [Quality Assurance](#quality-assurance)
- [Pre-Commit Validation](#pre-commit-validation)
- [Common Task Scenarios](#common-task-scenarios)
- [Important Commands Reference](#important-commands-reference)
- [Code Organization Standards](#code-organization-standards)
- [Testing Standards](#testing-standards)
- [Naming Conventions](#naming-conventions)
- [Best Practices for AI Agents](#best-practices-for-ai-agents)
- [Support & Escalation](#support--escalation)
- [Related Documentation](#related-documentation)

---

## Overview

This document provides AI-specific guidance for completing tasks on the java-resumes project. It complements the repository-wide [copilot-instructions.md](copilot-instructions.md) and path-specific instructions in [.github/instructions/](.github/instructions/).

AI agents working on this project must follow these guidelines to maintain code quality, consistency, and documentation standards. These guidelines ensure that:

- Generated code adheres to project conventions
- Documentation is clear, comprehensive, and maintainable
- All markdown files follow consistent formatting with table of contents
- Code changes are properly tested and validated
- All acceptance criteria are met before task completion

---Task Completion Workflow

### Phase 1: Understand the Task

1. **Read the request carefully** - Identify exact requirements and constraints
2. **Review relevant documentation** - Check [docs/](docs/), path-specific instructions, and code samples
3. **Assess scope** - Determine impact on backend, frontend, or both
4. **Identify dependencies** - Note any files, services, or modules that must be modified

### Phase 2: Plan the Work

1. **Create a plan** - Document what will be modified and why
2. **Check for conflicts** - Ensure changes don't conflict with existing code
3. **Verify test strategy** - Plan what tests need to be written/updated
4. **Consider documentation** - Identify what markdown files need updates

### Phase 3: Implement

1. **Start with backend** (if needed) - Implement services and models first
2. **Then frontend** (if needed) - Implement components and hooks
3. **Write tests** - Create unit tests as you go
4. **Run validation** - Check builds, tests, and code quality before finishing

### Phase 4: Validate

1. **Backend validation** - Run `./gradlew clean build && ./gradlew checkstyleMain`
2. **Frontend validation** - Run `npm run type-check && npm test && npm run build`
3. **Full test suite** - Ensure all tests pass with adequate coverage (80%+)
4. **Code quality** - Fix any style violations or type errors

### Phase 5: Document

1. **Update markdown** - Add/update relevant documentation
2. **Add comments** - Include JSDoc/TSDoc for new methods/functions
3. **List changes** - Summarize what was modified and why

---

## Backend Task Guidelines

### Adding a New Endpoint

**Step 1: Design the request/response**

- Create DTOs if needed in `src/main/java/ca/letkeman/resumes/model/`
- Use appropriate naming conventions
- Add field validation annotations

**Step 2: Implement service layer**

- Create or extend service in `src/main/java/ca/letkeman/resumes/service/`
- Add business logic and error handling
- Write unit tests in `src/test/java/ca/letkeman/resumes/service/`

**Step 3: Add controller endpoint**

- Add method to `ResumeController.java`
- Use appropriate HTTP method (@PostMapping, @GetMapping, etc.)
- Include @CrossOrigin for React frontend
- Add proper error handling and logging

**Step 4: Test the endpoint**

- Write unit tests in `src/test/java/ca/letkeman/resumes/controller/ResumeControllerTest.java`
- Test success and error cases
- Verify HTTP status codes

**Step 5: Validate**

```bash
./gradlew clean test
./gradlew checkstyleMain checkstyleTest
./gradlew clean build
```

### Backend Code Quality Requirements

1. **Checkstyle**: Zero violations (run `./gradlew checkstyleMain`)
2. **Testing**: 80%+ coverage required
3. **Types**: Proper type annotations on all methods
4. **Imports**: No unused imports
5. **Comments**: JSDoc for public methods, explain WHY not WHAT

### Backend REST Endpoint Pattern

```java
@PostMapping("/endpoint")
@CrossOrigin(origins = "*")
public ResponseEntity<?> endpointName(
    @RequestParam String param,
    @RequestBody RequestDTO request) {

    if (request == null || request.getValue().isEmpty()) {
        return ResponseEntity.badRequest()
            .body(new ResponseMessage("Invalid request"));
    }

    try {
        ResultDTO result = service.process(request);
        return ResponseEntity.ok(result);
    } catch (Exception e) {
        logger.error("Error processing request", e);
        return ResponseEntity.status(500)
            .body(new ResponseMessage("Processing failed"));
    }
}
```

### Backend Service Method Pattern

```java
public ResultDTO process(RequestDTO request) {
    // Validate input
    if (request == null) throw new IllegalArgumentException("Request cannot be null");

    // Process
    Result result = performLogic(request);

    // Return
    return mapToDTO(result);
}
```

### Backend Unit Test Pattern

```java
@Test
@DisplayName("Should process valid request successfully")
void test_process_validRequest_success() {
    RequestDTO request = new RequestDTO("valid");
    ResultDTO result = service.process(request);

    assertNotNull(result);
    assertEquals("expected", result.getValue());
}
```

---

## Frontend Task Guidelines

### Adding a New Component

**Step 1: Plan the component**

- Determine props (use TypeScript interfaces)
- P
  export const MyComponent: React.FC<ComponentProps> = ({ title, onSubmit }) => {
  const [state, setState] = useState("");
  const { data, loading, error } = useApi("/endpoint");

  const handleSubmit = () => {
  if (state.trim()) {
  onSubmit?.(state);
  }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
  <div className="p-4">
  <h2>{title}</h2>
  <input
  value={state}
  onChange={(e) => setState(e.target.value)}
  className="border rounded px-2 py-1"
  />
  <button
          onClick={handleSubmit}
          className="bg-blue-500 text-white px-4 py-2 rounded"
        >
  Submit
  </button>
  </div>
  );
  };

````

### Frontend Custom Hook Pattern

```tsx
export const useCustom = (param: string) => {
  const [state, setState] = useState<string>("initial");

  useEffect(() => {
    setState(param);
  }, [param]);

  return { state };
};
````

### Frontend Component Test Pattern

```tsx
import { render, screen, fireEvent } from "@testing-library/react";
import { MyComponent } from "./MyComponent";

describe("MyComponent", () => {
  it("should render with title", () => {
    render(<MyComponent title="Test" />);
    expect(screen.getByText("Test")).toBeInTheDocument();
  });

  it("should call onSubmit when button clicked", () => {
    const onSubmit = vi.fn();
    render(
      <MyComponent
        title="Test"
        onSubmit={onSubmit}
      />,
    );

    fireEvent.click(screen.getByText("Submit"));
    expect(onSubmit).toHaveBeenCalled();
  });
});
```

---

##

## Documentation Standards

### Markdown File Organization

**All markdown files with 100+ lines MUST include a table of contents** at the beginning of the file, after the main title and before the first major section.

#### Table of Contents Format

````markdown
# Document Title

## Table of Contents

- [Section 1](#section-1)
- [Pre-Commit Validation

### Before committing ANY changes:

```bash
# Backend
./gradlew clean build
./gradlew checkstyleMain checkstyleTest

# Frontend
npm run type-check
npm run lint
npm run build
npm test

# Both (optional but recommended)
npm run test:coverage
```
````

### Checklist:

- [ ] Backend build passes (if changed Java code)
- [ ] Frontend builds without errors (if changed TypeScript/React code)
- [ ] No Checkstyle violations (backend)
- [ ] No lint warnings (frontend)
- [ ] All tests pass
- [ ] 80%+ test coverage maintained
- [ ] Documentation updated if architecture changed
- [ ] No console errors or warnings

---

## Common Task Scenarios

### Scenario 1: Fix a Bug

1. **Understand the bug** - Read issue, reproduce if possible
2. **Locate the code** - Find relevant files
3. **Implement fix** - Make minimal necessary changes
4. **Write test** - Ensure test fails before fix, passes after
5. **Validate** - Run full test suite and quality checks
6. **Document** - Update changelog or status document

### Scenario 2: Add a Feature

1. **Understand requirements** - Read feature description and acceptance criteria
2. **Design** - Plan backend + frontend changes needed
3. **Implement backend** - Add service, controller, tests
4. **Implement frontend** - Add components, hooks, tests
5. **Integrate** - Connect frontend to backend API
6. **Validate** - Full test suite, coverage, build
7. **Document** - Update Architecture.md and README files

### Scenario 3: Refactor Code

1. **Understand existing code** - Read and understand what needs refactoring
2. **Plan refactoring** - Design how code should be structured
3. **Keep tests working** - Refactor while maintaining test coverage
4. **Validate behavior** - Ensure refactored code behaves identically
5. **Update tests if needed** - If test structure changed
6. **Document changes** - Explain why refactoring improves the code

### Scenario 4: Update Configuration

1. **Understand change** - Why is this config change needed?
2. **Identify files** - What configuration files need updates?
3. **Make changes** - Update all relevant config files consistently
4. **Test changes** - Verify application works with new config
5. **Document** - Explain configuration changes in README/docs
6. **Validate encoding** - Ensure `.gitattributes` formatting applied

---

## Important Commands Reference

### Backend

```bash
# Build & Test
./gradlew clean build              # Full build with tests
./gradlew test                     # Run tests only
./gradlew checkstyleMain           # Check code style
./gradlew checkstyleTest           # Check test style
./gradlew check                    # All checks (build + style)

# Run
./gradlew bootRun                  # Start application (port 8080)

# Dependencies
./gradlew dependencies             # List all dependencies
./gradlew dependencyInsight        # Analyze specific dependency
```

### Frontend

````bash
# Setup & Dev
npm install                        # Install dependencies
npm run dev                        # Start dev server (port 5173)

# Build & Test
npm run type-check                 # TypeScript type checking
npm run lint                       # ESLint validation
npm test                           # Run tests
npm run test:coverage              # Run tests with coverage report
npm run build                      # Build for production

# Preview & Debug
npm run preview                    # Preview production build
npm run test:ui                    # Interactive test UI
```Best Practices for AI Agents

1. **Ask for Clarification**: If requirements are ambiguous, ask before implementing
2. **Plan Before Code**: Create a plan and list files to modify before writing code
3. **Test Thoroughly**: Write tests as you code, don't add them after
4. **Validate Frequently**: Run builds and tests after each logical change
5. **Keep Changes Minimal**: Only modify what's necessary for the task
6. **Document Clearly**: Add comments explaining WHY, not WHAT
7. **Follow Patterns**: Use existing patterns in the codebase
8. **Check Quality**: Ensure Checkstyle/ESLint pass before finishing
9. **Update Docs**: Always update markdown files if architecture/process changed
10. **Verify Completeness**: Ensure all acceptance criteria are met

---Summary

Successfully completing tasks on java-resumes requires:

✅ Understanding requirements clearly
✅ Following the workflow phases (Understand → Plan → Implement → Validate → Document)
✅ Adhering to code quality standards (Checkstyle 100%, tests 80%+, TypeScript strict)
✅ Writing comprehensive tests (both backend and frontend)
✅ Updating documentation for architecture/process changes
✅ Running all validation checks before completing task
✅ Keeping changes minimal and focused

Reference [copilot-instructions.md](copilot-instructions.md) and [docs/](docs/) for detailed guidance.

---

**Last Updated**: 2024
**For**: GitHub Copilot, Claude, and other AI agents
**Repository**: https://github.com/pbaletkeman/java-resumesmd](docs/BACKEND_README.md)** - Backend guide
4. **[docs/README.md](docs/README.md)** - Frontend guide
5. **[copilot-instructions.md](copilot-instructions.md)** - Repository-wide guidance
6. **[.github/instructions/backend.instructions.md](.github/instructions/backend.instructions.md)** - Backend specifics
7. **[.github/instructions/frontend.instructions.md](.github/instructions/frontend.instructions.md)** - Frontend specifics

---

##

---

## Section 2](#section-2)
  - [Subsection 2.1](#subsection-21)
  - [Subsection 2.2](#subsection-22)
- [Section 3](#section-3)

---

## Section 1

...
````

#### Guidelines

- Include all main sections (`## Heading`) and important subsections (`### Heading`)
- Use proper anchor links (lowercase, hyphens for spaces)
- Order TOC items to match document order
- Keep TOC concise but comprehensive
- Separate TOC from document with horizontal rule (`---`)
- Update TOC whenever document structure changes

#### Line Count Thresholds

- **0-100 lines**: Table of contents optional
- **100+ lines**: Table of contents required
- **Very long documents** (500+ lines): Consider breaking into separate documents

### Documentation Content Standards

When generating documentation, follow these principles:

1. **Clarity First**: Write for the primary audience (developers, users, etc.)
2. **Examples**: Include practical examples and code snippets
3. **Completeness**: Cover setup, usage, troubleshooting, and advanced topics
4. **Consistency**: Match existing documentation style and structure
5. **Maintenance**: Include update instructions and version information
6. **Navigation**: Use meaningful headings and cross-references

### Markdown Formatting Standards

- Use consistent heading hierarchy (start with `#`, not `##`)
- Use blank lines between sections
- Use code blocks with language specification (` ```bash `, ` ```java `, etc.)
- Use tables for structured information
- Use lists for sequential or grouped information
- Use blockquotes for important notes or warnings
- Add horizontal rules (`---`) to separate major sections

---

## Code Generation Guidelines

### Backend Code (Java/Spring Boot)

When generating backend code:

1. **Follow Google Java Style Guide** as enforced by Checkstyle
2. **Java Version**: Use Java 25 LTS features and syntax
3. **Spring Boot**: Use latest Spring Boot 3.5.1 features
4. **Testing**: Write JUnit 5 tests for all public methods
5. **Documentation**: Add Javadoc for public APIs
6. **Code Quality**: Ensure Checkstyle compliance before submission

#### Code Generation Example

```java
/**
 * Retrieves and optimizes a resume based on job description.
 *
 * @param jobDescription the target job posting
 * @param resume the current resume content
 * @return optimized resume text
 * @throws IOException if service communication fails
 */
public String optimizeResume(String jobDescription, String resume) throws IOException {
  if (jobDescription == null || jobDescription.isEmpty()) {
    throw new IllegalArgumentException("Job description cannot be empty");
  }

  String prompt = buildPrompt(jobDescription, resume);
  return apiService.callLLM(prompt);
}
```

### Frontend Code (React/TypeScript)

When generating frontend code:

1. **Use React 19** with TypeScript 5.9.3
2. **Functional Components**: Use function declarations, not class components
3. **Custom Hooks**: Extract logic into custom hooks
4. **Type Safety**: Use TypeScript strict mode for all code
5. **PrimeReact**: Use existing PrimeReact components when available
6. **Testing**: Write Vitest tests for components and hooks
7. **Styling**: Use Tailwind CSS utility classes

#### Code Generation Example

```typescript
/**
 * Hook for managing document upload and optimization
 */
export const useDocumentOptimization = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const optimize = useCallback(async (jobDesc: string, resume: string) => {
    setLoading(true);
    setError(null);

    try {
      const response = await apiClient.post("/upload", {
        jobDescription: jobDesc,
        resume: resume,
      });

      return response.data;
    } catch (err) {
      const message = err instanceof Error ? err.message : "Unknown error";
      setError(message);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  return { optimize, loading, error };
};
```

---

## Task Completion Workflow

### Step 1: Analyze Requirements

- Read all requirements carefully
- Check existing code patterns
- Review related documentation
- Identify dependencies and constraints

### Step 2: Plan Implementation

- Create implementation plan
- Identify files to create or modify
- Plan test cases
- Outline documentation updates

### Step 3: Implement Code

- Generate code following project conventions
- Write tests alongside code
- Add inline documentation
- Maintain code quality standards

### Step 4: Validate

- Run all tests: `./gradlew test` (backend), `npm test` (frontend)
- Check code quality: `./gradlew checkstyleMain` (backend), `npm run lint` (frontend)
- Verify no regressions
- Update documentation as needed

### Step 5: Document

- Update README files if user-facing changes
- Update API documentation (Swagger for backend)
- Add inline code comments for complex logic
- Update architecture documentation if needed

### Step 6: Submit

- Ensure all tests pass
- Verify code quality checks pass
- Create descriptive commit message
- Include reference to related issues

---

## Quality Assurance

### Pre-Submission Checklist

Before submitting code changes, verify:

**Backend (Java):**

- [ ] All tests pass: `./gradlew test`
- [ ] Checkstyle compliance: `./gradlew checkstyleMain checkstyleTest`
- [ ] Build succeeds: `./gradlew clean build`
- [ ] Code coverage meets 80%+ target
- [ ] No compiler warnings
- [ ] All public methods have Javadoc
- [ ] No unused imports or variables

**Frontend (React/TypeScript):**

- [ ] All tests pass: `npm test`
- [ ] No TypeScript errors: `npm run type-check`
- [ ] Linting passes: `npm run lint`
- [ ] Build succeeds: `npm run build`
- [ ] Code coverage meets 80%+ target
- [ ] No console errors or warnings
- [ ] No unused imports or variables

**Documentation:**

- [ ] Markdown files with 100+ lines have table of contents
- [ ] Code examples are accurate and tested
- [ ] Links are working
- [ ] Formatting is consistent
- [ ] Spelling and grammar are correct

### Continuous Improvement

- Monitor test results and fix failures immediately
- Address any performance regressions
- Keep dependencies updated
- Review and improve code quality metrics
- Gather feedback and iterate

---

## Code Organization Standards

### Backend Project Structure

```
src/main/java/ca/letkeman/resumes/
├── controller/          # REST API controllers
├── service/             # Business logic
├── model/               # Data models
├── optimizer/           # LLM integration
├── message/             # Response wrappers
├── Config.java          # Configuration
├── Utility.java         # Utilities
└── RestServiceApplication.java
```

### Frontend Project Structure

```
frontend/src/
├── components/          # React components
├── pages/               # Page-level components
├── hooks/               # Custom React hooks
├── services/            # API service layer
├── context/             # Context providers
├── utils/               # Utility functions
├── assets/              # Static assets
├── App.tsx              # Root component
├── main.tsx             # Entry point
└── index.css            # Global styles
```

---

## Testing Standards

### Backend Testing

**All public methods must have unit tests:**

```java
@Test
void test_methodName_withSpecificInput_returnsExpectedOutput() {
  // Arrange
  String input = "test data";

  // Act
  String result = service.methodName(input);

  // Assert
  assertEquals("expected", result);
}
```

**Test coverage targets:**

- Minimum: 80% overall
- Statements: 80%+
- Branches: 75%+
- Methods: 80%+

### Frontend Testing

**Test React components and hooks:**

```typescript
describe('MyComponent', () => {
  it('should render with required props', () => {
    const { getByText } = render(<MyComponent title="Test" />);
    expect(getByText('Test')).toBeInTheDocument();
  });

  it('should handle user interaction correctly', async () => {
    const { getByRole } = render(<MyComponent />);
    const button = getByRole('button');
    await userEvent.click(button);
    // Assert expected behavior
  });
});
```

**Test coverage targets:**

- Minimum: 80% overall
- Critical user paths: 100%
- Error handling: covered
- Edge cases: covered

---

## Naming Conventions

### Backend (Java)

- **Classes**: PascalCase (`ResumeController`, `OptimizeRequest`)
- **Methods**: camelCase (`uploadDocument()`, `getListFiles()`)
- **Variables**: camelCase (`fileName`, `documentContent`)
- **Constants**: UPPER_SNAKE_CASE (`MAX_FILE_SIZE`, `DEFAULT_TIMEOUT`)
- **Packages**: lowercase.with.dots (`ca.letkeman.resumes.controller`)

### Frontend (React/TypeScript)

- **Components**: PascalCase (`ResumeUpload`, `FileHistory`)
- **Hooks**: camelCase starting with `use` (`useDocumentUpload`, `useTheme`)
- **Functions**: camelCase (`formatDate()`, `parseResponse()`)
- **Constants**: UPPER_SNAKE_CASE (`MAX_FILE_SIZE`, `API_TIMEOUT`)
- **Types/Interfaces**: PascalCase (`DocumentFile`, `OptimizeResponse`)

---

## Related Documentation

For more information about project standards and guidelines:

- [copilot-instructions.md](copilot-instructions.md) - Repository-wide developer guidance
- [.github/instructions/backend.instructions.md](.github/instructions/backend.instructions.md) - Backend-specific guidelines
- [.github/instructions/frontend.instructions.md](.github/instructions/frontend.instructions.md) - Frontend-specific guidelines
- [docs/BACKEND_README.md](docs/BACKEND_README.md) - Backend setup and API documentation
- [docs/README.md](docs/README.md) - Frontend setup and component documentation
- [docs/Architecture.md](docs/architecture/ARCHITECTURE.md) - System architecture and design

---

## Version History

| Version | Date       | Changes                                             |
| ------- | ---------- | --------------------------------------------------- |
| 1.0     | 2025-01-17 | Initial creation with core guidelines and standards |

---

## Questions and Feedback

For questions or suggestions about these guidelines:

1. Check existing documentation
2. Review code examples in repository
3. Open an issue on GitHub with `[AGENTS]` prefix
4. Consult with project maintainers

---

**Last Updated**: January 17, 2025
**Next Review**: July 2025
