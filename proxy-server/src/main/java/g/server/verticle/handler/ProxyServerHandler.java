package g.server.verticle.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import g.proxy.FilterPipeline;
import g.proxy.filter.DecodeFilter;
import g.proxy.filter.DecryptFilter;
import g.proxy.filter.UnpackFilter;
import g.proxy.protocol.Message;
import g.proxy.socket.NetSocketWrapper;
import g.server.agent.AgentClient;
import g.server.message.MessageAction;
import g.server.message.MessageActionFactory;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;

/**
 * @author chengjin.lyf on 2018/9/2 上午10:38
 * @since 1.0.25
 */
public class ProxyServerHandler implements Handler<NetSocket> {

    private static final Logger logger = LoggerFactory.getLogger(ProxyServerHandler.class);

    private Vertx vertx;


    public ProxyServerHandler(Vertx vertx){
        this.vertx = vertx;
    }

    @Override
    public void handle(NetSocket agentSocket) {
        try {
            // 写回agent 管线

            NetSocketWrapper wrapper = new NetSocketWrapper(agentSocket);
            // 代表agent
            AgentClient agentClient = new AgentClient(wrapper, vertx);



            // agent 写入管线
            FilterPipeline inputPipeline = new FilterPipeline().addToTail(new UnpackFilter())
                .addToTail(new DecryptFilter())
                .addToTail(new DecodeFilter())
                .setHandler(msg -> {
                    MessageAction action = MessageActionFactory.getInstance().getAction(msg.getClass());
                    action.process((Message) msg, agentClient);
                });

            wrapper.handler(buffer -> {
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
