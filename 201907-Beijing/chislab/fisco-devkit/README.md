# 本项目为FISCO BCOS 2.0的开发套件fisco devkit, 如需使用1.3版本, 请进行分支切换到1.3-fisco分支

## 项目说明
本项目旨在构建基于FISCO BCOS的开发套件，以便开发者迅速地搭建开发环境和测试，提供的功能包括Docker搭链、合约编译、多语言SDK和性能测试。开发者可以通过本项目迅速地搭链，对合约进行部署测试，并转化为相应语言的SDK，并且集成了链的性能测试模块caliper，方便开发者进行性能测试。

此分支在FISCO BCOS 2.0的底层链上，实现了nodejsSDK，并集成了性能测试模块，goSDK目前只支持1.3.x版本。
有疑问请联系作者 flysommer@outlook.com

## 环境依赖
0. 工具可以运行在macos，泛linux内核(如archlinux, ubuntu, fedora, centos)
1. 需要提前安装好的: docker docker-compose node@8.*

## 使用方法
### 1  工程初始化与合约编译
  将编写好的合约放入contracts目录(目录里有实例合约)

  工程目录初始化
  ```
  make init 
  ```
  配置好合约名, 修改truffle-config.js, Makefile的deps区域的solc版本,需要编译的合约名，及节点的运行位置

  开始编译, 如果合约不能编译成功，更改Makefile里面的编译器版本号对应到合约的版本号。再进行编译
  ```
  make deps
  ```


### 2  合约部署与功能测试
  部署合约
  ```
  node operations.js 1
  ```

  查看合约是否部署成功(必须), 此步骤会将合约地址写入文件(build/HelloWorld.address)
  ```
  node operations.js 2 '第一步获得的交易哈希值'
  ```
  
  调用合约的get()方法, 获取合约里面存储的字段
  ```
  node operations.js 3
  ```

  调用合约的set()方法, set的值为当前的UTC时间字符串
  ```
  node operations.js 4
  ```

  再次调用get()方法，查看新写入合约的值
  
### 3 接口性能测试
```
make bench
```
输出接口性能测试报告, 合约接口的性能测试参照benchmark/HelloWorld/get.js和benchmark/HelloWorld/set.js进行改动调用

### 若是自己编写的合约，可以参照operations.js进行修改，使用自己的方法进行调用
