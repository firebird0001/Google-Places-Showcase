package com.google.places.showcase.utils;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Bus that handles all tasks in main thread
 */
public class MainThreadBus extends Bus {
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MainThreadBus.super.post(event);
                }
            });
        }
    }

    @Override
    public void register(final Object subscriber) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.register(subscriber);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MainThreadBus.super.register(subscriber);
                }
            });
        }
    }

    @Override
    public void unregister(final Object subscriber) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.unregister(subscriber);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MainThreadBus.super.unregister(subscriber);
                }
            });
        }
    }
}