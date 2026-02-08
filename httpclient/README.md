# HTTP Client Collection

This directory contains API request collections formatted for [VS Code REST Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client).

- [HTTP Client Collection](#http-client-collection)
  - [Files](#files)
    - [Resume.http](#resumehttp)
  - [Prerequisites](#prerequisites)
    - [Option 1: REST Client (VS Code Extension) - Lightweight \& Version Control Friendly](#option-1-rest-client-vs-code-extension---lightweight--version-control-friendly)
    - [Option 2: Bruno - Lightweight \& Modern Desktop App](#option-2-bruno---lightweight--modern-desktop-app)
  - [Usage](#usage)
    - [Using REST Client (VS Code Extension)](#using-rest-client-vs-code-extension)
    - [Using Bruno (Desktop App)](#using-bruno-desktop-app)
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

Choose one of two API testing tools:

### Option 1: REST Client (VS Code Extension) - Lightweight & Version Control Friendly

- **Marketplace**: [humao.rest-client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client)
- **VS Code Command**: `ext install humao.rest-client`
- **Pros**: Minimal setup, requests stored in `.http` files (version control friendly), fast
- **Cons**: VS Code only, basic features

### Option 2: Bruno - Lightweight & Modern Desktop App

- **Website**: [bruno.app](https://www.usebruno.com/)
- **Download**: Available for Windows, macOS, Linux
- **Pros**: Lightweight, modern interface, git-friendly, excellent multipart/form-data support
- **Cons**: Separate application (not VS Code extension)

**Recommendation**: Use **REST Client** for quick testing in VS Code, or **Bruno** for a dedicated API testing experience.

## Usage

### Using REST Client (VS Code Extension)

1. Open any `.http` file in VS Code
2. Hover over or click the **Send Request** link above any endpoint
3. View the response in the **Response** panel on the right side
4. Use the response output for debugging and testing

### Using Bruno (Desktop App)

1. Install and launch Bruno
2. Click **Import** → Select the `Resume.http` file
3. Bruno automatically parses the `.http` file format
4. Click the **Send** button next to any request
5. View the response in the right panel
6. Collections and responses are stored locally in `.bru` format (version control friendly)

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

| Tool               | Type              | Best For                               | Version Control    |
| ------------------ | ----------------- | -------------------------------------- | ------------------ |
| **REST Client**    | VS Code Extension | Quick testing in IDE, lightweight      | ✅ `.http` files   |
| **Bruno**          | Desktop App       | Dedicated API testing, modern UI       | ✅ `.bru` files    |
| **Postman**        | Desktop App       | Enterprise teams, advanced features    | ❌ Cloud-dependent |
| **Thunder Client** | VS Code Extension | Alternative to REST Client             | ✅ `.toml` files   |
| **Swagger UI**     | Web Interface     | Interactive API docs at `/swagger-ui/` | N/A                |
