package net.avenwu.support.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by chaobin on 3/3/15.
 */
public class BreathingLayout extends FrameLayout {
    BreathingDelegate mDelegate;

    public BreathingLayout(Context context) {
        this(context, null);
    }

    public BreathingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BreathingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDelegate = new BreathingDelegate(context, attrs, this);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mDelegate.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mDelegate.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mDelegate.onDetachedFromWindow();
    }
}
