package com.example.amscopy.utils.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class RedisQueueMessageListenerContainer implements InitializingBean, DisposableBean {

    //单个queue开启的监听线程数
    private int runThreadNumPerQueue = 1;
    //是否运行
    private boolean isRunning;
    //执行监听的线程池
    private ExecutorService exec;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private Map<String, RedisQueue<String>> queueMap = new HashMap<>();

    private RedisQueue<String> getRedisQueue(String queueName) {
        return queueMap.get(queueName);
    }

    public void addMessageListener(String queueName, RedisQueueListener listener) {
        RedisQueue<String> queue = new RedisQueue<>(stringRedisTemplate, queueName);

        for (int i = 0; i < runThreadNumPerQueue; i++) {
            this.exec.execute(new ListenerThread(this, queue, listener));
        }
        queueMap.put(queueName, queue);
    }

    @Override
    public void destroy() throws Exception {
        isRunning = false;
        this.exec.shutdown();
        log.info("redisMQListener exiting...");
        while (!this.exec.isTerminated()) {

        }
        log.info("redisMQListener exited.");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.isRunning = true;
        this.exec = Executors.newCachedThreadPool((r -> {
            final AtomicInteger threadNumber = new AtomicInteger(1);
            return new Thread(r, "redisMQListener-" + threadNumber.getAndDecrement());
        }));
        this.queueMap = new HashMap<>();
    }

    class ListenerThread<T> implements Runnable {
        private RedisQueueMessageListenerContainer container;
        private RedisQueue<T> queue;
        private RedisQueueListener listener;

        public ListenerThread(RedisQueueMessageListenerContainer container, RedisQueue<T> queue, RedisQueueListener listener) {
            this.container = container;
            this.queue = queue;
            this.listener = listener;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    T value = this.queue.takeFromTail();
                    if (value != null) {
                        listener.onMessage(value);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
