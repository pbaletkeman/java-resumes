import { describe, it, expect, vi } from 'vitest';
import { API_BASE_URL, API_TIMEOUT } from '../../utils/constants';

// Store the interceptor functions
let responseSuccessHandler: any;
let responseErrorHandler: any;

// Mock axios before importing apiClient
vi.mock('axios', () => {
  return {
    default: {
      create: vi.fn(() => ({
        interceptors: {
          response: {
            use: vi.fn((successFn, errorFn) => {
              responseSuccessHandler = successFn;
              responseErrorHandler = errorFn;
            }),
          },
        },
      })),
    },
  };
});

describe('api service', () => {
  it('should create axios instance with correct config', async () => {
    const axios = await import('axios');
    await import('../../services/api');
    
    expect(axios.default.create).toHaveBeenCalledWith({
      baseURL: API_BASE_URL,
      timeout: API_TIMEOUT,
      headers: {
        'Content-Type': 'application/json',
      },
    });
  });

  it('should handle successful response in interceptor', async () => {
    await import('../../services/api');
    
    const mockResponse = { data: { success: true }, status: 200 };
    const result = responseSuccessHandler(mockResponse);
    expect(result).toBe(mockResponse);
  });

  it('should handle error with response data in interceptor', async () => {
    await import('../../services/api');
    
    const mockError = {
      response: {
        data: 'Server error message',
        status: 500,
      },
      message: 'Network error',
    };

    try {
      await responseErrorHandler(mockError);
      expect.fail('Should have thrown an error');
    } catch (error) {
      expect(error).toBe('Server error message');
    }
  });

  it('should handle error with message when no response data', async () => {
    await import('../../services/api');
    
    const mockError = {
      response: null,
      message: 'Network error',
    };

    try {
      await responseErrorHandler(mockError);
      expect.fail('Should have thrown an error');
    } catch (error) {
      expect(error).toBe('Network error');
    }
  });

  it('should handle error with default message when neither response data nor message', async () => {
    await import('../../services/api');
    
    const mockError = {
      response: null,
      message: '',
    };

    try {
      await responseErrorHandler(mockError);
      expect.fail('Should have thrown an error');
    } catch (error) {
      expect(error).toBe('An unexpected error occurred');
    }
  });

  it('should handle error response without data property', async () => {
    await import('../../services/api');
    
    const mockError = {
      response: { status: 404 },
      message: 'Not found',
    };

    try {
      await responseErrorHandler(mockError);
      expect.fail('Should have thrown an error');
    } catch (error) {
      expect(error).toBe('Not found');
    }
  });
});
