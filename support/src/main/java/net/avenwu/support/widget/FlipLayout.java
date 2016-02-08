package net.avenwu.support.widget;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

/**
 * Created by aven on 1/19/16.
 */
public class FlipLayout extends FrameLayout {
    View mFrontView;
    View mBackView;
    boolean isFlipping = false;

    public FlipLayout(Context context) {
        this(context, null);
    }

    public FlipLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 1) {
            throw new IllegalArgumentException("FlipLayout can only have to child view");
        }
        super.addView(child, index, params);
        if (mBackView == null) {
            mBackView = child;
        } else {
            mFrontView = child;
        }
    }

    public void setViewOrder(View front, View back) {
        mFrontView = front;
        mBackView = back;
        mFrontView.setVisibility(VISIBLE);
        mBackView.setVisibility(INVISIBLE);
    }

    /**
     * toggle the flip state
     */
    public void flip() {
        if (isFlipping) {
            return;
        }
        mBackView.setVisibility(GONE);
        mFrontView.setVisibility(VISIBLE);
        final float centerX = mFrontView.getWidth() / 2f;
        final float centerY = mFrontView.getHeight() / 2f;
        final FlipAnimation frontViewAnimation = new FlipAnimation(centerX, centerY, true);
        frontViewAnimation.setDuration(500);
        frontViewAnimation.setFillAfter(true);
        frontViewAnimation.setInterpolator(new AccelerateInterpolator());
        frontViewAnimation.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mBackView.setVisibility(VISIBLE);
                mFrontView.setVisibility(INVISIBLE);
                final float centerX = mBackView.getWidth() / 2f;
                final float centerY = mBackView.getHeight() / 2f;
                FlipAnimation backViewAnimation = new FlipAnimation(centerX, centerY, false);
                backViewAnimation.setDuration(500);
                backViewAnimation.setFillAfter(true);
                backViewAnimation.setInterpolator(new DecelerateInterpolator());
                backViewAnimation.setAnimationListener(new SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        View temp = mFrontView;
                        mFrontView = mBackView;
                        mBackView = temp;
                        isFlipping = false;
                    }
                });
                mBackView.startAnimation(backViewAnimation);
            }
        });
        mFrontView.startAnimation(frontViewAnimation);
        isFlipping = true;
    }

    private static class FlipAnimation extends Animation {
        private final float mFrom;
        private final float mTo;
        private final float mCenterX;
        private final float mCenterY;
        private final float SCALE_DEFAULT = 0.618f;
        private final float ALPHA_DEFAULT = 0.8f;
        private Camera mCamera;
        private boolean mScaleDown;

        public FlipAnimation(float mCenterX, float mCenterY, boolean scaleDown) {
            this.mCenterX = mCenterX;
            this.mCenterY = mCenterY;
            mScaleDown = scaleDown;
            if (scaleDown) {
                this.mFrom = 0;
                this.mTo = 90;
            } else {
                this.mFrom = -90;
                this.mTo = 0;
            }
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float degree = mFrom + (mTo - mFrom) * interpolatedTime;
            float scale = mScaleDown ? (1 - (1 - SCALE_DEFAULT) * interpolatedTime) :
                    (SCALE_DEFAULT + (1 - SCALE_DEFAULT) * interpolatedTime);
            float alpha = mScaleDown ? (1 - (1 - ALPHA_DEFAULT) * interpolatedTime) :
                    (ALPHA_DEFAULT + (1 - ALPHA_DEFAULT) * interpolatedTime);
            t.setAlpha(alpha);

            final Matrix matrix = t.getMatrix();
            mCamera.save();
            mCamera.rotateY(degree);
            mCamera.getMatrix(matrix);
            mCamera.restore();

            matrix.preTranslate(-mCenterX, -mCenterY);
            matrix.postTranslate(mCenterX, mCenterY);
            matrix.preScale(scale, scale, mCenterX, mCenterY);
        }
    }

    private static class SimpleAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
