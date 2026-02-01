# Test Coverage Status and Implementation Plan

## Executive Summary

**Date**: January 31, 2026  
**Goal**: Achieve >80% test coverage for both frontend and backend  
**Current Status**: Infrastructure complete, tests passing, coverage below target

---

## Current Coverage Status

### Backend: 57% Coverage ⚠️
- **Tests**: 94/94 passing ✅
- **Target**: 80%
- **Gap**: +23 percentage points
- **Required**: ~40-50 additional tests

#### Coverage by Package:
| Package | Current | Target | Gap | Priority |
|---------|---------|--------|-----|----------|
| optimizer | 54% | 80% | +26% | HIGH |
| controller | 58% | 80% | +22% | HIGH |
| service | 63% | 80% | +17% | MEDIUM |
| main | 55% | 80% | +25% | MEDIUM |

### Frontend: 35% Coverage ⚠️
- **Tests**: 14/14 passing ✅
- **Target**: 80%
- **Gap**: +45 percentage points
- **Required**: ~30-40 additional tests

#### Coverage by Area:
| Area | Current | Target | Gap | Priority |
|------|---------|--------|-----|----------|
| components/Forms | 29% | 80% | +51% | HIGH |
| services | 40% | 80% | +40% | HIGH |
| utils | 32% | 80% | +48% | HIGH |
| components/Common | 61% | 80% | +19% | MEDIUM |
| components/Tabs | 100% | 80% | ✅ | DONE |

---

## Implementation Strategy

### Phase 1: High-Impact Backend Tests (Priority 1)

Focus on areas with largest coverage gaps and highest impact:

#### 1.1 ApiService Error Handling (+10%)
**Location**: `src/test/java/ca/letkeman/resumes/optimizer/ApiServiceTest.java`

Add 12-15 tests covering:
- Connection timeouts
- Socket timeouts  
- Malformed JSON responses
- 4xx client errors (400, 401, 403, 404)
- 5xx server errors (500, 502, 503)
- Network interruptions
- Empty responses
- Null/invalid endpoints
- LLM API failures

**Impact**: Significant improvement in optimizer package coverage

#### 1.2 Controller Integration Tests (+8%)
**Location**: `src/test/java/ca/letkeman/resumes/controller/ResumeControllerTest.java`

Add 15-20 tests covering:
- All 6 new interview/networking endpoints
- Error response handling
- Validation failures
- Async processing verification
- Database persistence checks
- Edge cases (null inputs, empty files, etc.)

**Impact**: Major improvement in controller package coverage

#### 1.3 PromptService Database Tests (+5%)
**Location**: `src/test/java/ca/letkeman/resumes/service/PromptServiceTest.java`

Add 8-10 tests covering:
- savePromptToHistory() with various scenarios
- getAllHistory() pagination
- getHistoryByType() filtering
- Error handling for database failures
- Edge cases (null values, empty lists, etc.)

**Impact**: Boosts service package over 80%

**Estimated Backend Coverage After Phase 1**: 80%+ ✅

---

### Phase 2: High-Impact Frontend Tests (Priority 1)

Focus on components and utilities with lowest coverage:

#### 2.1 Utility Functions (+20%)
**Location**: `frontend/src/__tests__/utils/`

Create comprehensive tests for:
- **helpers.test.ts** (expand existing): +15%
  - formatFileSize()
  - formatDate()
  - truncateText()
  - debounce()
  - etc.

- **validators.test.ts** (expand existing): +5%
  - All validation rules
  - Edge cases
  - Error messages

**Impact**: Utils goes from 32% to ~80%

#### 2.2 Form Components (+15%)
**Location**: `frontend/src/__tests__/components/Forms/`

Expand existing tests:
- **MarkdownToPdfForm.test.tsx**: Full component lifecycle
- **DocumentUploadForm.test.tsx** (NEW): Complete coverage

Test scenarios:
- Form submission
- Validation errors
- File upload/selection
- Button states
- Loading states
- Error handling
- Success messages

**Impact**: Forms go from 29% to ~75%

#### 2.3 Services (+15%)
**Location**: `frontend/src/__tests__/services/`

Create comprehensive service tests:
- **api.test.ts** (NEW): API client functions
- **fileService.test.ts** (NEW): File operations

Test coverage:
- HTTP requests (GET, POST, DELETE)
- Error handling
- Response parsing
- File uploads
- Download handling

**Impact**: Services go from 40% to ~80%

**Estimated Frontend Coverage After Phase 2**: ~70%

---

### Phase 3: Remaining Coverage (Priority 2)

#### Backend Miscellaneous (+3%)
- Config class tests
- Utility method tests  
- BackgroundResume edge cases
- Entity validation tests

#### Frontend Components (+10%)
- MainContentTab component
- Navbar component
- FileHistory component
- Hook tests (useApi, useTheme, useFileManagement)

**Final Estimated Coverage**: 
- Backend: 83%+ ✅
- Frontend: 80%+ ✅

---

## Implementation Timeline

### Week 1: Backend Priority Tests
- **Days 1-2**: ApiService error handling (12-15 tests)
- **Days 3-4**: Controller integration (15-20 tests)
- **Day 5**: PromptService database (8-10 tests)
- **Verification**: Run coverage, should be ~80%

### Week 2: Frontend Priority Tests  
- **Days 1-2**: Utility function tests (20+ tests)
- **Days 3-4**: Form component tests (15+ tests)
- **Day 5**: Service tests (15+ tests)
- **Verification**: Run coverage, should be ~70%

### Week 3: Remaining Coverage
- **Days 1-3**: Backend miscellaneous tests
- **Days 4-5**: Frontend component and hook tests
- **Final Verification**: Both >80%

---

## Quality Assurance Checklist

### Before Each Commit:
- [ ] All new tests pass locally
- [ ] Existing tests still pass (no regression)
- [ ] Code follows project conventions
- [ ] Tests use proper mocking where appropriate
- [ ] Test names are descriptive

### Before Phase Completion:
- [ ] Run full test suite
- [ ] Generate coverage report
- [ ] Verify coverage meets phase target
- [ ] Review uncovered code
- [ ] Document any intentionally untested code

### Final Verification:
- [ ] Backend coverage >80% ✅
- [ ] Frontend coverage >80% ✅
- [ ] All tests passing (backend + frontend)
- [ ] No flaky tests
- [ ] Coverage reports generated
- [ ] Documentation updated

---

## Risk Mitigation

### Potential Risks:
1. **Time constraints**: Large number of tests needed
   - Mitigation: Focus on high-impact tests first
   
2. **Flaky tests**: Async operations, timing issues
   - Mitigation: Use proper async testing patterns
   
3. **Mock complexity**: Complex dependencies
   - Mitigation: Use test infrastructure patterns from existing tests

4. **Coverage measurement accuracy**: Generated code included
   - Mitigation: Properly configured exclusions in test config

---

## Success Criteria

✅ **Backend**: >80% test coverage  
✅ **Frontend**: >80% test coverage  
✅ **All tests passing**: 100% pass rate  
✅ **No regression**: Existing functionality intact  
✅ **Documentation**: Coverage reports available  

---

## References

- **Detailed Roadmap**: `TEST_COVERAGE_80_PERCENT_ROADMAP.md`
- **Current Status**: `TEST_COVERAGE_FINAL_REPORT.md`
- **Test Guide**: `INTERVIEW_PROMPTS_TEST_GUIDE.md`
- **All Tests Fixed**: `ALL_TESTS_FIXED_SUMMARY.md`

---

**Status**: Planning Complete | Implementation Ready | Infrastructure ✅
