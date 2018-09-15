package g;

import g.proxy.protocol.BrokenBrowserMessage;
import g.proxy.protocol.BrowserMessage;
import g.proxy.protocol.Message;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.eventbus.impl.codecs.BufferMessageCodec;
import io.vertx.core.json.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author chengjin.lyf on 2018/9/11 上午10:35
 * @since 1.0.25
 */
public class StaticsUnit extends AbstractVerticle {

    private long timerId;

    private static final long period = TimeUnit.SECONDS.toMillis(15);

    private Map<Long, Statics> staticsMap = new ConcurrentHashMap<>();

    class Statics{
        private long uid;
        private long connectTIME;
        /**
         * 0: connect , 1 exchange , 2 broken
         */
        private int state;
        private long lastRequest;
        private long lastResponse;
        /**
         * 0 : server, 1: client
         */
        private long brokenSide;

    }

    @Override
    public void start() throws Exception {

        timerId = vertx.setPeriodic(period, time -> {
            printLog();
        });

        vertx.eventBus().consumer("proxyBroken", msg->{
            Statics s = staticsMap.get((Json.decodeValue((String)msg.body(), BrokenBrowserMessage.class)).getBrowserId());
            s.state = 2;
            s.brokenSide = 0;
        });
        vertx.eventBus().consumer("brokenProxy", msg->{
            Statics s = staticsMap.get((Json.decodeValue((String)msg.body(), BrokenBrowserMessage.class)).getBrowserId());
            s.state = 2;
            s.brokenSide = 1;
        });
        vertx.eventBus().consumer("proxyResponse", msg->{
            Statics s = staticsMap.get((Json.decodeValue((String)msg.body(), BrowserMessage.class)).getBrowserId());
            s.state = 1;
            s.lastResponse = System.currentTimeMillis();

        });
        vertx.eventBus().consumer("requestProxy", msg->{
            Statics s = staticsMap.get(Json.decodeValue((String)msg.body(), BrowserMessage.class).getBrowserId());
            s.state = 1;
            s.lastRequest = System.currentTimeMillis();
        });
        vertx.eventBus().consumer("connect", uid->{
            Statics newOne = new Statics();
            newOne.state = 0;
            newOne.uid = (long) uid.body();
            newOne.connectTIME = System.currentTimeMillis();
            Statics s = staticsMap.put(newOne.uid, newOne);
            if (s != null){
                System.out.println("error , duplicate uid!" + newOne.uid);
            }

        });
    }

    private void printLog(){
        long warning = TimeUnit.SECONDS.toMillis(10);
        List<Statics> noData = new ArrayList();
        List<Statics> noResponse = new ArrayList();
        List<Statics> proxyBroken = new ArrayList();
        List<Statics> browserBroken = new ArrayList();
        long now = System.currentTimeMillis();
        for (Map.Entry<Long, Statics> entry : staticsMap.entrySet()){
            Statics s = entry.getValue();
            switch (s.state)
            {
                case 0:
                    if(now - s.connectTIME > warning){
                        noData.add(s);
                    }
                    break;
                case 1:
                    if(s.lastRequest > s.lastResponse && now - s.lastRequest > warning){
                        noResponse.add(s);
                    }
                    break;
                case 2:
                    if (s.brokenSide == 0){
                        proxyBroken.add(s);
                    }
                    break;
                case 3:
                    if (s.brokenSide == 1){
                        browserBroken.add(s);
                    }
                    break;
            }
        }
        System.out.println(String.format("%5d, %5d, %5d, %5d", noData.size(), noResponse.size(), proxyBroken.size(), browserBroken.size()));
    }

    @Override
    public void stop() throws Exception {
        vertx.cancelTimer(timerId);
    }
}
