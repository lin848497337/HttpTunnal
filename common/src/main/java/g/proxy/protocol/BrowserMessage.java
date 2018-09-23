package g.proxy.protocol;

import g.util.CommonConsts;
import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/9/5 上午10:22
 * @since 1.0.25
 */
public class BrowserMessage extends Message {

    private long browserId;
    private byte[] data;

    public BrowserMessage() {
        this.cmd = CommonConsts.COMMAND_MSG;
    }

    public long getBrowserId() {
        return browserId;
    }

    public void setBrowserId(long browserId) {
        this.browserId = browserId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void decode() throws Exception {
        Buffer buffer = Buffer.buffer(codec);
        browserId = buffer.getLong(0);
        data = buffer.getBytes(8, buffer.length());
    }

    @Override
    public void encode() throws Exception {
        Buffer buffer = Buffer.buffer();
        buffer.appendLong(browserId);
        buffer.appendBytes(data);
        codec = buffer.getBytes();
    }
}
