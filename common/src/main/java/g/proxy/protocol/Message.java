package g.proxy.protocol;


/**
 * @author chengjin.lyf on 2018/8/30 上午12:56
 * @since 1.0.25
 */
public abstract class Message {

    protected byte cmd;
    protected byte[] codec;

    public byte getCmd() {
        return cmd;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public byte[] getCodec() {
        return codec;
    }

    public void setCodec(byte[] codec) {
        this.codec = codec;
    }

    public abstract void decode() throws Exception;

    public abstract void encode() throws Exception;
}
