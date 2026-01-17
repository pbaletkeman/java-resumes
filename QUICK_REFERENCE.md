# Quick Reference: Frontend Layout Verification

## Verification Command

```bash
cd c:\Users\Pete\Desktop\java-resumes\frontend
node playwright-screenshot.mjs
```

## Expected Output (Passing)

```
✓ Job Description textarea visible: true
✓ Source Resume textarea visible: true
✓ Fields are stacked vertically: true

Field Positioning:
Job Description Y: 38px
Source Resume Y: 472px
Y difference: 434px ✓ (> 100px = stacked)
```

## Layout Verification Checklist

Use this checklist to verify the layout meets all requirements:

### ✅ Form Structure

- [ ] Resume Optimizer header visible with icon
- [ ] Input Mode selector has outlined border container
- [ ] "Paste Text" and "Upload Files" buttons have spacing (gap-6)
- [ ] "Document Upload & Processing" label visible

### ✅ Text Input Fields (Paste Mode)

- [ ] Job Description label visible
- [ ] Job Description textarea displays
- [ ] Job Description textarea is at least 400px tall
- [ ] Source Resume label visible below Job Description
- [ ] Source Resume textarea displays below (not beside)
- [ ] Source Resume textarea is at least 400px tall
- [ ] Both textareas are full width

### ✅ File Upload Fields (Upload Mode)

- [ ] Job Description File label visible
- [ ] Job Description File upload button displays
- [ ] Resume File label visible below
- [ ] Resume File upload button displays below (not beside)

### ✅ Sidebar & Layout

- [ ] FileHistory sidebar visible on right side
- [ ] FileHistory doesn't overlap with main form
- [ ] Main form content has adequate space
- [ ] Navbar "Resume Optimizer" doesn't take full width
- [ ] Layout is responsive (tested at 1280px viewport)

### ✅ Automated Verification

- [ ] Playwright script runs without errors
- [ ] Screenshots saved to `frontend/screenshots/`
- [ ] Element positioning report shows Y difference > 100px
- [ ] No console errors in browser

## Files to Check

| File                                          | What to Look For                         |
| --------------------------------------------- | ---------------------------------------- |
| `src/components/Forms/DocumentUploadForm.tsx` | Form layout, field stacking, header      |
| `src/layouts/MainLayout.tsx`                  | Sidebar width: `w-2 md:w-2 lg:w-2`       |
| `src/components/Navbar.tsx`                   | flex-shrink-0, whitespace-nowrap classes |
| `playwright-screenshot.mjs`                   | Automation script for verification       |

## CSS Classes & Inline Styles

### Form Wrapper (Fixed Stacking)

```tsx
<div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
```

### Field Container (Block Display)

```tsx
<div className="field w-full" style={{ display: 'block' }}>
```

### Textarea (Full Width)

```tsx
<InputTextarea
  rows={20}
  className="w-full"
  style={{ minHeight: "400px", width: "100%", display: "block" }}
/>
```

## Troubleshooting

### Issue: Textareas Still Side-by-Side

**Check**: Inline CSS on wrapper div

- Must have: `display: 'flex'` AND `flexDirection: 'column'`
- Location: Wrapper div around field divs

### Issue: Input Mode Buttons Too Close

**Check**: SelectButton className

- Should have: `className="gap-6"`
- Location: SelectButton props in DocumentUploadForm.tsx

### Issue: FileHistory Overlapping Content

**Check**: MainLayout.tsx sidebar width

- Should be: `w-2 md:w-2 lg:w-2` (2/12 columns = 16.67%)
- Not: `w-full md:w-4` (100% on mobile)

### Issue: Playwright Script Errors

**Check**: Dependencies installed

```bash
npm install --save-dev @playwright/test playwright
npx playwright install
```

### Issue: Elements Not Found in Playwright

**Check**: Element IDs match

- Job Description textarea: `id="jobDescription"` → selector `#jobDescription`
- Source Resume textarea: `id="resume"` → selector `#resume`

## Key Measurements

| Component       | Measurement       | Purpose                     |
| --------------- | ----------------- | --------------------------- |
| Textarea Height | 400px (minHeight) | Optimal size for user input |
| Textarea Rows   | 20 rows           | Visible line count          |
| Y-Position Gap  | 434px             | Confirms vertical stacking  |
| Form Card Width | 932px             | ~70% of 1280px viewport     |
| Sidebar Width   | 2/12 columns      | 16.67% of screen            |

## Screenshot Naming Convention

Automatically generated in `frontend/screenshots/`:

```
page-layout-{YYYY-MM-DDTHH-MM-SS-sssZ}.png
page-viewport-{YYYY-MM-DDTHH-MM-SS-sssZ}.png
```

Example: `page-layout-2026-01-17T21-15-28-862Z.png`

## Running Playwright Tests

### One-Time Screenshot

```bash
node playwright-screenshot.mjs
```

### Install/Setup Playwright

```bash
npm install --save-dev @playwright/test playwright
npx playwright install
```

### View Latest Screenshots

```bash
cd frontend/screenshots
start .  # Opens folder in Explorer
```

## Verification Pass/Fail Criteria

### ✅ PASS

- Job Description Y: ~38px
- Source Resume Y: ~472px
- Y Difference: 434px
- Status: Fields stacked vertically = **TRUE**

### ❌ FAIL

- Both Y positions identical or very close (< 100px difference)
- Status: Fields stacked vertically = **FALSE**

## Support

For additional information, see:

- `LAYOUT_REFINEMENT_SUMMARY.md` - Detailed changes
- `PLAYWRIGHT_VERIFICATION_REPORT.md` - Test results
- `SESSION_LOG.md` - Complete work history

---

**Last Updated**: January 17, 2026
**Status**: ✅ Layout Verified and Approved
**Next Update**: As layout requirements change
