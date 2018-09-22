package g;

import g.exception.CloseSocketException;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.net.SocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chengjin.lyf on 2018/9/21 下午9:24
 * @since 1.0.25
 */
public class HASocket {

    private DatagramSocket socket;

    private SenderQueue sendBuffer;

    private ReceiveQueue receiveBuffer;

    private Map<SocketAddress, HASocket> socketMap = new HashMap<>();

    private boolean close = false;

    private long timerId = -1;

    private static final Logger logger = LoggerFactory.getLogger(HASocket.class);

    public HASocket(DatagramSocket socket) {
        this(socket, null, -1);
    }

    public HASocket(DatagramSocket socket, String targetIp, int port) {
        this.socket = socket;
        if (targetIp == null || targetIp.length() == 0 || port <= 0){
            return;
        }
        this.sendBuffer = new SenderQueue(socket, targetIp, port);
        this.sendBuffer.exceptionHandle(e->{
            if (e instanceof CloseSocketException){
                close();
            }
        });
        this.receiveBuffer = new ReceiveQueue();
    }

    public void handler(Handler<Buffer> handler){
        receiveBuffer.handler(handler);
        socket.handler(packet->{
            try {
                receiveData(packet.data());
            } catch (Exception e) {
                logger.error("receive data error!" , e);
            }
        });

    }

    private void receiveData(Buffer buffer) throws Exception {
        DataPacket packet = new DataPacket(DataPacket.DATA_TYPE_DATA);
        packet.unpacket(buffer);
        if (packet.isACK()){
            this.sendBuffer.ack(packet);
        }else{
            // first response ack
            this.sendBuffer.sendAckForPacket(packet);
            this.receiveBuffer.onReceivePacket(packet);
        }
    }

    public void write(Buffer buffer) throws Exception {
        sendBuffer.send(buffer);
    }

    public boolean isClose() {
        return close;
    }

    public void close(){
        close = true;
        if (timerId != -1){
            VertxContext.getVertx().cancelTimer(timerId);
        }
        socket.close();
        sendBuffer.release();
        receiveBuffer.release();
    }


    public void acceptHandler(Handler<HASocket> haSocketHandler){
        socket.handler(packet->{
            SocketAddress socketAddress = packet.sender();
            HASocket haSocket = socketMap.get(socketAddress);
            if (haSocket == null){
                haSocket = new HASocket(socket, socketAddress.host(), socketAddress.port());
                socketMap.put(socketAddress, haSocket);
                haSocketHandler.handle(haSocket);
            }
            try {
                haSocket.receiveData(packet.data());
            } catch (Exception e) {
                logger.error("receive data occur error!",e);
            }
        });
    }

    public void listen(int port){

        socket.listen(port, "0.0.0.0", result->{
            if (result.succeeded()){
                logger.info("server start success @ "+port);
                VertxContext.getVertx().setPeriodic(TimeUnit.SECONDS.toMillis(5), id->{
                    Iterator<Map.Entry<SocketAddress, HASocket>> socketIterator = socketMap.entrySet().iterator();
                    while (socketIterator.hasNext()){
                        Map.Entry<SocketAddress, HASocket> socketEntry = socketIterator.next();
                        if (socketEntry.getValue().isClose()){
                            socketIterator.remove();
                        }
                    }
                });
            }else{
                logger.error("server start failed @ "+port);
            }
        });
    }


}
