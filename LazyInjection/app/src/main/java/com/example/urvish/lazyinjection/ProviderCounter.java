package com.example.urvish.lazyinjection;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * provider will create every new object every time get() method is called.
 * will create new Instance every time it'll be called
 */
public class ProviderCounter {
    @Inject
    Provider<Integer> integerProvider;
    @Inject
    public ProviderCounter(){

    }
    public Integer  print(){
        System.out.println("printing...");
        return integerProvider.get();
    }
}
