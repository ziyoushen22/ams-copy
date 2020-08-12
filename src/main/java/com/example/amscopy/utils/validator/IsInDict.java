package com.example.amscopy.utils.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {IsInDictValidator.class})
public @interface IsInDict {

    boolean required() default true;

    String dictGroup() default "";

    String message() default "该数据字典不存在！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
