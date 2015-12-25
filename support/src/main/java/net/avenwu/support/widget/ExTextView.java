package net.avenwu.support.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import net.avenwu.support.R;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

/**
 * Created by chaobin on 11/18/15.
 */
@TargetApi(11)
public class ExTextView extends TextView implements View.OnClickListener, ValueAnimator.AnimatorUpdateListener {

    static final String TAG = ExTextView.class.getCanonicalName();
    static final String HTML_IMG = "&nbsp;。。。&nbsp;<img src='icon'/>";
    int mMaxHeight;
    int mCollapsedHeight;
    int mMaxLine;
    boolean isCollapsed;
    boolean isLayout = false;

    ValueAnimator mExpandAnimator;
    CharSequence mCollapsedText;
    CharSequence mFullText;

    Drawable mIndicator;

    public ExTextView(Context context) {
        this(context, null);
    }

    public ExTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExTextView);
        isCollapsed = a.getBoolean(R.styleable.ExTextView_expand_collapse_default, true);
        mIndicator = a.getDrawable(R.styleable.ExTextView_expand_indicator);
        if (mIndicator != null) {
            mIndicator.setBounds(0, 0, mIndicator.getIntrinsicWidth(), mIndicator.getIntrinsicHeight
                ());
        }
        a.recycle();
        reflectMaxLines();
        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxHeight <= 0 || mCollapsedHeight <= 0) {
            setMaxLines(Integer.MAX_VALUE);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            mMaxHeight = getMeasuredHeight();

            setMaxLines(mMaxLine);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            mCollapsedHeight = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!isLayout && isExpandable()) {
            if (isCollapsed) {
                if (mCollapsedText == null) {
                    int end = getLayout().getLineVisibleEnd(mMaxLine - 1);
                    CharSequence subString = mFullText.subSequence(0, end - 9);
                    Log.d(TAG, "substring=" + subString + HTML_IMG);
                    mCollapsedText = Html.fromHtml(subString + HTML_IMG, mImageGetter, null);
                }
                super.setText(mCollapsedText, reflectCurrentBufferType());
            } else {
                super.setText(mFullText, reflectCurrentBufferType());
            }
            isLayout = true;
            requestLayout();
            return;
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    private void reflectMaxLines() {
        try {
            Field maximumField = TextView.class.getDeclaredField("mMaximum");
            maximumField.setAccessible(true);
            mMaxLine = maximumField.getInt(this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private BufferType reflectCurrentBufferType() {
        try {
            Field field = TextView.class.getDeclaredField("mBufferType");
            field.setAccessible(true);
            return (BufferType) field.get(this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return BufferType.NORMAL;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        mFullText = text;
        mCollapsedText = null;
        isLayout = false;

        super.setText(text, type);
    }

    @Override
    public void onClick(View v) {
        if (!isExpandable()) {
            return;
        }
        if (mExpandAnimator != null) {
            mExpandAnimator.end();
        }
        isLayout = false;
        int from = getHeight();
        int to;
        if (from < mMaxHeight) {
            to = mMaxHeight;
            isCollapsed = false;
            setMaxLines(Integer.MAX_VALUE);
        } else {
            to = mCollapsedHeight;
            isCollapsed = true;
            setMaxLines(mMaxLine);
        }
        if (mExpandAnimator == null) {
            mExpandAnimator = ValueAnimator.ofInt(from, to);
            mExpandAnimator.setInterpolator(new DecelerateInterpolator());
            mExpandAnimator.addUpdateListener(this);
        } else {
            mExpandAnimator.setIntValues(from, to);
        }
        mExpandAnimator.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        getLayoutParams().height = (Integer) animation.getAnimatedValue();
        requestLayout();
    }

    private boolean isExpandable() {
        return mCollapsedHeight != mMaxHeight;
    }

    Html.ImageGetter mImageGetter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            Log.d(TAG, "source=" + source);
            return "icon".equals(source) ? mIndicator : null;
        }
    };

}
