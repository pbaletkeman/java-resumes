import React from 'react';
import { ThemeProvider } from './context/ThemeContext';
import { AppProvider } from './context/AppContext';
import { ErrorBoundary } from './components/Common/ErrorBoundary';
import { MainLayout } from './components/Layout/MainLayout';
import { HomePage } from './pages/HomePage';

const App: React.FC = () => {
  return (
    <ErrorBoundary>
      <ThemeProvider>
        <AppProvider>
          <MainLayout>
            <HomePage />
          </MainLayout>
        </AppProvider>
      </ThemeProvider>
    </ErrorBoundary>
  );
};

export default App;
