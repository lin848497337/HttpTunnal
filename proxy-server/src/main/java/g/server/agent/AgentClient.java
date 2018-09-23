package g.server.agent;

import g.proxy.FilterPipeline;
import g.proxy.ProxyContext;
import g.proxy.filter.EncodeFilter;
import g.proxy.filter.EncryptFilter;
import g.proxy.filter.IFilter;
import g.proxy.filter.PackageFilter;
import g.proxy.protocol.Message;
import g.proxy.socket.ISocketWrapper;
import g.server.account.AccountManager;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chengjin.lyf on 2018/9/23 上午10:47
 * @since 1.0.25
 */
public class AgentClient implements IFilter{

    private FilterPipeline agentPipeline;

    private ISocketWrapper agentSocket;

    private boolean auth;

    private Map<Long, BrowserClient> clientMap = new ConcurrentHashMap<>();

    private Vertx vertx;

    public AgentClient(ISocketWrapper agentSocket, Vertx vertx) {
        this.agentSocket = agentSocket;
        this.vertx =vertx;
        this.agentPipeline = new FilterPipeline()
                .addToTail(new EncodeFilter())
                .addToTail(new EncryptFilter())
                .addToTail(new PackageFilter())
                .addToTail(this);
    }


    public boolean isAuth(){
        return auth;
    }

    public BrowserClient getBrowserClient(long clientId){
        return clientMap.get(clientId);
    }

    public BrowserClient removeBrowserClient(long clientId){
        return clientMap.remove(clientId);
    }

    public void newClient(BrowserClient client){
        this.clientMap.put(client.getBrowserId(), client);
    }


    public boolean checkAccountAndPassword(String account, String password){
        this.auth = password != null &&
                    password.equalsIgnoreCase(AccountManager.getInstance().getPassword(account));
        return auth;
    }

    public void write(Message message) throws Exception{
        agentPipeline.doWrite(message);
    }

    public Vertx getVertx() {
        return vertx;
    }

    public void close() {
        agentSocket.close();
    }


    @Override
    public void doFilter(Object msg, ProxyContext context) throws Exception {
        agentSocket.write((Buffer) msg);
    }
}
