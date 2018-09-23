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
    将构建好的 server proxy-server-1.0-SNAPSHOT.jar 上传至服务器目录，
    同目录下创建 user.json，配置用户密码，执行startup.sh启动
    您可以修改startup.sh文件来修改您的启动监听端口，默认为8000
    
#### 2、agent
    在一层代理主机上上传 agent-1.0-SNAPSHOT.jar 并执行
    
    java -jar agent-1.0-SNAPSHOT.jar {proxy server ip} {proxy server port} {agent port} {connection size} {account} {password}
    
    {proxy server ip} 替换为您的 server主机ip。
    {proxy server port} 替换为您的 server 主机port。
    {agent port} 为agent代理的监听端口。
    {connection size} 为agent 与 server 的连接数，加大连接数可以提高您的网络吞吐量，建议连接数设置为10以上。
    {account} 为 agent链接server 需要的用户名。
    {password} 为 agent链接server 需要的密码，您可以在 server的 user.json中配置您的用户名和密码。
    
    您可以直接设置您的 http 代理为服务器为 agent 即可通过代理链接网络  

    

    
