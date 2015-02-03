package net.avenwu.support.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import net.avenwu.support.R;

/**
 * Created by chaobin on 1/7/15.
 */
public class DimImageView extends ImageView {
    public static int DEFAULT_DIM = 0x99000000;
    int mDimColor;

    public DimImageView(Context context) {
        this(context, null);
    }

    public DimImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DimImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DimImageView, defStyleAttr, 0);
        mDimColor = array.getColor(R.styleable.DimImageView_dim, DEFAULT_DIM);
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mDimColor);
    }
}
