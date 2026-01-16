import React from 'react';
import type { ReactNode } from 'react';
import { Navbar } from './Navbar';
import { FileHistory } from './FileHistory';

interface MainLayoutProps {
  children: ReactNode;
}

export const MainLayout: React.FC<MainLayoutProps> = ({ children }) => {
  return (
    <div className="min-h-screen flex flex-column">
      <Navbar />
      <div className="flex-1 flex">
        <div className="flex-1 p-3">{children}</div>
        <div className="w-full md:w-4 lg:w-3 p-3 border-left-1 surface-border overflow-y-auto">
          <FileHistory />
        </div>
      </div>
    </div>
  );
};
