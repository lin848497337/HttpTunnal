package g.proxy.socket;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

/**
 * @author chengjin.lyf on 2018/9/20 上午9:50
 * @since 1.0.25
 */
public class NetSocketWrapper implements ISocketWrapper {

    private NetSocket targetSocket;

    public NetSocketWrapper(NetSocket targetSocket){
        this.targetSocket = targetSocket;
    }

    @Override
    public void write(Buffer buffer) {
        targetSocket.write(buffer);
    }

    @Override
    public void close() {
        targetSocket.close();
    }

    @Override
    public void handler(Handler<Buffer> bufferHandler) {
        targetSocket.handler(bufferHandler);
    }
}
