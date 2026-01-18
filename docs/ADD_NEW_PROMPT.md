# Adding New Prompts: Developer Guide

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Step-by-Step Guide](#step-by-step-guide)
- [Prompt Structure](#prompt-structure)
- [Integration Points](#integration-points)
- [Testing](#testing)
- [Examples](#examples)
- [Troubleshooting](#troubleshooting)

---

## Overview

This guide explains how to add a new prompt to the java-resumes application. The system supports multiple prompts (Resume, Cover Letter, Skills suggestions, and more) through a standardized template and integration approach.

### What is a Prompt?

A prompt is a markdown file that contains instructions for the LLM on how to process user inputs. Prompts are:

- **Stored as markdown files** in `src/main/resources/prompts/` (bundled)
- **Loaded dynamically** by the `PromptService`
- **Template-based** with placeholders like `{job_title}` and `{job_description}`
- **Versioned** in Git for tracking changes
- **Modular** - each prompt is independent

### Existing Prompts

| Prompt Name | File        | Purpose                                  | Variables Used                                                                |
| ----------- | ----------- | ---------------------------------------- | ----------------------------------------------------------------------------- |
| RESUME      | `RESUME.md` | Optimize resumes for ATS and target jobs | `{job_title}`, `{job_description}`, `{resume_string}`, `{today}`, `{company}` |
| COVER       | `COVER.md`  | Generate personalized cover letters      | `{job_title}`, `{job_description}`, `{resume_string}`, `{today}`, `{company}` |
| SKILLS      | `SKILLS.md` | Suggest certifications and experiences   | `{job_title}`, `{job_description}`                                            |

---

## Architecture

### System Components

The prompt system consists of:

#### 1. **Prompt Files** (`src/main/resources/prompts/*.md`)

- Markdown files containing LLM instructions
- Template variables enclosed in `{braces}`
- Can be up to 2000-3000 words for comprehensive instructions

#### 2. **PromptService** (`src/main/java/.../service/PromptService.java`)

- Loads prompts from bundled resources or external directory
- Replaces template variables with runtime values
- Handles missing prompts gracefully

#### 3. **Optimize Model** (`src/main/java/.../model/Optimize.java`)

- Stores prompt configuration and parameters
- Validates that required fields are present
- Supports different prompt types

#### 4. **ApiService** (`src/main/java/.../optimizer/ApiService.java`)

- Receives `Optimize` object with selected prompt
- Loads prompt via `PromptService`
- Replaces variables and sends to LLM
- Saves results to output files

#### 5. **Controller Endpoints** (`src/main/java/.../controller/ResumeController.java`)

- HTTP endpoints for different prompt types
- `/api/process/resume` - Resume optimization
- `/api/process/cover-letter` - Cover letter generation
- `/api/process/skills` - Skills suggestions

#### 6. **Frontend** (`frontend/src/`)

- UI components for user input
- Model selection (via `ModelSelector`)
- Calls appropriate backend endpoints

### Data Flow

```
User Input (Frontend)
         ↓
HTTP Request to `/api/process/{promptType}`
         ↓
ResumeController validates input
         ↓
Create/Update Optimize object
         ↓
BackgroundResume thread starts
         ↓
ApiService.produceFiles() called
         ↓
PromptService.loadPrompt(promptType)
         ↓
Template variables replaced
         ↓
ChatBody prepared with prompt + LLM config
         ↓
HTTP call to LLM endpoint
         ↓
Result processed and saved to markdown/PDF
```

---

## Step-by-Step Guide

### 1. Create the Prompt File

Create a new markdown file in `src/main/resources/prompts/`:

```bash
# Example: adding a SUGGESTIONS prompt
src/main/resources/prompts/SUGGESTIONS.md
```

**File naming convention:**

- Use uppercase names that match the prompt type
- Examples: `RESUME.md`, `COVER.md`, `SKILLS.md`, `SUGGESTIONS.md`

**Template structure:**

```markdown
# [Prompt Title]

You are an expert [role description].

## Your Task

[Clear description of what the LLM should do]

## Strategy/Approach

[Detailed instructions for the LLM]

### 1. [First Strategy]

[Explanation]

### 2. [Second Strategy]

[Explanation]

## Input Information

**Job Title:** {job_title}
**Job Description:** {job_description}
[Add other variables as needed]

## Output Requirements

[Clear specifications for the output]

## Quality Standards

✓ [Standard 1]
✓ [Standard 2]

## Output Format

[Specify format: markdown, JSON, etc.]
```

### 2. Define Template Variables

Identify all variables your prompt needs:

**Standard Variables:**

- `{job_title}` - The job title (from Optimize model)
- `{job_description}` - The full job posting (from Optimize model)
- `{resume_string}` - Candidate's resume (from Optimize model)
- `{company}` - Company name (from Optimize model)
- `{today}` - Current date (auto-generated)

**Adding New Variables:**

If you need additional variables:

1. **Update Optimize.java** - Add getter/setter method
2. **Update ApiService.java** - Add `.replace()` call for new variable
3. **Update Controller** - Accept the new parameter
4. **Update Frontend** - Add UI field for input
5. **Document in this guide** - List the new variable

### 3. Update Backend Code

#### 3A. Update Optimize Model

If adding a new prompt that needs additional fields:

```java
// src/main/java/.../model/Optimize.java

private String newField;  // Add field

public String getNewField() {
    return newField;
}

public void setNewField(String newField) {
    this.newField = newField;
}

// Update isValid() if needed
public boolean isValid() {
    return ... && getNewField() != null;
}
```

#### 3B. Update ApiService

Replace your new variables in the prompt:

```java
// src/main/java/.../optimizer/ApiService.java

public void produceFiles(String promptType, Optimize optimize, String endpoint, String apikey, String model, String root) {
    // ... existing code ...

    // Load prompt
    String promptData = getPromptService().loadPrompt(promptType);

    // Replace variables
    promptData = promptData
        .replace("{resume_string}", optimize.getResume())
        .replace("{job_description}", optimize.getJobDescription())
        .replace("{job_title}", optimize.getJobTitle())
        .replace("{new_variable}", optimize.getNewField())  // Add this
        .replace("{today}", today);

    // ... rest of code ...
}
```

#### 3C. Update ResumeController

Add a new endpoint if needed:

```java
// src/main/java/.../controller/ResumeController.java

@PostMapping(path = "/process/newprompt")
public ResponseEntity<ResponseMessage> processNewPrompt(
    @RequestParam(name = "job", required = false) MultipartFile job,
    @RequestParam(name = "optimize", required = false) String opt) {

    Optimize optimize = new Optimize();
    // ... setup optimize object ...
    optimize.setPromptType(new String[]{"NEWPROMPT"});

    // Process
    BackgroundResume bg = new BackgroundResume(optimize, root);
    Thread t = new Thread(bg);
    t.start();

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseMessage("Processing started"));
}
```

### 4. Add Unit Tests

#### Backend Tests

**Test the Optimize model:**

```java
// src/test/java/.../model/OptimizeTest.java

@Test
void test_newprompt_validation() {
    Optimize optimize = new Optimize();
    optimize.setPromptType(new String[]{"NEWPROMPT"});
    optimize.setJobDescription("Description");
    optimize.setJobTitle("Title");
    optimize.setCompany("Company");
    // ... set other required fields ...

    assertTrue(optimize.isValid());
}
```

**Test the PromptService:**

```java
// src/test/java/.../service/PromptServiceTest.java

@Test
void testLoadNewPrompt() {
    PromptService service = new PromptService();
    String prompt = service.loadPrompt("NEWPROMPT");

    assertNotNull(prompt);
    assertFalse(prompt.isEmpty());
    assertTrue(prompt.contains("{job_title}"));
}
```

**Test the Controller:**

```java
// src/test/java/.../controller/ResumeControllerTest.java

@Test
void test_processNewPrompt_with_valid_input() throws Exception {
    MockMultipartFile job = new MockMultipartFile(
        "job", "jd.txt", "text/plain", "Job description".getBytes());
    String optimize = "{...json...}";

    mockMvc.perform(multipart("/api/process/newprompt")
        .file(job)
        .param("optimize", optimize))
        .andExpect(status().isOk());
}
```

#### Frontend Tests

```typescript
// frontend/src/components/Forms/YourComponent.test.tsx

describe('YourComponent', () => {
  it('should send new prompt type to backend', async () => {
    const { getByRole } = render(<YourComponent />);

    fireEvent.click(getByRole('button', { name: /submit/i }));

    // Verify API call
    await waitFor(() => {
      expect(mockApiClient.post).toHaveBeenCalledWith(
        '/api/process/newprompt',
        expect.any(FormData)
      );
    });
  });
});
```

### 5. Update Frontend

Add UI component for the new prompt:

```typescript
// Example: frontend/src/components/Forms/SkillsSuggestionsForm.tsx

import React, { useState } from 'react';
import { fileService } from '../../services/fileService';

export const SkillsSuggestionsForm: React.FC = () => {
  const [jobDescription, setJobDescription] = useState('');
  const [jobTitle, setJobTitle] = useState('');

  const handleSubmit = async () => {
    const result = await fileService.processSkills({
      jobDescription,
      jobTitle,
      optimize: JSON.stringify({
        company: 'Company',
        jobTitle,
        promptType: ['SKILLS'],
        temperature: 0.15,
        model: 'mistral:latest',
      }),
    });
    // Handle result
  };

  return (
    <div>
      {/* Form UI */}
    </div>
  );
};
```

### 6. Update FileService

If needed, add method to `frontend/src/services/fileService.ts`:

```typescript
async processSkills(data: {
  jobDescription: string;
  jobTitle: string;
  optimize?: string;
}): Promise<ProcessingResult> {
  // Similar to processResume
  const formData = new FormData();
  formData.append('job', new File([data.jobDescription], 'job.txt'));

  const response = await apiClient.post<ProcessingResult>(
    API_ENDPOINTS.PROCESS_SKILLS,
    formData
  );
  return response.data;
}
```

### 7. Update Constants

Update frontend constants:

```typescript
// frontend/src/utils/constants.ts

export const API_ENDPOINTS = {
  // ... existing endpoints ...
  PROCESS_SKILLS: "/api/process/skills",
};
```

### 8. Validate and Test

```bash
# Backend
./gradlew clean test          # Run all tests
./gradlew test --tests PromptServiceTest  # Run specific test
./gradlew checkstyleMain      # Check code style

# Frontend
npm test                      # Run all tests
npm run test -- ModelSelector # Run specific test
npm run type-check           # Type checking
npm run lint                 # Linting
```

### 9. Build and Deploy

```bash
# Backend
./gradlew clean build

# Frontend
npm run build

# Full deployment
docker-compose up --build
```

---

## Prompt Structure

### Anatomy of a Good Prompt

1. **Role Definition** - Who is the LLM playing?

   ```markdown
   You are an expert [profession] with [years] of experience...
   ```

2. **Task Clarity** - What should the LLM do?

   ```markdown
   ## Your Task

   [Clear, specific instructions]
   ```

3. **Strategy Section** - How should it approach the task?

   ```markdown
   ## Strategy

   ### 1. [Step 1]

   ### 2. [Step 2]
   ```

4. **Input Specification** - What data is provided?

   ```markdown
   ## Input Information

   **Variable Name:** {variable_placeholder}
   ```

5. **Output Requirements** - What should the result look like?

   ```markdown
   ## Output Requirements

   - Requirement 1
   - Requirement 2
   ```

6. **Quality Standards** - What makes a good response?
   ```markdown
   ## Quality Standards

   ✓ Standard 1
   ✓ Standard 2
   ```

### Best Practices

✅ **DO:**

- Be specific and detailed
- Include examples when helpful
- State edge cases explicitly
- Define output format clearly
- Use consistent terminology
- Keep prompts between 500-2000 words
- Include reasoning/strategy sections

❌ **DON'T:**

- Use vague language ("make it better")
- Assume the LLM knows your industry
- Forget to specify output format
- Include multiple conflicting instructions
- Make prompts longer than 3000 words (usually ineffective)

---

## Integration Points

### 1. Prompt Registration

Add to the list of valid prompt types:

In `Optimize.java`, update `isValidPromptType()`:

```java
public boolean isValidPromptType() {
    return hasResumeOrCoverPrompt() || isSkillsPrompt() || isNewPromptType();
}

public boolean isNewPromptType() {
    return Arrays.stream(getPromptType())
        .anyMatch(x -> x.equalsIgnoreCase("newprompt"));
}
```

### 2. File Output Naming

Results are saved with this pattern:

```
{promptType}-{company}-{jobTitle}-{timestamp}.md
{promptType}-{company}-{jobTitle}-{timestamp}.pdf
```

Example:

```
SKILLS-TechCorp-JavaDeveloper-2024-01-18-10-30.md
SKILLS-TechCorp-JavaDeveloper-2024-01-18-10-30.pdf
```

### 3. API Endpoint Pattern

Follow this pattern:

```
POST /api/process/{prompt-type-lowercase}

Example: POST /api/process/skills
```

---

## Testing

### Test Checklist

- [ ] Prompt file exists in `src/main/resources/prompts/`
- [ ] All template variables are used in prompt
- [ ] PromptService tests pass
- [ ] Optimize model validation tests pass
- [ ] Controller endpoint tests pass
- [ ] Frontend component tests pass
- [ ] End-to-end test (manual) passes
- [ ] Checkstyle compliance (0 errors)
- [ ] Unit test coverage > 80%

### Manual Testing Procedure

1. **Start backend:**

   ```bash
   ./gradlew bootRun
   ```

2. **Start frontend:**

   ```bash
   npm run dev
   ```

3. **Test via UI:**
   - Navigate to prompt input section
   - Fill in required fields
   - Submit form
   - Verify result file is generated

4. **Test via API (curl):**

   ```bash
   curl -X POST http://localhost:8080/api/process/newprompt \
     -F "job=@job_description.txt" \
     -F "optimize={...}"
   ```

5. **Check logs:**
   ```bash
   tail -f application.log | grep NEWPROMPT
   ```

---

## Examples

### Example 1: Simple Suggestion Prompt

See `prompts/SKILLS.md` for a complete example of a prompt that takes just job title and description.

### Example 2: Adding a LinkedIn Summary Prompt

**File:** `src/main/resources/prompts/LINKEDIN.md`

```markdown
# LinkedIn Summary Prompt

You are a LinkedIn profile optimization expert...

## Your Task

Create a compelling LinkedIn summary that...

## Input Information

**Job Title:** {job_title}
**Job Description:** {job_description}
**Professional Background:** {experience_summary}

## Output Requirements

- 220 characters maximum
- Include top 3-5 keywords
- Call-to-action statement
- Professional but personable tone

## Output Format

Provide ONLY the summary text, no additional commentary.
```

**Backend changes:**

```java
// Optimize.java - add field
private String experienceSummary;

// ApiService.java - add replacement
.replace("{experience_summary}", optimize.getExperienceSummary())

// ResumeController.java - add endpoint
@PostMapping("/process/linkedin")
public ResponseEntity<ResponseMessage> processLinkedin(...)

// Controller: set prompt type
optimize.setPromptType(new String[]{"LINKEDIN"});
```

**Tests:**

```java
@Test
void testLinkedinPromptLoads() {
    String prompt = promptService.loadPrompt("LINKEDIN");
    assertTrue(prompt.contains("{job_title}"));
}

@Test
void testProcessLinkedin() throws Exception {
    // Test endpoint
}
```

---

## Troubleshooting

### Prompt Not Loading

**Error:** "Could not load prompt: NEWPROMPT"

**Solutions:**

1. Verify file exists: `src/main/resources/prompts/NEWPROMPT.md`
2. Verify file name is uppercase
3. Rebuild JAR: `./gradlew clean build`
4. Check logs for missing files

### Variables Not Replaced

**Error:** Output contains `{job_title}` instead of actual value

**Solutions:**

1. Verify variable is in `ApiService.replaceVariables()` call
2. Verify variable name matches exactly (case-sensitive)
3. Verify Optimize object has the value set
4. Check logs for replacement operations

### Tests Failing

**Solutions:**

1. Ensure prompt file exists in test resources too
2. Mock PromptService correctly
3. Verify test data is valid
4. Run `./gradlew clean test` to clear cache

### Build Fails

**Solutions:**

1. Run `./gradlew clean` to clear old builds
2. Verify checkstyle compliance: `./gradlew checkstyleMain`
3. Check for syntax errors in new code
4. Verify all imports are correct

---

## See Also

- [PROMPTS_CONFIGURATION.md](../PROMPTS_CONFIGURATION.md) - Configuration overview
- [AGENTS.md](../../AGENTS.md) - AI agent guidelines
- [API Documentation](../api.md) - API endpoints
- [Architecture Overview](../architecture/ARCHITECTURE.md) - System design

---

**Last Updated:** January 18, 2026
**Version:** 1.0
