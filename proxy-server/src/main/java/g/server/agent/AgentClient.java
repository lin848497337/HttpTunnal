package g.server.agent;

import g.proxy.protocol.Message;

/**
 * @author chengjin.lyf on 2018/8/30 上午1:02
 * @since 1.0.25
 */
public interface AgentClient {
    boolean isAuth();
    boolean checkAccountAndPassword(String account, String password);
    void write(Message message) throws Exception;
    void close() throws Exception;
}
