package com.example.amscopy.utils.redis;

public interface RedisQueueListener<T> {

    void onMessage(T value);
}
