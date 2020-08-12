package com.example.amscopy.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.amscopy.listener.kafka.KafkaMessageEvent;
import com.example.amscopy.listener.kafka.KafkaMessageEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AssetsMessageHandler implements KafkaMessageEventHandler {

    @Override
    public KafkaMessageEvent supportEvent() {
        return KafkaMessageEvent.ASSET_EVENT;
    }

    @Override
    public void onMessage(JSONObject message) {
        JSONArray asset = message.getJSONArray("asset");
        //TODO

    }
}
