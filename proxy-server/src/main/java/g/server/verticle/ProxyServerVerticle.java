package g.server.verticle;

import g.server.agent.AbstractAgentClient;
import g.server.agent.AgentClient;
import g.server.verticle.handler.ProxyServerHandler;
import g.server.verticle.handler.SimpleProxyServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;

/**
 * @author chengjin.lyf on 2018/8/30 上午1:36
 * @since 1.0.25
 */
public class ProxyServerVerticle extends AbstractVerticle {


    private int listenPort;

    private NetServer netServer;

    private static final Logger logger = LoggerFactory.getLogger(ProxyServerVerticle.class);

    private Class<? extends AbstractAgentClient> agentClientClass;

    public ProxyServerVerticle(int listenPort, Class<? extends AbstractAgentClient> agentClientClass) {
        this.listenPort = listenPort;
        this.agentClientClass = agentClientClass;
    }

    @Override
    public void start() {
        Vertx vertx = getVertx();
        netServer = vertx.createNetServer();
        netServer.connectHandler(new ProxyServerHandler(vertx, agentClientClass));
        netServer.exceptionHandler(e ->{
            logger.error("net exception", e);
        });
        netServer.listen(listenPort, result -> {
            if (result.succeeded()) {
                logger.info("start proxy server success @ " + listenPort);
            } else {
                logger.error("start proxy server failed @ " + listenPort);
                System.exit(1);
            }
        });
    }

    @Override
    public void stop() throws Exception {
        netServer.close();
    }
}
