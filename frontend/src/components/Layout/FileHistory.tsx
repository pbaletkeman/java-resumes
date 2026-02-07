import React, { useState } from 'react';
import { Card } from 'primereact/card';
import { DataView } from 'primereact/dataview';
import { Button } from 'primereact/button';
import { Skeleton } from 'primereact/skeleton';
import { Message } from 'primereact/message';
import type { FileMetadata } from '../../services/fileService';
import { useFileManagement } from '../../hooks/useFileManagement';
import { ConfirmDialog } from '../Common/ConfirmDialog';
import { formatDate } from '../../utils/helpers';

export const FileHistory: React.FC = () => {
  const { files, loading, downloadFile, deleteFile, fetchFiles, fileCount, newFilesAlert } =
    useFileManagement();
  const [deleteDialogVisible, setDeleteDialogVisible] = useState(false);
  const [fileToDelete, setFileToDelete] = useState<string | null>(null);

  const handleDeleteClick = (filename: string) => {
    setFileToDelete(filename);
    setDeleteDialogVisible(true);
  };

  const handleConfirmDelete = () => {
    if (fileToDelete) {
      deleteFile(fileToDelete);
    }
    setDeleteDialogVisible(false);
    setFileToDelete(null);
  };

  const itemTemplate = (file: FileMetadata, index: number) => {
    const backgroundColor = index % 2 === 0 ? 'var(--highlight-bg)' : 'var(--surface-ground)';
    return (
      <div className="col-12">
        <div
          className="p-3 border-bottom-1 surface-border"
          style={{
            backgroundColor,
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'flex-start',
            gap: '1rem',
          }}
        >
          <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem', flex: 1 }}>
            {/* Line 1: File icon + name */}
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <i className="pi pi-file text-xl" />
              <span style={{ fontWeight: 600, fontSize: '1rem', color: 'var(--text-color)' }}>
                {file.name}
              </span>
            </div>
            {/* Line 2: Date */}
            <div style={{ fontSize: '0.875rem', color: '#6b7280', marginLeft: '1.75rem' }}>
              {file.uploadedDate && formatDate(file.uploadedDate)}
            </div>
          </div>
          <div style={{ display: 'flex', gap: '0.5rem' }}>
            <Button
              icon="pi pi-download"
              rounded
              outlined
              severity="success"
              onClick={() => downloadFile(file.name)}
              aria-label={`Download ${file.name}`}
              tooltip="Download"
              tooltipOptions={{ position: 'top' }}
            />
            <Button
              icon="pi pi-trash"
              rounded
              outlined
              severity="danger"
              onClick={() => handleDeleteClick(file.name)}
              aria-label={`Delete ${file.name}`}
              tooltip="Delete"
              tooltipOptions={{ position: 'top' }}
            />
          </div>
        </div>
      </div>
    );
  };

  const listTemplate = (items: FileMetadata[]) => {
    if (!items || items.length === 0) return null;
    return (
      <div className="grid grid-nogutter">
        {items.map((item, index) => (
          <React.Fragment key={item.name}>{itemTemplate(item, index)}</React.Fragment>
        ))}
      </div>
    );
  };

  return (
    <>
      <Card title={`File History (${fileCount} files)`} className="h-full">
        {newFilesAlert && (
          <Message
            severity="success"
            text="New files detected! List has been updated."
            className="mb-3 w-full"
          />
        )}
        <div className="flex gap-2 mb-4">
          <Button
            label="Refresh Now"
            icon="pi pi-refresh"
            onClick={fetchFiles}
            disabled={loading}
            severity="info"
            outlined
          />
          <div className="text-sm text-gray-500 flex align-items-center ml-2">
            Auto-refreshes every 60 seconds
          </div>
        </div>
        {loading ? (
          <div className="flex flex-column gap-3">
            {[1, 2, 3].map(i => (
              <Skeleton key={i} height="4rem" />
            ))}
          </div>
        ) : files.length === 0 ? (
          <div className="text-center p-4 text-gray-500">No Files Found</div>
        ) : (
          <DataView value={files} listTemplate={listTemplate} />
        )}
      </Card>
      <ConfirmDialog
        visible={deleteDialogVisible}
        onHide={() => setDeleteDialogVisible(false)}
        onConfirm={handleConfirmDelete}
        message={`Are you sure you want to delete "${fileToDelete}"?`}
        header="Confirm Delete"
      />
    </>
  );
};
