pragma solidity ^ 0.4.4;

/**
 * @title RoleController
 *  This contract provides basic authentication control which defines who (address)
 *  belongs to what specific role and has what specific permission.
 */

contract RoleController{

    /**
     * The universal NO_PERMISSION error code.
     */
    uint constant public RETURN_CODE_FAILURE_NO_PERMISSION = 500000;

/**
 * Role related Constants.
 */
uint constant public ROLE_GOVERNMENT = 100;
uint constant public ROLE_ORGANIZATION = 101;
uint constant public ROLE_PERSON = 102;

/**
 * Operation related Constants.
 */
uint constant public MODIFY_GOVERNMENT = 200;
uint constant public MODIFY_ORGANIZATION = 201;
uint constant public MODIFY_PERSON = 202;
uint constant public MODIFY_KEY_CPT = 203;

mapping(address => bool) private governmentRoleBearer;
mapping(address => bool) private organizationRoleBearer;
mapping(address => bool) private personRoleBearer;

function RoleController() public {
    governmentRoleBearer[msg.sender] = true;
    personRoleBearer[msg.sender] = true;
    organizationRoleBearer[msg.sender] = true;
}

/**
 * Public common checkPermission logic.
 */
function checkPermission(
    address addr,
    uint operation
)
    public
    constant
    returns(bool)
{
    if (operation == MODIFY_GOVERNMENT) {
        if (governmentRoleBearer[addr]) {
            return true;
        }
    }
    if (operation == MODIFY_ORGANIZATION) {
        if (organizationRoleBearer[addr]) {
            return true;
        }
    }
    if (operation == MODIFY_PERSON) {
        if (personRoleBearer[addr]) {
            return true;
        }
    }
    if (operation == MODIFY_KEY_CPT) {
        if (governmentRoleBearer[addr]) {
            return true;
        }
    }
    return false;
}

/**
 * Add Role.
 */
function addRole(
    address addr,
    uint role
)
    public
{
    if (role == ROLE_GOVERNMENT) {
        if (checkPermission(tx.origin, MODIFY_GOVERNMENT)) {
            governmentRoleBearer[addr] = true;
        }
    }
    if (role == ROLE_ORGANIZATION) {
        if (checkPermission(tx.origin, MODIFY_ORGANIZATION)) {
            organizationRoleBearer[addr] = true;
        }
    }
    if (role == ROLE_PERSON) {
        if (checkPermission(tx.origin, MODIFY_PERSON)) {
            personRoleBearer[addr] = true;
        }
    }
}

/**
 * Remove Role.
 */
function removeRole(
    address addr,
    uint role
)
    public
{
    if (role == ROLE_GOVERNMENT) {
        if (checkPermission(tx.origin, MODIFY_GOVERNMENT)) {
            governmentRoleBearer[addr] = false;
        }
    }
    if (role == ROLE_ORGANIZATION) {
        if (checkPermission(tx.origin, MODIFY_ORGANIZATION)) {
            organizationRoleBearer[addr] = false;
        }
    }
    if (role == ROLE_PERSON) {
        if (checkPermission(tx.origin, MODIFY_PERSON)) {
            personRoleBearer[addr] = false;
        }
    }
}
    
    /**
     * Check Role.
     */
    function checkRole(
        address addr,
        uint role
    )
        public
        constant
        returns(bool)
    {
        if (role == ROLE_GOVERNMENT) {
            return governmentRoleBearer[addr];
        }
        if (role == ROLE_ORGANIZATION) {
            return organizationRoleBearer[addr];
        }
        if (role == ROLE_PERSON) {
            return personRoleBearer[addr];
        }
    }
    }