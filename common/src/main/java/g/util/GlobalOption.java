package g.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chengjin.lyf on 2018/8/29 上午12:15
 * @since 1.0.25
 */
public class GlobalOption {

    private static GlobalOption instance = new GlobalOption();

    private GlobalOption(){
        //do sth
    }

    public static GlobalOption getInstance(){
        return instance;
    }

    private Map<String, Object> map = new HashMap<>();

    public <T> T get(String key){
        return (T) map.get(key);
    }

    public void put(String key, Object value){
        map.put(key, value);
    }
}
