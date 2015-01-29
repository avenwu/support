package com.avenwu.deepinandroid.eventbus;

import java.lang.reflect.Method;

/**
 * Created by chaobin on 1/29/15.
 */
public class Subscriber {

    Object mSubscriber;

    Method mMethod;

    Class<?> mEventType;

    public Subscriber(Object subscriber, Method method) {
        mSubscriber = subscriber;
        mMethod = method;
        mEventType = method.getParameterTypes()[0];
    }
}
