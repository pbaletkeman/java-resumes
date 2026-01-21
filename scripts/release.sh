#!/bin/bash

# ============================================================================
# Release Script - Frontend and Backend Versioning & Git Push
# Manages version bumping, tagging, and GitHub push for both packages
# ============================================================================

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Color codes for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Function to print colored output
print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

# Function to get current version from package.json
get_frontend_version() {
    grep '"version"' "$PROJECT_ROOT/frontend/package.json" | head -1 | sed 's/.*"version"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/'
}

# Function to get current version from build.gradle
get_backend_version() {
    grep "version = " "$PROJECT_ROOT/build.gradle" | head -1 | sed "s/version = '//" | sed "s/'//g"
}

# Function to parse semantic version
parse_version() {
    local version=$1
    local major=$(echo $version | cut -d. -f1)
    local minor=$(echo $version | cut -d. -f2)
    local patch=$(echo $version | cut -d. -f3 | cut -d- -f1)

    echo "$major $minor $patch"
}

# Function to bump version
bump_version() {
    local version=$1
    local bump_type=$2

    read major minor patch <<< "$(parse_version $version)"

    case $bump_type in
        major)
            major=$((major + 1))
            minor=0
            patch=0
            ;;
        minor)
            minor=$((minor + 1))
            patch=0
            ;;
        patch)
            patch=$((patch + 1))
            ;;
        *)
            print_error "Invalid bump type: $bump_type"
            return 1
            ;;
    esac

    echo "${major}.${minor}.${patch}"
}

# Function to update frontend version
update_frontend_version() {
    local new_version=$1
    local package_json="$PROJECT_ROOT/frontend/package.json"

    # Use sed to update version in package.json
    sed -i "s/\"version\": \"[^\"]*\"/\"version\": \"$new_version\"/" "$package_json"
    print_success "Updated frontend version to $new_version"
}

# Function to update backend version
update_backend_version() {
    local new_version=$1
    local build_gradle="$PROJECT_ROOT/build.gradle"

    # Use sed to update version in build.gradle
    sed -i "s/version = '[^']*'/version = '$new_version'/" "$build_gradle"
    print_success "Updated backend version to $new_version"
}

echo ""
echo "======================================================================"
echo "Release Management Script"
echo "======================================================================"
echo ""

# Check if git is initialized
if [ ! -d "$PROJECT_ROOT/.git" ]; then
    print_error "Git repository not initialized in $PROJECT_ROOT"
    exit 1
fi

cd "$PROJECT_ROOT"

# Get current versions
FRONTEND_VERSION=$(get_frontend_version)
BACKEND_VERSION=$(get_backend_version)

print_info "Current versions:"
echo "  Frontend: v$FRONTEND_VERSION"
echo "  Backend:  v$BACKEND_VERSION"
echo ""

# Check for uncommitted changes
if ! git diff-index --quiet HEAD --; then
    print_warning "Uncommitted changes detected. Please commit them first."
    git status --short
    exit 1
fi

# Menu
echo "Release Options:"
echo "  1. Bump frontend patch version"
echo "  2. Bump frontend minor version"
echo "  3. Bump frontend major version"
echo "  4. Bump backend patch version"
echo "  5. Bump backend minor version"
echo "  6. Bump backend major version"
echo "  7. Bump both (patch)"
echo "  0. Exit"
echo ""

read -p "Select option (0-7): " option

case $option in
    1)
        NEW_FRONTEND=$(bump_version "$FRONTEND_VERSION" "patch")
        update_frontend_version "$NEW_FRONTEND"
        TAG="frontend/v$NEW_FRONTEND"
        ;;
    2)
        NEW_FRONTEND=$(bump_version "$FRONTEND_VERSION" "minor")
        update_frontend_version "$NEW_FRONTEND"
        TAG="frontend/v$NEW_FRONTEND"
        ;;
    3)
        NEW_FRONTEND=$(bump_version "$FRONTEND_VERSION" "major")
        update_frontend_version "$NEW_FRONTEND"
        TAG="frontend/v$NEW_FRONTEND"
        ;;
    4)
        NEW_BACKEND=$(bump_version "$BACKEND_VERSION" "patch")
        update_backend_version "$NEW_BACKEND"
        TAG="backend/v$NEW_BACKEND"
        ;;
    5)
        NEW_BACKEND=$(bump_version "$BACKEND_VERSION" "minor")
        update_backend_version "$NEW_BACKEND"
        TAG="backend/v$NEW_BACKEND"
        ;;
    6)
        NEW_BACKEND=$(bump_version "$BACKEND_VERSION" "major")
        update_backend_version "$NEW_BACKEND"
        TAG="backend/v$NEW_BACKEND"
        ;;
    7)
        NEW_FRONTEND=$(bump_version "$FRONTEND_VERSION" "patch")
        NEW_BACKEND=$(bump_version "$BACKEND_VERSION" "patch")
        update_frontend_version "$NEW_FRONTEND"
        update_backend_version "$NEW_BACKEND"
        TAG="v$NEW_FRONTEND-$NEW_BACKEND"
        ;;
    0)
        print_info "Exiting..."
        exit 0
        ;;
    *)
        print_error "Invalid option"
        exit 1
        ;;
esac

echo ""
echo "Preparing release..."
echo ""

# Stage version files
git add frontend/package.json build.gradle

# Create commit
read -p "Enter commit message [Release $TAG]: " commit_msg
commit_msg="${commit_msg:-Release $TAG}"

git commit -m "$commit_msg"
print_success "Committed changes"

# Create tag
git tag -a "$TAG" -m "Release $TAG"
print_success "Created tag: $TAG"

echo ""
echo "======================================================================"
echo "Ready to push to GitHub"
echo "======================================================================"
echo ""
echo "Changes to push:"
echo "  Tag: $TAG"
echo ""

read -p "Push to GitHub? (y/n): " push_confirm

if [ "$push_confirm" = "y" ] || [ "$push_confirm" = "Y" ]; then
    print_info "Pushing to GitHub..."

    # Push commits
    git push origin master
    print_success "Pushed commits"

    # Push tag
    git push origin "$TAG"
    print_success "Pushed tag: $TAG"

    echo ""
    print_success "Release complete!"
    echo ""
    echo "GitHub URLs:"
    echo "  Commits: https://github.com/pbaletkeman/java-resumes/commits/master"
    echo "  Release: https://github.com/pbaletkeman/java-resumes/releases/tag/$TAG"
else
    print_warning "Push cancelled. Local changes remain."
    echo ""
    echo "To push later, run:"
    echo "  git push origin master"
    echo "  git push origin $TAG"
fi

echo ""
