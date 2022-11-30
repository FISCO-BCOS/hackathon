pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;
import "./Roles.sol";
// import "./patient.sol";
// import "./PEMR.sol";
import "./Module.sol";
//定义医院类型的合约，通过这个合约来进行运算操作;
contract Hospital is Module{
    //定义一个医院的结构体类型;
    //address就是医院的公钥;
    //用Roles.Role来绑定Roles里面的所有函数方法;
    using Roles for Roles.Role;
    //Role是定义的一个变量;
    Roles.Role private HosRole;
    event Add(address DoAddress, string log);
    //医生注册:
    function AddDoctor(address Hos,address Daddre, string Dname) public {
        require(!HosRole.has(Daddre), "This Doctor is already exit...");
        DocAdd.push(Daddre); //加入医生地址;
        HosRole.add(Daddre);
        DoctorList[Daddre].Dname = Dname;
         DoctorList[Daddre].BelongHospital=Hos;
        //address[] DocAdd;
        HospitalList[Hos].Doctor.push(Daddre);
        emit Add(Daddre, "It is success!");
    }

}
