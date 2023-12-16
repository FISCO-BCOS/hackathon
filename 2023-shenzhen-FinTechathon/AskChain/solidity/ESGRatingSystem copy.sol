// SPDX-License-Identifier: MIT
pragma solidity 0.4.25;

pragma experimental ABIEncoderV2;

contract ESGRatingSystem {
    // 存储企业的ESG评分
    mapping(address => uint256) companyScores;

    // 存储评估机构的信息
    mapping(address => bool) assessmentAgencies;

    // 存储评估机构的信息及等级
    mapping(address => AgencyData) assessmentAgenciesData;

    // 存储项目信息
    mapping(string => ProjectData) projects;
    string[] public projectNames;

    // 项目信息结构
    struct ProjectData {
        string projectName;
        string projectDescription;
        address projectOwner;
    }

    // 评估机构信息结构
    struct AgencyData {
        string name;
        uint256 level; // 评估机构的等级，可以是1、2、3等
    }

    // 存储政府的信息
    address public government = 0xf4abb7c0624e2311eec36a3e3887da605c14ff24;

    // 存储评估机构地址的数组
    address[] private assessmentAgencyAddresses;

    // 定义评分变化事件
    event ScoreChanged(address indexed company, uint256 newScore);

    // 仅允许政府调用的修饰器
    modifier onlyGovernment() {
        require(
            msg.sender == government,
            "Only the government can call this function"
        );
        _;
    }

    // 评估机构注册，允许指定等级，只能合约部署者调用
    function registerAssessmentAgency(
        address agency,
        uint256 level
    ) external onlyGovernment {
        require(level > 0, "Invalid level, must be greater than 0");

        assessmentAgencies[agency] = true;
        assessmentAgencyAddresses.push(agency);

        // 存储评估机构信息及等级
        assessmentAgenciesData[agency] = AgencyData({
            name: "Assessment Agency", // 评估机构名称，可以根据需要修改
            level: level
        });
    }

    // 企业进行ESG评分，传入企业地址和评分
    function scoreESG(address company, uint256 score) external {
        require(
            score >= 0 && score <= 100,
            "Invalid score, must be between 0 and 100"
        );
        require(
            assessmentAgencies[msg.sender],
            "Only assessment agencies can score"
        );

        companyScores[company] = score;

        // 触发评分变化事件
        emit ScoreChanged(company, score);
    }

    // 政府获取企业ESG评分
    function getCompanyScore(
        address company
    ) external view onlyGovernment returns (uint256) {
        return companyScores[company];
    }

    // 允许企业查看自己的ESG评分
    function getCompanyScore() external view returns (uint256) {
        return companyScores[msg.sender];
    }

    // 允许评估机构查看自己的ESG评分
    function getAssessmentAgencyScore() external view returns (uint256) {
        require(
            assessmentAgencies[msg.sender],
            "Only assessment agencies can view scores"
        );
        return companyScores[msg.sender];
    }

    // 获取所有评估机构的地址
    function getAssessmentAgencies() external view returns (address[] memory) {
        return assessmentAgencyAddresses;
    }

    // 获取评估机构数量
    function getAssessmentAgenciesCount() external view returns (uint256) {
        return assessmentAgencyAddresses.length;
    }

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
    }

    // 获取用户信息
    function getUserInfo(address _address)
        external
        view
        returns (string memory, string memory)
    {
        return (users[_address].username, users[_address].identity);
    }

    // ESG评分功能，计算所有评估机构的总分
    function calculateTotalScore() external view returns (uint256) {
        require(
            assessmentAgencyAddresses.length > 0,
            "No assessment agencies registered"
        );

        uint256 totalWeightedScore = 0;
        uint256 totalWeight = 0;

        for (uint256 i = 0; i < assessmentAgencyAddresses.length; i++) {
            address agency = assessmentAgencyAddresses[i];
            uint256 score = companyScores[agency];
            uint256 weight = getWeight(assessmentAgenciesData[agency].level);

            totalWeightedScore += score * weight;
            totalWeight += weight;
        }

        if (totalWeight > 0) {
            return totalWeightedScore / totalWeight;
        } else {
            return 0;
        }
    }

    // 获取评估机构权重
    function getWeight(uint256 level) internal pure returns (uint256) {
        // 在这里定义不同评估机构等级对应的权重
        // 可根据实际需求进行调整
        if (level == 1) {
            return 1;
        } else if (level == 2) {
            return 2;
        } else if (level == 3) {
            return 3;
        } else {
            return 1; // 默认权重为1
        }
    }

    // 上传项目信息
    function uploadProject(
        string projectName,
        string projectDescription
    ) external {
        require(bytes(projectName).length > 0, "Project name cannot be empty");
        require(
            !assessmentAgencies[msg.sender],
            "Assessment agencies cannot upload projects"
        );

        projects[projectName] = ProjectData({
            projectName: projectName,
            projectDescription: projectDescription,
            projectOwner: msg.sender
        });
        
        projectNames.push(projectName);
    }

    // 修改项目信息
    function modifyProject(
        string projectName,
        string newDescription
    ) external {
        require(
            bytes(newDescription).length > 0,
            "New description cannot be empty"
        );
        require(
            projects[projectName].projectOwner == msg.sender,
            "Only the project owner can modify the project"
        );

        projects[projectName].projectDescription = newDescription;
    }

    // 获取所有项目名称
    function getAllProjectNames() external view returns (string[] memory) {
        return projectNames;
    }

    // 通过项目名称查看项目信息
    function viewProject(
        string projectName
    )
        external
        view
        returns (string memory name, string memory description, address owner)
    {
        ProjectData storage project = projects[projectName];
        require(bytes(project.projectName).length > 0, "Project not found");

        return (
            project.projectName,
            project.projectDescription,
            project.projectOwner
        );
    }
}

