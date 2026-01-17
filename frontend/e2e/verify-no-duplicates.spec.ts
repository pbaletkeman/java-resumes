import { test, expect } from '@playwright/test';

test('Verify Resume Optimizer appears only in navbar and Main Content appears only once', async ({
  page,
}) => {
  await page.goto('http://localhost:3001', { waitUntil: 'networkidle' });

  // Test 1: Verify "Resume Optimizer" appears only ONCE (in navbar)
  const resumeOptimizerLocators = page.locator('text=Resume Optimizer');
  const resumeOptimizerCount = await resumeOptimizerLocators.count();
  console.log(`✓ Resume Optimizer appears ${resumeOptimizerCount} time(s)`);

  if (resumeOptimizerCount === 1) {
    await expect(resumeOptimizerLocators.first()).toBeVisible();
    console.log('✓ Resume Optimizer is in navbar only');
  } else {
    throw new Error(`Expected 1 occurrence of Resume Optimizer but found ${resumeOptimizerCount}`);
  }

  // Test 2: Verify "Main Content / Additional Tools" appears ONLY in tabs (not in form)
  const mainContentLabels = page.locator('text=Main Content / Additional Tools');
  const mainContentCount = await mainContentLabels.count();
  console.log(`✓ Main Content / Additional Tools label appears ${mainContentCount} time(s)`);

  if (mainContentCount === 0) {
    console.log('✓ Main Content / Additional Tools heading removed from form (tabs handle this)');
  } else {
    throw new Error(
      `Expected 0 occurrences of Main Content / Additional Tools in form but found ${mainContentCount}`
    );
  }

  // Test 3: Verify tab headers are present
  const mainContentTab = page.locator('.p-tabview-nav').locator('text=Main Content');
  const additionalToolsTab = page.locator('.p-tabview-nav').locator('text=Additional Tools');

  await expect(mainContentTab).toBeVisible();
  await expect(additionalToolsTab).toBeVisible();
  console.log('✓ Tab headers Main Content and Additional Tools are visible');

  // Test 4: Verify Input Mode selector is visible
  const inputModeLabel = page.locator('text=Input Mode');
  await expect(inputModeLabel).toBeVisible();
  console.log('✓ Input Mode selector is visible');

  // Test 5: Verify form fields
  const jobDescriptionLabel = page.locator('text=Job Description');
  const sourceResumeLabel = page.locator('text=Source Resume');
  await expect(jobDescriptionLabel).toBeVisible();
  await expect(sourceResumeLabel).toBeVisible();
  console.log('✓ Form fields are visible');

  // Test 6: Verify empty red box is still present
  const redBox = page.locator('.bg-red-50');
  await expect(redBox).toBeVisible();
  console.log('✓ Red box (placeholder) is visible');

  console.log('\n✅ All verifications passed!');
  console.log('\nFinal layout:');
  console.log('- Resume Optimizer: Only in NavBar');
  console.log('- Main Content / Additional Tools: Only as tab headers');
  console.log('- Red box: Empty placeholder at top of form');
  console.log('- Input Mode selector: In yellow box');
  console.log('- Form fields: Job Description and Source Resume');
});
