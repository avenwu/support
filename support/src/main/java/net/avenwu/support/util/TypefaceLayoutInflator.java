package net.avenwu.support.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * modified from CalligraphyLayoutInflater(Created by chris on 19/12/2013)
 * <p/>
 * Created by chaobin on 10/8/15.
 */
public class TypefaceLayoutInflator extends LayoutInflater {
    public TypefaceLayoutInflator(Context context) {
        super(context);
    }

    public TypefaceLayoutInflator(LayoutInflater original, Context newContext) {
        super(original, newContext);
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new TypefaceLayoutInflator(this, newContext);
    }

    @Override
    protected View onCreateView(View parent, String name, AttributeSet attrs) throws ClassNotFoundException {
        View view = super.onCreateView(parent, name, attrs);
        onViewCreatedInternal(view, getContext(), attrs);
        return view;
    }

    @Override
    protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
        View view = super.onCreateView(name, attrs);
        onViewCreatedInternal(view, getContext(), attrs);
        return view;
    }

    @Override
    public void setFactory2(Factory2 factory) {
        // Only set our factory and wrap calls to the Factory2 trying to be set!
        if (!(factory instanceof WrapperFactory2)) {
//            LayoutInflaterCompat.setFactory(this, new WrapperFactory2(factory2, mCalligraphyFactory));
            super.setFactory2(new WrapperFactory2(factory));
        } else {
            super.setFactory2(factory);
        }
    }

    @Override
    public void setFactory(LayoutInflater.Factory factory) {
        // Only set our factory and wrap calls to the Factory trying to be set!
        if (!(factory instanceof WrapperFactory)) {
            super.setFactory(new WrapperFactory(factory, this));
        } else {
            super.setFactory(factory);
        }
    }

    @Override
    public View inflate(XmlPullParser parser, ViewGroup root, boolean attachToRoot) {
        setPrivateFactoryInternal();
        return super.inflate(parser, root, attachToRoot);
    }

    private boolean mSetPrivateFactory = false;

    private void setPrivateFactoryInternal() {
        // Already tried to set the factory.
        if (mSetPrivateFactory) return;
        // Reflection (Or Old Device) skip.
//        if (!CalligraphyConfig.get().isReflection()) return;
        // Skip if not attached to an activity.
        if (!(getContext() instanceof Factory2)) {
            mSetPrivateFactory = true;
            return;
        }
        final Method setPrivateFactoryMethod = ReflectionUtils
            .getMethod(LayoutInflater.class, "setPrivateFactory");

        if (setPrivateFactoryMethod != null) {
            ReflectionUtils.invokeMethod(this,
                setPrivateFactoryMethod,
                new PrivateWrapperFactory2((Factory2) getContext(), this));
        }
        mSetPrivateFactory = true;
    }

    private static class WrapperFactory implements LayoutInflater.Factory {
        private final Factory mFactory;
        private final TypefaceLayoutInflator mInflater;

        public WrapperFactory(Factory factory, TypefaceLayoutInflator inflator) {
            mFactory = factory;
            mInflater = inflator;
        }

        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                try {
                    View view = mInflater.createCustomViewInternal(
                        null, mFactory.onCreateView(name, context, attrs), name, context, attrs);
                    onViewCreatedInternal(view, context, attrs);
                    return view;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            View view = mFactory.onCreateView(name, context, attrs);
            onViewCreatedInternal(view, context, attrs);
            return view;
        }
    }

    static void onViewCreatedInternal(View view, final Context context, AttributeSet attrs) {
        if (view instanceof TextView) {
            TypefaceUtils.setTypeface(context, (TextView) view, "fonts/RobotoCondensed-Regular.ttf");
        }
    }

    /**
     * Nasty method to inflate custom layouts that haven't been handled else where. If this fails it
     * will fall back through to the PhoneLayoutInflater method of inflating custom views where
     * Calligraphy will NOT have a hook into.
     *
     * @param parent      parent view
     * @param view        view if it has been inflated by this point, if this is not null this method
     *                    just returns this value.
     * @param name        name of the thing to inflate.
     * @param viewContext Context to inflate by if parent is null
     * @param attrs       Attr for this view which we can steal fontPath from too.
     * @return view or the View we inflate in here.
     */
    private View createCustomViewInternal(View parent, View view, String name, Context
        viewContext, AttributeSet attrs) throws Exception {
        // I by no means advise anyone to do this normally, but Google have locked down access to
        // the createView() method, so we never get a callback with attributes at the end of the
        // createViewFromTag chain (which would solve all this unnecessary rubbish).
        // We at the very least try to optimise this as much as possible.
        // We only call for customViews (As they are the ones that never go through onCreateView(...)).
        // We also maintain the Field reference and make it accessible which will make a pretty
        // significant difference to performance on Android 4.0+.

        // If CustomViewCreation is off skip this.
        if (view == null && name.indexOf('.') > -1) {
            if (mConstructorArgs == null)
                mConstructorArgs = ReflectionUtils.getField(LayoutInflater.class, "mConstructorArgs");

            final Object[] mConstructorArgsArr = (Object[]) ReflectionUtils.getValue(mConstructorArgs, this);
            final Object lastContext = mConstructorArgsArr[0];
            // The LayoutInflater actually finds out the correct context to use. We just need to set
            // it on the mConstructor for the internal method.
            // Set the constructor ars up for the createView, not sure why we can't pass these in.
            mConstructorArgsArr[0] = viewContext;
            ReflectionUtils.setValue(mConstructorArgs, this, mConstructorArgsArr);
            try {
                view = createView(name, null, attrs);
            } catch (ClassNotFoundException ignored) {
            } finally {
                mConstructorArgsArr[0] = lastContext;
                ReflectionUtils.setValue(mConstructorArgs, this, mConstructorArgsArr);
            }
        }
        return view;
    }

    private Field mConstructorArgs = null;

    @TargetApi(11)
    private static class WrapperFactory2 implements Factory2 {
        protected final Factory2 mFactory2;

        public WrapperFactory2(Factory2 factory2) {
            mFactory2 = factory2;
        }

        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            View view = mFactory2.onCreateView(name, context, attrs);
            onViewCreatedInternal(view, context, attrs);
            return view;
        }

        @Override
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
            View view = mFactory2.onCreateView(parent, name, context, attrs);
            onViewCreatedInternal(view, context, attrs);
            return view;
        }
    }

    @TargetApi(11)
    private static class PrivateWrapperFactory2 extends WrapperFactory2 {

        private final TypefaceLayoutInflator mInflater;

        public PrivateWrapperFactory2(Factory2 factory2, TypefaceLayoutInflator inflater) {
            super(factory2);
            mInflater = inflater;
        }

        @Override
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
            try {
                View view = mInflater.createCustomViewInternal(parent,
                    mFactory2.onCreateView(parent, name, context, attrs),
                    name, context, attrs
                );
                onViewCreatedInternal(view, context, attrs);
                return view;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return super.onCreateView(parent, name, context, attrs);
        }
    }
}
