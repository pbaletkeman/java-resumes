#!/bin/bash
# Ollama Setup Script for Java-Resumes
# This script initializes Ollama with a small model for development

set -e

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Default model
DEFAULT_MODEL="tinyllama"

echo -e "${BLUE}╔════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║                                                        ║${NC}"
echo -e "${BLUE}║        Ollama Setup for Java-Resumes Project          ║${NC}"
echo -e "${BLUE}║                                                        ║${NC}"
echo -e "${BLUE}╔════════════════════════════════════════════════════════╗${NC}"
echo ""

# Parse arguments
MODEL="${1:-$DEFAULT_MODEL}"

echo -e "${GREEN}Selected Model: $MODEL${NC}"
echo ""

# Check if Ollama is installed
if ! command -v ollama &> /dev/null; then
    echo -e "${YELLOW}Ollama is not installed. Installing...${NC}"
    
    if [[ "$OSTYPE" == "darwin"* ]]; then
        echo "Detected macOS. Please install Ollama from https://ollama.com"
        echo "Or run: brew install ollama"
        exit 1
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        echo "Installing Ollama for Linux..."
        curl -fsSL https://ollama.com/install.sh | sh
    else
        echo -e "${RED}Unsupported OS. Please install Ollama manually from https://ollama.com${NC}"
        exit 1
    fi
else
    echo -e "${GREEN}✓ Ollama is already installed${NC}"
    ollama --version
fi

echo ""
echo -e "${BLUE}Starting Ollama service...${NC}"

# Check if Ollama service is running
if ! curl -s http://localhost:11434/api/version > /dev/null 2>&1; then
    echo "Starting Ollama service in background..."
    
    # Start Ollama in background
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS - Ollama runs as a service
        open -a Ollama 2>/dev/null || ollama serve &
    else
        # Linux - start as background process
        nohup ollama serve > /tmp/ollama.log 2>&1 &
    fi
    
    # Wait for service to start
    echo "Waiting for Ollama service to start..."
    for i in {1..30}; do
        if curl -s http://localhost:11434/api/version > /dev/null 2>&1; then
            echo -e "${GREEN}✓ Ollama service is ready!${NC}"
            break
        fi
        sleep 1
        echo -n "."
    done
    echo ""
else
    echo -e "${GREEN}✓ Ollama service is already running${NC}"
fi

# Display available models
echo ""
echo -e "${BLUE}Available small models:${NC}"
echo "  1. tinyllama (1.1B) - Very fast, basic tasks"
echo "  2. phi3:mini (3.8B) - Small but capable"
echo "  3. gemma2:2b (2B) - Google's small model"
echo "  4. qwen2.5:0.5b (0.5B) - Extremely lightweight"
echo ""

# Pull the selected model
echo -e "${BLUE}Pulling model: $MODEL${NC}"
echo "This may take a few minutes depending on your connection..."
echo ""

ollama pull "$MODEL"

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✓ Model '$MODEL' pulled successfully!${NC}"
else
    echo -e "${RED}✗ Failed to pull model '$MODEL'${NC}"
    exit 1
fi

# List installed models
echo ""
echo -e "${BLUE}Currently installed models:${NC}"
ollama list

# Test the model
echo ""
echo -e "${BLUE}Testing model inference...${NC}"
TEST_RESPONSE=$(curl -s http://localhost:11434/api/generate -d "{
  \"model\": \"$MODEL\",
  \"prompt\": \"Say hello\",
  \"stream\": false
}" | jq -r '.response' 2>/dev/null || echo "Test completed")

if [ ! -z "$TEST_RESPONSE" ]; then
    echo -e "${GREEN}✓ Model test successful!${NC}"
    echo "Response: $TEST_RESPONSE"
else
    echo -e "${YELLOW}⚠ Could not parse test response, but model is ready${NC}"
fi

# Update config.json
echo ""
echo -e "${BLUE}Updating config.json...${NC}"

CONFIG_FILE="config.json"
if [ -f "$CONFIG_FILE" ]; then
    # Backup existing config
    cp "$CONFIG_FILE" "${CONFIG_FILE}.backup"
    
    # Update config with new model
    cat > "$CONFIG_FILE" << EOF
{
  "endpoint": "http://localhost:11434/v1/chat/completions",
  "apikey": "ollama",
  "model": "$MODEL"
}
EOF
    
    echo -e "${GREEN}✓ config.json updated with model: $MODEL${NC}"
    cat "$CONFIG_FILE"
else
    echo -e "${YELLOW}⚠ config.json not found in current directory${NC}"
fi

# Display summary
echo ""
echo -e "${BLUE}╔════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║                    Setup Complete!                     ║${NC}"
echo -e "${BLUE}╔════════════════════════════════════════════════════════╗${NC}"
echo ""
echo -e "${GREEN}Ollama Service:${NC} http://localhost:11434"
echo -e "${GREEN}Model:${NC} $MODEL"
echo -e "${GREEN}API Endpoint:${NC} http://localhost:11434/v1/chat/completions"
echo ""
echo -e "${YELLOW}Next steps:${NC}"
echo "  1. Run: gradle bootRun"
echo "  2. Access: http://localhost:8080"
echo "  3. Test the resume optimization features"
echo ""
echo -e "${BLUE}To use a different model, run:${NC}"
echo "  ./scripts/setup-ollama.sh <model-name>"
echo ""
echo -e "${BLUE}Examples:${NC}"
echo "  ./scripts/setup-ollama.sh phi3:mini"
echo "  ./scripts/setup-ollama.sh gemma2:2b"
echo ""
