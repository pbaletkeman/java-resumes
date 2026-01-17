import apiClient from './api';
import { API_ENDPOINTS } from '../utils/constants';
import { downloadFile } from '../utils/helpers';

export interface FileMetadata {
  id?: string;
  name: string;
  type?: string;
  uploadedDate?: string;
  size?: number;
}

export interface ProcessingResult {
  optimizedResume?: string;
  optimizedCoverLetter?: string;
  suggestions?: string[];
  matchPercentage?: number;
}

export const fileService = {
  async getFiles(): Promise<FileMetadata[]> {
    const response = await apiClient.get<{ files: FileMetadata[] }>(API_ENDPOINTS.FILES);
    return response.data.files || [];
  },

  async downloadFile(filename: string): Promise<void> {
    const response = await apiClient.get(`${API_ENDPOINTS.FILES}/${filename}`, {
      responseType: 'blob',
    });
    const contentDisposition = response.headers['content-disposition'];
    const extractedFilename = contentDisposition
      ? contentDisposition.split('filename=')[1]?.replace(/"/g, '')
      : filename;
    downloadFile(response.data, extractedFilename || filename);
  },

  async deleteFile(filename: string): Promise<{ success: boolean; message: string }> {
    const response = await apiClient.delete(`${API_ENDPOINTS.FILES}/${filename}`);
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
  }): Promise<ProcessingResult> {
    const response = await apiClient.post<ProcessingResult>(API_ENDPOINTS.PROCESS_RESUME, data);
    return response.data;
  },

  async processCoverLetter(data: {
    jobDescription: string;
    resume: string;
  }): Promise<ProcessingResult> {
    const response = await apiClient.post<ProcessingResult>(
      API_ENDPOINTS.PROCESS_COVER_LETTER,
      data
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
