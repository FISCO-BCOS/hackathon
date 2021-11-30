pragma solidity ^0.4.24;

// 预编译合约的接口合约
contract CommitteePrecompiled{
    function RegisterNode(int256 group) public;                     //节点注册
    function QueryState() public view returns(string, int);         //查询状态
    function QueryGlobalModel() public view returns(string, int);   //获取全局模型
    function UploadLocalUpdate(string update, int256 epoch) public; //上传本地模型
    function UploadScores(int256 epoch, string scores) public;      //上传评分
    function QueryAllUpdates() public view returns(string);         //获取所有本地模型
    function UploadLocalAggregation(string update, int256 epoch) public;           //上传本地聚合模型更新
    function QueryAllAccuracy() public view returns(string, string);                       //获取所有本地训练准确率和全局准确率
    function UploadGlobalAccuracy(string gb_ac) public;                       //获取所有本地训练准确率和全局准确率
}

// 因为我们预编译合约是我们实验室目前发的一篇论文和专利的核心内容，所以暂时还不方便向各位专家展示，还请谅解。
// 目前这是预编译合约的sol文件接口。如有需要，我们可以现场展示运行过程。谢谢专家们的理解！
