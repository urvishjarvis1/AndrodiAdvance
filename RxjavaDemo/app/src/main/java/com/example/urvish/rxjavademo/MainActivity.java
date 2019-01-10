package com.example.urvish.rxjavademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    private static final String TAG=MainActivity.class.getSimpleName()+"this";
    private Observable<Integer> observable=Observable.just(1,2,3,4,5,6,7,8,9,10);
    private Observable<Integer> observableInt=observable.filter(new Predicate<Integer>() {
        @Override
        public boolean test(Integer integer) throws Exception {
            return integer%2 != 0;
        }
    });
    private Observable<String> observableString=observable.map(new Function<Integer, String>() {
        @Override
        public String apply(Integer integer) throws Exception {
            return integer.toString()+"String";
        }
    }).skip(3);
    private Observable<Integer> observableZip=observable.zipWith(observableInt, new BiFunction<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer integer, Integer s) throws Exception {
            return integer+Integer.valueOf(s);
        }
    }).take(4);
    private Observable<List<String>> observableBuffer=observableString.buffer(3,1);
    private Observable<Integer> observableConcate=observable.concat(observableInt,observable);
    private Observer<Integer> observerInt;
    private Observer<String> observerString;
    private Observer<Integer> observerZip;
    private Observer<List<String>> observerBuffer;
    private Observer<Integer> observerConcate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //just and filter opn
        observerInt=new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: Subsribed");
            }

            @Override
            public void onNext(Integer integer) {
                ((TextView)findViewById(R.id.textres)).append("\n"+integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: error occured in observer",e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: all items are emitted");
            }
        };
        //map operation
        observerString=new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                ((TextView)findViewById(R.id.textmap)).append("\n"+s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        //zip
        observerZip=new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                ((TextView)findViewById(R.id.textzip)).append("\n"+integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ",e );
            }

            @Override
            public void onComplete() {

            }
        };
        //Buffer
        observerBuffer=new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<String> strings) {
                for(String s:strings)
                    ((TextView)findViewById(R.id.textBuffer)).append("\n"+s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        //Concat
        observerConcate=new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                ((TextView )findViewById(R.id.textConcate)).append("\n"+integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        observableInt.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(observerInt);
        observableString.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observerString);
        observableZip.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(observerZip);
        observableBuffer.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(observerBuffer);
        observableConcate.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(observerConcate);
    }
}
