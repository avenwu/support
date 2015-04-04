package net.avenwu.support.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by chaobin on 3/3/15.
 */
public class BreathLightView extends FrameLayout {
    private Paint mPaint;
    private RectF mRippleRect = new RectF();
    private float mRippleRadius = 200;
    private float mInitRadius = 50;
    private int mDuration = 3000;
    private RectF mBorderRect = new RectF();
    private Scroller mScroller;
    private CountDownTimer mTimer;
    private boolean isZoomUp = true;

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
        setDrawingCacheEnabled(false);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xFF0099CC);
        mPaint.setStyle(Paint.Style.FILL);
        mScroller = new Scroller(getContext(), new LinearInterpolator());
        mTimer = new CountDownTimer(100, 1000 / 30) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("BreathLightView", "currentX=" + mScroller.getCurrX());
                final float dx = mBorderRect.centerX() - mScroller.getCurrX();
                final float dy = mBorderRect.centerY() - mScroller.getCurrY();
                mRippleRect.set(mScroller.getCurrX(), mScroller.getCurrY(), mBorderRect.centerX() + dx, mBorderRect.centerY() + dy);
                int alpha = ((int) (dx / mRippleRadius * 0xff));
                Log.d("BreathLightView", "alpha=" + alpha);
                int a = 255 - alpha;
//                mPaint.setAlpha(a);
                Log.d("BreathLightView", "rect:=" + mRippleRect.toString());
                invalidate();
            }

            @Override
            public void onFinish() {
                if (!mScroller.computeScrollOffset()) {
                    isZoomUp = !isZoomUp;
                    final int radius = (int) (isZoomUp ? -mRippleRadius : mRippleRadius);
                    mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), radius, radius, mDuration);
                }
                mTimer.start();

            }
        };
        mTimer.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawOval(mRippleRect, mPaint);
        Log.d("BreathLightView", "onDraw");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mBorderRect.set(left, top, right, bottom);
        final float centerX = mBorderRect.centerX();
        final float centerY = mBorderRect.centerY();
        final float l = centerX - mInitRadius;
        final float t = centerY - mInitRadius;
        mRippleRect.set(l, t, centerX + mInitRadius, centerY + mInitRadius);
        int radius = (int) (isZoomUp ? (-mRippleRadius) : (mRippleRadius));
        mScroller.startScroll((int) l, (int) t, radius, radius, mDuration);
        Log.d("BreathLightView", "onLayout:" + mRippleRect.toString());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTimer != null) {
            mTimer.cancel();
        }
        Log.d("BreathLightView", "onDetachedFromWindow");
    }
}
