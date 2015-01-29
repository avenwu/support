package com.avenwu.deepinandroid.eventbus;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by chaobin on 1/29/15.
 */
public class PostHandler extends Handler {

    final Bus mBus;

    public PostHandler(Looper looper, Bus bus) {
        super(looper);
        mBus = bus;
    }

    @Override
    public void handleMessage(Message msg) {
        CopyOnWriteArrayList<Subscriber> subscribers = mBus.mSubscriberMap.get(msg.obj.getClass());
        for (Subscriber subscriber : subscribers) {
            subscriber.mMethod.setAccessible(true);
            try {
                subscriber.mMethod.invoke(subscriber.mSubscriber, msg.obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void enqueue(Object event) {
        Message message = obtainMessage();
        message.obj = event;
        sendMessage(message);
    }
}
