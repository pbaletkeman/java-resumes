import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import { ConfirmDialog } from '../../src/components/Common/ConfirmDialog';

describe('ConfirmDialog', () => {
  it('renders when visible', () => {
    render(
      <ConfirmDialog
        visible={true}
        onHide={vi.fn()}
        onConfirm={vi.fn()}
        message="Are you sure?"
      />
    );
    expect(screen.getByText('Are you sure?')).toBeInTheDocument();
  });

  it('calls onConfirm when confirm button is clicked', () => {
    const onConfirm = vi.fn();
    render(
      <ConfirmDialog
        visible={true}
        onHide={vi.fn()}
        onConfirm={onConfirm}
        message="Are you sure?"
      />
    );
    const confirmButton = screen.getByText('Confirm');
    fireEvent.click(confirmButton);
    expect(onConfirm).toHaveBeenCalled();
  });
});
