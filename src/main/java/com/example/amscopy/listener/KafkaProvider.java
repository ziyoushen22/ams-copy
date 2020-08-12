package com.example.amscopy.listener;

import com.alibaba.fastjson.JSONObject;
import com.example.amscopy.dto.EventDataEntity;
import com.example.amscopy.model.RiskWarningEventsModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProvider {

    private final String AMS_EVENT_TOPIC = "ams-event-topic";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void riskEventInfoSend(RiskWarningEventsModel info, String type) {
        try {
            EventDataEntity<RiskWarningEventsModel> entity = new EventDataEntity<>();
            entity.setEventDataType("");
            entity.setEventType(type);
            entity.setEventData(info);
            String s = JSONObject.toJSONString(entity);
            log.info("");
            kafkaTemplate.send(AMS_EVENT_TOPIC, s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
