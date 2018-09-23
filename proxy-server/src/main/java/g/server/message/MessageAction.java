package g.server.message;

import g.proxy.protocol.Message;
import g.server.agent.AgentClient;

/**
 * @author chengjin.lyf on 2018/8/30 下午1:47
 * @since 1.0.25
 */
public interface MessageAction<T extends Message> {
    void process(T msg, AgentClient agentClient) throws Exception;
}
