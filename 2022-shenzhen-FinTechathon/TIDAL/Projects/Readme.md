# 代码说明

本内容为代码配置与运行环境相关说明文档

## 运行环境相关

### 运行环境前置

本系统的开发环境与工具为：

- Ubuntu 20.04
- Java 11
- Solidity 0.4.25
- MySQL 5.6
- Python 3.8
- IDEA & VSCode

### 区块链底层环境搭建

本系统所依赖的底层区块链系统为 FISCO BCOS 区块链系统

该系统的配置与搭建请参考官方教程：[FISCO BCOS 搭建教程](<https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/installation.html>)

> 注：因为速度问题，我们对安装脚本进行了修改，将对应的github仓库地址加上了国内的代理前缀，以加快速度。

### 中间件管理平台搭建

本系统所依赖的中间件管理平台为 WeBASE 中间件平台

该平台的配置与搭建请参考官方教程：[WeBASE 搭建教程](<https://webasedoc.readthedocs.io/zh_CN/latest/docs/WeBASE/install.html>)

### 区块链浏览器搭建

本系统所依赖的区块链浏览器为 FISCO BCOS 官方所提供的对应区块链浏览器

该浏览器的配置与搭建请参考官方教程：[区块链浏览器搭建教程](<https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/browser/deploy.html>)

## 项目内系统配置与代码编写

### 区块链合约代码编写

本系统的区块链合约部分代码编写参照了 FISCO BCOS 区块链官方提供的区块链应用开发教程：[教程：编写第一个区块链应用](<https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/tutorial/sdk_application.html>)

### 合约代码转Java合约类

为了方便我们的开发与部署，我们利用了 FISCO BCOS 控制台下的`sol2java.sh`脚本，将合约编译为`abi`、`bin`文件的同时，得到了对应的`Java`合约类，以方便我们的Java应用中对合约进行调用。

具体来说，我们的合约转换也参照上一部分中提到的教程。

### Gradle项目与JPBC库的耦合

因为在我们的系统中，我们采用了JPBC库为我们的加密解密过程中提供数学运算支持，但现有的关于JPBC教程中并没有针对Gradle项目的配置，经过我们的调研与实验，得到的解决方法如下：

1. 首先将下载下来的JPBC项目中的Jar文件放置在项目目录下新创建的libs目录下。

2. 然后在Gradle项目内的`build.gradle`文件中对`dependencies`部分修改如下：

   ```json
   dependencies {
       implementation 'org.jetbrains:annotations:20.1.0'
       testImplementation group: 'junit', name: 'junit', version: '4.12'
       implementation ("org.fisco-bcos.java-sdk:fisco-bcos-java-sdk:2.9.1")
       implementation spring
       implementation group: 'org.slf4j', name: 'slf4j-log4j12', version: '1.7.26'
       // This part includes the vital JARs.
       implementation fileTree(dir: 'libs', include: ['*.jar'])
   }
   ```

3. 然后将`a.properties`放置在项目目录下。

经过这些操作后，我们的Gradle项目解决了不能够引入JPBC库的问题。

### 项目部署与运行

同样参照这一教程：[教程：编写第一个区块链应用](<https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/tutorial/sdk_application.html>)，将对应的文件资源放置在对应的目录下。然后参照上一部分解决JPBC的依赖问题。最后只需要运行我们核心代码中的main方法即可运行我们的程序。
