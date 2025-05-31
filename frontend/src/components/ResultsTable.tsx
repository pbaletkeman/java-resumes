import { useEffect, useState } from "react";
import "primeicons/primeicons.css";

import { Card } from "primereact/card";
import { Button } from "primereact/button";
import { ScrollPanel } from "primereact/scrollpanel";
import { API_HOST } from "./MainForm";

interface FileType {
  url: string;
  name: string;
  size: string;
  date: string;
}

export default function ResultsTable() {
  const [files, setFiles] = useState<FileType[] | null>(null);
  const [busyCursor, setBusyCursor] = useState<string>("cursor-wait");

  useEffect(() => {
    getFiles();
  }, []);

  function getFiles() {
    setBusyCursor("cursor-wait");
    fetch(API_HOST + "/get-files")
      .then((response) => response.json())
      .then((json) => setFiles(json))
      .catch((error) => console.error(error));
    setBusyCursor("cursor-auto");
  }

  function handleDelete(fileName: string) {
    setBusyCursor("cursor-wait");
    fetch(API_HOST + "/files/" + fileName, { method: "DELETE" });
    setTimeout(getFiles, 2000);
  }

  setInterval(() => {
    getFiles();
  }, 30000);

  const header = (
    <table
      width="100%"
      className={busyCursor}
    >
      <tr>
        <td className="text-3xl">Files: {files?.length}</td>
        <td
          colSpan={11}
          align="right"
        >
          <Button
            label="Refresh"
            icon="pi pi-refresh"
            iconPos="right"
            className="border-round-xl"
            onClick={getFiles}
          />
        </td>
      </tr>
    </table>
  );

  return (
    <Card className={"border-round-3xl " + busyCursor}>
      {header}
      <ScrollPanel style={{ width: "100%", height: "41vh" }}>
        <div className="grid pl-2 pt-2">
          {files?.flatMap((x) => (
            <>
              <div className="col-1 border-right-1 border-y-1">
                <Button
                  icon="pi pi-times"
                  tooltip={"Delete " + x.name}
                  className="border-circle border-2"
                  onClick={() => handleDelete(x.name)}
                />
              </div>
              <div className="col-10 border-1 border-x-none">
                <a href={x.url}>{x.name}</a> ({x.date + ") - " + x.size}
                <br />
                <a href={x.url}>{x.url}</a>
              </div>
              <div className="col-1 border-1 border-x-none"></div>
            </>
          ))}
        </div>
      </ScrollPanel>
    </Card>
  );
}
