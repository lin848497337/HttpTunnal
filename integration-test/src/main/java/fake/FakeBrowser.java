package fake;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chengjin.lyf on 2018/9/13 下午10:33
 * @since 1.0.25
 */
public class FakeBrowser {
    private String msg;
    private int agentPort;
    private Vertx vertx;
    private NetSocket agentSocket;
    private static AtomicInteger numeber = new AtomicInteger(0);
    private int curNum;

    public FakeBrowser(String msg, int agentPort, Vertx vertx) {
        this.msg = msg;
        this.agentPort = agentPort;
        this.vertx = vertx;
        this.curNum = numeber.addAndGet(1);
    }

    public void connect(){
        NetClient netClient = vertx.createNetClient();
        netClient.connect(agentPort, "127.0.0.1", connectResult->{
            if (connectResult.succeeded()){
                agentSocket = connectResult.result();
                agentSocket.handler(buffer->{
                    System.out.println("receive eq send : "+(buffer.toString().equalsIgnoreCase("i am real server" + msg + curNum)) + " "+curNum);
                });
                System.out.println("connect success "+curNum);
                agentSocket.write(msg + curNum);
            }else{
                System.out.println("connect failed "+curNum);
            }
        });
    }

}
