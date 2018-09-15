package g.server.verticle.handler;

import g.proxy.FilterPipeline;
import g.proxy.filter.*;
import g.proxy.protocol.LoginResponseMessage;
import g.proxy.protocol.Message;
import g.server.agent.AbstractAgentClient;
import g.server.agent.AgentClient;
import g.server.agent.BrowserClient;
import g.server.message.AgentMessageAction;
import g.server.message.MessageAction;
import g.util.CommonConsts;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chengjin.lyf on 2018/9/2 上午10:38
 * @since 1.0.25
 */
public class ProxyServerHandler implements Handler<NetSocket> {

    private static final Logger logger = LoggerFactory.getLogger(ProxyServerHandler.class);

    private Vertx vertx;

    private Class<? extends AgentClient> agentClass;

    public ProxyServerHandler(Vertx vertx, Class<? extends AgentClient> agentClass){
        this.vertx = vertx;
        this.agentClass = agentClass;
    }

    @Override
    public void handle(NetSocket agentSocket) {
        try {
            // 写回agent 管线

            // 代表agent
            AgentClient agentClient;

            if (agentClass == null){
                agentClient = new AbstractAgentClient(agentSocket) {

                    private boolean auth = false;

                    @Override
                    public boolean isAuth() {
                        return auth;
                    }

                    @Override
                    public boolean checkAccountAndPassword(String account, String password) {
                        return "admin".equalsIgnoreCase(account) && "admin_test".equalsIgnoreCase(password);
                    }
                };

            }else {
                Constructor<? extends AgentClient> constructor = agentClass.getConstructor(NetSocket.class);
                agentClient = constructor.newInstance(agentSocket);
            }

            // connect real server and exchange message
            MessageAction action = new AgentMessageAction(vertx);

            // agent 写入管线
            FilterPipeline inputPipeline = new FilterPipeline().addToTail(new UnpackFilter())
                .addToTail(new DecryptFilter())
                .addToTail(new DecodeFilter())
                .setHandler(msg -> action.process((Message) msg, agentClient));

            agentSocket.handler(buffer -> {
                try {
                    inputPipeline.doRead(buffer);
                } catch (Exception e) {
                    logger.error("do read failed", e);
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
