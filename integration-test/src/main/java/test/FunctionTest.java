package test;

import fake.FakeBrowser;
import fake.FakeServer;
import g.server.AgentServer;
import g.server.verticle.ProxyServerVerticle;
import io.vertx.core.Vertx;

/**
 * @author chengjin.lyf on 2018/9/13 下午10:43
 * @since 1.0.25
 */
public class FunctionTest {


    public static void main(String args[]) throws InterruptedException {
        FakeServer fakeServer = new FakeServer("i am real server" , 6666, Vertx.vertx());
        fakeServer.listen();
        Thread.sleep(3000L);
        Vertx.vertx().deployVerticle(new ProxyServerVerticle(8000, null));
        Thread.sleep(3000L);
        Vertx.vertx().deployVerticle(new AgentServer(4321, "127.0.0.1", 8000));
        Vertx browserVertx = Vertx.vertx();
        Thread.sleep(3000L);

        for (int i=0 ; i<100 ; i++){
            FakeBrowser fakeBrowser = new FakeBrowser("GET 127.0.0.1:6666 http/1.1\r\n" + "test:test\r\n" + "\r\n", 4321, browserVertx);
            Thread.sleep(1L);
            fakeBrowser.connect();
        }
        Thread.sleep(500000L);

    }
}
