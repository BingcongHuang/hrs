package com.hrs.cloud.filter.session;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 手动注入spring容器
 */
@Component(value="applicationContextUtil")
public class ApplicationContextUtil implements ApplicationContextAware {


    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext  = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        assertApplicationContext();
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        assertApplicationContext();
        return (T) applicationContext.getBean(beanName);
    }
    public static <T> T getBean(Class<T> clazz) {
        assertApplicationContext();
        return applicationContext.getBean(clazz);
    }
    public static <T> T getBean(String penName,Class<T> clazz){
        assertApplicationContext();
        return applicationContext.getBean(penName,clazz);
    }

    //判断上下文对象是否未注入
    private static void assertApplicationContext() {
        if (ApplicationContextUtil.applicationContext == null) {
            throw new RuntimeException("没有注入 applicationContext");
        }
    }

}
