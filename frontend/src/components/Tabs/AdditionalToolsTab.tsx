import React from 'react';
import { MarkdownToPdfForm } from '../Forms/MarkdownToPdfForm';
import { MarkdownToDocxForm } from '../Forms/MarkdownToDocxForm';

export const AdditionalToolsTab: React.FC = () => {
  return (
    <div className="p-3">
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <MarkdownToPdfForm />
        <MarkdownToDocxForm />
      </div>
    </div>
  );
};
