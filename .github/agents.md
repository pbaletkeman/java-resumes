# AI Agent Guidelines - java-resumes

Comprehensive guidelines for AI agents (GitHub Copilot, Claude, etc.) working on java-resumes project tasks.

## Overview

This document provides AI-specific guidance for completing tasks on the java-resumes project. It complements the repository-wide [copilot-instructions.md](../copilot-instructions.md) and path-specific instructions in [instructions/](./instructions/).

---

## Task Completion Workflow

### Phase 1: Understand the Task

1. **Read the request carefully** - Identify exact requirements and constraints
2. **Review relevant documentation** - Check [docs/](../docs/), path-specific instructions, and code samples
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

- Create DTOs if needed in `src/main/java/com/model/`
- Use snake_case for JSON field names: `@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)`
- Add field validation annotations

**Step 2: Implement service layer**

- Create or extend service in `src/main/java/com/service/`
- Add business logic and error handling
- Write unit tests in `src/test/java/com/service/`

**Step 3: Add controller endpoint**

- Add method to `ResumeController.java`
- Use appropriate HTTP method (@PostMapping, @GetMapping, etc.)
- Include @CrossOrigin for React frontend
- Add proper error handling and logging

**Step 4: Test the endpoint**

- Write unit tests in `src/test/java/com/controller/ResumeControllerTest.java`
- Test success and error cases
- Verify HTTP status codes

**Step 5: Validate**

```bash
./gradlew clean test
./gradlew checkstyleMain checkstyleTest
./gradlew clean build
```

### Code Quality Requirements

1. **Checkstyle**: Zero violations (run `./gradlew checkstyleMain`)
2. **Testing**: 80%+ coverage required
3. **Types**: Proper type annotations on all methods
4. **Imports**: No unused imports
5. **Comments**: JSDoc for public methods, explain WHY not WHAT

### Common Patterns

**REST Endpoint:**

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

**Service Method:**

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

**Unit Test:**

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
- Plan internal state (use useState hooks)
- Identify API calls needed (use useApi hook)

**Step 2: Create the component**

- Add component file to `frontend/src/components/`
- Use TypeScript with strict types
- Use Tailwind CSS for styling
- Handle loading and error states

**Step 3: Write the test**

- Create test file: `ComponentName.test.tsx`
- Test rendering, user interactions, API calls
- Target 80%+ coverage

**Step 4: Integrate into app**

- Import and use in parent component
- Update routing if necessary
- Verify styling and responsiveness

**Step 5: Validate**

```bash
npm run type-check
npm test
npm run lint
npm run build
```

### Code Quality Requirements

1. **TypeScript**: Strict mode, no `any` types
2. **Testing**: 80%+ coverage for components and hooks
3. **Linting**: No ESLint errors or warnings
4. **Types**: Proper interface definitions for all props/state
5. **Comments**: TSDoc for exported functions/components

### Common Patterns

**Component:**

```tsx
interface ComponentProps {
  title: string;
  onSubmit?: (value: string) => void;
}

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
```

**Custom Hook:**

```tsx
export const useCustom = (param: string) => {
  const [state, setState] = useState<string>("initial");

  useEffect(() => {
    setState(param);
  }, [param]);

  return { state };
};
```

**Component Test:**

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
      />
    );

    fireEvent.click(screen.getByText("Submit"));
    expect(onSubmit).toHaveBeenCalled();
  });
});
```

---

## Pre-Commit Validation

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

```bash
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
```

---

## Documentation

**Key documentation files:**

1. **[docs/INDEX.md](../docs/INDEX.md)** - Complete documentation index
2. **[docs/Architecture.md](../docs/Architecture.md)** - System architecture
3. **[docs/BACKEND_README.md](../docs/BACKEND_README.md)** - Backend guide
4. **[docs/README.md](../docs/README.md)** - Frontend guide
5. **[copilot-instructions.md](../copilot-instructions.md)** - Repository-wide guidance
6. **[instructions/backend.instructions.md](./instructions/backend.instructions.md)** - Backend specifics
7. **[instructions/frontend.instructions.md](./instructions/frontend.instructions.md)** - Frontend specifics

---

## Best Practices for AI Agents

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

---

## Support & Escalation

**If you encounter issues:**

1. **Check the docs** - Review [docs/INDEX.md](../docs/INDEX.md)
2. **Review existing code** - Look at similar implementations
3. **Read error messages** - They often indicate the exact problem
4. **Validate your changes** - Run full test suite and quality checks
5. **Escalate if needed** - Document the issue and ask for human review

---

## Summary

Successfully completing tasks on java-resumes requires:

✅ Understanding requirements clearly
✅ Following the workflow phases (Understand → Plan → Implement → Validate → Document)
✅ Adhering to code quality standards (Checkstyle 100%, tests 80%+, TypeScript strict)
✅ Writing comprehensive tests (both backend and frontend)
✅ Updating documentation for architecture/process changes
✅ Running all validation checks before completing task
✅ Keeping changes minimal and focused

Reference [copilot-instructions.md](../copilot-instructions.md) and [docs/](../docs/) for detailed guidance.

---

**Last Updated**: 2024
**For**: GitHub Copilot, Claude, and other AI agents
**Repository**: https://github.com/pbaletkeman/java-resumes
