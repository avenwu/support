package net.avenwu.support.widget;

import net.avenwu.support.BuildConfig;
import net.avenwu.support.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class DrawerFrame extends FrameLayout {
	FrameLayout left;
	FrameLayout main;
	Scroller scroller;

	public DrawerFrame(Context context) {
		this(context, null);
	}

	public DrawerFrame(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DrawerFrame(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
//		final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DrawerFrame, defStyle, 0);
//		a.recycle();
		scroller = new Scroller(getContext(), new Interpolator() {
			@Override
			public float getInterpolation(float input) {
				return (float) (1.0D + Math.pow(input - 1.0D, 5.0D));
			}
		});
		MENU_WIDTH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200,
				getResources().getDisplayMetrics());
		main = new FrameLayout(getContext());
		main.setId(R.id.main);
		main.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		main.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
		addView(main);

		left = new FrameLayout(getContext());
		left.setId(R.id.menu);
		left.setLayoutParams(new FrameLayout.LayoutParams(MENU_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT));
		addView(left);
		ViewConfiguration configuration = ViewConfiguration.get(getContext());
		touchSlop = configuration.getScaledTouchSlop();
	}

	float mSrcX;
	int MENU_WIDTH;

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
				if (Math.abs(ev.getX() - mSrcX) > touchSlop) {
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

	boolean isSliding = false;
	boolean mSlidable = true;
	int touchSlop;

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
				break;
			case MotionEvent.ACTION_MOVE:
				int oldx = left.getScrollX();
				float dx = mSrcX - event.getX();
				if (dx != 0) {
					left.setVisibility(VISIBLE);
					float x = oldx + dx;
					d("onTouchEvent", "move, oldx=" + oldx + ", dx=" + dx + ", mLeftView=" +
							left.getLeft() + ", right=" + left.getRight() + ", getX=" + event.getX() + ", mSrcX=" + mSrcX);
					d("onTouchEvent", "before x=" + x);
					if (MENU_WIDTH < x) {
						x = MENU_WIDTH;
					}
					if (0 > x) {
						x = 0;
					}
					d("onTouchEvent", "after x=" + x);
					left.scrollTo((int) x, 0);
					mSrcX = event.getX();
				}
				break;
			case MotionEvent.ACTION_UP:
				int currentX = left.getScrollX();
				if (currentX + mSrcX - event.getX() >= MENU_WIDTH / 2.0) {
					int duration = (int) (Math.abs(MENU_WIDTH - currentX + 0.5f) / MENU_WIDTH * 1000);
					scroller.startScroll(currentX, 0, MENU_WIDTH - currentX, 0, duration);
					invalidate();
				} else {
					int duration = (int) (Math.abs(currentX + 0.5f) / MENU_WIDTH * 1000);
					scroller.startScroll(currentX, 0, 0 - currentX, 0, duration);
					invalidate();
				}
				break;
			case MotionEvent.ACTION_CANCEL:
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
				child.scrollTo(MENU_WIDTH, 0);
			} else if (child.getId() == R.id.main) {
				child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
			}
		}
	}

	public void showMenuSmoothly() {
		scroller.startScroll(MENU_WIDTH, 0, -MENU_WIDTH, 0, 1000);
		invalidate();
	}

	public void dismissSmoothly() {
		int start = scroller.getCurrX();
		scroller.startScroll(start, 0, MENU_WIDTH - start, 0, 1000);
		invalidate();
	}

	public void setMenuView(View view) {
		left.removeAllViews();
		left.addView(view);
		requestLayout();
	}

	public void setContentView(View view) {
		main.removeAllViews();
		main.addView(view);
		requestLayout();
	}

	@Override
	public void computeScroll() {
		d("computeScroll", "computeScroll");
		if (scroller.computeScrollOffset()) {
			int oldx = left.getScrollX();
			int x = scroller.getCurrX();
			d("computeScroll", "try scroll, oldx=" + oldx + ", x=" + x);
			if (oldx != x) {
				//this can only effect on the content view inside of mLeftView
				left.scrollTo(x, 0);
				left.invalidate();
			}
			invalidate();
		} else {
			scroller.abortAnimation();
		}
	}

	private void d(String tag, String text) {
		if (BuildConfig.DEBUG) {
			Log.d(tag, text);
		}
	}
}
