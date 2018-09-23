package g.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import g.handler.ProxyServerConnectHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;

/**
 * @author chengjin.lyf on 2018/8/31 上午8:21
 * @since 1.0.25
 */
public class AgentServer extends AbstractVerticle {

    private int port;

    private String proxyServerIp;
    private int proxyServerPort;
    private String account;
    private String password;

    private Logger logger = LoggerFactory.getLogger(AgentServer.class);

    public AgentServer(int agentPort, String proxyServerIp, int proxyServerPort, String account, String password) {
        this.port = agentPort;
        this.proxyServerIp = proxyServerIp;
        this.proxyServerPort = proxyServerPort;
        this.account = account;
        this.password = password;
    }

    @Override
    public void start() throws Exception {
        Vertx vertx = getVertx();
        logger.info("init agent server !");
        NetClient client = vertx.createNetClient();
        client.connect(proxyServerPort, proxyServerIp,
                new ProxyServerConnectHandler(vertx, port, account, password));
    }

    @Override
    public void stop() throws Exception {
        logger.info("agent server stop!");
    }
}
