package g.proxy.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import g.util.CommonConsts;
import io.vertx.core.buffer.Buffer;
import lombok.Data;

/**
 * @author chengjin.lyf on 2018/9/5 上午10:07
 * @since 1.0.25
 */
@Data
public class LoginResponseMessage extends Message {

    private String token;
    private byte result;
    private String msg;

    public LoginResponseMessage() {
        this.cmd = CommonConsts.COMMAND_RESPONSE;
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
