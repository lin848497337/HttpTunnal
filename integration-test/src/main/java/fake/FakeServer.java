package fake;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;

/**
 * @author chengjin.lyf on 2018/9/13 下午10:37
 * @since 1.0.25
 */
public class FakeServer {

    private String msg;
    private int port;
    private Vertx vertx;

    public FakeServer(String msg, int port, Vertx vertx) {
        this.msg = msg;
        this.port = port;
        this.vertx = vertx;
    }

    public void listen(){
        NetServer netServer = vertx.createNetServer();
        netServer.connectHandler(netSocket -> {
           netSocket.handler(buffer -> {
                netSocket.write(msg + buffer.toString());
           });
        });
        netServer.listen(port, result->{
            if (result.succeeded()){
                System.out.println("start real server success");
            }else{
                System.out.println("start real server falied");
            }
        });
    }
}
