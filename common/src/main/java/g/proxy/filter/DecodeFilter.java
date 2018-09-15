package g.proxy.filter;

import g.proxy.ProxyContext;
import g.proxy.protocol.Message;
import g.proxy.protocol.MessageFactory;
import g.util.TypeUtil;
import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/8/30 上午1:26
 * @since 1.0.25
 */
public class DecodeFilter implements IFilter {

    @Override
    public void doFilter(Object msg, ProxyContext context) throws Exception {
        Buffer buffer = TypeUtil.convertToBuffer(msg);
        Message message = MessageFactory.getMessage(buffer);
        context.doNextFilter(message);
    }
}
