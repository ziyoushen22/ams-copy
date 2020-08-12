package com.example.amscopy.utils.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
@Component
public class RedisListenerResolver implements ApplicationContextAware, InitializingBean {

    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Autowired
    private RedisQueueMessageListenerContainer redisQueueMessageListenerContainer;

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("scanning redis listener");
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RedisListener.class);
        if (!CollectionUtils.isEmpty(beansWithAnnotation)) {
            for (Object listener : beansWithAnnotation.values()) {
                Class<?> clazz = listener.getClass();
                if (!clazz.isAnnotationPresent(RedisListener.class)) {
                    clazz = clazz.getSuperclass();
                }
                for (Method method : clazz.getDeclaredMethods()) {
                    if (!method.isAnnotationPresent(OnRedisMessage.class)) {
                        continue;
                    }
                    OnRedisMessage annotation = method.getAnnotation(OnRedisMessage.class);
                    String topic = annotation.topic();
                    if (StringUtils.isNotEmpty(topic)) {
                        log.info("listen redis on topic [{}] with [{}.{}]", topic, listener.getClass(), method.getName());
                        redisMessageListenerContainer.addMessageListener(listenerAdapter(listener, method.getName()), new PatternTopic(topic));
                    }
                    String queue = annotation.queue();
                    if (StringUtils.isNotEmpty(queue)) {
                        log.info("listen redis on queue [{}] with [{}.{}]", topic, listener.getClass(), method.getName());
                        redisQueueMessageListenerContainer.addMessageListener(queue, new RedisQueueListener() {
                            @Override
                            public void onMessage(Object value) {
                                try {
                                    method.invoke(listener, value);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                    throw new RuntimeException("access target method failed:\n", e);
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                    throw new RuntimeException("access target method failed:\n", e);
                                }
                            }
                        });
                    }
                }
            }
            log.info("listener started...");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private MessageListenerAdapter listenerAdapter(Object listener, String methodName) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(listener, methodName);
        messageListenerAdapter.afterPropertiesSet();
        return messageListenerAdapter;
    }

}
