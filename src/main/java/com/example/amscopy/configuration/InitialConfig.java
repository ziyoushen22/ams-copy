package com.example.amscopy.configuration;

import com.example.amscopy.service.api.RedisSerVice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class InitialConfig implements ApplicationRunner {

    @Autowired
    private RedisSerVice redisSerVice;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        redisSerVice.loadDictDataToRedis();
    }
}
