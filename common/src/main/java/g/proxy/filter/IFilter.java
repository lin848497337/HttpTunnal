package g.proxy.filter;

import g.proxy.ProxyContext;

/**
 * @author chengjin.lyf on 2018/8/30 上午1:06
 * @since 1.0.25
 */
public interface IFilter {
    void doFilter(Object msg, ProxyContext context) throws Exception;
}
