package g.util;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @author chengjin.lyf on 2018/8/17 下午7:35
 * @since 1.0.25
 */

@Data
public class JsonResponse<T> {

    private int code;
    private String message;
    private T data;

    public JsonResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public  static  JsonResponse success(String msg){
        return new JsonResponse(GSConstant.SUCCESS_CODE, msg);
    }

    public  static  JsonResponse success(){
        return success("success");
    }

    public  static <T> JsonResponse<T> success(T data){
        JsonResponse response = success();
        response.setData(data);
        return response;
    }

    public  static <T> JsonResponse<T> success(T data, String msg){
        JsonResponse response = success(msg);
        response.setData(data);
        return response;
    }

    public static JsonResponse failed(String msg){
        return new JsonResponse(GSConstant.FAILURE_CODE, msg);
    }

    public static JsonResponse failed(){
        return failed("failed");
    }

    public JSON toJSON(){
        return (JSON) JSON.toJSON(this);
    }
}
