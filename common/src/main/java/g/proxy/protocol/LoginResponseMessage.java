package g.proxy.protocol;

import g.util.CommonConsts;
import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/9/5 上午10:07
 * @since 1.0.25
 */
public class LoginResponseMessage extends Message {

    private String token;
    private byte result;
    private String msg;

    public LoginResponseMessage() {
        this.cmd = CommonConsts.COMMAND_RESPONSE;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public byte getResult() {
        return result;
    }

    public void setResult(byte result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public void decode() throws Exception {
        Buffer buffer = Buffer.buffer(codec);
        result = buffer.getByte(0);
        msg = buffer.getString(1, buffer.length());
    }

    @Override
    public void encode() throws Exception {
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(result);
        buffer.appendString(msg);
        codec = buffer.getBytes();
    }
}
