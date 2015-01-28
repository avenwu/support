package net.avenwu.support.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Created by Chaobin Wu on 2014/10/10.
 */
public class RefreshLayout extends LinearLayout {
	private FrameLayout mHeaderLayout;
	private ListView mListView;
	private float mX;
	private float mY;
	private Scroller mScroller;
	private int mPaddingTop = 0;
	private int MAX_OVER_SCROLL_HEIGHT;
	boolean isSliding = false;
	int touchSlop;

	public RefreshLayout(Context context) {
		this(context, null);
	}

	public RefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(VERTICAL);
		ViewConfiguration configuration = ViewConfiguration.get(getContext());
		touchSlop = configuration.getScaledTouchSlop();
		mScroller = new Scroller(context, new BounceInterpolator());
		mHeaderLayout = new FrameLayout(context);
		LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
		mHeaderLayout.setLayoutParams(layoutParams);
		mHeaderLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
		addView(mHeaderLayout);
		//TODO add text header
		TextView textView = new TextView(context);
		textView.setText("This is header layout content");
		int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
		LayoutParams textParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
		textParams.gravity = Gravity.CENTER;
		textView.setLayoutParams(textParams);
		addHeaderChild(textView);
		mPaddingTop -= height;
		setPadding(0, mPaddingTop, 0, 0);
		MAX_OVER_SCROLL_HEIGHT = 2 * height;
		mListView = new ListView(context, attrs);
		mListView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		addView(mListView);
	}

	public void addHeaderChild(View view) {
		if (mHeaderLayout != null) {
			mHeaderLayout.addView(view);
			requestLayout();
		}
	}

	public void setAdapter(ListAdapter adapter) {
		mListView.setAdapter(adapter);
		if (adapter instanceof BaseAdapter) {
			((BaseAdapter) adapter).notifyDataSetChanged();
		}
	}

	private boolean isFirstItemVisible() {
		if (null == mListView.getAdapter() || mListView.getAdapter().isEmpty()) {
			return true;
		} else {
			return mListView.getFirstVisiblePosition() == 0;
//			if (mListView.getFirstVisiblePosition() <= 1) {
//				final View firstVisibleChild = mListView.getChildAt(0);
//				if (firstVisibleChild != null) {
//					return firstVisibleChild.getTop() >= mListView.getTop();
//				}
//			}
		}
//		return false;
	}

	private boolean isLastItemVisible() {
		if (null == mListView.getAdapter() || mListView.getAdapter().isEmpty()) {
			return true;
		} else {
			return mListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1;
		}
	}

	public ListView getListView() {
		return mListView;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		if (action != MotionEvent.ACTION_DOWN && isSliding) return true;
		switch (action) {
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				isSliding = false;
				break;
			case MotionEvent.ACTION_MOVE:
				Log.d("onInterceptTouchEvent", "scrollY=" + getScrollY() + ", " + ev.toString());
				if (ev.getY() > mY) {
					if (isFirstItemVisible() && Math.abs(ev.getY() - mY) > touchSlop) {
						mY = ev.getY();
						isSliding = true;
					}
				} else {
					if (getScrollY() < 0) {
						mY = ev.getY();
						isSliding = true;
					}
				}
				break;
			case MotionEvent.ACTION_DOWN:
				isSliding = false;
				mY = ev.getY();
				break;
		}
		return isSliding || super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("RefreshLayout", event.toString());
		boolean eatTouchEvent = false;
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mY = event.getY();
				eatTouchEvent = true;
				break;
			case MotionEvent.ACTION_MOVE:
				if (isSliding) {// move down:getScrollY becomes smaller
					float y = event.getY();
					float diff = y - mY;
					mY = event.getY();
					if (diff != 0) {
						float distance = getScrollY() - diff;
						Log.d("RefreshLayout", "move, distance=" + distance + ", getScrollY=" + getScrollY() + ", diff=" + diff);
						if (distance > 0) {
							distance = Math.min(distance, MAX_OVER_SCROLL_HEIGHT);
						} else {
							distance = Math.max(distance, -MAX_OVER_SCROLL_HEIGHT);
						}
						scrollTo(getScrollX(), (int) distance);
					}
					eatTouchEvent = true;
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				if (isSliding) {
					isSliding = false;
					mScroller.abortAnimation();
					int currentY = getScrollY();
					int desY = -currentY - MAX_OVER_SCROLL_HEIGHT / 2;
					Log.d("RefreshLayout", "getScrollY=" + currentY + ", desY=" + desY);
					//current y > 2/3 of max height, scroll to display the whole header
					if (currentY + event.getY() - mY < -MAX_OVER_SCROLL_HEIGHT / 2) {
						mScroller.startScroll(0, currentY, 0, desY);
					} else {
						mScroller.startScroll(0, currentY, 0, 0 - currentY);
					}
					invalidate();
					eatTouchEvent = true;
				}
				break;
		}
		return eatTouchEvent || super.onTouchEvent(event);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int oldY = getScrollY();
			int y = mScroller.getCurrY();
			if (oldY != y) {
				scrollTo(0, y);
			}
			invalidate();
		} else {
			mScroller.abortAnimation();
		}
	}
}
