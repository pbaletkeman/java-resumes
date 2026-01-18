# UI Changes Visual Guide

> **📍 Location:** `docs/UI_CHANGES_VISUAL_GUIDE.md`
> **👥 Audience:** QA, Designers, Stakeholders
> **🔗 Related:** [Frontend Enhancements](FRONTEND_ENHANCEMENTS.md) | [Screenshot Capture Summary](SCREENSHOT_CAPTURE_SUMMARY.md) | [Index](INDEX.md)

---

## Before vs After

### BEFORE: Limited Options

```
Job Information & Model Selection (3-column layout)
┌─────────────────┬──────────────────┬────────────────┐
│  Job Title *    │  Company *       │  AI Model      │
│ ┌─────────────┐ │ ┌──────────────┐ │ ┌────────────┐ │
│ │             │ │ │              │ │ │  Dropdown  │ │
│ └─────────────┘ │ └──────────────┘ │ └────────────┘ │
└─────────────────┴──────────────────┴────────────────┘

Action Buttons (Fixed - Always 2 buttons)
┌────────────────────────────────────────────────────┐
│  [Create Optimized Resume]  [Create Cover Letter]  │
└────────────────────────────────────────────────────┘
```

### AFTER: Flexible & Expandable

```plaintext
Job Information & Model Selection (4-column layout)
┌─────────────┬──────────────┬──────────────┬─────────────────┐
│ Job Title * │  Company *   │  AI Model    │ Output Types    │
│┌───────────┐│┌────────────┐│┌──────────┐  │┌───────────────┐│
││           │││            │││          │  ││ Resume ✓      ||
│└───────────┘│└────────────┘│└──────────┘  ││ Cover Letter   ││
│             │              │              ││ Skills ✓      ││
│             │              │              │└───────────────┘│
└─────────────┴──────────────┴──────────────┴─────────────────┘

Action Buttons (Dynamic - Only selected types)
┌────────────────────────────────────────────────────────────┐
│ [Optimize Resume] [Generate Cover Letter] [Generate Skills]│
└────────────────────────────────────────────────────────────┘
```

---

## New Features Highlighted

### 1. Output Types Dropdown

- **Type**: Multi-select Dropdown
- **Options**:
  - Resume Optimization
  - Cover Letter
  - Skills & Certifications
- **Default**: Resume Optimization
- **Behavior**: Only selected output types show action buttons

### 2. Dynamic Action Buttons

- **Resume Button**: Blue (info)
  - Label: "Optimize Resume"
  - Icon: 📄 file-edit
  - Appears when "Resume Optimization" is selected

- **Cover Letter Button**: Green (success)
  - Label: "Generate Cover Letter"
  - Icon: 📄 file
  - Appears when "Cover Letter" is selected

- **Skills Button**: Yellow (warning) **← NEW**
  - Label: "Generate Skills & Certs"
  - Icon: ⭐ star
  - Appears when "Skills & Certifications" is selected

### 3. Enhanced Grid Layout

Before: 3 columns (Job Title | Company | Model)
After: 4 columns (Job Title | Company | Model | Output Types)

---

## User Interaction Flow

### Scenario 1: Generate Resume Only

```
1. User selects "Resume Optimization" (or defaults to it)
2. Only "Optimize Resume" button is visible
3. User fills job description and resume
4. User clicks "Optimize Resume"
5. Backend processes with promptType: ['resume']
```

### Scenario 2: Generate Multiple Outputs

```
1. User selects "Resume Optimization" + "Cover Letter" + "Skills"
2. All three buttons are visible
3. User can:
   - Click "Optimize Resume" to get optimized resume
   - Click "Generate Cover Letter" to get cover letter
   - Click "Generate Skills & Certs" to get skills suggestions
4. Each submission is independent
```

### Scenario 3: Complex Selection

```
1. User selects only "Skills & Certifications"
2. Only Skills button is visible
3. User gets skills/certs suggestions
4. User can change selection in dropdown to add more output types
5. More buttons appear as user toggles options
```

---

## Code Examples

### Dropdown Usage

```tsx
<Dropdown
  id="promptTypes"
  value={promptTypes} // ['resume', 'skills']
  onChange={(e) => setPromptTypes(Array.isArray(e.value) ? e.value : [e.value])}
  options={promptTypeOptions} // Three options
  optionLabel="label" // Shows "Resume Optimization", etc.
  optionValue="value" // Uses 'resume', 'cover', 'skills'
  placeholder="Select output types..."
  className="w-full"
  multiple // Allow multi-select
/>
```

### Dynamic Button Rendering

```tsx
{
  promptTypes.includes("resume") && (
    <Button
      label="Optimize Resume"
      icon="pi pi-file-edit"
      onClick={handleProcessResume}
      disabled={loading}
      severity="info"
      className="flex-1"
    />
  );
}

{
  promptTypes.includes("cover") && (
    <Button
      label="Generate Cover Letter"
      icon="pi pi-file"
      onClick={handleProcessCoverLetter}
      disabled={loading}
      severity="success"
      className="flex-1"
    />
  );
}

{
  promptTypes.includes("skills") && (
    <Button
      label="Generate Skills & Certs"
      icon="pi pi-star"
      onClick={handleProcessSkills}
      disabled={loading}
      severity="warning"
      className="flex-1"
    />
  );
}
```

### New Handler Function

```tsx
const handleProcessSkills = async () => {
  if (!validateForm()) return;

  try {
    const optimize = {
      promptType: ['skills'],            // Different prompt type
      temperature: 0.15,
      model: model,
      // ... other fields
    };

    const data = {
      jobDescription: ...,
      resume: ...,
      optimize: JSON.stringify(optimize),
    };

    const result = await resumeApi.execute(() =>
      fileService.processSkills(data)
    );
    showSuccess('Skills and certifications suggestions generated successfully');
    console.log('Skills Suggestions:', result);
  } catch (err: unknown) {
    showError((err as Error)?.message || 'Failed to generate skills suggestions');
  }
};
```

---

## Backend Integration

### Existing Endpoint

Uses the same backend endpoint: `/api/optimize`

### Prompt Type Handling

Backend receives in the JSON:

```json
{
  "promptType": ["skills"],
  "resume": "resume.txt",
  "jobDescription": "job.txt",
  "jobTitle": "Senior Developer",
  "company": "Tech Corp",
  "model": "gpt-4-turbo",
  "temperature": 0.15
}
```

### Backend Processing

The backend's ResumeController handles all three prompt types through the same endpoint, routing based on the promptType value:

- `['resume']` → Uses RESUME.md prompt
- `['cover']` → Uses COVER.md prompt
- `['skills']` → Uses SKILLS.md prompt (NEW)

---

## Responsive Behavior

### Desktop (4 columns)

```
[Job Title] [Company] [Model] [Output Types]
```

### Tablet (2x2 grid)

```
[Job Title]    [Company]
[Model]        [Output Types]
```

### Mobile (Stack)

```
[Job Title]
[Company]
[Model]
[Output Types]
```

The TailwindCSS grid automatically handles responsive behavior.

---

## State Management

### State Variables

```tsx
const [promptTypes, setPromptTypes] = useState<string[]>(["resume"]);
```

### State Updates

- **On selection change**: `setPromptTypes(newArray)`
- **On form clear**: `setPromptTypes(['resume'])` (reset to default)
- **Initial value**: `['resume']` (Resume Optimization selected by default)

### Type Safety

- Stored as string array: `string[]`
- Values are literal types: `'resume' | 'cover' | 'skills'`
- Full TypeScript support with no type errors

---

## Testing Scenarios

### Unit Test Ideas

```tsx
// Test 1: Default selection
expect(screen.getByText("Optimize Resume")).toBeInTheDocument();
expect(screen.queryByText("Generate Cover Letter")).not.toBeInTheDocument();

// Test 2: Multi-select
userEvent.click(screen.getByDisplayValue("Cover Letter"));
expect(screen.getByText("Generate Cover Letter")).toBeInTheDocument();

// Test 3: Skills button
userEvent.click(screen.getByDisplayValue("Skills & Certifications"));
expect(screen.getByText("Generate Skills & Certs")).toBeInTheDocument();

// Test 4: Clear form resets selection
userEvent.click(screen.getByText("Clear & Start Over"));
expect(screen.getByDisplayValue("Resume Optimization")).toBeInTheDocument();
```

---

## Summary of Changes

| Aspect                  | Before       | After            |
| ----------------------- | ------------ | ---------------- |
| **Grid Layout**         | 3 columns    | 4 columns        |
| **Job Title**           | Column 1     | Column 1 ✓       |
| **Company**             | Column 2     | Column 2 ✓       |
| **Model**               | Column 3     | Column 3 ✓       |
| **Output Types**        | N/A          | Column 4 ✓       |
| **Available Buttons**   | 2 (fixed)    | 1-3 (dynamic)    |
| **Resume Button**       | Always shown | Conditional ✓    |
| **Cover Letter Button** | Always shown | Conditional ✓    |
| **Skills Button**       | N/A          | New ✓            |
| **User Control**        | Limited      | Full selection ✓ |

---

**This enhancement makes the UI more flexible and provides users full control over which outputs to generate!**
