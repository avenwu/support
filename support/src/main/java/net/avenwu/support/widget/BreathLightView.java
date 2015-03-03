package net.avenwu.support.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by chaobin on 3/3/15.
 */
public class BreathLightView extends FrameLayout {
    public BreathLightView(Context context) {
        this(context, null);
    }

    public BreathLightView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BreathLightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }
}
