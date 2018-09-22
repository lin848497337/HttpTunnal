package g.server.message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import g.http.HttpMessageHeader;
import g.proxy.protocol.*;
import g.server.agent.AgentClient;
import g.server.agent.BrowserClient;
import g.util.CommonConsts;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chengjin.lyf on 2018/8/30 下午2:07
 * @since 1.0.25
 */
public class AgentMessageAction implements MessageAction {

    private static final Logger logger = LoggerFactory.getLogger(AgentMessageAction.class);

    private Map<Long, BrowserClient> clientMap = new ConcurrentHashMap<>();
    private Vertx vertx;

    public AgentMessageAction(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void process(Message msg, AgentClient agentClient) throws Exception{
        if (!agentClient.isAuth()){
            if (!(msg instanceof LoginMessage)){
                agentClient.close();
            }
            LoginMessage lm = (LoginMessage) msg;
            vertx.executeBlocking((Future<Boolean> future) -> {
                try{
                    future.complete(agentClient.checkAccountAndPassword(lm.getAccount(), lm.getPassword()));
                }catch (Exception e){
                    future.fail(e.getCause());
                }
            }, asyncResult -> {
                if(asyncResult.succeeded()){
                    LoginResponseMessage responseMessage = new LoginResponseMessage();
                    responseMessage.setCmd(CommonConsts.COMMAND_RESPONSE);

                    if (asyncResult.result() == true){
                        responseMessage.setMsg("success");
                        responseMessage.setResult((byte) 1);
                        responseMessage.setToken("aaaaa");
                        try {
                            agentClient.write(responseMessage);
                            logger.error("response login success success! "+lm.getAccount());
                        } catch (Exception e) {
                            logger.error("response login success failed!" + lm.getAccount());
                            try {
                                agentClient.close();
                            } catch (Exception e1) {
                            }
                        }
                    }else {
                        responseMessage.setMsg("account or password incorrect! "+lm.getAccount());
                        responseMessage.setResult((byte) 0);
                        responseMessage.setToken("");
                        try {
                            agentClient.write(responseMessage);
                            agentClient.close();
                        } catch (Exception e) {
                        }
                    }
                }else{
                    LoginResponseMessage responseMessage = new LoginResponseMessage();
                    responseMessage.setCmd(CommonConsts.COMMAND_RESPONSE);
                    responseMessage.setMsg("check account or password failed!");
                    responseMessage.setResult((byte) 0);
                    responseMessage.setToken("");
                    try {
                        agentClient.write(responseMessage);
                        agentClient.close();
                    } catch (Exception e) {
                    }
                }
            });
            return;
        }else if (msg instanceof BrokenBrowserMessage){
            // agent 端断开连接
            BrokenBrowserMessage brokenBrowserMessage = (BrokenBrowserMessage) msg;
            BrowserClient client = clientMap.remove(brokenBrowserMessage.getBrowserId());
            if (client != null){
                client.close();
            }
        }else if (msg instanceof BrowserMessage){
            // 客户端与real server 交换数据
            BrowserMessage browserMessage = (BrowserMessage) msg;
            BrowserClient client = clientMap.get(browserMessage.getBrowserId());
            if (client == null){
                client = new BrowserClient(vertx, agentClient, browserMessage.getBrowserId());
                HttpMessageHeader header = HttpMessageHeader.buildFromBuffer(Buffer.buffer(((BrowserMessage) msg).getData()));
                if (!header.isOk()){
                    throw new RuntimeException("not message header!");
                }
                client.connect(header);
                clientMap.put(browserMessage.getBrowserId(), client);
                return;
            }
            if (client.isClosed()){
                return;
            }
            client.write(browserMessage);
        }else{
            throw new UnsupportedOperationException();
        }

    }
}
