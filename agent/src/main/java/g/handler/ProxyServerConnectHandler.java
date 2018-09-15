package g.handler;

import g.client.ProxyClient;
import g.client.ProxyClientImpl;
import g.proxy.FilterPipeline;
import g.proxy.filter.*;
import g.proxy.protocol.LoginMessage;
import g.util.CommonConsts;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 1. 链接 proxy
 * 2. 登录auth
 * 3. 启动 agent server
 * @author chengjin.lyf on 2018/9/2 上午10:44
 * @since 1.0.25
 */
public class ProxyServerConnectHandler implements Handler<AsyncResult<NetSocket>> {

    private static final Logger logger = LoggerFactory.getLogger(ProxyServerConnectHandler.class);

    private Vertx vertx;

    private int port;

    public ProxyServerConnectHandler(Vertx vertx, int port) {
        this.vertx = vertx;
        this.port = port;
    }

    @Override
    public void handle(AsyncResult<NetSocket> result) {
        if (result.succeeded()){
            logger.info("connect proxy server success!");
            NetSocket proxySocket = result.result();
            ProxyClient proxyClient = initProxyClient(proxySocket);

            FilterPipeline proxyReadPipeline = new FilterPipeline()
                    .addToTail(new UnpackFilter())
                    .addToTail(new DecryptFilter())
                    .addToTail(new DecodeFilter())
                    .setHandler(new ProxyDataHandler(vertx, proxyClient, port));

            proxySocket.handler(new PiplineReadHandler(proxyReadPipeline));

            logger.info("try to login");
            // all ok now try login
            LoginMessage loginMessage = new LoginMessage();
            loginMessage.setCmd(CommonConsts.COMMAND_LOGIN);
            loginMessage.setAccount("ccccc");
            loginMessage.setPassword("testc");
            proxyClient.write(loginMessage);
        }else {
            logger.error("connect to proxy server failed, please retry later or connect to the admin!");
            System.exit(1);
        }
    }

    private ProxyClient initProxyClient(NetSocket proxySocket){

        FilterPipeline outputToProxyPipeline = new FilterPipeline()
                .addToTail(new EncodeFilter())
                .addToTail(new EncryptFilter())
                .addToTail(new PackageFilter())
                .setHandler(new NetSocketHandler(proxySocket));

        ProxyClient proxyClient = new ProxyClientImpl(outputToProxyPipeline);
        return proxyClient;
    }
}
