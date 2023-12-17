// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.4.16 <0.9.0;

/** 
 * @title Carbon emission accounting
 * @dev This contract allows users carbon emitting enterprises to account for their own carbon emissions

 */
 contract EmissionAccounting {

    // Emission Types; should be float -> 1000 times larger
    uint constant et1 = 1981; // 原煤碳排放因子
    uint constant et2 = 2405; // 洗精煤碳排放因子
    uint constant et3 = 2860; // 焦炭排放因子
    uint constant et4 = 21622; // 天然气排放因子
    uint constant et5 = 2925; // 汽油排放因子
    uint constant et6 = 3096; // 柴油碳排放因子
    uint constant et7 = 318; // K2CO3排放因子
    uint constant et8 = 223; // BaCO3排放因子
    uint constant et9 = 522; // MgCO3排放因子
    uint constant et10 = 440; // CaCO3碳排放因子

    uint sum1;
    uint sum2;
    
    /**
     * @dev Calculate emissions based on emission factors and amounts and sum them up
     * @param amount1 Amount of raw coal emissions
     * @param amount2 Amount of washed refined coal emissions
     * @param amount3 Amount of coke emissions
     * @param amount4 Amount of natural gas emissions
     * @param amount5 Amount of gasoline emissions
     * @return log A string logging the calculated emissions and their sum
     */
    function _AccountEmission1(
        uint amount1,
        uint amount2,
        uint amount3,
        uint amount4,
        uint amount5
    ) public returns (string memory log) {
        uint emission1 = amount1 * et1;
        uint emission2 = amount2 * et2;
        uint emission3 = amount3 * et3;
        uint emission4 = amount4 * et4;
        uint emission5 = amount5 * et5;
        uint totalEmissions1 = emission1 + emission2 + emission3 + emission4 + emission5;
        sum1 = totalEmissions1;

        return string(abi.encodePacked(
            "Emissions: ",
            "Coal: ", uintToString(emission1), ", ",
            "Refined Coal: ", uintToString(emission2), ", ",
            "Coke: ", uintToString(emission3), ", ",
            "Natural Gas: ", uintToString(emission4), ", ",
            "Gasoline: ", uintToString(emission5), "; ",
            "Total: ", uintToString(totalEmissions1)
        ));
    }

    /**
     * @dev Calculate emissions for diesel, K2CO3, BaCO3, MgCO3, CaCO3
     * @param amount6 Amount of diesel emissions
     * @param amount7 Amount of K2CO3 emissions
     * @param amount8 Amount of BaCO3 emissions
     * @param amount9 Amount of MgCO3 emissions
     * @param amount10 Amount of CaCO3 emissions
     * @return log A string logging the calculated emissions and their sum
     */
    function _AccountEmission2(
        uint amount6,
        uint amount7,
        uint amount8,
        uint amount9,
        uint amount10
    ) public returns (string memory log) {
        uint emission6 = amount6 * et6;
        uint emission7 = amount7 * et7;
        uint emission8 = amount8 * et8;
        uint emission9 = amount9 * et9;
        uint emission10 = amount10 * et10;
        uint totalEmissions2 = emission6 + emission7 + emission8 + emission9 + emission10;
        sum2 = totalEmissions2;

        return string(abi.encodePacked(
            "Emissions: ",
            "Diesel: ", uintToString(emission6), ", ",
            "K2CO3: ", uintToString(emission7), ", ",
            "BaCO3: ", uintToString(emission8), ", ",
            "MgCO3: ", uintToString(emission9), ", ",
            "CaCO3: ", uintToString(emission10), "; ",
            "Total: ", uintToString(totalEmissions2)
        ));
    }


    /**
     * @dev Returns the sum of total emissions from both _AccountEmission1 and _AccountEmission2. Only be called after calling function both _AccountEmission1 and _AccountEmission2
     * @return totalEmissions The sum of sum1 and sum2. (1000 times larger than actual value)
     */
    function _sumEmission() public view returns (uint totalEmissions) {
        totalEmissions = sum1 + sum2;
        return totalEmissions;
    }

 /**
     * @dev Returns the monthly carbon emissions data
     * @return emissions An array containing the monthly carbon emissions data
     */
    function _queryEmission() public view returns(uint[12] memory emissions) {
        // First 9 months data as given
        emissions[0] = 52;
        emissions[1] = 60;
        emissions[2] = 53;
        emissions[3] = 31;
        emissions[4] = 30;
        emissions[5] = 42;
        emissions[6] = 50;
        emissions[7] = 44;
        emissions[8] = 32;
        emissions[9] = 29;
        emissions[10] = 40;
        // The 12th month's data is the sum of sum1 and sum2
        emissions[11] = sum1 + sum2; // only this one is 1000 times larger
        return emissions;
    }
    /**
     * @dev Helper function to convert uint to string
     * @param _i The integer to convert
     * @return _uintAsString The string representation of the integer
     */
    function uintToString(uint _i) internal pure returns (string memory _uintAsString) {
        if (_i == 0) {
            return "0";
        }
        uint j = _i;
        uint len;
        while (j != 0) {
            len++;
            j /= 10;
        }
        bytes memory bstr = new bytes(len);
        uint k = len;
        while (_i != 0) {
            k = k - 1;
            uint8 temp = (48 + uint8(_i - _i / 10 * 10));
            bytes1 b1 = bytes1(temp);
            bstr[k] = b1;
            _i /= 10;
        }
        return string(bstr);
    }
 }