# FBoost Visucal Code Extension

## 项目地址

[fboost-vscode-extension](https://github.com/includeleec/fboost-vscode-extension)，欢迎提 pr。

## 项目简介

FBoost Visucal Code Extension 是针对 [Visual Code](https://code.visualstudio.com/) 设计的 [FISCO BCOS](http://fisco-bcos.org/zh/) 扩展插件。

## 项目背景

在比赛中我们发现在 FISCO BCOS 联盟的部署和调试过程中，有大量重复的命令需要输入，所以萌生出依托与目前开发者使用很多的 Visual Code 开发快捷键插件。

## 项目目标

方便开发者们更快、更方便的使用 FISCO BCOS，不必再记忆各种命令，简化操作。

## Features

主要根据 [FISCO BCOS 文档](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/installation.html) 集成相关命令，通过 `ctrl+shift+p (win)` 或者 `command+shift+p (mac)` 调出快捷命令，使用户在 Visual Code 中便捷操作 FISCO BCOS。

### Deploy

FISCO BCOS 的部署与启动。

| 命令                                    |                  说明                  |
| :-------------------------------------- | :------------------------------------: |
| `FBoost:[Deploy](1).Initial envirement` | 设置初始化环境，可选 local 或者 remote |
| `FBoost:[Deploy](2).Install dependency` |              安装相关依赖              |
| `FBoost:[Deploy](3).Set node base`      |        设置 FISCO BCOS 部署目录        |
| `FBoost:[Deploy](4).Build chain`        |            构建 FISCO BCOS             |
| `FBoost:[Deploy](5).Start nodes`        |                启动节点                |

### Check

检查 FISCO BCOS 节点相关状态。

| 命令                                    |     说明     |
| :-------------------------------------- | :----------: |
| `FBoost:[Check] nodes process`          | 检查节点配置 |
| `FBoost:[Check] logs`                   |   查看日志   |
| `FBoost:[Check] nodes consensus status` | 检查共识状态 |

### Console

FISCO BCOS 控制台部署与启动。

| 命令                                           |        说明        |
| :--------------------------------------------- | :----------------: |
| `FBoost:[Console](1).Install console`          |     部署控制台     |
| `FBoost:[Console](2).Copy console config`      | 拷贝控制台配置文件 |
| `FBoost:[Console](3).Copy console certificate` |   配置控制台证书   |
| `FBoost:[Console] Start console`               |     启动控制台     |

### Scaffold

FISCO BCOS 的脚手架工程。

| 命令                                    |          说明          |
| :-------------------------------------- | :--------------------: |
| `FBoost:[Scaffold] Spring Boot Starter` | Spring Boot 脚手架工程 |

## TODO

- 增加 Python Scaffold (ing)
- 支持 deploy FISCO BCOS Docker
- 支持 deploy smart contract

## Release Notes

### 0.1

初代版本，在 DoraHacks 2019 比赛时构思，设计和开发。
