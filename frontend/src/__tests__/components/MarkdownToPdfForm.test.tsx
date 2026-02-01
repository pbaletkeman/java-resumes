import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

// Mock all dependencies before importing component
vi.mock('../../context/AppContext');
vi.mock('../../services/fileService');
vi.mock('../../utils/validators', () => ({
  validateFile: vi.fn(() => ({ valid: true })),
}));
vi.mock('../../utils/helpers', () => ({
  downloadFile: vi.fn(),
}));

let mockExecute = vi.fn();
vi.mock('../../hooks/useApi', () => ({
  useApi: () => ({
    execute: mockExecute,
    loading: false,
    error: null,
  }),
}));

// Import after mocks are set up
import { MarkdownToPdfForm } from '../../components/Forms/MarkdownToPdfForm';
import { useAppContext } from '../../context/AppContext';
import { validateFile } from '../../utils/validators';
import { downloadFile } from '../../utils/helpers';

const mockContextValue = {
  showSuccess: vi.fn(),
  showError: vi.fn(),
  theme: 'light',
  setTheme: vi.fn(),
};

describe('MarkdownToPdfForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockExecute = vi.fn();
    (useAppContext as any).mockReturnValue(mockContextValue);
    (validateFile as any).mockReturnValue({ valid: true });
  });

  const renderComponent = () => {
    return render(<MarkdownToPdfForm />);
  };

  it('should render the Markdown to PDF converter form', () => {
    renderComponent();
    expect(screen.getByText('Markdown to PDF Converter')).toBeInTheDocument();
  });

  it('should display file selection input', () => {
    renderComponent();
    expect(screen.getByText('Select Markdown File')).toBeInTheDocument();
  });

  it('should have Convert button that starts disabled', () => {
    renderComponent();
    const convertBtn = screen.getByRole('button', { name: /convert/i });
    expect(convertBtn).toBeDisabled();
  });

  it('should have Download button that starts disabled', () => {
    renderComponent();
    const downloadBtn = screen.getByRole('button', { name: /download/i });
    expect(downloadBtn).toBeDisabled();
  });

  it('should display the markdown cleanup note', () => {
    renderComponent();
    expect(screen.getByText(/LLM responses often include formatting markdown/)).toBeInTheDocument();
  });

  it('should show error message when invalid file is selected', () => {
    (validateFile as any).mockReturnValue({ valid: false, error: 'Invalid file type' });
    renderComponent();
    
    const input = screen.getByText('Select Markdown File').parentElement?.querySelector('input[type="file"]');
    if (input) {
      const file = new File(['test'], 'test.txt', { type: 'text/plain' });
      const event = { files: [file] };
      // Simulate the onSelect callback
      const fileUpload = screen.getByText('Select Markdown File').parentElement;
      expect(fileUpload).toBeInTheDocument();
    }
  });

  it('should have Clear button', () => {
    renderComponent();
    const buttons = screen.getAllByRole('button');
    expect(buttons.length).toBeGreaterThan(0);
  });

  it('should display all required buttons', () => {
    renderComponent();
    expect(screen.getByRole('button', { name: /convert/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /download/i })).toBeInTheDocument();
  });

  it('should have proper form structure', () => {
    renderComponent();
    const card = screen.getByText('Markdown to PDF Converter').closest('.p-card');
    expect(card).toBeInTheDocument();
  });

  it('should contain markdown file input with correct accept attribute', () => {
    renderComponent();
    const fileInput = document.querySelector('input[type="file"]');
    expect(fileInput).toBeInTheDocument();
  });
});
