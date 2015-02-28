package net.avenwu.support.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import net.avenwu.support.R;

public class DrawerFrameV2 extends FrameLayout {
    View mLeftView;
    View mContentView;
    Scroller mScroller;
    float mSrcX;
    int mLeftViewWidth;

    boolean isSliding = false;
    boolean mSlidable = true;
    int mTouchSlop;

    public DrawerFrameV2(Context context) {
        this(context, null);
    }

    public DrawerFrameV2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerFrameV2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mScroller = new Scroller(getContext(), new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                return (float) (1.0D + Math.pow(input - 1.0D, 5.0D));
            }
        });
        mLeftViewWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200,
                getResources().getDisplayMetrics());
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mShadowLeft = getResources().getDrawable(android.R.drawable.ic_media_play);
    }

    @Override
    protected void onFinishInflate() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getId() == R.id.menu) {
                mLeftView = child;
            } else if (child.getId() == R.id.main) {
                mContentView = child;
            }
        }
        if (mLeftView == null) {
            mLeftView = new FrameLayout(getContext());
            mLeftView.setId(R.id.menu);
            mLeftView.setLayoutParams(new LayoutParams(mLeftViewWidth, ViewGroup.LayoutParams.MATCH_PARENT));
            mLeftView.setBackgroundColor(0xff669900);
            addView(mLeftView);
        }
        if (mContentView == null) {
            mContentView = new FrameLayout(getContext());
            mContentView.setId(R.id.main);
            mContentView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mContentView.setBackgroundColor(0xffff8800);
            addView(mContentView);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mSlidable) return false;
        final int action = ev.getAction();
        if (action != MotionEvent.ACTION_DOWN && isSliding) return true;
        switch (action) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                return false;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - mSrcX) > mTouchSlop) {
                    mSrcX = ev.getX();
                    isSliding = true;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                isSliding = false;
                mSrcX = ev.getX();
                break;
        }
        return isSliding;
    }

    public void setSlide(boolean enable) {
        mSlidable = enable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        d("UIView", "event:" + event.toString());
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mSrcX = event.getX();
                // TODO after left menu drag outside screen, the menu is somewhat invisible while drag in again?
                mLeftView.setVisibility(VISIBLE);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - mSrcX;
                int x = mLeftView.getLeft();
                if (dx != 0) {
                    if (dx + x > 0) {
                        dx = 0 - x;
                    }
                    mLeftView.offsetLeftAndRight((int) dx);
                    mSrcX = event.getX();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int currentX = mLeftView.getLeft();
                if (currentX + event.getX() - mSrcX >= -mLeftViewWidth / 2.0) {
                    int duration = (int) (Math.abs(0 - currentX + 0.5f) / mLeftViewWidth * 1000);
                    mScroller.startScroll(currentX, 0, 0 - currentX, 0, duration);
                } else {
                    int duration = (int) (Math.abs(-mLeftViewWidth - currentX + 0.5f) / mLeftViewWidth * 1000);
                    mScroller.startScroll(currentX, 0, -mLeftViewWidth - currentX, 0, duration);
                }
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (child.getId() == R.id.menu) {
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();
                int childLeft = 0;
                child.layout(childLeft, lp.topMargin, childLeft + childWidth, lp.topMargin + childHeight);
                child.offsetLeftAndRight(-mLeftViewWidth);
            } else if (child.getId() == R.id.main) {
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            }
        }
    }

    Drawable mShadowLeft;// = new ColorDrawable(Color.RED);
    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;
    private int mScrimColor = DEFAULT_SCRIM_COLOR;
    private float mScrimOpacity;
    private Paint mScrimPaint = new Paint();
    float alpha;

  /*  @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean result = super.drawChild(canvas, child, drawingTime);

        if (child.getId() == R.id.main) {
            int clipLeft = 0, clipRight = getWidth();
            final int baseAlpha = (mScrimColor & 0xff000000) >>> 24;

//            final int imag = (int) (baseAlpha * mScrimOpacity);
//            final int color = imag << 24 | (mScrimColor & 0xffffff);
            mScrimPaint.setColor(Color.argb((int) (255 * alpha * 0xff), 0, 0, 0));
            canvas.drawRect(clipLeft, 0, clipRight, getHeight(), mScrimPaint);
        } else if (child.getId() == R.id.menu) {
            final int shadowWidth = mShadowLeft.getIntrinsicWidth();
            final int childRight = child.getRight();
            final int drawerPeekDistance = mLeftViewWidth;
            alpha =
                    Math.max(0, Math.min((float) childRight / drawerPeekDistance, 1.f));
            mShadowLeft.setBounds(childRight, child.getTop(),
                    childRight + shadowWidth, child.getBottom());
            mShadowLeft.setAlpha((int) (0xff * alpha));
            mShadowLeft.draw(canvas);
        }
        return result;
    }*/

    boolean isContentView(View child) {
        return ((LayoutParams) child.getLayoutParams()).gravity == Gravity.NO_GRAVITY;
    }

    boolean isDrawerView(View child) {
        final int gravity = ((LayoutParams) child.getLayoutParams()).gravity;
//        final int absGravity = GravityCompat.getAbsoluteGravity(gravity,
//                ViewCompat.getLayoutDirection(child));
//        return (absGravity & (Gravity.LEFT | Gravity.RIGHT)) != 0;
        return gravity == Gravity.LEFT || gravity == Gravity.RIGHT;
    }

    public void showMenuSmoothly() {
        mScroller.startScroll(mLeftViewWidth, 0, -mLeftViewWidth, 0, 1000);
        invalidate();
    }

    public void dismissSmoothly() {
        int start = mScroller.getCurrX();
        mScroller.startScroll(start, 0, mLeftViewWidth - start, 0, 1000);
        invalidate();
    }

    public void setMenuView(View view) {
        if (mLeftView instanceof ViewGroup) {
            ((ViewGroup) mLeftView).removeAllViews();
            ((ViewGroup) mLeftView).addView(view);
            requestLayout();
        }
    }

    public void setContentView(View view) {
        if (mContentView instanceof ViewGroup) {
            ((ViewGroup) mContentView).removeAllViews();
            ((ViewGroup) mContentView).addView(view);
            requestLayout();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int oldLeft = mLeftView.getLeft();
            int left = mScroller.getCurrX();
            if (oldLeft != mScroller.getFinalX()) {
                int xOffset = left - oldLeft;
                int dx = xOffset + oldLeft;
                if (dx > 0) {
                    xOffset = 0 - oldLeft;
                } else if (dx < -mLeftViewWidth) {
                    xOffset = -mLeftViewWidth - oldLeft;
                }
                mLeftView.offsetLeftAndRight(xOffset);
                onScrollChanged(left, 0, oldLeft, 0);
            }
            invalidate();
        } else {
            mScroller.abortAnimation();
        }
    }

    private void d(String tag, String text) {
//        if (BuildConfig.DEBUG) {
        Log.d(tag, text);
//        }
    }

    /**
     * This is called in response to an internal scroll in this view (i.e., the
     * view scrolled its own contents). This is typically as a result of
     * {@link #scrollBy(int, int)} or {@link #scrollTo(int, int)} having been
     * called.
     *
     * @param l    Current horizontal scroll origin.
     * @param t    Current vertical scroll origin.
     * @param oldl Previous horizontal scroll origin.
     * @param oldt Previous vertical scroll origin.
     */
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

    }
}
