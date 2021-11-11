pragma solidity ^0.4.4;
/*
 *       Copyright© (2018-2019) WeBank Co., Ltd.
 *
 *       This file is part of weidentity-contract.
 *
 *       weidentity-contract is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       weidentity-contract is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with weidentity-contract.  If not, see <https://www.gnu.org/licenses/>.
 */

import "./RoleController.sol";

/**
 * @title OrganizationData
 * Organization data contract.
 */

contract OrganizationData {

    uint constant private RETURN_CODE_SUCCESS = 0;
    uint constant private RETURN_CODE_FAILURE_ALREADY_EXISTS = 500251;
    uint constant private RETURN_CODE_FAILURE_NOT_EXIST = 500252;

    address[] private OrganizationArray;
    RoleController private roleController;

    function OrganizationData(address addr) public {
        roleController = RoleController(addr);
    }

    function isOrganization(
        address addr
    ) 
        public 
        constant 
        returns (bool) 
    {
        // Use LOCAL ARRAY INDEX here, not the RoleController data.
        // The latter one might lose track in the fresh-deploy or upgrade case.
        for (uint index = 0; index < OrganizationArray.length; index++) {
            if (OrganizationArray[index] == addr) {
                return true;
            }
        }
        return false;
    }

    function addOrganizationFromAddress(
        address addr
    ) 
        public
        returns (uint)
    {
        if (isOrganization(addr)) {
            return RETURN_CODE_FAILURE_ALREADY_EXISTS;
        }
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_ORGANIZATION())) {
            return roleController.RETURN_CODE_FAILURE_NO_PERMISSION();
        }
        roleController.addRole(addr, roleController.ROLE_ORGANIZATION());
        OrganizationArray.push(addr);
        return RETURN_CODE_SUCCESS;
    }

    function deleteOrganizationFromAddress(
        address addr
    ) 
        public
        returns (uint)
    {
        if (!isOrganization(addr)) {
            return RETURN_CODE_FAILURE_NOT_EXIST;
        }
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_ORGANIZATION())) {
            return roleController.RETURN_CODE_FAILURE_NO_PERMISSION();
        }
        roleController.removeRole(addr, roleController.ROLE_ORGANIZATION());
        uint datasetLength = OrganizationArray.length;
        for (uint index = 0; index < datasetLength; index++) {
            if (OrganizationArray[index] == addr) {break;}
        }
        if (index != datasetLength-1) {
            OrganizationArray[index] = OrganizationArray[datasetLength-1];
        }
        delete OrganizationArray[datasetLength-1];
        OrganizationArray.length--;
        return RETURN_CODE_SUCCESS;
    }

    function getDatasetLength() 
        public 
        constant 
        returns (uint) 
    {
        return OrganizationArray.length;
    }

    function getOrganizationAddressFromIndex(
        uint index
    ) 
        public 
        constant 
        returns (address) 
    {
        return OrganizationArray[index];
    }
}