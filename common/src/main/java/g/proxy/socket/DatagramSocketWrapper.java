package g.proxy.socket;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import io.vertx.core.Vertx;
import io.vertx.core.datagram.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;

/**
 * @author chengjin.lyf on 2018/9/20 上午9:51
 * @since 1.0.25
 */
public class DatagramSocketWrapper implements ISocketWrapper {

    private String host;

    private int port;

    private DatagramSocket socket;

    private Handler<Buffer> bufferHandler;

    private static int MAX_SIZE = 500 - 12;

    private static long TIMEOUT = TimeUnit.SECONDS.toMillis(10);

    private AtomicLong sequence = new AtomicLong(0);

    private static byte DATA = 1;
    private static byte ACK = 2;

    /**
     * sequence, subSeq, data
     * 需要检测长时间收不到消息
     */
    private Map<Long, Map<Short,Buffer>> receBufferList = new ConcurrentHashMap<>();

    private Map<Long, Map<Short,Buffer>> sendBufferList = new ConcurrentHashMap<>();
    private Map<Long, Map<Short,Long>> sendTimestampList = new ConcurrentHashMap<>();

    private Vertx vertx;

    private static Logger logger = LoggerFactory.getLogger(DatagramSocketWrapper.class);


    private long timerId;

    public DatagramSocketWrapper(String host, int port, DatagramSocket socket, Vertx vertx) {
        this.host = host;
        this.port = port;
        this.socket = socket;
        this.vertx = vertx;
        timerId = vertx.setPeriodic(TIMEOUT, ts->{
            long now = System.currentTimeMillis();
            Iterator<Map.Entry<Long, Map<Short,Long>>> entryIterator = sendTimestampList.entrySet().iterator();
            while (entryIterator.hasNext()){
                Map.Entry<Long, Map<Short,Long>> entry = entryIterator.next();
                long seq = entry.getKey();
                Map<Short,Long> subMap = entry.getValue();
                Iterator<Map.Entry<Short, Long>> subIterator = subMap.entrySet().iterator();
                while (subIterator.hasNext()){
                    Map.Entry<Short, Long> shortLongEntry = subIterator.next();
                    short subSeq = shortLongEntry.getKey();
                    long timestamp = shortLongEntry.getValue();
                    long diff = now - timestamp;
                    if (diff >= TIMEOUT * 2){
                        logger.error("socket has not response ");
                        subIterator.remove();
                    }else if (diff > TIMEOUT){
                        doWrite(sendBufferList.get(seq).get(subSeq));
                        logger.warn("retry send");
                    }
                }
                if (subMap.isEmpty()){
                    entryIterator.remove();
                }
            }
        });
    }

    private List<Buffer> split(Buffer buffer, boolean cache, boolean isACK){
        int packetSize = (int) Math.ceil(buffer.length() / (float)MAX_SIZE);
        List<Buffer> bufferList = new ArrayList<>(packetSize);
        int totalSize = buffer.length();
        int pos = 0;
        long seq = sequence.addAndGet(1);
        byte cmd = DATA;
        if (isACK){
            cmd = ACK;
        }
        if (totalSize <= MAX_SIZE){
            Buffer data = Buffer.buffer();
            data.
                appendLong(seq).
                appendShort((short) 1).
                appendShort((short) 1).
                appendShort((short) totalSize).
                appendByte(cmd).
                appendBuffer(buffer);
            bufferList.add(data);
            cache(seq, (short) 1, data, cache);
            return bufferList;
        }
        short subSeq = 1;
        while (pos < totalSize){
            int appendSize = Math.min(MAX_SIZE, totalSize - pos);
            Buffer data = Buffer.buffer();
            data.appendLong(seq).
                    appendShort(subSeq).
                    appendShort((short) packetSize).
                    appendShort((short) appendSize).
                    appendByte(cmd);
            data.appendBuffer(buffer.getBuffer(pos, pos+appendSize));
            bufferList.add(data);
            cache(seq, subSeq, data, cache);
            pos = pos + appendSize;
            subSeq++;
        }
        return bufferList;
    }

    private void cache(long seq, short subSeq, Buffer buffer, boolean cache){
        if (!cache){
            return;
        }
        Map<Short,Buffer> bufferMap = sendBufferList.get(seq);
        Map<Short,Long> tmpMap = sendTimestampList.get(seq);
        if (bufferMap == null){
            bufferMap = new ConcurrentHashMap<>();
            tmpMap = new ConcurrentHashMap<>();
            sendBufferList.put(seq, bufferMap);
            sendTimestampList.put(seq, tmpMap);
        }
        bufferMap.put(subSeq, buffer);
        tmpMap.put(subSeq, System.currentTimeMillis());
    }


    @Override
    public void write(Buffer buffer) {
        List<Buffer> needSendBufs = split(buffer, true, false);
        doWrite(needSendBufs);
    }

    private void doWrite(List<Buffer> sendBufList){
        for (Buffer sendBuf: sendBufList){
            doWrite(sendBuf);
        }

    }

    private void doWrite(Buffer sendBuf){
        socket.send(sendBuf, port, host, result->{
            if(!result.succeeded()){
                logger.error("send failed! ",result.cause());
            }
        });
    }


    @Override
    public void close() {
        socket.close();
        vertx.cancelTimer(timerId);
    }

    private void responseAck(long seq, short sub){
        List<Buffer> sendBufList = split(Buffer.buffer().appendLong(seq).appendShort(sub), false, true);
        doWrite(sendBufList);
    }

    private void ack(long seq, short sub){
        Map<Short,Long> tmpMap = sendTimestampList.get(seq);
        if (tmpMap == null){
            logger.warn(" duplicate receive ack !");
            return;
        }
        tmpMap.remove(sub);
        Map<Short,Buffer> bufferMap = sendBufferList.get(seq);
        bufferMap.remove(sub);
        if (bufferMap.isEmpty()){
            sendTimestampList.remove(seq);
            sendBufferList.remove(seq);
        }
    }

    public void receivePacket(DatagramPacket packet){
        Buffer data = packet.data();
        long seq = data.getLong(0);
        short subSeq = data.getShort(8);
        short packetSize = data.getShort(10);
        short dataSize = data.getShort(12);
        byte command = data.getByte(14);
        short realSize = (short) (data.length() - 15);
        if (dataSize != realSize){
            logger.warn("drop data , size not match : require "+dataSize + " , real size : "+ realSize);
            return;
        }
        if (command == ACK){
            ack(data.getLong(15), data.getShort(23));
            return;
        }
        if (command != DATA){
            throw new RuntimeException("error data");
        }
        responseAck(seq, subSeq);
        if (packetSize == 1){
            bufferHandler.handle(data.getBuffer(15, data.length()));
            return;
        }
        Map<Short, Buffer> msgMap = receBufferList.get(seq);
        if (msgMap == null){
            msgMap = new ConcurrentHashMap<>();
            msgMap.put(subSeq, data);
            receBufferList.put(seq, msgMap);
            return;
        }
        if(msgMap.put(subSeq, data)!=null){
            logger.warn("duplicate receive packet msg!");
        }
        if (msgMap.size()== packetSize){
            Buffer finalBuffer = Buffer.buffer();
            for (short i=1 ; i<=packetSize ; i++){
                Buffer tmp = msgMap.get(i);
                if (tmp == null){
                    logger.error("not all packet receive!");
                }
                finalBuffer.appendBuffer(tmp.getBuffer(15, tmp.length()));
            }
            bufferHandler.handle(finalBuffer);
            receBufferList.remove(seq);
            msgMap.clear();
            return;
        }
    }


    @Override
    public void handler(Handler<Buffer> bufferHandler) {
        this.bufferHandler = bufferHandler;
    }
}
