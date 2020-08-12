package com.example.amscopy.configuration.datasources;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Slf4j
public class DruidDataSourceManagement {

    @Component
    @ConfigurationProperties(prefix = "druid.management")
    @Data
    class DruidManagementProperties {
        private String uri = "druid";
        private String username;
        private String password;
        private String allow = "127.0.0.1";
        private String deny = "";

        @Bean
        @ConditionalOnProperty(name = "druid.management.enabled", havingValue = "true")
        public ServletRegistrationBean druidServlet() {
            log.info("初始化数据库连接池");
            ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<>(new StatViewServlet(), "/" + uri + "/");
            servletRegistrationBean.addInitParameter("allow", allow);
            servletRegistrationBean.addInitParameter("deny", deny);
            servletRegistrationBean.addInitParameter("loginUsername", username);
            servletRegistrationBean.addInitParameter("loginPassword", password);
            servletRegistrationBean.addInitParameter("resetEnable", "false");
            return servletRegistrationBean;
        }

        @Bean
        @ConditionalOnProperty(name = "druid.management.enabled", havingValue = "true")
        public FilterRegistrationBean filterRegistrationBean() {
            FilterRegistrationBean<WebStatFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>(new WebStatFilter());
            filterFilterRegistrationBean.addUrlPatterns("/*");
            filterFilterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
            return filterFilterRegistrationBean;
        }

        @Bean
        public WallConfig wallConfig() {
            WallConfig wallConfig = new WallConfig();
            wallConfig.setMultiStatementAllow(true);
            return wallConfig;
        }

        @Bean
        public WallFilter wallFilter() {
            return new WallFilter();
        }
    }
}
