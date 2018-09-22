package g.proxy.socket;

import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/9/21 下午9:14
 * @since 1.0.25
 */
public class DatagramPackage {
    private long seq;
    private short subSeq;
    private short packetSize;
    private short dataSize;
    private byte command;
    private Buffer buffer;
}
