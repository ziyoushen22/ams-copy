package com.example.amscopy.utils.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisQueuePublisher {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void send(String queue,String message){
        BoundListOperations<String, String> listOperations = stringRedisTemplate.boundListOps(queue);
        listOperations.leftPush(message);

    }
}
