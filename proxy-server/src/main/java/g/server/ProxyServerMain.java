package g.server;

import g.server.message.BrokenBrowserMessageAction;
import g.server.message.BrowserMessageAction;
import g.server.message.LoginMessageAction;
import g.server.message.MessageActionFactory;
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
        MessageActionFactory.getInstance().addMessageAction(new LoginMessageAction());
        MessageActionFactory.getInstance().addMessageAction(new BrowserMessageAction());
        MessageActionFactory.getInstance().addMessageAction(new BrokenBrowserMessageAction());
        vertx.deployVerticle(new ProxyServerVerticle(proxyPort));
//        VertxContext.init(vertx, new HAGlobalOption());
//        vertx.deployVerticle(new UDPProxyServerVerticle(proxyPort, null));
//        vertx.deployVerticle(new StaticsUnitVerticle());
    }
}
