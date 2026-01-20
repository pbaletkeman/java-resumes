import { chromium } from 'playwright';
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __dirname = path.dirname(fileURLToPath(import.meta.url));

const screenshotsDir = path.join(__dirname, '..', 'docs', 'screenshots', 'frontend');
const backendScreenshotsDir = path.join(__dirname, '..', 'docs', 'screenshots', 'backend');

// Ensure directories exist
if (!fs.existsSync(screenshotsDir)) {
  fs.mkdirSync(screenshotsDir, { recursive: true });
}
if (!fs.existsSync(backendScreenshotsDir)) {
  fs.mkdirSync(backendScreenshotsDir, { recursive: true });
}

const MIN_FILE_SIZE = 10000;
const MAX_RETRIES = 5;

async function isValidScreenshot(filePath) {
  if (!fs.existsSync(filePath)) {
    console.log(`    ❌ File doesn't exist`);
    return false;
  }

  const stats = fs.statSync(filePath);
  if (stats.size < MIN_FILE_SIZE) {
    console.log(
      `    ❌ File too small (${(stats.size / 1024).toFixed(1)}KB, min: ${(MIN_FILE_SIZE / 1024).toFixed(1)}KB)`
    );
    return false;
  }

  console.log(`    ✅ Valid (${(stats.size / 1024).toFixed(1)}KB)`);
  return true;
}

async function captureScreenshotWithRetry(page, filePath, options = {}, retryCount = 0) {
  try {
    console.log(`  Attempt ${retryCount + 1}/${MAX_RETRIES}...`);

    // Wait for navigation
    await page.waitForLoadState('networkidle', { timeout: 10000 }).catch(() => {});
    await page.waitForTimeout(1500);

    // Take screenshot
    await page.screenshot({
      path: filePath,
      fullPage: true,
      ...options,
    });

    // Verify
    const isValid = await isValidScreenshot(filePath);
    if (isValid) {
      return true;
    }

    // Retry if needed
    if (retryCount < MAX_RETRIES - 1) {
      console.log(`  🔄 Retrying...`);
      await page.reload().catch(() => {});
      await page.waitForTimeout(1000);
      return captureScreenshotWithRetry(page, filePath, options, retryCount + 1);
    }

    return false;
  } catch (error) {
    console.log(`  ⚠️ Error: ${error.message}`);

    if (retryCount < MAX_RETRIES - 1) {
      console.log(`  🔄 Retrying after error...`);
      await page.waitForTimeout(1000);
      return captureScreenshotWithRetry(page, filePath, options, retryCount + 1);
    }

    return false;
  }
}

async function main() {
  console.log('\n🚀 Starting screenshot capture...\n');

  let browser;
  try {
    browser = await chromium.launch({
      headless: true,
    });

    // Frontend screenshots
    console.log('📸 Frontend Screenshots\n');
    {
      let page;
      try {
        page = await browser.newPage();
        await page.setViewportSize({ width: 1400, height: 900 });

        const screenshots = [
          { name: 'main-tab.png', url: 'http://localhost:3000', label: 'Main Tab' },
          { name: 'light-theme.png', url: 'http://localhost:3000', label: 'Light Theme' },
          { name: 'dark-theme.png', url: 'http://localhost:3000', label: 'Dark Theme' },
          { name: 'tools-tab.png', url: 'http://localhost:3000', label: 'Tools Tab' },
          { name: 'file-history.png', url: 'http://localhost:3000', label: 'File History' },
        ];

        for (const screenshot of screenshots) {
          console.log(`  ${screenshot.label}...`);
          await page.goto(screenshot.url, { waitUntil: 'networkidle' }).catch(() => {});
          const filePath = path.join(screenshotsDir, screenshot.name);
          const success = await captureScreenshotWithRetry(page, filePath);
          console.log(success ? '  ✅ Done\n' : '  ❌ Failed\n');
        }

        // Mobile screenshot
        console.log(`  Mobile Responsive...`);
        await page.setViewportSize({ width: 375, height: 812 });
        await page.goto('http://localhost:3000', { waitUntil: 'networkidle' }).catch(() => {});
        const mobilePath = path.join(screenshotsDir, 'responsive-mobile.png');
        const mobileSuccess = await captureScreenshotWithRetry(page, mobilePath);
        console.log(mobileSuccess ? '  ✅ Done\n' : '  ❌ Failed\n');

        await page.close();
      } catch (err) {
        console.log(`  ⚠️ Error: ${err.message}\n`);
        if (page) await page.close().catch(() => {});
      }
    }

    // Backend screenshots
    console.log('📸 Backend Screenshots\n');
    {
      let page;
      try {
        page = await browser.newPage();
        await page.setViewportSize({ width: 1400, height: 900 });

        const screenshots = [
          {
            name: 'swagger-ui.png',
            url: 'http://localhost:8080/swagger-ui/index.html',
            label: 'Swagger UI',
          },
          {
            name: 'api-endpoints.png',
            url: 'http://localhost:8080/swagger-ui/index.html',
            label: 'API Endpoints',
          },
          {
            name: 'error-responses.png',
            url: 'http://localhost:8080/swagger-ui/index.html',
            label: 'Error Responses',
          },
        ];

        for (const screenshot of screenshots) {
          console.log(`  ${screenshot.label}...`);
          await page.goto(screenshot.url, { waitUntil: 'networkidle' }).catch(() => {});
          await page.waitForTimeout(2000);
          const filePath = path.join(backendScreenshotsDir, screenshot.name);
          const success = await captureScreenshotWithRetry(page, filePath);
          console.log(success ? '  ✅ Done\n' : '  ❌ Failed\n');
        }

        await page.close();
      } catch (err) {
        console.log(`  ⚠️ Error: ${err.message}\n`);
        if (page) await page.close().catch(() => {});
      }
    }

    console.log('\n✅ Screenshot capture completed!\n');
  } catch (error) {
    console.error('\n❌ Fatal error:', error.message, '\n');
  } finally {
    if (browser) {
      await browser.close();
    }
  }
}

main();
