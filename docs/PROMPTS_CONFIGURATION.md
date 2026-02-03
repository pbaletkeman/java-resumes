# Prompt Configuration Guide

---

## Table of Contents

1. [Overview](#overview)
2. [Default Behavior (Bundled Prompts)](#default-behavior-bundled-prompts)
3. [Using External Prompts (No Recompilation Required)](#using-external-prompts-no-recompilation-required)
4. [Updating Prompts Without Recompilation](#updating-prompts-without-recompilation)
5. [Prompt File Format](#prompt-file-format)
6. [File Structure](#file-structure)
7. [Troubleshooting](#troubleshooting)
8. [Architecture](#architecture)
9. [Best Practices](#best-practices)
10. [Quick Start](#quick-start)
11. [Adding New Prompts](#adding-new-prompts)
12. [See Also](#see-also)

---

This document explains how to configure and manage prompts in the java-resumes application.

## Overview

The application uses a **hybrid prompt loading system** that:

✅ **Loads bundled prompts** from `src/main/resources/prompts/` by default (no recompilation needed after updates)
✅ **Allows external overrides** by setting the `PROMPTS_DIR` environment variable
✅ **Falls back gracefully** if external files don't exist

## Default Behavior (Bundled Prompts)

By default, the application uses prompts bundled in the JAR:

- `src/main/resources/prompts/RESUME.md` - Resume optimization prompt
- `src/main/resources/prompts/COVER.md` - Cover letter generation prompt
- `src/main/resources/prompts/SKILLS.md` - Skills, certifications, and experience suggestions

**These prompts are NOT recompiled into the JAR.** They are read at runtime as resources, so you can:

1. Update them in your repository
2. Git push the changes
3. They take effect automatically without recompiling (assuming fresh JAR build)

## Using External Prompts (No Recompilation Required)

### Option 1: Environment Variable

Set the `PROMPTS_DIR` environment variable to point to an external prompts directory:

```bash
# Windows
set PROMPTS_DIR=C:\path\to\prompts

# Linux/Mac
export PROMPTS_DIR=/path/to/prompts
```

Then run the application:

```bash
java -jar java-resumes.jar
```

The application will:

1. Check `C:\path\to\prompts\` (or your path) for `RESUME.md` and `COVER.md`
2. If found, use those versions
3. If not found, fall back to bundled versions

### Option 2: application.yml Configuration

Edit `application.yml` to set the external prompts directory:

```yaml
prompts:
  external-dir: /path/to/prompts
```

Or use environment variable substitution (already configured):

```yaml
prompts:
  external-dir: ${PROMPTS_DIR:}
```

Leave it empty (as shown) to disable external overrides and always use bundled prompts.

### Option 3: Docker with Volume Mount

Mount an external prompts directory when running in Docker:

```bash
docker run -it \
  -e PROMPTS_DIR=/etc/java-resumes/prompts \
  -v /host/path/to/prompts:/etc/java-resumes/prompts \
  java-resumes:latest
```

## Updating Prompts Without Recompilation

### For Bundled Prompts:

1. Edit `src/main/resources/prompts/RESUME.md` or `COVER.md`
2. Git commit and push
3. Build a new JAR: `gradlew clean build`
4. Deploy the new JAR

**No code changes needed!** The prompts are just text resources.

### For External Prompts:

1. Create a `prompts/` directory with `RESUME.md` and `COVER.md`
2. Set `PROMPTS_DIR` to point to this directory
3. Update prompt files anytime—**no recompilation needed**
4. Restart the application for changes to take effect

## Prompt File Format

Both `RESUME.md` and `COVER.md` use template variables:

```markdown
# Prompt Title

...

## Input Information

**Job Title:** {job_title}
**Company:** {company}
**Job Description:** {job_description}

**Current Resume:** {resume_string}
**Today's Date:** {today}

...
```

**Example: SKILLS Prompt (doesn't require resume)**

```markdown
# Skills Recommendations

You are an expert career advisor.

## Input Information

**Job Title:** {job_title}
**Job Description:** {job_description}

## Output

Provide certifications, skills, and experiences needed.
```

These placeholders are replaced at runtime with actual values:

- `{job_title}` - The job title from user input
- `{company}` - The company name from user input
- `{job_description}` - The job description content
- `{resume_string}` - The candidate's resume content (Resume and Cover Letter only)
- `{today}` - Current date (Cover Letter only)

## File Structure

```
prompts/
├── RESUME.md          # Resume optimization prompt
├── COVER.md           # Cover letter generation prompt
└── SKILLS.md          # Skills, certifications, and experiences
```

All prompt files should be in the same directory (either bundled or external).

## Troubleshooting

### Prompts Not Updating

**Problem:** Changes to prompts aren't reflected in output.

**Solution:**

- If using bundled prompts: Rebuild the JAR (`gradlew clean build`)
- If using external prompts:
  - Verify `PROMPTS_DIR` is set correctly
  - Check file permissions
  - Ensure files exist at the specified path
  - Restart the application

### Can't Find Prompts

**Problem:** Application fails with "Could not load prompt" error.

**Solution:**

- Check logs for the error message
- Verify `PROMPTS_DIR` is set if using external prompts
- Ensure bundled resources are included in JAR (`src/main/resources/prompts/*.md`)
- Verify file names match exactly: `RESUME.md` and `COVER.md`

## Architecture

The `PromptService` class handles prompt loading:

```
PromptService.loadPrompt(promptName)
    ↓
Check external directory (if configured)
    ↓
If found → Return external version
    ↓
If not found → Check bundled resources
    ↓
Return bundled version or empty string
```

This architecture ensures:

- **Development**: Use bundled prompts for portability
- **Production**: Override with external prompts for flexibility
- **Fallback**: Always works even if external files are missing

## Best Practices

1. **Keep both versions in sync**: If using external overrides, maintain consistency with bundled versions
2. **Version control**: Commit prompt updates to Git for tracking
3. **Environment parity**: Use same prompts across dev/staging/production
4. **Validation**: Test prompt changes before deploying to production
5. **Documentation**: Keep prompt versions documented in your deployment notes

## Quick Start

### Simplest Option (Bundled):

```bash
# Edit prompts
nano src/main/resources/prompts/RESUME.md
nano src/main/resources/prompts/COVER.md
nano src/main/resources/prompts/SKILLS.md

# Build JAR
gradlew clean build

# Deploy JAR
java -jar build/libs/java-resumes.jar
```

### Advanced Option (External):

```bash
# Create external prompts directory
mkdir /opt/java-resumes/prompts

# Copy prompts there
cp src/main/resources/prompts/*.md /opt/java-resumes/prompts/

# Run with external prompts
PROMPTS_DIR=/opt/java-resumes/prompts java -jar java-resumes.jar

# Update prompts anytime (no recompilation!)
nano /opt/java-resumes/prompts/RESUME.md
nano /opt/java-resumes/prompts/SKILLS.md

# Restart application for changes to take effect
```

## Adding New Prompts

To add a new prompt (e.g., LINKEDIN for LinkedIn summaries):

1. Create `src/main/resources/prompts/LINKEDIN.md`
2. Add template variables using `{variable_name}` format
3. Update backend model and service to support the new prompt
4. Add controller endpoint for processing
5. Add frontend UI component
6. Write comprehensive unit tests (80%+ coverage)

**See [ADD_NEW_PROMPT.md](./ADD_NEW_PROMPT.md)** for detailed step-by-step instructions.

## See Also

- [API Documentation](../docs/api.md)
- [Architecture Overview](../docs/architecture/ARCHITECTURE.md)
- [Deployment Guide](../docs/DEPLOYMENT.md)

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
