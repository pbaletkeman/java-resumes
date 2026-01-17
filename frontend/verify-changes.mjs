import fs from 'fs';
import path from 'path';

const filePath = path.join(process.cwd(), 'src/components/Forms/DocumentUploadForm.tsx');
const content = fs.readFileSync(filePath, 'utf-8');

// Count occurrences of "Resume Optimizer" text in the JSX
const resumeOptimizerMatches = content.match(/Resume Optimizer/g) || [];
console.log(`\n✓ Found ${resumeOptimizerMatches.length} occurrence(s) of "Resume Optimizer"`);

// Check that Input Mode selector was removed
const hasInputModeSelector = content.includes('SelectButton');
console.log(`✓ Input Mode selector (SelectButton) removed: ${!hasInputModeSelector}`);

// Check that only the theme selector red box remains
const hasRedBox = content.includes('bg-red-50') && content.includes('text-red-700');
console.log(`✓ Theme selector red box present: ${hasRedBox}`);

// Verify structure
if (resumeOptimizerMatches.length === 1) {
  console.log('\n✅ SUCCESS: Only one "Resume Optimizer" text remains (in the theme selector)');
} else {
  console.log(
    `\n❌ ERROR: Expected 1 "Resume Optimizer" but found ${resumeOptimizerMatches.length}`
  );
}

if (!hasInputModeSelector) {
  console.log('✅ SUCCESS: Input Mode selector has been removed');
} else {
  console.log('❌ ERROR: Input Mode selector still present');
}

if (hasRedBox) {
  console.log('✅ SUCCESS: Theme selector red box is still in place');
} else {
  console.log('❌ ERROR: Theme selector red box is missing');
}
