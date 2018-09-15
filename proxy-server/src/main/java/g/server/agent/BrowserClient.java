package g.server.agent;

import g.http.HttpMessageHeader;
import g.proxy.protocol.BrokenBrowserMessage;
import g.proxy.protocol.BrowserMessage;
import g.proxy.protocol.Message;
import g.util.CommonConsts;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chengjin.lyf on 2018/8/30 上午1:03
 * @since 1.0.25
 */
public class BrowserClient {

    private AgentClient agentClient;

    private boolean connected = false;

    private boolean closed = false;

    private volatile NetSocket socket;

    private Vertx vertx;

    private Buffer buffer = Buffer.buffer();

    private long browserId;

    private static Logger logger = LoggerFactory.getLogger(BrowserClient.class);

    public BrowserClient(Vertx vertx, AgentClient agentClient, long browserId) {
        this.vertx = vertx;
        this.agentClient = agentClient;
        this.browserId = browserId;
    }


    public Message buildMsgMessage(byte[] data){
        BrowserMessage message = new BrowserMessage();
        message.setCmd(CommonConsts.COMMAND_MSG);
        message.setBrowserId(browserId);
        message.setData(data);
        return message;
    }

    public Message buildBrokeMessage(){
        BrokenBrowserMessage message = new BrokenBrowserMessage();
        message.setCmd(CommonConsts.COMMAND_QUIT);
        message.setBrowserId(browserId);
        return message;
    }

    public void connect(HttpMessageHeader header){
        NetClient client = vertx.createNetClient();
        client.connect(header.getSocketAddress(), result->{
            if (result.succeeded()){
                connected = true;
                NetSocket _socket = result.result();
                if (closed){
                    logger.error("just connect to remote ,but agent close this connection!");
                    _socket.close();
                    return;
                }
                _socket.handler(buffer->{
                    Message message = buildMsgMessage(buffer.getBytes());
                    try {
                        agentClient.write(message);
                    } catch (Exception e) {
                        logger.error("response to agent error: ", e);
                        try {
                            agentClient.close();
                        } catch (Exception e1) {
                        }
                    }
                });
                _socket.exceptionHandler(e->{
                   logger.error("connect error!",e);
                   socket.close();
                });
                _socket.closeHandler(Void->{
                    if (closed){
                        logger.error("close by agent : "+_socket.remoteAddress());
                        return;
                    }
                    logger.error("close by remote! agent : "+socket.remoteAddress() +" target : "+header.getUri());
                    Message msg = buildBrokeMessage();
                    try {
                        agentClient.write(msg);
                    } catch (Exception e) {
                        logger.error("response broker to agent error: "+browserId, e);
                    }
                    closed = true;
                });
                //与write共存可能有多线程问题
                socket = _socket;
                if (header.isConnect()){
                    Message message = buildMsgMessage(CommonConsts.CONNECT_RESPONSE.getBytes());
                    try {
                        agentClient.write(message);
                    } catch (Exception e) {
                        logger.error("response to agent error: "+browserId, e);
                        try {
                            agentClient.close();
                        } catch (Exception e1) {
                        }
                    }
                    return;
                }
                socket.write(header.getBuffer());
            }else{
                logger.info(browserId +  " connect to "+header.getUri()+" failed!");
                Message message = buildBrokeMessage();
                try {
                    agentClient.write(message);
                } catch (Exception e) {
                    logger.error("response broker to agent error: "+browserId, e);
                    try {
                        agentClient.close();
                    } catch (Exception e1) {
                    }
                }
                closed = true;
            }
        });
    }

    public boolean isConnected(){
        return connected;
    }

    public void close(){
        closed = true;
        if(socket!=null){
            socket.close();
        }
    }

    public boolean isClosed() {
        return closed;
    }

    public void write(BrowserMessage message){
        if (socket == null){
            buffer.appendBytes(message.getData());
        }else{
            socket.write(Buffer.buffer(message.getData()));
        }
    }

}
