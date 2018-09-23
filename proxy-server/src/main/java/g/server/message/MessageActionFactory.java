package g.server.message;

import g.proxy.protocol.Message;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chengjin.lyf on 2018/9/23 上午11:03
 * @since 1.0.25
 */
public class MessageActionFactory {
    private static MessageActionFactory instance = new MessageActionFactory();

    private Map<Class<? extends Message>, MessageAction> actionMap = new HashMap<>();

    private MessageActionFactory(){
        //do sth
    }

    public void addMessageAction(MessageAction action){
        Type type = action.getClass().getGenericInterfaces()[0];
        if( type instanceof ParameterizedType){
            ParameterizedType pType = (ParameterizedType)type;
            Type claz = pType.getActualTypeArguments()[0];
            if( claz instanceof Class ){
                actionMap.put((Class<? extends Message>) claz, action);
            }
        }
    }

    public MessageAction getAction(Class cls){
        return actionMap.get(cls);
    }

    public static MessageActionFactory getInstance(){
        return instance;
    }

}
