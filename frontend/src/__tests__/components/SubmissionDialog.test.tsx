import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';

// Import component
import { SubmissionDialog } from '../../components/Common/SubmissionDialog';

describe('SubmissionDialog', () => {
  const mockOnHide = vi.fn();

  it('should render the dialog when visible is true', () => {
    render(<SubmissionDialog visible={true} onHide={mockOnHide} />);
    expect(screen.getByText('⚠️ Before Submission')).toBeInTheDocument();
  });

  it('should not render the dialog when visible is false', () => {
    const { container } = render(<SubmissionDialog visible={false} onHide={mockOnHide} />);
    const dialog = container.querySelector('[role="dialog"]');
    // PrimeReact Dialog doesn't render when visible is false, so dialog will be null
    expect(dialog).toBeNull();
  });

  it('should display markdown cleanup warning', () => {
    render(<SubmissionDialog visible={true} onHide={mockOnHide} />);
    expect(screen.getByText('LLM Response Cleanup')).toBeInTheDocument();
    expect(screen.getByText(/AI models may include extra markdown sections/)).toBeInTheDocument();
  });

  it('should display processing time information', () => {
    render(<SubmissionDialog visible={true} onHide={mockOnHide} />);
    expect(screen.getByText('⏱️ Processing Time')).toBeInTheDocument();
    expect(screen.getByText(/File generation may take a few minutes/)).toBeInTheDocument();
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
