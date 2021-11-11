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

import "./OrganizationData.sol";
import "./RoleController.sol";

/**
 * @title OrganizationController
 * Issuer contract manages authority issuer info.
 */

contract OrganizationController {

    OrganizationData private organizationData;
    RoleController private roleController;

    // Event structure to store tx records
    uint constant private OPERATION_ADD = 0;
    uint constant private OPERATION_REMOVE = 1;
    
    event OrganizationRetLog(uint operation, uint retCode, address addr);

    // Constructor.
    function OrganizationController(
        address OrganizationDataAddress,
        address roleControllerAddress
    )
        public 
    {
        organizationData = OrganizationData(OrganizationDataAddress);
        roleController = RoleController(roleControllerAddress);
    }
    
    function addOrganization(
        address addr
    ) 
        public 
    {
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_ORGANIZATION())) {
            OrganizationRetLog(OPERATION_ADD, roleController.RETURN_CODE_FAILURE_NO_PERMISSION(), addr);
            return;
        }
        uint result = organizationData.addOrganizationFromAddress(addr);
        OrganizationRetLog(OPERATION_ADD, result, addr);
    }

    function removeOrganization(
        address addr
    ) 
        public 
    {
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_ORGANIZATION())) {
            OrganizationRetLog(OPERATION_REMOVE, roleController.RETURN_CODE_FAILURE_NO_PERMISSION(), addr);
            return;
        }
        uint result = organizationData.deleteOrganizationFromAddress(addr);
        OrganizationRetLog(OPERATION_REMOVE, result, addr);
    }

    function getAllOrganizationAddress() 
        public 
        constant 
        returns (address[]) 
    {
        // Per-index access
        uint datasetLength = organizationData.getDatasetLength();
        address[] memory memberArray = new address[](datasetLength);
        for (uint index = 0; index < datasetLength; index++) {
            memberArray[index] = organizationData.getOrganizationAddressFromIndex(index);
        }
        return memberArray;
    }

    function isOrganization(
        address addr
    ) 
        public 
        constant 
        returns (bool) 
    {
        return organizationData.isOrganization(addr);
    }
}