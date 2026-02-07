import React, { useState, useEffect } from 'react';
import { Card } from 'primereact/card';
import { Button } from 'primereact/button';
import { Dropdown } from 'primereact/dropdown';
import { ConfirmDialog, confirmDialog } from 'primereact/confirmdialog';
import { useApi } from '../../hooks/useApi';
import { useAppContext } from '../../context/AppContext';
import { fileService } from '../../services/fileService';
import { LoadingSpinner } from '../Common/LoadingSpinner';

export interface FileItem {
  name: string;
  url: string;
  date: string;
  size?: number;
}

type SortField = 'date' | 'name';
type SortOrder = 'asc' | 'desc';
type FilterPrefix = 'all' | 'cover' | 'resume' | 'skills';

export const FilesTab: React.FC = () => {
  const [files, setFiles] = useState<FileItem[]>([]);
  const [selectedFiles, setSelectedFiles] = useState<Set<string>>(new Set());
  const [sortField, setSortField] = useState<SortField>('date');
  const [sortOrder, setSortOrder] = useState<SortOrder>('desc');
  const [filterPrefix, setFilterPrefix] = useState<FilterPrefix>('all');
  const [isLoadingFiles, setIsLoadingFiles] = useState(true);

  const { showSuccess, showError } = useAppContext();
  const api = useApi();

  const loadFiles = async () => {
    try {
      setIsLoadingFiles(true);
      const fileList = await fileService.getFiles();
      setFiles(fileList);
      setSelectedFiles(new Set());
    } catch (err) {
      showError((err as Error)?.message || 'Failed to load files');
      setFiles([]);
    } finally {
      setIsLoadingFiles(false);
    }
  };

  useEffect(() => {
    loadFiles();
  }, []);

  const getFilePrefix = (filename: string): string => {
    const baseName = filename.toLowerCase();
    if (baseName.includes('cover')) return 'cover';
    if (baseName.includes('resume') || baseName.includes('resume-optimized')) return 'resume';
    if (baseName.includes('skills') || baseName.includes('development-plan')) return 'skills';
    return 'other';
  };

  const filteredFiles = files.filter(file => {
    if (filterPrefix === 'all') return true;
    return getFilePrefix(file.name) === filterPrefix;
  });

  const sortedFiles = [...filteredFiles].sort((a, b) => {
    let comparison = 0;

    if (sortField === 'date') {
      const dateA = new Date(a.date).getTime();
      const dateB = new Date(b.date).getTime();
      comparison = dateB - dateA; // Default descending for date
    } else {
      comparison = a.name.localeCompare(b.name);
    }

    return sortOrder === 'asc' ? -comparison : comparison;
  });

  const toggleFileSelection = (filename: string) => {
    const newSelected = new Set(selectedFiles);
    if (newSelected.has(filename)) {
      newSelected.delete(filename);
    } else {
      newSelected.add(filename);
    }
    setSelectedFiles(newSelected);
  };

  const toggleAllFiles = () => {
    if (selectedFiles.size === sortedFiles.length) {
      setSelectedFiles(new Set());
    } else {
      setSelectedFiles(new Set(sortedFiles.map(f => f.name)));
    }
  };

  const handleDeleteSelected = async () => {
    const selectedList = Array.from(selectedFiles);
    if (selectedList.length === 0) {
      showError('No files selected for deletion');
      return;
    }

    confirmDialog({
      message: `Delete ${selectedList.length} file(s)?`,
      header: 'Confirm Deletion',
      icon: 'pi pi-exclamation-triangle',
      accept: () => performDelete(selectedList),
      reject: () => {},
      contentClassName: 'p-16',
    });
  };

  const performDelete = async (filesToDelete: string[]) => {
    try {
      for (const filename of filesToDelete) {
        await api.execute(() => fileService.deleteFile(filename));
      }
      showSuccess(`Deleted ${filesToDelete.length} file(s)`);
      setSelectedFiles(new Set());
      await loadFiles();
    } catch (err) {
      showError((err as Error)?.message || 'Failed to delete files');
    }
  };

  const handleDownload = async (filename: string) => {
    try {
      await api.execute(() => fileService.downloadFile(filename));
      showSuccess(`Downloaded ${filename}`);
    } catch (err) {
      showError((err as Error)?.message || 'Failed to download file');
    }
  };

  const formatFileSize = (bytes?: number): string => {
    if (!bytes) return 'N/A';
    if (bytes < 1024) return `${bytes}B`;
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)}KB`;
    return `${(bytes / (1024 * 1024)).toFixed(1)}MB`;
  };

  const formatDate = (dateString: string): string => {
    try {
      return new Date(dateString).toLocaleString();
    } catch {
      return dateString;
    }
  };

  const filterOptions = [
    { label: 'All Files', value: 'all' },
    { label: 'Cover Letters', value: 'cover' },
    { label: 'Resumes', value: 'resume' },
    { label: 'Skills Plans', value: 'skills' },
  ];

  const sortFieldOptions = [
    { label: 'Date', value: 'date' },
    { label: 'File Name', value: 'name' },
  ];

  const sortOrderOptions = [
    { label: 'Ascending', value: 'asc' },
    { label: 'Descending', value: 'desc' },
  ];

  if (isLoadingFiles && files.length === 0) {
    return <LoadingSpinner message="Loading files..." />;
  }

  return (
    <Card className="h-full">
      <div className="flex flex-column gap-4 w-full">
        {/* Files List */}
        {sortedFiles.length === 0 ? (
          <div className="text-center py-6 text-gray-500">
            <i className="pi pi-inbox text-3xl mb-3 block" />
            <p>No files found</p>
          </div>
        ) : (
          <div className="border-1 border-gray-300 rounded overflow-hidden">
            {/* Filters and Actions Section - Inside Table */}
            <div
              className="bg-gray-50 border-b-1 border-gray-300"
              style={{ paddingLeft: '1rem', paddingTop: '1rem', paddingBottom: '1rem' }}
            >
              <div className="flex align-items-end justify-content-between flex-wrap">
                <div className="flex align-items-end">
                  {/* Filter by Type */}
                  <div className="flex flex-column gap-2" style={{ minWidth: '200px' }}>
                    <label className="font-semibold text-sm">Filter by Type{'\u00A0'}</label>
                    <Dropdown
                      value={filterPrefix}
                      onChange={e => setFilterPrefix(e.value)}
                      options={filterOptions}
                      optionLabel="label"
                      optionValue="value"
                      style={{ width: '200px' }}
                    />
                  </div>

                  <span style={{ marginRight: '2rem' }}>{'\u00A0\u00A0\u00A0\u00A0'}</span>

                  {/* Sort by File Name */}
                  <Button
                    label={sortField === 'name' ? 'Sorted by Name' : 'Sort by Name'}
                    icon={
                      sortField === 'name'
                        ? sortOrder === 'asc'
                          ? 'pi pi-arrow-up'
                          : 'pi pi-arrow-down'
                        : 'pi pi-sort-alt'
                    }
                    onClick={() => {
                      if (sortField === 'name') {
                        setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
                      } else {
                        setSortField('name');
                        setSortOrder('asc');
                      }
                    }}
                    severity={sortField === 'name' ? 'info' : 'secondary'}
                    size="small"
                  />

                  <span style={{ marginRight: '2rem' }}>{'\u00A0\u00A0\u00A0\u00A0'}</span>

                  {/* Sort by File Date */}
                  <Button
                    label={sortField === 'date' ? 'Sorted by Date' : 'Sort by Date'}
                    icon={
                      sortField === 'date'
                        ? sortOrder === 'asc'
                          ? 'pi pi-arrow-up'
                          : 'pi pi-arrow-down'
                        : 'pi pi-sort-alt'
                    }
                    onClick={() => {
                      if (sortField === 'date') {
                        setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
                      } else {
                        setSortField('date');
                        setSortOrder('desc');
                      }
                    }}
                    severity={sortField === 'date' ? 'info' : 'secondary'}
                    size="small"
                  />
                </div>

                {/* File Count and Action Buttons */}
                <div className="flex gap-2 align-items-center">
                  <span style={{ marginRight: '1rem' }}>{'\u00A0\u00A0'}</span>
                  <span className="text-sm text-gray-600">
                    Showing {sortedFiles.length} file(s)
                    {selectedFiles.size > 0 && ` • ${selectedFiles.size} selected`}
                  </span>
                  <span style={{ marginLeft: '1rem' }}>{'\u00A0\u00A0'}</span>
                  {sortedFiles.length > 0 && (
                    <div className="flex gap-2 align-items-center">
                      <Button
                        label="Select All"
                        icon="pi pi-check"
                        severity="secondary"
                        outlined
                        size="small"
                        onClick={toggleAllFiles}
                        disabled={api.loading}
                      />
                      <span>{'\u00A0\u00A0'}</span>
                      <Button
                        label={`Delete (${selectedFiles.size})`}
                        icon="pi pi-trash"
                        severity="danger"
                        size="small"
                        onClick={handleDeleteSelected}
                        disabled={selectedFiles.size === 0 || api.loading}
                      />
                    </div>
                  )}
                </div>
              </div>
            </div>
            {/* Header */}
            <div className="bg-gray-100 p-3 font-semibold text-sm grid grid-cols-12 gap-3 border-b-1 border-gray-300">
              <div className="col-span-1 flex align-items-center" style={{ paddingLeft: '1rem' }}>
                <input
                  type="checkbox"
                  checked={selectedFiles.size === sortedFiles.length && sortedFiles.length > 0}
                  onChange={toggleAllFiles}
                  className="w-1rem h-1rem"
                  aria-label="Select all files"
                  disabled={api.loading}
                  style={{ marginLeft: '0.5rem' }}
                />
              </div>
              <div className="col-span-5">File Name</div>
              <div className="col-span-2">Date Modified</div>
              <div className="col-span-2">Size</div>
              <div className="col-span-2">Actions</div>
            </div>

            {/* Rows with Zebra Striping */}
            {sortedFiles.map((file, index) => (
              <div
                key={file.name}
                style={{
                  backgroundColor: index % 2 === 0 ? '#ffffff' : '#f3f4f6',
                  padding: '0.75rem',
                  display: 'grid',
                  gridTemplateColumns: 'repeat(12, minmax(0, 1fr))',
                  gap: '0.75rem',
                  borderBottom: '1px solid #e5e7eb',
                  alignItems: 'center',
                  transition: 'background-color 0.2s',
                }}
                className="hover:bg-blue-50"
              >
                <div
                  style={{
                    gridColumn: 'span 1',
                    display: 'flex',
                    alignItems: 'center',
                    paddingLeft: '1rem',
                  }}
                >
                  <input
                    type="checkbox"
                    checked={selectedFiles.has(file.name)}
                    onChange={() => toggleFileSelection(file.name)}
                    className="w-1rem h-1rem"
                    aria-label={`Select ${file.name}`}
                    disabled={api.loading}
                    style={{ marginLeft: '0.5rem' }}
                  />
                </div>
                <div style={{ gridColumn: 'span 5' }}>
                  <div className="text-sm font-semibold text-gray-900 truncate" title={file.name}>
                    {file.name}
                  </div>
                  <div className="text-xs text-gray-500 mt-1">
                    <i
                      className={`pi pi-${getFilePrefix(file.name) === 'cover' ? 'file' : 'file-edit'} mr-2`}
                    />
                    {getFilePrefix(file.name).charAt(0).toUpperCase() +
                      getFilePrefix(file.name).slice(1)}
                  </div>
                </div>
                <div style={{ gridColumn: 'span 2', fontSize: '0.875rem', color: '#4b5563' }}>
                  {formatDate(file.date)}
                </div>
                <div style={{ gridColumn: 'span 2', fontSize: '0.875rem', color: '#4b5563' }}>
                  {formatFileSize(file.size)}
                </div>
                <div style={{ gridColumn: 'span 2', display: 'flex', gap: '0.5rem' }}>
                  <Button
                    icon="pi pi-download"
                    rounded
                    text
                    severity="info"
                    size="small"
                    onClick={() => handleDownload(file.name)}
                    disabled={api.loading}
                    tooltip="Download"
                  />
                  <Button
                    icon="pi pi-trash"
                    rounded
                    text
                    severity="danger"
                    size="small"
                    onClick={() => {
                      confirmDialog({
                        message: `Delete ${file.name}?`,
                        header: 'Confirm Deletion',
                        icon: 'pi pi-exclamation-triangle',
                        accept: () => performDelete([file.name]),
                        reject: () => {},
                        contentClassName: 'p-16 m-16',
                      });
                    }}
                    disabled={api.loading}
                    tooltip="Delete"
                  />
                </div>
              </div>
            ))}
          </div>
        )}

        <ConfirmDialog />
      </div>
    </Card>
  );
};
