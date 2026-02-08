# Git Hooks Configuration

- [Git Hooks Configuration](#git-hooks-configuration)
  - [⚙️ Configured Hooks](#️-configured-hooks)
    - [`pre-commit`](#pre-commit)
    - [`pre-push`](#pre-push)
  - [🔧 Setup](#-setup)
    - [Automatic (Recommended)](#automatic-recommended)
    - [Manual Setup](#manual-setup)
  - [🔄 How It Works](#-how-it-works)
  - [🐛 Troubleshooting](#-troubleshooting)
    - [Hooks Not Running](#hooks-not-running)
    - [Hook Execution Fails](#hook-execution-fails)
    - [Skipping Hooks (Not Recommended)](#skipping-hooks-not-recommended)
  - [✨ What Gets Auto-Fixed](#-what-gets-auto-fixed)
  - [⌨️ Manual Execution](#️-manual-execution)
  - [📋 Configuration Files](#-configuration-files)

This directory contains git hooks that ensure code quality and formatting compliance.

## ⚙️ Configured Hooks

### `pre-commit`

Runs automatically before each commit. This hook:

- **Automatically applies** `spotlessApply` to fix formatting issues
- Leaves any formatting changes unstaged; you must stage them manually
- Runs Checkstyle validation
- Runs SpotBugs analysis (warning only)

**Behavior:** If Spotless or Checkstyle fails, the commit is prevented.

### `pre-push`

Runs automatically before each push. This hook:

- **Automatically applies** `spotlessApply` to fix formatting issues
- Runs full quality checks (Checkstyle, SpotBugs)
- Executes the complete test suite
- Verifies the build succeeds

**Behavior:** If any check fails, the push is prevented.

## 🔧 Setup

### Automatic (Recommended)

Git is already configured to use these hooks. No additional setup needed!

```bash
# Verify configuration (should show ".githooks")
git config core.hooksPath
```

### Manual Setup

If hooks aren't being triggered:

```bash
# Configure git to use .githooks directory
git config core.hooksPath .githooks

# For all repositories (optional)
git config --global core.hooksPath .githooks
```

## 🔄 How It Works

1. **Before each commit:**
   - `spotlessApply` automatically fixes formatting issues
   - Any changes are staged automatically
   - Commit succeeds if quality checks pass

2. **Before each push:**
   - `spotlessApply` runs again
   - Full test suite executes
   - Build verification runs
   - Push succeeds if all checks pass

## 🐛 Troubleshooting

### Hooks Not Running

```bash
# Verify hooks path is set
git config core.hooksPath

# Verify hooks are executable (on Unix/Linux)
ls -la .githooks/

# On Windows, hooks should work automatically
# If not, ensure Git Bash is installed
```

### Hook Execution Fails

If you see errors like `permission denied`:

```bash
# On Unix/Linux, make hooks executable
chmod +x .githooks/pre-commit
chmod +x .githooks/pre-push
```

### Skipping Hooks (Not Recommended)

To skip hooks for a single commit:

```bash
git commit --no-verify  # Skips pre-commit hook
git push --no-verify   # Skips pre-push hook
```

## ✨ What Gets Auto-Fixed

The `spotlessApply` task automatically fixes:

- ✓ Line length violations
- ✓ Trailing whitespace
- ✓ Indentation issues
- ✓ Blank line violations
- ✓ Other formatting issues defined in `.editorconfig` and `spotless.gradle`

## ⌨️ Manual Execution

Run these commands manually anytime:

```bash
# Apply spotless formatting
./gradlew spotlessApply

# Check spotless without applying
./gradlew spotlessCheck

# Run all pre-commit checks
./gradlew spotlessApply checkstyleMain spotbugsMain

# Run all pre-push checks
./gradlew spotlessApply check spotbugsMain test build
```

## 📋 Configuration Files

- `.editorconfig` - EditorConfig formatting rules
- `spotless.gradle` - Spotless configuration
- `.checkstyle` - Checkstyle configuration
- `.spotbugsexclude.xml` - SpotBugs exclusions

---

**Last Updated:** February 5, 2026
