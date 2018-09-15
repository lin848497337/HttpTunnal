package g.server;

import g.server.verticle.ProxyServerVerticle;
import io.vertx.core.Vertx;

/**
 * @author chengjin.lyf on 2018/8/30 下午2:02
 * @since 1.0.25
 */
public class ProxyServerMain {

    public static void main(String args[]){
        int proxyPort = 8000;
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ProxyServerVerticle(proxyPort, null));
//        vertx.deployVerticle(new StaticsUnitVerticle());
    }
}
