package g.proxy.filter;

import g.proxy.ProxyContext;
import g.proxy.protocol.Message;
import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/8/30 上午1:24
 * @since 1.0.25
 */
public class EncodeFilter implements IFilter {

    @Override
    public void doFilter(Object msg, ProxyContext context) throws Exception {
        Buffer buffer = Buffer.buffer();
        Message message = (Message) msg;
        message.encode();
        buffer.appendByte(message.getCmd());
        buffer.appendBytes(message.getCodec());
        context.doNextFilter(buffer);
    }
}
