package com.zfx;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.Executors;

public class rxjava {
    static Scheduler scheduler = Schedulers.from(Executors.newSingleThreadExecutor());
    public int res = 0;

    public static void main(String[] args) {
        rxjava rxjava = new rxjava();


        //
        for (int i = 0; i < 100; i++) {
            int finalI = i;
//            new Thread(() -> {
            Observable<String> observable = exec(String.valueOf(finalI), rxjava)
                    // 决定 observable 具体的 发送行为
//                    .subscribeOn(scheduler)
                    // 决定 observer 具体的 onNext 和 onCompleted
                    .observeOn(scheduler);
            observable.subscribe(new Observer<String>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull String s) {
                        System.out.println(Thread.currentThread().getName() + ":发送后的" + s);
                }

                @Override
                public void onError(@NonNull Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });

//            });

        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(rxjava.res);
    }

    private static Observable<String> exec(String s, rxjava rxjava) {
        //订阅
        return Observable.create(emitter -> {
            //生产消息
            rxjava.res = rxjava.res + Integer.valueOf(s);
            System.out.println(Thread.currentThread().getName() + ":发送前" + s);
            emitter.onNext(s);
            emitter.onComplete();
        });
    }


}
