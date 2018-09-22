package g.server.verticle;

import g.HASocket;
import g.server.agent.AbstractAgentClient;
import g.server.verticle.handler.ProxyServerHandler;
import g.server.verticle.handler.UDPProxyServerHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chengjin.lyf on 2018/9/20 上午7:26
 * @since 1.0.25
 */
public class UDPProxyServerVerticle extends AbstractVerticle {

    private int port;
    private Class<? extends AbstractAgentClient> agentClientClass;
    private static Logger logger = LoggerFactory.getLogger(UDPProxyServerVerticle.class);

    public UDPProxyServerVerticle(int port, Class<? extends AbstractAgentClient> agentClientClass) {
        this.port = port;
        this.agentClientClass = agentClientClass;
    }



    @Override
    public void start() throws Exception {
        DatagramSocket socket = vertx.createDatagramSocket(new DatagramSocketOptions());
        Vertx vertx = getVertx();
        HASocket server = new HASocket(socket);
        server.acceptHandler(new UDPProxyServerHandler(vertx, agentClientClass, socket));
        server.listen(port);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
