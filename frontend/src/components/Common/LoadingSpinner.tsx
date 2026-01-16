import React from 'react';
import { ProgressSpinner } from 'primereact/progressspinner';

interface LoadingSpinnerProps {
  message?: string;
  fullScreen?: boolean;
}

export const LoadingSpinner: React.FC<LoadingSpinnerProps> = ({
  message = 'Loading...',
  fullScreen = false,
}) => {
  const containerClass = fullScreen
    ? 'flex flex-col items-center justify-center h-screen'
    : 'flex flex-col items-center justify-center p-4';

  return (
    <div className={containerClass}>
      <ProgressSpinner />
      {message && <p className="mt-3 text-gray-600">{message}</p>}
    </div>
  );
};
