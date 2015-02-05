package net.avenwu.support.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.avenwu.support.R;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chaobin on 2/4/15.
 */
public class FlatTabGroup extends RadioGroup implements RadioGroup.OnCheckedChangeListener {
    public FlatTabGroup(Context context) {
        this(context, null);
    }

    private int mRadius;
    private int mStroke;
    private int mHighlightColor;
    private String[] mItemString;
    private float mTextSize;
    private ColorStateList mTextColor;
    private int[] mTabViewIds;
    private OnTabCheckedListener mTabCheckedListener;
    private OnCheckedChangeListener mListener;

    public FlatTabGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlatTabGroup);
        mHighlightColor = array.getColor(R.styleable.FlatTabGroup_tab_border_color, Color.WHITE);
        mStroke = array.getDimensionPixelSize(R.styleable.FlatTabGroup_tab_border_width, 2);
        mRadius = array.getDimensionPixelOffset(R.styleable.FlatTabGroup_tab_radius, 5);
        mTextColor = array.getColorStateList(R.styleable.FlatTabGroup_tab_textColor);
        mTextSize = array.getDimensionPixelSize(R.styleable.FlatTabGroup_tab_textSize, 14);
        array.recycle();
        int id = array.getResourceId(R.styleable.FlatTabGroup_tab_items, 0);
        mItemString = isInEditMode() ? new String[]{"TAB A", "TAB B", "TAB C"} : context.getResources().getStringArray(id);
        generateTabView(context, attrs);
        updateChildBackground();
        super.setOnCheckedChangeListener(this);
    }

    private void generateTabView(Context context, AttributeSet attrs) {
        if (mItemString == null) {
            return;
        }
        mTabViewIds = new int[mItemString.length];
        int i = 0;
        for (String text : mItemString) {
            RadioButton button = new RadioButton(context, attrs);
            button.setGravity(Gravity.CENTER);
            button.setButtonDrawable(android.R.color.transparent);
            button.setText(text);
            button.setTextColor(mTextColor);
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            int id = generateViewId();
            button.setId(mTabViewIds[i++] = id);
            addView(button, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        }
    }

    public void setOnTabCheckedListener(OnTabCheckedListener listener) {
        mTabCheckedListener = listener;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (mListener != null) {
            mListener.onCheckedChanged(group, checkedId);
        }
        if (mTabCheckedListener != null) {
            int checkedPosition = -1;
            for (int i = 0; i < mTabViewIds.length; i++) {
                if (mTabViewIds[i] == checkedId) {
                    checkedPosition = i;
                    break;
                }
            }
            mTabCheckedListener.onChecked(this, checkedPosition);
        }
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        updateChildBackground();
    }

    private void updateChildBackground() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof RadioButton) {
                child.setBackgroundDrawable(generateTabBackground(i, mHighlightColor));
            }
        }
    }

    private Drawable generateTabBackground(int position, int color) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, generateDrawable(position, color));
        stateListDrawable.addState(new int[]{}, generateDrawable(position, Color.TRANSPARENT));
        return stateListDrawable;
    }

    private Drawable generateDrawable(int position, int color) {
        float[] radius;
        if (position == 0) {
            radius = new float[]{
                    mRadius, mRadius,
                    0, 0,
                    0, 0,
                    mRadius, mRadius
            };
        } else if (position == getChildCount() - 1) {
            radius = new float[]{
                    0, 0,
                    mRadius, mRadius,
                    mRadius, mRadius,
                    0, 0
            };
        } else {
            radius = new float[]{
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0
            };
        }
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadii(radius);
        shape.setColor(color);
        shape.setStroke(mStroke, mHighlightColor);
        return shape;
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * Generate a value suitable for use in {@link #setId(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public static interface OnTabCheckedListener {
        public void onChecked(FlatTabGroup group, int position);
    }
}
