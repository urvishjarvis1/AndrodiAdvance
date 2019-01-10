package com.example.urvish.lazyinjection;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView mTextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextview=findViewById(R.id.textview);
        CounterComponant counterComponant=DaggerCounterComponant.builder().application(this.getApplication()).build();
        LazyCounter lazyCounter=counterComponant.getLazyCounter();
        Context context=counterComponant.getAppContext();
        ProviderCounter providerCounter=counterComponant.getProviderCounter();
        mTextview.append("Lazy1:"+lazyCounter.print()+"\tProviderCounter"+providerCounter.print()+"\n");
        mTextview.append("Lazy1:"+lazyCounter.print()+"\tProviderCounter"+providerCounter.print()+"\n");
        mTextview.append("Lazy1:"+lazyCounter.print()+"\tProviderCounter"+providerCounter.print()+"\n");
        LazyCounter lazyCounter1=counterComponant.getLazyCounter();
        mTextview.append("Lazy2:"+lazyCounter1.print()+"\n");
        mTextview.append("Lazy2:"+lazyCounter1.print()+"\n");
        mTextview.append("Lazy2:"+lazyCounter1.print()+"\n");
        Toast.makeText(context, "Toast using DI", Toast.LENGTH_LONG).show();

    }
}
