import { useEffect, useRef, useState } from "react";
import "primeicons/primeicons.css";
import { Toast } from "primereact/toast";
import { Card } from "primereact/card";
import { Button } from "primereact/button";
import { ScrollPanel } from "primereact/scrollpanel";
import { API_HOST } from "./MainForm";
import { ConfirmDialog, confirmDialog } from "primereact/confirmdialog";

interface FileType {
  url: string;
  name: string;
  size: string;
  date: string;
}

export interface ResultsTableType {
  updateFiles?: boolean;
  setUpdateFiles: (b: boolean) => void;
}

export default function ResultsTable({
  updateFiles,
  setUpdateFiles,
}: ResultsTableType) {
  const toast = useRef<Toast>(null);
  const [files, setFiles] = useState<FileType[] | null>(null);
  const [busyCursor, setBusyCursor] = useState<string>("cursor-wait");

  let deleteFile = "";

  useEffect(() => {
    getFiles(false); // initial page load
  }, []);

  useEffect(() => {
    if (updateFiles) {
      getFiles(true);
      setUpdateFiles(false);
    }
    setInterval(() => {
      // refresh file listing every 30 seconds
      getFiles(false);
    }, 30000);
  }, [updateFiles, setUpdateFiles]);

  function getFiles(showToast: boolean) {
    if (showToast) {
      setBusyCursor("cursor-wait");
      toast?.current?.show({
        severity: "info",
        summary: "Refreshing",
        detail: "Refreshing File Listing...",
        life: 3000,
      });
    }

    fetch(API_HOST + "/get-files")
      .then((response) => response.json())
      .then((json) => {
        setFiles(json);
      })
      .catch((error) => {
        console.error(error);
      });
    setBusyCursor("cursor-auto");
  }

  function handleDelete(fileName: string) {
    setBusyCursor("cursor-wait");
    fetch(API_HOST + "/files/" + fileName, { method: "DELETE" });
    setInterval(() => {
      // wait 1.5 seconds and refresh file listing
      getFiles(false);
    }, 1500);
    setBusyCursor("cursor-auto");
  }

  const accept = () => {
    handleDelete(deleteFile);
    toast?.current?.show({
      severity: "info",
      summary: "Confirmed",
      detail: "Deleting File",
      life: 3000,
    });
    setUpdateFiles(false);
  };

  const reject = () => {
    console.log("cancel pressed, do nothing");
  };

  const deleteFileConfirm = (fileName: string) => {
    deleteFile = fileName;
    confirmDialog({
      message: "Delete file '" + fileName + "'?",
      header: "Delete Confirmation",
      icon: "pi pi-info-circle",
      defaultFocus: "reject",
      acceptClassName: "p-button-danger",
      accept,
      reject,
    });
  };

  const header = (
    <table
      width="100%"
      className={busyCursor}
    >
      <tr>
        <td className="text-3xl">
          Files: {files && files.length ? files.length : 0}
        </td>
        <td className="card flex justify-content-center"></td>
        <td align="right">
          <Button
            label="Refresh"
            icon="pi pi-refresh"
            iconPos="right"
            className="border-round-xl mb-2 mt-2"
            onClick={() => {
              setUpdateFiles(true);
            }}
          />
        </td>
      </tr>
    </table>
  );

  return (
    <>
      <Toast ref={toast} />
      <ConfirmDialog />
      <Card className={"border-round-3xl " + busyCursor}>
        {header}
        <ScrollPanel style={{ width: "100%", height: "41vh" }}>
          <div className="grid mt-2 ml-2">
            {files?.flatMap((x) => (
              <div
                className="grid mb-2 border-top-1"
                style={{ width: "100%" }}
              >
                <div className="col-1">
                  <Button
                    icon="pi pi-times"
                    tooltip={"Delete " + x.name}
                    className="border-circle"
                    onClick={() => deleteFileConfirm(x.name)}
                  />
                </div>
                <div className="col-11 border-left-1 border-y-none p-1">
                  <div className="flex m-0 p-0">
                    <div className="flex">
                      <a
                        href={x.url}
                        className="text-sm"
                      >
                        {x.name}
                      </a>
                    </div>
                    <div className="flex text-xs font-bold ml-1">
                      ({x.date + ") - " + x.size}
                    </div>
                  </div>
                  <div className="text-xs">
                    <a href={x.url}>{x.url}</a>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </ScrollPanel>
      </Card>
    </>
  );
}
