# Git Hooks Setup Guide

This project uses automated git hooks to enforce code quality standards using **Spotless**, **Checkstyle**, and **SpotBugs**.

- [Git Hooks Setup Guide](#git-hooks-setup-guide)
  - [Installation](#installation)
  - [What Each Hook Does](#what-each-hook-does)
    - [Pre-Commit Hook](#pre-commit-hook)
    - [Pre-Push Hook](#pre-push-hook)
  - [Common Tasks](#common-tasks)
    - [Format Code Automatically](#format-code-automatically)
    - [Run Individual Checks](#run-individual-checks)
    - [Run All Quality Checks](#run-all-quality-checks)
    - [Skip Hooks (Not Recommended)](#skip-hooks-not-recommended)
  - [Viewing Reports](#viewing-reports)
    - [Checkstyle Report](#checkstyle-report)
    - [SpotBugs Report](#spotbugs-report)
    - [Test Report](#test-report)
  - [Understanding Violations](#understanding-violations)
    - [Checkstyle Violations](#checkstyle-violations)
    - [SpotBugs Warnings](#spotbugs-warnings)
    - [Spotless Issues](#spotless-issues)
  - [Customization](#customization)
    - [Modify Checkstyle Rules](#modify-checkstyle-rules)
    - [Modify SpotBugs Rules](#modify-spotbugs-rules)
    - [Modify Spotless Rules](#modify-spotless-rules)
  - [Troubleshooting](#troubleshooting)
    - [Hooks Not Running](#hooks-not-running)
  - [Best Practices](#best-practices)
  - [Integration with IDEs](#integration-with-ides)
    - [IntelliJ IDEA / Android Studio](#intellij-idea--android-studio)
    - [VS Code](#vs-code)
  - [References](#references)

---

## Installation

Git hooks are automatically installed when you build the project:

```bash
./gradlew build
```

Or manually install them:

```bash
./gradlew setupGitHooks
```

This creates two git hooks:

- `.git/hooks/pre-commit` - Runs before committing
- `.git/hooks/pre-push` - Runs before pushing

## What Each Hook Does

### Pre-Commit Hook

Runs when you execute `git commit`:

1. **Spotless Format Check** (`spotlessCheck`)
   - Validates Java code formatting
   - If issues found, run: `./gradlew spotlessApply` to auto-fix
   - Then re-stage and commit

2. **Checkstyle** (`checkstyleMain`)
   - Enforces coding standards from `config/checkstyle/checkstyle.xml`
   - Max line length: 120 characters
   - Naming conventions, imports organization, etc.
   - Must fix manually; violations block commits

3. **SpotBugs** (`spotbugsMain`) - Warning only
   - Analyzes code for potential bugs
   - Review report: `build/reports/spotbugs/main.html`
   - Does NOT block commits (informational)

### Pre-Push Hook

Runs when you execute `git push`:

1. Full quality suite: `./gradlew clean check spotlessCheck spotbugsMain`
2. All tests: `./gradlew test`
3. Blocks push if any checks fail

## Common Tasks

### Format Code Automatically

```bash
./gradlew spotlessApply
```

This auto-fixes formatting issues found by Spotless (Google Java Format).

### Run Individual Checks

```bash
# Spotless check (no fixes)
./gradlew spotlessCheck

# Spotless apply (auto-fixes)
./gradlew spotlessApply

# Checkstyle
./gradlew checkstyleMain

# SpotBugs analysis
./gradlew spotbugsMain
```

### Run All Quality Checks

```bash
./gradlew check
```

### Skip Hooks (Not Recommended)

Skip pre-commit hook:

```bash
git commit --no-verify
```

Skip pre-push hook:

```bash
git push --no-verify
```

## Viewing Reports

After running quality checks, view the reports:

### Checkstyle Report

```
build/reports/checkstyle/main.html
```

### SpotBugs Report

```
build/reports/spotbugs/main.html
```

### Test Report

```
build/reports/tests/test/index.html
```

## Understanding Violations

### Checkstyle Violations

- **Line length**: Max 120 characters
- **Imports**: Must be organized and not use `.*`
- **Naming**: Variables, methods follow camelCase; classes follow PascalCase
- **Indentation**: 4 spaces (no tabs)
- **Javadoc**: Public methods and classes should have documentation

### SpotBugs Warnings

Common patterns to watch:

- **NP (Null Pointer)**: Potential null pointer dereference
- **RC (Resource Leak)**: Resource not properly closed
- **BIT (Bitwise)**: Questionable bitwise operation
- **REC (Exception)**: Generic exception caught
- **SQL (SQL Injection)**: Potential SQL injection
- **UC (Unchecked Cast)**: Unchecked type cast

### Spotless Issues

Usually related to:

- Line length exceeding limits
- Incorrect import organization
- Inconsistent whitespace
- Trailing whitespace

Run `./gradlew spotlessApply` to auto-fix most issues.

## Customization

### Modify Checkstyle Rules

Edit: `config/checkstyle/checkstyle.xml`

### Modify SpotBugs Rules

Edit: `config/spotbugs/spotbugs-exclude.xml`

### Modify Spotless Rules

Edit: `build.gradle` (search for `spotless { }` block)

## Troubleshooting

### Hooks Not Running

Verify they're executable:

````bash

Both `pre-commit` and `pre-push` should have `x` (execute) permission.
Reinstall:

```bash
./gradlew setupGitHooks

### Different Behavior on Windows vs Mac/Linux


- Git for Windows installed
- Using Git Bash terminal (not Command Prompt)

### Gradle Build Issues


```bash
./gradlew clean
````

Rebuild with fresh dependencies:

```bash
./gradlew clean build
```

## Best Practices

1. **Run checks locally before pushing**

   ```bash
   ./gradlew clean check
   ```

2. **Auto-format before committing**

   ```bash
   ./gradlew spotlessApply
   ```

3. **Review SpotBugs reports regularly**
   - Aim to keep warnings minimal
   - Don't ignore warnings without understanding them

4. **Keep code quality consistent**
   - Follow established patterns in codebase
   - Use IDE plugins for real-time checks (ESLint, Checkstyle Plugin)

5. **Commit frequently with small, focused changes**
   - Makes it easier to fix issues
   - Cleaner history for debugging

## Integration with IDEs

### IntelliJ IDEA / Android Studio

1. **Checkstyle Plugin**
   - Install: Plugins → Search "Checkstyle-IDEA"
   - Configure: Settings → Tools → Checkstyle
   - Select config file: `config/checkstyle/checkstyle.xml`

2. **SpotBugs Plugin**
   - Install: Plugins → Search "SpotBugs"
   - Configure: Settings → Tools → SpotBugs
   - Run: Tools → Run SpotBugs

### VS Code

1. **Checkstyle Extension**
   - Install: "Checkstyle for Java"

2. **SpotBugs for VS Code**
   - Install: "SpotBugs"

## References

- [Spotless Documentation](https://github.com/diffplug/spotless)
- [Checkstyle Configuration](https://checkstyle.org/config.html)
- [SpotBugs Documentation](https://spotbugs.readthedocs.io/)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
