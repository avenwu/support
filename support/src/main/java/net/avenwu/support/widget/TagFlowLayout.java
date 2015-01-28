package net.avenwu.support.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.util.Xml;
import android.view.Gravity;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by chaobin on 1/14/15.
 */
public class TagFlowLayout extends ViewGroup
        implements TextWatcher, View.OnKeyListener, View.OnClickListener {

    private static final int INVALID_VALUE = -1;

    private SparseIntArray mCachedPosition = new SparseIntArray();

    private EditText mInputView;

    /**
     * Comma in both English & Chinese
     */
    private char[] mKeyChar = new char[]{',', '，'};

    /**
     * Key event of enter, comma
     */
    private int[] mKeyCode = new int[]{
            KeyEvent.KEYCODE_ENTER,
            KeyEvent.KEYCODE_COMMA,
            KeyEvent.KEYCODE_NUMPAD_COMMA
    };

    private int mCheckIndex = INVALID_VALUE;

    private Decorator mDecorator;

    public TagFlowLayout(Context context) {
        this(context, null);
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInputView = new EditText(context);
        mInputView.setBackgroundColor(Color.TRANSPARENT);
        mInputView.addTextChangedListener(this);
        mInputView.setOnKeyListener(this);
        mInputView.setPadding(0, 0, 0, 0);
        mInputView.setMinWidth(20);
        mInputView.setSingleLine();
        mInputView.setGravity(Gravity.CENTER_VERTICAL);
        setDecorator(new SimpleDecorator(context));
        addView(mInputView);
        setOnClickListener(this);
        previewInEditMode();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = 0;
        int maxHeight = 0;
        int width = 0;
        int height = 0;
        mCachedPosition.clear();
        final int widthSpace = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
                - getPaddingRight();
        final int count = getChildCount();
        int verticalCount = 0;
        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);
            if (nullChildView(child)) {
                continue;
            }
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidthSpace = lp.leftMargin + child.getMeasuredWidth() + lp.rightMargin;
            int childHeightSpace = lp.topMargin + child.getMeasuredHeight() + lp.bottomMargin;
            if (maxWidth + childWidthSpace > widthSpace) {
                maxWidth = childWidthSpace;
                maxHeight = Math.max(maxHeight, childHeightSpace);
                height += maxHeight;
                mCachedPosition.put(i, ++verticalCount);
            } else {
                maxWidth += childWidthSpace;
                maxHeight = childHeightSpace;
            }
            width = Math.max(width, maxWidth);
        }
        height += maxHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(getImprovedSize(widthMeasureSpec, width),
                getImprovedSize(heightMeasureSpec, height));
    }

    private boolean nullChildView(View child) {
        return child == null || child.getVisibility() == GONE;
    }

    private int getImprovedSize(int measureSpec, int size) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                return size;
            case MeasureSpec.EXACTLY:
                return specSize;
        }
        return specSize;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childTop = getPaddingTop();
        int childLeft = getPaddingLeft();
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (nullChildView(child)) {
                continue;
            }
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();
            final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            if (mCachedPosition.get(i, INVALID_VALUE) != INVALID_VALUE) {
                childTop += lp.topMargin + childHeight + lp.bottomMargin;
                childLeft = getPaddingLeft();
            } else if (childTop == getPaddingTop()) {
                childTop += lp.topMargin;
            }
            childLeft += lp.leftMargin;
            setChildFrame(child, childLeft, childTop, childWidth, childHeight);
            childLeft += childWidth + lp.rightMargin;
        }
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        MarginLayoutParams params = new MarginLayoutParams(p);
        if (mDecorator != null) {
            params.setMargins(mDecorator.getMargin()[0], mDecorator.getMargin()[1],
                    mDecorator.getMargin()[2], mDecorator.getMargin()[3]);
        }
        return params;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        MarginLayoutParams params = new MarginLayoutParams(getContext(), attrs);
        if (mDecorator != null) {
            params.setMargins(mDecorator.getMargin()[0], mDecorator.getMargin()[1],
                    mDecorator.getMargin()[2], mDecorator.getMargin()[3]);
        }
        return params;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        MarginLayoutParams params = new MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                mDecorator != null ? mDecorator.getHeight() : ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mDecorator != null) {
            params.setMargins(mDecorator.getMargin()[0], mDecorator.getMargin()[1],
                    mDecorator.getMargin()[2], mDecorator.getMargin()[3]);
        }
        return params;
    }

    /**
     * set the keyCode list which trigger the TAG generation, the default keyCode are
     * {@link android.view.KeyEvent#KEYCODE_ENTER},{@link android.view.KeyEvent#KEYCODE_COMMA}
     *
     * @param keyChar comma on mobile are not always keyCode, null will be ignored
     * @param keyCode keyCode defined in {@link android.view.KeyEvent}, null will be ignored
     */
    public void setActionKeyCode(char[] keyChar, int... keyCode) {
        if (keyChar != null && keyChar.length > 0) {
            mKeyChar = keyChar;
        }
        if (keyCode != null && keyCode.length > 0) {
            mKeyCode = keyCode;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (isKeyCodeHit(keyCode)) {
                if (!TextUtils.isEmpty(mInputView.getText().toString())) {
                    generateTag(mInputView.getText().toString());
                }
                return true;
            } else if (KeyEvent.KEYCODE_DEL == keyCode) {
                if (TextUtils.isEmpty(mInputView.getText().toString())) {
                    deleteTag();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isKeyCodeHit(int keyCode) {
        if (mKeyCode != null && mKeyCode.length > 0) {
            for (int key : mKeyCode) {
                if (key == keyCode) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isKeyCharHit(char keyChar) {
        if (mKeyChar != null && mKeyChar.length > 0) {
            for (char key : mKeyChar) {
                if (key == keyChar) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            if (isKeyCharHit(s.charAt(0))) {
                mInputView.setText("");
            } else if (isKeyCharHit(s.charAt(s.length() - 1))) {
                mInputView.setText("");
                generateTag(s.subSequence(0, s.length() - 1) + "");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TagFlowLayout) {
            mInputView.requestFocus();
            InputMethodManager m = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            m.showSoftInput(mInputView, InputMethodManager.SHOW_FORCED);
            // clear check status
            if (mCheckIndex != INVALID_VALUE) {
                updateCheckStatus(getChildAt(mCheckIndex), false);
                mCheckIndex = INVALID_VALUE;
            }
            return;
        }
        final int index = indexOfChild(v);
        // skip unnecessary setting
        if (index != mCheckIndex) {
            mCheckIndex = index;
            updateCheckStatus(v, true);
            for (int i = 0; i < index; i++) {
                updateCheckStatus(getChildAt(i), false);
            }
            //skip input box
            for (int i = index + 1; i < getChildCount() - 1; i++) {
                updateCheckStatus(getChildAt(i), false);
            }
        }
    }

    private void deleteTag() {
        if (getChildCount() > 1) {
            removeViewAt(mCheckIndex == INVALID_VALUE ? indexOfChild(mInputView) - 1 : mCheckIndex);
            mCheckIndex = INVALID_VALUE;
            mInputView.requestFocus();
        }
    }

    /**
     * can be null
     */
    private void generateTag(CharSequence tag) {
        CharSequence tagString = tag == null ? mInputView.getText().toString() : tag;
        mInputView.getText().clear();
        final int targetIndex = indexOfChild(mInputView);
        TextView tagLabel;
        if (mDecorator.getLayout() != INVALID_VALUE) {
            View view = View.inflate(getContext(), mDecorator.getLayout(), null);
            if (view instanceof TextView) {
                tagLabel = (TextView) view;
            } else {
                throw new IllegalArgumentException(
                        "The custom layout for tagLabel label must have TextView as root element");
            }
        } else {
            tagLabel = new TextView(getContext());
            tagLabel.setPadding(mDecorator.getPadding()[0], mDecorator.getPadding()[1],
                    mDecorator.getPadding()[2], mDecorator.getPadding()[3]);
            tagLabel.setTextSize(mDecorator.getTextSize());
        }
        updateCheckStatus(tagLabel, false);
        tagLabel.setText(tagString);
        tagLabel.setSingleLine();
        tagLabel.setGravity(Gravity.CENTER_VERTICAL);
        tagLabel.setEllipsize(TextUtils.TruncateAt.END);
        if (mDecorator.getMaxLength() != INVALID_VALUE) {
            InputFilter maxLengthFilter = new InputFilter.LengthFilter(mDecorator.getMaxLength());
            tagLabel.setFilters(new InputFilter[]{maxLengthFilter});
        }
        tagLabel.setClickable(true);
        tagLabel.setOnClickListener(this);
        addView(tagLabel, targetIndex);
        mInputView.requestFocus();
    }

    private void updateCheckStatus(View view, boolean checked) {
        if (view == null) {
            return;
        }
        // don't reuse drawable for different tag label
        Drawable[] drawables = mDecorator.getBackgroundDrawable();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawables[drawables.length > 1 ? (checked ? 1 : 0) : 0]);
        } else {
            view.setBackground(drawables[drawables.length > 1 ? (checked ? 1 : 0) : 0]);
        }
        final int[] color = mDecorator.getTextColor();
        if (color != null && color.length > 0) {
            ((TextView) view).setTextColor((color[color.length > 1 ? (checked ? 1 : 0) : 0]));
        }
    }

    private void previewInEditMode() {
        if (isInEditMode()) {
            generateTag("Hot Tag Ckecked");
            generateTag("TAG A");
            generateTag("TAG B");
            generateTag("TAG C");
            updateCheckStatus(getChildAt(0), true);
            mInputView.setText("Input here...");
        }
    }

    public void setDecorator(Decorator decorator) {
        if (decorator != null) {
            mDecorator = decorator;
            try {
                setInnerAttribute();
            } catch (Exception e) {
                e.printStackTrace();
                throw new UnknownError(e.getMessage() + "\n unavailable to setDecorator");
            }
        }
    }

    /**
     * Make sure the input EditView looks like TextView label
     */
    private void setInnerAttribute()
            throws XmlPullParserException, IOException, ClassNotFoundException {
        mInputView.setTextSize(mDecorator.getTextSize());
        if (mDecorator.getMaxLength() != INVALID_VALUE) {
            InputFilter maxLengthFilter = new InputFilter.LengthFilter(mDecorator.getMaxLength());
            mInputView.setFilters(new InputFilter[]{maxLengthFilter});
        }
        if (mDecorator.getTextColor() != null && mDecorator.getTextColor().length > 1) {
            mInputView.setTextColor(mDecorator.getTextColor()[0]);
        }
        if (mDecorator.getLayout() != INVALID_VALUE) {
            XmlResourceParser parser = getResources().getLayout(mDecorator.getLayout());
            final AttributeSet set = Xml.asAttributeSet(parser);
            int type = 0;
            while ((type = parser.next()) != XmlPullParser.START_TAG &&
                    type != XmlPullParser.END_DOCUMENT) {
                // Empty
            }
            if (type != XmlPullParser.START_TAG) {
                throw new InflateException(parser.getPositionDescription()
                        + ": No start tag found!");
            }
            final String name = parser.getName();
            if ("TextView".equals(name) || Class.forName(name).isInstance(TextView.class)) {
                int[] attr = new int[]{
                        android.R.attr.layout_width,
                        android.R.attr.layout_height,
                };
                TypedArray array = getContext().obtainStyledAttributes(set, attr);
                final int height = array.getDimensionPixelSize(1, 0);
                if (height != 0) {
                    MarginLayoutParams layoutParams = (MarginLayoutParams) mInputView
                            .getLayoutParams();
                    layoutParams.height = height;
                }
                //TODO other useful attribute
                array.recycle();
            } else {
                throw new InflateException(parser.getPositionDescription()
                        + ": Only TextView or subclass of TextView is supported!");
            }
        }
    }

    public CharSequence[] getTagArray() {
        final int count = getChildCount();
        if (count > 1) {
            CharSequence[] tags = new CharSequence[count - 1];
            for (int i = 0; i < count - 1; i++) {
                View child = getChildAt(i);
                if (child instanceof TextView) {
                    tags[i] = ((TextView) child).getText();
                }
            }
            return tags;
        }
        return new CharSequence[]{};
    }

    public void setTagArray(CharSequence[] tags) {
        for (CharSequence tag : tags) {
            generateTag(tag);
        }
    }

    /**
     * Implements your own Decorator to custom the tag view
     */
    interface Decorator {

        /**
         * Size in unit of sp
         */
        public int getTextSize();

        /**
         * Padding on left, top, right, bottom in unit of dip
         */
        public int[] getPadding();

        /**
         * Margin on left, top, right, bottom in unit of dip
         */
        public int[] getMargin();

        /**
         * Color in format of AARRGGBB
         */
        public int[] getTextColor();

        /**
         * Tag view's background can be satisfied either by color or drawable resources,
         */
        public Drawable[] getBackgroundDrawable();

        /**
         * Size in unit of dip, if you provide custom layout by {@link #getLayout()}, it must have
         * the same height value
         */
        public int getHeight();

        /**
         * Provide your own layout id so you can custom the tag view what ever you like,
         * keep in mind the layout must have TextView as root element
         *
         * @return -1 will be ignored
         */
        public int getLayout();

        /**
         * @return
         */
        public int getMaxLength();
    }

    /**
     * Default decorator which will set on the TAG view
     */
    public static class SimpleDecorator implements Decorator {

        protected int textSize;

        protected int[] textColor;

        protected int[] padding;

        protected int[] margin;

        protected int mTagHeight;

        protected int mRadius;

        public SimpleDecorator(Context context) {
            textSize = 14;
            final int p = getPixelSize(context, TypedValue.COMPLEX_UNIT_DIP, 5);
            padding = new int[]{p, p, p, p};
            final int m = getPixelSize(context, TypedValue.COMPLEX_UNIT_DIP, 2);
            margin = new int[]{m, m, m, m};
            mRadius = getPixelSize(context, TypedValue.COMPLEX_UNIT_DIP, 5);
            mTagHeight = getPixelSize(context, TypedValue.COMPLEX_UNIT_DIP, 30);
            textColor = new int[]{0xFF000000, 0xFFFFFFFF};
        }

        private int getPixelSize(Context context, int unit, int size) {
            return (int) TypedValue
                    .applyDimension(unit, size, context.getResources().getDisplayMetrics());
        }

        @Override
        public int getTextSize() {
            return textSize;
        }

        @Override
        public int[] getPadding() {
            return padding;
        }

        @Override
        public int[] getMargin() {
            return margin;
        }

        @Override
        public int[] getTextColor() {
            return textColor;
        }

        public int getHeight() {
            return mTagHeight;
        }

        public Drawable[] getBackgroundDrawable() {
            return new Drawable[]{
                    newRoundRectShape(0xFF4285f4, mRadius),
                    newRoundRectShape(0xFF3f51b5, mRadius)
            };
        }

        @Override
        public int getLayout() {
            return INVALID_VALUE;
        }

        @Override
        public int getMaxLength() {
            return 20;
        }

        protected Drawable newRoundRectShape(int color, int radius) {
            ShapeDrawable shape = new ShapeDrawable(new RoundRectShape(new float[]{radius, radius,
                    radius, radius, radius, radius, radius, radius}, null, null));
            shape.getPaint().setStyle(Paint.Style.FILL);
            shape.getPaint().setColor(color);
            shape.getPaint().setAntiAlias(true);
            return shape;
        }
    }

}
