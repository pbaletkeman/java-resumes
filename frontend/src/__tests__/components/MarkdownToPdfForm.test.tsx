import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';

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
import { MarkdownToPdfForm } from '../../components/Forms/MarkdownToPdfForm';
import { useAppContext } from '../../context/AppContext';

const mockContextValue = {
  showSuccess: vi.fn(),
  showError: vi.fn(),
  theme: 'light',
  setTheme: vi.fn(),
};

describe('MarkdownToPdfForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    (useAppContext as any).mockReturnValue(mockContextValue);
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
});
