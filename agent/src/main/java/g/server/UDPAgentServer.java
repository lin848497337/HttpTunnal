package g.server;

import g.client.ProxyClient;
import g.client.ProxyClientImpl;
import g.handler.NetSocketHandler;
import g.handler.PiplineReadHandler;
import g.handler.ProxyDataHandler;
import g.proxy.FilterPipeline;
import g.proxy.filter.*;
import g.proxy.protocol.LoginMessage;
import g.proxy.socket.DatagramSocketWrapper;
import g.proxy.socket.ISocketWrapper;
import g.util.CommonConsts;
import io.vertx.core.Vertx;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;



/**
 * @author chengjin.lyf on 2018/9/20 下午9:58
 * @since 1.0.25
 */
public class UDPAgentServer extends AbstractVerticle {

    private int port;

    private String proxyServerIp;
    private int proxyServerPort;

    private Logger logger = LoggerFactory.getLogger(UDPAgentServer.class);

    public UDPAgentServer(int port, String proxyServerIp, int proxyServerPort) {
        this.port = port;
        this.proxyServerIp = proxyServerIp;
        this.proxyServerPort = proxyServerPort;
    }

    @Override
    public void start() throws Exception {
        Vertx vertx = getVertx();
        logger.info("init agent server !");
        DatagramSocket proxySocket = vertx.createDatagramSocket();
        DatagramSocketWrapper socketWrapper = new DatagramSocketWrapper(proxyServerIp, proxyServerPort, proxySocket, vertx);
        ProxyClient proxyClient = initProxyClient(socketWrapper);

        FilterPipeline proxyReadPipeline = new FilterPipeline()
                .addToTail(new UnpackFilter())
                .addToTail(new DecryptFilter())
                .addToTail(new DecodeFilter())
                .setHandler(new ProxyDataHandler(vertx, proxyClient, port));

        socketWrapper.handler(new PiplineReadHandler(proxyReadPipeline));

        proxySocket.handler(packet->{
            socketWrapper.receivePacket(packet);
        });

        logger.info("try to login");
        // all ok now try login
        LoginMessage loginMessage = new LoginMessage();
        loginMessage.setCmd(CommonConsts.COMMAND_LOGIN);
        loginMessage.setAccount("admin");
        loginMessage.setPassword("admin_test");
        proxyClient.write(loginMessage);


    }

    private ProxyClient initProxyClient(ISocketWrapper proxySocket) {

        FilterPipeline outputToProxyPipeline = new FilterPipeline().addToTail(new EncodeFilter())
                .addToTail(new EncryptFilter())
                .addToTail(new PackageFilter())
                .setHandler(new NetSocketHandler(proxySocket));

        ProxyClient proxyClient = new ProxyClientImpl(outputToProxyPipeline);
        return proxyClient;
    }

    @Override
    public void stop() throws Exception {
    }
}
