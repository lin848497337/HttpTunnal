package g.http;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.SocketAddress;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chengjin.lyf on 2018/8/25 下午8:09
 * @since 1.0.25
 */
public class HttpMessageHeader {

    private List<String> lineList;

    private Map<String, String> headerMap = new HashMap<>();

    private int length;

    private String method;

    private String uri;

    private String protocol;

    private Buffer buffer;

    private boolean simpleProcess = true;

    public static HttpMessageHeader buildFromBuffer(Buffer buffer) {
        HttpMessageHeader messageHeader = new HttpMessageHeader();
        messageHeader.processMessageBefore(buffer.getBytes());
        messageHeader.buffer = buffer;
        messageHeader.buildHeader();
        return messageHeader;
    }

    public SocketAddress getSocketAddress() {
        String domain = uri;
        int port = 80;
        if (domain.startsWith("https://")) {
            domain = domain.substring(8);
            port = 443;
        } else if (domain.startsWith("http://")) {
            domain = domain.substring(7);
            port = 80;
        }
        int firstCol = domain.indexOf("/");
        if (firstCol != -1){
            domain = domain.substring(0, firstCol);
        }
        if (isConnect()){
            port = 443;
        }
        String[] ipport = domain.split(":");
        if (ipport.length == 2) {
            port = Integer.valueOf(ipport[1]);
            domain = ipport[0];
        }
        SocketAddress inetAddress = SocketAddress.inetSocketAddress(port, domain);
        return inetAddress;
    }

    public boolean isConnect() {
        return isMethod("CONNECT");
    }

    public boolean isPost() {
        return isMethod("POST");
    }

    public boolean isGet() {
        return isMethod("GET");
    }

    public boolean isUpgradeSecure(){
        return "1".equalsIgnoreCase(headerMap.get("Upgrade-Insecure-Requests"));
    }

    public int getContentLength() {
        String len = headerMap.get("Content-Length");
        if (len == null) {
            return 0;
        }
        return Integer.valueOf(len);
    }

    public String getProtocol() {
        return protocol;
    }

    public boolean isMethod(String methodName) {
        return method.equalsIgnoreCase(methodName);
    }

    private void buildHeader() {
        String firstLine = lineList.get(0);
        String[] firstLineData = firstLine.split(" ");
        method = firstLineData[0];
        uri = firstLineData[1];
        protocol = firstLineData[2];

        if (length == 0) {
            return;
        }
        for (int i = 1; i < lineList.size(); i++) {
            String propValue = lineList.get(i);
            int splitIdx = propValue.indexOf(":");
            String key = propValue.substring(0, splitIdx).trim();
            String value = propValue.substring(splitIdx + 1).trim();
            headerMap.put(key, value);
        }
    }

    public boolean isOk() {
        return length != 0;
    }


    private void processMessageBefore(byte[] data) {
        List<String> lineList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int CRLFCount = 0;
        int DOUBLE = 2;

        boolean occurCR = false;
        for (int i = 0; i < data.length; i++) {
            char code = (char) data[i];
            switch (code) {
                case '\r':
                    occurCR = true;
                    break;
                case '\n':
                    if (occurCR) {
                        CRLFCount++;
                    }
                    occurCR = false;
                    if (sb.length() == 0) {
                        break;
                    }
                    lineList.add(sb.toString());
                    sb.delete(0, sb.length());
                    break;

                default:
                    sb.append(code);
                    CRLFCount = 0;
                    occurCR = false;
                    break;

            }

            if (simpleProcess && lineList.size() > 0){
                length = i;
                break;
            }

            if (CRLFCount == DOUBLE) {
                length = i;
                break;
            }
        }
        this.lineList = lineList;
    }

    public String getUri() {
        return uri;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    @Override
    public String toString() {
        return "HttpMessageHeader{" + "lineList=" + lineList + ", headerMap=" + headerMap + ", length=" + length
               + ", method='" + method + '\'' + ", uri='" + uri + '\'' + ", protocol='" + protocol + '\'' + '}';
    }
}
