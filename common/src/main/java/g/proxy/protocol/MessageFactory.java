package g.proxy.protocol;

import g.util.CommonConsts;
import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/9/5 上午10:02
 * @since 1.0.25
 */
public class MessageFactory {
    public static Message getMessage(Buffer buffer) throws Exception {
        byte cmd = buffer.getByte(0);
        byte []codec = buffer.getBytes(1, buffer.length());
        Message msg;
        switch (cmd)
        {
            case CommonConsts.COMMAND_LOGIN:
                msg = new LoginMessage();
                break;
            case CommonConsts.COMMAND_MSG:
                msg = new BrowserMessage();
                break;
            case CommonConsts.COMMAND_QUIT:
                msg = new BrokenBrowserMessage();
                break;
            case CommonConsts.COMMAND_RESPONSE:
                msg = new LoginResponseMessage();
                break;
            default:
                throw new UnsupportedOperationException();
        }
        msg.setCodec(codec);
        msg.setCmd(cmd);
        msg.decode();
        return msg;
    }
}
