package g.proxy;

import g.server.verticle.ProxyServerVerticle;
import io.vertx.core.Vertx;
import lombok.Data;

/**
 * @author chengjin.lyf on 2018/9/14 上午10:38
 * @since 1.0.25
 */
@Data
public class ProxyServerManager {

    private int port;

    public void start(){
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ProxyServerVerticle(port, AgentClientImpl.class));
    }
}
