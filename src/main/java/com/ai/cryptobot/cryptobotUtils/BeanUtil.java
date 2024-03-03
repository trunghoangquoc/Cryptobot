package com.ai.cryptobot.cryptobotUtils;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author ADMIN
 */
@Component
@SuppressWarnings({"java:S1118" , "java:S3010"})
public class BeanUtil {
    private static ApplicationContext applicationContext;

    public BeanUtil(ApplicationContext applicationContext) {
        BeanUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
