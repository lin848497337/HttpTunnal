package g.proxy.filter;

import g.proxy.ProxyContext;
import g.util.TypeUtil;
import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/8/30 上午12:59
 * @since 1.0.25
 */
public class UnpackFilter implements IFilter {

    private Buffer cache = Buffer.buffer();
    private static final int INT_SIZE = 4;

    @Override
    public void doFilter(Object msg, ProxyContext context) throws Exception {
        Buffer buffer = TypeUtil.convertToBuffer(msg);
        cache.appendBuffer(buffer);
        if (cache.length() < INT_SIZE){
            return;
        }
        int length = cache.getInt(0);
        while (length <= cache.length() - INT_SIZE){
            Buffer output = cache.getBuffer(INT_SIZE, length+INT_SIZE);
            cache = cache.getBuffer(length + INT_SIZE, cache.length());
            context.doNextFilter(output);
            if (cache.length() < INT_SIZE){
                return;
            }
            length = cache.getInt(0);
        }

    }
}
