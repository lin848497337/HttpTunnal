package g;

import g.server.AgentServer;
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
        int connectionSize;
        String account;
        String password;
        try{
            password = args[5];
            account = args[4];
            connectionSize = Integer.parseInt(args[3]);
            agentPort = Integer.parseInt(args[2]);
            proxyPort = Integer.parseInt(args[1]);
            proxyUrl = args[0];
        }catch (Exception e){
            System.out.println("java -jar [agent jar file] proxyIp  proxyPort agentPort connectionSize account password");
            return;
        }
        Vertx vertx = Vertx.vertx();

        if (connectionSize <= 0){
            System.out.println("connectionSize mast bigger than 0");
            return;
        }

        for (int i=0 ; i< connectionSize ; i++){
            vertx.deployVerticle(new AgentServer(agentPort, proxyUrl, proxyPort, account, password));
        }
    }
}
