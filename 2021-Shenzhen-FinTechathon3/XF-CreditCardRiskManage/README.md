## 运行指引
1. 预编译合约AggregationPrecompiled.cpp和AggregationPrecompiled.h（因为涉及实验室还在进行中的论文和专利，暂时不方便提供，还请专家们理解。预编译合约接口可以通过AggregationPrecompiled.sol查看）需要放入源码编译文件FISCO-BCOS里面的libprecompiled/extension/中，然后进行区块链的部署，以及将SDK文件拷贝进Python-SDK
2. 接下来进入WeBASE，python3 deploy.py startAll 启动区块链节点、以及WeBASE对应组件
3. 进入WeIdentity-build-service中./start.sh，启动WeIdentity部署工具
4. 进入WeIdentity-http-service/dist中./start.sh，启动Rest Service
6. 后端接口需要运行Server.py
7. WeIdentity-CPT在WeIdengtity
8. 前端运行在Front文件夹中有运行指引
9. 在前端进行交互
