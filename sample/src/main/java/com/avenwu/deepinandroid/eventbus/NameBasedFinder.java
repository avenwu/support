package com.avenwu.deepinandroid.eventbus;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaobin on 1/29/15.
 */
public class NameBasedFinder implements Finder {

    @Override
    public List<Method> findSubscriber(Class<?> subscriber) {
        List<Method> methods = new ArrayList<>();
        for (Method method : subscriber.getDeclaredMethods()) {
            if (method.getName().startsWith("onEvent") && method.getParameterTypes().length == 1) {
                methods.add(method);
                Log.d("findSubscriber", "add method:" + method.getName());
            }
        }
        return methods;
    }
}
