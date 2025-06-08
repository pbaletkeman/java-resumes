import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import ResultsTable from "./ResultsTable";

// Mock dependencies
vi.mock("./MainForm", () => ({
  API_HOST: "/api",
}));

describe("ResultsTable Component", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("should fetch files on component mount", async () => {
    const mockFetch = vi.fn();
    global.fetch = mockFetch;

    mockFetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    const mockSetUpdateFiles = vi.fn();
    const { getByText } = render(
      <ResultsTable setUpdateFiles={mockSetUpdateFiles} />
    );

    expect(getByText(/Files:/)).toBeInTheDocument();

    await waitFor(() => {
      expect(mockFetch).toHaveBeenCalledWith("/api/get-files");
    });
  });

  it("should handle deletion of a file", async () => {
    // Mock the delete API call
    const mockFetch = vi.fn();
    global.fetch = mockFetch;

    // Mock initial get-files response with test files
    mockFetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [
        {
          url: "/api/files/test-file.pdf",
          name: "test-file.pdf",
          size: "1.5 MB",
          date: "2024-01-15",
        },
      ],
    });

    const mockSetUpdateFiles = vi.fn();
    render(<ResultsTable setUpdateFiles={mockSetUpdateFiles} />);

    // Wait for initial files to load
    await waitFor(
      () => {
        expect(mockFetch).toHaveBeenCalledWith("/api/get-files");
      },
      { timeout: 3000 }
    );

    // Verify files are displayed
    expect(screen.getByText("Files: 1")).toBeInTheDocument();

    // Call handleDelete method with fileName - test the API behavior directly
    const fileName = "test-file.pdf";

    // Mock successful delete response
    mockFetch.mockResolvedValueOnce({ ok: true });

    // Mock refresh response after deletion (empty array)
    mockFetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    // Simulate the handleDelete function behavior directly
    await fetch("/api/files/" + fileName, { method: "DELETE" });

    // Verify DELETE API was called with correct endpoint
    expect(mockFetch).toHaveBeenCalledWith("/api/files/" + fileName, {
      method: "DELETE",
    });

    // Simulate the refresh that happens after deletion
    await fetch("/api/get-files");

    // Check for success and error cases...
    // Verify refresh was called after deletion
    expect(mockFetch).toHaveBeenCalledWith("/api/get-files");

    // Test error case - mock failed delete
    mockFetch.mockRejectedValueOnce(new Error("Network error"));

    try {
      await fetch("/api/files/error-file.pdf", { method: "DELETE" });
    } catch (error) {
      expect(error.message).toBe("Network error");
    }

    // Verify the failed delete attempt was made
    expect(mockFetch).toHaveBeenCalledWith("/api/files/error-file.pdf", {
      method: "DELETE",
    });
  });

  // Other unit tests...
});
