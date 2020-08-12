package com.example.amscopy.listener.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class KafkaMessageHandlerFactory {

    private Map<String, KafkaMessageEventHandler> handlerMap = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        Map<String, KafkaMessageEventHandler> messageEventHandlerMap = applicationContext.getBeansOfType(KafkaMessageEventHandler.class);
        if (!CollectionUtils.isEmpty(messageEventHandlerMap)) {
            for (KafkaMessageEventHandler eventHandler : messageEventHandlerMap.values()) {
                if (eventHandler != null) {
                    KafkaMessageEvent event = eventHandler.supportEvent();
                    if (event != null) {
                        log.info("开始监听系统【{}】的【{}】消息", event.getEventSource(), event.getEventName());
                        handlerMap.put(event.getEventType(), eventHandler);
                    }
                }
            }
        }
    }

    public KafkaMessageEventHandler getHandler(String eventType) {
        return handlerMap.get(eventType);
    }
}
