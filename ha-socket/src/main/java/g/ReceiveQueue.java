package g;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chengjin.lyf on 2018/9/21 下午9:25
 * @since 1.0.25
 */
public class ReceiveQueue{

    private Map<Long, Map<Short, DataPacket>> cache = new ConcurrentHashMap<>();

    private Set<Long> duplicateCheckSet = new HashSet<>();

    private Handler<Buffer> handler;

    private static final Logger logger = LoggerFactory.getLogger(ReceiveQueue.class);

    public void onReceivePacket(DataPacket packet) throws Exception {
        if (packet.isACK()) {
            throw new RuntimeException("can not save ack msg!");
        }
        long sequence = packet.getSequence();
        if (duplicateCheckSet.contains(sequence)){
            logger.warn("duplicate receive package sequence : "+ sequence);
            return;
        }

        Map<Short, DataPacket> map = cache.get(sequence);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            cache.put(sequence, map);
        }
        map.put(packet.getSubSequence(), packet);
        boolean isFull = packet.getPacketSize() == map.size();
        if (isFull){
            Buffer allData = mergeBuffer(sequence);
            duplicateCheckSet.add(sequence);
            if (this.handler != null){
                this.handler.handle(allData);
            }
        }
    }

    public void handler(Handler<Buffer> handler){
        this.handler = handler;
    }

    public Buffer mergeBuffer(long sequence) {
        Map<Short, DataPacket> map = cache.remove(sequence);
        int size = map.size();
        Buffer buffer = Buffer.buffer();
        for (short i = 1; i <= size; i++) {
            DataPacket packet = map.get(i);
            buffer.appendBuffer(packet.getData());
        }
        logger.error(" merge data size : "+size + " packet size : "+ map.get((short)1).getPacketSize());
        return buffer;
    }

    public void release(){
        cache.clear();
        cache = null;
    }

}
