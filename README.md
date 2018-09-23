## HttpTunnal
### 简介 
    httpTunnal 是一个基于 [vert.x](https://vertx.io/) 的高性能http双层代理,
    通过 在服务器上部署 proxy-server 后端代理 ， 本地启动 agent 代理后即可实现双层代理链接。

### 环境依赖

    运行 server 或者 agent ,您需要在你的主机上安装:
    [java8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 或者以上版本，
    [maven](https://maven.apache.org/download.cgi) 构建工具。

### 部署步骤
 
#### 1、server
    在获取项目最新代码并且配置好依赖后，在项目根目录执行 mvn clean package -Dmaven.test.skip=true 构建java可执行jar包。
    将构建好的 server proxy-server-1.0-SNAPSHOT.jar 上传值服务器目录
    
#### 2、agent
    在一层代理主机上上传 agent-1.0-SNAPSHOT.jar 并启动
    您可以直接设置您的 http 代理为服务器为 agent 即可通过代理链接网络  

    

    
