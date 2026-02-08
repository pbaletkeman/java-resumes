import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { Navbar } from '../../components/Layout/Navbar';
import { ThemeProvider } from '../../context/ThemeContext';

// Mock the PrimeReact Button to simplify testing
vi.mock('primereact/button', () => ({
  Button: ({ icon, onClick, 'aria-label': ariaLabel }: any) => (
    <button onClick={onClick} aria-label={ariaLabel}>
      <i className={icon} />
    </button>
  ),
}));

describe('Navbar', () => {
  it('should render the navbar with title', () => {
    render(
      <ThemeProvider>
        <Navbar />
      </ThemeProvider>
    );

    expect(screen.getByText('Resume Optimizer')).toBeInTheDocument();
  });

  it('should render the file icon', () => {
    const { container } = render(
      <ThemeProvider>
        <Navbar />
      </ThemeProvider>
    );

    const icon = container.querySelector('.pi-file');
    expect(icon).toBeInTheDocument();
  });

  it('should render theme toggle button', () => {
    render(
      <ThemeProvider>
        <Navbar />
      </ThemeProvider>
    );

    const themeButton = screen.getByRole('button', { name: /toggle theme/i });
    expect(themeButton).toBeInTheDocument();
  });

  it('should toggle theme when button is clicked', async () => {
    const user = userEvent.setup();
    const { container } = render(
      <ThemeProvider>
        <Navbar />
      </ThemeProvider>
    );

    const themeButton = screen.getByRole('button', { name: /toggle theme/i });
    
    // Initially should have moon icon (light mode)
    expect(container.querySelector('.pi-moon')).toBeInTheDocument();

    // Click to toggle
    await user.click(themeButton);

    // After toggle, should have sun icon (dark mode)
    expect(container.querySelector('.pi-sun')).toBeInTheDocument();
  });
});
