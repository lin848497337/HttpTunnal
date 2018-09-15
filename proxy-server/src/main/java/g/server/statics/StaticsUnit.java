package g.server.statics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chengjin.lyf on 2018/8/25 上午11:44
 * @since 1.0.25
 */
public class StaticsUnit {

    private static final Logger logger = LoggerFactory.getLogger(StaticsUnit.class);

    private AtomicLong totalAgentIn = new AtomicLong(0);
    private AtomicLong totalAgentOut = new AtomicLong(0);


    private AtomicLong last20AgentIn = new AtomicLong(0);
    private AtomicLong last20AgentOut = new AtomicLong(0);

    private AtomicLong totalServerIn = new AtomicLong(0);
    private AtomicLong totalServerOut = new AtomicLong(0);


    private AtomicLong last20ServerIn = new AtomicLong(0);
    private AtomicLong last20ServerOut = new AtomicLong(0);

    /** begin 20 s 统计 tps**/
    private volatile long serverInTps = 0;
    private volatile long serverOutTps = 0;
    private volatile long agentInTps = 0;
    private volatile long agentOutTps = 0;
    /** end 20 s 统计 tps**/

    private AtomicLong totalWithAgentUseTime = new AtomicLong(0);

    private AtomicLong maxWithAgentUseTime = new AtomicLong(0);

    private AtomicLong minWithAgentUseTime = new AtomicLong(0);

    private AtomicLong totalWithRemoveUseTime = new AtomicLong(0);

    private AtomicLong maxWithRemoveUseTime = new AtomicLong(0);

    private AtomicLong minWithRemoveUseTime = new AtomicLong(0);

    private static ThreadLocal<Long> beginWithAgentLocal = new ThreadLocal();

    private static ThreadLocal<Long> beginWithRemoveLocal = new ThreadLocal();



    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, r -> {
        Thread t = new Thread(r, "statics-unit");
        t.setDaemon(true);
        return t;
    });

    public void doAgentOut(int byteSize){
        totalAgentOut.addAndGet(byteSize);
        last20AgentOut.addAndGet(byteSize);
    }

    public void doAgentIn(int byteSize){
        totalAgentIn.addAndGet(byteSize);
        last20AgentIn.addAndGet(byteSize);
        beginWithAgentLocal.set(System.currentTimeMillis());

    }


    public void doServerOut(int byteSize){
        totalServerOut.addAndGet(byteSize);
        last20ServerOut.addAndGet(byteSize);
        beginWithRemoveLocal.set(System.currentTimeMillis());
    }

    public void doServerIn(int byteSize){
        totalServerIn.addAndGet(byteSize);
        last20ServerIn.addAndGet(byteSize);
    }


    public void start(){
        executorService.scheduleAtFixedRate(() -> {
            serverInTps = last20ServerIn.get() / 20;
            last20ServerIn.set(0);

            serverOutTps = last20ServerOut.get() / 20;
            last20ServerOut.set(0);

            agentInTps = last20AgentIn.get() / 20;
            last20AgentIn.set(0);

            agentOutTps = last20AgentOut.get() / 20;
            last20AgentOut.set(0);
        }, 20, 20, TimeUnit.SECONDS);

        executorService.scheduleAtFixedRate(()->{
            print();
        }, 1, 1, TimeUnit.MINUTES);
    }

    private void print(){
        StringBuilder sb = new StringBuilder();
        String totalUnit = String.format("totalAgentIn totalAgentOut totalServerIn totalServerOut\n%12d %13d %13d %14d",
                totalAgentIn.get(), totalAgentOut.get(), totalServerIn.get(), totalServerOut.get());

        String lastUnit = String.format("20AgentIn 20AgentOut 20ServerIn 20ServerOut\n%7d/s %8d/s %8d/s %9d/s",
                agentInTps, agentOutTps, serverInTps, serverOutTps);

        sb.append(totalUnit).append("\n").append(lastUnit);
        logger.info("statics unit :\n" + sb.toString());
    }

    public void stop(){
        executorService.shutdown();
    }


}
