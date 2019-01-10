package com.example.urvish.lazyinjection;

import android.app.Application;
import android.content.Context;

import dagger.BindsInstance;
import dagger.Component;

/**
 * @BindingInstance - will Bind the Object which value is known by CompileTime.
 */
@Component(modules = CountModule.class)
public interface CounterComponant {
    LazyCounter getLazyCounter();
    ProviderCounter getProviderCounter();
    Context getAppContext();
    /*void inject(Application application);*/
    @Component.Builder
    interface Builder{
        @BindsInstance Builder application(Application application);
        CounterComponant build();
    }
}
