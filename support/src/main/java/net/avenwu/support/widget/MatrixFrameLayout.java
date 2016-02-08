package net.avenwu.support.widget;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

/**
 * Created by aven on 1/20/16.
 */
public class MatrixFrameLayout extends FrameLayout {
    Camera mCamera = new Camera();
    Transformation mTransformation = new Transformation();
    private PaintFlagsDrawFilter mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public MatrixFrameLayout(Context context) {
        this(context, null);
    }

    public MatrixFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    @Override
    public void draw(Canvas canvas) {
        mTransformation.clear();
        mTransformation.setTransformationType(Transformation.TYPE_MATRIX);
        Matrix matrix = mTransformation.getMatrix();
        float degree = 15;
        mCamera.save();
        mCamera.rotateY(degree);
        mCamera.getMatrix(matrix);
        mCamera.restore();

        final float centerY = getHeight() / 2f;
        matrix.preTranslate(0, -centerY);
        matrix.postTranslate(0, centerY);

        canvas.save();
        canvas.concat(mTransformation.getMatrix());
        canvas.setDrawFilter(mPaintFlagsDrawFilter);
        super.draw(canvas);
        canvas.restore();
    }
}
