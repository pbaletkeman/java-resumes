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

export const MarkdownToPdfForm: React.FC = () => {
  const [markdownFile, setMarkdownFile] = useState<File | null>(null);
  const [convertedPdf, setConvertedPdf] = useState<Blob | null>(null);
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
    setConvertedPdf(null);
  };

  const handleConvert = async () => {
    if (!markdownFile) {
      setError('Please select a markdown file');
      return;
    }

    try {
      const pdf = await convertApi.execute(() => fileService.convertMarkdownToPdf(markdownFile));
      setConvertedPdf(pdf);
      showSuccess('Markdown converted to PDF successfully');
    } catch (err: unknown) {
      showError((err as Error)?.message || 'Failed to convert markdown to PDF');
    }
  };

  const handleDownload = () => {
    if (convertedPdf && markdownFile) {
      const pdfFilename = markdownFile.name.replace(/\.md$/i, '.pdf');
      downloadFile(convertedPdf, pdfFilename);
      showSuccess('PDF downloaded successfully');
    }
  };

  const handleClear = () => {
    setMarkdownFile(null);
    setConvertedPdf(null);
    setError(null);
  };

  return (
    <Card title="Markdown to PDF Converter" className="h-full">
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
          <div
            className="mt-4 w-full p-4"
            style={{
              backgroundColor: 'var(--surface-section)',
              border: '1px solid var(--surface-border)',
              borderRadius: '6px',
            }}
          >
            <div style={{ display: 'flex', gap: '0.75rem' }}>
              <i
                className="pi pi-info-circle"
                style={{ color: 'var(--primary-color)', marginTop: '0.25rem', flexShrink: 0 }}
              />
              <div
                style={{
                  display: 'flex',
                  flexDirection: 'column',
                  gap: '0.5rem',
                  textAlign: 'left',
                }}
              >
                <p style={{ margin: 0, fontSize: '0.875rem', color: 'var(--text-color)' }}>
                  Note: LLM responses often include formatting markdown or extra content.
                </p>
                <p style={{ margin: 0, fontSize: '0.875rem', color: 'var(--text-color)' }}>
                  You may need to clean up your markdown file (remove headers like ### Suggestions,
                  extra markdown formatting, etc.) before converting to ensure professional-looking
                  output.
                </p>
                <p style={{ margin: 0, fontSize: '0.875rem', color: 'var(--text-color)' }}>
                  Review the markdown file before converting.
                </p>
              </div>
            </div>
          </div>

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
              disabled={!convertedPdf}
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

          {convertedPdf && (
            <Message
              severity="success"
              text="Conversion successful! Click Download to save the PDF."
              className="mt-4 w-full"
            />
          )}
        </div>

        {convertApi.loading && <LoadingSpinner message="Converting..." />}
      </div>
    </Card>
  );
};
