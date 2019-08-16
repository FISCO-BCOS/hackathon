# “第四次工业革命极客马拉松”微众银行区块链分赛场参赛作品

## 提交说明

1. 每个队伍请在本目录创建文件夹，文件夹中用于展示项目介绍、合约代码等。
2. 如果需要提交图片或大于500KB文件，请使用[git-lfs](https://git-lfs.github.com/)，由于git-lfs等组件对中文支持不友好，文件夹和文件名请使用英文。
3. 参考格式
    + 项目类：项目简介、项目背景、项目目标、方案等
    + 工具类：项目说明、环境依赖、使用方法等

4. 参赛队伍可以在各自子目录`Contrcts`目录下提交Solidity合约。合约接口需要有说明文档，要求提供通过[console](https://github.com/FISCO-BCOS/console)、[nodejs-sdk](https://github.com/FISCO-BCOS/nodejs-sdk)或[python-sdk](https://github.com/FISCO-BCOS/python-sdk)调用的操作截图。
5. 参赛队伍可以在各自子目录`Project`目录下提交项目代码。项目代码要求提供运行指引且需要经过FISCO BCOS团队审核。项目代码要求安装步骤清晰、保证能运行和项目干净清晰。项目详细介绍请[参考这里的格式](example_project/README.md)提交。

## [参赛作品（部分）](https://mp.weixin.qq.com/s/1hiij7bteHIvg2ypBma2fg)

+ [启思队：FISCO BCOS多语言开发套件](https://github.com/FISCO-BCOS/hackathon/tree/master/201907-Beijing/chislab)

本项目旨在构建基于FISCO BCOS的开发套件，以便开发者迅速地搭建开发环境和测试，提供的功能包括Docker搭链、合约编译、NodeJS与Go语言的SDK和性能测试。开发者可以通过本项目迅速地搭链，对合约进行部署测试，并转化为相应语言的SDK，并且集成了链的性能测试模块caliper，方便开发者进行性能测试。
