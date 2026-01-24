#!/bin/bash
# Setup git hooks for the project
# This script installs git hooks from .githooks directory

set -e

REPO_ROOT="$(git rev-parse --show-toplevel)"
HOOKS_SOURCE_DIR="$REPO_ROOT/.githooks"
HOOKS_TARGET_DIR="$REPO_ROOT/.git/hooks"

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo ""
echo -e "${BLUE}╔════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║      Setting Up Git Hooks              ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════╝${NC}"
echo ""

# Check if .githooks directory exists
if [ ! -d "$HOOKS_SOURCE_DIR" ]; then
    echo -e "${RED}✗ Error: .githooks directory not found!${NC}"
    echo "Expected: $HOOKS_SOURCE_DIR"
    exit 1
fi

# Create .git/hooks directory if it doesn't exist
mkdir -p "$HOOKS_TARGET_DIR"

# Install hooks
HOOKS=("pre-commit" "pre-push")
FAILED=0

for hook in "${HOOKS[@]}"; do
    SOURCE="$HOOKS_SOURCE_DIR/$hook"
    TARGET="$HOOKS_TARGET_DIR/$hook"

    if [ ! -f "$SOURCE" ]; then
        echo -e "${RED}✗ Hook not found: $SOURCE${NC}"
        FAILED=$((FAILED + 1))
        continue
    fi

    # Copy and make executable
    cp "$SOURCE" "$TARGET"
    chmod +x "$TARGET"

    echo -e "${GREEN}✓ Installed: $hook${NC}"
done

echo ""

if [ $FAILED -eq 0 ]; then
    echo -e "${GREEN}╔════════════════════════════════════════╗${NC}"
    echo -e "${GREEN}║  ✓ All git hooks installed successfully! ║${NC}"
    echo -e "${GREEN}╚════════════════════════════════════════╝${NC}"
    echo ""
    echo -e "${YELLOW}Installed hooks:${NC}"
    for hook in "${HOOKS[@]}"; do
        echo "  • $hook"
    done
    echo ""
    echo -e "${YELLOW}To skip hooks temporarily:${NC}"
    echo "  git commit --no-verify"
    echo "  git push --no-verify"
    echo ""
    exit 0
else
    echo -e "${RED}✗ Failed to install some hooks${NC}"
    exit 1
fi
