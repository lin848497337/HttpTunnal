package g.util;

import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * @author chengjin.lyf on 2018/8/23 下午9:19
 * @since 1.0.25
 */
public class CompressUtil {

    public static boolean OPEN_COMPRESS = false;

    public static byte[] compress(byte[] input) throws IOException {
        if (!OPEN_COMPRESS){
            return input;
        }
        return Snappy.compress(input);
    }

    public static byte[] uncompress(byte [] input) throws IOException {
        if (!OPEN_COMPRESS){
            return input;
        }
        return Snappy.uncompress(input);
    }
}
