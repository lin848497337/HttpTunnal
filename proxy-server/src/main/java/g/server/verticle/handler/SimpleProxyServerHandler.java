package g.server.verticle.handler;

import g.http.HttpMessageHeader;
import g.util.CommonConsts;
import io.vertx.core.net.NetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import g.proxy.FilterPipeline;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

/**
 * @author chengjin.lyf on 2018/9/2 上午10:38
 * @since 1.0.25
 */
public class SimpleProxyServerHandler implements Handler<NetSocket> {

    private static final Logger logger = LoggerFactory.getLogger(SimpleProxyServerHandler.class);

    private Vertx vertx ;

    public SimpleProxyServerHandler(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void handle(NetSocket agentSocket) {
        try {
            FilterPipeline agentPipeline = new FilterPipeline()
                    .addToTail((msg, context) -> agentSocket.write((Buffer) msg));


            FilterPipeline inputPipeline = new FilterPipeline()
                    .setHandler(msg -> {
                        HttpMessageHeader header = HttpMessageHeader.buildFromBuffer((Buffer) msg);
                        NetClient client = vertx.createNetClient();
                        client.connect(header.getSocketAddress(), result->{
                            if (result.succeeded()){
                                NetSocket socket = result.result();
                                if (header.isConnect()){
                                    Buffer response = Buffer.buffer(CommonConsts.CONNECT_RESPONSE);
                                    try {
                                        agentPipeline.doWrite(response);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                socket.handler(buffer->{
                                    try {
                                        agentPipeline.doWrite(buffer);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }else{
                                agentSocket.close();
                            }
                        });
                    });

            agentSocket.handler(buffer -> {
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
