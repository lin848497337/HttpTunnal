package g;

import g.proxy.protocol.BrowserMessage;
import g.proxy.protocol.Message;
import g.proxy.protocol.MessageFactory;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * @author chengjin.lyf on 2018/9/11 上午11:17
 * @since 1.0.25
 */
public class Message2MessageCodec implements MessageCodec<BrowserMessage, BrowserMessage> {

    @Override
    public void encodeToWire(Buffer buffer, BrowserMessage message) {
        try {
            message.encode();
            buffer.appendBytes(message.getCodec());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BrowserMessage decodeFromWire(int i, Buffer buffer) {
        try {
            Message msg = MessageFactory.getMessage(buffer);
            return (BrowserMessage) msg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BrowserMessage transform(BrowserMessage message) {
        return message;
    }

    @Override
    public String name() {
        return "msg2msg";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
