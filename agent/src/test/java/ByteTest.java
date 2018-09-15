import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/9/13 下午4:54
 * @since 1.0.25
 */
public class ByteTest {


    public static void  main(String args[]){
        Buffer buffer = Buffer.buffer();
        byte[] b = new byte[]{21,3,1,0,2,2,22};
        buffer.appendBytes(b);
        System.out.println(buffer.toString(""));
    }
}
