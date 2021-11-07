原理：solc是solidity合约的编译器。采用二进制的solc编译器，对合约进行编译，而不是采用python代码实现的编译器来直接对合约进行编译。

## solc的发布地址：
    https://github.com/FISCO-BCOS/solidity/releases
		
**合约的编译器选择方式包括**

    1) 操作系统类型，linux/windows 
    2) 合约所用的solidity语言版本，如0.4.x，0.5.x ，0.6.x
    3) 是否国密版本

**操作次序：**

    1.在以下链接中选择对应的release发布包，下载到python-sdk/bin/solc目录，或其他可访问的目录
    2.将下载的文件解压（如为压缩包）,确认其中的solc程序有可执行权限
    3.确认python-sdk/client_config.py 文件里对solc名字或路径的定义和实际的solc能完全匹配，总之目的是使得编译命令行一定能找到对应的solc
