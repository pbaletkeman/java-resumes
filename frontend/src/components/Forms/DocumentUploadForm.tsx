import React, { useState } from 'react';
import { Card } from 'primereact/card';
import { Button } from 'primereact/button';
import { InputTextarea } from 'primereact/inputtextarea';
import { FileUpload } from 'primereact/fileupload';
import { SelectButton } from 'primereact/selectbutton';
import { useApi } from '../../hooks/useApi';
import { useAppContext } from '../../context/AppContext';
import { fileService } from '../../services/fileService';
import { validateTextInput, validateFile } from '../../utils/validators';
import { INPUT_MODES, ALLOWED_FILE_TYPES } from '../../utils/constants';
import { LoadingSpinner } from '../Common/LoadingSpinner';

export const DocumentUploadForm: React.FC = () => {
  const [inputMode, setInputMode] = useState(INPUT_MODES.PASTE);
  const [jobDescription, setJobDescription] = useState('');
  const [resume, setResume] = useState('');
  const [jobFile, setJobFile] = useState<File | null>(null);
  const [resumeFile, setResumeFile] = useState<File | null>(null);
  const [errors, setErrors] = useState<{ job?: string; resume?: string }>({});

  const { showSuccess, showError } = useAppContext();
  const uploadApi = useApi();
  const resumeApi = useApi();
  const coverLetterApi = useApi();

  const modes = [
    { label: 'Paste Text', value: INPUT_MODES.PASTE },
    { label: 'Upload Files', value: INPUT_MODES.UPLOAD },
  ];

  const validateForm = (): boolean => {
    const newErrors: { job?: string; resume?: string } = {};

    if (inputMode === INPUT_MODES.PASTE) {
      const jobError = validateTextInput(jobDescription, 'Job Description');
      const resumeError = validateTextInput(resume, 'Resume');
      if (jobError) newErrors.job = jobError;
      if (resumeError) newErrors.resume = resumeError;
    } else {
      if (!jobFile) newErrors.job = 'Job description file is required';
      if (!resumeFile) newErrors.resume = 'Resume file is required';

      if (jobFile) {
        const jobValidation = validateFile(jobFile, ALLOWED_FILE_TYPES.DOCUMENTS);
        if (!jobValidation.valid) newErrors.job = jobValidation.error;
      }

      if (resumeFile) {
        const resumeValidation = validateFile(resumeFile, ALLOWED_FILE_TYPES.DOCUMENTS);
        if (!resumeValidation.valid) newErrors.resume = resumeValidation.error;
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleUpload = async () => {
    if (!validateForm()) return;

    try {
      const formData = new FormData();

      if (inputMode === INPUT_MODES.PASTE) {
        formData.append('jobDescription', jobDescription);
        formData.append('resume', resume);
      } else {
        if (jobFile) formData.append('jobDescription', jobFile);
        if (resumeFile) formData.append('resume', resumeFile);
      }

      await uploadApi.execute(() => fileService.uploadDocuments(formData));
      showSuccess('Documents uploaded successfully');
      clearForm();
    } catch (err: unknown) {
      showError((err as Error)?.message || 'Failed to upload documents');
    }
  };

  const handleProcessResume = async () => {
    if (!validateForm()) return;

    try {
      const data = {
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        resume: inputMode === INPUT_MODES.PASTE ? resume : resumeFile?.name || '',
      };

      const result = await resumeApi.execute(() => fileService.processResume(data));
      showSuccess('Resume optimized successfully');
      console.log('Optimized Resume:', result);
    } catch (err: unknown) {
      showError((err as Error)?.message || 'Failed to process resume');
    }
  };

  const handleProcessCoverLetter = async () => {
    if (!validateForm()) return;

    try {
      const data = {
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        resume: inputMode === INPUT_MODES.PASTE ? resume : resumeFile?.name || '',
      };

      const result = await coverLetterApi.execute(() => fileService.processCoverLetter(data));
      showSuccess('Cover letter generated successfully');
      console.log('Cover Letter:', result);
    } catch (err: unknown) {
      showError((err as Error)?.message || 'Failed to generate cover letter');
    }
  };

  const clearForm = () => {
    setJobDescription('');
    setResume('');
    setJobFile(null);
    setResumeFile(null);
    setErrors({});
  };

  const loading = uploadApi.loading || resumeApi.loading || coverLetterApi.loading;

  return (
    <Card title="Document Upload & Processing" className="h-full">
      <div className="flex flex-column gap-4">
        <div className="field">
          <label className="font-bold block mb-2">Input Mode</label>
          <SelectButton
            value={inputMode}
            onChange={e => setInputMode(e.value)}
            options={modes}
            aria-label="Select input mode"
          />
        </div>

        {inputMode === INPUT_MODES.PASTE ? (
          <>
            <div className="field">
              <label htmlFor="jobDescription" className="font-bold block mb-2">
                Job Description *
              </label>
              <InputTextarea
                id="jobDescription"
                value={jobDescription}
                onChange={e => setJobDescription(e.target.value)}
                rows={6}
                className={`w-full ${errors.job ? 'p-invalid' : ''}`}
                placeholder="Paste the job description here..."
                aria-describedby="job-error"
              />
              {errors.job && (
                <small id="job-error" className="p-error block mt-1">
                  {errors.job}
                </small>
              )}
            </div>

            <div className="field">
              <label htmlFor="resume" className="font-bold block mb-2">
                Source Resume *
              </label>
              <InputTextarea
                id="resume"
                value={resume}
                onChange={e => setResume(e.target.value)}
                rows={6}
                className={`w-full ${errors.resume ? 'p-invalid' : ''}`}
                placeholder="Paste your resume here..."
                aria-describedby="resume-error"
              />
              {errors.resume && (
                <small id="resume-error" className="p-error block mt-1">
                  {errors.resume}
                </small>
              )}
            </div>
          </>
        ) : (
          <>
            <div className="field">
              <label className="font-bold block mb-2">Job Description File *</label>
              <FileUpload
                mode="basic"
                name="jobFile"
                accept=".pdf,.doc,.docx,.txt"
                maxFileSize={50000000}
                onSelect={e => setJobFile(e.files[0])}
                onClear={() => setJobFile(null)}
                chooseLabel="Choose File"
                className="w-full"
                aria-describedby="job-file-error"
              />
              {errors.job && (
                <small id="job-file-error" className="p-error block mt-1">
                  {errors.job}
                </small>
              )}
            </div>

            <div className="field">
              <label className="font-bold block mb-2">Resume File *</label>
              <FileUpload
                mode="basic"
                name="resumeFile"
                accept=".pdf,.doc,.docx,.txt"
                maxFileSize={50000000}
                onSelect={e => setResumeFile(e.files[0])}
                onClear={() => setResumeFile(null)}
                chooseLabel="Choose File"
                className="w-full"
                aria-describedby="resume-file-error"
              />
              {errors.resume && (
                <small id="resume-file-error" className="p-error block mt-1">
                  {errors.resume}
                </small>
              )}
            </div>
          </>
        )}

        {loading && <LoadingSpinner message="Processing..." />}

        <div className="flex flex-wrap gap-2">
          <Button
            label="Create Optimized Resume"
            icon="pi pi-file-edit"
            onClick={handleProcessResume}
            disabled={loading}
            severity="info"
          />
          <Button
            label="Create Cover Letter"
            icon="pi pi-file"
            onClick={handleProcessCoverLetter}
            disabled={loading}
            severity="success"
          />
          <Button
            label="Submit"
            icon="pi pi-upload"
            onClick={handleUpload}
            disabled={loading}
          />
          <Button
            label="Clear"
            icon="pi pi-times"
            onClick={clearForm}
            disabled={loading}
            severity="secondary"
            outlined
          />
        </div>
      </div>
    </Card>
  );
};
