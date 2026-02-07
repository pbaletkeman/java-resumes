export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

export const API_ENDPOINTS = {
  FILES: '/api/files',
  UPLOAD: '/api/upload',
  PROCESS_RESUME: '/api/process/resume',
  PROCESS_COVER_LETTER: '/api/process/cover-letter',
  PROCESS_SKILLS: '/api/process/skills',
  GENERATE_INTERVIEW_HR: '/api/generate/interview-hr-questions',
  GENERATE_INTERVIEW_JOB_SPECIFIC: '/api/generate/interview-job-specific',
  GENERATE_INTERVIEW_REVERSE: '/api/generate/interview-reverse',
  GENERATE_COLD_EMAIL: '/api/generate/cold-email',
  GENERATE_COLD_LINKEDIN: '/api/generate/cold-linkedin-message',
  GENERATE_THANK_YOU_EMAIL: '/api/generate/thank-you-email',
  MARKDOWN_TO_PDF: '/api/markdownFile2PDF',
  MARKDOWN_TO_DOCX: '/api/markdownFile2DOCX',
  HEALTH: '/api/health',
} as const;

export const THEMES = {
  LIGHT: 'lara-light-blue',
  DARK: 'lara-dark-blue',
} as const;

export const THEME_STORAGE_KEY = 'app-theme';

export const INPUT_MODES = {
  PASTE: 'paste',
  UPLOAD: 'upload',
} as const;

export const FILE_SIZE_LIMIT = 50 * 1024 * 1024; // 50MB

export const ALLOWED_FILE_TYPES = {
  DOCUMENTS: ['.pdf', '.doc', '.docx', '.txt'],
  MARKDOWN: ['.md'],
} as const;

export const TOAST_LIFE = 3000;

export const API_TIMEOUT = 30000;
