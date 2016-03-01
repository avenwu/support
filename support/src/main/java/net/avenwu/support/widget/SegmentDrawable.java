package net.avenwu.support.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Segment tab background drawable which can not realized by xml shape drawable
 *
 * @author aven
 */
public class SegmentDrawable extends Drawable {

    Paint mPaint;
    Path mPath;
    int mStyle;
    int mCornerRadius = 10;

    public interface Style {
        int LEFT_EDGE = 0;
        int MIDDLE = 1;
        int RIGHT_EDGE = 2;
    }

    public SegmentDrawable(int style) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPath = new Path();
        mStyle = style;
    }

    public SegmentDrawable(SegmentDrawable drawable) {
        mPaint = new Paint(drawable.mPaint);
        mPath = new Path(drawable.mPath);
        mStyle = drawable.mStyle;
        mCornerRadius = drawable.mCornerRadius;
    }

    public void setStrokeWidth(int px) {
        mPaint.setStrokeWidth(px);
    }

    public void setCornerRadius(int radius) {
        mCornerRadius = radius;
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        updateShape();
    }

    private void updateShape() {
        Path path = mPath;
        if (path != null) {
            path.reset();
            int width = 2 * mCornerRadius;
            final Rect r = getBounds();
            final float strokeWidth = mPaint.getStrokeWidth();
            switch (mStyle) {
                case Style.LEFT_EDGE:
                    RectF topLeftRect = new RectF(strokeWidth, strokeWidth, width, width);
                    path.arcTo(topLeftRect, 180, 90);
                    path.lineTo(r.width(), strokeWidth);
                    path.lineTo(r.width(), r.height() - strokeWidth);
                    path.lineTo(width + strokeWidth, r.height() - strokeWidth);
                    RectF BottomLeftRect = new RectF(strokeWidth, r.height() - width - strokeWidth, width, r.height() - strokeWidth);
                    path.arcTo(BottomLeftRect, 90, 90);
                    path.close();
                    break;
                case Style.MIDDLE:
                    path.moveTo(0, strokeWidth);
                    path.lineTo(r.width(), strokeWidth);
                    path.lineTo(r.width(), r.height() - strokeWidth);
                    path.lineTo(0, r.height() - strokeWidth);
                    path.close();
                    break;
                case Style.RIGHT_EDGE:
                    path.moveTo(0, strokeWidth);
                    path.lineTo(r.width() - width - strokeWidth, strokeWidth);
                    RectF topRightRect = new RectF(r.width() - width - strokeWidth, strokeWidth, r.width() - strokeWidth, width + strokeWidth);
                    path.arcTo(topRightRect, 270, 90);
                    path.lineTo(r.width() - strokeWidth, r.height() - width - strokeWidth);
                    RectF bottomRightRect = new RectF(r.width() - width - strokeWidth, r.height() - width - strokeWidth, r.width() - strokeWidth, r.height() - strokeWidth);
                    path.arcTo(bottomRightRect, 0, 90);
                    path.lineTo(0, r.height() - strokeWidth);
                    path.close();
                    break;
            }
        }
    }

    @Override
    protected SegmentDrawable clone() throws CloneNotSupportedException {
        return new SegmentDrawable(this);
    }

    public StateListDrawable newStateListDrawable() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        try {
            SegmentDrawable checked = clone();
            checked.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            stateListDrawable.addState(new int[]{android.R.attr.state_checked}, checked);
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, checked);
            stateListDrawable.addState(new int[]{}, this);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stateListDrawable;
    }
}
