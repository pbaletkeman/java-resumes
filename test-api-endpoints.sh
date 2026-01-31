#!/bin/bash
# Quick test script to verify new endpoints exist in the controller

echo "Testing Interview & Networking Prompts Implementation"
echo "======================================================"
echo ""

echo "Checking controller for new endpoints..."
CONTROLLER_FILE="src/main/java/ca/letkeman/resumes/controller/ResumeController.java"

endpoints=(
  "interview-hr-questions"
  "interview-job-specific"
  "interview-reverse"
  "cold-email"
  "cold-linkedin-message"
  "thank-you-email"
)

all_found=true
for endpoint in "${endpoints[@]}"; do
  if grep -q "/generate/$endpoint" "$CONTROLLER_FILE"; then
    echo "✅ Found: POST /api/generate/$endpoint"
  else
    echo "❌ Missing: POST /api/generate/$endpoint"
    all_found=false
  fi
done

echo ""
echo "Checking prompt templates..."
PROMPTS_DIR="src/main/resources/prompts"

prompt_files=(
  "INTERVIEW-HR-QUESTIONS.md"
  "INTERVIEW-JOB-SPECIFIC.md"
  "INTERVIEW-REVERSE.md"
  "COLD-EMAIL.md"
  "COLD-LINKEDIN-MESSAGE.md"
  "THANK-YOU-EMAIL.md"
)

for prompt_file in "${prompt_files[@]}"; do
  if [ -f "$PROMPTS_DIR/$prompt_file" ]; then
    size=$(wc -c < "$PROMPTS_DIR/$prompt_file")
    echo "✅ Found: $prompt_file (${size} bytes)"
  else
    echo "❌ Missing: $prompt_file"
    all_found=false
  fi
done

echo ""
echo "Checking database components..."
if [ -f "src/main/java/ca/letkeman/resumes/entity/PromptHistory.java" ]; then
  echo "✅ Found: PromptHistory entity"
else
  echo "❌ Missing: PromptHistory entity"
  all_found=false
fi

if [ -f "src/main/java/ca/letkeman/resumes/repository/PromptHistoryRepository.java" ]; then
  echo "✅ Found: PromptHistoryRepository"
else
  echo "❌ Missing: PromptHistoryRepository"
  all_found=false
fi

if [ -f "src/main/resources/db/migration/sqlite/V1__create_prompt_history.sql" ]; then
  echo "✅ Found: Flyway migration"
else
  echo "❌ Missing: Flyway migration"
  all_found=false
fi

echo ""
echo "======================================================"
if $all_found; then
  echo "✅ ALL COMPONENTS VERIFIED - Implementation Complete!"
  exit 0
else
  echo "❌ SOME COMPONENTS MISSING - Check implementation"
  exit 1
fi
