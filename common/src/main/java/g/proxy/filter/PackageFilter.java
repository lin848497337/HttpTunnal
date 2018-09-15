package g.proxy.filter;

import g.proxy.ProxyContext;
import g.util.TypeUtil;
import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/8/30 上午12:59
 * @since 1.0.25
 */
public class PackageFilter implements IFilter {

    @Override
    public void doFilter(Object msg, ProxyContext context) throws Exception {
        byte[] data = TypeUtil.convertToByteArray(msg);
        Buffer buffer = Buffer.buffer();
        buffer.appendInt(data.length);
        buffer.appendBytes(data);
        context.doNextFilter(buffer);
    }
}
