import React, { useState, useEffect } from 'react';
import { Dialog } from 'primereact/dialog';

interface SubmissionDialogProps {
  visible: boolean;
  onHide: () => void;
}

export const SubmissionDialog: React.FC<SubmissionDialogProps> = ({ visible, onHide }) => {
  const [timeRemaining, setTimeRemaining] = useState(15);

  useEffect(() => {
    if (!visible) {
      setTimeRemaining(15);
      return;
    }

    // Start countdown timer
    const timer = setInterval(() => {
      setTimeRemaining(prev => {
        if (prev <= 1) {
          onHide();
          return 15;
        }
        return prev - 1;
      });
    }, 1000);

    return () => clearInterval(timer);
  }, [visible, onHide]);

  return (
    <Dialog
      header="⚠️ Before Submission"
      visible={visible}
      onHide={onHide}
      modal
      style={{ width: '90vw', maxWidth: '450px' }}
      closeOnEscape
      closable
    >
      <div className="space-y-3">
        <p className="text-sm font-medium text-surface-700 dark:text-surface-300">
          Review content before downloading
        </p>
        <div>
          <p>&nbsp;</p>
        </div>
        <div>
          <p className="font-semibold text-sm mb-1">LLM Response Cleanup</p>
          <p className="text-xs text-surface-600 dark:text-surface-400">
            AI models may include extra markdown sections, decorative formatting, or incomplete
            content. Review your file before downloading to ensure quality.
          </p>
        </div>
        <div>
          <p>&nbsp;</p>
        </div>
        <div>
          <p className="font-semibold text-sm mb-1">⏱️ Processing Time</p>
          <p className="text-xs text-surface-600 dark:text-surface-400">
            File generation may take a few minutes. Check the Files tab once complete.
          </p>
        </div>

        <div className="pt-2 border-t border-surface-200 dark:border-surface-700 text-center">
          <p className="text-xs text-surface-500 dark:text-surface-500 italic">
            Auto-closes in {timeRemaining}s
          </p>
        </div>
      </div>
    </Dialog>
  );
};
