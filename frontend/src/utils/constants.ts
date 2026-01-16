export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

export const API_ENDPOINTS = {
  FILES: '/api/files',
  UPLOAD: '/api/upload',
  PROCESS_RESUME: '/api/process/resume',
  PROCESS_COVER_LETTER: '/api/process/cover-letter',
  MARKDOWN_TO_PDF: '/api/markdownFile2PDF',
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
