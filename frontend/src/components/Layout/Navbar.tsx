import React from 'react';
import { Menubar } from 'primereact/menubar';
import { Button } from 'primereact/button';
import { useTheme } from '../../hooks/useTheme';

export const Navbar: React.FC = () => {
  const { isDark, toggleTheme } = useTheme();

  const start = (
    <div className="flex align-items-center">
      <i className="pi pi-file text-2xl mr-2" />
      <span className="font-bold text-xl">Resume Optimizer</span>
    </div>
  );

  const end = (
    <Button
      icon={isDark ? 'pi pi-sun' : 'pi pi-moon'}
      rounded
      text
      severity={isDark ? 'warning' : 'info'}
      onClick={toggleTheme}
      aria-label="Toggle theme"
      tooltip={isDark ? 'Switch to Light Mode' : 'Switch to Dark Mode'}
      tooltipOptions={{ position: 'bottom' }}
    />
  );

  return <Menubar start={start} end={end} className="border-round-none" />;
};
