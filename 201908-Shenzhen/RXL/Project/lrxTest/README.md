# “融信链” JAVA端 gRPC 服务端操作说明

### 1.下载依赖包

根据 `moven`文件，拉取相应的依赖包，其中，主要的依赖包有：

```
<dependency>
            <groupId>org.fisco-bcos</groupId>
            <artifactId>web3sdk</artifactId>
            <version>2.0.4</version>
            <exclusions>
                <exclusion>
                    <groupId>org.ethereum</groupId>
                    <artifactId>solcJ-all</artifactId>
                </exclusion>
            </exclusions>
            <scope>compile</scope>
        </dependency>

<!--Grpc-->
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-netty-shaded</artifactId>
            <version>1.22.1</version>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-protobuf</artifactId>
            <version>1.22.1</version>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-stub</artifactId>
            <version>1.22.1</version>
        </dependency>
```

### 2. 编译运行

进入主目录`src\main\java\bcosServer\`下

运行`CreditCertServer.java`里的主函数，开启服务，函数开启50051端口

```
八月 25, 2019 12:22:35 上午 bcosServer.CreditCertServer start
信息: Server started, listening on 50051
```

### 3.和Go语言端对接

开启服务后，等待Go语言的客户端调用方法