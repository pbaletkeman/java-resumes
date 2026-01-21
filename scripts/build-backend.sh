#!/bin/bash

# ============================================================================
# Backend Production Build Script
# Build Spring Boot Fat JAR for production deployment
# ============================================================================

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
BUILD_DIR="$PROJECT_ROOT/build"
JAR_DIR="$BUILD_DIR/libs"

echo "======================================================================"
echo "Backend Production Build Script"
echo "======================================================================"
echo "Project Root: $PROJECT_ROOT"
echo "Build Directory: $BUILD_DIR"
echo "JAR Output: $JAR_DIR"
echo ""

# Step 1: Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ ERROR: Java is not installed"
    echo "   Please install Java 25 LTS or later"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -1)
echo "✓ $JAVA_VERSION"
echo ""

# Step 2: Navigate to project root
cd "$PROJECT_ROOT"

# Step 3: Clean previous builds
echo "🧹 Cleaning previous builds..."
gradle clean --no-daemon

echo ""

# Step 4: Run tests
echo "🧪 Running tests..."
gradle test --no-daemon -x checkstyleMain -x checkstyleTest

echo ""

# Step 5: Check code quality
echo "✓ Running code quality checks..."
gradle checkstyleMain checkstyleTest --no-daemon

echo ""

# Step 6: Build fat JAR
echo "🔨 Building production fat JAR..."
gradle build --no-daemon -x test -x checkstyleMain -x checkstyleTest

echo ""

# Step 7: Verify JAR file
if [ -d "$JAR_DIR" ]; then
    JAR_FILE=$(find "$JAR_DIR" -name "*.jar" -type f | head -1)
    if [ -f "$JAR_FILE" ]; then
        echo "✓ JAR file created successfully!"
        echo "  File: $JAR_FILE"
        ls -lh "$JAR_FILE"
    else
        echo "❌ ERROR: No JAR file found in build/libs/"
        exit 1
    fi
else
    echo "❌ ERROR: Build directory not created"
    exit 1
fi

echo ""
echo "======================================================================"
echo "✅ Backend build completed successfully!"
echo "======================================================================"
echo ""
echo "Next steps:"
echo "  1. Configure application.properties for production"
echo "  2. Deploy JAR file to production server"
echo "  3. Run: java -jar $(basename $JAR_FILE)"
echo ""
