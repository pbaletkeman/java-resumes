import { test, expect } from '@playwright/test';

test('Verify UI state after changes', async ({ page }) => {
  await page.goto('http://localhost:3001', { waitUntil: 'networkidle' });

  // Test 1: Verify red box with "Resume Optimizer" is REMOVED
  const redBox = page.locator('.bg-red-50');
  await expect(redBox).not.toBeVisible();
  console.log('✓ Red box element is removed');

  // Test 2: Verify Input Mode selector is MISSING
  const inputModeLabel = page.locator('text=Input Mode');
  const pasteTextOption = page.locator('text=Paste Text');
  const uploadFilesOption = page.locator('text=Upload files');

  await expect(inputModeLabel).not.toBeVisible();
  await expect(pasteTextOption).not.toBeVisible();
  await expect(uploadFilesOption).not.toBeVisible();
  console.log('✓ Input Mode selector is missing');

  // Test 3: Verify "Resume Optimizer" in navbar is still present
  const navbarTitle = page.locator('text=Resume Optimizer').first();
  await expect(navbarTitle).toBeVisible();
  console.log('✓ Resume Optimizer in navbar is still visible');

  // Test 4: Verify form fields are visible
  const jobDescriptionLabel = page.locator('text=Job Description');
  const sourceResumeLabel = page.locator('text=Source Resume');

  await expect(jobDescriptionLabel).toBeVisible();
  await expect(sourceResumeLabel).toBeVisible();
  console.log('✓ Form fields are visible');

  // Test 5: Verify theme toggle button is in navbar
  const themeToggleButton = page.locator('button[aria-label*="Toggle theme"]');
  await expect(themeToggleButton).toBeVisible();
  console.log('✓ Theme toggle button is visible in navbar');

  console.log('\n✅ All verifications passed!');
});
