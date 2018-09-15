package g.client;

import g.http.HttpMessageHeader;
import g.proxy.protocol.BrowserMessage;
import g.util.CommonConsts;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chengjin.lyf on 2018/9/13 下午9:24
 * @since 1.0.25
 */
public class BrowserClientImpl implements BrowserClient {

    private static final Logger logger = LoggerFactory.getLogger(BrowserClientImpl.class);

    private long uuid;

    private Vertx vertx;

    private ProxyClient proxyClient;

    private NetSocket browserClient;


    public BrowserClientImpl(long uuid, Vertx vertx, ProxyClient proxyClient, NetSocket browserClient) {
        this.uuid = uuid;
        this.vertx = vertx;
        this.proxyClient = proxyClient;
        this.browserClient = browserClient;
    }


    @Override
    public void handle(Buffer buf) {
        BrowserMessage message = new BrowserMessage();
        message.setBrowserId(uuid);
        message.setData(buf.getBytes());
        message.setCmd(CommonConsts.COMMAND_MSG);
        vertx.eventBus().send("requestProxy", Json.encode(message));
        try {
            proxyClient.write(message);
        } catch (Exception e) {
            logger.error("do output failed!", e);
        }
    }

    @Override
    public void write(Buffer buffer) {
        browserClient.write(buffer);
    }

    @Override
    public void close() {
        browserClient.close();
    }
}
