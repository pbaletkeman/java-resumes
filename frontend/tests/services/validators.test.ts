import { describe, it, expect } from 'vitest';
import { validateTextInput, sanitizeFilename } from '../../src/utils/validators';

describe('validateTextInput', () => {
  it('returns error for empty text', () => {
    const error = validateTextInput('', 'Field');
    expect(error).toBe('Field is required');
  });

  it('returns error for short text', () => {
    const error = validateTextInput('short', 'Field');
    expect(error).toBe('Field must be at least 10 characters');
  });

  it('returns null for valid text', () => {
    const error = validateTextInput('This is a valid text input', 'Field');
    expect(error).toBeNull();
  });
});

describe('sanitizeFilename', () => {
  it('sanitizes filename correctly', () => {
    expect(sanitizeFilename('file name!@#.pdf')).toBe('file_name___.pdf');
  });
});
