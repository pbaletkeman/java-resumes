#!/bin/bash
# Docker Ollama Model Initialization Script
# This script is run inside the Ollama container to pull the default model

MODEL="${OLLAMA_MODEL:-tinyllama}"

echo "Waiting for Ollama service to be ready..."
sleep 5

# Pull the model
echo "Pulling model: $MODEL"
ollama pull "$MODEL"

echo "Model $MODEL is ready!"
ollama list
