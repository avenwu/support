package net.avenwu.support.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;

/**
 * Created by chaobin on 10/8/15.
 */
public class TypefaceContextWrapper extends ContextWrapper {
    private TypefaceLayoutInflator mInflater;

    public TypefaceContextWrapper(Context base) {
        super(base);
    }

    public static ContextWrapper wrap(Context base) {
        return new TypefaceContextWrapper(base);
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                mInflater = new TypefaceLayoutInflator(LayoutInflater.from(getBaseContext()), this);
            }
            return mInflater;
        }
        return super.getSystemService(name);
    }
}
