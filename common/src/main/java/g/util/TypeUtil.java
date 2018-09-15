package g.util;

import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/8/30 上午1:14
 * @since 1.0.25
 */
public class TypeUtil {
    public static boolean isBuffer(Object obj){
        return obj instanceof Buffer;
    }

    public static boolean isByteArray(Object obj){
        return obj instanceof byte[];
    }

    public static byte [] convertToByteArray(Object msg){
        if (TypeUtil.isBuffer(msg)){
            return ((Buffer)msg).getBytes();
        }else if (TypeUtil.isByteArray(msg)){
            return (byte[]) msg;
        }else{
            return null;
        }
    }

    public static Buffer convertToBuffer(Object msg){
        if (TypeUtil.isBuffer(msg)){
            return (Buffer) msg;
        }else if (TypeUtil.isByteArray(msg)){
            return Buffer.buffer((byte[])msg);
        }else{
            return null;
        }
    }
}
