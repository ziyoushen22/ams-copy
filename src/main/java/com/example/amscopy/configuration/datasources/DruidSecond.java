package com.example.amscopy.configuration.datasources;

import com.alibaba.druid.util.DruidPasswordCallback;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Qualifier("druidSecond")
@ConfigurationProperties(prefix = "cyberark.second")
@Data
@Slf4j
public class DruidSecond extends DruidPasswordCallback {

    private String enable;
    private String version;
    private String appId;
    private String safe;
    private String folder;
    private String appKey;
    private String url;
    private String object;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        if ("true".equalsIgnoreCase(enable)){
            try{
                String password="";
                super.setPassword(password.toCharArray());
            }catch (Exception e){
                log.error("获取cyberark密码失败");
            }
        }
    }
}
