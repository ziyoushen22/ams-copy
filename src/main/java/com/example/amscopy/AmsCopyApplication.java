package com.example.amscopy;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.example.amscopy.mapper"})
@Slf4j
public class AmsCopyApplication {

    public static void main(String[] args) {
        log.info("ams正在启动");
        try {
            SpringApplication.run(AmsCopyApplication.class, args);
        } catch (Exception e) {
            log.error("启动失败", e);
            System.exit(-1);
        }
        log.info("启动成功");

    }

}
