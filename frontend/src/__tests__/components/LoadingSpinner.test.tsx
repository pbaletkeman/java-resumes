import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import { LoadingSpinner } from '../../components/Common/LoadingSpinner';

describe('LoadingSpinner', () => {
  it('should render with default message', () => {
    render(<LoadingSpinner />);
    
    expect(screen.getByText(/loading/i)).toBeInTheDocument();
  });

  it('should render with custom message', () => {
    const customMessage = 'Processing your request...';
    render(<LoadingSpinner message={customMessage} />);
    
    expect(screen.getByText(customMessage)).toBeInTheDocument();
  });

  it('should apply default styling when fullScreen is false', () => {
    const { container } = render(<LoadingSpinner fullScreen={false} />);
    
    const mainDiv = container.firstChild as HTMLElement;
    expect(mainDiv.className).toContain('p-4');
    expect(mainDiv.className).not.toContain('h-screen');
  });

  it('should apply fullScreen styling when fullScreen is true', () => {
    const { container } = render(<LoadingSpinner fullScreen={true} />);
    
    const mainDiv = container.firstChild as HTMLElement;
    expect(mainDiv.className).toContain('h-screen');
  });

  it('should render message when provided', () => {
    render(<LoadingSpinner message="Test message" />);
    
    const message = screen.getByText('Test message');
    expect(message).toBeInTheDocument();
    expect(message.className).toContain('text-gray-600');
  });

  it('should render without message when empty string provided', () => {
    render(<LoadingSpinner message="" />);
    
    // Should not render any text
    const paragraph = document.querySelector('p');
    expect(paragraph).not.toBeInTheDocument();
  });
});
