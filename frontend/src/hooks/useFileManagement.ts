import { useState, useCallback, useEffect, useRef } from 'react';
import type { FileMetadata } from '../services/fileService';
import { fileService } from '../services/fileService';
import { useAppContext } from '../context/AppContext';

export const useFileManagement = () => {
  const [files, setFiles] = useState<FileMetadata[]>([]);
  const [loading, setLoading] = useState(false);
  const [previousFileCount, setPreviousFileCount] = useState(0);
  const [newFilesAlert, setNewFilesAlert] = useState(false);
  const { showSuccess, showError } = useAppContext();
  const intervalRef = useRef<ReturnType<typeof setInterval> | null>(null);

  const fetchFiles = useCallback(async () => {
    setLoading(true);
    try {
      const fetchedFiles = await fileService.getFiles();
      const newCount = fetchedFiles.length;

      // Check if new files were added
      if (previousFileCount > 0 && newCount > previousFileCount) {
        setNewFilesAlert(true);
        setTimeout(() => setNewFilesAlert(false), 5000); // Auto-hide alert after 5 seconds
        showSuccess(`${newCount - previousFileCount} new file(s) detected!`);
      }

      setPreviousFileCount(newCount);
      setFiles(fetchedFiles);
    } catch (err: unknown) {
      const error = err as { message?: string };
      showError(error?.message || 'Failed to fetch files');
    } finally {
      setLoading(false);
    }
  }, [previousFileCount, showSuccess, showError]);

  // Auto-refresh every 60 seconds
  useEffect(() => {
    // Initial fetch
    fetchFiles();

    // Set up interval for auto-refresh
    intervalRef.current = setInterval(() => {
      fetchFiles();
    }, 60000); // 60 seconds

    // Cleanup on unmount
    return () => {
      if (intervalRef.current) {
        clearInterval(intervalRef.current);
      }
    };
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
    fileCount: files.length,
    newFilesAlert,
  };
};
