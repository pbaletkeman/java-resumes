import { useState, useCallback } from 'react';
import React from 'react';
import type { FileMetadata } from '../services/fileService';
import { fileService } from '../services/fileService';
import { useAppContext } from '../context/AppContext';

export const useFileManagement = () => {
  const [files, setFiles] = useState<FileMetadata[]>([]);
  const [loading, setLoading] = useState(false);
  const { showSuccess, showError } = useAppContext();

  const fetchFiles = useCallback(async () => {
    setLoading(true);
    try {
      const fetchedFiles = await fileService.getFiles();
      setFiles(fetchedFiles);
    } catch (err: unknown) {
      const error = err as { message?: string };
      showError(error?.message || 'Failed to fetch files');
    } finally {
      setLoading(false);
    }
  }, [showError]);

  React.useEffect(() => {
    fetchFiles();
  }, [fetchFiles]);

  const downloadFile = useCallback(
    async (filename: string) => {
      try {
        await fileService.downloadFile(filename);
        showSuccess(`File "${filename}" downloaded successfully`);
      } catch (err: unknown) {
        showError((err as Error)?.message || 'Failed to download file');
      }
    },
    [showSuccess, showError]
  );

  const deleteFile = useCallback(
    async (filename: string) => {
      try {
        await fileService.deleteFile(filename);
        showSuccess(`File "${filename}" deleted successfully`);
        await fetchFiles();
      } catch (err: unknown) {
        showError((err as Error)?.message || 'Failed to delete file');
      }
    },
    [showSuccess, showError, fetchFiles]
  );

  return {
    files,
    loading,
    fetchFiles,
    downloadFile,
    deleteFile,
  };
};
