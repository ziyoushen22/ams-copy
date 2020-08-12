package com.example.amscopy.utils.redis;

import java.lang.annotation.*;

@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface OnRedisMessage {
    String topic() default "";

    String queue() default "";

}
