import React from 'react';
import { DocumentUploadForm } from '../Forms/DocumentUploadForm';

export const MainContentTab: React.FC = () => {
  return (
    <div className="p-3">
      <DocumentUploadForm />
    </div>
  );
};
