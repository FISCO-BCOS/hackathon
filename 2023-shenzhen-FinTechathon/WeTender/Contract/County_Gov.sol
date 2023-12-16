pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./Government.sol";
import "./Town_Gov.sol";

contract County_Gov is Government
{
    constructor(string name) public Government(name) {}
    
    
    
    struct Towns {
        address town;
        bool valid;
    }
    
    mapping(string => Towns) town_list;         //县级政府特有的乡级政府列表
    
    event SelectTown(string name, address town, string remark);
    
    //添加乡级政府
    function addTown(string name, address town) public onlyOwner()
    {
        require(town_list[name].valid == false, "该乡政府已经存在!");
        town_list[name].town = town;
        town_list[name].valid = true;
        emit SelectTown(name, town, "add");
    }
    
    //删除乡级政府
    function removeTown(string name) public onlyOwner()
    {
        require(town_list[name].valid == true, "该乡政府不存在!");
        emit SelectTown(name, town_list[name].town, "delete");
        delete town_list[name];
    }
    
    //查询乡级政府是否存在
    function checkTown(string name) public view returns(string, address)
    {
        require(town_list[name].valid == true, "该乡政府不存在!");
        return (name, town_list[name].town);
    }
}