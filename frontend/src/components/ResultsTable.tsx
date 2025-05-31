import { useEffect, useState } from "react";
import "primeicons/primeicons.css";

import { Card } from "primereact/card";
import { Button } from "primereact/button";
import { ScrollPanel } from "primereact/scrollpanel";

interface FileType {
  url: string;
  name: string;
  size: string;
  date: string;
}

export default function ResultsTable() {
  const [files, setFiles] = useState<FileType[] | null>(null);

  useEffect(() => {
    fetch("http://localhost:8080/get-files")
      .then((response) => response.json())
      .then((json) => setFiles(json))
      .catch((error) => console.error(error));
  }, []);

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
      {header}
      <ScrollPanel style={{ width: "100%", height: "41vh" }}>
        <ul>
          {files?.flatMap((x) => (
            <li className="m-2">
              <p>
                <a href={x.url}>{x.name}</a> ({x.date + ") - " + x.size}
                <br />
                <a href={x.url}>{x.url}</a>
              </p>
            </li>
          ))}
        </ul>
      </ScrollPanel>
    </Card>
  );
}
