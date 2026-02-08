import { describe, it, expect, vi } from 'vitest';
import { formatFileSize, formatDate, downloadFile, debounce } from '../../utils/helpers';

describe('helpers', () => {
  describe('formatFileSize', () => {
    it('should format 0 bytes', () => {
      expect(formatFileSize(0)).toBe('0 Bytes');
    });

    it('should format bytes', () => {
      expect(formatFileSize(500)).toBe('500 Bytes');
      expect(formatFileSize(1023)).toBe('1023 Bytes');
    });

    it('should format kilobytes', () => {
      expect(formatFileSize(1024)).toBe('1 KB');
      expect(formatFileSize(2048)).toBe('2 KB');
      expect(formatFileSize(1536)).toBe('1.5 KB');
    });

    it('should format megabytes', () => {
      expect(formatFileSize(1048576)).toBe('1 MB');
      expect(formatFileSize(2097152)).toBe('2 MB');
      expect(formatFileSize(1572864)).toBe('1.5 MB');
    });

    it('should format gigabytes', () => {
      expect(formatFileSize(1073741824)).toBe('1 GB');
      expect(formatFileSize(2147483648)).toBe('2 GB');
    });

    it('should handle fractional values', () => {
      expect(formatFileSize(1536)).toBe('1.5 KB');
      expect(formatFileSize(1638400)).toBe('1.56 MB');
    });
  });

  describe('formatDate', () => {
    it('should format a valid date string', () => {
      const result = formatDate('2024-01-15T10:30:00');
      expect(result).toContain('Jan');
      expect(result).toContain('15');
      expect(result).toContain('2024');
    });

    it('should format ISO date string', () => {
      const result = formatDate('2024-12-25T00:00:00Z');
      expect(result).toContain('Dec');
      expect(result).toContain('25');
      expect(result).toContain('2024');
    });

    it('should handle different date formats', () => {
      const result = formatDate('2024-06-01');
      expect(result).toContain('Jun');
      expect(result).toContain('1');
      expect(result).toContain('2024');
    });

    it('should format with time', () => {
      const result = formatDate('2024-01-15T14:30:00');
      expect(result).toMatch(/\d{1,2}:\d{2}/);
    });
  });

  describe('downloadFile', () => {
    it('should create and click a download link', () => {
      const createObjectURLSpy = vi.spyOn(window.URL, 'createObjectURL');
      const revokeObjectURLSpy = vi.spyOn(window.URL, 'revokeObjectURL');
      const createElementSpy = vi.spyOn(document, 'createElement');
      
      const mockUrl = 'blob:mock-url';
      createObjectURLSpy.mockReturnValue(mockUrl);
      revokeObjectURLSpy.mockImplementation(() => {});
      
      const mockLink = {
        href: '',
        download: '',
        click: vi.fn(),
      } as unknown as HTMLAnchorElement;
      
      createElementSpy.mockReturnValue(mockLink);
      vi.spyOn(document.body, 'appendChild').mockImplementation(() => mockLink);
      vi.spyOn(document.body, 'removeChild').mockImplementation(() => mockLink);

      const mockBlob = new Blob(['test content'], { type: 'text/plain' });
      downloadFile(mockBlob, 'test-file.txt');

      expect(createObjectURLSpy).toHaveBeenCalledWith(mockBlob);
      expect(mockLink.href).toBe(mockUrl);
      expect(mockLink.download).toBe('test-file.txt');
      expect(mockLink.click).toHaveBeenCalled();
      expect(revokeObjectURLSpy).toHaveBeenCalledWith(mockUrl);

      createObjectURLSpy.mockRestore();
      revokeObjectURLSpy.mockRestore();
    });
  });

  describe('debounce', () => {
    it('should delay function execution', async () => {
      vi.useFakeTimers();
      const mockFn = vi.fn();
      const debouncedFn = debounce(mockFn, 100);

      debouncedFn('test');
      expect(mockFn).not.toHaveBeenCalled();

      vi.advanceTimersByTime(100);
      expect(mockFn).toHaveBeenCalledWith('test');

      vi.useRealTimers();
    });

    it('should cancel previous calls', async () => {
      vi.useFakeTimers();
      const mockFn = vi.fn();
      const debouncedFn = debounce(mockFn, 100);

      debouncedFn('first');
      vi.advanceTimersByTime(50);
      debouncedFn('second');
      vi.advanceTimersByTime(50);
      debouncedFn('third');
      
      vi.advanceTimersByTime(100);

      expect(mockFn).toHaveBeenCalledTimes(1);
      expect(mockFn).toHaveBeenCalledWith('third');

      vi.useRealTimers();
    });

    it('should handle multiple arguments', async () => {
      vi.useFakeTimers();
      const mockFn = vi.fn();
      const debouncedFn = debounce(mockFn, 100);

      debouncedFn('arg1', 'arg2', 'arg3');
      vi.advanceTimersByTime(100);

      expect(mockFn).toHaveBeenCalledWith('arg1', 'arg2', 'arg3');

      vi.useRealTimers();
    });
  });
});
