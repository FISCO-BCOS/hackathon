pragma solidity ^0.4.25;
import "./PEMR.sol";
//医生负责对用户的电子病历或则体检报告进行上链操作;
contract DoctorCon is PEMR {
    event Wemr(
        address PatiAdd,
        address DocAdd,
        string Treatprocess,
        string Division,
        string Illness,
        string Prmedicine,
        string log
    );
    event Wphydata(
        address PatiAdd,
        address DocAdd,
        string Bloodpressure,
        string BMI, //BMI值;
        string BloodTest, //血检;
        string ALT, //谷草转氨酶
        string AST //天门冬氨酸氨基转移酶
    );
    //电子病历进行签名;
    function DocSinger(address Add) public {
        PEMR.Sign(Add);
    }
    //对检查报告进行签名:
    function DocSingerPhy(address Add) public {
        PEMR.SignPhy(Add);
    }

    //书写用户电子病历：
    function DEMR(
        address PatiAdd,
        address DocAdd,
        string Treatprocess,
        string Division,
        string Illness,
        string Prmedicine
    ) public {
        PEMR.WEMR(PatiAdd, DocAdd, Treatprocess, Division, Illness, Prmedicine);
        emit Wemr(
            PatiAdd,
            DocAdd,
            Treatprocess,
            Division,
            Illness,
            Prmedicine,
            "This is successful"
        );
    }

    //书写用户的检查报告：
    function DPHY(
        address PatiAdd,
        address DocAdd,
        string Bloodpressure,
        string BMI, //BMI值;
        string BloodTest, //血检;
        string ALT, //谷草转氨酶
        string AST //天门冬氨酸氨基转移酶
    ) public {
        PEMR.WPHY(
            PatiAdd,
            DocAdd,
            Bloodpressure,
            BMI, //BMI值;F
            BloodTest, //血检;
            ALT, //谷草转氨酶
            AST
        );
        emit Wphydata(
            PatiAdd,
            DocAdd,
            Bloodpressure,
            BMI, //BMI值;
            BloodTest, //血检;
            ALT, //谷草转氨酶
            AST //天门冬氨酸氨基转移酶
        );
    }
}
