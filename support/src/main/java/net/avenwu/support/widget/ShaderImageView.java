package net.avenwu.support.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by chaobin on 2/3/15.
 */
public class ShaderImageView extends ImageView {
    Paint paint;
    boolean shouldDrawOpening = true;
    PointF mCenterPoint = new PointF();

    public ShaderImageView(Context context) {
        this(context, null);
    }

    public ShaderImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShaderImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (paint == null) {
            Bitmap original = Bitmap.createBitmap(
                    getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas originalCanvas = new Canvas(original);
            super.onDraw(originalCanvas);

            Shader shader = new BitmapShader(original,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setShader(shader);
            greyBitmap = drawBitmap(original);
        }
        canvas.drawBitmap(greyBitmap, 0, 0, greyPaint);
        canvas.drawColor(0x99000000);
        if (shouldDrawOpening) {
            canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, 50, paint);
        }
    }

    Bitmap greyBitmap;
    Paint greyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Bitmap drawBitmap(Bitmap original) {
        Bitmap bitmap = Bitmap.createBitmap(original.getWidth(),
                original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(
                getColorMatrix()));
        paint.setMaskFilter(new BlurMaskFilter(200, BlurMaskFilter.Blur.NORMAL));
        canvas.drawBitmap(original, 0, 0, paint);
        return bitmap;
    }

    private ColorMatrix getColorMatrix() {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        return colorMatrix;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isPeekThrough = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isPeekThrough = true;
                mCenterPoint.set(event.getX(), event.getY());
                invalidate();
                break;
        }
        return isPeekThrough || super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mCenterPoint.set(getWidth() / 2.0f, getHeight() / 2.0f);
    }
}
