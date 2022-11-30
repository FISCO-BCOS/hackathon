import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import React from "react";
import { AccountBalance, LightMode } from "@mui/icons-material";
import { NavigateFunction, useNavigate } from "react-router-dom";

function Tabline() {
  const [value, setValue] = React.useState(0);
  const navigate: NavigateFunction = useNavigate();

  const handleChange = (_event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };
  const insurance = () => {
    navigate("/insuranceInput");
  };
  const weather = () => {
    navigate("/weatherInput");
  };

  return (
    <Tabs
      value={value}
      onChange={handleChange}
      aria-label="icon label tabs example"
    >
      <Tab icon={<AccountBalance />} label="Insurance" onClick={insurance} />
      <Tab icon={<LightMode />} label="Weather" onClick={weather} />
    </Tabs>
  );
}

export default Tabline;
