package g.server.verticle.handler;

import g.proxy.FilterPipeline;
import g.proxy.filter.DecodeFilter;
import g.proxy.filter.DecryptFilter;
import g.proxy.filter.UnpackFilter;
import g.proxy.protocol.Message;
import g.proxy.socket.DatagramSocketWrapper;
import g.proxy.socket.ISocketWrapper;
import g.server.agent.AbstractAgentClient;
import g.server.message.AgentMessageAction;
import g.server.message.MessageAction;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.net.SocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import g.server.agent.AgentClient;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.datagram.DatagramPacket;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chengjin.lyf on 2018/9/20 上午7:34
 * @since 1.0.25
 */
public class UDPProxyServerHandler implements Handler<DatagramPacket> {

    private static final Logger logger = LoggerFactory.getLogger(UDPProxyServerHandler.class);

    private Vertx vertx;

    private Class<? extends AgentClient> agentClass;

    private Map<SocketAddress, DatagramSocketWrapper> wrapperMap = new HashMap<>();

    public UDPProxyServerHandler(Vertx vertx, Class<? extends AgentClient> agentClass){
        this.vertx = vertx;
        this.agentClass = agentClass;
    }

    @Override
    public void handle(DatagramPacket packet) {
        try {
            // 写回agent 管线


            SocketAddress socketAddress = packet.sender();
            DatagramSocketWrapper wrapper = wrapperMap.get(socketAddress);
            if (wrapper == null){
                DatagramSocket agentSocket = vertx.createDatagramSocket();
                wrapper = new DatagramSocketWrapper(socketAddress.host(), socketAddress.port(), agentSocket, vertx);
                wrapperMap.put(socketAddress, wrapper);
            }else{
                wrapper.receivePacket(packet);
                return;
            }

            // 代表agent
            AgentClient agentClient;

            if (agentClass == null){
                agentClient = new AbstractAgentClient(wrapper) {

                    private boolean auth = false;

                    @Override
                    public boolean isAuth() {
                        return auth;
                    }

                    @Override
                    public boolean checkAccountAndPassword(String account, String password) {
                        this.auth = "admin".equalsIgnoreCase(account) && "admin_test".equalsIgnoreCase(password);
                        return auth;
                    }
                };

            }else {
                Constructor<? extends AgentClient> constructor = agentClass.getConstructor(ISocketWrapper.class);
                agentClient = constructor.newInstance(wrapper);
            }

            // connect real server and exchange message
            MessageAction action = new AgentMessageAction(vertx);

            // agent 写入管线
            FilterPipeline inputPipeline = new FilterPipeline().addToTail(new UnpackFilter())
                    .addToTail(new DecryptFilter())
                    .addToTail(new DecodeFilter())
                    .setHandler(msg -> action.process((Message) msg, agentClient));

            wrapper.handler(buffer -> {
                try {
                    inputPipeline.doRead(buffer);
                } catch (Exception e) {
                    logger.error("do read failed", e);
                }
            });
            wrapper.receivePacket(packet);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
