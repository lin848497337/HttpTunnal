package g;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chengjin.lyf on 2018/9/21 下午9:27
 * @since 1.0.25
 */
public class Sequence {

    private AtomicLong atomicLong;

    public Sequence() {
        this(0);
    }

    public Sequence(long initial){
        atomicLong = new AtomicLong(initial);
    }

    public long next(){
        return atomicLong.addAndGet(1);
    }
}
