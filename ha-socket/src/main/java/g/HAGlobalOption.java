package g;

/**
 * @author chengjin.lyf on 2018/9/22 上午7:39
 * @since 1.0.25
 */
public class HAGlobalOption {

    private HAMode haMode = HAMode.PERIODIC;

    private long rto = 5000;

    private int maxPacketDataSize = 485;

    private int ttl = 10;

    public int getTtl() {
        return ttl;
    }

    public HAGlobalOption setTtl(int ttl) {
        this.ttl = ttl;
        return this;
    }

    public HAMode getHaMode() {
        return haMode;
    }

    public HAGlobalOption setHaMode(HAMode haMode) {
        this.haMode = haMode;
        return this;
    }

    public long getRto() {
        return rto;
    }

    public HAGlobalOption setRto(long rto) {
        this.rto = rto;
        return this;
    }

    public int getMaxPacketDataSize() {
        return maxPacketDataSize;
    }

    public HAGlobalOption setMaxPacketDataSize(int maxPacketDataSize) {
        this.maxPacketDataSize = maxPacketDataSize;
        return this;
    }
}
