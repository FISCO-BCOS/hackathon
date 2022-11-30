import logo from './logo.svg';
import './App.css';
import Login from './components/Login';
import { Outlet } from 'react-router-dom';
import { useRoutes} from 'react-router-dom';
import routes from './routes';
function App() {
  const element=useRoutes(routes)
  // return element;
  return (
    <div>
      {element}
    </div>
  )
}

export default App;
