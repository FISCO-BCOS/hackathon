# “融信链” Go客户端 gRPC 操作说明

### 1. 下载gRPC的Go依赖包

`golang` 安装`gRpc`，其官方安装命令是：

```
go get google.golang.org/grpc
```

是安装不起的，会报错：

```
package google.golang.org/grpc: unrecognized import path "google.golang.org/grpc"(https fetch: Get https://google.golang.org/grpc?go-get=1: dial tcp 216.239.37.1:443: i/o timeout)
```

由于下载速度很慢，所以不能使用go get的方式安装，正确的安装方式：

```
git clone https://github.com/grpc/grpc-go.git $GOPATH/src/google.golang.org/grpc
git clone https://github.com/golang/net.git $GOPATH/src/golang.org/x/net
git clone https://github.com/golang/text.git $GOPATH/src/golang.org/x/text
go get -u github.com/golang/protobuf/{proto,protoc-gen-go}
git clone https://github.com/google/go-genproto.git $GOPATH/src/google.golang.org/genproto

cd $GOPATH/src/
go install google.golang.org/grpc
```

最后在 `$GOPATH/src` 目录下可以找到这些依赖包。

### 2. 利用 gRPC 的依赖包生成 proto 文件

在github上下载proto插件，用于生成 proto 文件，配置好环境变量

运行命令，生成  `.pb.go` 文件：

```
$ protoc --go_out=plugins=grpc:. route_guide.proto
```

我们已经生成了文件 `connect1.pb.go`

### 3. 运行

在github上克隆这段代码，放到 `$GOPATH/src`目录下

进入主目录 `GoRXL\myRXL\web`,开三个终端，分别运行主函数：

```
go run main_firm.go
go run main_persion.go
go run main_school.go
```

或者运行`go build` 命令，生成 `.exe` 文件