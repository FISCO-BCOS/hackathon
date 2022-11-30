import * as React from 'react';
import { styled } from '@mui/material/styles';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { useLocation } from 'react-router-dom';



const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: theme.palette.common.black,
    color: theme.palette.common.white,
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  '&:nth-of-type(odd)': {
    backgroundColor: theme.palette.action.hover,
  },
  // hide last border
  '&:last-child td, &:last-child th': {
    border: 0,
  },
}));

function WeatherInfos() {
  const location: any = useLocation();
  const rows = location.state.weathers;
  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 600 }} aria-label="customized table">
        <TableHead>
          <TableRow>
            <StyledTableCell align="center">Province</StyledTableCell>
            <StyledTableCell align="center">City</StyledTableCell>
            <StyledTableCell align="center">Weather</StyledTableCell>
            <StyledTableCell align="center">Temperature</StyledTableCell>
            <StyledTableCell align="center">Winddirection</StyledTableCell>
            <StyledTableCell align="center">Windpower</StyledTableCell>
            <StyledTableCell align="center">Humidity</StyledTableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row: any) => (
            <StyledTableRow>
              <StyledTableCell align="center">{row.Province}</StyledTableCell>
              <StyledTableCell align="center">{row.City}</StyledTableCell>
              <StyledTableCell align="center">{row.Weather}</StyledTableCell>
              <StyledTableCell align="center">{row.Temperature}</StyledTableCell>
              <StyledTableCell align="center">{row.Winddirection}</StyledTableCell>
              <StyledTableCell align="center">{row.Windpower}</StyledTableCell>
              <StyledTableCell align="center">{row.Humidity}</StyledTableCell>
            </StyledTableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );

}
export default WeatherInfos;
