# Prompts Directory

This directory contains LLM prompt templates used by the java-resumes application.

## Available Prompts

### RESUME.md

**Purpose:** Optimize resumes for Applicant Tracking Systems (ATS) and recruiters

**Input Variables:**

- `{job_title}` - Target job title
- `{job_description}` - Full job posting/description
- `{resume_string}` - Candidate's current resume
- `{company}` - Company name
- `{today}` - Current date

**Output:** Optimized resume content tailored to the job

**Use Case:**

- Resume optimization for specific job applications
- Keyword alignment for ATS
- Relevance scoring to job posting

**Backend Endpoint:** `POST /api/process/resume`

---

### COVER.md

**Purpose:** Generate personalized cover letters matching job requirements

**Input Variables:**

- `{job_title}` - Target job title
- `{job_description}` - Full job posting/description
- `{resume_string}` - Candidate's resume for context
- `{company}` - Company name
- `{today}` - Current date

**Output:** Professional, personalized cover letter

**Use Case:**

- Cover letter generation for job applications
- Personalized matching to job requirements
- Tone and style matching to company culture

**Backend Endpoint:** `POST /api/process/cover-letter`

---

### SKILLS.md

**Purpose:** Suggest certifications, experiences, and skills for career development

**Input Variables:**

- `{job_title}` - Target job title
- `{job_description}` - Full job posting/description

**Output:**

- Recommended certifications
- Key experiences to develop
- Skills roadmap
- Learning suggestions

**Use Case:**

- Career path planning
- Skill gap analysis
- Professional development suggestions
- No resume required

**Backend Endpoint:** `POST /api/process/skills`

---

## Key Information

### Variable Naming Convention

All templates use **standardized variable names**:

| Variable            | Type   | Example                    |
| ------------------- | ------ | -------------------------- |
| `{job_title}`       | String | "Senior Java Developer"    |
| `{job_description}` | String | "Full job posting text..." |
| `{resume_string}`   | String | "Complete resume text..."  |
| `{company}`         | String | "Acme Corp"                |
| `{today}`           | String | "January 18, 2026"         |

### How Templates Are Loaded

1. **Check external directory** (if `PROMPTS_DIR` environment variable is set)
2. **Fall back to bundled resources** (from this directory)
3. **Variables are replaced** by `ApiService` at runtime

See [docs/PROMPTS_CONFIGURATION.md](../../docs/PROMPTS_CONFIGURATION.md) for configuration details.

### Best Practices

✅ **DO:**

- Keep prompts focused on single task
- Use clear, specific instructions
- Include edge cases and constraints
- Use consistent variable names
- Test prompts before committing
- Document new variables in this README

❌ **DON'T:**

- Mix multiple tasks in one prompt
- Use vague language
- Forget to replace all variable references
- Change variable names without updating code
- Create extremely long prompts (>3000 words usually less effective)

## Adding New Prompts

To add a new prompt:

1. Create new `.md` file (e.g., `LINKEDIN.md`)
2. Use standard template variables or define new ones
3. Document the prompt in this README
4. Update backend to support new prompt type
5. Add controller endpoint
6. Create comprehensive unit tests
7. Update frontend (if needed)

**Full guide:** See [docs/ADD_NEW_PROMPT.md](../../docs/ADD_NEW_PROMPT.md)

## File Structure

```
prompts/
├── README.md              # This file
├── RESUME.md              # Resume optimization prompt
├── COVER.md               # Cover letter generation prompt
└── SKILLS.md              # Skills and certifications prompt
```

## Related Documentation

- [PROMPTS_CONFIGURATION.md](../../docs/PROMPTS_CONFIGURATION.md) - Configuration and deployment
- [ADD_NEW_PROMPT.md](../../docs/ADD_NEW_PROMPT.md) - Step-by-step guide for adding new prompts
- [Architecture.md](../../docs/architecture/ARCHITECTURE.md) - System architecture
- [API Documentation](../../docs/api.md) - REST API endpoints

---

**Last Updated:** January 18, 2026
