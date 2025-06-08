import { render, fireEvent, screen } from "@testing-library/react";
import { describe, it, expect, vi } from "vitest";
import FileUpload from "./FileUpload";

// Mock alert function
global.alert = vi.fn();

describe("FileUpload", () => {
  it("should validate file size and type before adding to state", () => {
    // Create proper File objects for testing
    const validFile = new File(["test content"], "test.txt", {
      type: "text/plain",
      lastModified: Date.now(),
    });

    const invalidFile = new File(["large content"], "large_file.jpg", {
      type: "image/jpeg",
      lastModified: Date.now(),
    });

    // Mock the size property for the invalid file to exceed limit (>10MB)
    Object.defineProperty(invalidFile, "size", {
      value: 15 * 1024 * 1024, // 15MB - exceeds the 10MB limit
      writable: false,
    });

    const mockOnChange = vi.fn();
    const mockOnCancel = vi.fn();

    // Render the component with required props
    render(
      <FileUpload
        onChange={mockOnChange}
        onCancel={mockOnCancel}
        width="300px"
        multiple={true}
      />
    );

    // Find the file input by its type since there's no test-id
    const fileInput = document.querySelector(
      'input[type="file"]'
    ) as HTMLInputElement;

    expect(fileInput).toBeInTheDocument();

    // Create a proper FileList-like object
    const mockFileList = {
      0: validFile,
      1: invalidFile,
      length: 2,
      item: (index: number) => (index === 0 ? validFile : invalidFile),
    } as FileList;

    // Trigger the file input change event
    fireEvent.change(fileInput, {
      target: { files: mockFileList },
    });

    // Check that alert was called for the invalid file
    expect(global.alert).toHaveBeenCalledWith(
      "File large_file.jpg exceeds 10MB limit"
    );

    // Check that onChange was called with only the valid file
    expect(mockOnChange).toHaveBeenCalledWith([validFile]);

    // Check that the valid file is displayed in the selected files list
    expect(screen.getByText("Selected Files:")).toBeInTheDocument();

    // Check that the file list contains the valid file
    const fileListElement = screen.getByRole("list");
    expect(fileListElement).toHaveTextContent("test.txt");

    // Check that the invalid file is not in the list
    expect(fileListElement).not.toHaveTextContent("large_file.jpg");
  });

  it("should clear files when clear button is clicked", () => {
    const validFile = new File(["test content"], "test.txt", {
      type: "text/plain",
      lastModified: Date.now(),
    });

    const mockOnChange = vi.fn();

    render(
      <FileUpload
        onChange={mockOnChange}
        width="300px"
      />
    );

    const fileInput = document.querySelector(
      'input[type="file"]'
    ) as HTMLInputElement;

    // Add a file
    fireEvent.change(fileInput, {
      target: { files: [validFile] },
    });

    // Verify file is displayed
    expect(screen.getByText("Selected Files:")).toBeInTheDocument();

    // Check that the file list contains the file
    const fileListElement = screen.getByRole("list");
    expect(fileListElement).toHaveTextContent("test.txt");

    // Click clear button
    const clearButton = screen.getByText("Clear Files");
    fireEvent.click(clearButton);

    // Verify files are cleared
    expect(screen.queryByText("Selected Files:")).not.toBeInTheDocument();
    expect(screen.queryByRole("list")).not.toBeInTheDocument();
  });
});
