package net.avenwu.support.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import net.avenwu.support.R;

import java.lang.reflect.Field;

/**
 * Created by chaobin on 11/18/15.
 */
@TargetApi(11)
public class ExTextView extends TextView implements View.OnClickListener, ValueAnimator.AnimatorUpdateListener {

    static final String TAG = ExTextView.class.getCanonicalName();
    static final int END = 0;
    static final int RIGHT = 1;

    int mMaxHeight;
    int mCollapsedHeight;
    int mMaxLine;
    boolean isCollapsed;
    boolean isLayout = false;
    boolean isMeasured = false;

    ValueAnimator mExpandAnimator;
    CharSequence mCollapsedText;
    CharSequence mFullText;

    Drawable mIndicator;
    OnClickListener mOuterListener;
    Style mStyle;

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
        int style = a.getInt(R.styleable.ExTextView_expand_style, 0);
        switch (style) {
            case END:
                mStyle = new EndStyle();
                break;
            case RIGHT:
                mStyle = new RightStyle();
                break;
        }
        a.recycle();
        reflectMaxLines();
        super.setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        if (!TextUtils.isEmpty(mFullText) && !isMeasured) {
            Log.d(TAG, "onMeasure isCollapsed=" + isCollapsed);
            if (isCollapsed) {
                setMaxLines(Integer.MAX_VALUE);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                mMaxHeight = getMeasuredHeight();

                setMaxLines(mMaxLine);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                mCollapsedHeight = getMeasuredHeight();
            } else {
                setMaxLines(mMaxLine);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                mCollapsedHeight = getMeasuredHeight();

                setMaxLines(Integer.MAX_VALUE);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                mMaxHeight = getMeasuredHeight();
            }
            isMeasured = true;
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!TextUtils.isEmpty(mFullText) && (!isLayout && isExpandable())) {
            Log.d(TAG, "onLayout reset text");
            mStyle.onLayout(isCollapsed, this, mIndicator);
            if (isCollapsed) {
                if (mCollapsedText == null) {
                    mCollapsedText = mStyle.collapsedText(this, mIndicator, mFullText, mMaxLine);
                }
                super.setText(mCollapsedText, reflectCurrentBufferType());
            } else {
                super.setText(mFullText, reflectCurrentBufferType());
            }
            isLayout = true;
        } else {
            super.onLayout(changed, left, top, right, bottom);
        }
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
        //view 复用的时候会重复绑定数据
        if (TextUtils.isEmpty(text)) {
            super.setText(text, type);
        } else if (text.equals(mFullText)) {
            super.setText(isCollapsed ? mCollapsedText : mFullText, reflectCurrentBufferType());
        } else {
            mFullText = text;
            mCollapsedText = null;
            isLayout = false;
            isMeasured = false;
            super.setText(mFullText, type);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOuterListener != null) {
            mOuterListener.onClick(v);
        }
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


    @Override
    public void setOnClickListener(OnClickListener listener) {
        mOuterListener = listener;
    }

    public interface Style {

        CharSequence collapsedText(TextView textView, Drawable drawable, CharSequence text, int
            maxLine);

        void onLayout(boolean collapsed, TextView textView, Drawable drawable);
    }

    private static class EndStyle implements Style {
        static final String HTML_IMG = "...<img src='icon'/>";
        static final String HTML_NEW_LINE = "<br>";

        @Override
        public CharSequence collapsedText(TextView view, final Drawable drawable, CharSequence text,
                                          int maxLine) {
            StringBuilder stringBuilder = new StringBuilder();
            int start = 0;
            // 由于中英文字符等排版问题断行具有不确定性，此处强行对缩略文本断行
            for (int i = 0; i < maxLine; i++) {
                int end = view.getLayout().getLineVisibleEnd(i);
                String append;
                if (i == maxLine - 1) {
                    end -= 3;
                    append = HTML_IMG;
                } else {
                    append = HTML_NEW_LINE;
                }
                stringBuilder.append(text.subSequence(start, end)).append(append);
                start = end;
            }
            String subString = stringBuilder.toString();
            return Html.fromHtml(subString, new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                    return "icon".equals(source) ? drawable : null;
                }
            }, null);
        }

        @Override
        public void onLayout(boolean collapsed, TextView textView, Drawable drawable) {
        }
    }

    private static class RightStyle implements Style {
        @Override
        public CharSequence collapsedText(TextView view, Drawable drawable, CharSequence text,
                                          int maxLine) {
            int end = view.getLayout().getLineVisibleEnd(maxLine - 1);
            return text.subSequence(0, end - 3) + "...";
        }

        @Override
        public void onLayout(boolean collapsed, TextView textView, Drawable drawable) {
            Drawable[] d = textView.getCompoundDrawables();
            if (collapsed) {
                d[2] = drawable;
            } else {
                d[2] = null;
            }
            textView.setCompoundDrawables(d[0], d[1], d[2], d[3]);
        }
    }
}
