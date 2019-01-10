package com.example.urvish.annotationdemo;


import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

class Child extends Parent{


    @Override
    @Status()
    String toastMessage(@NonNull String abc) {
        abc=super.toastMessage("abcd");
        return "Hello world from child\n"+abc;
    }
}