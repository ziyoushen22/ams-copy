package com.example.amscopy.utils.redis;


import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface RedisListener {


}
