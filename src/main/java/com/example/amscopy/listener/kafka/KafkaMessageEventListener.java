package com.example.amscopy.listener.kafka;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class KafkaMessageEventListener {

    private static final String PMS_KAFKA_TOPIC = "pms-topic-wx";

    @Autowired
    private KafkaMessageHandlerFactory pmsMessageHandlerFactory;

    @KafkaListener(topics = {PMS_KAFKA_TOPIC})
    public void listener(ConsumerRecord record) {
        Optional<Object> optional = Optional.ofNullable(record.value());
        if (!optional.isPresent()) {
            log.info("[ kafka ] 监听器下发消息 | 消息为空，不予处理 ");
        }
        String msgStr = optional.get().toString();
        log.info("[ kafka ] 监听器下发消息 message = {}", msgStr);
        try {
            JSONObject message = JSONObject.parseObject(msgStr);
            String eventType = message.getString("eventType");
            KafkaMessageEventHandler handler = pmsMessageHandlerFactory.getHandler(eventType);
            if (handler != null) {
                handler.onMessage(message);
            } else {
                log.info("忽略未知eventType消息");
            }
        } catch (Exception e) {
            log.info("[ kafka ] 消息处理异常", e);
        }
    }
}
