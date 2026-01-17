import { test, expect } from '@playwright/test';

/**
 * Verification test for DocumentUploadForm layout change
 * Tests that "Document Upload & Processing" title is moved under Input Mode selector
 */

test.describe('DocumentUploadForm - Title Position Verification', () => {
  test('Title should be inside yellow box below Input Mode selector', async ({ page }) => {
    // Navigate to localhost:3001
    await page.goto('http://localhost:3001/');

    // Wait for the card to load
    await page.waitForSelector('.p-card', { timeout: 15000 });
    await page.waitForTimeout(1000); // Additional wait for rendering

    // Find the yellow box
    const yellowBox = page.locator('.bg-yellow-50.border-yellow-300');

    // Verify yellow box is visible
    console.log('Checking if yellow box is visible...');
    await expect(yellowBox).toBeVisible({ timeout: 10000 });
    console.log('✓ Yellow box is visible');

    // Check for Input Mode label
    const inputModeLabel = page.locator('text="Input Mode"');
    await expect(inputModeLabel).toBeVisible();
    console.log('✓ Input Mode label found');

    // Check for the title text
    const titleText = page.locator('text="Document Upload & Processing"');
    await expect(titleText).toBeVisible();
    console.log('✓ Title "Document Upload & Processing" is visible');

    // Verify title has correct styling
    const titleElement = titleText;
    const titleClasses = await titleElement.getAttribute('class');
    console.log('Title classes:', titleClasses);
    expect(titleClasses).toContain('font-bold');
    expect(titleClasses).toContain('text-cyan-900');
    console.log('✓ Title has correct styling');

    // Check that title is inside the yellow box (it's a child element)
    const isInside = await yellowBox.evaluate(box => {
      const title = box.querySelector('.font-bold.text-lg.text-cyan-900');
      return title !== null;
    });

    console.log('Title is inside yellow box:', isInside);
    expect(isInside).toBe(true);
    console.log('✓ Title is correctly positioned inside yellow box');

    // Take a screenshot for visual verification
    await page.screenshot({ path: 'document-upload-layout.png' });
    console.log('✓ Screenshot saved');

    console.log('\n✅ All layout verification tests passed!');
  });
});
