# B-Recycle Battery Recycle Platform

## Architecture
B-Recycle a battery recycle platform which is base on FISCO BCOS. We use Vue+SpringBoot
to build our frontend and backend, plunge in Java-SDK which is provided by FISCO BCOS. 
We also use WeEvent, a blockchain based message queue.

project architecture:
```
-- B-Recycle
    |-- contract
    |-- docs
    |-- front
    |-- script
    `-- server
```
- contract: solidity contract
- docs: Sequence diagram or other .puml files
- front: Vue project 
- script: SQL script
- server: SpringBoot based project

## dependency
- Nodejs
- OpenJDK 12+
- FISCO BCOS blockchain core sever
- WeEvent message broker server
- MySQL
- MongoDB
- Redis

## Configuration
### Java server
`server/src/main/resources/application.yml`
```
# initial port is 8090
server:
  port: 8090

spring:
  ……
  datasource:
    # MySQL related config
    url: jdbc:mysql://localhost:3306/brecycle?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
  ……
  redis:
    # Redis related config
    database: 0
    host: 127.0.0.1
    ……
    port: 6379
  data:
    # Mongodb related config
    mongodb:
      host: 127.0.0.1
      port: 27017
      database: brecycle
……
# copy FISCO BCOS's nodes cert in relative path
fisco:
  cryptoMaterial:
    certPath: "conf"
    caCert: "conf/ca.crt"
    sslCert: "conf/sdk.crt"
    sslKey: "conf/sdk.key"
# FISCO BCOS nodes list
  network:
    peers:
    - "192.168.111.128:20201"
    - "192.168.111.128:20200"
……

# WeEvent config
weevent:
  # WeEvent broker server url
  brokerUrl: "http://192.168.111.128:8091/weevent-broker"
……

point:
  ……
  # pointController contract addr
  pointController: "0xbb4ae196c032c53ce856289acb76c36771e8a74e"
```
configuration spec：
1. server port：server.port
2. MySQL url：spring.datasource:url
3. Redis connection：spring.redis.host/port/database
4. MongoDB connection：spring.data.mongodb.host/port/database
5. Fisco BCOS cert and nodes list
6. WeEvent broker server url: weevent.brokerUrl
7. pointController contract addr: point.pointController

### front server
`front/.env.serve-dev`
```
# java server url
VITE_APP_PROXY_URL = 'http://localhost:8090/'
```

## System Architecture
![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/%E7%B3%BB%E7%BB%9F%E6%9E%B6%E6%9E%84%E5%9B%BE.png?raw=true)

## key business sequence diagram
### user regist
![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/%E7%94%A8%E6%88%B7%E6%B3%A8%E5%86%8C-%E7%94%A8%E6%88%B7%E6%B3%A8%E5%86%8C.png?raw=true)

### user login
![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/%E7%94%A8%E6%88%B7%E7%99%BB%E5%BD%95-%E7%94%A8%E6%88%B7%E7%99%BB%E5%BD%95.png?raw=true)

### battery produce
![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/%E7%94%B5%E6%B1%A0%E7%94%9F%E4%BA%A7-%E7%94%B5%E6%B1%A0%E7%94%9F%E4%BA%A7.png?raw=true)

### battery transfer
![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/%E7%94%B5%E6%B1%A0%E6%B5%81%E8%BD%AC-%E7%94%B5%E6%B1%A0%E6%B5%81%E8%BD%AC.png?raw=true)

### battery recycle
![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/%E7%94%B5%E6%B1%A0%E5%9B%9E%E6%94%B6-%E7%94%B5%E6%B1%A0%E5%9B%9E%E6%94%B6.png?raw=true)

### enterprise access
![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/%E4%BC%81%E4%B8%9A%E5%87%86%E5%85%A5-%E4%BC%81%E4%B8%9A%E5%87%86%E5%85%A5%EF%BC%88%E5%9B%9E%E6%94%B6%E5%95%86%EF%BC%89.png?raw=true)

### point transfer
![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/%E7%A7%AF%E5%88%86%E6%B5%81%E8%BD%AC-%E7%A7%AF%E5%88%86%E6%B5%81%E8%BD%AC.png?raw=true)