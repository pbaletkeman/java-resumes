import "./App.css";
import MainForm from "./components/MainForm";
import ResultsTable from "./components/ResultsTable";
import MD2PDF from "./components/MD2PDF";

function App() {
  return (
    <div className="grid">
      <div className="col-7">
        <MainForm />
      </div>
      <div className="col-5">
        <ResultsTable />
        <br />
        <MD2PDF />
      </div>
    </div>
  );
}

export default App;
