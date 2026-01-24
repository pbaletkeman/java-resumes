#!/usr/bin/env python3
"""
Git hooks installer for cross-platform support
Installs pre-commit and pre-push hooks for code quality checks
"""

import os
import sys
import stat
import shutil
from pathlib import Path


def get_repo_root():
    """Get the git repository root"""
    try:
        import subprocess
        result = subprocess.run(
            ["git", "rev-parse", "--show-toplevel"],
            capture_output=True,
            text=True,
            check=True
        )
        return result.stdout.strip()
    except (subprocess.CalledProcessError, FileNotFoundError):
        print("[ERROR] Not in a git repository or git not installed")
        return None


def install_hooks():
    """Install git hooks"""
    repo_root = get_repo_root()
    if not repo_root:
        return False

    hooks_source_dir = Path(repo_root) / ".githooks"
    hooks_target_dir = Path(repo_root) / ".git" / "hooks"

    print()
    print("╔════════════════════════════════════════╗")
    print("║       Setting Up Git Hooks            ║")
    print("╚════════════════════════════════════════╝")
    print()

    # Check if .githooks directory exists
    if not hooks_source_dir.exists():
        print(f"[ERROR] .githooks directory not found!")
        print(f"Expected: {hooks_source_dir}")
        return False

    # Create .git/hooks directory if it doesn't exist
    hooks_target_dir.mkdir(parents=True, exist_ok=True)

    # Install hooks
    hooks = ["pre-commit", "pre-push"]
    failed = 0

    for hook in hooks:
        source = hooks_source_dir / hook
        target = hooks_target_dir / hook

        if not source.exists():
            print(f"[ERROR] Hook not found: {source}")
            failed += 1
            continue

        # Copy hook
        shutil.copy2(source, target)

        # Make executable (Unix-like systems)
        st = os.stat(target)
        os.chmod(target, st.st_mode | stat.S_IXUSR | stat.S_IXGRP | stat.S_IXOTH)

        print(f"[✓] Installed: {hook}")

    print()

    if failed == 0:
        print("╔════════════════════════════════════════╗")
        print("║  ✓ All git hooks installed successfully!║")
        print("╚════════════════════════════════════════╝")
        print()
        print("Installed hooks:")
        for hook in hooks:
            print(f"  • {hook}")
        print()
        print("To skip hooks temporarily:")
        print("  git commit --no-verify")
        print("  git push --no-verify")
        print()
        return True
    else:
        print(f"[ERROR] Failed to install {failed} hook(s)")
        return False


if __name__ == "__main__":
    success = install_hooks()
    sys.exit(0 if success else 1)
