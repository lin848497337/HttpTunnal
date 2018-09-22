package g.proxy.socket;

import g.HASocket;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/9/22 下午1:21
 * @since 1.0.25
 */
public class HASocketWrapper implements ISocketWrapper {

    private HASocket socket;

    public HASocketWrapper(HASocket socket) {
        this.socket = socket;
    }

    @Override
    public void write(Buffer buffer) throws Exception {
        socket.write(buffer);
    }

    @Override
    public void close() {
        socket.close();
    }

    @Override
    public void handler(Handler<Buffer> bufferHandler) {
        socket.handler(bufferHandler);
    }
}
