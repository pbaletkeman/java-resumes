import { chromium } from 'playwright';
import * as fs from 'fs';
import * as path from 'path';

async function takeScreenshot() {
  const browser = await chromium.launch();
  const page = await browser.newPage();

  try {
    // Navigate to the application
    console.log('Navigating to http://localhost:3000...');
    await page.goto('http://localhost:3000', { waitUntil: 'networkidle' });

    // Wait a bit for everything to render
    await page.waitForTimeout(2000);

    // Create screenshots directory if it doesn't exist
    const screenshotsDir = path.join(__dirname, 'screenshots');
    if (!fs.existsSync(screenshotsDir)) {
      fs.mkdirSync(screenshotsDir, { recursive: true });
    }

    // Take full page screenshot
    const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
    const filename = path.join(screenshotsDir, `page-layout-${timestamp}.png`);

    console.log(`Taking screenshot: ${filename}`);
    await page.screenshot({ path: filename, fullPage: true });
    console.log('✓ Screenshot saved successfully');

    // Print page structure for debugging
    console.log('\n--- Page Structure ---');
    const structure = await page.evaluate(() => {
      return {
        title: document.title,
        mainContent: document.querySelector('.p-card-content')?.textContent?.substring(0, 100),
        hasFileHistory: !!document.querySelector('[class*="border-left"]'),
        isResponsive: window.innerWidth,
      };
    });
    console.log(JSON.stringify(structure, null, 2));

    // Check specific elements
    const jobDescLabel = await page.locator('label:has-text("Job Description")').count();
    const sourceResumeLabel = await page.locator('label:has-text("Source Resume")').count();
    const pasteTextBtn = await page.locator('button:has-text("Paste Text")').count();
    const uploadFilesBtn = await page.locator('button:has-text("Upload Files")').count();

    console.log('\n--- Element Checks ---');
    console.log(`✓ Job Description labels found: ${jobDescLabel}`);
    console.log(`✓ Source Resume labels found: ${sourceResumeLabel}`);
    console.log(`✓ Paste Text buttons found: ${pasteTextBtn}`);
    console.log(`✓ Upload Files buttons found: ${uploadFilesBtn}`);

    // Verify layout
    const formCard = await page.locator('.p-card').first();
    const formBounds = await formCard.boundingBox();

    if (formBounds) {
      console.log('\n--- Form Card Dimensions ---');
      console.log(`Width: ${formBounds.width}px`);
      console.log(`Height: ${formBounds.height}px`);
      console.log(`X Position: ${formBounds.x}px`);
    }
  } catch (error) {
    console.error('Error taking screenshot:', error);
    process.exit(1);
  } finally {
    await browser.close();
  }
}

takeScreenshot();
