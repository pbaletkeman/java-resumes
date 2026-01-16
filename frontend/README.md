# Resume Optimizer Frontend

A modern React application built with PrimeReact components for resume optimization and document processing.

## Technology Stack

- **Framework:** React 19.2.0
- **Build Tool:** Vite 7.2.4
- **Language:** TypeScript 5.9.3
- **UI Library:** PrimeReact 10.9.7
- **Styling:** Tailwind CSS 4.1.18
- **HTTP Client:** Axios 1.13.2
- **Testing:** Vitest 4.0.17 + React Testing Library

## Installation

```bash
npm install
cp .env.example .env
```

## Development

```bash
npm run dev
```

The application will be available at http://localhost:3000

## Testing

```bash
npm run test
npm run test:coverage
```

## Building

```bash
npm run build
```

## Features

- Tab-based UI with Main Content and Additional Tools
- Document upload (paste or file upload)
- Resume optimization and cover letter generation
- Markdown to PDF conversion
- File history with download/delete
- Light/dark theme toggle with localStorage persistence
- Responsive design (320px - 1920px)
- Toast notifications and error handling

For complete documentation, see the [PRD](../PRD-PRIMEREACT-DOCKER-v2.md).
