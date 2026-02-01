# Test Coverage 80% Roadmap

## Executive Summary

This document provides a comprehensive roadmap to achieve 80%+ test coverage for both frontend and backend of the java-resumes application. All test infrastructure is in place and working. This roadmap defines exactly what tests need to be added to reach the 80% target.

**Current Status:**
- **Backend**: 57% coverage, 94 tests passing
- **Frontend**: Tests running, 8/16 passing, coverage TBD
- **Infrastructure**: ✅ Complete

**Target:**
- **Backend**: 80%+ coverage
- **Frontend**: 80%+ coverage

---

## Backend Roadmap (57% → 80%+)

### Current Coverage by Package

| Package | Coverage | Lines | Target | Gap |
|---------|----------|-------|--------|-----|
| ca.letkeman.resumes.optimizer | 54% | 364 | 80% | +26% |
| ca.letkeman.resumes.controller | 58% | 185 | 80% | +22% |
| ca.letkeman.resumes.service | 63% | 212 | 80% | +17% |
| ca.letkeman.resumes | 55% | 55 | 80% | +25% |
| **Total** | **57%** | **816** | **80%** | **+23%** |

### Required Tests: ~40 Tests

---

## 1. ApiService Error Handling Tests (+10% coverage)

**File**: `src/test/java/ca/letkeman/resumes/optimizer/ApiServiceTest.java`

### Test Cases to Add (12-15 tests)

```java
@Test
void testInvokeApi_withConnectionTimeout() {
    // Mock HTTP connection timeout
    // Verify retry logic
    // Verify error handling
}

@Test
void testInvokeApi_withSocketTimeout() {
    // Mock socket timeout during read
    // Verify timeout handling
}

@Test
void testInvokeApi_withMalformedJsonResponse() {
    // Mock invalid JSON from LLM
    // Verify parsing error handling
}

@Test
void testInvokeApi_with4xxClientError() {
    // Mock 400, 401, 403, 404 responses
    // Verify error handling for each
}

@Test
void testInvokeApi_with5xxServerError() {
    // Mock 500, 502, 503 responses
    // Verify error handling
}

@Test
void testInvokeApi_withNetworkInterruption() {
    // Mock IOException during request
    // Verify graceful failure
}

@Test
void testInvokeApi_withEmptyResponse() {
    // Mock empty response body
    // Verify handling
}

@Test
void testInvokeApi_withNullEndpoint() {
    // Test with null endpoint URL
    // Verify validation
}

@Test
void testInvokeApi_withInvalidEndpoint() {
    // Test with malformed URL
    // Verify validation
}

@Test
void testProduceFiles_withLLMFailure() {
    // Mock LLM API failure
    // Verify error propagation
}

@Test
void testProduceFiles_withFileWriteFailure() {
    // Mock file system error
    // Verify error handling
}

@Test
void testSendHttpRequest_withRetrySuccess() {
    // Mock failure then success
    // Verify retry mechanism
}
```

**Estimated Impact**: +10% coverage  
**Time**: 6-8 hours

---

## 2. Controller Integration Tests (+8% coverage)

**Files**: 
- `src/test/java/ca/letkeman/resumes/controller/ResumeControllerTest.java`
- New: `src/test/java/ca/letkeman/resumes/controller/InterviewEndpointsIntegrationTest.java`

### Test Cases to Add (15-20 tests)

```java
// Test all 6 new interview/networking endpoints

@Test
void testInterviewHrQuestions_successFlow() {
    // POST to /api/generate/interview-hr-questions
    // Verify 202 Accepted response
    // Verify background task spawned
    // Verify database entry created
}

@Test
void testInterviewHrQuestions_withInvalidRequest() {
    // Missing required fields
    // Verify 417 Expectation Failed
}

@Test
void testInterviewJobSpecific_successFlow() {
    // POST to /api/generate/interview-job-specific
    // Full workflow verification
}

@Test
void testInterviewReverse_successFlow() {
    // POST to /api/generate/interview-reverse
    // Full workflow verification
}

@Test
void testColdEmail_successFlow() {
    // POST to /api/generate/cold-email
    // Full workflow verification
}

@Test
void testColdLinkedInMessage_successFlow() {
    // POST to /api/generate/cold-linkedin-message
    // Full workflow verification
}

@Test
void testThankYouEmail_successFlow() {
    // POST to /api/generate/thank-you-email
    // Full workflow verification
}

@Test
void testThankYouEmail_withoutInterviewerName() {
    // Test optional field handling
}

@Test
void testAllEndpoints_withNullOptimize() {
    // Test validation for null request body
    // Verify 400 Bad Request
}

@Test
void testAllEndpoints_withEmptyJobDescription() {
    // Test validation for required field
    // Verify 417 Expectation Failed
}

@Test
void testAllEndpoints_verifyAsyncProcessing() {
    // Verify controller returns immediately
    // Verify background thread spawned
    // Verify no blocking
}

@Test
void testProcessPromptRequest_withDatabaseFailure() {
    // Mock database error
    // Verify error handling
}
```

**Estimated Impact**: +8% coverage  
**Time**: 8-10 hours

---

## 3. PromptService Database Tests (+5% coverage)

**File**: `src/test/java/ca/letkeman/resumes/service/PromptServiceTest.java`

### Test Cases to Add (8-10 tests)

```java
@Test
void testSavePromptToHistory_success() {
    // Create PromptHistory entity
    // Call savePromptToHistory()
    // Verify database entry
}

@Test
void testSavePromptToHistory_withNullValues() {
    // Test with optional fields null
    // Verify graceful handling
}

@Test
void testSavePromptToHistory_withDatabaseError() {
    // Mock repository save failure
    // Verify error handling
}

@Test
void testGetAllHistory_withResults() {
    // Add test data
    // Call getAllHistory()
    // Verify results
}

@Test
void testGetAllHistory_empty() {
    // Empty database
    // Verify empty list returned
}

@Test
void testGetHistoryByType_filtering() {
    // Add mixed prompt types
    // Filter by specific type
    // Verify correct results
}

@Test
void testGetHistoryById_found() {
    // Add entry
    // Find by ID
    // Verify result
}

@Test
void testGetHistoryById_notFound() {
    // Search for non-existent ID
    // Verify Optional.empty()
}

@Test
void testDeleteHistoryById_success() {
    // Add entry
    // Delete by ID
    // Verify deletion
}

@Test
void testExpandPrompt_withAllVariables() {
    // Test variable substitution
    // Verify all placeholders replaced
}
```

**Estimated Impact**: +5% coverage  
**Time**: 4-6 hours

---

## 4. Additional Backend Tests (misc)

### Config Loading Tests
```java
@Test
void testConfigLoading_validFile() {
    // Load config.json
    // Verify fields populated
}

@Test
void testConfigLoading_invalidFile() {
    // Test with malformed JSON
    // Verify error handling
}
```

### Utility Tests
```java
@Test
void testReadFileAsString_success() {
    // Read existing file
    // Verify content
}

@Test
void testReadFileAsString_fileNotFound() {
    // Test with non-existent file
    // Verify exception
}
```

### BackgroundResume Tests
```java
@Test
void testRun_withException() {
    // Mock exception during processing
    // Verify error logged
}
```

**Estimated Impact**: Combined ~5% coverage  
**Time**: 4-6 hours

---

## Backend Summary

**Total Tests to Add**: ~40 tests  
**Total Time**: 22-30 hours  
**Expected Final Coverage**: 82-84%  
**Status**: Exceeds 80% target ✅

---

## Frontend Roadmap (Current → 80%+)

### Current Status

- **Tests Existing**: 16 (8 passing, 8 failing)
- **Coverage**: Not yet measurable (need all tests passing)
- **Infrastructure**: ✅ Working

### Required Work

1. **Fix 8 Failing Tests** (4-6 hours)
2. **Add ~30 New Tests** (15-20 hours)

---

## 1. Fix Existing Tests (8 failures)

### SubmissionDialog Tests (5 failures)

**File**: `frontend/src/__tests__/components/SubmissionDialog.test.tsx`

```typescript
// Update test assertions to match current component

it('should render the dialog when visible is true', () => {
  render(<SubmissionDialog visible={true} onHide={mockOnHide} />);
  // Update: Title changed from "Important: Before Submission" to "⚠️ Before Submission"
  expect(screen.getByText('⚠️ Before Submission')).toBeInTheDocument();
});

it('should display markdown cleanup warning', () => {
  render(<SubmissionDialog visible={true} onHide={mockOnHide} />);
  // Update: Text changed to "LLM Response Cleanup"
  expect(screen.getByText('LLM Response Cleanup')).toBeInTheDocument();
});

// Remove tests for content that no longer exists:
// - DOCX conversion announcement (removed from component)
// - Specific markdown issue examples (simplified in component)
```

### MarkdownToDocxForm Tests (3 failures)

**File**: `frontend/src/__tests__/components/MarkdownToDocxForm.test.tsx`

```typescript
// Fix PrimeReact button queries

it('should have Convert button that starts disabled', () => {
  renderComponent();
  // Update: Query for actual button element, not span
  const convertBtn = screen.getByRole('button', { name: /convert/i });
  expect(convertBtn).toBeDisabled();
});

it('should have Download button that starts disabled', () => {
  renderComponent();
  // Update: Query for actual button element
  const downloadBtn = screen.getByRole('button', { name: /download/i });
  expect(downloadBtn).toBeDisabled();
});
```

**Estimated Time**: 4-6 hours

---

## 2. Add Component Tests (~15 tests)

### MainContentTab Tests

**File**: `frontend/src/__tests__/components/MainContentTab.test.tsx`

```typescript
describe('MainContentTab', () => {
  it('should render document upload form', () => {});
  it('should display company name input', () => {});
  it('should display job title input', () => {});
  it('should handle prompt type selection', () => {});
  it('should submit form with valid data', () => {});
});
```

### DocumentUploadForm Tests

**File**: `frontend/src/__tests__/components/DocumentUploadForm.test.tsx`

```typescript
describe('DocumentUploadForm', () => {
  it('should render all required fields', () => {});
  it('should validate form inputs', () => {});
  it('should handle file uploads', () => {});
  it('should display validation errors', () => {});
});
```

### MarkdownToPdfForm Tests

**File**: `frontend/src/__tests__/components/MarkdownToPdfForm.test.tsx`

```typescript
describe('MarkdownToPdfForm', () => {
  it('should render markdown to PDF converter', () => {});
  it('should handle file selection', () => {});
  it('should convert markdown to PDF', () => {});
});
```

### Navbar Tests

**File**: `frontend/src/__tests__/components/Navbar.test.tsx`

```typescript
describe('Navbar', () => {
  it('should render navigation items', () => {});
  it('should toggle theme', () => {});
  it('should highlight active tab', () => {});
});
```

**Estimated Time**: 8-10 hours

---

## 3. Add Hook Tests (~8 tests)

### useApi Hook Tests

**File**: `frontend/src/__tests__/hooks/useApi.test.ts`

```typescript
describe('useApi', () => {
  it('should handle successful API call', () => {});
  it('should handle API error', () => {});
  it('should manage loading state', () => {});
  it('should abort on unmount', () => {});
});
```

### useTheme Hook Tests

**File**: `frontend/src/__tests__/hooks/useTheme.test.ts`

```typescript
describe('useTheme', () => {
  it('should toggle between light and dark', () => {});
  it('should persist theme to localStorage', () => {});
});
```

### useFileManagement Hook Tests

**File**: `frontend/src/__tests__/hooks/useFileManagement.test.ts`

```typescript
describe('useFileManagement', () => {
  it('should load files list', () => {});
  it('should delete file', () => {});
});
```

**Estimated Time**: 4-6 hours

---

## 4. Add Service Tests (~7 tests)

### fileService Tests

**File**: `frontend/src/__tests__/services/fileService.test.ts`

```typescript
describe('fileService', () => {
  it('should upload file', () => {});
  it('should download file', () => {});
  it('should convert markdown to PDF', () => {});
  it('should handle API errors', () => {});
});
```

### API Service Tests

**File**: `frontend/src/__tests__/services/api.test.ts`

```typescript
describe('api', () => {
  it('should configure axios instance', () => {});
  it('should add auth headers', () => {});
  it('should handle network errors', () => {});
});
```

**Estimated Time**: 3-5 hours

---

## Frontend Summary

**Fix Existing**: 8 tests (4-6 hours)  
**Add New**: ~30 tests (15-20 hours)  
**Total Time**: 19-26 hours  
**Expected Final Coverage**: 80%+  
**Status**: Achievable ✅

---

## Overall Timeline

### Week 1 (Priority)
- ✅ Fix 8 failing frontend tests (Day 1-2)
- ✅ Add ApiService error tests (Day 3-4)
- ✅ Add PromptService database tests (Day 5)

### Week 2 (Backend Focus)
- ✅ Add controller integration tests (Day 1-3)
- ✅ Add remaining backend tests (Day 4-5)
- ✅ Verify 80%+ backend coverage

### Week 3 (Frontend Focus)
- ✅ Add component tests (Day 1-3)
- ✅ Add hook tests (Day 4)
- ✅ Add service tests (Day 5)

### Week 4 (Verification)
- ✅ Run full test suites
- ✅ Generate coverage reports
- ✅ Verify 80%+ on both
- ✅ Document results
- ✅ Add CI/CD coverage gates

---

## Build Commands

### Backend
```bash
# Run tests
./gradlew clean test

# Generate coverage report
./gradlew jacocoTestReport

# View report
open build/reports/jacoco/test/html/index.html

# Verify 80% threshold
./gradlew jacocoTestCoverageVerification
```

### Frontend
```bash
cd frontend

# Run tests
npm run test

# Run with coverage
npm run test:coverage

# View coverage report
open coverage/index.html
```

---

## Success Criteria

### Backend
- [ ] 80%+ overall coverage
- [ ] All packages above 75%
- [ ] 130+ tests passing
- [ ] Build passes with coverage verification

### Frontend
- [ ] 80%+ overall coverage
- [ ] All components above 75%
- [ ] 45+ tests passing
- [ ] Build passes with coverage verification

### Both
- [ ] All tests passing (green)
- [ ] No flaky tests
- [ ] Coverage reports generated
- [ ] CI/CD gates in place

---

## Risk Mitigation

### Potential Issues

1. **Time Constraints**
   - Mitigation: Prioritize high-impact tests first
   - Focus on Optimizer and Controller packages

2. **Mock Complexity**
   - Mitigation: Reuse existing mock patterns
   - Reference MockLlmService as template

3. **Integration Test Challenges**
   - Mitigation: Use H2 database (already configured)
   - Mock external dependencies

4. **Frontend Component Complexity**
   - Mitigation: Start with simple component tests
   - Use testing-library best practices

---

## Maintenance

### After Reaching 80%

1. **Add Coverage Gates to CI/CD**
   ```yaml
   # GitHub Actions
   - name: Verify Coverage
     run: |
       ./gradlew jacocoTestCoverageVerification
       cd frontend && npm run test:coverage -- --threshold 80
   ```

2. **Require Tests for New Code**
   - All PRs must maintain 80%+ coverage
   - Add pre-commit hooks for test verification

3. **Regular Review**
   - Monthly coverage review
   - Quarterly test maintenance
   - Update tests when components change

---

## Resources

### Documentation
- Backend: `TEST_COVERAGE_FINAL_REPORT.md`
- Frontend: `frontend/README.md`
- Testing: `TESTING_AND_DOCKER_STATUS.md`

### Tools
- Backend: JaCoCo, JUnit 5, Mockito
- Frontend: Vitest, Testing Library, Coverage-v8

### Examples
- `src/test/java/ca/letkeman/resumes/service/MockLlmServiceTest.java`
- `frontend/src/__tests__/components/AdditionalToolsTab.test.tsx`

---

## Conclusion

This roadmap provides a clear, actionable path to 80%+ test coverage for both frontend and backend. All infrastructure is in place. The project is ready for test development.

**Estimated Total Time**: 41-56 hours  
**Expected Result**: 80%+ coverage on both frontend and backend  
**Status**: Ready to Execute ✅

---

**Last Updated**: January 31, 2026  
**Next Review**: After reaching 80% coverage
