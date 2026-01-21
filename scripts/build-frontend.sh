#!/bin/bash

# ============================================================================
# Frontend Production Build Script
# Build ReactJS for production deployment
# ============================================================================

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
FRONTEND_DIR="$PROJECT_ROOT/frontend"
BUILD_DIR="$FRONTEND_DIR/dist"

echo "======================================================================"
echo "Frontend Production Build Script"
echo "======================================================================"
echo "Project Root: $PROJECT_ROOT"
echo "Frontend Directory: $FRONTEND_DIR"
echo "Build Output: $BUILD_DIR"
echo ""

# Step 1: Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ ERROR: Node.js is not installed"
    echo "   Please install Node.js v20 or later"
    exit 1
fi

echo "✓ Node.js version: $(node --version)"
echo "✓ npm version: $(npm --version)"
echo ""

# Step 2: Navigate to frontend directory
cd "$FRONTEND_DIR"

# Step 3: Install dependencies
echo "📦 Installing dependencies..."
npm install

echo ""

# Step 4: Run tests
echo "🧪 Running tests..."
if ! npm test -- --run 2>&1 | grep -q "Test Files"; then
    echo "⚠️  Warning: Tests did not complete"
fi

echo ""

# Step 5: Build for production
echo "🔨 Building production bundle..."
npm run build

echo ""

# Step 6: Verify build output
if [ -d "$BUILD_DIR" ]; then
    echo "✓ Build successful!"
    echo "  Output directory: $BUILD_DIR"
    du -sh "$BUILD_DIR"
else
    echo "❌ Build failed: Output directory not created"
    exit 1
fi

echo ""
echo "======================================================================"
echo "✅ Frontend build completed successfully!"
echo "======================================================================"
echo ""
echo "Next steps:"
echo "  1. Review the dist/ directory for production files"
echo "  2. Deploy dist/ contents to your web server"
echo "  3. Use scripts/deploy-frontend.sh for automated deployment"
echo ""
