package g.server.message;

import g.proxy.protocol.LoginMessage;
import g.proxy.protocol.LoginResponseMessage;
import g.server.agent.AgentClient;
import g.util.CommonConsts;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chengjin.lyf on 2018/9/23 上午10:55
 * @since 1.0.25
 */
public class LoginMessageAction implements MessageAction<LoginMessage> {

    private static final Logger logger = LoggerFactory.getLogger(LoginMessageAction.class);

    @Override
    public void process(LoginMessage msg, AgentClient agentClient) throws Exception {
        Vertx vertx = agentClient.getVertx();
        vertx.executeBlocking((Future<Boolean> future) -> {
            try{
                future.complete(agentClient.checkAccountAndPassword(msg.getAccount(), msg.getPassword()));
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
                        logger.error("response login success success! "+msg.getAccount());
                    } catch (Exception e) {
                        logger.error("response login success failed!" + msg.getAccount());
                        try {
                            agentClient.close();
                        } catch (Exception e1) {
                        }
                    }
                }else {
                    responseMessage.setMsg("account or password incorrect! "+msg.getAccount());
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
    }
}
