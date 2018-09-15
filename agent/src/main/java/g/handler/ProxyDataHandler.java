package g.handler;

import java.util.Map;

import g.client.ProxyClient;
import g.proxy.protocol.BrokenBrowserMessage;
import g.proxy.protocol.BrowserMessage;
import g.proxy.protocol.LoginResponseMessage;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.net.NetServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import g.client.BrowserClient;
import g.proxy.IMessageHandler;
import g.proxy.protocol.Message;
import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/9/1 上午12:47
 * @since 1.0.25
 */
public class ProxyDataHandler implements IMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(ProxyDataHandler.class);

    private Vertx vertx;
    private ProxyClient proxyClient;
    private int port;

    public ProxyDataHandler(Vertx vertx, ProxyClient proxyClient, int port) {
        this.vertx = vertx;
        this.proxyClient = proxyClient;
        this.port = port;
    }

    @Override
    public void handle(Object obj) throws Exception {
        Message msg = (Message) obj;
        if (msg instanceof LoginResponseMessage){
            LoginResponseMessage responseMessage = (LoginResponseMessage) msg;
            if (responseMessage.getResult() == 1){
                logger.info("login proxy success!");
                // init server
                NetServer server = vertx.createNetServer();
                server.connectHandler(new BrowserConnectHandler(vertx, proxyClient));
                server.listen(port, rs -> {
                    if (rs.succeeded()) {
                        logger.info("start agent success " + port + "!");
                    }else{
                        logger.error("start agent failed!");
                        System.exit(1);
                    }
                });
            }else{
                logger.info("login proxy failed, reason: " +responseMessage.getMsg());
                System.exit(1);
            }
        }else if(msg instanceof BrokenBrowserMessage){

            BrokenBrowserMessage brokenBrowserMessage = (BrokenBrowserMessage) msg;
            long browserId = brokenBrowserMessage.getBrowserId();
            Map<Long,BrowserClient> clientMap = vertx.sharedData().getLocalMap("clientMap");
            clientMap.remove(browserId).close();
            vertx.eventBus().send("proxyBroken", Json.encode(msg));

        }else if(msg instanceof BrowserMessage){

            BrowserMessage browserMessage = (BrowserMessage) msg;
            Map<Long,BrowserClient> clientMap = vertx.sharedData().getLocalMap("clientMap");
            clientMap.get(browserMessage.getBrowserId()).write(Buffer.buffer(browserMessage.getData()));
            vertx.eventBus().send("proxyResponse", Json.encode(msg));
        }else {
            throw new UnsupportedOperationException("not support msg type : "+msg.getClass().getName());
        }

    }
}
