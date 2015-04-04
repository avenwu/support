package com.avenwu.deepinandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chaobin on 3/6/15.
 */
public class LinearGradientView extends View {
    Paint mPaint;
    LinearGradient mGradient;

    public LinearGradientView(Context context) {
        this(context, null);
    }

    public LinearGradientView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearGradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mGradient = new LinearGradient(0, 0, 500, 0, new int[]{
                Color.RED, Color.YELLOW, Color.GREEN
        }, null, Shader.TileMode.CLAMP);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setShader(mGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, 500, 500, mPaint);
    }
}
