package g;

import g.server.AgentServer;
import g.server.UDPAgentServer;
import io.vertx.core.Vertx;

/**
 * @author chengjin.lyf on 2018/8/31 上午8:44
 * @since 1.0.25
 */
public class AgentMain {

    public static void main(String args[]){
        int agentPort;
        String proxyUrl;
        int proxyPort;
        try{
            agentPort = Integer.parseInt(args[2]);
            proxyPort = Integer.parseInt(args[1]);
            proxyUrl = args[0];
        }catch (Exception e){
            System.out.println("java -jar proxyIp  proxyPort agentPort");
            return;
        }
        Vertx vertx = Vertx.vertx();
        VertxContext.init(vertx, new HAGlobalOption());

        for (int i=0 ; i< 10 ; i++){
            vertx.deployVerticle(new AgentServer(agentPort, proxyUrl, proxyPort));
        }
//        vertx.deployVerticle(new UDPAgentServer(agentPort, proxyUrl, proxyPort));
//        vertx.deployVerticle(new StaticsUnit());
    }
}
