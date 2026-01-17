# Generated & Uploaded Files Repository

This directory serves as the central hub for all document management within the Resume Optimization System. It houses both user-uploaded materials and AI-generated outputs, maintaining a complete record of all processing activities.

## Table of Contents

- [Purpose](#purpose)
- [What Gets Stored Here](#what-gets-stored-here)
- [File Organization](#file-organization)
- [Accessing Files](#accessing-files)
- [File Management Best Practices](#file-management-best-practices)
- [Storage Limits](#storage-limits)
- [API Integration](#api-integration)
- [Security Considerations](#security-considerations)
- [Troubleshooting](#troubleshooting)
- [Integration with Processing Pipeline](#integration-with-processing-pipeline)

---

## Purpose

The `files` directory is structured to efficiently manage:

- **Uploaded Assets**: User-submitted resumes, job descriptions, and source documents
- **Generated Outputs**: AI-optimized resumes, cover letters, and PDF conversions
- **Processing History**: Complete audit trail of all document transformations and versions

---

## What Gets Stored Here

### Uploaded Documents

- Resume files in various formats (PDF, TXT, DOCX)
- Job description documents
- Reference materials and supplementary documents

### Generated Outputs

- Optimized resume versions
- AI-generated cover letters
- Markdown-to-PDF conversions
- Metadata files and processing logs

---

## File Organization

Files are automatically organized with:

- **Timestamps**: Creation and modification dates for version tracking
- **Descriptive Names**: Clear naming conventions indicating content type
- **Format Support**: Multiple file formats for compatibility

---

## Accessing Files

### Listing Files

Use the **List Files** endpoint to view all available documents:

```shell
GET /api/files
```

### Downloading Files

Retrieve any file by name:

```shell
GET /api/files/{filename}
```

### Deleting Files

Remove files when no longer needed:

```shell
DELETE /api/files/{filename}
```

---

## File Management Best Practices

1. **Naming Convention**: Use descriptive, timestamp-friendly names
   - Good: `Resume-2026-01-17.pdf`, `Job-Description-TechCorp.txt`
   - Avoid: `Resume.pdf`, `File1.txt`, `Latest.docx`

2. **Version Control**: Keep track of file iterations
   - Original uploads remain for reference
   - Generated versions include processing date
   - Remove outdated versions to save space

3. **File Cleanup**: Periodically delete processed files
   - Use the Delete File endpoint
   - Archive important documents externally
   - Maintain performance and storage efficiency

---

## Storage Limits

- **Individual File Size**: Limited by system configuration
- **Total Storage**: Depends on available disk space
- **Retention Policy**: Files persist until manually deleted

---

## API Integration

The file management system integrates seamlessly with the Resume Optimization API:

```json
{
  "filename": "Resume-Optimized-2026-01-17.pdf",
  "url": "http://localhost:8080/api/files/Resume-Optimized-2026-01-17.pdf",
  "date": "2026-01-17 14:30",
  "size": "245KB"
}
```

---

## Security Considerations

- Files are stored in a dedicated directory separate from application code
- Access is controlled through the API endpoints
- No direct file system access required for normal operations
- Sensitive information should be removed before file deletion

---

## Troubleshooting

**File Not Found When Listing**:

- Ensure upload completed successfully
- Check exact filename (case-sensitive on some systems)

**Download Errors**:

- Verify the file exists using List Files
- Check file permissions
- Ensure sufficient disk space

**Storage Full**:

- Use Delete File endpoint to remove unnecessary documents
- Archive important files to external storage
- Monitor available disk space

---

## Integration with Processing Pipeline

1. User uploads resume and job description
2. Files stored in this directory
3. Backend processes documents
4. Generated outputs saved here with versioning
5. User downloads optimized results

---

**Directory Path**: `files/`
**Purpose**: Central file storage for uploads and generated content
**Access**: Via REST API endpoints only
**Last Updated**: January 17, 2026
