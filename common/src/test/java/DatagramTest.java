import g.proxy.socket.DatagramSocketWrapper;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.net.SocketAddress;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chengjin.lyf on 2018/9/20 下午3:32
 * @since 1.0.25
 */
public class DatagramTest {

    private static void startServer(){
        Vertx vertx = Vertx.vertx();
        DatagramSocket socket = vertx.createDatagramSocket();
        Map<SocketAddress, DatagramSocketWrapper> wrapperMap = new HashMap<>();
        socket.handler(packet->{
            SocketAddress socketAddress = packet.sender();
            DatagramSocketWrapper wrapper = wrapperMap.get(socketAddress);
            if (wrapper == null){
                wrapper = new DatagramSocketWrapper(socketAddress.host(), socketAddress.port(), socket, vertx);
                wrapper.handler(buffer->{
                    System.out.println(buffer.toString());
                });
                wrapperMap.put(socketAddress, wrapper);
            }
            wrapper.receivePacket(packet);
        });

        socket.listen(6666, "0.0.0.0", result->{
            if (result.succeeded()){
                System.out.println("start server success!");
            }
        });


    }

    public static void main(String args[]){
        startServer();
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("send data to server");
        DatagramSocketWrapper wrapper = new DatagramSocketWrapper("127.0.0.1", 6666, Vertx.vertx().createDatagramSocket(), null);
        wrapper.write(Buffer.buffer("hello world!"));
    }
}
