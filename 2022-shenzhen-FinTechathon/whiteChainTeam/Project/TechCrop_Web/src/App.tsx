import { useRoutes } from "react-router-dom";
import "./App.css";
import Tabline from "./components/tab";
import routes from "./routes";

function App() {
  const element = useRoutes(routes);
  return (
    <div className="box">
      <Tabline />
      <div>{element}</div>
    </div>
  );
}

export default App;
