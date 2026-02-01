# Backend Test Coverage Analysis: Path to 83%

## Current Status
- **Current Coverage**: 80% instruction, 71% branch
- **Target**: >83% instruction coverage
- **Gap**: 3% instruction coverage needed

## Coverage Breakdown by Package

| Package | Instructions | Branch | Priority |
|---------|--------------|--------|----------|
| ca.letkeman.resumes | 92% | 80% | ✅ Good |
| ca.letkeman.resumes.optimizer | 81% | 68% | Medium |
| ca.letkeman.resumes.controller | 77% | 72% | High |
| ca.letkeman.resumes.service | 76% | 73% | High |

## Top 3 Lowest Coverage Areas (Problem Statement Focus)

### 1. PromptService - 51% Coverage ⚠️ CRITICAL
**Missed Instructions**: 152  
**Current Tests**: 39

**Untested Code**:
- `loadFromExternalDirectory()` - filesystem operations
- `savePromptToHistory()` with actual repository
- `getHistoryByCompany()` with actual data
- `getHistoryByDateRange()` with actual data

**Why Not Tested**:
- Requires filesystem mocking for external directory
- Requires Spring repository mocking for database ops
- Current tests only hit null repository paths

**To Improve**:
- Add @MockBean for PromptHistoryRepository
- Test actual save/retrieve operations
- Mock filesystem for external directory tests
- **Expected gain**: +10-15% PromptService → +1.5% overall

### 2. ApiService - 73% Coverage
**Missed Instructions**: 207 (highest absolute)  
**Current Tests**: Multiple test files

**Untested Code**:
- HTTP error responses (404, 500, timeout)
- Connection failure scenarios
- Large payload handling
- Retry logic paths
- Error message formatting

**Why Not Tested**:
- Requires HTTP client mocking
- Complex setup for error scenarios
- Need mock server or WireMock

**To Improve**:
- Add MockWebServer or WireMock
- Test error response codes
- Test connection timeouts
- **Expected gain**: +12% ApiService → +1.5% overall

### 3. ResumeController - 77% Coverage
**Missed Instructions**: 187  
**Current Tests**: ResumeControllerTest, ResumeControllerAdvancedTest

**Untested Code**:
- Some endpoint error scenarios
- Specific validation paths
- Edge cases in file operations

**Why Not Tested**:
- Not all endpoints have error tests
- Some validation branches not exercised

**To Improve**:
- Add more @SpringBootTest integration tests
- Test all error response paths
- Add validation failure tests
- **Expected gain**: +8% ResumeController → +0.5% overall

## Detailed Analysis

### Current Test Suite
- **Total Tests**: 343 (@Test methods)
- **Passing**: 343 (100%)
- **Execution Time**: ~30 seconds

### Test Distribution
- Controller tests: ~40
- Service tests: ~50  
- Optimizer tests: ~50
- Model/Entity tests: ~100
- Other: ~100

### Why 80% and Not Higher?

The remaining 20% consists of:
1. **Error paths** (30%): Exception handling, HTTP errors
2. **Database operations** (25%): Actual repo operations
3. **Filesystem operations** (20%): External file loading
4. **Complex conditions** (15%): Nested if/else, retries
5. **Defensive code** (10%): Null checks, validations

## Path to 83% Coverage

### Required Work

**Estimated Tests Needed**: 20-25 additional tests  
**Estimated Time**: 8-12 hours  
**Complexity**: Moderate to High (requires mocking)

### Approach 1: Focus on PromptService (Highest Impact)
1. Add @SpringBootTest for PromptServiceTest
2. Mock PromptHistoryRepository
3. Test actual database save/retrieve operations
4. Test external directory loading with temp files

**Tests to Add**: 8-10  
**Expected Gain**: +1.5%  
**Difficulty**: Moderate

### Approach 2: Add ApiService HTTP Mocking
1. Add MockWebServer dependency
2. Test HTTP error responses (404, 500, timeout)
3. Test connection failures
4. Test retry logic

**Tests to Add**: 10-12  
**Expected Gain**: +1.5%  
**Difficulty**: High (complex mocking)

### Approach 3: Expand Controller Tests
1. Add more endpoint error scenarios
2. Test validation failures
3. Test file upload edge cases

**Tests to Add**: 5-8  
**Expected Gain**: +0.5-1%  
**Difficulty**: Moderate

### Combined Approach (Recommended)
- PromptService: 5 tests (+0.8%)
- ApiService: 8 tests (+1.2%)
- ResumeController: 5 tests (+0.5%)
- Other: 5 tests (+0.5%)
- **Total**: 23 tests, +3% coverage = 83%

## What Was Done So Far

### Recent Additions
- Added 7 PromptService tests (32 → 39)
- Tests focus on error paths and edge cases
- **Result**: No coverage increase (already-covered paths)

### Why No Increase?
The tests added were for:
- Null repository checks (already covered)
- Error handling (already covered)
- Edge cases (already covered)

**Lesson**: Need to target UNCOVERED code, not add more tests for covered code.

## Technical Challenges

### 1. Mocking Complex Dependencies
- HTTP clients require MockWebServer or WireMock
- Database requires @SpringBootTest + @MockBean
- Filesystem requires temp directories or mocking

### 2. Test Setup Complexity
- Spring context initialization
- Mock server configuration
- Temporary file management

### 3. Test Reliability
- Ensure tests are deterministic
- Avoid flaky tests
- Proper cleanup

## Recommendations

### Short Term (Reach 83%)
1. **Focus on PromptService** with mocked repository (5 tests, +0.8%)
2. **Add ApiService error tests** with MockWebServer (8 tests, +1.2%)
3. **Add Controller edge cases** (5 tests, +0.5%)
4. **Add FilesStorageService tests** (5 tests, +0.5%)

### Long Term (Reach 85%+)
1. Comprehensive integration tests
2. End-to-end workflow tests  
3. Performance tests with coverage
4. Mutation testing for quality

## Conclusion

**Current**: 80% coverage with 343 tests  
**Target**: 83% coverage  
**Gap**: 3% (need ~23 targeted tests)  
**Effort**: 8-12 hours of focused work  
**Complexity**: Moderate to High (requires proper mocking)

The key is not to add more tests, but to add the RIGHT tests that target uncovered code paths. This requires:
1. Analyzing JaCoCo reports for missed instructions
2. Setting up proper mocking infrastructure
3. Writing tests for error paths and complex scenarios

**Status**: Documented ✅ | Path Forward Clear ✅ | Ready for Implementation 🚀
