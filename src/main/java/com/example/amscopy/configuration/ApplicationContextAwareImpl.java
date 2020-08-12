package com.example.amscopy.configuration;

import com.example.amscopy.utils.ApplicationContextUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Data
public class ApplicationContextAwareImpl implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("应用程序上下文 ：[{}]", "开始初始化");
        ApplicationContextUtil.setApplicationContextUtil(applicationContext);
        log.info("应用程序上下文 getId ：[{}]", applicationContext.getId());
        log.info("应用程序上下文 getApplicationName ：[{}]", applicationContext.getApplicationName());
        log.info("应用程序上下文 getStartupDate ：[{}]", applicationContext.getStartupDate());
        log.info("应用程序上下文 ：[{}]", "初始化完成");

    }
}
