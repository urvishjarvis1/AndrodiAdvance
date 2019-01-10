package com.example.urvish.annotationdemo;

import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

class Parent{
    @CheckResult
    @CallSuper
    @Status(priority = Status.Priority.HIGH,completion = 100,author = "Urvish")
    String  toastMessage(@NonNull String abc){
        return "Hello world from Parent"+abc;
    }
}