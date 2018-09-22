package g;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chengjin.lyf on 2018/9/21 下午9:25
 * @since 1.0.25
 */
public class ReceiveQueue{

    private Map<Long, Map<Short, DataPacket>> cache = new HashMap<>();

    private long avaliableSequence = 0;

    private Handler<Buffer> handler;

    private static final Logger logger = LoggerFactory.getLogger(ReceiveQueue.class);

    public void onReceivePacket(DataPacket packet) throws Exception {
        if (packet.isACK()) {
            throw new RuntimeException("can not save ack msg!");
        }
        long sequence = packet.getSequence();
        if (sequence <= avaliableSequence){
            logger.warn("duplicate receive package sequence : "+ sequence);
            return;
        }

        Map<Short, DataPacket> map = cache.get(sequence);
        if (map == null) {
            map = new HashMap<>();
            cache.put(sequence, map);
        }
        map.put(packet.getSubSequence(), packet);
        boolean isFull = packet.getPacketSize() == map.size();
        if (isFull){
            Buffer allData = mergeBuffer(sequence);
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
        return buffer;
    }

    public void release(){
        cache.clear();
        cache = null;
    }

}
