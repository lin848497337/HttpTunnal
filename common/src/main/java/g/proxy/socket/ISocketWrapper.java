package g.proxy.socket;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/9/20 上午9:49
 * @since 1.0.25
 */
public interface ISocketWrapper {
    void write(Buffer buffer) throws Exception;
    void close();
    void handler(Handler<Buffer> bufferHandler);
}
