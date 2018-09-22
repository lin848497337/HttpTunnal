package g;

import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/9/21 下午9:29
 * @since 1.0.25
 */
public class DataPacket {

    public static byte DATA_TYPE_ACK = 0;
    public static byte DATA_TYPE_DATA = 1;
    private byte dataType;
    private long sequence;
    private short subSequence;
    private short packetSize;
    private short dataLength;
    private Buffer data;
    private long timestamp;
    private int sendCounter = 0;

    private boolean valid = true;

    public DataPacket(byte dataType) {
        this.dataType = dataType;
    }

    public DataPacket() {
    }

    public boolean isACK(){
        return dataType == DATA_TYPE_ACK;
    }

    public Buffer packet() {
        Buffer packet = Buffer.buffer();
        packet.appendByte(dataType);
        packet.appendLong(sequence);
        packet.appendShort(subSequence);
        if (isACK()){
            return packet;
        }
        dataLength = (short) (15 + data.length());
        packet.appendShort(packetSize);
        packet.appendShort(dataLength);
        packet.appendBuffer(data);
        return packet;
    }

    public void unpacket(Buffer buffer){
        dataType = buffer.getByte(0);
        sequence = buffer.getLong(1);
        subSequence = buffer.getShort(9);
        if (dataType == DATA_TYPE_ACK){
            return;
        }
        packetSize = buffer.getShort(11);
        dataLength = buffer.getShort(13);
        if (dataLength != buffer.length()){
            valid = false;
            return ;
        }
        data = buffer.getBuffer(15, dataLength);
    }

    public boolean isValid() {
        return valid;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public short getSubSequence() {
        return subSequence;
    }

    public void setSubSequence(short subSequence) {
        this.subSequence = subSequence;
    }

    public short getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(short packetSize) {
        this.packetSize = packetSize;
    }

    public short getDataLength() {
        return dataLength;
    }

    public Buffer getData() {
        return data;
    }

    public void setData(Buffer data) {
        this.data = data;
    }

    public void mark(){
        this.timestamp = System.currentTimeMillis();
        this.sendCounter++;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getSendCounter() {
        return sendCounter;
    }

    @Override public String toString() {
        return "DataPacket{" + "dataType=" + dataType + ", sequence=" + sequence + ", subSequence=" + subSequence
               + ", packetSize=" + packetSize + ", dataLength=" + dataLength + ", timestamp=" + timestamp
               + ", sendCounter=" + sendCounter + '}';
    }
}
