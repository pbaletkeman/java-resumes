import React, { Component } from 'react';
import type { ReactNode } from 'react';
import { Message } from 'primereact/message';

interface Props {
  children: ReactNode;
}

interface State {
  hasError: boolean;
  error: Error | null;
}

export class ErrorBoundary extends Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error };
  }

  componentDidCatch(error: Error, errorInfo: React.ErrorInfo): void {
    console.error('ErrorBoundary caught an error:', error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      return (
        <div className="flex justify-center items-center h-screen p-4">
          <Message
            severity="error"
            text={`An unexpected error occurred: ${this.state.error?.message || 'Unknown error'}`}
          />
        </div>
      );
    }

    return this.props.children;
  }
}
