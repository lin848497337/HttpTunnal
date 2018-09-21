package g.handler;

import g.proxy.IMessageHandler;
import g.proxy.socket.DatagramSocketWrapper;
import g.proxy.socket.ISocketWrapper;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chengjin.lyf on 2018/9/13 下午9:03
 * @since 1.0.25
 */
public class NetSocketHandler implements IMessageHandler {

    private ISocketWrapper netSocket;

    private static AtomicInteger counter = new AtomicInteger(0);

    public NetSocketHandler(ISocketWrapper netSocket) {
        this.netSocket = netSocket;
    }

    @Override
    public void handle(Object msg) throws Exception {
        netSocket.write((Buffer) msg);
    }
}
