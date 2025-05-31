import { useState } from "react";
import "primeicons/primeicons.css";

import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { Filedata } from "./FileData";
import { Card } from "primereact/card";
import { Button } from "primereact/button";

interface FileType {
  url: string;
  name: string;
  size: string;
}

export default function ResultsTable() {
  const [files, setFiles] = useState<FileType[]>(Filedata);

  const header = (
    <div className="grid">
      <div className="col-5 text-3xl">Files</div>
      <div className="col-7 absolute right-0 bg-primary flex align-items-right justify-content-end">
        <Button
          label="Refresh"
          icon="pi pi-refresh"
        />
      </div>
    </div>
  );

  return (
    <Card className="border-round-3xl">
      <DataTable
        value={files}
        tableStyle={{ minWidth: "50rem" }}
        header={header}
        size="small"
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
