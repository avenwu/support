package net.avenwu.support.presenter;

import net.avenwu.support.protocol.RenderAction;
import net.avenwu.support.protocol.UIAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by chaobin on 7/12/15.
 */
public class Presenter {
    List<DetachableCallback<?>> mCallback;
    Map<String, RenderAction<?>> mActions;
    boolean mAttached = false;

    public void attach() {
        mAttached = true;
    }

    public <T> Callback<T> addCallback(DetachableCallback<T> callback) {
        if (mCallback == null) {
            mCallback = new ArrayList<DetachableCallback<?>>();
        }
        mCallback.add(callback);
        return callback;
    }

    public Presenter addAction(String key, RenderAction<?> action) {
        if (mActions == null) {
            mActions = new HashMap<String, RenderAction<?>>();
        }
        mActions.put(key, action);
        return this;
    }

    public void detach() {
        mAttached = false;
        if (mCallback != null) {
            for (DetachableCallback<?> callback : mCallback) {
                callback.detach();
            }
        }
        if (mActions != null) {
            Iterator<Map.Entry<String, RenderAction<?>>> iterator = mActions.entrySet().iterator();
            while (iterator.hasNext()) {
                // avoid memory leak
                if (iterator.next().getValue() instanceof UIAction) {
                    iterator.remove();
                }
            }
        }
    }

    public void invoke(String key, Object data) {
        if (mActions != null && mActions.size() > 0) {
            RenderAction action = mActions.get(key);
            if (action != null) {
                action.onUpdate(data);
                if (mAttached && action instanceof UIAction) {
                    ((UIAction) action).onUpdateUI(data);
                }
            }
        }
    }

    protected static class DetachableCallback<T> implements Callback<T> {
        Callback<T> callback;

        public DetachableCallback(Callback<T> callback) {
            this.callback = callback;
        }

        @Override
        public void success(T t, Response response) {
            if (callback != null) {
                callback.success(t, response);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            if (callback != null) {
                callback.failure(error);
            }
        }

        public void detach() {
            callback = null;
        }
    }
}
