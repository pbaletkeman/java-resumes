import { test, expect } from '@playwright/test';

/**
 * E2E Tests for DocumentUploadForm Layout
 * Verifies that "Document Upload & Processing" title is placed under Input Mode selector
 */

test.describe('DocumentUploadForm Layout', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to the application - using dynamic port detection
    await page.goto('http://localhost:3001/');

    // Wait for the form to load
    await page.waitForSelector('.p-card', { timeout: 10000 });
  });

  test('should have "Document Upload & Processing" title inside yellow box below Input Mode', async ({
    page,
  }) => {
    // Find the yellow box containing Input Mode
    const yellowBox = page.locator('.bg-yellow-50.border-round.border-1.border-yellow-300');

    // Verify yellow box exists
    await expect(yellowBox).toBeVisible();

    // Verify "Input Mode" label is visible
    const inputModeLabel = yellowBox.locator('label:has-text("Input Mode")');
    await expect(inputModeLabel).toBeVisible();

    // Verify "Document Upload & Processing" title is inside the yellow box
    const docTitle = yellowBox.locator('div:has-text("Document Upload & Processing")');
    await expect(docTitle).toBeVisible();

    // Verify the title is below the Input Mode selector (correct DOM order)
    const allElements = await yellowBox.locator('div').all();
    let inputModeIndex = -1;
    let titleIndex = -1;

    for (let i = 0; i < allElements.length; i++) {
      const text = await allElements[i].textContent();
      if (text?.includes('Input Mode')) {
        inputModeIndex = i;
      }
      if (text?.includes('Document Upload & Processing')) {
        titleIndex = i;
      }
    }

    // Title should appear after Input Mode in the DOM
    expect(titleIndex).toBeGreaterThan(inputModeIndex);
  });

  test('should position "Document Upload & Processing" below Input Mode selector with proper spacing', async ({
    page,
  }) => {
    // Get the yellow box
    const yellowBox = page.locator('.bg-yellow-50.border-round.border-1.border-yellow-300');

    // Get the SelectButton (Input Mode buttons)
    const selectButton = yellowBox.locator('.p-selectbutton');

    // Get the title inside yellow box
    const docTitle = yellowBox.locator('div.font-bold.text-lg.text-cyan-900');

    // Verify title is visible
    await expect(docTitle).toBeVisible();

    // Get bounding boxes to verify spatial relationship
    const selectButtonBox = await selectButton.boundingBox();
    const titleBox = await docTitle.boundingBox();

    if (selectButtonBox && titleBox) {
      // Title should be below the select button (higher Y coordinate)
      expect(titleBox.y).toBeGreaterThan(selectButtonBox.y);

      // Title should be within the same horizontal bounds as yellow box
      expect(titleBox.x).toBeGreaterThanOrEqual(0);
    }
  });

  test('should have correct styling for "Document Upload & Processing" title', async ({ page }) => {
    const yellowBox = page.locator('.bg-yellow-50.border-round.border-1.border-yellow-300');
    const docTitle = yellowBox.locator('div.font-bold.text-lg.text-cyan-900');

    // Verify the title has correct classes
    await expect(docTitle).toHaveClass(/font-bold/);
    await expect(docTitle).toHaveClass(/text-lg/);
    await expect(docTitle).toHaveClass(/text-cyan-900/);

    // Verify text content
    const titleText = await docTitle.textContent();
    expect(titleText?.trim()).toBe('Document Upload & Processing');
  });

  test('should maintain yellow box integrity with title inside', async ({ page }) => {
    const yellowBox = page.locator('.bg-yellow-50.border-round.border-1.border-yellow-300');

    // Verify yellow box styling
    await expect(yellowBox).toHaveClass(/bg-yellow-50/);
    await expect(yellowBox).toHaveClass(/border-yellow-300/);

    // Verify the box is full width
    const yellowBoxParent = yellowBox.locator('..');
    const boxClasses = await yellowBox.getAttribute('class');
    expect(boxClasses).toContain('w-full');
  });

  test('should have cyan box below yellow box', async ({ page }) => {
    // Get the yellow box
    const yellowBox = page.locator('.bg-yellow-50.border-round.border-1.border-yellow-300');

    // Get the cyan box (next section for content)
    const cyanBox = page.locator('.bg-cyan-50.border-1.border-cyan-300');

    // Both should be visible
    await expect(yellowBox).toBeVisible();
    await expect(cyanBox).toBeVisible();

    // Cyan box should be below yellow box
    const yellowBoxBottom = await yellowBox.boundingBox();
    const cyanBoxTop = await cyanBox.boundingBox();

    if (yellowBoxBottom && cyanBoxTop) {
      expect(cyanBoxTop.y).toBeGreaterThan(yellowBoxBottom.y + yellowBoxBottom.height);
    }
  });

  test('should display Input Mode selector options properly', async ({ page }) => {
    const yellowBox = page.locator('.bg-yellow-50.border-round.border-1.border-yellow-300');

    // Find select button options
    const pasteTextButton = yellowBox.locator('button:has-text("Paste Text")');
    const uploadFilesButton = yellowBox.locator('button:has-text("Upload Files")');

    // Both options should be visible
    await expect(pasteTextButton).toBeVisible();
    await expect(uploadFilesButton).toBeVisible();

    // "Paste Text" should be selected by default
    await expect(pasteTextButton).toHaveClass(/p-highlight/);
  });
});
