package g;

import io.vertx.core.Vertx;

/**
 * @author chengjin.lyf on 2018/9/22 上午6:28
 * @since 1.0.25
 */
public class VertxContext {

    private static Vertx vertx;

    private static HAGlobalOption option;

    public static void init(Vertx vertx, HAGlobalOption option){
        VertxContext.vertx = vertx;
        VertxContext.option = option;
    }

    public static Vertx getVertx() {
        return vertx;
    }

    public static HAGlobalOption getOption() {
        return option;
    }
}
