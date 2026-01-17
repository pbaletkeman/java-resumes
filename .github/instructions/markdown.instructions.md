---
applyTo: "**/*.md"
---

# Markdown Documentation Instructions

## Overview

This guide establishes comprehensive standards for creating and maintaining markdown documentation in the java-resumes project. These instructions apply to all `.md` files in the repository, ensuring consistency, clarity, and professional quality across all documentation.

**Purpose:**

- Maintain consistent formatting across all markdown files
- Ensure documentation is clear, navigable, and user-friendly
- Establish standards that complement existing project guidelines
- Support both human readers and automated documentation systems

**Scope:**

These guidelines apply to:

- Root-level markdown files (README.md, AGENTS.md, copilot-instructions.md, etc.)
- Documentation in `docs/` subdirectory
- All path-specific instruction files in `.github/instructions/`
- Any new markdown files created during development

---

## Table of Contents

- [Document Structure](#document-structure)
- [Table of Contents Requirements](#table-of-contents-requirements)
- [Heading Hierarchy](#heading-hierarchy)
- [Code Blocks and Syntax Highlighting](#code-blocks-and-syntax-highlighting)
- [Lists and Formatting](#lists-and-formatting)
- [Links and Cross-References](#links-and-cross-references)
- [Tables](#tables)
- [Special Formatting Elements](#special-formatting-elements)
- [Documentation Completeness](#documentation-completeness)
- [Examples from Project](#examples-from-project)
- [Best Practices Checklist](#best-practices-checklist)

---

## Document Structure

Every markdown document should follow this logical structure:

### 1. Document Title (H1)

- Use a single `# Heading` as the main title
- Title should be descriptive and reflect document purpose
- Separate title from content with blank line

**Example:**

```markdown
# Markdown Documentation Instructions
```

### 2. Opening Description

- Include 2-3 sentences explaining document purpose
- State the intended audience
- Link to related documentation if applicable

**Example:**

```markdown
This guide establishes comprehensive standards for creating and maintaining markdown
documentation in the java-resumes project. These instructions apply to all `.md`
files in the repository.
```

### 3. Table of Contents (if document is 100+ lines)

- Required for markdown files exceeding 100 lines
- Place immediately after opening description
- Use 2-level hierarchy (H2 and H3 sections only)
- Include horizontal rule (`---`) after TOC

### 4. Content Sections

- Use consistent heading hierarchy (H2 for main sections, H3 for subsections)
- Separate major sections with horizontal rules
- Include clear explanations before code examples
- Use proper linking between sections

### 5. Footer Information

- Optional: Version history or last updated date
- Optional: Contributing guidelines or contact information
- Document the maintenance schedule

---

## Table of Contents Requirements

### When to Include a Table of Contents

- **Required**: All markdown files with 100+ lines
- **Optional**: Markdown files with 50-100 lines (recommended for organization)
- **Not Required**: Small markdown files under 50 lines

### Table of Contents Format

```markdown
## Table of Contents

- [Section 1](#section-1)
  - [Subsection 1.1](#subsection-11)
  - [Subsection 1.2](#subsection-12)
- [Section 2](#section-2)
  - [Subsection 2.1](#subsection-21)
- [Section 3](#section-3)

---
```

### Anchor Link Rules

- Convert heading text to lowercase
- Replace spaces with hyphens
- Remove special characters (keep only alphanumeric and hyphens)
- Order links to match document structure exactly
- Use relative paths for cross-document linking

**Examples:**

```markdown
## Getting Started → [Getting Started](#getting-started)

## API Endpoints (v2) → [API Endpoints (v2)](#api-endpoints-v2)

### Configuration Options → [Configuration Options](#configuration-options)
```

### Placement Rules

- Position TOC after opening description
- Precede TOC with a horizontal rule (`---`)
- Follow TOC with a horizontal rule (`---`)
- Never embed TOC content; it's navigation only

---

## Heading Hierarchy

### Proper Hierarchy Structure

```
# Main Title (Document Title - H1)
  Text content here

## Major Section (H2)
  Text content here

### Subsection (H3)
  Text content here

#### Minor Topic (H4)
  Use sparingly; prefer keeping to H2/H3
```

### Heading Rules

**✅ DO:**

- Start document with single `# Title`
- Use `##` (H2) for major sections
- Use `###` (H3) for subsections
- Maintain consistent hierarchy throughout
- Use heading hierarchy to reflect content organization
- Include blank lines before and after headings

**❌ DON'T:**

- Use multiple `#` titles in one document
- Skip heading levels (e.g., H2 directly to H4)
- Use heading hierarchy inconsistently
- Create heading-only documents without content
- Use decorative headings without substantive content

### Section Separation

Separate major sections with horizontal rules:

```markdown
---

## Next Major Section
```

---

## Code Blocks and Syntax Highlighting

### Code Block Formatting

Always use fenced code blocks with language specification:

````markdown
```language
code content here
```
````

### Supported Languages

**Backend:**

```
java, gradle, xml, sql, yaml, yml, properties
```

**Frontend:**

```
typescript, tsx, javascript, jsx, html, css, scss, json
```

**Build & Config:**

```
bash, shell, sh, dockerfile, makefile, json, yaml, yml
```

**Documentation:**

```
markdown, plaintext, text
```

### Code Block Best Practices

**✅ DO:**

- Specify the programming language (bash, java, typescript, etc.)
- Include complete, working code examples
- Use comments to explain non-obvious code
- Keep examples focused and concise
- Ensure examples are accurate and up-to-date
- Add context before complex code blocks

**❌ DON'T:**

- Use generic ```code blocks without language specification
- Include incomplete or non-working code
- Use placeholder text like `// ... rest of code`
- Create overly complex examples
- Show outdated code from previous versions

### Code Example Patterns

**Java Example:**

```java
/**
 * Optimizes resume based on job description.
 */
public String optimizeResume(String jobDesc, String resume) {
  if (jobDesc == null || jobDesc.isEmpty()) {
    throw new IllegalArgumentException("Job description required");
  }

  String prompt = buildPrompt(jobDesc, resume);
  return apiService.callLLM(prompt);
}
```

**TypeScript Example:**

```typescript
interface DocumentProps {
  title: string;
  onSubmit?: (content: string) => void;
}

export const DocumentUpload: React.FC<DocumentProps> = ({
  title,
  onSubmit
}) => {
  const [content, setContent] = useState("");

  const handleSubmit = () => {
    onSubmit?.(content);
  };

  return <div>{title}</div>;
};
```

**Bash Example:**

```bash
# Build and test the application
./gradlew clean build
npm run type-check && npm test
```

---

## Lists and Formatting

### Bullet Lists

Use for unordered items:

```markdown
- First item
- Second item
  - Nested item
  - Another nested item
- Third item
```

**Rules:**

- Use single dash `-` for consistency
- Indent nested items by 2 spaces
- Use blank lines between list items for readability
- Maintain consistent indentation

### Numbered Lists

Use for sequential steps or prioritized items:

```markdown
1. First step
2. Second step
   - Sub-point
   - Another sub-point
3. Third step
```

**Rules:**

- Use numbers with periods (1., 2., 3.)
- Nest sub-items with 3 spaces
- Use blank lines between items for readability
- Ensure numbered sequence is logically ordered

### Mixed Lists

Combine bullets and numbers appropriately:

```markdown
**Setup Steps:**

1. Install dependencies
   - npm install
   - gradle dependencies
2. Configure environment
   - Copy .env.example to .env
   - Update API keys
3. Run tests
   - Backend: ./gradlew test
   - Frontend: npm test
```

### Text Formatting

```markdown
**Bold text** for emphasis
_Italic text_ for definitions
`inline code` for variable/method names
~~Strikethrough~~ for corrections
```

**Usage:**

- **Bold**: Technical terms, important concepts, headings
- _Italic_: File names, directory paths, emphasis
- `Code`: Variable names, method names, file names inline
- Strikethrough: Deprecated features, removed items

---

## Links and Cross-References

### Absolute Links (External)

```markdown
[Link Text](https://example.com)
```

**Usage:**

- External websites
- GitHub repositories
- Official documentation
- Public resources

**Example:**

```markdown
See the [Spring Boot Documentation](https://spring.io/projects/spring-boot)
for more information.
```

### Relative Links (Internal)

```markdown
[Link Text](relative/path/to/file.md)
```

**Rules:**

- Use forward slashes `/` (works on all platforms)
- Link from current file to target file
- Include file extension `.md`
- Use relative paths, not absolute

**Examples:**

```markdown
[Backend Guide](../docs/BACKEND_README.md)
[Installation](./SETUP.md)
[API Reference](#api-endpoints)
```

### Anchor Links (Same File)

```markdown
[Jump to Section](#section-name)
```

**Rules:**

- Use heading text as anchor target
- Convert to lowercase, replace spaces with hyphens
- Link from Table of Contents to sections
- Use for internal navigation in long documents

**Example:**

```markdown
## Table of Contents

- [Installation](#installation)
- [Configuration](#configuration)

## Installation

Instructions here...

## Configuration

Instructions here...
```

### Cross-File References

Link to sections in other files:

```markdown
[Section in Other File](docs/BACKEND_README.md#api-endpoints)
```

**Pattern:**

- File path + `#` + anchor name
- Anchor is derived from heading in target file

---

## Tables

### Table Format

```markdown
| Column 1 | Column 2 | Column 3 |
| -------- | -------- | -------- |
| Data 1a  | Data 1b  | Data 1c  |
| Data 2a  | Data 2b  | Data 2c  |
```

### Table Rules

**✅ DO:**

- Use pipes `|` to separate columns
- Include header row (with separator row using hyphens)
- Align columns using hyphens (3+ per column)
- Left-align text (default)
- Right-align numbers with `:---:` or `---:`
- Center important headers with `:---:`

**❌ DON'T:**

- Create tables with inconsistent column counts
- Use single hyphens in separator row
- Leave columns misaligned
- Create overly complex nested tables

### Table Alignment

```markdown
| Left Aligned | Center Aligned | Right Aligned |
| :----------- | :------------: | ------------: |
| Text         |   Important    |            99 |
| Content      |      Data      |           150 |
```

### Table Examples

**Technology Stack Table:**

```markdown
| Component | Technology  | Version    |
| --------- | ----------- | ---------- |
| Backend   | Spring Boot | 3.5.1      |
| Frontend  | React       | 19.2.0     |
| Build     | Gradle/npm  | 8.7/latest |
```

**API Endpoints Table:**

```markdown
| Method | Endpoint        | Description     |
| ------ | --------------- | --------------- |
| POST   | /api/upload     | Upload document |
| GET    | /api/files      | List files      |
| DELETE | /api/files/{id} | Delete file     |
```

---

## Special Formatting Elements

### Horizontal Rules

Use `---` to separate major sections:

```markdown
## Section 1

Content here

---

## Section 2

Content here
```

**Rules:**

- Use before and after Table of Contents
- Separate major topic sections
- Use blank lines before and after
- Keep to 3-5 horizontal rules per document

### Block Quotes

Use for notes, warnings, and important information:

```markdown
> **Note:** This is important information that deserves emphasis.

> **Warning:** This section requires careful attention.

> **Tip:** Here's a helpful hint for users.
```

**Usage Levels:**

- **Note**: Helpful information or clarification
- **Warning**: Important cautions or potential issues
- **Tip**: Helpful suggestions and best practices

### Inline Code

Use backticks for single-word or short code references:

```markdown
Use the `npm run build` command to compile the project.
The `ResumeController` class handles API requests.
```

**Rules:**

- Use for method names: `methodName()`
- Use for class names: `ClassName`
- Use for command-line: `command-name`
- Use for file names: `filename.ext`
- Avoid backticks in headers

### Emphasis and Bold

```markdown
**Bold text** for important concepts
_Italic text_ for emphasis
**_Bold and italic_** for strong emphasis
```

**Guidelines:**

- Use bold for terms being defined
- Use italic for file/directory names
- Use for first mention of important concepts

---

## Documentation Completeness

### Required Elements by Document Type

**README Documentation:**

- Project title and badges
- Brief description
- Table of Contents (for 100+ lines)
- Problem statement or use case
- Key features and capabilities
- Quick start instructions
- Architecture overview
- Environment setup
- API/Usage documentation
- Contributing guidelines
- License information

**Technical Documentation (Backend/Frontend):**

- Purpose and scope
- Technology stack
- Directory structure
- Key components or modules
- Common operations
- Testing strategies
- Code quality standards
- Troubleshooting guides

**Instruction Files (Copilot):**

- Document purpose
- Path pattern (applyTo)
- Project overview
- Project structure
- Development guidelines
- Code patterns and examples
- Quality requirements
- Testing standards

### Completeness Checklist

Before finalizing any markdown document:

- [ ] Document has clear, descriptive title (H1)
- [ ] Opening paragraph explains purpose
- [ ] Table of Contents included (if 100+ lines)
- [ ] All section headings have content below them
- [ ] Code examples are complete and working
- [ ] Links are tested and working
- [ ] No broken internal references
- [ ] Consistent heading hierarchy throughout
- [ ] Proper code block syntax highlighting
- [ ] No spelling or grammar errors
- [ ] Consistent formatting with project style
- [ ] Updated date or version information
- [ ] All technical accuracy verified

---

## Examples from Project

### Example 1: README.md (Large Document)

The project README.md demonstrates:

- Badges and visual indicators at top
- Clear Table of Contents with emoji indicators
- Consistent use of H2 headings for major sections
- Diverse code block types (bash, java, typescript, docker)
- Emoji usage for visual navigation
- Horizontal rules separating major sections
- Tables for API endpoints and feature lists
- Clear directory structure documentation
- Setup instructions organized by method
- Link to supplementary documentation

**Key Patterns:**

````markdown
# Java Resumes - Full-Stack Resume Optimization Application

[![Badge](url)](link) - Status badges at top

## 📋 Table of Contents

- [Section](#section-name)

---

## 🌟 Main Section

Description and content here

```bash
code examples here
```
````

### Subsection

Details here

````

### Example 2: AGENTS.md (Instruction Document)

The project AGENTS.md demonstrates:
- Clear purpose statement
- Table of Contents with logical organization
- Horizontal rules separating sections
- Consistent H2/H3 hierarchy
- Practical code examples
- Checklists for verification
- Links to related documentation
- Process workflows with numbered steps

**Key Patterns:**
```markdown
# AGENTS.md - AI Agent Guidelines for java-resumes

Clear description of purpose

---

## Table of Contents
- [Section](#section)

---

## Overview
Introductory content

### Subsection
Detailed information

**Checklist:**
- [ ] Item 1
- [ ] Item 2

**Code Example:**
```language
code here
````

```

### Example 3: copilot-instructions.md (Reference Document)

The project copilot-instructions.md demonstrates:
- Detailed project overview
- Multiple document sections
- Technology stack tables
- Directory structure documentation
- Clear development guidelines
- Setup instructions
- Troubleshooting sections
- Cross-references to other files

---

## Best Practices Checklist

### Before Publishing Markdown

**Content Quality:**
- [ ] All information is accurate and current
- [ ] No outdated references or deprecated features
- [ ] Examples are tested and working
- [ ] Tone is professional and consistent
- [ ] No spelling, grammar, or syntax errors
- [ ] No invalid unicode characters
- [ ] No invalid emoji characters
- [ ] No invalid icons
- [ ] Formatting follows project conventions

**Structure and Navigation:**
- [ ] Document has clear title (H1)
- [ ] Heading hierarchy is consistent and logical
- [ ] Table of Contents is present (if 100+ lines)
- [ ] All anchor links work correctly
- [ ] Related documentation is linked
- [ ] No broken internal or external links

**Code and Examples:**
- [ ] All code blocks have language specification
- [ ] Code examples are complete and functional
- [ ] Examples match current code patterns
- [ ] Comments explain complex logic
- [ ] No placeholder text like `// ... rest`
- [ ] Examples follow project conventions

**Formatting Standards:**
- [ ] Consistent use of heading levels
- [ ] Proper use of bold, italic, and code formatting
- [ ] Lists use consistent bullet style
- [ ] Tables are well-formatted and aligned
- [ ] Horizontal rules separate major sections
- [ ] Proper spacing around all elements

**Maintenance:**
- [ ] Version or last-updated date included
- [ ] Links to related documentation present
- [ ] Instructions for updating document clear
- [ ] Maintenance schedule documented
- [ ] No personal notes or TODO comments
- [ ] Ready for public/team consumption

---

## Key Principles

### Clarity First

Write for the broadest audience that will read this documentation. Explain technical concepts clearly, define specialized terms, and provide context.

### Examples Over Explanation

Show working examples before or alongside detailed explanations. Developers learn better from code samples than from text-only explanations.

### Consistency Across Project

Match the style of existing documentation. Consistency helps readers navigate the documentation and find information quickly.

### Maintainability

Write documentation that's easy to update and maintain. Avoid hardcoding version numbers or dates that will quickly become stale.

### Completeness

Provide enough information for both beginners and experienced developers. Include setup instructions, usage examples, troubleshooting, and advanced topics.

---

## Maintenance and Updates

### When to Update Documentation

- When code patterns or standards change
- When new features are added
- When dependencies are upgraded
- When bugs are fixed (if they affected documentation accuracy)
- When user feedback indicates missing information
- Quarterly review to check accuracy

### How to Update

1. Review the document for outdated information
2. Update code examples if they've changed
3. Update version numbers and dependencies
4. Test all links and code examples
5. Update Table of Contents if structure changed
6. Update version/date information
7. Test the updated document in markdown viewer

---

## Related Documentation

For broader guidance on the java-resumes project, see:

- [copilot-instructions.md](../../copilot-instructions.md) - Repository-wide guidelines
- [AGENTS.md](../../AGENTS.md) - AI agent development guidelines
- [docs/Architecture.md](../../docs/Architecture.md) - System architecture
- [.github/instructions/backend.instructions.md](backend.instructions.md) - Backend development
- [.github/instructions/frontend.instructions.md](frontend.instructions.md) - Frontend development

---

**Last Updated:** January 17, 2026
**Next Review:** July 2026 ?
**Maintained By:** java-resumes development team
```
