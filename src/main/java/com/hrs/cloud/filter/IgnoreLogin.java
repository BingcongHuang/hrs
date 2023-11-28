package com.hrs.cloud.filter;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
@Documented
public @interface IgnoreLogin {
    boolean isIgnore() default true;
}
