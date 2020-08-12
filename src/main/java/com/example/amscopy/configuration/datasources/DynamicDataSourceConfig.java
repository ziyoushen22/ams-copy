package com.example.amscopy.configuration.datasources;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Configuration
@Slf4j
public class DynamicDataSourceConfig {

//    @Autowired
//    private DruidFirst druidFirst;
//
//    @Autowired
//    private DruidSecond druidSecond;

    @Autowired
    private WallConfig wallConfig;

    @Autowired
    private WallFilter wallFilter;

    private List wallFilter() {
        ArrayList<Object> list = new ArrayList<>();
        wallFilter.setConfig(wallConfig);
        list.add(wallFilter);
        return list;
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.first")
    public DataSource firstDataSource() {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        try {
//            if (druidFirst != null) {
//                dataSource.setPasswordCallback(druidFirst);       //如果密码存在
//            }
            dataSource.setProxyFilters(wallFilter());
        } catch (Exception e) {
            log.error("druid configuration initialization passwordCallbackClassName", e);
        }
        return dataSource;
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.second")
    public DataSource secondDataSource() {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        try {
//            if (druidSecond != null) {
//                dataSource.setPasswordCallback(druidSecond);
//            }
            dataSource.setProxyFilters(wallFilter());
        } catch (Exception e) {
            log.error("druid configuration initialization passwordCallbackClassName", e);
        }
        return dataSource;
    }

    @Bean
    @Primary
    @DependsOn(value = {"firstDataSource", "secondDataSource"})
    public DynamicDataSource dataSource(DataSource firstDataSource, DataSource secondDataSource) {
        HashMap<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("first", firstDataSource);
        targetDataSources.put("second", secondDataSource);
        return new DynamicDataSource(firstDataSource, targetDataSources);
    }


}
