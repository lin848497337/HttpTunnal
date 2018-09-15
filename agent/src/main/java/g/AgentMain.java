package g;

import g.server.AgentServer;
import io.vertx.core.Vertx;

/**
 * @author chengjin.lyf on 2018/8/31 上午8:44
 * @since 1.0.25
 */
public class AgentMain {

    public static void main(String args[]){
        int agentPort = 4321;
        String proxyUrl = "127.0.0.1";
        int proxyPort = 8000;
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new AgentServer(agentPort, proxyUrl, proxyPort));
        vertx.deployVerticle(new StaticsUnit());
    }
}
