# Java Resumes

AI-powered resume and cover letter optimization system using Large Language Models.

## 📚 Documentation

| Document                                               | Purpose                                                        |
| ------------------------------------------------------ | -------------------------------------------------------------- |
| **[copilot-instructions.md](copilot-instructions.md)** | Developer guide with coding standards and practices            |
| **[docs/README.md](docs/README.md)**                   | Comprehensive documentation with full features and API details |
| **[docs/Architecture.md](docs/Architecture.md)**       | System architecture with Mermaid diagrams and design patterns  |

## Problem Statement

Every job posting requires a uniquely tailored resume and cover letter, which can take an hour or more to create manually:

- **Miss Details**: May overlook important requirements
- **Include Irrelevant Content**: Accidentally include experience that turns off employers
- **Omit Skills**: Forget to include vital skills employers are seeking
- **No Insight**: Lack guidance on certifications or skills to learn

## Solution

Java Resumes leverages Large Language Models (LLMs) to:

1. **Accept Input**: Job description and current resume
2. **Process with AI**: Sends content to LLM for intelligent analysis
3. **Generate Output**: Creates tailored resume and/or cover letter
4. **Export Formats**: Provides both Markdown and PDF versions
5. **Identify Gaps**: Suggests certifications and skills to acquire

## 🛠️ Technology Stack

- **Language**: Java 25 LTS
- **Framework**: Spring Boot 3.5+
- **Build Tool**: Gradle
- **Frontend**: React + TypeScript + PrimeReact
- **LLM Service**: OpenAI-compatible (Ollama, LM Studio, OpenAI API)

## 🚀 Quick Start

### Prerequisites

- Java 25 LTS
- Gradle
- LLM Service (Ollama or LM Studio)

### Setup

1. **Clone & Navigate**:

   ```bash
   git clone https://github.com/pbaletkeman/java-resumes.git
   cd java-resumes
   ```

2. **Start LLM Service** (choose one):

   ```bash
   # Ollama
   ollama serve
   ollama pull gemma-3-4b-it

   # Or: LM Studio (download and run, then load a model)
   ```

3. **Configure** `config.json`:

   ```json
   {
     "endpoint": "http://localhost:11434",
     "apikey": "not-needed-for-local",
     "model": "gemma-3-4b-it"
   }
   ```

4. **Build & Run**:

   ```bash
   ./gradlew build
   ./gradlew bootRun
   ```

5. **Access**:
   - Web UI: http://localhost:8080/spotlight/index.html
   - API Docs: http://localhost:8080/swagger-ui/index.html

## ✨ Features

- ✅ Resume optimization with AI
- ✅ Cover letter generation
- ✅ Markdown and PDF export
- ✅ ATS optimization
- ✅ Skills gap analysis
- ✅ Markdown to PDF conversion
- ✅ Comprehensive REST API
- ✅ Full unit test coverage

## 🎯 Key Endpoints

| Method     | Endpoint            | Purpose                      |
| ---------- | ------------------- | ---------------------------- |
| **POST**   | `/upload`           | Optimize resume/cover letter |
| **POST**   | `/markdownFile2PDF` | Convert Markdown to PDF      |
| **GET**    | `/files`            | List files                   |
| **GET**    | `/files/{filename}` | Download file                |
| **DELETE** | `/files/{filename}` | Delete file                  |

See [docs/README.md](docs/README.md#api-endpoints) for full API reference.

## 📂 Project Structure

```shell
java-resumes/
├── src/main/java/ca/letkeman/resumes/
│   ├── controller/              # REST endpoints
│   ├── service/                 # File operations
│   ├── model/                   # Data models
│   ├── optimizer/               # LLM integration
│   └── ...
├── src/test/java/               # Unit tests
├── docs/
│   ├── README.md                # Detailed documentation
│   └── Architecture.md          # Architecture diagrams
├── config/checkstyle/           # Code quality rules
├── copilot-instructions.md      # Developer guide
└── config.json                  # LLM configuration
```

## 🧪 Testing

**Comprehensive test coverage**:

- AdvancedControllerTest: REST endpoint validation
- OptimizeTest: Model validation
- ApiServiceTest: LLM integration

**Run tests**:

```bash
./gradlew test                          # All tests
./gradlew test --tests AdvancedControllerTest  # Specific test
```

## 🔍 Code Quality

**Checkstyle** (Checkstyle 10.14.2):

```bash
./gradlew checkstyleMain       # Check code
./gradlew checkstyleTest       # Check tests
./gradlew check                # Full check
```

## 🖼️ Screenshots

### Web Interface (Spotlight)

![Spotlight UI](https://github.com/user-attachments/assets/d6fb204f-8ac4-446e-b853-8a5d8e75d02e)

### API Documentation (Swagger)

![Swagger UI](https://github.com/user-attachments/assets/930b18cd-d0e8-4088-8f54-dafb1792e523)
