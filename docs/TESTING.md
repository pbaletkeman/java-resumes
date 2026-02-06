# Testing Guide

Comprehensive testing strategy for java-resumes application.

- [Testing Guide](#testing-guide)
  - [🧪 Testing Overview](#testing-overview)
    - [Coverage Targets](#coverage-targets)
    - [Test Types](#test-types)
  - [🐓 Backend Testing](#backend-testing)
    - [Test Structure](#test-structure)
    - [Running Tests](#running-tests)
    - [Test Categories](#test-categories)
      - [1. Controller Tests (`ResumeControllerTest.java`)](#1-controller-tests-resumecontrollertestjava)
      - [2. Model Tests (`OptimizeTest.java`, `FileInfoTest.java`)](#2-model-tests-optimizetestjava-fileinfotestjava)
      - [3. Service Tests (`FilesStorageServiceTest.java`)](#3-service-tests-filesstorageservicetestjava)
      - [4. Integration Tests (`ApiServiceTest.java`)](#4-integration-tests-apiservicetestjava)
    - [Best Practices](#best-practices)
  - [🎯 Frontend Testing](#frontend-testing)
    - [Test Structure](#test-structure-1)
    - [Running Tests](#running-tests-1)
    - [Test Categories](#test-categories-1)
      - [1. Component Tests](#1-component-tests)
      - [2. Hook Tests](#2-hook-tests)
      - [3. Service Tests](#3-service-tests)
    - [Best Practices](#best-practices-1)
  - [🔗 Integration Testing](#integration-testing)
    - [End-to-End Flow Testing](#end-to-end-flow-testing)
    - [Example Integration Test](#example-integration-test)
  - [📋 Coverage Reports](#coverage-reports)
    - [Backend Coverage](#backend-coverage)
    - [Frontend Coverage](#frontend-coverage)
    - [Coverage Goals](#coverage-goals)
  - [⚠️ Common Test Issues](#common-test-issues)
    - [Issue: Mock Not Working](#issue-mock-not-working)
    - [Issue: Test Timeout](#issue-test-timeout)
    - [Issue: Flaky Tests](#issue-flaky-tests)
  - [🔄 CI/CD Integration](#cicd-integration)
    - [GitHub Actions (Future)](#github-actions-future)

---

## 🧪 Testing Overview

### Coverage Targets

- **Backend**: 80%+ code coverage
- **Frontend**: 80%+ code coverage
- **Integration**: End-to-end API testing
- **E2E**: Browser-based user workflow testing

### Test Types

| Type            | Tool              | Coverage | Purpose                   |
| --------------- | ----------------- | -------- | ------------------------- |
| **Unit Tests**  | JUnit 5 / Vitest  | 50-70%   | Individual method testing |
| **Integration** | Spring Test / RTL | 20-30%   | Component interaction     |
| **E2E**         | Cypress (future)  | 5-10%    | Full workflow testing     |

---

## ⚙️ Backend Testing

### Test Structure

```
src/test/java/ca/letkeman/resumes/
 controller/
    ResumeControllerTest.java
 model/
    OptimizeTest.java
    FileInfoTest.java
 service/
    FilesStorageServiceTest.java
 optimizer/
     ApiServiceTest.java
```

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests ResumeControllerTest

# Run specific test method
./gradlew test --tests ResumeControllerTest.testUploadResume

# Run tests with verbose output
./gradlew test --info

# Generate coverage report
./gradlew test jacocoTestReport
# View at: build/reports/jacoco/test/html/index.html

# Run tests excluding integration tests
./gradlew test -x integrationTest
```

### Test Categories

#### 1. Controller Tests (`ResumeControllerTest.java`)

**Purpose:** Validate REST API endpoints

**Example:**

```java
@SpringBootTest
@AutoConfigureMockMvc
class ResumeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testUploadResume_Success() throws Exception {
        // Setup
        MockMultipartFile file = new MockMultipartFile(
            "file", "resume.pdf", "application/pdf", "content".getBytes());

        // Execute
        mockMvc.perform(multipart("/api/upload").file(file))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testUploadResume_EmptyFile() throws Exception {
        MockMultipartFile empty = new MockMultipartFile(
            "file", "empty.pdf", "application/pdf", new byte[0]);

        mockMvc.perform(multipart("/api/upload").file(empty))
            .andExpect(status().isBadRequest());
    }
}
```

**Tests Included:**

- Valid file upload
- Empty file rejection
- Missing required parameters
- Invalid file types
- Response format validation

#### 2. Model Tests (`OptimizeTest.java`, `FileInfoTest.java`)

**Purpose:** Validate data model behavior

**Example:**

```java
class OptimizeTest {
    @Test
    void testOptimizeModelValidation() {
        Optimize optimize = new Optimize();
        optimize.setResume("Sample resume");
        optimize.setJobDescription("Job desc");

        assertEquals("Sample resume", optimize.getResume());
        assertEquals("Job desc", optimize.getJobDescription());
    }

    @Test
    void testOptimizeWithNullValues() {
        Optimize optimize = new Optimize();
        assertNull(optimize.getResume());
        assertNull(optimize.getJobDescription());
    }
}
```

**Tests Included:**

- Getter/setter functionality
- Validation rules
- Default values
- Serialization/deserialization

#### 3. Service Tests (`FilesStorageServiceTest.java`)

**Purpose:** Validate business logic

**Example:**

```java
@SpringBootTest
class FilesStorageServiceTest {
    @Autowired
    private FilesStorageService service;

    @Test
    void testSaveFile() throws IOException {
        // Setup
        MultipartFile file = new MockMultipartFile(
            "test.pdf", "test.pdf", "application/pdf", "content".getBytes());

        // Execute
        Path path = service.save(file);

        // Assert
        assertNotNull(path);
        assertTrue(Files.exists(path));

        // Cleanup
        Files.delete(path);
    }

    @Test
    void testDeleteFile() throws IOException {
        // Create test file
        Path path = Files.createTempFile("test", ".pdf");

        // Execute delete
        service.delete(path.getFileName().toString());

        // Assert
        assertFalse(Files.exists(path));
    }
}
```

**Tests Included:**

- File save operations
- File retrieval
- File deletion
- Error handling
- File permissions

#### 4. Integration Tests (`ApiServiceTest.java`)

**Purpose:** Validate external service integration

**Example:**

```java
@SpringBootTest
class ApiServiceTest {
    @Autowired
    private ApiService apiService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void testLLMIntegration() {
        // Mock response
        LLMResponse response = new LLMResponse();
        response.setChoices(Arrays.asList(
            new Choice("Optimized resume content")));

        when(restTemplate.postForObject(
            anyString(), any(), any())).thenReturn(response);

        // Execute
        String result = apiService.produceFiles("resume", "job desc");

        // Assert
        assertNotNull(result);
        assertTrue(result.length() > 0);

        // Verify
        verify(restTemplate, times(1)).postForObject(
            anyString(), any(), any());
    }
}
```

**Tests Included:**

- LLM API communication
- Response parsing
- Error handling
- Retry logic
- Timeout handling

### Best Practices

**Setup & Teardown:**

```java
@BeforeEach
void setUp() {
    // Initialize test data
}

@AfterEach
void tearDown() {
    // Clean up resources
}
```

**Mocking External Dependencies:**

```java
@MockBean
private FilesStorageService mockService;

when(mockService.save(any())).thenReturn(Paths.get("file.pdf"));
```

**Assertions:**

```java
assertEquals(expected, actual);
assertTrue(condition);
assertThrows(Exception.class, () -> methodThatThrows());
```

---

## 🎨 Frontend Testing

### Test Structure

```
frontend/tests/
 components/
    MainContentTab.test.tsx
    FileHistory.test.tsx
    ThemeToggle.test.tsx
 hooks/
    useApi.test.ts
    useTheme.test.ts
 services/
     fileService.test.ts
```

### Running Tests

```bash
cd frontend

# Run all tests
npm run test

# Watch mode (rerun on changes)
npm run test --watch

# Run specific test file
npm run test -- components/MainContentTab.test.tsx

# Run tests matching pattern
npm run test -- --grep "upload"

# Generate coverage report
npm run test:coverage
# View at: frontend/coverage/index.html

# Debug tests
npm run test -- --inspect-brk
```

### Test Categories

#### 1. Component Tests

**Purpose:** Validate React component behavior

**Example:**

```tsx
import { render, screen } from "@testing-library/react";
import MainContentTab from "../MainContentTab";

describe("MainContentTab", () => {
  it("should render upload form", () => {
    render(<MainContentTab />);

    expect(screen.getByText(/Upload Resume/i)).toBeInTheDocument();
    expect(
      screen.getByPlaceholderText(/Paste job description/i),
    ).toBeInTheDocument();
  });

  it("should handle file upload", async () => {
    render(<MainContentTab />);

    const input = screen.getByLabelText(/Upload/i);
    fireEvent.change(input, {
      target: { files: [new File(["test"], "test.txt")] },
    });

    await waitFor(() => {
      expect(input.files[0].name).toBe("test.txt");
    });
  });
});
```

**Tests Included:**

- Component rendering
- Props validation
- Event handling
- State updates
- Conditional rendering

#### 2. Hook Tests

**Purpose:** Validate custom React hooks

**Example:**

```ts
import { renderHook, act } from "@testing-library/react";
import { useTheme } from "../useTheme";

describe("useTheme", () => {
  it("should initialize with default theme", () => {
    const { result } = renderHook(() => useTheme());

    expect(result.current.theme).toBe("light");
  });

  it("should toggle theme", () => {
    const { result } = renderHook(() => useTheme());

    act(() => {
      result.current.toggleTheme();
    });

    expect(result.current.theme).toBe("dark");
  });

  it("should persist theme preference", () => {
    const { result } = renderHook(() => useTheme());

    act(() => {
      result.current.toggleTheme();
    });

    expect(localStorage.getItem("theme")).toBe("dark");
  });
});
```

**Tests Included:**

- Hook initialization
- State updates
- Side effects
- LocalStorage interaction
- Error handling

#### 3. Service Tests

**Purpose:** Validate API service functions

**Example:**

```ts
import axios from "axios";
import { fileService } from "../fileService";

jest.mock("axios");

describe("fileService", () => {
  it("should list files", async () => {
    const mockFiles = [{ filename: "resume.md", size: 2048 }];

    (axios.get as jest.Mock).mockResolvedValue({
      data: mockFiles,
    });

    const result = await fileService.listFiles();

    expect(result).toEqual(mockFiles);
    expect(axios.get).toHaveBeenCalledWith("/api/files");
  });

  it("should handle API errors", async () => {
    (axios.get as jest.Mock).mockRejectedValue(new Error("Network error"));

    await expect(fileService.listFiles()).rejects.toThrow("Network error");
  });
});
```

**Tests Included:**

- API calls
- Response handling
- Error scenarios
- Request validation
- Timeout handling

### Best Practices

**Setup & Teardown:**

```ts
beforeEach(() => {
  // Reset mocks
  jest.clearAllMocks();
});

afterEach(() => {
  // Cleanup
  jest.resetAllMocks();
});
```

**Mocking API Calls:**

```ts
jest.mock("axios");

(axios.post as jest.Mock).mockResolvedValue({
  data: { success: true },
});
```

**Async Testing:**

```ts
it("should handle async operations", async () => {
  const result = await asyncFunction();
  expect(result).toBeDefined();
});
```

---

## 🔄 Integration Testing

### End-to-End Flow Testing

**Backend + Frontend Together:**

```bash
# Terminal 1: Start backend
./gradlew bootRun

# Terminal 2: Start frontend
npm run dev

# Terminal 3: Run integration tests
npm run test:integration
```

### Example Integration Test

```bash
# Test: Complete resume optimization workflow

1. User opens application (Frontend)
2. User uploads resume (Frontend  API)
3. Backend receives upload (Spring Boot)
4. Backend sends to LLM (Spring Boot  Ollama)
5. LLM returns optimized resume (Ollama  Spring Boot)
6. Backend saves files (Spring Boot)
7. Frontend polls for results (Frontend  API)
8. Frontend displays optimized resume (React)
9. User downloads file (Frontend  API)
```

---

## 📊 Coverage Reports

### Backend Coverage

```bash
./gradlew jacocoTestReport
```

**View Report:**

```
build/reports/jacoco/test/html/index.html
```

**Coverage Areas:**

- Line coverage (statements executed)
- Branch coverage (conditionals tested)
- Method coverage (public methods tested)

### Frontend Coverage

```bash
npm run test:coverage
```

**View Report:**

```
frontend/coverage/index.html
```

**Coverage Summary:**

```
Statements   : X% ( X/Y )
Branches     : X% ( X/Y )
Functions    : X% ( X/Y )
Lines        : X% ( X/Y )
```

### Coverage Goals

| Metric     | Target | Current |
| ---------- | ------ | ------- |
| Statements | 80%    | TBD     |
| Branches   | 75%    | TBD     |
| Functions  | 80%    | TBD     |
| Lines      | 80%    | TBD     |

---

## 🐛 Common Test Issues

### Issue: Mock Not Working

```java
// Wrong: @Mock not in @ExtendWith(MockitoExtension.class)
class TestClass {
    @Mock
    private Service service;
}

// Correct:
@ExtendWith(MockitoExtension.class)
class TestClass {
    @Mock
    private Service service;
}
```

### Issue: Test Timeout

```java
// Add timeout
@Test
@Timeout(5) // 5 seconds
void testWithTimeout() {
    // test code
}
```

### Issue: Flaky Tests

```java
// Avoid timing-dependent tests
// Bad:
Thread.sleep(1000);
assertTrue(condition);

// Good:
await().atMost(5, SECONDS).until(() -> condition);
```

---

## 🔄 CI/CD Integration

### GitHub Actions (Future)

**Workflow file:** `.github/workflows/test.yml`

```yaml
name: Tests

on: [push, pull_request]

jobs:
  backend:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: "21"
      - run: ./gradlew build
      - run: ./gradlew jacocoTestReport

  frontend:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with:
          node-version: "22"
      - run: npm install
      - run: npm run test
      - run: npm run test:coverage
```

---

**See also:**

- [Development Setup](DEVELOPMENT_SETUP.md) - Environment setup
- [Code Quality](CODE_QUALITY.md) - Testing standards
- [Troubleshooting](TROUBLESHOOTING.md) - Test troubleshooting
