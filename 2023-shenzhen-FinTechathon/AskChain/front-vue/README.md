## 安装步骤

首先安装 node.js

接着可以克隆运行项目了

```
git clone -b dev https://gitee.com/U201915000/front-vue.git      // 把模板下载到本地
cd front-vue    // 进入模板目录

npm install  --legacy-peer-deps       // 安装项目依赖，等待安装完成之后，安装失败可用 cnpm 或 yarn
// 如果出现版本问题，请加上参数--force或者 --legacy-peer-deps

// 开启服务器，浏览器访问 http://localhost:8081
npm run dev

// 执行构建命令，生成的dist文件夹放在服务器下即可访问
npm run build
```
