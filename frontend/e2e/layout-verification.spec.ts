import { test, expect } from '@playwright/test';

test.describe('DocumentUploadForm - New Layout Verification', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to the application
    await page.goto('http://localhost:3001');
    // Wait for the card to be visible
    await page.waitForSelector('.p-card', { timeout: 5000 });
  });

  test('should display Theme Selection (Resume Optimizer) at top', async ({ page }) => {
    // Check for the red box with Resume Optimizer header
    const themeHeader = page.locator('text=Resume Optimizer').first();
    await expect(themeHeader).toBeVisible();

    // Verify it has red styling
    const redBox = themeHeader.locator('..').locator('..');
    const bgClass = await redBox.getAttribute('class');
    expect(bgClass).toContain('bg-red-50');
  });

  test('should display Main Content / Additional Tools section header', async ({ page }) => {
    const sectionHeader = page.locator('text=Main Content / Additional Tools');
    await expect(sectionHeader).toBeVisible();

    // Verify it's an h3 tag
    const tagName = await sectionHeader.evaluate(el => el.tagName);
    expect(tagName).toBe('H3');
  });

  test('should display Input Mode selector with yellow box', async ({ page }) => {
    // Check for Input Mode label
    const inputModeLabel = page.locator('text=Input Mode').first();
    await expect(inputModeLabel).toBeVisible();

    // Find the yellow box container
    const yellowBox = inputModeLabel.locator('..').locator('..');
    const bgClass = await yellowBox.getAttribute('class');
    expect(bgClass).toContain('bg-yellow-50');
    expect(bgClass).toContain('border-yellow-300');
  });

  test('should display Input Mode options (Paste Text, Upload Files)', async ({ page }) => {
    // Check for Paste Text option
    const pasteTextOption = page.locator('button').filter({ hasText: 'Paste Text' });
    await expect(pasteTextOption).toBeVisible();

    // Check for Upload Files option
    const uploadFilesOption = page.locator('button').filter({ hasText: 'Upload Files' });
    await expect(uploadFilesOption).toBeVisible();
  });

  test('should display Document Upload & Processing title after Input Mode', async ({ page }) => {
    const title = page.locator('text=Document Upload & Processing').first();
    await expect(title).toBeVisible();

    // Verify it has the correct styling (font-bold, text-lg, text-cyan-900)
    const titleClass = await title.getAttribute('class');
    expect(titleClass).toContain('font-bold');
    expect(titleClass).toContain('text-lg');
    expect(titleClass).toContain('text-cyan-900');
  });

  test('should display Job Description field', async ({ page }) => {
    const jobLabel = page.locator('text=Job Description *');
    await expect(jobLabel).toBeVisible();

    // Check for the textarea
    const textarea = page.locator('textarea').first();
    await expect(textarea).toBeVisible();
  });

  test('should display Source Resume field', async ({ page }) => {
    const resumeLabel = page.locator('text=Source Resume *');
    await expect(resumeLabel).toBeVisible();

    // Check for the second textarea
    const textareas = page.locator('textarea');
    const count = await textareas.count();
    expect(count).toBeGreaterThanOrEqual(2);
  });

  test('should verify layout order: Theme → Section → Input Mode → Title → Job Description → Resume', async ({
    page,
  }) => {
    // Get all visible text in order
    const themeHeader = page.locator('text=Resume Optimizer').first();
    const sectionHeader = page.locator('text=Main Content / Additional Tools');
    const inputModeLabel = page.locator('text=Input Mode').first();
    const docTitle = page.locator('text=Document Upload & Processing').first();
    const jobLabel = page.locator('text=Job Description *');
    const resumeLabel = page.locator('text=Source Resume *');

    // Verify all elements are visible
    await expect(themeHeader).toBeVisible();
    await expect(sectionHeader).toBeVisible();
    await expect(inputModeLabel).toBeVisible();
    await expect(docTitle).toBeVisible();
    await expect(jobLabel).toBeVisible();
    await expect(resumeLabel).toBeVisible();

    // Get bounding boxes to verify top-to-bottom ordering
    const themeBox = await themeHeader.boundingBox();
    const sectionBox = await sectionHeader.boundingBox();
    const inputBox = await inputModeLabel.boundingBox();
    const docBox = await docTitle.boundingBox();
    const jobBox = await jobLabel.boundingBox();
    const resumeBox = await resumeLabel.boundingBox();

    // Verify vertical ordering (each should be lower than the previous)
    expect(themeBox?.top).toBeLessThan(sectionBox!.top);
    expect(sectionBox?.top).toBeLessThan(inputBox!.top);
    expect(inputBox?.top).toBeLessThan(docBox!.top);
    expect(docBox?.top).toBeLessThan(jobBox!.top);
    expect(jobBox?.top).toBeLessThan(resumeBox!.top);
  });

  test('should capture layout screenshot', async ({ page }) => {
    // Wait a moment for rendering
    await page.waitForTimeout(500);

    // Take screenshot
    await page.screenshot({ path: 'layout-new-structure.png', fullPage: false });
    console.log('Layout screenshot saved: layout-new-structure.png');
  });

  test('should verify yellow box contains Input Mode and subtitle', async ({ page }) => {
    const inputModeLabel = page.locator('text=Input Mode').first();
    const yellowBox = inputModeLabel.locator('../..');

    // Check if the yellow box contains both Input Mode and the buttons
    const pasteButton = yellowBox.locator('button').filter({ hasText: 'Paste Text' });
    await expect(pasteButton).toBeVisible();

    const uploadButton = yellowBox.locator('button').filter({ hasText: 'Upload Files' });
    await expect(uploadButton).toBeVisible();
  });

  test('should switch between Paste Text and Upload Files modes', async ({ page }) => {
    // Click on Upload Files button
    const uploadButton = page.locator('button').filter({ hasText: 'Upload Files' });
    await uploadButton.click();

    // Verify file upload fields appear
    const fileUploadLabel = page.locator('text=Job Description File *');
    await expect(fileUploadLabel).toBeVisible();

    // Click back to Paste Text
    const pasteButton = page.locator('button').filter({ hasText: 'Paste Text' });
    await pasteButton.click();

    // Verify textarea fields appear
    const textarea = page.locator('textarea').first();
    await expect(textarea).toBeVisible();
  });
});
