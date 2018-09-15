package g.proxy.filter;

import g.proxy.ProxyContext;
import g.util.AES;
import g.util.TypeUtil;

/**
 * @author chengjin.lyf on 2018/8/30 上午12:59
 * @since 1.0.25
 */
public class EncryptFilter implements IFilter {

    @Override
    public void doFilter(Object msg, ProxyContext context) throws Exception {
        byte []data = TypeUtil.convertToByteArray(msg);
        byte[] encrypt = AES.encrypt(data);
        context.doNextFilter(encrypt);
    }
}
