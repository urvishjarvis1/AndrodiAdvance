package com.example.urvish.daggerdemo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * @Qualifier which is used to distinguish between two different Objects who has same ReturnType.
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ContextName {
    String value() default "";
}
