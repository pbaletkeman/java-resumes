# Frontend Screenshots Guide

Detailed guide for capturing and maintaining frontend UI screenshots for the java-resumes project.

## 📸 Frontend UI Screenshots

### Quick Reference

| Screenshot         | File                    | Size       | Frequency              | Status    |
| ------------------ | ----------------------- | ---------- | ---------------------- | --------- |
| Main Upload Tab    | `main-tab.png`          | 800x600px  | Per UI changes         | 📌 Needed |
| File History Panel | `file-history.png`      | 350x600px  | Per feature changes    | 📌 Needed |
| Tools Tab          | `tools-tab.png`         | 800x600px  | Per feature changes    | 📌 Needed |
| Light Theme        | `light-theme.png`       | 1200x800px | Per theme changes      | 📌 Needed |
| Dark Theme         | `dark-theme.png`        | 1200x800px | Per theme changes      | 📌 Needed |
| Mobile Responsive  | `responsive-mobile.png` | 375x812px  | Per responsive changes | 📌 Needed |

---

## 1. Main Upload Tab

### What to Show

The primary interface for resume optimization.

### Components to Include

```
┌─────────────────────────────────────────────────┐
│  Main Content Tab (highlighted)                 │
├─────────────────────────────────────────────────┤
│                                                 │
│  Job Description                                │
│  ┌───────────────────────────────────────────┐  │
│  │ Paste job description or upload file...   │  │
│  │                                            │  │
│  │ [Sample job description would appear here]│  │
│  └───────────────────────────────────────────┘  │
│                                                 │
│  Resume/CV                                      │
│  ┌───────────────────────────────────────────┐  │
│  │ Paste resume or upload file...            │  │
│  │                                            │  │
│  │ [Sample resume content would appear here] │  │
│  └───────────────────────────────────────────┘  │
│                                                 │
│  Optimization Options:                          │
│  ☑ Resume  ☑ Cover Letter                       │
│                                                 │
│  Model: [mistral ▼]    Temperature: [═══●───]   │
│                                                 │
│  [         Process Document          ]          │
│                                                 │
│  Output Preview:                                │
│  ┌───────────────────────────────────────────┐  │
│  │ Optimization results appear here...       │  │
│  │ Markdown format with formatting preview  │  │
│  └───────────────────────────────────────────┘  │
│                                                 │
└─────────────────────────────────────────────────┘
```

### Capture Instructions

1. **Start Development Server**

   ```bash
   cd frontend
   npm run dev
   ```

2. **Prepare Sample Data**
   - Use provided sample job description
   - Use provided sample resume
   - Sample data available in `sample/` directory

3. **Take Screenshot**
   - Open DevTools (F12)
   - Right-click on page → Screenshot (or use Snip & Sketch)
   - Capture the main content area
   - Include both input and preview sections

4. **Optimize Image**
   - Resize to 800x600px
   - Compress with TinyPNG
   - Verify text is readable

5. **Save File**
   ```
   docs/screenshots/frontend/main-tab.png
   ```

### Verification Checklist

- [ ] All input fields visible
- [ ] Buttons are highlighted/clear
- [ ] Text is readable (no blur)
- [ ] Colors match current theme
- [ ] Sample data is visible
- [ ] File size < 300KB

---

## 2. File History Panel

### What to Show

The right sidebar showing file management.

### Components to Include

```
┌──────────────────┐
│  📄 File History │
├──────────────────┤
│                  │
│ 📄 resume.pdf    │
│ ↓ 📥 🗑️           │
│                  │
│ 📄 resume_opt.md │
│ ↓ 📥 🗑️           │
│                  │
│ 📄 cover_ltr.pdf │
│ ↓ 📥 🗑️           │
│                  │
│ 📄 cover_ltr.md  │
│ ↓ 📥 🗑️           │
│                  │
│ [  Clear All  ]  │
│                  │
└──────────────────┘
```

### Capture Instructions

1. **Upload Multiple Files**
   - Upload 3-4 different files
   - Mix of PDFs and Markdown files
   - Vary file names and sizes

2. **Generate Some Documents**
   - Process a document to generate optimized versions
   - Should show both input and output files

3. **Take Screenshot**
   - Capture just the right sidebar
   - Include all files in list
   - Ensure all buttons are visible

4. **Save File**
   ```
   docs/screenshots/frontend/file-history.png
   ```

### Verification Checklist

- [ ] All files visible in list
- [ ] File icons are clear
- [ ] Timestamps visible (if included)
- [ ] Download/delete buttons visible
- [ ] Clear All button prominent
- [ ] Scrollbar visible if needed
- [ ] File size < 200KB

---

## 3. Additional Tools Tab

### What to Show

The tools section with Markdown to PDF converter.

### Components to Include

```
┌────────────────────────────────────────────────┐
│  Additional Tools Tab (highlighted)            │
├────────────────────────────────────────────────┤
│                                                │
│  Markdown to PDF Converter                     │
│                                                │
│  Input Markdown:                               │
│  ┌──────────────────────────────────────────┐  │
│  │ # Resume Title                           │  │
│  │                                           │  │
│  │ ## Professional Summary                  │  │
│  │ Experienced developer with expertise...  │  │
│  │                                           │  │
│  │ ## Experience                            │  │
│  │ ### Senior Developer at Company          │  │
│  └──────────────────────────────────────────┘  │
│                                                │
│  [ Upload Markdown ] [ Convert to PDF ]        │
│                                                │
│  PDF Preview:                                  │
│  ┌──────────────────────────────────────────┐  │
│  │  Resume Title                            │  │
│  │                                           │  │
│  │  Professional Summary                    │  │
│  │  Experienced developer with expertise... │  │
│  │                                           │  │
│  │  [ Download PDF ]                        │  │
│  └──────────────────────────────────────────┘  │
│                                                │
└────────────────────────────────────────────────┘
```

### Capture Instructions

1. **Navigate to Tools Tab**
   - Click "Additional Tools" tab
   - Ensure main tab is not selected

2. **Prepare Markdown**
   - Paste sample markdown with proper formatting
   - Include headers, lists, and bold text
   - Keep content professional

3. **Generate PDF Preview**
   - Click Convert button (if applicable)
   - Wait for preview to render

4. **Take Screenshot**
   - Capture full tools section
   - Include both input and preview
   - Show convert button

5. **Save File**
   ```
   docs/screenshots/frontend/tools-tab.png
   ```

### Verification Checklist

- [ ] Markdown input visible
- [ ] Convert button prominent
- [ ] PDF preview shows formatting
- [ ] Download button visible
- [ ] All text readable
- [ ] File size < 300KB

---

## 4. Light Theme

### What to Show

Complete application interface in light theme.

### Capture Instructions

1. **Enable Light Theme**
   - Look for Settings or Theme toggle
   - Select "Light Theme"
   - Verify background is light

2. **Full Application View**
   - Maximize browser window (1200x800px minimum)
   - Show full interface with content
   - Ensure all UI elements visible

3. **Optimal Timing**
   - Application should be fully loaded
   - Have some content in the interface
   - Show multiple UI sections

4. **Take Screenshot**
   - Use full-page screenshot capability
   - Or use browser's screenshot tool
   - Resize to 1200x800px

5. **Save File**
   ```
   docs/screenshots/frontend/light-theme.png
   ```

### Verification Checklist

- [ ] Background is light (near white)
- [ ] Text is dark and readable
- [ ] All UI elements visible
- [ ] No dark overlays
- [ ] Buttons clearly visible
- [ ] File size < 400KB

---

## 5. Dark Theme

### What to Show

Complete application interface in dark theme.

### Capture Instructions

1. **Enable Dark Theme**
   - Click Settings or Theme toggle
   - Select "Dark Theme"
   - Verify background is dark

2. **Full Application View**
   - Maximize browser window (1200x800px minimum)
   - Show full interface with content
   - Ensure accent colors are visible

3. **Content for Context**
   - Have some data in the interface
   - Show various UI elements
   - Demonstrate text contrast

4. **Take Screenshot**
   - Use full-page screenshot capability
   - Ensure no auto-brightness adjustment
   - Resize to 1200x800px

5. **Save File**
   ```
   docs/screenshots/frontend/dark-theme.png
   ```

### Verification Checklist

- [ ] Background is dark (dark gray/black)
- [ ] Text is light and readable
- [ ] All UI elements visible
- [ ] Accent colors pop
- [ ] Buttons clearly distinguished
- [ ] File size < 400KB

---

## 6. Responsive Mobile View

### What to Show

Application interface on mobile device.

### Capture Instructions

1. **Open DevTools**
   - Press F12 or right-click → Inspect
   - Open Device Toolbar (Ctrl+Shift+M)

2. **Select Device**
   - Choose iPhone 12 (375x812px)
   - Or adjust to mobile size manually

3. **Adjust Content**
   - Add some content to application
   - Verify responsive layout works
   - Check all buttons are accessible

4. **Take Screenshot**
   - DevTools menu → Screenshot
   - Or use Windows Snip & Sketch
   - Capture full mobile viewport

5. **Save File**
   ```
   docs/screenshots/frontend/responsive-mobile.png
   ```

### Verification Checklist

- [ ] Mobile layout is responsive
- [ ] Text is readable on small screen
- [ ] Buttons are touch-friendly
- [ ] No horizontal scrolling needed
- [ ] All features accessible
- [ ] Image size exactly 375x812px
- [ ] File size < 250KB

---

## 📋 General Screenshot Guidelines

### Before Capturing

1. ✅ Ensure application is properly styled
2. ✅ Clear browser cache (Ctrl+Shift+Del)
3. ✅ Disable extensions that might affect appearance
4. ✅ Zoom to 100% (Ctrl+0)
5. ✅ Maximize browser window
6. ✅ Add representative sample data

### During Capturing

1. ✅ Use high-contrast theme
2. ✅ Ensure good lighting
3. ✅ Avoid reflections on screen
4. ✅ Capture multiple angles if needed
5. ✅ Verify no sensitive data is visible

### After Capturing

1. ✅ Optimize file size (TinyPNG)
2. ✅ Verify image clarity
3. ✅ Check file permissions
4. ✅ Add to Git version control
5. ✅ Update documentation links

---

## 🔄 Maintenance Schedule

### When to Update

- **Daily Development**: After major UI changes
- **Feature Branch**: When adding new features
- **Release Candidate**: Week before release
- **Quarterly Review**: At least every 3 months
- **Bug Fixes**: If visual appearance changes

### Update Process

1. Take new screenshots following guidelines
2. Compare with current versions
3. Replace old files if significantly different
4. Verify all documentation links still work
5. Test on multiple devices
6. Commit with descriptive message

---

## 🎨 Styling Consistency

### Colors Should Match

- **Light Theme**:
  - Background: #F5F5F5 or similar light gray
  - Text: #333333 or similar dark gray
  - Accents: Primary color (usually blue)

- **Dark Theme**:
  - Background: #1E1E1E or similar dark gray
  - Text: #E0E0E0 or similar light gray
  - Accents: Primary color (usually vibrant)

### Component Consistency

- Button styles consistent
- Input field styling matches
- Icons are properly rendered
- Font sizes are readable
- Spacing is uniform

---

## 💾 Storage & Organization

### File Naming

```
{category}-{component}-{description}.png

Examples:
- frontend-main-upload-interface.png
- frontend-file-history-panel.png
- frontend-tools-markdown-converter.png
- frontend-light-theme-full.png
- frontend-dark-theme-full.png
- frontend-responsive-mobile-iphone12.png
```

### Directory Structure

```
docs/screenshots/frontend/
├── main-tab.png
├── file-history.png
├── tools-tab.png
├── light-theme.png
├── dark-theme.png
└── responsive-mobile.png
```

### Version Control

- Store in Git with .gitignore for large files
- Or use Git LFS for binary files
- Document image source (screenshot tool, date)
- Keep changelog of updates

---

## 📞 Troubleshooting

### Images Look Blurry

- Ensure zoom is at 100%
- Capture at correct resolution
- Use native screenshot tool
- Avoid stretching in documentation

### Text Not Readable

- Increase contrast in settings
- Capture at higher resolution
- Use cleaner fonts
- Reduce image compression

### Colors Look Wrong

- Check monitor color calibration
- Verify CSS has loaded
- Disable dark mode filters
- Take screenshot again

### File Size Too Large

- Use TinyPNG for compression
- Reduce color depth if possible
- Use PNG instead of JPG/WebP
- Remove unnecessary metadata

---

## 📚 References

- [Main Screenshots Guide](../SCREENSHOTS_GUIDE.md)
- [Backend Screenshots Guide](../backend/README.md)
- [Architecture Diagrams](../architecture/DIAGRAMS.md)
- [Frontend README](../../frontend/README.md)

---

Last Updated: 2026-01-16
