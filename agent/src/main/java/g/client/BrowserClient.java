package g.client;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.shareddata.Shareable;

/**
 * @author chengjin.lyf on 2018/8/31 上午8:24
 * @since 1.0.25
 */
public interface BrowserClient extends Shareable ,Handler<Buffer>{
    void write(Buffer buffer);
    void close();
}
