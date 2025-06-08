import React, {
  forwardRef,
  useCallback,
  useImperativeHandle,
  useState,
} from "react";
interface FileUploadProps {
  accept?: string; // e.g., "image/*", "application/pdf" - Optional file type filter
  onChange?: (files: File[]) => void; // Callback function when files are selected
  onCancel?: () => void; // Callback for cancel button
  multiple?: boolean;
  width: string;
  resetValue?: boolean;
  setResetValue?: (b: boolean) => void;
}

interface FileUploadRef {
  reset: () => void;
}

const inputBaseStyle = {
  padding: "8px",
  border: "1px solid #ccc",
  borderRadius: "4px",
  fontSize: "14px",
  boxShadow: "0px 0px 10px 0px grey",
};

const FileUpload = forwardRef<FileUploadRef, FileUploadProps>(
  ({ accept, onChange, onCancel, multiple, width }, ref) => {
    const [selectedFiles, setSelectedFiles] = useState<File[]>([]);
    const [inputKey, setInputKey] = useState<number>(0);

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
      const files: File[] = event.target.files
        ? Array.from(event.target.files)
        : [];

      // Validate files before adding to state
      const validFiles = files.filter((file) => {
        // Check file size (e.g., 10MB limit)
        if (file.size > 10 * 1024 * 1024) {
          alert(`File ${file.name} exceeds 10MB limit`);
          return false;
        }

        // Validate against accept prop if provided
        if (accept && !accept.includes(file.type)) {
          alert(`File ${file.name} type not allowed`);
          return false;
        }

        return true;
      });

      setSelectedFiles([...validFiles]);

      if (validFiles.length > 0 && onChange) {
        onChange(validFiles);
      }
    };

    const inputStyle = { ...inputBaseStyle, width };

    const handleClearFile = useCallback(() => {
      setSelectedFiles([]);
      setInputKey((prev) => prev + 1);
    }, []);

    useImperativeHandle(ref, () => ({
      reset: handleClearFile,
    }));

    return (
      <div>
        <input
          key={inputKey}
          type="file"
          onChange={handleFileChange}
          accept={accept}
          multiple={multiple ? multiple : false} // Allows multiple file selection
          style={inputStyle}
          size={10}
        />

        {selectedFiles.length > 0 && (
          <div>
            <h3>Selected Files:</h3>
            <ul>
              {selectedFiles.map((file, index) => (
                <li key={index}>
                  <strong>Name:</strong> {file.name} <br />
                  <strong>Type:</strong>{" "}
                  {file.name && file.name.toLowerCase().endsWith("md")
                    ? "text/markdown"
                    : file.type}
                  <br />
                  <strong>Size:</strong> {(file.size / 1024).toFixed(2)} KB
                </li>
              ))}
            </ul>
            <button onClick={handleClearFile}>Clear Files</button>
          </div>
        )}

        {onCancel && <button onClick={onCancel}>Cancel</button>}
      </div>
    );
  }
);

export default FileUpload;
