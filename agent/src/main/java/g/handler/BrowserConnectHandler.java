package g.handler;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import g.client.BrowserClientImpl;
import g.proxy.protocol.BrokenBrowserMessage;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import g.client.BrowserClient;
import g.client.ProxyClient;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;

/**
 * @author chengjin.lyf on 2018/9/1 上午12:54
 * @since 1.0.25
 */
public class BrowserConnectHandler implements Handler<NetSocket> {

    private Vertx vertx;

    private static final Logger logger = LoggerFactory.getLogger(BrowserConnectHandler.class);

    private ProxyClient proxyClient;

    private static AtomicLong uuid = new AtomicLong();


    public BrowserConnectHandler(Vertx vertx, ProxyClient proxyClient) {
        this.vertx = vertx;
        this.proxyClient = proxyClient;
    }

    @Override
    public void handle(NetSocket browserSocket) {
        long _uuid = uuid.addAndGet(1);
        vertx.eventBus().send("connect", _uuid);

        BrowserClient browserClient = new BrowserClientImpl(_uuid, vertx, proxyClient, browserSocket);

        Map<Long, BrowserClient> clientMap = vertx.sharedData().getLocalMap("clientMap");
        clientMap.put(_uuid, browserClient);

        browserSocket.handler(browserClient);

        browserSocket.closeHandler(Void->{
            BrokenBrowserMessage brokenBrowserMessage = new BrokenBrowserMessage();
            brokenBrowserMessage.setBrowserId(_uuid);
            vertx.eventBus().send("brokenProxy", Json.encode(brokenBrowserMessage));
            proxyClient.write(brokenBrowserMessage);
        });
    }
}
