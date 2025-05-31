import "./App.css";
import MainForm from "./components/MainForm";
import ResultsTable from "./components/ResultsTable";
import MD2PDF from "./components/MD2PDF";

function App() {
  return (
    <div className="grid">
      <div className="col-8">
        <MainForm />
      </div>
      <div className="col-4">
        <ResultsTable />
        <br />
        <MD2PDF />
      </div>
    </div>
  );
}

export default App;
