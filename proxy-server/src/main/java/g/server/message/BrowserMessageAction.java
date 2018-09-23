package g.server.message;

import g.http.HttpMessageHeader;
import g.proxy.protocol.BrowserMessage;
import g.server.agent.AgentClient;
import g.server.agent.BrowserClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/9/23 上午11:00
 * @since 1.0.25
 */
public class BrowserMessageAction implements MessageAction<BrowserMessage> {

    @Override
    public void process(BrowserMessage msg, AgentClient agentClient) throws Exception {
        // 客户端与real server 交换数据
        Vertx vertx = agentClient.getVertx();
        BrowserClient client = agentClient.getBrowserClient(msg.getBrowserId());
        if (client == null){
            client = new BrowserClient(vertx, agentClient, msg.getBrowserId());
            HttpMessageHeader header = HttpMessageHeader.buildFromBuffer(Buffer.buffer(msg.getData()));
            if (!header.isOk()){
                throw new RuntimeException("not message header!");
            }
            client.connect(header);
            agentClient.newClient(client);
            return;
        }
        if (client.isClosed()){
            return;
        }
        client.write(msg);
    }
}
