package g.proxy;

import g.proxy.filter.IFilter;
import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/8/30 上午1:28
 * @since 1.0.25
 */
public class FilterPipeline {

    private ProxyContext head;
    private ProxyContext tail;
    private IMessageHandler handler;

    public FilterPipeline(){
        head = new ProxyContext(new Head());
        tail = new ProxyContext(new Tail());
        connect(head, tail);
    }

    public FilterPipeline addToTail(IFilter filter) {
        ProxyContext context = new ProxyContext(filter);
        connect(tail.getBefore(), context);
        connect(context, tail);
        return this;
    }

    public FilterPipeline setHandler(IMessageHandler handler) {
        this.handler = handler;
        return this;
    }

    public void doRead(Buffer buffer) throws Exception {
        head.doNextFilter(buffer);
    }

    public void doWrite(Object object) throws Exception {
        head.doNextFilter(object);
    }

    class Head implements IFilter {

        @Override
        public void doFilter(Object msg, ProxyContext context) throws Exception {
            context.doNextFilter(msg);
        }
    }

    class Tail implements IFilter {

        @Override
        public void doFilter(Object msg, ProxyContext context) throws Exception {
            if (handler != null){
                handler.handle(msg);
            }
        }
    }

    private void connect(ProxyContext before, ProxyContext after) {
        before.setNext(after);
        after.setBefore(before);
    }
}
