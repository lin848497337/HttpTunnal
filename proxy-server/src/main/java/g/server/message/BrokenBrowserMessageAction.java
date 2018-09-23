package g.server.message;

import g.proxy.protocol.BrokenBrowserMessage;
import g.server.agent.AgentClient;
import g.server.agent.BrowserClient;

/**
 * @author chengjin.lyf on 2018/9/23 上午10:58
 * @since 1.0.25
 */
public class BrokenBrowserMessageAction implements MessageAction<BrokenBrowserMessage> {

    @Override
    public void process(BrokenBrowserMessage msg, AgentClient agentClient) throws Exception {
        BrowserClient client = agentClient.removeBrowserClient(msg.getBrowserId());
        if (client != null){
            client.close();
        }
    }
}
