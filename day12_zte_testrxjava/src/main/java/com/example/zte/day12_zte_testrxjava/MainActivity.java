package com.example.zte.day12_zte_testrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "111";
    private String[] strs = {"a,a","abb","acc","dd","aee"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                Log.e(TAG, "onNext: "+value );
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        ObservableOnSubscribe<String> subscribe = new ObservableOnSubscribe<String>(){

            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("hezijie");
            }
        };
        Observable<String> observable = Observable.create(subscribe);
        observable.subscribe(observer);

    }

    public void filter(View view) {
        Observable.fromArray(strs).filter(new AppendOnlyLinkedArrayList.NonThrowingPredicate<String>() {
            @Override
            public boolean test(String s) {
                return "bb".equals(s);
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                Log.e(TAG, "onNext: "+value );
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void mapAndFlatMap(View view) {
        Observable.fromArray(strs)
                .filter(new AppendOnlyLinkedArrayList.NonThrowingPredicate<String>() {
                    @Override
                    public boolean test(String s) {
                        Log.e(TAG, "test: "+Thread.currentThread().getName());
                        return s.contains("a");
                    }
                })
                .subscribeOn(Schedulers.io())

                //对数据进行二次处理。
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Log.e(TAG, "apply: "+Thread.currentThread().getName());
                        return s+",hezijie";
                    }
                })
                .observeOn(Schedulers.newThread())
                //将集合中的集合取出来。
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        Log.e(TAG, "apply: "+Thread.currentThread().getName());
                        String[] items = s.split(",");
                        return Observable.fromArray(items);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        Log.e(TAG, "onNext: "+Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
