import "primeicons/primeicons.css";

import { Card } from "primereact/card";
import { Button } from "primereact/button";
import FileUpload from "./FileUpload";
import { useRef, useState } from "react";
import { API_HOST } from "./MainForm";
import type { ResultsTableType } from "./ResultsTable";
import { Toast } from "primereact/toast";

export default function MD2PDF({ setUpdateFiles }: ResultsTableType) {
  const [resetMDFile, setResetMDFile] = useState<boolean>(false);
  const [mdFile, setMDFile] = useState<File | null>();
  const toast = useRef<Toast>(null);
  const [showToast, setShowToast] = useState<boolean>(false);

  const handleFilesSelected = (files: File[]) => {
    if (files?.length > 0) {
      setMDFile(files[0]);
    } else {
      setMDFile(null);
    }
  };

  const handleFormSubmit = async () => {
    setShowToast(true);
    if (mdFile && mdFile.name) {
      const formData = new FormData();
      formData.append("file", mdFile, mdFile.name);

      const response = await fetch(API_HOST + "/markdownFile2PDF", {
        method: "POST",
        body: formData,
      });

      if (response.ok) {
        console.log("Form submitted successfully!");
        alert("Form submitted successfully!"); // Optional feedback to the user
        setResetMDFile(true);
        setUpdateFiles(true);
      } else {
        console.error(
          "Error submitting form:",
          response.status,
          response.statusText
        );
        alert(`Error submitting form: ${response.statusText}`); // Provide more informative error feedback
      }
    }
  };

  if (showToast) {
    toast?.current?.show({
      severity: "success",
      summary: "Converting File",
      detail: "Markdown To PDF In Progress...",
      life: 3000,
    });
    setShowToast(false);
  }

  return (
    <>
      <Toast ref={toast} />
      <Card className="border-round-3xl">
        <table width={"100%"}>
          <tr>
            <td
              className="p-0 m-0 text-3xl"
              colSpan={2}
            >
              <p className="m-2">Markdown To PDF &nbsp;</p>
            </td>
          </tr>
          <tr>
            <td width="75%">
              <div className="mb-2 ml-0 mr-0">Markdown File</div>
              <FileUpload
                resetValue={resetMDFile}
                setResetValue={setResetMDFile}
                accept="text/markdown, text/plain" // Optional: Filter file types
                onChange={(files) => handleFilesSelected(files)} // Callback function
                width="55vh"
              />
            </td>
            <td valign="bottom">
              <Button
                icon="pi pi-file-export"
                iconPos="right"
                label="Convert"
                className="mb-0 border-round-xl"
                onClick={handleFormSubmit}
              />
            </td>
          </tr>
        </table>
      </Card>
    </>
  );
}
