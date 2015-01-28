package net.avenwu.support.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by chaobin on 12/25/14.
 */
public class PolygonWithQuadraticBezirView extends View {
    Paint mCirclePaint;
    float mPointOneX;
    float mPointOneY;
    float mPointTwoX;
    float mPointTwoY;
    float mPointOneRadius;
    float mPointTwoRadius;
    Path mPath;
    Paint mShape;
    float MAX_HORIZOTNAL_DISTANCE;
    float MAX_VERTICAL_DISTANCE;

    public PolygonWithQuadraticBezirView(Context context) {
        this(context, null);
    }

    public PolygonWithQuadraticBezirView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolygonWithQuadraticBezirView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(0xff00bcd4);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        MAX_HORIZOTNAL_DISTANCE = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, metrics);
        MAX_VERTICAL_DISTANCE = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, metrics);

        mPointOneX = MAX_VERTICAL_DISTANCE * 0.4f;
        mPointOneY = mPointOneX;
        mPointTwoX = mPointOneX * 1.75f;
        mPointTwoY = MAX_HORIZOTNAL_DISTANCE * 0.5f;
        mPointOneRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, metrics);
        mPointTwoRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, metrics);
        mPath = new Path();
        mShape = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShape.setColor(Color.RED);
        mShape.setStyle(Paint.Style.STROKE);
        mShape.setStrokeWidth(1);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawCircle(mPointOneX, mPointOneY, mPointOneRadius, mCirclePaint);
        canvas.drawCircle(mPointTwoX, mPointTwoY, mPointTwoRadius, mCirclePaint);
        mPath.reset();
        float d = (float) Math.sqrt(Math.pow(mPointTwoX - mPointOneX, 2) + Math.pow(mPointTwoY - mPointOneY, 2));
        float midX = (mPointOneX + mPointTwoX) / 2.0f;
        float midY = (mPointOneY + mPointTwoY) / 2.0f;
        float k = (mPointTwoY - mPointOneY) / (mPointTwoX - mPointOneX);

        float offsetX = (float) (mPointOneRadius * Math.sin(Math.atan(k)));
        float offsetY = (float) (mPointOneRadius * Math.cos(Math.atan(k)));
        float offsetX2 = (float) (mPointTwoRadius * Math.sin(Math.atan(k)));
        float offsetY2 = (float) (mPointTwoRadius * Math.cos(Math.atan(k)));
        float x1 = mPointOneX - offsetX;
        float y1 = mPointOneY + offsetY;

        float x2 = mPointTwoX - offsetX2;
        float y2 = mPointTwoY + offsetY2;

        float x3 = mPointTwoX + offsetX2;
        float y3 = mPointTwoY - offsetY2;

        float x4 = mPointOneX + offsetX;
        float y4 = mPointOneY - offsetY;
        mPath.moveTo(x1, y1);
        mPath.quadTo(midX, midY, x2, y2);
        mPath.lineTo(x3, y3);
        mPath.quadTo(midX, midY, x4, y4);
        mPath.lineTo(x1, y1);
        canvas.drawPath(mPath, mShape);
    }

    public void setFilled(boolean fill) {
        if (fill) {
            mShape.setStyle(Paint.Style.FILL);
            mShape.setColor(mCirclePaint.getColor());
        } else {
            mShape.setStyle(Paint.Style.STROKE);
            mShape.setColor(Color.RED);
        }
        invalidate();
    }

    public void moveHorizontal(float percent) {
        mPointTwoX = MAX_HORIZOTNAL_DISTANCE * percent;
        invalidate();
    }

    public void moveVertical(float percent) {
        mPointTwoY = MAX_VERTICAL_DISTANCE * percent;
        invalidate();
    }
}
