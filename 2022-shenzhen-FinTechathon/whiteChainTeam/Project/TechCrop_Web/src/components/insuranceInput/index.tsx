import InputAdornment from "@mui/material/InputAdornment";
import TextField from "@mui/material/TextField";
import { AccountBalance, AutoStories } from "@mui/icons-material";
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

function InsuranceInput() {
  const [contractAddress, setContractAddress]: any = useState();
  const [insurancerAddress, setInsurancerAddress]: any = useState();
  const handleContract = (e: any) => setContractAddress(e.target.value);
  const handleInsurancer = (e: any) => setInsurancerAddress(e.target.value);

  const navigate: NavigateFunction = useNavigate();
  function send() {
    let data;
    axios
      .post("/insuranceInfos", {
        contractAddress: contractAddress,
        insurancerAddress: insurancerAddress,
      })
      .then(function (response) {
        data = response.data;
        navigate("/insurance", { state: { insurance: data } });
        console.log(data);
      })
      .catch(function (error) {
        // handle error
        console.log(error);
      });
  }
  return (
    <div>
      <br />
      <br />
      <div style={{ textAlign: "center" }}>
        <h2>TechCrop</h2>
      </div>
      <br />
      <h3>Insurance Information</h3>
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
            <TableRow
              sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
            >
              <TableCell component="th" scope="row">
                <h3>InsurancerAddress</h3>
              </TableCell>
              <TableCell component="th" scope="row">
                <TextField
                  id="input-with-icon-textfield"
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <AccountBalance />
                      </InputAdornment>
                    ),
                  }}
                  variant="standard"
                  onChange={handleInsurancer}
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

export default InsuranceInput;
