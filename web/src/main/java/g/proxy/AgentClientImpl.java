package g.proxy;

import g.model.UserDO;
import g.proxy.socket.ISocketWrapper;
import g.server.agent.AbstractAgentClient;
import g.service.UserService;
import g.util.SpringApplicationHolder;
import io.vertx.core.net.NetSocket;

/**
 * @author chengjin.lyf on 2018/9/14 下午2:24
 * @since 1.0.25
 */
public class AgentClientImpl extends AbstractAgentClient{

    private boolean isAuth = false;

    public AgentClientImpl(ISocketWrapper agentSocket) {
        super(agentSocket);
    }

    @Override
    public boolean isAuth() {
        return isAuth;
    }

    @Override
    public boolean checkAccountAndPassword(String account, String password) {
        UserService userService = SpringApplicationHolder.getBean(UserService.class);
        UserDO userDO = userService.getUserByAccount(account);
        if (userDO == null) {
            return false;
        }
        isAuth = userDO.getPassword().equalsIgnoreCase(password);
        return isAuth;
    }
}
