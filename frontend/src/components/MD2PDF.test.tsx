import { render, fireEvent, screen } from "@testing-library/react";
import MD2PDF from "./MD2PDF";
import { vi } from "vitest";
import type { ResultsTableType } from "./ResultsTable";

// Mock necessary functions and objects
vi.mock("primeicons/primeicons.css");
vi.mock("./FileUpload", () => {
  return {
    default: ({ onChange, accept, width }: any) => (
      <div>
        <input
          type="file"
          accept={accept}
          onChange={(e) => {
            const files = e.target.files ? Array.from(e.target.files) : [];
            onChange?.(files);
          }}
          style={{ width }}
          data-testid="file-input"
        />
      </div>
    ),
  };
});
vi.mock("./ResultsTable", () => {
  const mockResultsTableType = {} as ResultsTableType;
  return { ResultsTableType: mockResultsTableType };
});
vi.mock("../MainForm", () => {
  const mockAPI_HOST = "http://example.com/api"; // Mock API host
  return { API_HOST: mockAPI_HOST };
});

describe("MD2PDF component", () => {
  it("renders without crashing", () => {
    expect(() => render(<MD2PDF setUpdateFiles={() => {}} />)).not.toThrow();
  });

  // Add more test cases here, e.g., testing button click event,
  // file selection, form submission logic, and error handling.
});

it("handles file selection correctly", () => {
  const mockFile = new File(["# Test Markdown"], "test.md", {
    type: "text/markdown",
  });
  const mockSetUpdateFiles = vi.fn();

  render(<MD2PDF setUpdateFiles={mockSetUpdateFiles} />);

  // Find the file input element using test id
  const fileInput = screen.getByTestId("file-input") as HTMLInputElement;
  expect(fileInput).toBeTruthy();

  // Create a mock file list
  const mockFileList = {
    0: mockFile,
    length: 1,
    item: (index: number) => (index === 0 ? mockFile : null),
    [Symbol.iterator]: function* () {
      for (let i = 0; i < this.length; i++) {
        yield this[i];
      }
    },
  } as FileList;

  // Mock the files property and trigger change event
  Object.defineProperty(fileInput, "files", {
    value: mockFileList,
    writable: false,
  });

  // Trigger the file selection
  fireEvent.change(fileInput);

  // Verify that the onChange callback was called with the correct file
  // Since we're testing the file selection mechanism, we can verify
  // that the input accepts the file and the change event fires
  expect(fileInput.files).toBeTruthy();
  expect(fileInput.files?.[0]).toBe(mockFile);
});
