import { useState } from "react";
import "primeicons/primeicons.css";

import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { Filedata } from "./FileData";
import { Card } from "primereact/card";
import { Button } from "primereact/button";
import { Fieldset } from "primereact/fieldset";

interface FileType {
  url: string;
  name: string;
  size: string;
}

export default function ResultsTable() {
  const [files, setFiles] = useState<FileType[]>(Filedata);

  const header = (
    <table width="100%">
      <tr>
        <td className="text-3xl">Files</td>
        <td
          colSpan={11}
          align="right"
        >
          <Button
            label="Refresh"
            icon="pi pi-refresh"
            iconPos="right"
            className="border-round-xl"
          />
        </td>
      </tr>
    </table>
  );

  return (
    <Card className="border-round-3xl">
      <DataTable
        value={files}
        size="small"
        header={header}
      >
        <Column
          field="name"
          header="name"
        ></Column>
        <Column
          field="size"
          header="size"
        ></Column>
        <Column
          field="url"
          header="url"
        ></Column>
      </DataTable>
    </Card>
  );
}
