# Non-Technical Model Management Guide

---

- [Non-Technical Model Management Guide](#non-technical-model-management-guide)
  - [What This Feature Does](#what-this-feature-does)
  - [Quick Start](#quick-start)
    - [Step 1: Open Settings](#step-1-open-settings)
    - [Step 2: You'll See the Model Settings Panel](#step-2-youll-see-the-model-settings-panel)
  - [How to Add a Model](#how-to-add-a-model)
    - [What You Need to Know:](#what-you-need-to-know)
    - [Steps:](#steps)
  - [How to Remove a Model](#how-to-remove-a-model)
    - [Steps:](#steps-1)
  - [How to Reset to Defaults](#how-to-reset-to-defaults)
    - [Why Reset?](#why-reset)
    - [Steps:](#steps-2)
  - [Understanding the Model Table](#understanding-the-model-table)
  - [Where Are Models Stored?](#where-are-models-stored)
  - [Advanced: JSON Format](#advanced-json-format)
    - [Rules](#rules)
  - [Frequently Asked Questions](#frequently-asked-questions)
    - [Q: I deleted a model - can I undo it?](#q-i-deleted-a-model---can-i-undo-it)
    - [Q: Can I use the same model twice with different labels?](#q-can-i-use-the-same-model-twice-with-different-labels)
    - [Q: What happens if I import a file?](#q-what-happens-if-i-import-a-file)
    - [Q: Can I have no models?](#q-can-i-have-no-models)
    - [Q: Do models sync across devices?](#q-do-models-sync-across-devices)
    - [Q: Can the technical person update these for us?](#q-can-the-technical-person-update-these-for-us)
  - [Common Use Cases](#common-use-cases)
    - [Scenario 1: Team Uses Same Models](#scenario-1-team-uses-same-models)
    - [Scenario 2: Add Specific Model](#scenario-2-add-specific-model)
    - [Scenario 3: Backup Before Testing](#scenario-3-backup-before-testing)
  - [You're Ready!](#youre-ready)
  - [Need Help?](#need-help)

---

> ** Location:** `docs/NONTECHNICAL_MODEL_GUIDE.md`
> ** Audience:** Non-Technical Users, Stakeholders
> ** Related:** [Quick Start Advanced](QUICK_START_ADVANCED.md) | [Project Status](PROJECT_STATUS.md) | [Index](INDEX.md)

---

## What This Feature Does

The **Settings** tab allows you to **add, remove, and manage AI models** without needing to know how to code. You can:

- Add new AI models to the application
- Remove models you don't need
- Export your model list as a file to share with others
- Import model lists from team members
- Reset back to default models if needed

---

## Quick Start

### Step 1: Open Settings

1. Look for the **Settings** tab at the top of the application (between "Main Content" and "Additional Tools")
2. The tab has a (gear) icon
3. Click on it

### Step 2: You'll See the Model Settings Panel

The panel shows:

- A **table** listing all available models
- **Four buttons** for managing models:
  - **Add Model** - Create a new model
  - **Reset to Defaults** - Go back to original models

---

## How to Add a Model

### What You Need to Know:

- **Label**: The friendly name users will see (e.g., "My Custom AI")
- **Value**: The technical ID (e.g., "my-custom-ai-v1")

### Steps:

1. Click **Add Model** button
2. A dialog box appears with two fields
3. Enter the **Label** (what users will see)
4. Enter the **Value** (the model ID)
5. Click **Add**
6. New model appears in the table and in the DocumentUploadForm dropdown

**Example:**

```
Label: "GPT-4 Turbo"
Value: "gpt-4-turbo"
```

---

## How to Remove a Model

### Steps:

1. Find the model in the table
2. Click the **delete icon** (trash can) on the right side of the row
3. Model is removed
4. The model will no longer appear in DocumentUploadForm

**Note:** You must keep at least one model in the list.

---

## How to Reset to Defaults

### Why Reset?

- You accidentally deleted important models
- You want to start fresh
- You made mistakes and want to undo

### Steps:

1. Click **Reset to Defaults** button
2. A confirmation appears
3. Confirm your choice
4. All models return to the original list

**Note:** This cannot be undone, but you can import a backup if you exported one.

---

## Understanding the Model Table

The table shows all available models with:

| Column          | Meaning                        |
| --------------- | ------------------------------ |
| **Label**       | Friendly name (what users see) |
| **Value**       | Technical ID (used by system)  |
| **Delete Icon** | Remove this model              |

---

## Where Are Models Stored?

- Models are stored in your **browser's local storage**
- They persist even after you close and reopen the browser
- Each browser/computer has its own models (not shared automatically)
- To share with others: **Export as JSON** then have them **Import**

---

## Advanced: JSON Format

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

### Rules

- Must be valid JSON format
- Each model needs both "label" and "value"
- Use double quotes, not single quotes
- Items must be separated by commas
- Array starts with `[` and ends with `]`

---

## Frequently Asked Questions

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

## Common Use Cases

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

## You're Ready!

You now know how to:

- Add models
- Remove models
- Export configurations
- Import configurations
- Reset to defaults

**No coding required. No technical knowledge needed. Just click buttons!**

---

## Need Help?

If something isn't working:

1. Click **Reset to Defaults** to start fresh
2. Export your current models as backup before making big changes
3. Make sure JSON files are properly formatted
4. Contact your technical administrator if you have questions

---

**Last Updated:** February 2, 2026
**Maintained By:** java-resumes development team
