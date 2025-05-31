import "primeicons/primeicons.css";

import { Card } from "primereact/card";
import { Button } from "primereact/button";
import FileUpload from "./FileUpload";
import { useState } from "react";
import { Fieldset } from "primereact/fieldset";

export default function MD2PDF() {
  const [resetMDFile, setResetMDFile] = useState<boolean>(false);
  const [mdFile, setMDFile] = useState<File | null>();

  const handleFilesSelected = (files: File[]) => {
    if (files?.length > 0) {
      setMDFile(files[0]);
    } else {
      setMDFile(null);
    }
  };
  return (
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
            />
          </td>
        </tr>
      </table>
    </Card>
  );
}
