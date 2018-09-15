package g.proxy;

/**
 * @author chengjin.lyf on 2018/8/30 上午1:31
 * @since 1.0.25
 */
public interface IMessageHandler {
    void handle(Object msg) throws  Exception;
}
