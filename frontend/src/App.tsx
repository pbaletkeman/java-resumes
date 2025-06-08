import "./App.css";
import MainForm from "./components/MainForm";
import ResultsTable from "./components/ResultsTable";
import MD2PDF from "./components/MD2PDF";
import { useState } from "react";

function App() {
  const [updateFiles, setUpdateFiles] = useState<boolean>(false);
  return (
    // <BasicDemo />
    <div className="grid">
      <div className="col-7">
        <MainForm />
      </div>
      <div className="col-5">
        <ResultsTable
          updateFiles={updateFiles}
          setUpdateFiles={setUpdateFiles}
        />
        <br />
        <MD2PDF
          updateFiles={updateFiles}
          setUpdateFiles={setUpdateFiles}
        />
      </div>
    </div>
  );
}

export default App;
