package g.proxy.protocol;

import lombok.Data;

/**
 * @author chengjin.lyf on 2018/8/30 上午12:56
 * @since 1.0.25
 */
@Data
public abstract class Message {

    protected byte cmd;
    protected byte[] codec;


    public abstract void decode() throws Exception;

    public abstract void encode() throws Exception;
}
