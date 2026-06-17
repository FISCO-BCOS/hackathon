## 开发基于FISCO BCOS区块链的iOS应用

### 1. 获得iOS SDK

您可以直接下载``FiscoBcosIosSdk.framework``库，如果您完成了下载，请跳过这一步。

#### 第一步. 准备环境

1. [安装golang 1.13.6及以上版本](https://golang.org/doc/install)

2. 安装gomobile

   ```bash
   cd ~
   # 如果您处在国内的网络环境中，请执行设置GORPOXY的步骤
   export GOPROXY=https://goproxy.cn,https://goproxy.io,direct
   # 确保GO111MODULE=on
   export GO111MODULE=on
   # 设置环境变量
   export PATH=$PATH:~/go/bin
   # 安装gomobile
   go get golang.org/x/mobile/cmd/gomobile
   ```

#### 第二步. 下载go-sdk源码

```bash
# 下载代码
mkdir -p ~/go/src/github.com/FISCO-BCOS && cd ~/go/src/github.com/FISCO-BCOS && git clone https://github.com/FISCO-BCOS/go-sdk.git && cd go-sdk
# 切换到develop分支
git checkout -b develop origin/develop
# 下载依赖
go mod download
```

#### 第三步. 编译iOS SDK

```bash
# 编译iOS SDK，当前目录~/go/src/github.com/FISCO-BCOS/go-sdk
export CGO_LDFLAGS_ALLOW=".*"
gomobile bind -target=ios -o FiscoBcosIosSdk.framework  --ldflags='-s -w' :./mobile/ios
# 编译成功后，目录下会多了一个FiscoBcosIosSdk.framework目录
```

### 2. 生成Objective-c合约

#### 第一步. 准备Solidity合约

```bash
# 当前目录~/go/src/github.com/FISCO-BCOS/go-sdk
mkdir helloworld && cd helloworld
# 将HelloWorld合约拷贝到目录下
cp ../.ci/hello/HelloWorld.sol .
```

HelloWorld.sol

```js
pragma solidity>=0.4.24 <0.6.11;

contract HelloWorld {
    string value;

    constructor() public {
        value = "Hello, World!";
    }

    function get() public view returns (string memory) {
        return value;
    }

    function set(string v) public {
        value = v;

    }
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
    [alertController addAction:cancelAction];
    [self presentViewController:alertController animated:YES completion:nil];
}

```

#### 第二步. 编译合约

```bash
# 当前目录~/go/src/github.com/FISCO-BCOS/go-sdk/helloworld
# 下载编译器
bash ../tools/download_solc.sh -v 0.4.25
# 编译合约
./solc-0.4.25 --bin --abi -o ./ ./HelloWorld.sol
# 得到HelloWorld.abi, HelloWorld.bin, HelloWorld.sol, solc-0.4.25， 生成了ABI和Bin文件
```

#### 第三步. 生成Objective-c调用接口

```bash
# 当前目录~/go/src/github.com/FISCO-BCOS/go-sdk/helloworld
# 编译生成abigen工具
go build ../cmd/abigen
# 生成Objective-c合约
./abigen --bin ./HelloWorld.bin --abi ./HelloWorld.abi --lang objc --pkg helloworld --type HelloWorld --out ./HelloWorld.m
ls
# 得到了HelloWorld.h，HelloWorld.m Objective-C调用接口
```



### 3. 开发基于FISCO BCOS区块链的iOS应用

#### 第一步. 准备环境

安装Xcode：请打开AppStore -> 搜索Xcode -> 点击Get

#### 第二步. 新建一个iOS应用项目

打开Xcode

点击Create a new Xcode project

选择``iOS``，``App``，点击``next``

在Product Name中输入``HelloWorld``, 并选择属性 ``Interface:Storyboard``, ``LifeCycle:UIKit App Delegate``,`` language:Objective-C1``, 点击``Next``

选择项目所在的文件夹，点击``Create``，完成项目创建。

#### 第三步. 将合约和iOS SDK引入项目中

将合约和iOS SDK放入项目中

```bash
# 当前位置 ~/go/src/github.com/FISCO-BCOS/go-sdk/helloworld
# 请替换 ~/code 为您存放HelloWorld项目的正确目录
cp -r HelloWorld.h HelloWorld.m ../FiscoBcosIosSdk.framework 你的项目路径/HelloWorld/HelloWorld/
# 拷贝私钥文件
cp ../.ci/0x83309d045a19c44dc3722d15a6abd472f95866ac.pem 你的项目路径/HelloWorld/HelloWorld/key.pem
```

私钥也可以[参考这里](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/account.html)，使用工具生成。

打开Finder，进入项目目录HelloWorld/HelloWorld，得到我们刚刚复制进去的文件和目录包括HelloWorld.h、HelloWorld.m、FiscoBcosIosSdk.framework、key.pem。**选中这些文件和目录拖入Xcode中左边的项目文件夹结构的HelloWorld > HelloWorld下，点击确认**。

#### 第四部. 搭建区块链网络和代理

区块链网络搭建：https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/installation.html

搭建代理：https://github.com/FISCO-BCOS/bcos-node-proxy/tree/feature_mobile_http

#### 第五步. 开发应用

**修改页面**

点击Main.storyboard, 展示手机界面

创建一个按钮。点击``+`` 按钮, 选择Button，并拖拽至页面。修改按钮名称“Button” -> "Deploy"

创建一个按钮。点击``+`` 按钮, 选择Button，并拖拽至页面。修改按钮名称“Button” -> "Set"

创建一个输入框。点击``+`` 按钮, 选择Text Field，并拖拽至页面。

创建一个按钮。点击``+`` 按钮, 选择Button，并拖拽至页面。修改按钮名称“Button” -> "Get"

**创建Button相关的事件**

同时按下Control+Option+Command+Enter四个键，打开辅助功能。

添加Deploy按钮按下事件。右键手机屏幕上的Set按钮，在弹出的菜单中找到Touch Down，按住右边的圆圈并拖拽鼠标至下方的代码的``- (void)viewDidLoad``函数结束的下一行，并输入Name为deploy，点击connect，完成Deploy按钮按下事件的设置。

添加Set按钮按下事件。右键手机屏幕上的Set按钮，在弹出的菜单中找到Touch Down，按住右边的圆圈并拖拽鼠标至下方的代码的``- (IBAction)deploy:(id)sender``函数结束的下一行，并输入Name为set，点击connect，完成Set按钮按下事件的设置。

添加Get按钮按下事件。右键手机屏幕上的Get按钮，在弹出的菜单中找到Touch Down，按住右边的圆圈并拖拽鼠标至下方的代码的``- (IBAction)set:(id)sender``函数结束的下一行，并输入Name为get，点击connect，完成Get按钮按下事件的设置。

添加文本框作为属性。按住Control，点击手机屏幕上的文本框，拖动至下方代码的``@interface ViewControler()``的下一行，输入Name为setValue。

事件创建完成，得到如下ViewController.m的文件。

```objc
#import "ViewController.h"

@interface ViewController ()
@property (weak, nonatomic) IBOutlet UITextField *setValue;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}
- (IBAction)deploy:(id)sender {
}
- (IBAction)set:(id)sender {
}
- (IBAction)get:(id)sender {
}
@end
```

**实现消息发送类**

在HelloWorld/HelloWorld文件夹下单击右键``New File..``， 选择``iOS``,`` Cocoa Touch Class``，点击``next``

输入``Class: MyPostCallback``, ``Subclass of: 空``,`` language:Objective-C1``, 点击``Next`` , 然后点击``Create``，创建出MyPostCallback.h和MyPostCallback.m

MyPostCallback.h

```objc
#import <FiscoBcosIosSdk/FiscoBcosIosSdk.h>

NS_ASSUME_NONNULL_BEGIN

@interface MyPostCallback : NSObject<MobilePostCallback>

@end

NS_ASSUME_NONNULL_END
```

MyPostCallback.m

```objc
#import "MyPostCallback.h"

@implementation MyPostCallback

- (NSString* _Nonnull)sendRequest:(NSString* _Nullable)rpcRequest{
    NSURL *nsurl = [NSURL URLWithString:@"http://localhost:8170/Bcos-node-proxy/rpc/v1"];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:nsurl];

    //设置请求类型
    request.HTTPMethod = @"POST";

    //将需要的信息放入请求头
    [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];//token

    //把参数放到请求体内
    request.HTTPBody = [rpcRequest dataUsingEncoding:NSUTF8StringEncoding];

    NSData *resultData = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil] ;
    NSString * resultString = [[NSString alloc] initWithData:resultData encoding:NSUTF8StringEncoding];
    NSLog(@"Get response: %@",resultString);
    return resultString;

}
@end
```

当你使用HTTP协议的Proxy时，请注意修改安全设置：

在项目的info.plist中添加一个Key：App Transport Security Settings，类型为字典类型。然后给它添加一个Key：Allow Arbitrary Loads，类型为Boolean类型，值为YES。

具体的，点击info.plist。在空白处右键``Add Row``, 输入``App Transport Security Settings``。在这个节点处点``+``按钮，添加新Key ，``Allow Arbitrary Loads``,类型为Boolean类型，值为``YES``。

打开Finder将HelloWorld项目下HelloWorld/key.pem拖到Xcode右边项目栏HelloWorld文件夹下，得到目录

```
HelloWorld
|-----HelloWorld
|			|-----FiscoBcosIosSdk.framework
|			|-----HelloWorld.m
|			|-----HelloWorld.h
|			|-----Key.pem
|			|-----AppDelegate.h
|			|-----AppDelegate.m
|			|-----SceneDelegate.h
|			|-----SceneDelegate.m
|			|-----Main.storyboard
|			|-----Assets.xcassets
|			|-----LaunchScrean.storyboard
|			|-----Info.plist
|			|-----main.m
|			|-----MyPostCallback.h
|			|-----MyPostCallback.m
|
|-----HelloWorldTest (省略内部文件)
|-----HelloWorldUITest (省略内部文件)
|-----Products (省略内部文件)
```

**调用Objective-C合约**

在ViewController.m中实现方法

```objc
//
//  ViewController.m
//  HelloWorld
//
//  Created by Maggie WU on 2021/1/28.
//

#import "ViewController.h"
#import <FiscoBcosIosSdk/FiscoBcosIosSdk.h>
#import "MyPostCallback.h"
#import "HelloWorld.h"

@interface ViewController ()
@property (weak, nonatomic) IBOutlet UITextField *setValue;
@property (nonatomic,strong)MobileBcosSDK* sdk;
@property HelloWorld * contract;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    NSString *path = [NSBundle mainBundle].bundlePath;
    NSString *keyFile = [NSString stringWithFormat:@"%@/%@", path, @"key.pem" ];

    [super viewDidLoad];
    self.sdk = [[MobileBcosSDK alloc]init];
    MobileBuildSDKResult* result = [self.sdk buildSDKWithParam:keyFile groupID:1 chainID:1 isSMCrypto:false callback:[[MyPostCallback alloc] init]];
}
- (IBAction)deploy:(id)sender {
    UIAlertController *alertController;
    self.contract = [[HelloWorld alloc]init:self.sdk];
    MobileReceiptResult *dr = [self.contract deploy];
    long zero = 0;
    if (dr.code != zero){
        NSLog(@"send tx error : %@", dr.message);
        alertController = [UIAlertController alertControllerWithTitle:@"Result" message:dr.message preferredStyle:UIAlertControllerStyleAlert];
    }else{
        NSLog(@"send tx success : %@", dr.receipt.contractAddress);
        alertController = [UIAlertController alertControllerWithTitle:@"Result" message:dr.receipt.contractAddress preferredStyle:UIAlertControllerStyleAlert];
        self.contract = [self.contract initWithAddress:dr.receipt.contractAddress sdk:self.sdk];
    }
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
    [alertController addAction:cancelAction];
    [self presentViewController:alertController animated:YES completion:nil];
}
- (IBAction)set:(id)sender {
    UIAlertController *alertController;
    MobileReceiptResult *result = [self.contract set: self.setValue.text];

    long zero = 0;
    if (result.code != zero){
        NSLog(@"send tx error : %@", result.message);
        alertController = [UIAlertController alertControllerWithTitle:@"Result" message:result.message preferredStyle:UIAlertControllerStyleAlert];
    }else{
        NSLog(@"send tx success : %@", result.receipt.blockNumber);
        alertController = [UIAlertController alertControllerWithTitle:@"Result" message:result.receipt.blockNumber preferredStyle:UIAlertControllerStyleAlert];
    }
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
    [alertController addAction:cancelAction];
    [self presentViewController:alertController animated:YES completion:nil];
}
- (IBAction)get:(id)sender {
    UIAlertController *alertController;
    MobileCallResult *result = [self.contract get];
    long zero = 0;
    if (result.code != zero){
        NSLog(@"send tx error : %@", result.message);
        alertController = [UIAlertController alertControllerWithTitle:@"Result" message:result.message preferredStyle:UIAlertControllerStyleAlert];
    }else{
        NSLog(@"send tx success : %@", result.result);
        alertController = [UIAlertController alertControllerWithTitle:@"Result" message:result.result preferredStyle:UIAlertControllerStyleAlert];
    }
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
    [alertController addAction:cancelAction];
    [self presentViewController:alertController animated:YES completion:nil];
}
@end
```

**运行应用**

在标题栏设置设备为``iPhone12``，再点击标题栏左上角的三角形的运行按钮。

在显示的模拟器中进行合约操作。

首先，点击Deploy按钮部署HelloWorld合约。

然后，点击Get按钮显示值。

接着，在文本框中填写你想设置的值，点击Set按钮。

最后，点击Get按钮显示值。