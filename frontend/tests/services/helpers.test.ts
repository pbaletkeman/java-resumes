import { describe, it, expect } from 'vitest';
import { formatFileSize, formatDate } from '../../src/utils/helpers';

describe('formatFileSize', () => {
  it('formats bytes correctly', () => {
    expect(formatFileSize(0)).toBe('0 Bytes');
    expect(formatFileSize(1024)).toBe('1 KB');
    expect(formatFileSize(1048576)).toBe('1 MB');
  });
});

describe('formatDate', () => {
  it('formats date correctly', () => {
    const date = '2024-01-16T10:30:00Z';
    const formatted = formatDate(date);
    expect(formatted).toContain('Jan');
    expect(formatted).toContain('16');
  });
});
