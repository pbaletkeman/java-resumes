import { chromium } from 'playwright';
import * as fs from 'fs';
import * as path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

async function takeScreenshot() {
  const browser = await chromium.launch();
  const page = await browser.newPage();
  page.setViewportSize({ width: 1280, height: 1024 });

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

    // Check specific elements with better selectors
    console.log('\n--- Element Checks ---');

    // Find Job Description and Source Resume fields by ID
    const jobDescTextarea = page.locator('#jobDescription');
    const resumeTextarea = page.locator('#resume');

    const hasJobDesc = await jobDescTextarea.isVisible().catch(() => false);
    const hasResume = await resumeTextarea.isVisible().catch(() => false);

    console.log(`✓ Job Description textarea visible: ${hasJobDesc}`);
    console.log(`✓ Source Resume textarea visible: ${hasResume}`);

    // Check for Resume Optimizer header (red box)
    const resumeHeader = page.locator('text=Resume Optimizer').first();
    const hasHeader = await resumeHeader.isVisible().catch(() => false);
    console.log(`✓ Resume Optimizer header visible: ${hasHeader}`);

    // Check for Input Mode selector
    const inputModeLabel = page.locator('text=Input Mode').first();
    const hasInputMode = await inputModeLabel.isVisible().catch(() => false);
    console.log(`✓ Input Mode selector visible: ${hasInputMode}`);

    // Check for cyan box content
    const cyantitle = page.locator('text=Document Upload & Processing').first();
    const hasCyanBox = await cyantitle.isVisible().catch(() => false);
    console.log(`✓ Document Upload & Processing (cyan box) visible: ${hasCyanBox}`);

    // Check for yellow box around Input Mode
    const yellowBox = page.locator('.bg-yellow-50').first();
    const hasYellowBox = await yellowBox.isVisible().catch(() => false);
    console.log(`✓ Yellow box around Input Mode visible: ${hasYellowBox}`);

    // Verify layout by measuring container positions and order
    if (hasHeader && hasInputMode && hasCyanBox) {
      console.log('\n--- VISUAL LAYOUT VERIFICATION ---');

      // Get the container bounds
      const redContainer = await page.locator('.bg-red-50').first().boundingBox();
      const yellowContainer = await page.locator('.bg-yellow-50').first().boundingBox();
      const cyanContainer = await page.locator('.bg-cyan-50').first().boundingBox();

      // Calculate positions
      const redBottom = redContainer ? Math.round(redContainer.y + redContainer.height) : null;
      const yellowBottom = yellowContainer
        ? Math.round(yellowContainer.y + yellowContainer.height)
        : null;

      console.log('\n📍 CONTAINER POSITIONS:');
      if (redContainer) {
        console.log(`RED BOX: Y=${redContainer.y}px, ends at Y=${redBottom}px`);
      }
      if (yellowContainer) {
        console.log(`YELLOW BOX: Y=${yellowContainer.y}px, ends at Y=${yellowBottom}px`);
      }
      if (cyanContainer) {
        console.log(`CYAN BOX: Y=${cyanContainer.y}px`);
      }

      console.log('\n✅ LAYOUT ORDER:');
      if (redContainer && yellowContainer && cyanContainer) {
        const redAboveYellow = redContainer.y < yellowContainer.y;
        const yellowAboveCyan = yellowContainer.y < cyanContainer.y;

        console.log(`✓ Red above Yellow: ${redAboveYellow}`);
        console.log(`✓ Yellow above Cyan: ${yellowAboveCyan}`);

        if (redAboveYellow && yellowAboveCyan) {
          console.log('\n✅✅✅ LAYOUT CORRECT: Red → Yellow → Cyan (top to bottom)');
        }
      }
    }

    // Check button visibility and styling
    const pasteTextBtn = await page.locator('button').filter({ hasText: 'Paste Text' }).count();
    const uploadFilesBtn = await page.locator('button').filter({ hasText: 'Upload Files' }).count();
    console.log(`✓ Paste Text buttons found: ${pasteTextBtn}`);
    console.log(`✓ Upload Files buttons found: ${uploadFilesBtn}`);

    // Check if textareas are stacked
    if (hasJobDesc && hasResume) {
      console.log('\n--- Field Positioning Check ---');
      const jobDescBox = await jobDescTextarea.boundingBox();
      const resumeBox = await resumeTextarea.boundingBox();

      if (jobDescBox && resumeBox) {
        const isStacked = resumeBox.y > jobDescBox.y + 100;
        console.log(`Job Description Y: ${jobDescBox.y}px (height: ${jobDescBox.height}px)`);
        console.log(`Source Resume Y: ${resumeBox.y}px (height: ${resumeBox.height}px)`);
        console.log(`Y difference: ${resumeBox.y - jobDescBox.y}px`);
        console.log(`✓ Fields are stacked vertically: ${isStacked}`);
      }
    }

    // Check Header and Input Mode positioning
    if (hasHeader && hasInputMode) {
      console.log('\n--- Layout Structure Check ---');
      const headerBox = await resumeHeader.boundingBox();
      const inputModeBox = await inputModeLabel.boundingBox();

      if (headerBox && inputModeBox) {
        console.log(`Resume Optimizer Header Y: ${headerBox.y}px`);
        console.log(`Input Mode Label Y: ${inputModeBox.y}px`);
        console.log(`✓ Header is above Input Mode: ${headerBox.y < inputModeBox.y}`);
      }
    }

    // Check cyan box is below input mode
    if (hasInputMode && hasCyanBox) {
      console.log('\n--- Cyan Box Positioning ---');
      const inputModeBox = await inputModeLabel.boundingBox();
      const cyanBox = await cyantitle.boundingBox();

      if (inputModeBox && cyanBox) {
        console.log(`Input Mode Y: ${inputModeBox.y}px`);
        console.log(`Cyan Box Content Y: ${cyanBox.y}px`);
        console.log(`✓ Cyan box is below Input Mode: ${cyanBox.y > inputModeBox.y}`);
      }
    }

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
