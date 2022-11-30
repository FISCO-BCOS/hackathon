import InputAdornment from "@mui/material/InputAdornment";
import TextField from "@mui/material/TextField";
import { AutoStories } from "@mui/icons-material";
import { useState } from "react";
import axios from "axios";
import {
  Button,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
} from "@mui/material";
import { NavigateFunction, useNavigate } from "react-router-dom";

function WeatherInput() {
  const [contractAddress, setContractAddress]: any = useState();
  const handleContract = (e: any) => setContractAddress(e.target.value);
  const navigate: NavigateFunction = useNavigate();
  const send = () => {
    let data;
    axios
      .post("/weatherInfos", {
        contractAddress: contractAddress,
      })
      .then(function (response) {
        data = response.data;
        navigate("/weather", { state: { weathers: data } });
        console.log(data);
      })
      .catch(function (error) {
        // handle error
        console.log(error);
      });
  };
  return (
    <div>
      <br />
      <br />
      <div style={{ textAlign: "center" }}>
        <h2>TechCrop</h2>
      </div>
      <br />
      <h3>Weather Information</h3>
      <br />
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 330 }} aria-label="simple table">
          <TableBody>
            <TableRow
              sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
            >
              <TableCell component="th" scope="row">
                <h3>ContractAddress</h3>
              </TableCell>
              <TableCell component="th" scope="row">
                <TextField
                  id="input-with-icon-textfield"
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <AutoStories />
                      </InputAdornment>
                    ),
                  }}
                  variant="standard"
                  onChange={handleContract}
                />
              </TableCell>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
      <br />
      <div className="button" style={{ textAlign: "center" }}>
        <Button onClick={send}>submit</Button>
      </div>
    </div>
  );
}

export default WeatherInput;
