package com.example.urvish.annotationdemo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Status {
    public enum Priority {LOW, MEDIUM, HIGH}
    Priority priority() default Priority.LOW;
    String author() default "Urvish";
    int completion() default 0;
}
