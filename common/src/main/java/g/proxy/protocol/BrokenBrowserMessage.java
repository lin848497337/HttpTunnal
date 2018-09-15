package g.proxy.protocol;

import g.util.CommonConsts;
import io.vertx.core.buffer.Buffer;
import lombok.Data;

/**
 * @author chengjin.lyf on 2018/9/5 上午10:17
 * @since 1.0.25
 */
@Data
public class BrokenBrowserMessage extends Message {

    private Long browserId;

    public BrokenBrowserMessage() {
        this.cmd = CommonConsts.COMMAND_QUIT;
    }

    @Override
    public void decode() throws Exception {
        Buffer buffer = Buffer.buffer(codec);
        browserId = buffer.getLong(0);
    }

    @Override
    public void encode() throws Exception {
        Buffer buffer = Buffer.buffer();
        buffer.appendLong(browserId);
        codec = buffer.getBytes();
    }
}
