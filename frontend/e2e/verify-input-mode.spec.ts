import { test, expect } from '@playwright/test';

test('Verify Input Mode selector is restored', async ({ page }) => {
  await page.goto('http://localhost:3001', { waitUntil: 'networkidle' });

  // Test 1: Verify Input Mode label is visible
  const inputModeLabel = page.locator('text=Input Mode');
  await expect(inputModeLabel).toBeVisible();
  console.log('✓ Input Mode label is visible');

  // Test 2: Verify "Paste Text" option is visible
  const pasteTextOption = page.locator('text=Paste Text');
  await expect(pasteTextOption).toBeVisible();
  console.log('✓ Paste Text option is visible');

  // Test 3: Verify "Upload Files" option is visible
  const uploadFilesOption = page.locator('text=Upload Files');
  await expect(uploadFilesOption).toBeVisible();
  console.log('✓ Upload Files option is visible');

  // Test 4: Verify yellow box container
  const yellowBox = page.locator('.bg-yellow-50');
  await expect(yellowBox).toBeVisible();
  console.log('✓ Yellow box container is visible');

  // Test 5: Verify form fields are visible
  const jobDescriptionLabel = page.locator('text=Job Description');
  const sourceResumeLabel = page.locator('text=Source Resume');

  await expect(jobDescriptionLabel).toBeVisible();
  await expect(sourceResumeLabel).toBeVisible();
  console.log('✓ Form fields are visible');

  // Test 6: Verify theme toggle and navbar
  const themeToggleButton = page.locator('button[aria-label*="Toggle theme"]');
  const navbarTitle = page.locator('text=Resume Optimizer').first();

  await expect(themeToggleButton).toBeVisible();
  await expect(navbarTitle).toBeVisible();
  console.log('✓ Navbar with theme toggle is visible');

  console.log('\n✅ All verifications passed!');
});
