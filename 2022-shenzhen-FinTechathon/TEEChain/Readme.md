### 环境

使用的操作系统为 Gramine 1.3.1版本 [Quick start — Gramine documentation](https://gramine.readthedocs.io/en/v1.2/quickstart.html#prerequisites)

可信执行环境（TEE）为SGX2.0版本

使用的FISCO BCOS为3.1.0版本。 [FISCO BCOS v3 v3.1.0 文档 (fisco-bcos-doc.readthedocs.io)](https://fisco-bcos-doc.readthedocs.io/zh_CN/latest/docs/develop/stress_testing.html)

### 编译过程

我们在本地使用Gramine拉取FISCO BCOS的文件，编译源码后得到air版本的二进制文件。

随后创建一个本地的区块链网络，此处可参考FISCO BCOS官方文档中的教程。

将该区块链网络中的二进制文件更换为编译好的air版文件。

编写Gramine中的Makefile和fisco-bcos.manifest.template从而生成在SGX中运行二进制文件所需要的相关配置文件。

在fisco-bcos.manifest.template中调整参数，从而让air版的二进制文件更好的适配SGX

编译每个节点，随后更改启动脚本中的命令，将其更换为用gramine启动fisco的命令。

随后即可开启节点。

