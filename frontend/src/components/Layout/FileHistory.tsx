import React, { useState } from 'react';
import { Card } from 'primereact/card';
import { DataView } from 'primereact/dataview';
import { Button } from 'primereact/button';
import { Skeleton } from 'primereact/skeleton';
import { useFileManagement } from '../../hooks/useFileManagement';
import { ConfirmDialog } from '../Common/ConfirmDialog';
import { formatFileSize, formatDate } from '../../utils/helpers';

export const FileHistory: React.FC = () => {
  const { files, loading, downloadFile, deleteFile } = useFileManagement();
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

  const itemTemplate = (file: FileMetadata) => {
    return (
      <div className="col-12">
        <div className="flex flex-column xl:flex-row xl:align-items-start p-3 gap-3 border-bottom-1 surface-border">
          <i className="pi pi-file text-2xl" />
          <div className="flex flex-column sm:flex-row justify-content-between align-items-center xl:align-items-start flex-1 gap-3">
            <div className="flex flex-column align-items-center sm:align-items-start gap-2">
              <div className="text-lg font-bold">{file.name}</div>
              <div className="text-sm text-gray-500">
                {file.uploadedDate && formatDate(file.uploadedDate)}
                {file.size && ` • ${formatFileSize(file.size)}`}
              </div>
            </div>
            <div className="flex gap-2">
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
      </div>
    );
  };

  const listTemplate = (items: FileMetadata[]) => {
    if (!items || items.length === 0) return null;
    return <div className="grid grid-nogutter">{items.map(item => itemTemplate(item))}</div>;
  };

  return (
    <>
      <Card title="File History" className="h-full">
        {loading ? (
          <div className="flex flex-column gap-3">
            {[1, 2, 3].map(i => (
              <Skeleton key={i} height="4rem" />
            ))}
          </div>
        ) : files.length === 0 ? (
          <div className="text-center p-4 text-gray-500">No files uploaded yet</div>
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
