package g.server.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import g.server.verticle.handler.ProxyServerHandler;
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

    public ProxyServerVerticle(int listenPort) {
        this.listenPort = listenPort;
    }

    @Override
    public void start() {
        Vertx vertx = getVertx();
        netServer = vertx.createNetServer();
        netServer.connectHandler(new ProxyServerHandler(vertx));
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
