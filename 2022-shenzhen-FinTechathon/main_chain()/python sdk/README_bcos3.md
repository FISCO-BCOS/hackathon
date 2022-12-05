支持FISCO BCOS 3.x接口: [JSON-RPC](https://fisco-bcos-doc.readthedocs.io/zh_CN/latest/docs/develop/api.html)。对于FISCO BCOS3.x，客户端采用Python包装C语言开发的底层SDK库，由SDK库负责网络协议封装和安全通信细节。


## 代码实现
在bcos3sdk目录:

- bcos3sdk_wrap.py : 将C语言库包装到Python的实现，基本上是对[C语言SDK的接口](https://fisco-bcos-doc.readthedocs.io/zh_CN/latest/docs/develop/sdk/c_sdk/api.html)逐一进行映射，熟悉python ctypes的话看起来毫无压力
- bcos3client.py : 基于bcos3sdk_wrap.py,增加可配置特性，适当简化接口

其他
- console3.py : 面向FISCO BCOS 3.x的控制台入口文件
- console_utils/cmd_bcos3_transaction,cmd_bcos3_rpc 等包含"bcos3"关键字的文件，是面向控制台的命令实现，基本上就是解析输入参数，调用bcos3client.py,并在屏幕上打印信息


## 编解码实现

- 面向合约的abi编解码采用Python实现，这部分FISCO BCOS 3.x和2.x没有区别
- 面向通信的协议编解码采用开源tars框架协议，不再适用2.x的RLP。由底层库实现，在python层无需关注细节。


配置说明参见 [README.md](./README.md) 里的相关段落

 