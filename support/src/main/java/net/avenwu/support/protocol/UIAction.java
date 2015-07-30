package net.avenwu.support.protocol;

/**
 * Created by chaobin on 7/14/15.
 */
public abstract class UIAction<T> implements RenderAction<T> {
    @Override
    public void onUpdate(T data) {
    }

    public abstract void onUpdateUI(T data);

}
