import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import { useLocation } from "react-router-dom";

function InsuranceInfos() {
  const location: any = useLocation();
  const insurance = location.state.insurance;
  console.log(insurance);

  return (
    <div>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 330 }} aria-label="simple table">
          <TableHead>
            <TableRow>
              <TableCell>InsuranceId</TableCell>
              <TableCell align="left">Beneficiary Address</TableCell>
              <TableCell align="left">Compensation</TableCell>
              <TableCell align="left">Insurance Status</TableCell>
              <TableCell align="left">EndTime</TableCell>
              <TableCell align="left">IPFS Address</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            <TableRow
              sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
            >
              <TableCell component="th" scope="row">
                {insurance.InsuranceId}
              </TableCell>
              <TableCell component="th" scope="row">
                {insurance.BeneficiaryAddress}
              </TableCell>
              <TableCell component="th" scope="row">
                {insurance.Compensation}
              </TableCell>
              <TableCell component="th" scope="row">
                {String(insurance.InsuranceStatus)}
              </TableCell>
              <TableCell component="th" scope="row">
                {insurance.EndTime}
              </TableCell>
              <TableCell component="th" scope="row">
                {insurance.InsuranceUri}
              </TableCell>
            </TableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}

export default InsuranceInfos;
