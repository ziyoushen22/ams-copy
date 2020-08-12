package com.example.amscopy.utils.redis;

import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RedisQueue<T> {

    private BoundListOperations<String, T> listOperations;
    //基于底层I/O阻塞考虑
    private Lock lock = new ReentrantLock();

    public RedisQueue(RedisTemplate redisTemplate, String key) {
        this.listOperations = redisTemplate.boundListOps(key);
    }

    public T takeFromTail(int timeout) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            return listOperations.rightPop(timeout, TimeUnit.SECONDS);
        } finally {
            lock.unlock();
        }
    }

    public T takeFromTail() throws InterruptedException {
        return takeFromTail(30);
    }

    public void pushFromHead(T value) {
        listOperations.leftPush(value);
    }

    public void pushFromTail(T value) {
        listOperations.rightPush(value);
    }

    public T removeFromHead() {
        return listOperations.leftPop();
    }

    public T removeFromTail() {
        return listOperations.rightPop();
    }

    public T takeFromHead(int timeout) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            return listOperations.leftPop(timeout, TimeUnit.SECONDS);
        } finally {
            lock.unlock();
        }
    }

    public T takeFromHead() throws InterruptedException {
        return takeFromHead(30);
    }


}
