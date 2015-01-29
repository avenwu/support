package com.avenwu.deepinandroid.eventbus;

import android.os.Looper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by chaobin on 1/29/15.
 */
public class Bus {

    static volatile Bus sInstance;

    Finder mFinder;

    Map<Class<?>, CopyOnWriteArrayList<Subscriber>> mSubscriberMap;

    PostHandler mPostHandler;

    private Bus() {
        mFinder = new NameBasedFinder();
        mSubscriberMap = new HashMap<>();
        mPostHandler = new PostHandler(Looper.getMainLooper(), this);
    }

    public static Bus getDefault() {
        if (sInstance == null) {
            synchronized (Bus.class) {
                if (sInstance == null) {
                    sInstance = new Bus();
                }
            }
        }
        return sInstance;
    }

    public void register(Object subscriber) {
        List<Method> methods = mFinder.findSubscriber(subscriber.getClass());
        if (methods == null || methods.size() < 1) {
            return;
        }
        CopyOnWriteArrayList<Subscriber> subscribers = mSubscriberMap.get(subscriber.getClass());
        if (subscribers == null) {
            subscribers = new CopyOnWriteArrayList<>();
            mSubscriberMap.put(methods.get(0).getParameterTypes()[0], subscribers);
        }
        for (Method method : methods) {
            Subscriber newSubscriber = new Subscriber(subscriber, method);
            subscribers.add(newSubscriber);
        }
    }

    public void unregister(Object subscriber) {
        CopyOnWriteArrayList<Subscriber> subscribers = mSubscriberMap.remove(subscriber.getClass());
        if (subscribers != null) {
            for (Subscriber s : subscribers) {
                s.mMethod = null;
                s.mSubscriber = null;
            }
        }
    }

    public void post(Object event) {
        //TODO post with handler
        mPostHandler.enqueue(event);
    }
}
