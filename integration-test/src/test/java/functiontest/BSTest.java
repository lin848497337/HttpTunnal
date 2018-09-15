package functiontest;

import java.util.ArrayList;
import java.util.List;

import g.server.ProxyServerMain;
import g.server.verticle.ProxyServerVerticle;
import org.junit.Assert;
import org.junit.Test;

import g.server.AgentServer;
import g.util.CommonConsts;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;

/**
 * @author chengjin.lyf on 2018/8/28 下午11:48
 * @since 1.0.25
 */
public class BSTest {

    private static int proxyPort = 8000;

    private static int agentPort = 4321;

    private static String proxyServerUrl = "127.0.0.1";


    public static int index = 0;

    @Test
    public void testSendAndReceMsg() throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ProxyServerVerticle(proxyPort, null));
        Thread.sleep(4000L);
        vertx.deployVerticle(new AgentServer(agentPort, proxyServerUrl, proxyPort));
        String sendMsg = "GET 127.0.0.1:6666 http/1.1\r\ntest:test\r\n\r\n";

        String responseMsg = "OK 200 I am real server!";

        NetServer realServer = vertx.createNetServer();
        realServer.connectHandler(socket->{
            socket.handler(buffer->{
                Assert.assertEquals(buffer.toString(), sendMsg);
                socket.write(responseMsg);
            });
        });
        realServer.listen(6666);

        System.out.println("start real server");

        Thread.sleep(4000L);

        List<Future> socketList = new ArrayList<>();
        for (int i=0; i<100;i++){
            Future<NetSocket> future = Future.future();
            socketList.add(future);
            NetClient browser = vertx.createNetClient();
            System.out.println("connect to agent" + i);
            final  int id = i;
            browser.connect(agentPort, "127.0.0.1", result->{
                if (result.succeeded()){
                    future.complete(result.result());
                    System.out.println("send to agent msg "+id);
                    NetSocket browserSocket = result.result();
                    browserSocket.exceptionHandler(e->{
                        System.out.println("failed" + e.getMessage());
                        Runtime.getRuntime().exit(0);
                    });
                    browserSocket.handler(browserBuffer->{
                        boolean isequsal = responseMsg.equalsIgnoreCase(browserBuffer.toString());
                        System.out.println("receive from real server!" + (index++) + " is equal : "+isequsal);
                        Assert.assertEquals(responseMsg, browserBuffer.toString());
                    });
                }else{
                    System.out.println("connect failed!");
                    future.fail(result.cause());
                }
            });
        }
        CompositeFuture.join(socketList).setHandler(result->{
            System.out.println("all prepare!");
            List<NetSocket> sockets = result.result().list();
            System.out.println("success connect size : "+sockets.size());
            for (NetSocket socket : sockets){
                socket.write(sendMsg);
            }
        });

        Thread.sleep(5000000L);
    }


    @Test
    public void testConnectMethod() throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        String sendMsg = "CONNECT 127.0.0.1:6666 http/1.1\r\ntest:test\r\n\r\n";



        NetServer realServer = vertx.createNetServer();
        realServer.connectHandler(socket->{
            socket.handler(buffer->{
                Assert.assertEquals(buffer.toString(), sendMsg);
            });
        });
        Thread.sleep(4000);
        realServer.listen(6666);
        System.out.println("start real server");


        NetClient browser = vertx.createNetClient();
        System.out.println("connect to agent");
        browser.connect(4321, "127.0.0.1", result->{
            if (result.succeeded()){
                System.out.println("send to agent msg");
                NetSocket browserSocket = result.result();
                browserSocket.handler(browserBuffer->{
                    Assert.assertEquals(CommonConsts.CONNECT_RESPONSE, browserBuffer.toString());
                    System.out.println("end" + browserBuffer.toString());
                    System.exit(0);
                });
                browserSocket.write(sendMsg);
            }
        });
        Thread.sleep(10000L);
    }

}
