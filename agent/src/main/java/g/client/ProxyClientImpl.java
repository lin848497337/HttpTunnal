package g.client;

import g.proxy.FilterPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chengjin.lyf on 2018/9/13 下午9:04
 * @since 1.0.25
 */
public class ProxyClientImpl implements ProxyClient {

    private static final Logger logger = LoggerFactory.getLogger(ProxyClientImpl.class);
    private FilterPipeline pipeline;

    public ProxyClientImpl(FilterPipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void write(Object obj) {
        try {
            this.pipeline.doWrite(obj);
        } catch (Exception e) {
            logger.error("output error ", e);
        }
    }
}
