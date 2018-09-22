package g;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import g.exception.CloseSocketException;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;

/**
 * @author chengjin.lyf on 2018/9/21 下午9:24
 * @since 1.0.25
 */
public class SenderQueue implements Handler<Long> {

    private static final Logger logger = LoggerFactory.getLogger(SenderQueue.class);

    private Map<Long, Map<Short, DataPacket>> cache = new HashMap<>();

    private long timerId = -1;

    private Sequence sequence = new Sequence(0);

    private int maxSize = 500;

    private int ttl = 5;

    private DatagramSocket datagramSocket;

    private String targetIp;

    private int targetPort;

    private ExceptionHandle exceptionHandle;

    public SenderQueue(DatagramSocket datagramSocket, String targetIp, int targetPort) {
        this.datagramSocket = datagramSocket;
        this.targetIp = targetIp;
        this.targetPort = targetPort;
        Vertx vertx = VertxContext.getVertx();
        HAGlobalOption option = VertxContext.getOption();
        if (option!=null && option.getHaMode() == HAMode.PERIODIC){
            timerId = vertx.setPeriodic(option.getRto(), this);
        }
        if (option!=null){
            maxSize = option.getMaxPacketDataSize();
            ttl = option.getTtl();
        }
    }

    public void exceptionHandle(ExceptionHandle handle){
        this.exceptionHandle = handle;
    }


    public void ack(DataPacket packet) throws Exception {
        Map<Short, DataPacket> map = cache.get(packet.getSequence());
        if (map != null) {
            map.remove(packet.getSubSequence());
        }
    }

    public void send(Buffer buffer) throws Exception {
        split(buffer);
    }

    private void cacheData(DataPacket packet) {
        Map<Short, DataPacket> map = cache.get(packet.getSequence());
        if (map == null) {
            map = new HashMap<>();
            cache.put(packet.getSequence(), map);
        }
        map.put(packet.getSubSequence(), packet);
    }

    public void sendAckForPacket(DataPacket packet){
        DataPacket ackPacket = new DataPacket(DataPacket.DATA_TYPE_ACK);
        ackPacket.setSequence(packet.getSequence());
        ackPacket.setSubSequence(packet.getSubSequence());
        doSend0(ackPacket);
    }

    public void doSend(long seq, short subSeq){
        Map<Short, DataPacket> map = cache.get(seq);
        if (map == null){
            logger.warn("not have data!"+seq);
            return;
        }
        if (subSeq != -1){
            DataPacket packet = map.get(subSeq);
            if (packet == null){
                logger.warn(" not have packet !", subSeq);
                return;
            }
            doSend0(packet);
            return;
        }
        for (DataPacket packet : map.values()){
            doSend0(packet);
        }
    }

    private void doSend0(DataPacket packet){
        Buffer buffer = packet.packet();
        packet.mark();
        datagramSocket.send(buffer, targetPort, targetIp, result->{
            if (!result.succeeded()){
                logger.warn(" send data failed! ", result.cause());
            }
        });
    }

    private void split(Buffer buffer) throws Exception {
        int packetSize = (int) Math.ceil(buffer.length() / (float) maxSize);
        int totalSize = buffer.length();
        int pos = 0;
        long seq = sequence.next();
        short subSeq = 1;
        while (pos < totalSize) {
            int appendSize = Math.min(maxSize, totalSize - pos);
            DataPacket packet = new DataPacket(DataPacket.DATA_TYPE_DATA);
            Buffer packetBuffer = buffer.getBuffer(pos, pos + appendSize);
            packet.setData(packetBuffer);
            packet.setPacketSize((short) packetSize);
            packet.setSequence(seq);
            packet.setSubSequence(subSeq);
            cacheData(packet);
            pos = pos + appendSize;
            subSeq++;
        }
        doSend(seq, (short) -1);
    }

    public void release(){
        cache.clear();
        if (timerId!= -1){
            VertxContext.getVertx().cancelTimer(timerId);
        }
        cache = null;
    }

    @Override
    public void handle(Long eventId) {
        // more than rto
        Iterator<Map.Entry<Long, Map<Short, DataPacket>>> sequenceIt = cache.entrySet().iterator();
        while (sequenceIt.hasNext()){
            Map.Entry<Long, Map<Short, DataPacket>> sequenceEntry = sequenceIt.next();
            Map<Short, DataPacket> subMap = sequenceEntry.getValue();
            if (subMap.isEmpty()){
                sequenceIt.remove();
                continue;
            }
            Iterator<Map.Entry<Short, DataPacket>> subIt = subMap.entrySet().iterator();
            while (subIt.hasNext()){
                Map.Entry<Short, DataPacket> subEntry = subIt.next();
                DataPacket packet = subEntry.getValue();
                if (packet.getSendCounter() >= ttl){
                    logger.warn("retry to send data ttl "+ packet.getSendCounter() + " will close socket");
                    if (exceptionHandle!=null){
                        exceptionHandle.handle(new CloseSocketException());
                    }
                    return;
                }
                logger.warn("retry to send data : " + packet);
                SenderQueue.this.doSend(packet.getSequence(), packet.getSubSequence());
            }
        }
    }
}
