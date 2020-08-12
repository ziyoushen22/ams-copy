package com.example.amscopy.utils.unit;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = AmountInfoSerialize.class)
public @interface AmountInfo {
    AmountType value();

    boolean roundHalfUp() default true;

    String numberFormat() default "###,##0.000000";

}
