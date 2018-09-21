package g.server.agent;

import g.proxy.FilterPipeline;
import g.proxy.ProxyContext;
import g.proxy.filter.EncodeFilter;
import g.proxy.filter.EncryptFilter;
import g.proxy.filter.IFilter;
import g.proxy.filter.PackageFilter;
import g.proxy.protocol.Message;
import g.proxy.socket.ISocketWrapper;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

/**
 * @author chengjin.lyf on 2018/9/14 上午11:03
 * @since 1.0.25
 */
public abstract class AbstractAgentClient implements AgentClient , IFilter{

    private FilterPipeline agentPipeline;

    private ISocketWrapper agentSocket;

    public AbstractAgentClient(ISocketWrapper agentSocket) {
        this.agentSocket = agentSocket;
        this.agentPipeline = new FilterPipeline()
                .addToTail(new EncodeFilter())
                .addToTail(new EncryptFilter())
                .addToTail(new PackageFilter())
                .addToTail(this);
    }

    @Override
    public abstract boolean isAuth();

    @Override
    public abstract boolean checkAccountAndPassword(String account, String password);

    @Override
    public void write(Message message) throws Exception{
        agentPipeline.doWrite(message);
    }

    @Override
    public void close() {
        agentSocket.close();
    }


    @Override
    public void doFilter(Object msg, ProxyContext context) throws Exception {
        agentSocket.write((Buffer) msg);
    }
}
