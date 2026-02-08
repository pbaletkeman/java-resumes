# Quick Start Guide - Git Hooks & Code Quality

- [Quick Start Guide - Git Hooks \& Code Quality](#quick-start-guide---git-hooks--code-quality)
  - [One-Time Setup](#one-time-setup)
    - [Option 1: Gradle (Recommended)](#option-1-gradle-recommended)
    - [Option 2: Shell Script (Mac/Linux)](#option-2-shell-script-maclinux)
    - [Option 3: Batch Script (Windows)](#option-3-batch-script-windows)
    - [Option 4: Python (Cross-Platform)](#option-4-python-cross-platform)
  - [📋 Daily Workflow](#-daily-workflow)
    - [Before Committing](#before-committing)
    - [Before Pushing](#before-pushing)
    - [To Bypass Hooks (Not Recommended)](#to-bypass-hooks-not-recommended)
  - [📊 Common Quality Commands](#-common-quality-commands)
  - [📈 Viewing Reports](#-viewing-reports)
  - [What Each Tool Does](#what-each-tool-does)
  - [Troubleshooting](#troubleshooting)
  - [See Also](#see-also)

---

This is a quick reference for setting up and using the automated code quality checks.

## One-Time Setup

### Option 1: Gradle (Recommended)

```bash
./gradlew setupGitHooks
```

### Option 2: Shell Script (Mac/Linux)

```bash
bash setup-hooks.sh
```

### Option 3: Batch Script (Windows)

```cmd
setup-hooks.bat
```

### Option 4: Python (Cross-Platform)

```bash
python setup-hooks.py
```

## 📋 Daily Workflow

### Before Committing

```bash
# Auto-format code
./gradlew spotlessApply

# Commit your changes (pre-commit hook will run automatically)
git add .
git commit -m "feat: your feature description"
```

### Before Pushing

```bash
# Pre-push hook will run automatically when you push
git push origin branch-name
```

### To Bypass Hooks (Not Recommended)

```bash
# Skip pre-commit hook
git commit --no-verify

# Skip pre-push hook
git push --no-verify
```

## 📊 Common Quality Commands

```bash
# Format code automatically
./gradlew spotlessApply

# Check formatting without changes
./gradlew spotlessCheck

# Run Checkstyle
./gradlew checkstyleMain

# Run SpotBugs
./gradlew spotbugsMain

# Run all checks
./gradlew check

# Run tests
./gradlew test

# Full pre-push verification
./gradlew clean check test
```

## 📈 Viewing Reports

- **Checkstyle**: `build/reports/checkstyle/main.html`
- **SpotBugs**: `build/reports/spotbugs/main.html`
- **Tests**: `build/reports/tests/test/index.html`
- **Coverage**: `build/reports/jacoco/test/html/index.html`

## What Each Tool Does

| Tool           | Purpose                | Action                               |
| -------------- | ---------------------- | ------------------------------------ |
| **Spotless**   | Auto-format code       | Blocks commit if format issues found |
| **Checkstyle** | Enforce code standards | Blocks commit if violations found    |
| **SpotBugs**   | Find potential bugs    | Review-only, won't block commits     |

## Troubleshooting

**Hooks not running?**

- Run setup again: `./gradlew setupGitHooks`
- Check hooks are executable: `ls -la .git/hooks/`

**On Windows, hooks not working?**

- Use Git Bash terminal (not Command Prompt)
- Or use Python: `python setup-hooks.py`

**Need to fix formatting before hook passes?**

- Run: `./gradlew spotlessApply`
- Re-stage changes: `git add .`
- Try commit again

**Too many violations to fix manually?**

- Spotless auto-fixes many issues
- For Checkstyle: Review and fix manually, or ask for help
- SpotBugs: Review report, but won't block commits

## See Also

- [Full Setup Guide](SETUP_GIT_HOOKS.md)
- [Backend Instructions](.github/instructions/backend.instructions.md)
- [Checkstyle Rules](config/checkstyle/checkstyle.xml)
- [SpotBugs Exclusions](config/spotbugs/spotbugs-exclude.xml)
