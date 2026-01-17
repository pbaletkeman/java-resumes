import React from 'react';
import { Button } from 'primereact/button';
import { useTheme } from '../../hooks/useTheme';

export const Navbar: React.FC = () => {
  const { isDark, toggleTheme } = useTheme();

  const start = (
    <div className="flex align-items-center gap-3 flex-shrink-0">
      <div className="flex align-items-center gap-2">
        <i className="pi pi-file" style={{ fontSize: '1.25rem' }} />
        <span className="font-bold whitespace-nowrap" style={{ fontSize: '0.95rem' }}>
          Resume Optimizer
        </span>
      </div>
      <Button
        icon={isDark ? 'pi pi-sun' : 'pi pi-moon'}
        rounded
        text
        size="small"
        severity={isDark ? 'warning' : 'info'}
        onClick={toggleTheme}
        aria-label="Toggle theme"
        tooltip={isDark ? 'Switch to Light Mode' : 'Switch to Dark Mode'}
        tooltipOptions={{ position: 'bottom' }}
      />
    </div>
  );

  return (
    <div
      className="flex align-items-center w-full bg-surface-section px-4 py-2 border-bottom-1 surface-border"
      style={{ minHeight: '2.5rem' }}
    >
      {start}
    </div>
  );
};
