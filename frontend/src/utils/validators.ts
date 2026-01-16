import { FILE_SIZE_LIMIT } from './constants';

export const validateFile = (
  file: File,
  allowedTypes: readonly string[]
): { valid: boolean; error?: string } => {
  if (!file) {
    return { valid: false, error: 'No file provided' };
  }

  if (file.size > FILE_SIZE_LIMIT) {
    return {
      valid: false,
      error: `File size exceeds ${FILE_SIZE_LIMIT / 1024 / 1024}MB limit`,
    };
  }

  const fileExtension = '.' + file.name.split('.').pop()?.toLowerCase();
  if (!allowedTypes.includes(fileExtension)) {
    return {
      valid: false,
      error: `File type not allowed. Accepted types: ${allowedTypes.join(', ')}`,
    };
  }

  return { valid: true };
};

export const validateTextInput = (text: string, fieldName: string): string | null => {
  if (!text || text.trim().length === 0) {
    return `${fieldName} is required`;
  }

  if (text.trim().length < 10) {
    return `${fieldName} must be at least 10 characters`;
  }

  return null;
};

export const sanitizeFilename = (filename: string): string => {
  return filename.replace(/[^a-zA-Z0-9.-]/g, '_');
};
