import React, { useCallback, useEffect, useRef, useState } from "react";

interface FileUploadProps {
  accept?: string; // e.g., "image/*", "application/pdf" - Optional file type filter
  onChange?: (files: File[]) => void; // Callback function when files are selected
  onCancel?: () => void; // Callback for cancel button
  multiple?: boolean;
  resetValue: boolean;
  setResetValue: (b: boolean) => void;
  width: string;
}

const FileUpload: React.FC<FileUploadProps> = ({
  accept,
  onChange,
  onCancel,
  multiple,
  resetValue,
  setResetValue,
  width,
}) => {
  const [selectedFiles, setSelectedFiles] = useState<File[]>([]);

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const files: File[] = event.target.files
      ? Array.from(event.target.files)
      : []; // Convert FileList to an array for easier handling

    setSelectedFiles(multiple ? [...selectedFiles, ...files] : [...files]); // Add new files to the state

    if (files != undefined && onChange != undefined) {
      onChange(files);
    }
  };

  const input = {
    width: width,
    padding: "8px",
    border: "1px solid #ccc",
    borderRadius: "4px",
    fontSize: "14px",
    boxShadow: "0px 0px 10px 0px grey",
  };
  console.log("input");
  console.log(input);

  // Ref object to reference the input element
  const inputFile = useRef<HTMLInputElement | null>(null);

  // Function to reset the input element
  const handleReset = () => {
    if (inputFile.current) {
      inputFile.current.value = "";
      inputFile.current.type = "text";
      inputFile.current.type = "file";
    }
  };

  const handleClearFile = useCallback(() => {
    setSelectedFiles([]);
    handleReset();
  });

  useEffect(() => {
    if (resetValue) {
      handleClearFile();
      setResetValue(false);
    }
  }, [handleClearFile, resetValue, setResetValue]);

  return (
    <div>
      <input
        type="file"
        onChange={handleFileChange}
        accept={accept}
        multiple={multiple ? multiple : false} // Allows multiple file selection
        style={input}
        ref={inputFile}
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
};

export default FileUpload;
