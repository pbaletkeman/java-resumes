import React from 'react';
import type { ReactNode } from 'react';
import { Navbar } from './Navbar';
import { FileHistory } from './FileHistory';

interface MainLayoutProps {
  children: ReactNode;
}

export const MainLayout: React.FC<MainLayoutProps> = ({ children }) => {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', height: '100vh', width: '100%' }}>
      <Navbar />
      <div style={{ display: 'flex', flex: 1, minHeight: 0, overflow: 'hidden' }}>
        <div style={{ flex: 1, padding: '1rem', overflowY: 'auto', overflowX: 'hidden' }}>
          {children}
        </div>
        <div
          style={{
            width: '500px',
            padding: '1rem',
            borderLeft: '1px solid var(--surface-border)',
            overflowY: 'auto',
          }}
        >
          <FileHistory />
        </div>
      </div>
    </div>
  );
};
