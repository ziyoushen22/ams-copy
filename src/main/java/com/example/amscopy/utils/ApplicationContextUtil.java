package com.example.amscopy.utils;

import org.springframework.context.ApplicationContext;

public class ApplicationContextUtil {

    private static ApplicationContext applicationContext;

    public static void setApplicationContextUtil(ApplicationContext applicationContext) {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<?> clazz) {
        return (T) applicationContext.getBean(clazz);
    }

    public static boolean has(String name) {
        return applicationContext.containsBean(name);
    }

}
