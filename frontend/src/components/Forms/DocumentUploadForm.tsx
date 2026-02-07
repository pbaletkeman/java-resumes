import React, { useState, useCallback, useEffect } from 'react';
import { Card } from 'primereact/card';
import { Button } from 'primereact/button';
import { InputTextarea } from 'primereact/inputtextarea';
import { InputText } from 'primereact/inputtext';
import { Dropdown } from 'primereact/dropdown';
import { FileUpload } from 'primereact/fileupload';
import { SelectButton } from 'primereact/selectbutton';
import { useApi } from '../../hooks/useApi';
import { useAppContext } from '../../context/AppContext';
import { fileService } from '../../services/fileService';
import { validateTextInput, validateFile } from '../../utils/validators';
import { INPUT_MODES, ALLOWED_FILE_TYPES } from '../../utils/constants';
import { LoadingSpinner } from '../Common/LoadingSpinner';
import { SubmissionDialog } from '../Common/SubmissionDialog';

// Default models (fallback if not in storage)
const DEFAULT_MODELS = [
  { label: 'Gemma 3.4B (Default)', value: 'gemma-3-4b-it' },
  { label: 'GPT-4 Turbo', value: 'gpt-4-turbo' },
  { label: 'Claude 3 Opus', value: 'claude-3-opus' },
  { label: 'Llama 2 Chat', value: 'llama-2-chat' },
  { label: 'Mistral', value: 'mistral' },
];

export const DocumentUploadForm: React.FC = () => {
  const [inputMode, setInputMode] = useState(INPUT_MODES.PASTE);
  const [jobDescription, setJobDescription] = useState('');
  const [resume_string, setResumeString] = useState('');
  const [jobFile, setJobFile] = useState<File | null>(null);
  const [resumeFile, setResumeFile] = useState<File | null>(null);
  const [jobTitle, setJobTitle] = useState('');
  const [company_name, setCompanyName] = useState('');
  const [interviewerName, setInterviewerName] = useState('');
  const [model, setModel] = useState('gemma-3-4b-it');
  const [promptTypes, setPromptTypes] = useState<string[]>(['resume']);
  const [modelOptions, setModelOptions] = useState(DEFAULT_MODELS);
  const [errors, setErrors] = useState<{
    job?: string;
    resume?: string;
    jobTitle?: string;
    company?: string;
  }>({});
  const [showSubmissionDialog, setShowSubmissionDialog] = useState(false);

  const { showSuccess, showError } = useAppContext();
  const uploadApi = useApi();
  const resumeApi = useApi();
  const coverLetterApi = useApi();

  // Load models from storage on mount
  useEffect(() => {
    const saved = localStorage.getItem('java-resumes-models');
    if (saved) {
      try {
        setModelOptions(JSON.parse(saved));
      } catch (err) {
        console.error('Failed to load saved models:', err);
      }
    }
  }, []);

  // Memoized change handlers to prevent re-renders
  const handleJobDescriptionChange = useCallback((e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setJobDescription(e.target.value);
  }, []);

  const handleResumeChange = useCallback((e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setResumeString(e.target.value);
  }, []);

  const handleJobTitleChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    setJobTitle(e.target.value);
  }, []);

  const handleCompanyChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    setCompanyName(e.target.value);
  }, []);

  const handleInterviewerNameChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    setInterviewerName(e.target.value);
  }, []);

  const promptTypeOptions = [
    // Resume and Cover Letter
    { label: 'Resume Optimization', value: 'resume' },
    { label: 'Cover Letter', value: 'cover' },
    { label: 'Skills & Certifications', value: 'skills' },
    // Interview Preparation
    { label: 'HR Interview Questions', value: 'interview-hr' },
    { label: 'Job-Specific Interview Questions', value: 'interview-job' },
    { label: 'Reverse Interview (Practice Yours)', value: 'interview-reverse' },
    // Networking & Outreach
    { label: 'Cold Email', value: 'cold-email' },
    { label: 'LinkedIn Connection Message', value: 'cold-linkedin' },
    { label: 'Thank You Email', value: 'thank-you-email' },
  ];

  const modes = [
    { label: 'Paste Text', value: INPUT_MODES.PASTE },
    { label: 'Upload Files', value: INPUT_MODES.UPLOAD },
  ];

  const validateForm = (): boolean => {
    const newErrors: { job?: string; resume?: string; jobTitle?: string; company?: string } = {};

    if (inputMode === INPUT_MODES.PASTE) {
      const jobError = validateTextInput(jobDescription, 'Job Description');
      // Resume is only required if not generating Skills only
      const isSkillsOnly = promptTypes.length === 1 && promptTypes.includes('skills');
      const resumeError = !isSkillsOnly ? validateTextInput(resume_string, 'Resume') : null;
      if (jobError) newErrors.job = jobError;
      if (resumeError) newErrors.resume = resumeError;
    } else {
      if (!jobFile) newErrors.job = 'Job description file is required';
      const isSkillsOnly = promptTypes.length === 1 && promptTypes.includes('skills');
      if (!resumeFile && !isSkillsOnly) newErrors.resume = 'Resume file is required';

      if (jobFile) {
        const jobValidation = validateFile(jobFile, ALLOWED_FILE_TYPES.DOCUMENTS);
        if (!jobValidation.valid) newErrors.job = jobValidation.error;
      }

      if (resumeFile) {
        const resumeValidation = validateFile(resumeFile, ALLOWED_FILE_TYPES.DOCUMENTS);
        if (!resumeValidation.valid) newErrors.resume = resumeValidation.error;
      }
    }

    if (!jobTitle.trim()) newErrors.jobTitle = 'Job Title is required';
    if (!company_name.trim()) newErrors.company = 'Company is required';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleUpload = async () => {
    if (!validateForm()) return;

    try {
      const formData = new FormData();

      if (inputMode === INPUT_MODES.PASTE) {
        formData.append('jobDescription', jobDescription);
        formData.append('resume', resume_string);
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
    setShowSubmissionDialog(true);
    if (!validateForm()) return;

    try {
      const optimize = {
        promptType: ['resume'],
        temperature: 0.15,
        model: model,
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        jobTitle: jobTitle,
        company: company_name,
        interviewerName: interviewerName,
      };

      const data = {
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        optimize: JSON.stringify(optimize),
      };

      const result = await resumeApi.execute(() => fileService.processResume(data));
      showSuccess('Resume optimized successfully');
      console.log('Optimized Resume:', result);
    } catch (err: unknown) {
      showError((err as Error)?.message || 'Failed to process resume');
    }
  };

  const handleProcessCoverLetter = async () => {
    setShowSubmissionDialog(true);
    if (!validateForm()) return;

    try {
      const optimize = {
        promptType: ['cover'],
        temperature: 0.15,
        model: model,
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        jobTitle: jobTitle,
        company: company_name,
        interviewerName: interviewerName,
      };

      const data = {
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        optimize: JSON.stringify(optimize),
      };

      const result = await coverLetterApi.execute(() => fileService.processCoverLetter(data));
      showSuccess('Cover letter generated successfully');
      console.log('Cover Letter:', result);
    } catch (err: unknown) {
      showError((err as Error)?.message || 'Failed to generate cover letter');
    }
  };

  const handleProcessSkills = async () => {
    if (!validateForm()) return;

    try {
      const optimize = {
        promptType: ['skills'],
        temperature: 0.15,
        model: model,
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        jobTitle: jobTitle,
        company: company_name,
        interviewerName: interviewerName,
      };

      const data = {
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        optimize: JSON.stringify(optimize),
      };

      const result = await resumeApi.execute(() => fileService.processSkills(data));
      showSuccess('Skills and certifications suggestions generated successfully');
      console.log('Skills Suggestions:', result);
    } catch (err: unknown) {
      showError((err as Error)?.message || 'Failed to generate skills suggestions');
    }
  };

  const handleProcessInterviewHr = async () => {
    if (!validateForm()) return;

    try {
      const optimize = {
        promptType: ['interview-hr-questions'],
        temperature: 0.15,
        model: model,
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        jobTitle: jobTitle,
        company: company_name,
        interviewerName: interviewerName,
      };

      const data = {
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        optimize: JSON.stringify(optimize),
      };

      const result = await resumeApi.execute(() => fileService.processInterviewHr(data));
      showSuccess('Interview questions generated successfully');
      console.log('Interview HR Questions:', result);
    } catch (err: unknown) {
      showError((err as Error)?.message || 'Failed to generate interview questions');
    }
  };

  const handleProcessInterviewJobSpecific = async () => {
    if (!validateForm()) return;

    try {
      const optimize = {
        promptType: ['interview-job-specific'],
        temperature: 0.15,
        model: model,
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        jobTitle: jobTitle,
        company: company_name,
        interviewerName: interviewerName,
      };

      const data = {
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        optimize: JSON.stringify(optimize),
      };

      const result = await resumeApi.execute(() => fileService.processInterviewJobSpecific(data));
      showSuccess('Job-specific interview questions generated successfully');
      console.log('Interview Job-Specific Questions:', result);
    } catch (err: unknown) {
      showError((err as Error)?.message || 'Failed to generate job-specific questions');
    }
  };

  const handleProcessInterviewReverse = async () => {
    if (!validateForm()) return;

    try {
      const optimize = {
        promptType: ['interview-reverse'],
        temperature: 0.15,
        model: model,
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        jobTitle: jobTitle,
        company: company_name,
        interviewerName: interviewerName,
      };

      const data = {
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        optimize: JSON.stringify(optimize),
      };

      const result = await resumeApi.execute(() => fileService.processInterviewReverse(data));
      showSuccess('Reverse interview practice questions generated successfully');
      console.log('Interview Reverse Questions:', result);
    } catch (err: unknown) {
      showError((err as Error)?.message || 'Failed to generate reverse interview questions');
    }
  };

  const handleGenerateColdEmail = async () => {
    if (!validateForm()) return;

    try {
      const optimize = {
        promptType: ['cold-email'],
        temperature: 0.15,
        model: model,
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        jobTitle: jobTitle,
        company: company_name,
        interviewerName: interviewerName,
      };

      const data = {
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        optimize: JSON.stringify(optimize),
      };

      const result = await resumeApi.execute(() => fileService.generateColdEmail(data));
      showSuccess('Cold email generated successfully');
      console.log('Cold Email:', result);
    } catch (err: unknown) {
      showError((err as Error)?.message || 'Failed to generate cold email');
    }
  };

  const handleGenerateColdLinkedIn = async () => {
    if (!validateForm()) return;

    try {
      const optimize = {
        promptType: ['cold-linkedin-message'],
        temperature: 0.15,
        model: model,
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        jobTitle: jobTitle,
        company: company_name,
        interviewerName: interviewerName,
      };

      const data = {
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        optimize: JSON.stringify(optimize),
      };

      const result = await resumeApi.execute(() => fileService.generateColdLinkedInMessage(data));
      showSuccess('LinkedIn message generated successfully');
      console.log('Cold LinkedIn Message:', result);
    } catch (err: unknown) {
      showError((err as Error)?.message || 'Failed to generate LinkedIn message');
    }
  };

  const handleGenerateThankYouEmail = async () => {
    if (!validateForm()) return;

    try {
      const optimize = {
        promptType: ['thank-you-email'],
        temperature: 0.15,
        model: model,
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        jobTitle: jobTitle,
        company: company_name,
        interviewerName: interviewerName,
      };

      const data = {
        jobDescription: inputMode === INPUT_MODES.PASTE ? jobDescription : jobFile?.name || '',
        resume: inputMode === INPUT_MODES.PASTE ? resume_string : resumeFile?.name || '',
        optimize: JSON.stringify(optimize),
      };

      const result = await resumeApi.execute(() => fileService.generateThankYouEmail(data));
      showSuccess('Thank you email generated successfully');
      console.log('Thank You Email:', result);
    } catch (err: unknown) {
      showError((err as Error)?.message || 'Failed to generate thank you email');
    }
  };

  const clearForm = () => {
    setJobDescription('');
    setResumeString('');
    setJobFile(null);
    setResumeFile(null);
    setJobTitle('');
    setCompanyName('');
    setInterviewerName('');
    setModel('gemma-3-4b-it');
    setPromptTypes(['resume']);
    setErrors({});
  };

  const loading = uploadApi.loading || resumeApi.loading || coverLetterApi.loading;
  const hasFormData = jobDescription || resume_string || jobFile || resumeFile;

  return (
    <Card className="h-full">
      <div className="flex flex-column gap-4 w-full">
        {/* 1. Input Mode Selection and Form Container */}
        <div className="w-full">
          {/* Input Mode Selector */}
          <div
            className="bg-yellow-50 border-1 border-yellow-300"
            style={{
              padding: '20px',
              marginLeft: '1rem',
              marginRight: '1rem',
              borderRadius: '6px',
              maxWidth: 'calc(100% - 2rem)',
              marginBottom: '2rem',
              marginTop: '1.5rem',
            }}
          >
            <div className="w-full flex gap-4 align-items-center justify-content-between">
              <label className="font-bold text-lg" style={{ minWidth: '20%' }}>
                Input Mode
              </label>
              <div
                className="flex align-items-center justify-content-center flex-1"
                style={{ gap: '2rem' }}
              >
                <SelectButton
                  value={inputMode}
                  onChange={e => setInputMode(e.value)}
                  options={modes}
                  aria-label="Select input mode"
                  className="text-base"
                  style={{ gap: '2rem' }}
                />
              </div>
            </div>
          </div>

          {/* 2. Document Upload & Processing Title */}
          <div
            className="font-bold text-lg text-cyan-900"
            style={{
              marginLeft: '1rem',
              marginRight: '1rem',
              marginTop: '1.5rem',
              marginBottom: '1rem',
            }}
          >
            Document Upload & Processing
          </div>

          {/* User Input Parameters Section */}
          <div
            className="bg-blue-50 border-1 border-blue-300 p-4 mb-6"
            style={{
              marginLeft: '1rem',
              marginRight: '1rem',
              borderRadius: '6px',
              maxWidth: 'calc(100% - 2rem)',
              marginTop: '1.5rem',
            }}
          >
            <div className="font-bold mb-6" style={{ marginLeft: '0.25rem' }}>
              Job Information & Model Selection
            </div>
            <div
              className="grid grid-cols-3 gap-5"
              style={{ marginTop: '1.5rem', marginBottom: '1.5rem' }}
            >
              <div className="field" style={{ marginRight: '15px', marginLeft: '0.5rem' }}>
                <label htmlFor="jobTitle" className="block mb-2">
                  Job Title *
                </label>
                <InputText
                  id="jobTitle"
                  value={jobTitle}
                  onChange={handleJobTitleChange}
                  placeholder="e.g., Senior Software Engineer"
                  className={`w-full ${errors.jobTitle ? 'p-invalid' : ''}`}
                  aria-describedby="jobtitle-error"
                />
                {errors.jobTitle && (
                  <small id="jobtitle-error" className="p-error block mt-1">
                    {errors.jobTitle}
                  </small>
                )}
              </div>

              <div className="field" style={{ marginRight: '15px' }}>
                <label htmlFor="company" className="block mb-2">
                  Company *
                </label>
                <InputText
                  id="company"
                  value={company_name}
                  onChange={handleCompanyChange}
                  placeholder="e.g., Tech Corporation"
                  className={`w-full ${errors.company ? 'p-invalid' : ''}`}
                  aria-describedby="company-error"
                />
                {errors.company && (
                  <small id="company-error" className="p-error block mt-1">
                    {errors.company}
                  </small>
                )}
              </div>

              <div className="field" style={{ marginRight: '1.5rem' }}>
                <label htmlFor="model" className="block mb-2">
                  AI Model
                </label>
                <Dropdown
                  id="model"
                  value={model}
                  onChange={e => setModel(e.value)}
                  options={modelOptions}
                  optionLabel="label"
                  optionValue="value"
                  placeholder="Select model..."
                  className="w-full"
                />
              </div>

              <div className="field" style={{ marginRight: '15px', marginTop: '20px' }}>
                <label htmlFor="promptTypes" className="block mb-2">
                  Output Types
                </label>
                <Dropdown
                  id="promptTypes"
                  value={promptTypes}
                  onChange={e => {
                    // console.log('Dropdown change event:', e, 'e.value:', e.value);
                    setPromptTypes(e.value || []);
                    // Clear previous results when changing output types to prevent stale data
                    uploadApi.reset();
                    resumeApi.reset();
                    coverLetterApi.reset();
                  }}
                  options={promptTypeOptions}
                  optionLabel="label"
                  optionValue="value"
                  placeholder="Select output types..."
                  className="w-full"
                  multiple
                />
              </div>

              <div className="field" style={{ marginRight: '15px', marginTop: '20px' }}>
                <label htmlFor="interviewerName" className="block mb-2">
                  Interviewer Name (Optional)
                </label>
                <InputText
                  id="interviewerName"
                  value={interviewerName}
                  onChange={handleInterviewerNameChange}
                  placeholder="e.g., John Smith"
                  className="w-full"
                />
              </div>
            </div>
          </div>
          <div className="w-full">
            {inputMode === INPUT_MODES.PASTE ? (
              <div
                className="w-full"
                style={{ display: 'flex', flexDirection: 'row', gap: '0.5rem' }}
              >
                <div
                  className="field"
                  style={{
                    flex: 1,
                    display: 'block',
                    backgroundColor: '#f9fafb',
                    padding: '1rem',
                    borderRadius: '6px',
                  }}
                >
                  <label htmlFor="jobDescription" className="font-bold block mb-2">
                    Job Description *
                  </label>
                  <InputTextarea
                    id="jobDescription"
                    value={jobDescription}
                    onChange={handleJobDescriptionChange}
                    rows={20}
                    className={`w-full ${errors.job ? 'p-invalid' : ''}`}
                    placeholder="Paste the job description here..."
                    aria-describedby="job-error"
                    style={{ minHeight: '400px', width: '100%', display: 'block' }}
                  />
                  {errors.job && (
                    <small id="job-error" className="p-error block mt-1">
                      {errors.job}
                    </small>
                  )}
                </div>

                <div
                  className="field"
                  style={{
                    flex: 1,
                    display: 'block',
                    backgroundColor: '#ffffff',
                    padding: '1rem',
                    borderRadius: '6px',
                  }}
                >
                  <label htmlFor="resume" className="font-bold block mb-2">
                    Source Resume{' '}
                    {promptTypes.length === 1 && promptTypes.includes('skills') ? '' : '*'}
                  </label>
                  <InputTextarea
                    id="resume"
                    value={resume_string}
                    onChange={handleResumeChange}
                    rows={20}
                    className={`w-full ${errors.resume ? 'p-invalid' : ''}`}
                    placeholder="Paste your resume here..."
                    aria-describedby="resume-error"
                    style={{ minHeight: '400px', width: '100%', display: 'block' }}
                  />
                  {errors.resume && (
                    <small id="resume-error" className="p-error block mt-1">
                      {errors.resume}
                    </small>
                  )}
                </div>
              </div>
            ) : (
              <div
                className="w-full"
                style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}
              >
                <div
                  className="field w-full"
                  style={{
                    display: 'block',
                    backgroundColor: '#f9fafb',
                    padding: '1rem',
                    borderRadius: '6px',
                  }}
                >
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

                <div
                  className="field w-full"
                  style={{
                    display: 'block',
                    backgroundColor: '#ffffff',
                    padding: '1rem',
                    borderRadius: '6px',
                  }}
                >
                  <label className="font-bold block mb-2">
                    Resume File{' '}
                    {promptTypes.length === 1 && promptTypes.includes('skills') ? '' : '*'}
                  </label>
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
              </div>
            )}

            {/* Processing Buttons - Only Show When Form Data Exists */}
            {hasFormData && (
              <>
                {loading && <LoadingSpinner message="Processing..." />}

                <div className="bg-primary-50 dark:bg-surface-800 p-4 border-1 border-primary surface-border border-round">
                  <div className="text-sm font-semibold mb-3">Documents Ready for Processing</div>
                  <div className="grid grid-cols-2 gap-2 mb-3 text-sm">
                    <div>
                      <span className="font-semibold">Job Description:</span>
                      <div className="text-gray-600 dark:text-gray-400 truncate">
                        {inputMode === INPUT_MODES.PASTE
                          ? jobDescription.substring(0, 40) + '...'
                          : jobFile?.name}
                      </div>
                    </div>
                    <div>
                      <span className="font-semibold">Resume:</span>
                      <div className="text-gray-600 dark:text-gray-400 truncate">
                        {inputMode === INPUT_MODES.PASTE
                          ? resume_string.substring(0, 40) + '...'
                          : resumeFile?.name}
                      </div>
                    </div>
                  </div>
                </div>

                <div className="flex flex-wrap gap-2">
                  {promptTypes.includes('resume') && (
                    <Button
                      key="btn-resume"
                      label="Optimize Resume"
                      icon="pi pi-file-edit"
                      onClick={handleProcessResume}
                      disabled={loading}
                      severity="info"
                      className="flex-1"
                    />
                  )}
                  {promptTypes.includes('cover') && (
                    <Button
                      key="btn-cover"
                      label="Generate Cover Letter"
                      icon="pi pi-file"
                      onClick={handleProcessCoverLetter}
                      disabled={loading}
                      severity="success"
                      className="flex-1"
                    />
                  )}
                  {promptTypes.includes('skills') && (
                    <Button
                      key="btn-skills"
                      label="Generate Skills & Certs"
                      icon="pi pi-star"
                      onClick={handleProcessSkills}
                      disabled={loading}
                      severity="warning"
                      className="flex-1"
                    />
                  )}
                  {promptTypes.includes('interview-hr') && (
                    <Button
                      key="btn-interview-hr"
                      label="Generate HR Questions"
                      icon="pi pi-question"
                      onClick={handleProcessInterviewHr}
                      disabled={loading}
                      severity="danger"
                      className="flex-1"
                    />
                  )}
                  {promptTypes.includes('interview-job') && (
                    <Button
                      key="btn-interview-job"
                      label="Job-Specific Questions"
                      icon="pi pi-question"
                      onClick={handleProcessInterviewJobSpecific}
                      disabled={loading}
                      severity="danger"
                      className="flex-1"
                    />
                  )}
                  {promptTypes.includes('interview-reverse') && (
                    <Button
                      key="btn-interview-reverse"
                      label="Reverse Interview"
                      icon="pi pi-comments"
                      onClick={handleProcessInterviewReverse}
                      disabled={loading}
                      severity="danger"
                      className="flex-1"
                    />
                  )}
                  {promptTypes.includes('cold-email') && (
                    <Button
                      key="btn-cold-email"
                      label="Cold Email"
                      icon="pi pi-send"
                      onClick={handleGenerateColdEmail}
                      disabled={loading}
                      severity="secondary"
                      className="flex-1"
                    />
                  )}
                  {promptTypes.includes('cold-linkedin') && (
                    <Button
                      key="btn-cold-linkedin"
                      label="LinkedIn Message"
                      icon="pi pi-send"
                      onClick={handleGenerateColdLinkedIn}
                      disabled={loading}
                      severity="secondary"
                      className="flex-1"
                    />
                  )}
                  {promptTypes.includes('thank-you-email') && (
                    <Button
                      key="btn-thank-you"
                      label="Thank You Email"
                      icon="pi pi-envelope"
                      onClick={handleGenerateThankYouEmail}
                      disabled={loading}
                      severity="secondary"
                      className="flex-1"
                    />
                  )}
                </div>

                <div className="bg-blue-50 border-1 border-blue-300 rounded p-3 mb-3">
                  <p className="text-sm text-blue-900 m-0">
                    <i className="pi pi-info-circle mr-2" />
                    <strong>Note:</strong> File generation can take a few minutes depending on the
                    model and content size. Please be patient while processing.
                  </p>
                </div>

                <div className="flex flex-wrap gap-2">
                  <Button
                    label="Submit"
                    icon="pi pi-upload"
                    onClick={handleUpload}
                    disabled={loading}
                    className="flex-1"
                  />
                  <Button
                    label="Clear & Start Over"
                    icon="pi pi-times"
                    onClick={clearForm}
                    disabled={loading}
                    severity="secondary"
                    outlined
                    className="flex-1"
                  />
                </div>
              </>
            )}
          </div>
        </div>
      </div>
      <SubmissionDialog
        visible={showSubmissionDialog}
        onHide={() => setShowSubmissionDialog(false)}
      />
    </Card>
  );
};
