import apiClient from './api';
import { API_ENDPOINTS } from '../utils/constants';
import { downloadFile } from '../utils/helpers';

export interface FileMetadata {
  name: string;
  uploadedDate?: string;
  size?: number;
  filename?: string;
  url?: string;
  date?: string;
}

export interface ProcessingResult {
  optimizedResume?: string;
  optimizedCoverLetter?: string;
  suggestions?: string[];
  matchPercentage?: number;
}

export const fileService = {
  async getFiles(): Promise<FileMetadata[]> {
    const response = await apiClient.get<
      Array<{ name: string; url: string; date: string; size?: string }>
    >(API_ENDPOINTS.FILES);
    if (Array.isArray(response.data)) {
      return response.data.map(file => ({
        name: file.name,
        uploadedDate: file.date,
        filename: file.name,
        url: file.url,
        date: file.date,
        size: file.size ? parseInt(file.size) : undefined,
      }));
    }
    return [];
  },

  async downloadFile(filename: string): Promise<void> {
    // URL encode the filename to handle special characters and spaces
    const encodedFilename = encodeURIComponent(filename);
    const response = await apiClient.get(`${API_ENDPOINTS.FILES}/${encodedFilename}`, {
      responseType: 'blob',
    });
    const contentDisposition = response.headers['content-disposition'];
    const extractedFilename = contentDisposition
      ? contentDisposition.split('filename=')[1]?.replace(/"/g, '')
      : filename;
    downloadFile(response.data, extractedFilename || filename);
  },

  async deleteFile(filename: string): Promise<{ success: boolean; message: string }> {
    // URL encode the filename to handle special characters and spaces
    const encodedFilename = encodeURIComponent(filename);
    const response = await apiClient.delete(`${API_ENDPOINTS.FILES}/${encodedFilename}`);
    return response.data;
  },

  async uploadDocuments(formData: FormData): Promise<{ uploadId: string }> {
    const response = await apiClient.post(API_ENDPOINTS.UPLOAD, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },

  async processResume(data: {
    jobDescription: string;
    resume: string;
    optimize?: string;
  }): Promise<ProcessingResult> {
    const formData = new FormData();

    // Create files from strings
    if (data.resume) {
      formData.append('resume', new File([data.resume], 'resume.txt', { type: 'text/plain' }));
    }

    if (data.jobDescription) {
      formData.append('job', new File([data.jobDescription], 'job.txt', { type: 'text/plain' }));
    }

    // Use provided optimize or create default
    const optimizeData =
      data.optimize ||
      JSON.stringify({
        resume: data.resume ? 'resume.txt' : '',
        jobDescription: data.jobDescription ? 'job.txt' : '',
        company: 'Company',
        jobTitle: 'Job Title',
        model: 'mistral:latest',
        temperature: 0.15,
        promptType: ['resume'],
      });

    formData.append('optimize', optimizeData);

    const response = await apiClient.post<ProcessingResult>(
      API_ENDPOINTS.PROCESS_RESUME,
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      }
    );
    return response.data;
  },

  async processCoverLetter(data: {
    jobDescription: string;
    resume: string;
    optimize?: string;
  }): Promise<ProcessingResult> {
    const formData = new FormData();

    // Create files from strings
    if (data.resume) {
      formData.append(
        'coverLetter',
        new File([data.resume], 'coverletter.txt', { type: 'text/plain' })
      );
    }

    if (data.jobDescription) {
      formData.append('job', new File([data.jobDescription], 'job.txt', { type: 'text/plain' }));
    }

    // Use provided optimize or create default
    const optimizeData =
      data.optimize ||
      JSON.stringify({
        resume: data.resume ? 'coverletter.txt' : '',
        jobDescription: data.jobDescription ? 'job.txt' : '',
        company: 'Company',
        jobTitle: 'Job Title',
        model: 'mistral:latest',
        temperature: 0.15,
        promptType: ['cover'],
      });

    formData.append('optimize', optimizeData);

    const response = await apiClient.post<ProcessingResult>(
      API_ENDPOINTS.PROCESS_COVER_LETTER,
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      }
    );
    return response.data;
  },

  async processSkills(data: {
    jobDescription: string;
    resume: string;
    optimize?: string;
  }): Promise<ProcessingResult> {
    const formData = new FormData();

    // Create files from strings
    if (data.resume) {
      formData.append('resume', new File([data.resume], 'resume.txt', { type: 'text/plain' }));
    }

    if (data.jobDescription) {
      formData.append('job', new File([data.jobDescription], 'job.txt', { type: 'text/plain' }));
    }

    // Use provided optimize or create default
    const optimizeData =
      data.optimize ||
      JSON.stringify({
        resume: data.resume ? 'resume.txt' : '',
        jobDescription: data.jobDescription ? 'job.txt' : '',
        company: 'Company',
        jobTitle: 'Job Title',
        model: 'mistral:latest',
        temperature: 0.15,
        promptType: ['skills'],
      });

    formData.append('optimize', optimizeData);

    const response = await apiClient.post<ProcessingResult>(
      API_ENDPOINTS.PROCESS_RESUME,
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      }
    );
    return response.data;
  },

  async convertMarkdownToPdf(file: File): Promise<Blob> {
    const formData = new FormData();
    formData.append('file', file);
    const response = await apiClient.post(API_ENDPOINTS.MARKDOWN_TO_PDF, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      responseType: 'blob',
    });
    return response.data;
  },

  async checkHealth(): Promise<{ status: string; timestamp: string }> {
    const response = await apiClient.get(API_ENDPOINTS.HEALTH);
    return response.data;
  },
};
