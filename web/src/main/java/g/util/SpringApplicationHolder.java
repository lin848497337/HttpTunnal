package g.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author chengjin.lyf on 2018/9/14 下午2:58
 * @since 1.0.25
 */
public class SpringApplicationHolder implements ApplicationContextAware{

    private static ApplicationContext applicationContext;


    public  static <T> T getBean(Class<T> tClass){
        return applicationContext.getBean(tClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationHolder.applicationContext = applicationContext;
    }
}
