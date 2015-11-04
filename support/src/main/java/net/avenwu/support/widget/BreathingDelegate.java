package net.avenwu.support.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import net.avenwu.support.R;

/**
 * Created by chaobin on 4/6/15.
 */
@SuppressWarnings("NewApi")
public class BreathingDelegate {
    private Paint mPaint;
    private RectF mRippleRect = new RectF();
    private float mRippleRadius;
    private float mEndRadius;
    private int mRippleAlpha = 0xff;
    private int mDuration;
    private RectF mBorderRect = new RectF();
    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private boolean mAutoStart;
    private int mRippleColor;
    private View mTarget;

    public BreathingDelegate(Context context, AttributeSet attrs, View view) {
        mTarget = view;
        mTarget.setWillNotDraw(false);
        mTarget.setDrawingCacheEnabled(false);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BreathingLayout);
        mAutoStart = a.getBoolean(R.styleable.BreathingLayout__autoStart, true);
        mRippleColor = a.getColor(R.styleable.BreathingLayout__rippleColor, 0xFF0099CC);
        mRippleRadius = a.getDimensionPixelSize(R.styleable.BreathingLayout__rippleStartRadius, -1);
        if (mRippleRadius == -1) {
            mRippleRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getContext().getResources().getDisplayMetrics());
        }
        mEndRadius = a.getDimensionPixelSize(R.styleable.BreathingLayout__rippleEndRadius, -1);
        if (mEndRadius == -1) {
            mEndRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getContext().getResources().getDisplayMetrics());
        }
        if (mEndRadius <= mRippleRadius) {
            throw new IllegalStateException("rippleEndRadius can not be smaller than rippleStartRadius");
        }
        mDuration = a.getInt(R.styleable.BreathingLayout__rippleTime, 3000);
        a.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mRippleColor);
        mPaint.setStyle(Paint.Style.FILL);
        prepareAnimation();
        if (mAutoStart) {
            start();
        }
    }

    Property<BreathingDelegate, Float> mRadiusProperty = new Property<BreathingDelegate, Float>(Float.class, "mRippleRadius") {
        @Override
        public Float get(BreathingDelegate object) {
            return object.getRadius();
        }

        @Override
        public void set(BreathingDelegate object, Float value) {
            object.setRadius(value);
        }
    };
    Property<BreathingDelegate, Integer> mAlphaProperty = new Property<BreathingDelegate, Integer>(Integer.class, "mRippleAlpha") {
        @Override
        public Integer get(BreathingDelegate object) {
            return object.getRippleAlpha();
        }

        @Override
        public void set(BreathingDelegate object, Integer value) {
            object.setRippleAlpha(value);
        }
    };

    private float getRadius() {
        return mRippleRadius;
    }

    private void setRadius(float radius) {
        this.mRippleRadius = radius;
    }

    public int getRippleAlpha() {
        return mRippleAlpha;
    }

    public void setRippleAlpha(int rippleAlpha) {
        this.mRippleAlpha = rippleAlpha;
    }

    private void prepareAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, mRadiusProperty, mRippleRadius, mEndRadius);
        animator.setDuration(mDuration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTarget.invalidate();
            }
        });

        ObjectAnimator alpha = ObjectAnimator.ofInt(this, mAlphaProperty, mRippleAlpha, 0x00);
        alpha.setDuration(mDuration);
        alpha.setRepeatCount(ValueAnimator.INFINITE);
        alpha.setRepeatMode(ValueAnimator.RESTART);
        mAnimatorSet.setInterpolator(new LinearInterpolator());
        mAnimatorSet.playTogether(animator, alpha);
    }

    public void toggle() {
        if (mAnimatorSet.isRunning()) {
            stop();
        }/* else if (mAnimatorSet.isPaused()) {
            mAnimatorSet.resume();
        } */ else {
            mAnimatorSet.start();
        }
    }

    public void start() {
        mAnimatorSet.start();
    }

    public void stop() {
        mAnimatorSet.cancel();
    }

    private Context getContext() {
        return mTarget.getContext();
    }

    public void onDraw(Canvas canvas) {
        mRippleRect.set(mBorderRect.centerX() - mRippleRadius, mBorderRect.centerY() - mRippleRadius,
                mBorderRect.centerX() + mRippleRadius, mBorderRect.centerY() + mRippleRadius);
        canvas.drawOval(mRippleRect, mPaint);
        Log.d("BreathingLayout", "onDraw=" + mRippleRect.toString());
    }

    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mBorderRect.set(left, top, right, bottom);
        Log.d("BreathingLayout", "onLayout:" + mBorderRect.toString());
    }

    public void onDetachedFromWindow() {
        mAnimatorSet.cancel();
        Log.d("BreathingLayout", "onDetachedFromWindow");
    }
}
