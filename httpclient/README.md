# HTTP Client Collection

This directory contains API request collections formatted for [VS Code REST Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client).

- [HTTP Client Collection](#http-client-collection)
  - [Files](#files)
    - [Resume.http](#resumehttp)
  - [Prerequisites](#prerequisites)
  - [Usage](#usage)
    - [Basic Workflow](#basic-workflow)
    - [Running Requests](#running-requests)
  - [Resume API Endpoints](#resume-api-endpoints)
    - [Health Check](#health-check)
    - [List Files](#list-files)
    - [Download File](#download-file)
    - [Delete File](#delete-file)
    - [Delete All Files](#delete-all-files)
    - [Convert Markdown to PDF](#convert-markdown-to-pdf)
    - [Optimize Resume](#optimize-resume)
  - [Request Examples](#request-examples)
    - [Setting Variables](#setting-variables)
    - [File Uploads](#file-uploads)
    - [Custom Headers](#custom-headers)
  - [Tips \& Best Practices](#tips--best-practices)
  - [Response Handling](#response-handling)
  - [Troubleshooting](#troubleshooting)
    - [SSL Certificate Issues](#ssl-certificate-issues)
    - [Port Not Accessible](#port-not-accessible)
    - [Timeout Issues](#timeout-issues)
  - [Resources](#resources)
  - [Related Tools](#related-tools)

## Files

### Resume.http

A complete collection of Resume API endpoints for testing and development.

## Prerequisites

Install the REST Client extension for VS Code:

- **Marketplace**: [humao.rest-client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client)
- **Command**: `ext install humao.rest-client`

## Usage

### Basic Workflow

1. Open any `.http` file in VS Code
2. Hover over or click the **Send Request** link above any endpoint
3. View the response in the **Response** panel on the right side
4. Use the response output for debugging and testing

### Running Requests

```
GET https://localhost:8080/api/health

### [Comment separators between requests]

POST https://localhost:8080/api/files
Content-Type: application/json

{
  "filename": "resume.pdf"
}
```

## Resume API Endpoints

### Health Check

- **Method**: `GET`
- **URL**: `https://localhost:8080/api/health`
- **Purpose**: Verify API is running

### List Files

- **Method**: `GET`
- **URL**: `https://localhost:8080/api/files`
- **Purpose**: Retrieve all available files

### Download File

- **Method**: `GET`
- **URL**: `https://localhost:8080/api/files/{filename}`
- **Purpose**: Download a specific file
- **Parameters**: `{filename}` - name of the file to download

### Delete File

- **Method**: `DELETE`
- **URL**: `https://localhost:8080/api/files/{filename}`
- **Purpose**: Delete a specific file
- **Parameters**: `{filename}` - name of the file to delete

### Delete All Files

- **Method**: `POST`
- **URL**: `https://localhost:8080/api/delete-all`
- **Purpose**: Remove all files (use with caution)

### Convert Markdown to PDF

- **Method**: `POST`
- **URL**: `https://localhost:8080/api/markdownFile2PDF`
- **Content-Type**: `multipart/form-data`
- **Purpose**: Convert Markdown files to PDF
- **Parameters**:
  - `file` - Markdown file to convert

### Optimize Resume

- **Method**: `POST`
- **URL**: `https://localhost:8080/api/upload`
- **Content-Type**: `multipart/form-data`
- **Purpose**: Optimize resume against job description
- **Parameters**:
  - `optimize` - JSON config: `{"resume": "", "jobDescription": ""}`
  - `resume` - Resume file (PDF)
  - `job` - Job description file (TXT)

## Request Examples

### Setting Variables

Create a `settings.json` or use environment variables:

```json
{
  "rest-client.environmentVariables": {
    "$shared": {
      "baseUrl": "https://localhost:8080"
    },
    "local": {
      "baseUrl": "https://localhost:8080"
    },
    "production": {
      "baseUrl": "https://api.example.com"
    }
  }
}
```

Then use in requests:

```
GET {{baseUrl}}/api/health
```

### File Uploads

Reference local files with `<` operator:

```
POST https://localhost:8080/api/markdownFile2PDF
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary

------WebKitFormBoundary
Content-Disposition: form-data; name="file"; filename="resume.md"
Content-Type: text/markdown

< ./path/to/resume.md
------WebKitFormBoundary--
```

### Custom Headers

```
GET https://localhost:8080/api/files
Authorization: Bearer YOUR_TOKEN_HERE
X-Custom-Header: CustomValue
```

## Tips & Best Practices

- **Keyboard Shortcut**: Use `Ctrl+Alt+R` (or `Cmd+Alt+R` on Mac) to send requests
- **History**: Access previous responses via the Response panel
- **Multiline Strings**: Use triple quotes for multiline body content
- **Comments**: Prefix with `//` or `#` for single-line comments
- **Request Separation**: Use `###` to separate requests
- **Testing**: Chain requests by extracting values from previous responses

## Response Handling

REST Client shows responses in a side panel with:

- **Status Code** and **Status Message**
- **Response Headers**
- **Response Body** (formatted JSON, XML, HTML, etc.)
- **Response Time** and **Size**

## Troubleshooting

### SSL Certificate Issues

For local development with self-signed certificates:

```json
"rest-client.excludeHostsForProxy": [
  "localhost",
  "127.0.0.1"
],
"http.proxyStrictSSL": false
```

### Port Not Accessible

Verify the API server is running:

```bash
curl https://localhost:8080/api/health
```

### Timeout Issues

Extend timeout in settings:

```json
"rest-client.requestTimeout": 30000
```

## Resources

- [REST Client GitHub](https://github.com/Huachao/vscode-restclient)
- [REST Client Documentation](https://github.com/Huachao/vscode-restclient/blob/master/README.md)
- [VS Code Extension Marketplace](https://marketplace.visualstudio.com/items?itemName=humao.rest-client)

## Related Tools

- **Postman**: Desktop app for API testing (alternative)
- **Bruno**: Lightweight API client (alternative)
- **Thunder Client**: VS Code extension alternative
- **REST Client**: This tool - lightweight, file-based, version control friendly
