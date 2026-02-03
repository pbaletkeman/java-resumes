# Prompts Directory

This directory contains LLM prompt templates used by the java-resumes application to generate AI-powered career optimization content.

## Available Prompts

### Document Optimization

#### RESUME.md

**Purpose:** Optimize resumes for Applicant Tracking Systems (ATS) and recruiters

**Input Variables:**

- `{job_title}` - Target job title
- `{job_description}` - Full job posting/description
- `{resume_string}` - Candidate's current resume
- `{company}` - Company name
- `{today}` - Current date

**Output:** Optimized resume content with ATS-friendly formatting, keyword alignment, and quantified achievements

**Use Case:**

- Resume optimization for specific job applications
- Keyword alignment for ATS systems
- Achievement quantification and impact highlighting

**Backend Endpoint:** `POST /api/process/resume`

---

#### COVER.md

**Purpose:** Generate personalized cover letters matching job requirements

**Input Variables:**

- `{job_title}` - Target job title
- `{job_description}` - Full job posting/description
- `{resume_string}` - Candidate's resume for context
- `{company}` - Company name
- `{today}` - Current date

**Output:** Professional, personalized cover letter with company-specific details and achievement highlights

**Use Case:**

- Cover letter generation for job applications
- Personalized matching to job requirements
- Company culture and tone alignment

**Backend Endpoint:** `POST /api/process/cover-letter`

---

### Interview Preparation

#### INTERVIEW-HR-QUESTIONS.md

**Purpose:** Generate 5 common HR interview questions covering behavioral competencies and cultural fit

**Input Variables:**

- `{job_title}` - Target job title
- `{company}` - Company name
- `{today}` - Current date

**Output:** 5 general HR questions with context on what interviewers assess

**Use Case:**

- General interview preparation
- Behavioral question practice
- Cultural fit assessment preparation
- Entry-level to mid-level candidate prep

**Backend Endpoint:** `POST /api/process/interview-hr-questions`

---

#### INTERVIEW-JOB-SPECIFIC.md

**Purpose:** Generate 5 role-specific technical and functional interview questions

**Input Variables:**

- `{job_title}` - Target job title
- `{job_description}` - Full job posting/description
- `{company}` - Company name
- `{today}` - Current date

**Output:** 5 targeted technical/functional questions specific to the role's core requirements

**Use Case:**

- Role-specific interview preparation
- Technical competency assessment prep
- Domain expertise demonstration practice

**Backend Endpoint:** `POST /api/process/interview-job-specific`

---

#### INTERVIEW-REVERSE.md

**Purpose:** Generate 10-12 strategic questions candidates should ask interviewers

**Input Variables:**

- `{job_title}` - Target job title
- `{job_description}` - Full job posting/description
- `{company}` - Company name
- `{today}` - Current date

**Output:** Strategic reverse interview questions organized by category (role clarity, team dynamics, growth, culture, red flags)

**Use Case:**

- Evaluating company culture and role fit
- Assessing organizational health
- Identifying potential red flags
- Demonstrating professional maturity

**Backend Endpoint:** `POST /api/process/interview-reverse`

---

### Networking & Outreach

#### COLD-EMAIL.md

**Purpose:** Generate 5 diverse cold outreach email variations for direct company contact

**Input Variables:**

- `{job_title}` - Target job title
- `{job_description}` - Full job posting/description (optional)
- `{resume_string}` - Candidate's resume for personalization
- `{company}` - Company name
- `{today}` - Current date

**Output:** 5 email variations with different tones, lengths, and approaches (direct, value-first, story-driven, etc.)

**Use Case:**

- Direct company outreach
- Networking with hiring managers
- Bypassing traditional job posting routes
- Building professional relationships

**Backend Endpoint:** `POST /api/process/cold-email`

---

#### COLD-LINKEDIN-MESSAGE.md

**Purpose:** Generate 5 LinkedIn-optimized cold outreach message variations

**Input Variables:**

- `{job_title}` - Target job title
- `{job_description}` - Full job posting/description (optional)
- `{resume_string}` - Candidate's resume for personalization
- `{company}` - Company name
- `{today}` - Current date

**Output:** 5 LinkedIn message variations (connection requests, InMails, post-connection messages) optimized for platform norms

**Use Case:**

- LinkedIn networking and outreach
- Recruiter and hiring manager connection
- Peer and employee connection
- Community and alumni group engagement

**Backend Endpoint:** `POST /api/process/cold-linkedin-message`

---

### Post-Interview Follow-Up

#### THANK-YOU-EMAIL.md

**Purpose:** Generate 5 thank you email variations for post-interview follow-up

**Input Variables:**

- `{job_title}` - Target job title
- `{company}` - Company name
- `{resume_string}` - Candidate's resume for context
- `{interviewer_name}` - Name of interviewer(s)
- `{today}` - Current date

**Output:** 5 thank you email variations with different tones and strategies (same-day, thoughtful, results-focused, etc.)

**Use Case:**

- Post-interview follow-up communication
- Reinforcing candidacy after conversation
- Addressing concerns or gaps mentioned
- Maintaining momentum in hiring process

**Backend Endpoint:** `POST /api/process/thank-you-email`

---

### Career Development

#### SKILLS.md

**Purpose:** Recommend skills, certifications, and experiences for career development in a target role

**Input Variables:**

- `{job_title}` - Target job title
- `{job_description}` - Full job posting/description
- `{today}` - Current date

**Output:**

- Recommended certifications with difficulty levels
- Key technical and soft skills with priority
- Practical experience suggestions and project ideas
- Learning resources and timelines
- Career progression guidance

**Use Case:**

- Career path planning and skill gap analysis
- Professional development roadmap creation
- Certification research and planning
- Competitive advantage identification
- No resume required

**Backend Endpoint:** `POST /api/process/skills`

---

## Key Information

### Variable Naming Convention

All templates use **standardized variable names**:

| Variable             | Type   | Example                    | Required | Usage                              |
| -------------------- | ------ | -------------------------- | -------- | ---------------------------------- |
| `{job_title}`        | String | "Senior Java Developer"    | Yes      | All prompts                        |
| `{job_description}`  | String | "Full job posting text..." | Yes\*    | Resume, cover, interview, skills   |
| `{resume_string}`    | String | "Complete resume text..."  | No\*     | Resume, outreach, thank-you emails |
| `{company}`          | String | "Acme Corp"                | Yes      | All prompts                        |
| `{today}`            | String | "February 2, 2026"         | Yes      | All prompts                        |
| `{interviewer_name}` | String | "John Smith"               | No\*     | Thank-you emails                   |

\*Optional or context-dependent

### How Templates Are Loaded

1. **Check external directory** (if `PROMPTS_DIR` environment variable is set)
2. **Fall back to bundled resources** (from this directory)
3. **Variables are replaced** by `ApiService` at runtime using regex substitution

See [docs/PROMPTS_CONFIGURATION.md](../../../../docs/PROMPTS_CONFIGURATION.md) for configuration details.

### Best Practices

✅ **DO:**

- Keep prompts focused on single task or output type
- Use clear, specific instructions with examples
- Include edge cases and constraints for better outputs
- Use consistent variable names across all prompts
- Test prompts with sample data before committing
- Document all variables in this README
- Update prompts based on user feedback and test results

❌ **DON'T:**

- Mix multiple tasks or outputs in one prompt
- Use vague language or unclear instructions
- Forget to replace all variable references
- Change variable names without updating code
- Create extremely long prompts (>3000 words usually less effective)
- Include sensitive or personal information in templates
- Hardcode company names or specific details

## Prompt Structure

Each prompt file follows this structure:

```markdown
# [Prompt Title]

You are an expert [role/expertise] with [X years] of experience...

## Your Task

[Clear statement of what the LLM should do]

## [Strategy/Approach/Framework Section]

### 1. [First approach/strategy]

- [Detailed breakdown points]

### 2. [Second approach/strategy]

- [Detailed breakdown points]

## Input Information

**Variable Name:** {variable_name}

## Output Format

[Structure for the LLM response, including markdown formatting examples]

## Important Notes

[Any special instructions or constraints]
```

## Adding New Prompts

To add a new prompt:

1. Create new `.md` file in this directory (e.g., `NETWORKING.md`)
2. Follow the standard prompt structure documented above
3. Use standardized variable names or define new ones clearly
4. Test the prompt with sample data for quality
5. Document the prompt in this README file
6. Update backend `ApiService` to recognize the new prompt type
7. Add new endpoint to `ResumeController` (e.g., `@PostMapping("/api/process/networking")`)
8. Create comprehensive unit tests for the endpoint
9. Add new option to frontend prompt selector dropdown
10. Update this README and any relevant documentation

**Full implementation guide:** See [docs/PROMPTS_CONFIGURATION.md](../../../../docs/PROMPTS_CONFIGURATION.md)

## File Structure

```
prompts/
├── README.md                        # This file
├── RESUME.md                        # Resume optimization
├── COVER.md                         # Cover letter generation
├── INTERVIEW-HR-QUESTIONS.md        # General HR interview prep
├── INTERVIEW-JOB-SPECIFIC.md        # Role-specific interview prep
├── INTERVIEW-REVERSE.md             # Questions to ask interviewers
├── COLD-EMAIL.md                    # Cold email outreach
├── COLD-LINKEDIN-MESSAGE.md         # LinkedIn cold messaging
├── THANK-YOU-EMAIL.md               # Post-interview thank you
└── SKILLS.md                        # Skills and career development
```

## Prompt Categories Summary

| Category           | Prompts                                                           | Purpose                                       |
| ------------------ | ----------------------------------------------------------------- | --------------------------------------------- |
| **Optimization**   | RESUME, COVER                                                     | Optimize documents for specific opportunities |
| **Interview Prep** | INTERVIEW-HR-QUESTIONS, INTERVIEW-JOB-SPECIFIC, INTERVIEW-REVERSE | Interview preparation and evaluation          |
| **Networking**     | COLD-EMAIL, COLD-LINKEDIN-MESSAGE                                 | Outreach and relationship building            |
| **Follow-Up**      | THANK-YOU-EMAIL                                                   | Post-interview communication                  |
| **Development**    | SKILLS                                                            | Career planning and skill development         |

## Related Documentation

- [PROMPTS_CONFIGURATION.md](../../../../docs/PROMPTS_CONFIGURATION.md) - Configuration, deployment, and detailed setup
- [Architecture.md](../../../../docs/architecture/ARCHITECTURE.md) - System architecture and integration points
- [BACKEND_README.md](../../../../docs/BACKEND_README.md) - Backend API documentation and endpoints

---

**Last Updated:** February 2, 2026
