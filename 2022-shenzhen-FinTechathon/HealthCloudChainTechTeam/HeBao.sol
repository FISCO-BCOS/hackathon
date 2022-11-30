//Contract based on [https://docs.openzeppelin.com/contracts/3.x/erc721](https://docs.openzeppelin.com/contracts/3.x/erc721)
// SPDX-License-Identifier: MIT
pragma solidity ^0.4.25;
pragma experimental ABIEncoderV2;

// 核保合约
contract HeBao {
    // 病种
    enum MedicalHistory {
        JiXingBingDuXingGanYan, // 急性病毒性肝炎
        JiXingJiaXingGanYan, // 急性甲型肝炎
        JiXingYiXingGanYan, // 急性乙型肝炎
        JiXingBingXingGanYan, // 急性乙型肝炎
        JiXingDingXingGanYan, // 急性丁型肝炎
        JiXingWuXingGanYan, // 急性戊型肝炎
        ManXingBingDuXingGanYan, // 慢性病毒性肝炎
        ManXingYiXingGanYan, // 慢性乙型肝炎
        ManXingBingXingGanYan, // 慢性乙型肝炎
        ManXingDingXingGanYan, // 慢性丁型肝炎
        ManXingWuXingGanYan // 慢性戊型肝炎
    }

    // 病种分值表
    mapping(uint256 => uint256) private mapScore;

    constructor() public {
        mapScore[(uint256)(MedicalHistory.JiXingBingDuXingGanYan)] = 5; // 急性病毒性肝炎分值为5
        mapScore[(uint256)(MedicalHistory.JiXingJiaXingGanYan)] = 10;
        mapScore[(uint256)(MedicalHistory.JiXingYiXingGanYan)] = 7;
        mapScore[(uint256)(MedicalHistory.JiXingBingXingGanYan)] = 20;
        mapScore[(uint256)(MedicalHistory.JiXingDingXingGanYan)] = 15;
        mapScore[(uint256)(MedicalHistory.JiXingWuXingGanYan)] = 69;
        mapScore[(uint256)(MedicalHistory.ManXingBingDuXingGanYan)] = 3;
        mapScore[(uint256)(MedicalHistory.ManXingYiXingGanYan)] = 35;
        mapScore[(uint256)(MedicalHistory.ManXingBingXingGanYan)] = 25;
        mapScore[(uint256)(MedicalHistory.ManXingDingXingGanYan)] = 10;
        mapScore[(uint256)(MedicalHistory.ManXingWuXingGanYan)] = 5;
    }

    event eventResult(string data, int256 score);

    // 功能:计算分值
    // 输入:姓名,电话,年龄,病史
    // 输出:分值
    function cal(
        string name, // 姓名
        string phone, // 电话
        uint8 age, // 年龄
        string memory data // 既往病史。长度必须是MedicalHistory。索引中0表示出现，1表示不出现，比如"10000000001"表示出现JiXingBingDuXingGanYan,ManXingWuXingGanYan
    )
        public
        returns (
            int256 // 分值
        )
    {
        bytes memory databytes = bytes(data);
        require(
            databytes.length ==
                (uint256)(MedicalHistory.ManXingWuXingGanYan) + 1,
            "wrong data size"
        );

        // 计算年龄
        int256 score = 100; // 总分100
        if (50 < age) {
            score = score - 20;
        } else if (40 < age) {
            score = score - 10;
        } else if (30 < age) {
            score = score - 5;
        }

        // 计算既往病史
        bytes1 bt1 = "1";
        for (
            uint256 index = (uint256)(MedicalHistory.JiXingBingDuXingGanYan);
            index < (uint256)(MedicalHistory.ManXingWuXingGanYan) + 1;
            index++
        ) {
            if (databytes[index] == bt1) {
                score = score - (int256)(mapScore[index]);
                if (score <= 0) {
                    score = 0; // 最低扣分到0
                    break;
                }
            }
        }
        emit eventResult(data, score);
        return score;
    }

    // function lenOfChars(string memory src) internal pure returns (uint256) {
    //     uint256 i = 0;
    //     uint256 length = 0;
    //     bytes memory string_rep = bytes(src);
    //     //UTF-8 skip word
    //     while (i < string_rep.length) {
    //         i += utf8CharBytesLength(string_rep, i);
    //         length++;
    //     }
    //     return length;
    // }
}
