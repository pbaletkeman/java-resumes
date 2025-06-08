import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom";
import { Button } from "primereact/button";
import { InputText } from "primereact/inputtext";
import { InputNumber } from "primereact/inputnumber";
import { Checkbox } from "primereact/checkbox";
import { Accordion, AccordionTab } from "primereact/accordion";
import { FileUpload } from "primereact/fileupload";

// Mock PrimeReact components
vi.mock("primereact/button", () => ({
  Button: ({ label, onClick, ...props }: any) => (
    <button
      onClick={onClick}
      {...props}
    >
      {label}
    </button>
  ),
}));

vi.mock("primereact/inputtext", () => ({
  InputText: ({ value, onChange, ...props }: any) => (
    <input
      type="text"
      value={value}
      onChange={onChange}
      {...props}
    />
  ),
}));

vi.mock("primereact/inputnumber", () => ({
  InputNumber: ({ value, onValueChange, ...props }: any) => (
    <input
      type="number"
      value={value}
      onChange={(e) =>
        onValueChange && onValueChange({ value: parseFloat(e.target.value) })
      }
      {...props}
    />
  ),
}));

vi.mock("primereact/checkbox", () => ({
  Checkbox: ({ checked, onChange, ...props }: any) => (
    <input
      type="checkbox"
      checked={checked}
      onChange={onChange}
      {...props}
    />
  ),
}));

vi.mock("primereact/accordion", () => ({
  Accordion: ({ children, ...props }: any) => <div {...props}>{children}</div>,
  AccordionTab: ({ header, children, ...props }: any) => (
    <div {...props}>
      <div>{header}</div>
      <div>{children}</div>
    </div>
  ),
}));

vi.mock("primereact/fileupload", () => ({
  FileUpload: ({ onSelect, ...props }: any) => (
    <input
      type="file"
      onChange={(e) => onSelect && onSelect({ files: e.target.files })}
      {...props}
    />
  ),
}));

// Define types for props
type ButtonProps = {
  label: string;
  onClick?: () => void;
};

type InputTextProps = {
  value: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  placeholder?: string;
};

type InputNumberProps = {
  value: number;
  onValueChange?: (e: { value: number }) => void;
  min?: number;
  max?: number;
  step?: number;
  minFractionDigits?: number;
};

type CheckboxProps = {
  checked: boolean;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
};

// Test individual components
describe("MainForm Components", () => {
  const mockModel = "gemma-3-4b-it";
  const mockJobTitle = "Software Developer";
  const mockCompany = "Tech Corp";
  const temperature = 0.2;

  // Mock file uploads
  const mockResumeFile = new File(["mock resume content"], "resume.pdf", {
    type: "application/pdf",
  });
  const mockJobFile = new File(["mock job description"], "job.txt", {
    type: "text/plain",
  });

  // Test InputText component
  it("should render InputText with correct value", () => {
    const mockOnChange = vi.fn();
    render(
      <InputText
        value={mockModel}
        onChange={mockOnChange}
        placeholder="Enter model name"
      />
    );

    const input = screen.getByDisplayValue(mockModel);
    expect(input).toBeInTheDocument();
    expect(input).toHaveAttribute("placeholder", "Enter model name");
  });

  // Test InputNumber component
  it("should render InputNumber with correct value", () => {
    const mockOnValueChange = vi.fn();
    render(
      <InputNumber
        value={temperature}
        onValueChange={mockOnValueChange}
        minFractionDigits={2}
        max={1.99}
        min={0.01}
        step={0.01}
      />
    );

    const input = screen.getByDisplayValue("0.2");
    expect(input).toBeInTheDocument();
    expect(input).toHaveAttribute("type", "number");
  });

  // Test Checkbox component
  it("should render Checkbox with correct checked state", () => {
    const mockOnChange = vi.fn();
    const mockPrompt = ["Resume"];

    render(
      <div>
        <Checkbox
          checked={mockPrompt.includes("Resume")}
          onChange={mockOnChange}
        />
        <label>Resume</label>
      </div>
    );

    const checkbox = screen.getByRole("checkbox");
    expect(checkbox).toBeInTheDocument();
    expect(checkbox).toBeChecked();
  });

  // Test FileUpload component
  it("should render FileUpload component", () => {
    const mockOnSelect = vi.fn();
    const { container } = render(
      <FileUpload
        onSelect={mockOnSelect}
        accept=".pdf,.txt"
      />
    );

    const fileInput = container.querySelector('input[type="file"]');
    expect(fileInput).toBeInTheDocument();
    expect(fileInput).toHaveAttribute("type", "file");
    expect(fileInput).toHaveAttribute("accept", ".pdf,.txt");
  });

  // Test Accordion component
  it("should render Accordion with AccordionTab", () => {
    render(
      <Accordion>
        <AccordionTab header="Options">
          <p>Accordion content</p>
        </AccordionTab>
      </Accordion>
    );

    expect(screen.getByText("Options")).toBeInTheDocument();
    expect(screen.getByText("Accordion content")).toBeInTheDocument();
  });

  // Test Button component
  it("should render Button and handle click events", () => {
    const mockSubmitHandler = vi.fn();
    render(
      <Button
        label="Generate Files"
        onClick={mockSubmitHandler}
      />
    );

    const button = screen.getByRole("button", { name: "Generate Files" });
    expect(button).toBeInTheDocument();

    fireEvent.click(button);
    expect(mockSubmitHandler).toHaveBeenCalledTimes(1);
  });

  // Test input interactions
  it("should handle input text changes", () => {
    const mockOnChange = vi.fn();
    render(
      <InputText
        value=""
        onChange={mockOnChange}
        placeholder="Enter text"
      />
    );

    const input = screen.getByPlaceholderText("Enter text");
    fireEvent.change(input, { target: { value: "new value" } });
    expect(mockOnChange).toHaveBeenCalled();
  });

  // Test number input interactions
  it("should handle number input changes", () => {
    const mockOnValueChange = vi.fn();
    render(
      <InputNumber
        value={0}
        onValueChange={mockOnValueChange}
      />
    );

    const input = screen.getByDisplayValue("0");
    fireEvent.change(input, { target: { value: "0.5" } });
    expect(mockOnValueChange).toHaveBeenCalledWith({ value: 0.5 });
  });

  // Test checkbox interactions
  it("should handle checkbox changes", () => {
    const mockOnChange = vi.fn();
    render(
      <Checkbox
        checked={false}
        onChange={mockOnChange}
      />
    );

    const checkbox = screen.getByRole("checkbox");
    fireEvent.click(checkbox);
    expect(mockOnChange).toHaveBeenCalled();
  });
});
