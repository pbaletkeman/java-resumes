# LLM Prompt Templates

This directory contains the carefully crafted prompt templates that drive the artificial intelligence engine behind the Resume Optimization System. These prompts are strategic instructions that guide the LLM (Large Language Model) to produce high-quality, contextually relevant resume and cover letter optimizations.

## Table of Contents

- [Overview](#overview)
- [Available Prompt Templates](#available-prompt-templates)
- [Prompt Design Philosophy](#prompt-design-philosophy)
- [How Prompts Are Used](#how-prompts-are-used)
- [Prompt Customization](#prompt-customization)
- [Quality Metrics](#quality-metrics)
- [Technical Details](#technical-details)
- [Best Practices for Prompt Engineering](#best-practices-for-prompt-engineering)
- [Maintenance & Updates](#maintenance--updates)
- [Examples](#examples)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)

---

## Overview

Prompts are the communication bridge between the application and the AI model. Each prompt is designed to:

- **Ensure Consistency**: Standardized formatting and tone across all outputs
- **Maximize Relevance**: Tailor optimizations to specific job descriptions
- **Maintain Quality**: Enforce best practices in resume and cover letter writing
- **Preserve Authenticity**: Balance optimization while maintaining the candidate's voice

## Available Prompt Templates

### 1. **RESUME.md**

**Purpose**: Guide the LLM in optimizing resumes for ATS (Applicant Tracking Systems) and recruiters

**Key Functions**:

- Analyze job descriptions to identify critical keywords
- Rewrite resume bullet points to match job requirements
- Emphasize relevant skills and experiences
- Optimize for both machine parsing and human readability
- Maintain chronological accuracy and authenticity

**Input Parameters**:

- Original resume content
- Target job description
- Desired resume format/style

**Output**:

- Optimized resume with enhanced relevance
- Keyword-aligned sections
- Improved formatting for ATS compatibility

### 2. **COVER.md**

**Purpose**: Generate compelling cover letters tailored to specific job opportunities

**Key Functions**:

- Create personalized opening statements
- Connect candidate experience to job requirements
- Highlight relevant achievements and qualifications
- Craft persuasive closing remarks
- Adapt tone to company culture and role

**Input Parameters**:

- Candidate information and background
- Job description and company details
- Cover letter preferences/style

**Output**:

- Custom cover letter (typically 3-4 paragraphs)
- Professional tone matching industry standards
- Ready-to-send formatted document

### 3. **SKILLS.md**

**Purpose**: Generate targeted recommendations for skills, certifications, and experiences to strengthen candidacy

**Key Functions**:

- Identify core technical skills from job requirements
- Recommend industry-recognized certifications
- Suggest practical hands-on experiences and projects
- Provide sequential learning path with timelines
- Consider career progression and specializations

**Input Parameters**:

- Job title
- Job description
- Career level (optional)

**Output**:

- Prioritized certification recommendations with costs and timelines
- Practical experience suggestions and project ideas
- Recommended learning path (0-6+ months)
- Estimated time investment and success metrics

### 4. **Interview Preparation Prompts** (new-prompts.md)

**Purpose**: Prepare candidates for comprehensive interview scenarios

#### 4a. **Interview HR Questions**
**Endpoint**: `/api/generate/interview-hr-questions`

**Key Functions**:
- Generate 5 general HR interview questions
- Cover behavioral, situational, and cultural fit topics
- Prepare candidates for common HR screening

**Input Parameters**:
- Job description

**Output**:
- 5 commonly asked HR interview questions

#### 4b. **Job-Specific Interview Questions**
**Endpoint**: `/api/generate/interview-job-specific`

**Key Functions**:
- Generate 5 role-specific technical/functional questions
- Align questions with job requirements
- Focus on skills and experience validation

**Input Parameters**:
- Job description

**Output**:
- 5 targeted interview questions based on job requirements

#### 4c. **Reverse Interview Questions**
**Endpoint**: `/api/generate/interview-reverse`

**Key Functions**:
- Generate thoughtful questions candidates can ask interviewers
- Demonstrate engagement and strategic thinking
- Assess company culture and role fit

**Input Parameters**:
- Job description

**Output**:
- 5 strategic questions for candidates to ask during interviews

### 5. **Networking & Outreach Prompts** (new-prompts.md)

**Purpose**: Generate professional communication for job search networking

#### 5a. **Cold Email**
**Endpoint**: `/api/generate/cold-email`

**Key Functions**:
- Generate 5 distinct cold outreach emails
- Professional introduction to target companies
- Express interest and highlight relevant qualifications

**Input Parameters**:
- Job description
- Target company information

**Output**:
- 5 variations of professional cold emails

#### 5b. **Cold LinkedIn Message**
**Endpoint**: `/api/generate/cold-linkedin-message`

**Key Functions**:
- Generate 5 distinct LinkedIn connection messages
- Concise, professional outreach appropriate for LinkedIn
- Build professional network connections

**Input Parameters**:
- Job description
- Target company/recruiter information

**Output**:
- 5 variations of LinkedIn outreach messages

#### 5c. **Thank You Email**
**Endpoint**: `/api/generate/thank-you-email`

**Key Functions**:
- Generate 5 distinct post-interview thank you emails
- Reinforce interest and qualifications
- Professional follow-up communication

**Input Parameters**:
- Job description
- Interview details (optional)

**Output**:
- 5 variations of thank you emails

---

## Prompt Design Philosophy

### 1. **Clarity & Specificity**

Each prompt contains explicit instructions:

- What to analyze (resume vs. job description)
- How to structure the output
- Quality standards to maintain
- Edge cases to handle

### 2. **Context Integration**

Prompts incorporate:

- Full resume content for analysis
- Complete job descriptions for matching
- User preferences and customization options
- Industry-specific terminology

### 3. **Quality Assurance**

Built-in quality measures:

- Instruction for fact-checking
- Rules for maintaining accuracy
- Guidelines for professional tone
- Constraints on content modification

### 4. **Optimization Focus**

Each prompt emphasizes:

- ATS keyword optimization
- Recruiter engagement
- Relevance scoring
- Competitive positioning

---

## How Prompts Are Used

### Processing Flow

1. **User Input**:
   - User uploads resume and job description
   - Optional: specifies customization preferences

2. **Prompt Selection**:
   - System determines which prompt(s) to use based on user request
   - RESUME.md for resume optimization
   - COVER.md for cover letter generation
   - SKILLS.md for skills/certifications recommendations
   - Interview preparation prompts for interview questions
   - Networking prompts for outreach communications

3. **Prompt Execution**:
   - Template is loaded and populated with user data
   - LLM receives the combined prompt and user content
   - Model generates optimized output

4. **Output Generation**:
   - Optimized resume or cover letter created
   - Formatting applied (Markdown, PDF, etc.)
   - Result returned to user

### Example Prompt Execution

```shell
Input:
├── Prompt Template (RESUME.md)
├── Original Resume (user-uploaded)
└── Job Description (user-provided)
    ↓
[LLM Processing]
    ↓
Output: Optimized Resume with enhanced relevance
```

---

## Prompt Customization

### Extending Prompts

To create new optimization scenarios:

1. **Analyze Requirements**: What transformation is needed?
2. **Study Existing Prompts**: Review RESUME.md and COVER.md structure
3. **Create New Template**: Follow the established format
4. **Include Clear Instructions**: Specify inputs, process, outputs
5. **Test Thoroughly**: Verify with sample inputs

### Modifying Existing Prompts

When improving prompts:

1. Document the rationale for changes
2. Test with various input combinations
3. Compare output quality before/after
4. Update supporting documentation
5. Version control all changes

---

## Quality Metrics

Prompts are evaluated on:

- **Relevance**: Does output match job requirements?
- **Accuracy**: Are facts and dates preserved?
- **Professionalism**: Does output maintain appropriate tone?
- **Completeness**: Are all inputs properly addressed?
- **Readability**: Is formatting clear and organized?

## Technical Details

### Prompt Format

Prompts are stored in Markdown (`.md`) format for:

- Human readability
- Version control compatibility
- Easy integration with processing pipeline
- Support for complex formatting

### Integration Points

Prompts connect to:

- Backend API endpoints
- LLM service provider
- File storage system
- Output formatting engine

## Best Practices for Prompt Engineering

### Do

✅ Be explicit about desired outputs
✅ Include contextual constraints
✅ Provide clear structural requirements
✅ Reference specific content to analyze
✅ Include quality assurance instructions

### Don't

❌ Use vague or ambiguous language
❌ Assume the model understands implicit requirements
❌ Include conflicting instructions
❌ Omit important context or constraints
❌ Ignore edge cases and error handling

---

### Regular Review

- Monthly: Quality assessment of outputs
- Quarterly: Prompt effectiveness evaluation
- Annually: Comprehensive prompt audit

### Version Control

- All changes tracked in Git
- Commit messages document improvements
- Rollback capability for problematic changes

### Feedback Loop

- Collect user feedback on output quality
- Monitor LLM performance metrics
- Iterate on prompt design based on results

## Examples

### Resume Optimization Example

```plaintext
Input Resume: Generic entry-level engineer resume
Input Job Description: Senior Full-Stack Developer, AWS expertise required

Processing:
- Identify relevant AWS experience
- Enhance technical skills section
- Rewrite accomplishments to match requirements
- Optimize keyword density for ATS

Output: Resume with emphasized cloud infrastructure experience
```

### Cover Letter Generation Example

```plaintext
Input: Candidate background, target company (TechCorp), role (Engineering Manager)

Processing:
- Tailor introduction to TechCorp culture
- Highlight management experience
- Connect past achievements to role requirements
- Create compelling closing statement

Output: Personalized cover letter specific to opportunity
```

### Skills Development Example

```plaintext
Input: Job Title: DevOps Engineer
Input: Job Description requires AWS, Kubernetes, CI/CD, Python

Processing:
- Identify certifications (AWS Solutions Architect, CKA)
- Recommend hands-on projects (deploy K8s cluster, CI/CD pipeline)
- Create learning path (0-6 months)
- Estimate time and cost for each certification

Output: Comprehensive skills development plan with certifications and projects
```

### Interview Preparation Example

```plaintext
Input: Job Description for Software Engineering Manager

Processing (HR Questions):
- Generate behavioral questions (leadership, conflict resolution)
- Create situational questions (team management scenarios)
- Include cultural fit questions

Output: 5 HR interview questions specific to management role

Processing (Job-Specific Questions):
- Generate technical questions (architecture decisions, code review)
- Create scenario-based questions (sprint planning, technical debt)
- Include leadership questions (team growth, mentoring)

Output: 5 role-specific interview questions
```

### Networking Outreach Example

```plaintext
Input: Job Description for Senior Developer at TechStartup

Processing (Cold Email):
- Research company culture and values
- Highlight relevant experience
- Create compelling introduction
- Include clear call-to-action

Output: 5 variations of professional cold emails

Processing (LinkedIn Message):
- Craft concise, LinkedIn-appropriate message
- Reference mutual connections if applicable
- Express genuine interest
- Request connection or conversation

Output: 5 variations of LinkedIn outreach messages
```

## Troubleshooting

**Output Lacks Relevance**:

- Verify job description is complete and clear
- Check that resume contains relevant experience
- Review prompt instructions for clarity

**Generated Content Seems Generic**:

- Ensure prompts include specific details
- Provide complete context to LLM
- Check for prompt versioning issues

**Formatting Issues**:

- Verify Markdown syntax in templates
- Test output rendering in target format
- Review post-processing steps

## Contributing

To improve prompts:

1. Identify area for enhancement
2. Create experimental version
3. Test with sample data
4. Document improvements
5. Submit for review

---

**Directory**: `prompts/`
**Contains**: Prompt templates for LLM optimization
**Format**: Markdown (`.md`)
**Scope**: Resume and cover letter generation
**Version**: 1.0
**Last Updated**: January 17, 2026
**Maintenance**: Ongoing optimization and improvement
