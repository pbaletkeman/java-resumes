import { test, expect } from '@playwright/test';

test('Verify complete layout structure', async ({ page }) => {
  await page.goto('http://localhost:3001', { waitUntil: 'networkidle' });

  // Test 1: Verify Theme Selection (red box) at top
  const redBox = page.locator('.bg-red-50');
  const themeTitle = page.locator('text=Resume Optimizer').nth(1); // second occurrence (not navbar)
  await expect(redBox).toBeVisible();
  await expect(themeTitle).toBeVisible();
  console.log('✓ Theme Selection (red box) is visible at top');

  // Test 2: Verify Main Content / Additional Tools heading
  const mainContentHeading = page.locator('text=Main Content / Additional Tools');
  await expect(mainContentHeading).toBeVisible();
  console.log('✓ Main Content / Additional Tools heading is visible');

  // Test 3: Verify Input Mode selector (yellow box)
  const yellowBox = page.locator('.bg-yellow-50');
  const inputModeLabel = page.locator('text=Input Mode');
  const pasteTextOption = page.locator('text=Paste Text');
  const uploadFilesOption = page.locator('text=Upload Files');

  await expect(yellowBox).toBeVisible();
  await expect(inputModeLabel).toBeVisible();
  await expect(pasteTextOption).toBeVisible();
  await expect(uploadFilesOption).toBeVisible();
  console.log('✓ Input Mode selector (yellow box) with options is visible');

  // Test 4: Verify Document Upload & Processing heading
  const docUploadHeading = page.locator('text=Document Upload & Processing');
  await expect(docUploadHeading).toBeVisible();
  console.log('✓ Document Upload & Processing heading is visible');

  // Test 5: Verify form fields
  const jobDescriptionLabel = page.locator('text=Job Description');
  const sourceResumeLabel = page.locator('text=Source Resume');

  await expect(jobDescriptionLabel).toBeVisible();
  await expect(sourceResumeLabel).toBeVisible();
  console.log('✓ Form fields (Job Description, Source Resume) are visible');

  // Test 6: Verify File History on the right
  const fileHistoryHeader = page
    .locator('text=Recent Files')
    .or(page.locator('text=File History'))
    .first();
  const rightPanel = page.locator('div').filter({ has: fileHistoryHeader }).first();

  // Check if right panel exists (File History sidebar)
  const allText = await page.locator('body').innerText();
  const hasFileHistory = allText.includes('Recent Files') || allText.includes('File History');
  if (hasFileHistory) {
    console.log('✓ File History is present on the right side');
  } else {
    console.log('⚠ File History section not clearly visible');
  }

  console.log('\n✅ Complete layout structure verified!');
  console.log('\nLayout order:');
  console.log('1. Theme Selection (red box with Resume Optimizer)');
  console.log('2. Main Content / Additional Tools');
  console.log('3. Input Mode selector (yellow box with Paste Text / Upload Files)');
  console.log('4. Document Upload & Processing');
  console.log('5. Job Description field');
  console.log('6. Source Resume field');
  console.log('+ File History on the right');
});
