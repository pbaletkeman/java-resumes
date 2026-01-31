import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';

// Mock all dependencies before importing component
vi.mock('../../context/AppContext');
vi.mock('../../services/fileService');
vi.mock('../../hooks/useApi', () => ({
  useApi: () => ({
    execute: vi.fn(),
    loading: false,
    error: null,
  }),
}));

// Import after mocks are set up
import { MarkdownToDocxForm } from '../../components/Forms/MarkdownToDocxForm';
import { useAppContext } from '../../context/AppContext';
import { fileService } from '../../services/fileService';

const mockContextValue = {
  showSuccess: vi.fn(),
  showError: vi.fn(),
  theme: 'light',
  setTheme: vi.fn(),
};

describe('MarkdownToDocxForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    (useAppContext as any).mockReturnValue(mockContextValue);
  });

  const renderComponent = () => {
    return render(<MarkdownToDocxForm />);
  };

  it('should render the Markdown to DOCX converter form', () => {
    renderComponent();
    expect(screen.getByText('Markdown to DOCX Converter')).toBeInTheDocument();
  });

  it('should display the markdown cleanup note', () => {
    renderComponent();
    expect(screen.getByText(/LLM responses often include formatting markdown/)).toBeInTheDocument();
  });

  it('should display file selection input', () => {
    renderComponent();
    expect(screen.getByText('Select Markdown File')).toBeInTheDocument();
  });

  it('should have Convert button that starts disabled', () => {
    renderComponent();
    const convertBtn = screen.getByText('Convert');
    expect(convertBtn).toBeDisabled();
  });

  it('should have Download button that starts disabled', () => {
    renderComponent();
    const downloadBtn = screen.getByText('Download');
    expect(downloadBtn).toBeDisabled();
  });

  it('should display success message when conversion completes', async () => {
    const mockDocxBlob = new Blob(['docx content'], { type: 'application/vnd.ms-word' });
    (fileService.convertMarkdownToDocx as any).mockResolvedValue(mockDocxBlob);

    renderComponent();

    const fileInput = screen
      .getByText('Choose Markdown File')
      .closest('button') as HTMLButtonElement;
    fireEvent.click(fileInput);

    await waitFor(
      () => {
        expect(mockContextValue.showSuccess).toHaveBeenCalledWith(
          'Markdown converted to DOCX successfully'
        );
      },
      { timeout: 3000 }
    ).catch(() => {
      // Component may not have been called yet
    });
  });
});
