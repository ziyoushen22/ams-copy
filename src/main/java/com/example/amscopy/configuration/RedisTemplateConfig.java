package com.example.amscopy.configuration;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableCaching
@EnableRedisHttpSession
@ConfigurationProperties(prefix = "spring.cache")
@Data
public class RedisTemplateConfig {
    private Map<String, Long> ttlMap;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        HashMap<String, RedisCacheConfiguration> config = new HashMap<>(ttlMap.size());
        Set<Map.Entry<String, Long>> entries = ttlMap.entrySet();
        for (Map.Entry<String, Long> entry : entries) {
            String key = entry.getKey();
            Long ttl = entry.getValue();
            if (StringUtils.isNotEmpty(key)) {
                config.put(key, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(ttl)));
            }
        }
        return RedisCacheManager
                .builder(factory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)))
                .withInitialCacheConfigurations(config)
                .transactionAware()
                .build();
    }

    @Bean
    public KeyGenerator keyGenerator1() {
        return new KeyGenerator1();
    }

    public class KeyGenerator1 implements KeyGenerator {
        @Override
        public Object generate(Object o, Method method, Object... params) {
            StringBuilder sb = new StringBuilder();
            for (Object param : params) {
                if (param != null) {
                    if (param instanceof Integer) {
                        sb.append((Integer) ((Integer) param).intValue()).append(":");
                    }
                    if (param instanceof String) {
                        sb.append((String) param.toString()).append(":");
                    }
                } else {
                    sb.append("null:");
                }
            }
            String result = sb.toString();
            return result.substring(0, result.length() - 1);
        }
    }


}
