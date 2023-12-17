// SPDX-License-Identifier: MIT
pragma solidity 0.4.25;

pragma experimental ABIEncoderV2;

contract ESGRatingSystem {
    // 存储政府的信息
    address private government = 0x4B20993Bc481177ec7E8f571ceCaE8A9e22C02db;

    // 存储评估机构地址的数组
    address[] private assessmentAddresses;

    // 存储评估机构的信息及等级
    mapping(address => uint8) assessmentLevel;

    // 存储项目信息
    mapping(address => ProjectData[]) companyProjects;

    // 存储所有企业地址
    address[] private companys;

    // 存储用户信息
    mapping(address => UserData) users;

    // 用户信息结构，添加三个字段
    struct UserData {
        string username;
        string identity;
        string organizationCode;
        string registrationAddress;
        string domain;
    }

    // 项目信息结构
    struct ProjectData {
        uint256 id;
        string projectName;
        string projectDescription;
        bytes1 property;
        address projectOwner;
        string fileUrl;
        mapping(address => uint8) scores;
        mapping(address => string) standard;
    }

    // 定义评分变化事件
    event ScoreChanged(
        address indexed company,
        address assessment,
        uint256 projectId,
        uint256 newScore
    );

    // 仅允许政府调用的修饰器
    modifier onlyGovernment() {
        require(
            msg.sender == government,
            "Only the government can call this function"
        );
        _;
    }

    // 获取所有企业地址
    function getCompanys() external view returns (address[]) {
        return companys;
    }

    // 评估机构注册，允许指定等级，只能合约部署者调用
    function registerAssessmentAgency(
        address agency,
        uint8 level
    ) external onlyGovernment {
        require(level > 0, "Invalid level, must be greater than 0");

        // 存储评估机构信息及等级
        assessmentLevel[agency] = level;
        assessmentAddresses.push(agency);
    }

    // 企业进行ESG评分，传入企业地址和评分
    function scoreESG(
        address company,
        uint256 projectId,
        uint8 score,
        string standard
    ) external {
        require(
            score > 0 && score <= 100,
            "Invalid score, must be between 0 and 100"
        );
        require(
            assessmentLevel[msg.sender] != 0,
            "Only assessment agencies can score"
        );

        companyProjects[company][projectId].scores[msg.sender] = score;
        companyProjects[company][projectId].standard[msg.sender] = standard;

        // 触发评分变化事件
        emit ScoreChanged(company, msg.sender, projectId, score);
    }

    // 政府获取企业ESG评分
    function getCompanyScore(
        address _company
    ) external view onlyGovernment returns (uint256) {
        return calculateCompanyScore(_company);
    }

    // 允许企业查看自己的ESG评分
    function getCompanyScore() external view returns (uint256) {
        return calculateCompanyScore(msg.sender);
    }

    // 获取所有评估机构的地址
    function getAssessmentAgencies() external view returns (address[] memory) {
        return assessmentAddresses;
    }

    // 注册用户，添加三个请求参数
    function registerUser(
        string _username,
        string _identity,
        string _organizationCode,
        string _registrationAddress,
        string _domain
    ) external {
        // 存储用户信息，包括新的三个参数
        users[msg.sender] = UserData({
            username: _username,
            identity: _identity,
            organizationCode: _organizationCode,
            registrationAddress: _registrationAddress,
            domain: _domain
        });

        if (keccak256(_identity) == keccak256("company")) {
            companys.push(msg.sender);
        }
    }

    // 获取用户信息
    function getUserInfo(
        address _address
    ) external view returns (string memory, string memory) {
        return (users[_address].username, users[_address].identity);
    }

    // 贝叶斯先验参数
    uint256 public priorStrength = 2;  // 先验强度
    uint256 public priorMean = 50;    // 先验均值

    function calculateProjectScore(
        address _company,
        uint256 _projectId
    ) view returns (uint8) {
        uint[3] memory levelScoreSum;
        uint[3] memory scoreCnt;
        uint[3] memory scoreResult;
        for (uint i = 0; i < assessmentAddresses.length; i++) {
            address curAssessment = assessmentAddresses[i];
            uint8 curLevel = assessmentLevel[curAssessment];
            uint8 curScore = companyProjects[_company][_projectId].scores[
                curAssessment
            ];
            if (curScore != 0) {
                scoreCnt[curLevel - 1] += 1;
                levelScoreSum[curLevel - 1] += curScore;
            }
        }
        for (i = 0; i < 3; i++) {
            if (scoreCnt[i] > 0) {
                scoreResult[i] = levelScoreSum[i] / scoreCnt[i];
            }
        }

        // 贝叶斯平均修正
        scoreResult[0] = adjustScore(scoreResult[0], scoreResult[1], scoreResult[2], scoreCnt, priorStrength, priorMean);

        return uint8(scoreResult[0]);

        // scoreResult[0] +=
        //     ((scoreResult[0] - scoreResult[1]) *
        //         (scoreCnt[1] / (scoreCnt[0] + scoreCnt[1]))) /
        //     4;
        // scoreResult[0] +=
        //     ((scoreResult[0] - scoreResult[2]) *
        //         (scoreCnt[2] / (scoreCnt[0] + scoreCnt[1] + scoreCnt[2]))) /
        //     8;
        // return uint8(scoreResult[0]);
    }

    // 贝叶斯平均修正函数，增加先验参数
    function adjustScore(uint256 score0, uint256 score1, uint256 score2, uint[3] memory scoreCnt, uint256 priorStrength, uint256 priorMean) pure internal returns (uint256) {
        uint256 adjustedScore;

        if (scoreCnt[0] + scoreCnt[1] > 0) {
            // 计算贝叶斯平均修正
            adjustedScore = score0 +
                ((score0 - score1) * (scoreCnt[1] * 4) / (scoreCnt[0] + scoreCnt[1])) / priorStrength +
                ((score0 - score2) * (scoreCnt[2] * 8) / (scoreCnt[0] + scoreCnt[1] + scoreCnt[2])) / priorStrength;
        } else {
            return 0;
        }

        return adjustedScore;
    }

    // ESG评分功能，计算所有评估机构的总分
    function calculateCompanyScore(
        address _company
    ) internal view returns (uint256) {
        uint scoreSum = 0;

        for (uint256 i = 0; i < companyProjects[_company].length; i++) {
            scoreSum += calculateProjectScore(_company, i);
        }

        return scoreSum / companyProjects[_company].length;
    }

    // 上传项目信息
    function uploadProject(
        string _projectName,
        string _projectDescription,
        bytes1 _property,
        string _fileUrl
    ) external {
        require(bytes(_projectName).length > 0, "Project name cannot be empty");
        require(
            assessmentLevel[msg.sender] == 0,
            "Assessment agencies cannot upload projects"
        );

        ProjectData memory project = ProjectData({
            id: companyProjects[msg.sender].length,
            projectName: _projectName,
            projectDescription: _projectDescription,
            property: _property,
            projectOwner: msg.sender,
            fileUrl: _fileUrl
        });
        companyProjects[msg.sender].push(project);
    }

    // 修改项目信息
    function modifyProject(
        uint256 _projectId,
        string _projectDescription,
        string _fileUrl
    ) external {
        require(
            companyProjects[msg.sender].length > _projectId,
            "Invalid project id."
        );

        companyProjects[msg.sender][_projectId]
            .projectDescription = _projectDescription;
        companyProjects[msg.sender][_projectId].fileUrl = _fileUrl;
    }

    // 通过项目id查看项目信息
    function viewProject(
        address _company,
        uint256 _id
    )
        external
        view
        returns (
            uint256 id,
            string memory name,
            string memory description,
            bytes1 property,
            address owner,
            string memory fileUrl,
            uint8 score
        )
    {
        require(
            _company == msg.sender || assessmentLevel[msg.sender] != 0,
            "Permission denied."
        );
        ProjectData memory project = companyProjects[_company][_id];
        uint8 projectScore = calculateProjectScore(_company, project.id);

        return (
            project.id,
            project.projectName,
            project.projectDescription,
            project.property,
            project.projectOwner,
            project.fileUrl,
            projectScore
        );
    }

    function getProjectsAmount(address _company) external view returns (uint256) {
        require(
            _company == msg.sender || assessmentLevel[msg.sender] != 0,
            "Permission denied."
        );
        return companyProjects[_company].length;
    }

    function getProjectScore(address _company, uint _projectId, address _assessment) external view returns (uint8) {
        require(
            _company == msg.sender || assessmentLevel[msg.sender] != 0,
            "Permission denied."
        );
        return companyProjects[_company][_projectId].scores[_assessment];
    }

    function getProjectStandard(address _company, uint _projectId, address _assessment) external view returns (string) {
        require(
            _company == msg.sender || assessmentLevel[msg.sender] != 0,
            "Permission denied."
        );
        return companyProjects[_company][_projectId].standard[_assessment];
    }
}
