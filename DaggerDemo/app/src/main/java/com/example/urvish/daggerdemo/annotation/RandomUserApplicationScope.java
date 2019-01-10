package com.example.urvish.daggerdemo.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @Scope used to create Scope of Object.
 */
@Scope
@Retention(RetentionPolicy.CLASS)
public @interface RandomUserApplicationScope {
}
