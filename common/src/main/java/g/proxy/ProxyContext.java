package g.proxy;

import g.proxy.filter.IFilter;

/**
 * @author chengjin.lyf on 2018/8/30 上午1:06
 * @since 1.0.25
 */
public class ProxyContext {

    private IFilter ifilter;

    private ProxyContext next;
    private ProxyContext before;

    public ProxyContext(IFilter ifIlter) {
        this.ifilter = ifIlter;
    }

    public void setNext(ProxyContext next) {
        this.next = next;
    }

    public void setBefore(ProxyContext before) {
        this.before = before;
    }

    public ProxyContext getNext() {
        return next;
    }

    public ProxyContext getBefore() {
        return before;
    }

    public void doNextFilter(Object msg) throws Exception {
        if (ifilter != null){
            ifilter.doFilter(msg, this.next);
        }
    }
}
