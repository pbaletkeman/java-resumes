# Backend Screenshots Guide

Documentation for capturing and maintaining backend API and system screenshots.

## 📸 Backend Screenshots Quick Reference

| Screenshot      | File                  | Size       | Usage             | Status    |
| --------------- | --------------------- | ---------- | ----------------- | --------- |
| Swagger UI      | `swagger-ui.png`      | 1200x800px | API documentation | 📌 Needed |
| API Endpoints   | `api-endpoints.png`   | 1000x600px | Endpoint examples | 📌 Needed |
| Error Responses | `error-responses.png` | 1000x600px | Error handling    | 📌 Needed |

---

## 1. Swagger UI Documentation

### What to Show

Interactive OpenAPI documentation interface for the REST API.

### URL

```plaintext
http://localhost:8080/swagger-ui/index.html
```

### Components to Include

```plaintext
┌─────────────────────────────────────────────────────────┐
│  Swagger UI - Java Resumes API                          │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  java-resumes API                                       │
│  AI-powered resume and cover letter optimization API    │
│                                                         │
│  [Authorize ▼]  [Servers: http://localhost:8080 ▼]      │
│                                                         │
│  ═════════════════════════════════════════════════════  │
│  POST /api/v1/upload                                    │
│  ───────────────────────────────────────────────────    │
│  Upload document and start optimization                 │
│  [Try it out ▼]                                         │
│                                                         │
│  GET /api/v1/files                                      │
│  ─────────────────────────────────────────────────      │
│  List all uploaded and generated files                  │
│  [Try it out ▼]                                         │
│                                                         │
│  GET /api/v1/files/{filename}                           │
│  ──────────────────────────────────────────────────     │
│  Download specific file                                 │
│  [Try it out ▼]                                         │
│                                                         │
│  DELETE /api/v1/files/{filename}                        │
│  ────────────────────────────────────────────────       │
│  Delete file                                            │
│  [Try it out ▼]                                         │
│                                                         │
│  POST /api/v1/markdownFile2PDF                          │
│  ─────────────────────────────────────────────────      │
│  Convert markdown document to PDF                       │
│  [Try it out ▼]                                         │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### Capture Instructions

1. **Start Backend Server**

   ```bash
   ./gradlew bootRun
   ```

   Wait for: `Tomcat started on port 8080`

2. **Open Swagger UI**
   - Navigate to: `http://localhost:8080/swagger-ui/index.html`
   - Wait for page to fully load

3. **Expand All Endpoints**
   - Click on each endpoint to expand details
   - Or use browser DevTools screenshot

4. **Optimal View**
   - Scroll to show multiple endpoints
   - Ensure API title and description visible
   - Show endpoint methods clearly

5. **Take Screenshot**
   - Use full-page screenshot tool
   - Capture showing 3-4 endpoints
   - Ensure all text is readable

6. **Save File**

   ```plaintext
   docs/screenshots/backend/swagger-ui.png
   ```

### Verification Checklist

- [ ] Swagger UI title visible
- [ ] API endpoints clearly listed
- [ ] HTTP methods visible (GET, POST, DELETE)
- [ ] Endpoint descriptions readable
- [ ] Try it out buttons visible
- [ ] No authentication dialogs blocking view
- [ ] File size < 400KB

---

## 2. API Endpoints Reference

### What to Show API

Detailed view of specific API endpoints with request/response examples.

### Endpoints to Include

#### POST /upload

```plaintext
POST /api/v1/upload HTTP/1.1
Host: localhost:8080
Content-Type: multipart/form-data

jobDescription: "We are seeking a Senior Java Developer..."
resume: "John Doe\nSenior Java Developer..."
promptType: ["Resume", "CoverLetter"]
temperature: 0.7
model: "mistral"

Response 200:
{
  "message": "Processing started",
  "jobId": "abc123def456",
  "status": "processing"
}
```

#### GET /files

```plaintext
GET /api/v1/files HTTP/1.1
Host: localhost:8080

Response 200:
{
  "files": [
    {
      "filename": "resume_optimized.pdf",
      "url": "/files/resume_optimized.pdf",
      "size": 245120,
      "type": "application/pdf",
      "uploadedAt": "2026-01-16T10:30:00Z"
    },
    {
      "filename": "resume_optimized.md",
      "url": "/files/resume_optimized.md",
      "size": 45230,
      "type": "text/markdown",
      "uploadedAt": "2026-01-16T10:30:00Z"
    }
  ]
}
```

#### DELETE /files/{filename}

```plaintext
DELETE /api/v1/files/resume_optimized.pdf HTTP/1.1
Host: localhost:8080

Response 200:
{
  "message": "File deleted successfully",
  "filename": "resume_optimized.pdf"
}
```

### Capture Instructions API

1. **Use API Testing Tool**
   - Postman (recommended)
   - Or Insomnia REST client
   - Or curl in terminal

2. **Example with Postman**
   - Create new POST request
   - URL: `http://localhost:8080/api/v1/upload`
   - Select Body → form-data
   - Add parameters (job description, resume, etc.)
   - Click Send
   - Capture request and response

3. **Example with curl (Terminal)**

   ```bash
   # List files
   curl -X GET http://localhost:8080/api/v1/files | json_pp

   # Upload document
   curl -X POST http://localhost:8080/api/v1/upload \
     -F "jobDescription=..." \
     -F "resume=@resume.txt" \
     -F "promptType=Resume" \
     -F "temperature=0.7" \
     -F "model=mistral"
   ```

4. **Capture Multiple Requests**
   - Show at least 3 different endpoints
   - Include request headers and body
   - Include response with data
   - Show response status code

5. **Take Screenshot**
   - Capture Postman window showing request/response
   - Or capture terminal output
   - Ensure all details visible

6. **Save File**

   ```plaintext
   docs/screenshots/backend/api-endpoints.png
   ```

### Verification Checklist APIs

- [ ] Request URL visible and correct
- [ ] HTTP method shown (GET, POST, DELETE)
- [ ] Request parameters/body shown
- [ ] Response status code visible (200, 400, 404, etc.)
- [ ] Response body JSON formatted
- [ ] All data is sample/test data (no sensitive info)
- [ ] Response headers visible (optional)
- [ ] File size < 300KB

---

## 3. Error Responses

### What to Show Errors

Common error scenarios and how the API handles them.

### Error Types to Include

#### 400 Bad Request

```plaintext
POST /api/v1/upload HTTP/1.1

// Missing required field: resume

Response 400:
{
  "timestamp": "2026-01-16T10:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Required property missing or invalid",
  "path": "/api/v1/upload"
}
```

#### 404 Not Found

```plaintext
GET /api/v1/files/nonexistent-file.pdf HTTP/1.1

Response 404:
{
  "timestamp": "2026-01-16T10:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "File not found: nonexistent-file.pdf",
  "path": "/api/v1/files/nonexistent-file.pdf"
}
```

#### 500 Internal Server Error

```plaintext
POST /api/v1/upload HTTP/1.1

// Server-side error during processing

Response 500:
{
  "timestamp": "2026-01-16T10:30:00.000+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An internal error occurred while processing the request",
  "path": "/api/v1/upload"
}
```

### Capture Instructions Details

1. **Prepare Error Scenarios**
   - Missing required fields
   - Request with invalid ID
   - Request with invalid data type
   - Server-side simulated error

2. **Using Postman**
   - Create requests for each error scenario
   - Configure request to trigger error
   - Send and observe response
   - Capture response window

3. **Take Multiple Screenshots**
   - Capture 3-4 different error responses
   - Show status codes (400, 404, 500)
   - Show error messages
   - Show response structure

4. **Alternative: Terminal View**

   ```bash
   # Trigger 400 error
   curl -X POST http://localhost:8080/api/v1/upload

   # Trigger 404 error
   curl -X GET http://localhost:8080/api/v1/files/nonexistent.pdf
   ```

5. **Combine in Single Screenshot**
   - If possible, show multiple errors
   - Or create collage of error responses
   - Clearly label each error type

6. **Save File**

   ```plaintext
   docs/screenshots/backend/error-responses.png
   ```

### Verification Checklist Error

- [ ] Error status codes visible (400, 404, 500)
- [ ] Error messages are helpful and readable
- [ ] Timestamp shown in response
- [ ] Response structure is consistent
- [ ] Multiple error types shown
- [ ] All data is clearly sample data
- [ ] No sensitive information exposed
- [ ] File size < 300KB

---

## 🔄 API Testing Workflow

### Prerequisites

1. Backend server running: `./gradlew bootRun`
2. Swagger UI accessible: `http://localhost:8080/swagger-ui/index.html`
3. API testing tool installed (Postman recommended)
4. Sample test data prepared

### Sample Test Data

#### Job Description Example

```plaintext
We are seeking an experienced Senior Java Developer to join our team.

Requirements:
- 7+ years of Java development experience
- Spring Boot and Spring Framework expertise
- RESTful API design and development
- Experience with relational databases (PostgreSQL, MySQL)
- Microservices architecture knowledge
- Continuous Integration/Continuous Deployment (CI/CD)
- Git version control
- Agile/Scrum methodology

Responsibilities:
- Design and develop scalable Java applications
- Collaborate with cross-functional teams
- Code review and mentoring junior developers
- Participate in architectural discussions
- Maintain code quality and best practices

Compensation: $120,000 - $160,000 annually
Benefits: Health insurance, 401k, Remote flexibility, Professional development
```

#### Resume Example

```plaintext
JOHN DOE
Senior Software Developer
john.doe@email.com | (555) 123-4567 | LinkedIn: linkedin.com/in/johndoe

PROFESSIONAL SUMMARY
Experienced software developer with 8+ years of expertise in Java, Spring Boot,
and cloud technologies. Proven track record of designing and implementing
scalable solutions for enterprise applications.

TECHNICAL SKILLS
Languages: Java, Python, JavaScript, SQL
Frameworks: Spring Boot, Spring Framework, Hibernate
Databases: PostgreSQL, MySQL, MongoDB, Redis
Tools: Maven, Gradle, Git, Docker, Kubernetes
Cloud: AWS (EC2, S3, Lambda), Google Cloud Platform

EXPERIENCE
Senior Developer | Tech Company Inc. | 2021-Present
- Led development of microservices architecture using Spring Boot
- Designed RESTful APIs serving 10M+ requests daily
- Mentored team of 4 junior developers
- Improved application performance by 40%

Full Stack Developer | Web Solutions LLC | 2018-2021
- Developed Java Spring web applications
- Implemented CI/CD pipelines using Jenkins
- Collaborated with product team on feature requirements
- Maintained database design and optimization

EDUCATION
Bachelor of Science in Computer Science
University Name | Graduation: 2016

CERTIFICATIONS
- Oracle Certified Associate Java Programmer
- AWS Certified Solutions Architect
```

---

## 🔍 API Documentation Reference

### Endpoints Summary

| Method | Endpoint                 | Purpose                           | Auth |
| ------ | ------------------------ | --------------------------------- | ---- |
| POST   | /api/v1/upload           | Submit documents for optimization | None |
| GET    | /api/v1/files            | List all files                    | None |
| GET    | /api/v1/files/{filename} | Download file                     | None |
| DELETE | /api/v1/files/{filename} | Delete file                       | None |
| POST   | /api/v1/markdownFile2PDF | Convert markdown to PDF           | None |

### Response Codes

| Code | Meaning      | Example Scenario        |
| ---- | ------------ | ----------------------- |
| 200  | OK           | Successful request      |
| 400  | Bad Request  | Missing required fields |
| 404  | Not Found    | File doesn't exist      |
| 500  | Server Error | Unexpected server error |

---

## 📋 Backend Screenshot Guidelines

### Documentation Should Include

- ✅ All REST endpoints
- ✅ Request/response examples
- ✅ Error handling examples
- ✅ Authentication/authorization (if applicable)
- ✅ Rate limiting info (if applicable)
- ✅ Performance metrics (optional)

### Data Sensitivity

- ❌ Never include real API keys
- ❌ Never include real authentication tokens
- ❌ Never include real user data
- ✅ Always use sample/test data
- ✅ Mask sensitive parts if needed

### Screenshot Quality

- ✅ Clear readability of all text
- ✅ Proper formatting/syntax highlighting
- ✅ Complete request/response shown
- ✅ Relevant context provided
- ✅ Professional appearance

---

## 🔧 Tools for Backend Testing

### Recommended Tools

1. **Postman** (Easy UI, collection management)
2. **Insomnia** (Clean interface, good for beginners)
3. **curl** (Command-line, scripts, automation)
4. **HTTPie** (User-friendly curl alternative)
5. **Thunder Client** (VS Code extension)

### Testing Commands

```bash
# List all files
curl -s http://localhost:8080/api/v1/files | jq '.'

# Upload document
curl -F "jobDescription=..." \
     -F "resume=..." \
     http://localhost:8080/api/v1/upload

# Download file
curl -O http://localhost:8080/api/v1/files/resume_optimized.pdf

# Delete file
curl -X DELETE http://localhost:8080/api/v1/files/resume_optimized.pdf
```

---

## 📚 References

- [Swagger UI Documentation](http://localhost:8080/swagger-ui/index.html)
- [Main API Documentation](../../docs/BACKEND_README.md)
- [Architecture Documentation](../architecture/DIAGRAMS.md)
- [Screenshots Guide](../SCREENSHOTS_GUIDE.md)

---

Last Updated: 2026-01-16
