import { describe, it, expect } from 'vitest';
import { validateFile, validateTextInput, sanitizeFilename } from '../../utils/validators';
import { FILE_SIZE_LIMIT } from '../../utils/constants';

describe('validators', () => {
  describe('validateFile', () => {
    it('should validate a correct file', () => {
      const file = new File(['content'], 'test.pdf', { type: 'application/pdf' });
      const result = validateFile(file, ['.pdf']);
      
      expect(result.valid).toBe(true);
      expect(result.error).toBeUndefined();
    });

    it('should reject file exceeding size limit', () => {
      // Create a file larger than the limit
      const file = new File([new ArrayBuffer(FILE_SIZE_LIMIT + 1)], 'large.pdf', { type: 'application/pdf' });
      const result = validateFile(file, ['.pdf']);
      
      expect(result.valid).toBe(false);
      expect(result.error).toContain('File size exceeds');
    });

    it('should reject invalid file type', () => {
      const file = new File(['content'], 'test.exe', { type: 'application/x-msdownload' });
      const result = validateFile(file, ['.pdf', '.doc']);
      
      expect(result.valid).toBe(false);
      expect(result.error).toContain('File type not allowed');
    });

    it('should handle multiple allowed types', () => {
      const pdfFile = new File(['content'], 'test.pdf', { type: 'application/pdf' });
      const docFile = new File(['content'], 'test.doc', { type: 'application/msword' });
      const txtFile = new File(['content'], 'test.txt', { type: 'text/plain' });
      
      const allowedTypes = ['.pdf', '.doc', '.txt'];
      
      expect(validateFile(pdfFile, allowedTypes).valid).toBe(true);
      expect(validateFile(docFile, allowedTypes).valid).toBe(true);
      expect(validateFile(txtFile, allowedTypes).valid).toBe(true);
    });

    it('should handle uppercase extensions', () => {
      const file = new File(['content'], 'TEST.PDF', { type: 'application/pdf' });
      const result = validateFile(file, ['.pdf']);
      
      expect(result.valid).toBe(true);
    });

    it('should handle files without extension', () => {
      const file = new File(['content'], 'noextension', { type: 'text/plain' });
      const result = validateFile(file, ['.txt']);
      
      expect(result.valid).toBe(false);
    });

    it('should handle empty filename', () => {
      const file = new File(['content'], '', { type: 'text/plain' });
      const result = validateFile(file, ['.txt']);
      
      expect(result.valid).toBe(false);
    });
  });

  describe('validateTextInput', () => {
    it('should validate correct input', () => {
      const result = validateTextInput('This is a valid input string', 'Field Name');
      expect(result).toBeNull();
    });

    it('should reject empty input', () => {
      const result = validateTextInput('', 'Job Title');
      expect(result).toBe('Job Title is required');
    });

    it('should reject whitespace-only input', () => {
      const result = validateTextInput('   ', 'Company Name');
      expect(result).toBe('Company Name is required');
    });

    it('should reject input less than 10 characters', () => {
      const result = validateTextInput('short', 'Description');
      expect(result).toBe('Description must be at least 10 characters');
    });

    it('should accept input with exactly 10 characters', () => {
      const result = validateTextInput('1234567890', 'Field');
      expect(result).toBeNull();
    });

    it('should handle input with leading/trailing spaces', () => {
      const result = validateTextInput('  valid input here  ', 'Field');
      expect(result).toBeNull();
    });

    it('should reject input with only 9 characters after trim', () => {
      const result = validateTextInput('  123456789  ', 'Field');
      expect(result).toBe('Field must be at least 10 characters');
    });
  });

  describe('sanitizeFilename', () => {
    it('should keep valid characters', () => {
      const result = sanitizeFilename('valid-filename.pdf');
      expect(result).toBe('valid-filename.pdf');
    });

    it('should replace spaces with underscores', () => {
      const result = sanitizeFilename('my document file.pdf');
      expect(result).toBe('my_document_file.pdf');
    });

    it('should replace special characters', () => {
      const result = sanitizeFilename('file@name#with$special%chars.pdf');
      expect(result).toBe('file_name_with_special_chars.pdf');
    });

    it('should handle multiple consecutive special characters', () => {
      const result = sanitizeFilename('file!!!name###.pdf');
      expect(result).toBe('file___name___.pdf');
    });

    it('should preserve dots and hyphens', () => {
      const result = sanitizeFilename('file.name-v1.2.pdf');
      expect(result).toBe('file.name-v1.2.pdf');
    });

    it('should handle empty string', () => {
      const result = sanitizeFilename('');
      expect(result).toBe('');
    });

    it('should handle filename with only special characters', () => {
      const result = sanitizeFilename('@#$%^&*()');
      expect(result).toBe('_________'); // 9 underscores for 9 special chars
    });

    it('should handle unicode characters', () => {
      const result = sanitizeFilename('файл-документ.pdf');
      expect(result).toBe('____-________.pdf'); // Actual result from regex replacement
    });
  });
});
