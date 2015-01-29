package com.avenwu.deepinandroid.eventbus;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by chaobin on 1/29/15.
 */
public interface Finder {

    List<Method> findSubscriber(Class<?> subscriber);
}
