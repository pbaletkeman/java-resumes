import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';

// Mock all dependencies before importing component
vi.mock('../../../context/AppContext', () => ({
  useAppContext: () => ({
    showSuccess: vi.fn(),
    showError: vi.fn(),
    theme: 'light',
    setTheme: vi.fn(),
  }),
}));

vi.mock('../../../hooks/useApi', () => ({
  useApi: () => ({
    execute: vi.fn(),
    loading: false,
    error: null,
  }),
}));

vi.mock('../../../services/fileService', () => ({
  fileService: {
    convertMarkdownToPdf: vi.fn(),
    convertMarkdownToDocx: vi.fn(),
  },
}));

// Import after mocks are set up
import { AdditionalToolsTab } from '../../../components/Tabs/AdditionalToolsTab';

const renderComponent = () => {
  return render(<AdditionalToolsTab />);
};

describe('AdditionalToolsTab', () => {
  it('should render both PDF and DOCX converter forms', () => {
    renderComponent();
    expect(screen.getByText('Markdown to PDF Converter')).toBeInTheDocument();
    expect(screen.getByText('Markdown to DOCX Converter')).toBeInTheDocument();
  });

  it('should display forms in a grid layout', () => {
    const { container } = renderComponent();
    const gridContainer = container.querySelector('.grid');
    expect(gridContainer).toBeInTheDocument();
  });

  it('should display markdown cleanup notes in both forms', () => {
    renderComponent();
    const notes = screen.getAllByText(/LLM responses often include formatting markdown/);
    expect(notes.length).toBeGreaterThanOrEqual(2);
  });
});
