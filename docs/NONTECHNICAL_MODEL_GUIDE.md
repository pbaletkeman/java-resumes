# Non-Technical Model Management Guide

> **📍 Location:** `docs/NONTECHNICAL_MODEL_GUIDE.md`
> **👥 Audience:** Non-Technical Users, Stakeholders
> **🔗 Related:** [Quick Start Advanced](QUICK_START_ADVANCED.md) | [Project Status](PROJECT_STATUS.md) | [Index](INDEX.md)

---

## 🎯 What This Feature Does

The **Settings** tab allows you to **add, remove, and manage AI models** without needing to know how to code. You can:

- ✅ Add new AI models to the application
- ✅ Remove models you don't need
- ✅ Export your model list as a file to share with others
- ✅ Import model lists from team members
- ✅ Reset back to default models if needed

---

## 🚀 Quick Start

### Step 1: Open Settings

1. Look for the **Settings** tab at the top of the application (between "Main Content" and "Additional Tools")
2. The tab has a ⚙️ (gear) icon
3. Click on it

### Step 2: You'll See the Model Settings Panel

The panel shows:

- A **table** listing all available models
- **Four buttons** for managing models:
  - **Add Model** - Create a new model
  - **Export Models** - Download as JSON file
  - **Import Models** - Upload a JSON file
  - **Reset to Defaults** - Go back to original models

---

## 📋 How to Add a Model

### What You Need to Know:

- **Label**: The friendly name users will see (e.g., "My Custom AI")
- **Value**: The technical ID (e.g., "my-custom-ai-v1")

### Steps:

1. Click **Add Model** button
2. A dialog box appears with two fields
3. Enter the **Label** (what users will see)
4. Enter the **Value** (the model ID)
5. Click **Add**
6. ✅ New model appears in the table and in the DocumentUploadForm dropdown

**Example:**

```
Label: "GPT-4 Turbo"
Value: "gpt-4-turbo"
```

---

## 🗑️ How to Remove a Model

### Steps:

1. Find the model in the table
2. Click the **delete icon** (trash can) on the right side of the row
3. ✅ Model is removed
4. The model will no longer appear in DocumentUploadForm

**Note:** You must keep at least one model in the list.

---

## 📤 How to Export Models (Share Configuration)

### Why Export?

- Share your model configuration with team members
- Back up your current model list
- Move configuration to another computer

### Steps:

1. Click **Export Models** button
2. A JSON file automatically downloads to your computer
3. ✅ File is named something like `models.json`
4. Share this file with your team

### What You Get:

```json
[
  { "label": "Gemma 3.4B (Default)", "value": "gemma-3-4b-it" },
  { "label": "GPT-4 Turbo", "value": "gpt-4-turbo" },
  { "label": "My Custom Model", "value": "my-model" }
]
```

---

## 📥 How to Import Models (Load Shared Configuration)

### Why Import?

- Use model configurations shared by team members
- Restore a previously exported backup
- Apply standardized model list

### Steps:

1. Get the JSON file from someone or from backup
2. Click **Import Models** button
3. Select the JSON file from your computer
4. ✅ Models load immediately
5. Any previous models are replaced with the imported list

**Note:** Make sure the file is valid JSON before importing.

---

## 🔄 How to Reset to Defaults

### Why Reset?

- You accidentally deleted important models
- You want to start fresh
- You made mistakes and want to undo

### Steps:

1. Click **Reset to Defaults** button
2. A confirmation appears
3. Confirm your choice
4. ✅ All models return to the original list

**Note:** This cannot be undone, but you can import a backup if you exported one.

---

## 📊 Understanding the Model Table

The table shows all available models with:

| Column          | Meaning                        |
| --------------- | ------------------------------ |
| **Label**       | Friendly name (what users see) |
| **Value**       | Technical ID (used by system)  |
| **Delete Icon** | Remove this model              |

---

## 💾 Where Are Models Stored?

- Models are stored in your **browser's local storage**
- They persist even after you close and reopen the browser
- Each browser/computer has its own models (not shared automatically)
- To share with others: **Export as JSON** then have them **Import**

---

## ⚡ Advanced: JSON Format

If you want to create a models JSON file manually:

```json
[
  {
    "label": "Display Name Here",
    "value": "model-id-here"
  },
  {
    "label": "Another Model",
    "value": "another-model-id"
  }
]
```

### Rules:

- Must be valid JSON format
- Each model needs both "label" and "value"
- Use double quotes, not single quotes
- Items must be separated by commas
- Array starts with `[` and ends with `]`

---

## ❓ Frequently Asked Questions

### Q: I deleted a model - can I undo it?

**A:** Not directly, but if you exported before deleting, you can import the backup. Otherwise, click "Reset to Defaults".

### Q: Can I use the same model twice with different labels?

**A:** Yes! Example:

```json
{ "label": "Fast Model", "value": "gpt-4" }
{ "label": "Accurate Model", "value": "gpt-4" }
```

### Q: What happens if I import a file?

**A:** Your current models are replaced with the imported list. Export first if you want to keep them!

### Q: Can I have no models?

**A:** No, you must have at least one model. The system prevents you from deleting all models.

### Q: Do models sync across devices?

**A:** No, each browser/device has its own models. Export and import to share.

### Q: Can the technical person update these for us?

**A:** Yes! They can:

1. Create a JSON file with your models
2. You import it here
3. Everyone gets the same models

---

## 🎓 Common Use Cases

### Scenario 1: Team Uses Same Models

1. Technical lead creates `team-models.json`
2. Sends file to all team members
3. Each member imports the file
4. Everyone sees the same models

### Scenario 2: Add Specific Model

1. Manager wants to try new model "Claude 3"
2. Goes to Settings
3. Clicks Add Model
4. Label: "Claude 3", Value: "claude-3-opus"
5. Model appears for all users

### Scenario 3: Backup Before Testing

1. Click Export Models
2. Save the file as "backup.json"
3. Delete/add models for testing
4. If something breaks, import "backup.json"
5. Everything restored

---

## ✅ You're Ready!

You now know how to:

- ✅ Add models
- ✅ Remove models
- ✅ Export configurations
- ✅ Import configurations
- ✅ Reset to defaults

**No coding required. No technical knowledge needed. Just click buttons!**

---

## 📞 Need Help?

If something isn't working:

1. Click **Reset to Defaults** to start fresh
2. Export your current models as backup before making big changes
3. Make sure JSON files are properly formatted
4. Contact your technical administrator if you have questions

---

**Last Updated**: January 17, 2025
