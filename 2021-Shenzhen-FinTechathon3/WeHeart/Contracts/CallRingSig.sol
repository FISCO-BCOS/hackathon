pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

import "./RingSigPrecompiled.sol";

contract CallRingSig{
    RingSigPrecompiled ringsig;
    string _ring_param_info;//specified ring_param_info
    string _rand_G;         //random_information
    string _target_addrG;   //secret_target(receiver)_address
    string _ring_sig;       //ring_signature
    string _message;        //secret_message
    bool _verified=false;   //_verified==1,valid signature

    //ensure signature is valid
    modifier isVerified(){
        require(_verified==true,"INVALID RINGSIGNATURE!");
        _;
    }
    function CallRingSig(){
        //call RingSigPrecompiled  precompiled_contract
        ringsig=RingSigPrecompiled(0x5005);
    }
    //verify signature valid or not
    function ringSigVerify(string signature, string message, string paramInfo) public constant returns(bool){
        _verified=ringsig.ringSigVerify(signature,message,paramInfo);
        return _verified;
    }
    //set up ring with specified param_info
    function setup_ring( string ring_param_info )public {
        _ring_param_info=ring_param_info;
    }
    //generate signature
    function ring_sig(string ring_sign)public {
        _ring_sig=ring_sign;
    }
    //generate secret_message
    function set_message(string message)public {
        _message=message;
    }
    //generate random_information and secret_target_address
    function set_target(string rand_G,string target_addrG)public{
        _rand_G=rand_G;
        _target_addrG=target_addrG;
    }
    //get random_information and secret_target_address
    function get_target() isVerified()public returns(string rand_G,string target_addrG){
        return(_rand_G,_target_addrG);
    }
    //get secret_message
    function get_message() isVerified()public returns(string) {
        return _message;
    }
}

