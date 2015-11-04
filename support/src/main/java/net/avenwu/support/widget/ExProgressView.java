package net.avenwu.support.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

/**
 * Created by aven on 10/20/15.
 */
@TargetApi(14)
public class ExProgressView extends View {
    Paint mPaint;
    Rect mRect;
    int mRectWidth;
    int mRectHeight;
    int mGap;
    int[] mIndexArray = {-2, -1, 0, 1, 2};
    int mCurrentIndex = 4;

    public ExProgressView(Context context) {
        this(context, null);
    }

    public ExProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
        mRect = new Rect();
        mRectWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
                getContext().getResources().getDisplayMetrics());
        mRectHeight = 4 * mRectWidth;
        mGap = (int) (mRectWidth * 0.8f);
        ObjectAnimator animator = ObjectAnimator.ofInt(this, mIndex, 0, 1, 2, 3, 4, 5);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(750);
        animator.setRepeatMode(Animation.RESTART);
        animator.setRepeatCount(Animation.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        animator.start();
    }

    public int getmCurrentIndex() {
        return mCurrentIndex;
    }

    public void setmCurrentIndex(int mCurrentIndex) {
        this.mCurrentIndex = mCurrentIndex;
    }

    Property<ExProgressView, Integer> mIndex = new Property<ExProgressView, Integer>(Integer.class, "mCurrentIndex") {
        @Override
        public Integer get(ExProgressView object) {
            return object.getmCurrentIndex();
        }

        @Override
        public void set(ExProgressView object, Integer value) {
            object.setmCurrentIndex(value);
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRect.width() > 0 && mRect.height() > 0) {
            for (int index : mIndexArray) {
                float[] t = getRectByIndex(mRect, index);
                if (index == mIndexArray[mCurrentIndex]) {
                    t[1] = mRect.top;
                    t[3] = mRect.bottom;
                    Log.d("Index", "value=" + index);
                }
                canvas.drawRect(t[0], t[1], t[2], t[3], mPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Rect rect = mRect;
        final int centerX = getMeasuredWidth() / 2;
        final int centerY = getMeasuredHeight() / 2;
        rect.left = centerX - mRectWidth / 2;
        rect.right = centerX + mRectWidth / 2;
        rect.top = centerY - mRectHeight / 2;
        rect.bottom = centerY + mRectHeight / 2;
    }

    private float[] getRectByIndex(Rect rect, int index) {
        final float diffX = index * (mRectWidth + mGap);
        final float diffY = index * (mRectHeight * 0.15f);
        return new float[]{
                rect.left + diffX,
                rect.top + Math.abs(diffY),
                rect.right + diffX,
                rect.bottom - Math.abs(diffY)
        };
    }
}
