package com.example.amscopy.listener.kafka;

import com.alibaba.fastjson.JSONObject;

public interface KafkaMessageEventHandler {

    KafkaMessageEvent supportEvent();

    void onMessage(JSONObject message);
}
