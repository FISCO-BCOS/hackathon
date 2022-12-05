import { Navigate } from "react-router-dom";
import InsuranceInfos from "./components/insuranceInfos";
import InsuranceInput from "./components/insuranceInput";
import WeatherInfos from "./components/weatherInfos";
import WeatherInput from "./components/weatherInput";

export default [
  {
    path: "/insuranceInput",
    element: <InsuranceInput />,
  },
  {
    path: "/insurance",
    element: <InsuranceInfos />,
  },
  {
    path: "/weatherInput",
    element: <WeatherInput />,
  },
  {
    path: "/weather",
    element: <WeatherInfos />,
  },
  {
    path: "/",
    element: <Navigate to="/insuranceInput" />,
  },
];
