const { chromium } = require("playwright");
const fs = require("fs");
const path = require("path");

const screenshotsDir = path.join(__dirname, "docs", "screenshots", "frontend");
const backendScreenshotsDir = path.join(
  __dirname,
  "docs",
  "screenshots",
  "backend",
);

// Ensure directories exist
if (!fs.existsSync(screenshotsDir)) {
  fs.mkdirSync(screenshotsDir, { recursive: true });
}
if (!fs.existsSync(backendScreenshotsDir)) {
  fs.mkdirSync(backendScreenshotsDir, { recursive: true });
}

const MIN_FILE_SIZE = 10000; // Minimum 10KB to consider a valid screenshot
const MAX_RETRIES = 5;

async function isValidScreenshot(filePath) {
  if (!fs.existsSync(filePath)) {
    console.log(`  ❌ File doesn't exist: ${filePath}`);
    return false;
  }

  const stats = fs.statSync(filePath);
  if (stats.size < MIN_FILE_SIZE) {
    console.log(
      `  ❌ File too small (${stats.size} bytes, min: ${MIN_FILE_SIZE})`,
    );
    return false;
  }

  console.log(`  ✅ Valid screenshot (${stats.size} bytes)`);
  return true;
}

async function captureScreenshotWithRetry(
  page,
  filePath,
  options = {},
  retryCount = 0,
) {
  try {
    console.log(
      `  Attempting capture (attempt ${retryCount + 1}/${MAX_RETRIES})...`,
    );

    // Wait for the page to be ready
    await page.waitForLoadState("networkidle", { timeout: 10000 });

    // Additional wait for content to render
    await page.waitForTimeout(2000);

    // Take screenshot
    await page.screenshot({
      path: filePath,
      fullPage: options.fullPage || false,
      ...options,
    });

    // Verify the screenshot is valid
    const isValid = await isValidScreenshot(filePath);

    if (isValid) {
      return true;
    }

    // If not valid and we have retries left, try again
    if (retryCount < MAX_RETRIES - 1) {
      console.log(`  🔄 Retrying screenshot capture...`);
      await page.reload();
      await page.waitForTimeout(1000);
      return captureScreenshotWithRetry(
        page,
        filePath,
        options,
        retryCount + 1,
      );
    }

    return false;
  } catch (error) {
    console.log(`  ⚠️ Error during capture: ${error.message}`);

    if (retryCount < MAX_RETRIES - 1) {
      console.log(`  🔄 Retrying after error...`);
      await page.waitForTimeout(1000);
      return captureScreenshotWithRetry(
        page,
        filePath,
        options,
        retryCount + 1,
      );
    }

    return false;
  }
}

async function captureFrontendScreenshots(browser) {
  console.log("\n📸 Capturing Frontend Screenshots...\n");

  const page = await browser.newPage();
  page.setViewportSize({ width: 1400, height: 900 });

  try {
    console.log("1️⃣  Main Upload Tab (main-tab.png)");
    await page.goto("http://localhost:3000", { waitUntil: "networkidle" });
    await page.waitForTimeout(2000);
    const mainTabPath = path.join(screenshotsDir, "main-tab.png");
    const mainTabSuccess = await captureScreenshotWithRetry(page, mainTabPath, {
      fullPage: true,
    });
    console.log(
      mainTabSuccess
        ? "✅ Main tab screenshot captured\n"
        : "❌ Failed to capture main tab\n",
    );

    console.log("2️⃣  Light Theme (light-theme.png)");
    // Check if light theme exists or is active
    const lightThemePath = path.join(screenshotsDir, "light-theme.png");
    const lightThemeSuccess = await captureScreenshotWithRetry(
      page,
      lightThemePath,
      { fullPage: true },
    );
    console.log(
      lightThemeSuccess
        ? "✅ Light theme screenshot captured\n"
        : "❌ Failed to capture light theme\n",
    );

    console.log("3️⃣  Dark Theme (dark-theme.png)");
    // Try to toggle to dark theme if there's a theme button
    try {
      const themeButtons = await page.locator(
        'button[data-theme], button[aria-label*="theme" i]',
      );
      if ((await themeButtons.count()) > 0) {
        await themeButtons.first().click();
        await page.waitForTimeout(1000);
      }
    } catch (e) {
      console.log("  ℹ️  Theme toggle not found, capturing current theme");
    }

    const darkThemePath = path.join(screenshotsDir, "dark-theme.png");
    const darkThemeSuccess = await captureScreenshotWithRetry(
      page,
      darkThemePath,
      { fullPage: true },
    );
    console.log(
      darkThemeSuccess
        ? "✅ Dark theme screenshot captured\n"
        : "❌ Failed to capture dark theme\n",
    );

    console.log("4️⃣  Mobile Responsive (responsive-mobile.png)");
    await page.setViewportSize({ width: 375, height: 812 });
    await page.goto("http://localhost:3000", { waitUntil: "networkidle" });
    await page.waitForTimeout(2000);
    const mobilePath = path.join(screenshotsDir, "responsive-mobile.png");
    const mobileSuccess = await captureScreenshotWithRetry(page, mobilePath, {
      fullPage: true,
    });
    console.log(
      mobileSuccess
        ? "✅ Mobile screenshot captured\n"
        : "❌ Failed to capture mobile\n",
    );

    console.log("5️⃣  File History Panel (file-history.png)");
    page.setViewportSize({ width: 1400, height: 900 });
    await page.goto("http://localhost:3000", { waitUntil: "networkidle" });
    await page.waitForTimeout(2000);
    // Look for file history sidebar - might need to adjust selector
    const historyPath = path.join(screenshotsDir, "file-history.png");
    const historySuccess = await captureScreenshotWithRetry(page, historyPath, {
      fullPage: true,
    });
    console.log(
      historySuccess
        ? "✅ File history screenshot captured\n"
        : "❌ Failed to capture file history\n",
    );

    console.log("6️⃣  Tools Tab (tools-tab.png)");
    // Look for tools tab
    try {
      const toolsTabs = await page.locator(
        'button:has-text("Tools"), [role="tab"]:has-text("Tools")',
      );
      if ((await toolsTabs.count()) > 0) {
        await toolsTabs.first().click();
        await page.waitForTimeout(1000);
      }
    } catch (e) {
      console.log("  ℹ️  Tools tab not found, capturing current view");
    }

    const toolsPath = path.join(screenshotsDir, "tools-tab.png");
    const toolsSuccess = await captureScreenshotWithRetry(page, toolsPath, {
      fullPage: true,
    });
    console.log(
      toolsSuccess
        ? "✅ Tools tab screenshot captured\n"
        : "❌ Failed to capture tools tab\n",
    );
  } finally {
    await page.close();
  }
}

async function captureBackendScreenshots(browser) {
  console.log("\n📸 Capturing Backend Screenshots...\n");

  const page = await browser.newPage();
  page.setViewportSize({ width: 1400, height: 900 });

  try {
    console.log("1️⃣  Swagger UI (swagger-ui.png)");
    await page.goto("http://localhost:8080/swagger-ui/index.html", {
      waitUntil: "networkidle",
    });
    await page.waitForTimeout(3000); // Swagger can take longer to load
    const swaggerPath = path.join(backendScreenshotsDir, "swagger-ui.png");
    const swaggerSuccess = await captureScreenshotWithRetry(page, swaggerPath, {
      fullPage: true,
    });
    console.log(
      swaggerSuccess
        ? "✅ Swagger UI screenshot captured\n"
        : "❌ Failed to capture Swagger UI\n",
    );

    console.log("2️⃣  API Endpoints (api-endpoints.png)");
    // Try to expand endpoints or just capture current state
    try {
      const endpoints = await page.locator('[id*="operations-tag"]');
      if ((await endpoints.count()) > 0) {
        await endpoints.first().click();
        await page.waitForTimeout(500);
      }
    } catch (e) {
      console.log("  ℹ️  Could not expand endpoints, capturing current view");
    }

    const apiPath = path.join(backendScreenshotsDir, "api-endpoints.png");
    const apiSuccess = await captureScreenshotWithRetry(page, apiPath, {
      fullPage: true,
    });
    console.log(
      apiSuccess
        ? "✅ API endpoints screenshot captured\n"
        : "❌ Failed to capture API endpoints\n",
    );

    console.log("3️⃣  Error Responses (error-responses.png)");
    // Scroll down to show error response schemas if available
    await page.evaluate(() => window.scrollBy(0, 1000));
    await page.waitForTimeout(500);
    const errorPath = path.join(backendScreenshotsDir, "error-responses.png");
    const errorSuccess = await captureScreenshotWithRetry(page, errorPath, {
      fullPage: true,
    });
    console.log(
      errorSuccess
        ? "✅ Error responses screenshot captured\n"
        : "❌ Failed to capture error responses\n",
    );
  } catch (error) {
    console.log(`⚠️  Backend screenshots error: ${error.message}`);
  } finally {
    await page.close();
  }
}

async function main() {
  console.log("🚀 Starting screenshot capture process...");
  console.log(`📁 Output directory: ${screenshotsDir}`);
  console.log(`📁 Backend directory: ${backendScreenshotsDir}`);
  console.log(`⚙️  Minimum file size: ${MIN_FILE_SIZE} bytes`);
  console.log(`🔄 Maximum retries per screenshot: ${MAX_RETRIES}`);

  let browser;
  try {
    browser = await chromium.launch({
      headless: true,
    });

    // Capture frontend screenshots
    await captureFrontendScreenshots(browser);

    // Capture backend screenshots
    await captureBackendScreenshots(browser);

    console.log("\n✅ Screenshot capture completed!");
    console.log(
      `📁 Check ${screenshotsDir} and ${backendScreenshotsDir} for results`,
    );
  } catch (error) {
    console.error("❌ Fatal error:", error);
  } finally {
    if (browser) {
      await browser.close();
    }
  }
}

main();
