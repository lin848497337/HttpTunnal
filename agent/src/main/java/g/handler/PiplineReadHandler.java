package g.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import g.proxy.FilterPipeline;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/9/13 下午9:06
 * @since 1.0.25
 */
public class PiplineReadHandler implements Handler<Buffer> {

    private FilterPipeline pipeline;

    private static final Logger logger = LoggerFactory.getLogger(PiplineReadHandler.class);

    public PiplineReadHandler(FilterPipeline pipeline){
        this.pipeline = pipeline;
    }


    @Override
    public void handle(Buffer buffer) {
        try {
            pipeline.doRead(buffer);
        } catch (Exception e) {
            logger.error("pipline read error!", e);
        }
    }
}
