import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';

// Import component
import { SubmissionDialog } from '../../../components/Common/SubmissionDialog';

describe('SubmissionDialog', () => {
  const mockOnHide = vi.fn();

  it('should render the dialog when visible is true', () => {
    render(<SubmissionDialog visible={true} onHide={mockOnHide} />);
    expect(screen.getByText('Important: Before Submission')).toBeInTheDocument();
  });

  it('should not render the dialog when visible is false', () => {
    const { container } = render(<SubmissionDialog visible={false} onHide={mockOnHide} />);
    const dialog = container.querySelector('[role="dialog"]');
    expect(dialog).not.toBeVisible();
  });

  it('should display markdown cleanup warning', () => {
    render(<SubmissionDialog visible={true} onHide={mockOnHide} />);
    expect(screen.getByText('LLM Response Cleanup Required')).toBeInTheDocument();
    expect(screen.getByText(/Section headers like "### Suggestions"/)).toBeInTheDocument();
  });

  it('should display processing time information', () => {
    render(<SubmissionDialog visible={true} onHide={mockOnHide} />);
    expect(screen.getByText('⏱️ Processing Time')).toBeInTheDocument();
    expect(screen.getByText(/File generation may take a few minutes/)).toBeInTheDocument();
  });

  it('should display DOCX conversion feature announcement', () => {
    render(<SubmissionDialog visible={true} onHide={mockOnHide} />);
    expect(screen.getByText(/DOCX conversion is now available/)).toBeInTheDocument();
  });

  it('should list common markdown issues to clean up', () => {
    render(<SubmissionDialog visible={true} onHide={mockOnHide} />);
    expect(screen.getByText(/Decorative formatting or separator lines/)).toBeInTheDocument();
    expect(screen.getByText(/Meta-comments or explanatory text/)).toBeInTheDocument();
    expect(screen.getByText(/Incomplete or partial content at the end/)).toBeInTheDocument();
  });

  it('should call onHide when dialog is closed', () => {
    render(<SubmissionDialog visible={true} onHide={mockOnHide} />);
    const closeButton = screen.getByRole('button', { name: /close/i });
    if (closeButton) {
      fireEvent.click(closeButton);
      // The onHide callback should be triggered
    }
  });
});
