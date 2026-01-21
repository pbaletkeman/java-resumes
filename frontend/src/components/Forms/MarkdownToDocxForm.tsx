import React, { useState } from 'react';
import { Card } from 'primereact/card';
import { Button } from 'primereact/button';
import { FileUpload } from 'primereact/fileupload';
import { Message } from 'primereact/message';
import { useApi } from '../../hooks/useApi';
import { useAppContext } from '../../context/AppContext';
import { fileService } from '../../services/fileService';
import { validateFile } from '../../utils/validators';
import { ALLOWED_FILE_TYPES } from '../../utils/constants';
import { downloadFile } from '../../utils/helpers';
import { LoadingSpinner } from '../Common/LoadingSpinner';

export const MarkdownToDocxForm: React.FC = () => {
  const [markdownFile, setMarkdownFile] = useState<File | null>(null);
  const [convertedDocx, setConvertedDocx] = useState<Blob | null>(null);
  const [error, setError] = useState<string | null>(null);

  const { showSuccess, showError } = useAppContext();
  const convertApi = useApi<Blob>();

  const handleFileSelect = (e: { files: File[] }) => {
    const file = e.files[0];
    if (!file) return;

    const validation = validateFile(file, ALLOWED_FILE_TYPES.MARKDOWN);
    if (!validation.valid) {
      setError(validation.error || 'Invalid file');
      setMarkdownFile(null);
      return;
    }

    setMarkdownFile(file);
    setError(null);
    setConvertedDocx(null);
  };

  const handleConvert = async () => {
    if (!markdownFile) {
      setError('Please select a markdown file');
      return;
    }

    try {
      const docx = await convertApi.execute(() => fileService.convertMarkdownToDocx(markdownFile));
      setConvertedDocx(docx);
      showSuccess('Markdown converted to DOCX successfully');
    } catch (err: unknown) {
      showError((err as Error)?.message || 'Failed to convert markdown to DOCX');
    }
  };

  const handleDownload = () => {
    if (convertedDocx && markdownFile) {
      const docxFilename = markdownFile.name.replace(/\.md$/i, '.docx');
      downloadFile(convertedDocx, docxFilename);
      showSuccess('DOCX downloaded successfully');
    }
  };

  const handleClear = () => {
    setMarkdownFile(null);
    setConvertedDocx(null);
    setError(null);
  };

  return (
    <Card title="Markdown to DOCX Converter" className="h-full">
      <div className="flex flex-column gap-6 p-4">
        <div className="field">
          <label className="font-bold block mb-3">Select Markdown File</label>
          <FileUpload
            mode="basic"
            name="markdownFile"
            accept=".md"
            maxFileSize={50000000}
            onSelect={handleFileSelect}
            onClear={handleClear}
            chooseLabel="Choose Markdown File"
            className="w-full"
          />
          {error && <Message severity="error" text={error} className="mt-3 w-full" />}
          {markdownFile && (
            <Message
              severity="info"
              text={`Selected: ${markdownFile.name}`}
              className="mt-3 w-full"
            />
          )}

          {/* Information message about extra markdown */}
          <Message
            severity="info"
            icon="pi pi-info-circle"
            text="Note: LLM responses often include formatting markdown or extra content. You may need to clean up your markdown file (remove headers like ### Suggestions, extra markdown formatting, etc.) before converting to ensure professional-looking output. Review the markdown file before converting."
            className="mt-4 w-full"
            style={{ textAlign: 'left' }}
          />

          {/* Action buttons below file chooser */}
          <div className="flex flex-wrap gap-4 mt-5" style={{ padding: '0.75rem' }}>
            <Button
              label="Convert"
              icon="pi pi-refresh"
              onClick={handleConvert}
              disabled={!markdownFile || convertApi.loading}
              style={{ padding: '0.75rem 1.5rem', marginRight: '1.5rem' }}
            />
            <Button
              label="Download"
              icon="pi pi-download"
              onClick={handleDownload}
              disabled={!convertedDocx}
              severity="success"
              style={{ padding: '0.75rem 1.5rem', marginRight: '1.5rem' }}
            />
            {markdownFile && (
              <Button
                label="Clear"
                icon="pi pi-times"
                onClick={handleClear}
                disabled={convertApi.loading}
                severity="secondary"
                outlined
                style={{ padding: '0.75rem 1.5rem' }}
              />
            )}
          </div>

          {convertedDocx && (
            <Message
              severity="success"
              text="Conversion successful! Click Download to save the DOCX file."
              className="mt-4 w-full"
            />
          )}
        </div>

        {convertApi.loading && <LoadingSpinner message="Converting..." />}
      </div>
    </Card>
  );
};
