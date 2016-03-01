package com.avenwu.deepinandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.avenwu.support.widget.SegmentDrawable;

public class CustomDrawableDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_drawable_layout);
        final int color = 0xff35b558;
        int strokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, getResources().getDisplayMetrics());
        int corner = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, getResources().getDisplayMetrics());


        SegmentDrawable drawable1 = new SegmentDrawable(SegmentDrawable.Style.LEFT_EDGE);
        drawable1.setStrokeWidth(strokeWidth);
        drawable1.setColor(color);
        drawable1.setCornerRadius(corner);
        findViewById(R.id.label).setBackgroundDrawable(drawable1);

        SegmentDrawable drawable2 = new SegmentDrawable(SegmentDrawable.Style.MIDDLE);
        drawable2.setStrokeWidth(strokeWidth);
        drawable2.setColor(color);
        drawable2.setCornerRadius(corner);

        findViewById(R.id.label2).setBackgroundDrawable(drawable2);

        SegmentDrawable drawable3 = new SegmentDrawable(SegmentDrawable.Style.RIGHT_EDGE);
        drawable3.setStrokeWidth(strokeWidth);
        drawable3.setColor(color);
        drawable3.setCornerRadius(corner);

        findViewById(R.id.label3).setBackgroundDrawable(drawable3);

        RadioGroup group = (RadioGroup) findViewById(R.id.container);
        int count = group.getChildCount();

        for (int i = 0; i < count; i++) {
            RadioButton child = (RadioButton) group.getChildAt(i);

            SegmentDrawable drawable;
            if (i == 0) {
                drawable = new SegmentDrawable(SegmentDrawable.Style.LEFT_EDGE);
                child.setChecked(true);
            } else if (i == count - 1) {
                drawable = new SegmentDrawable(SegmentDrawable.Style.RIGHT_EDGE);
            } else {
                drawable = new SegmentDrawable(SegmentDrawable.Style.MIDDLE);
            }
            drawable.setColor(color);
            drawable.setStrokeWidth(strokeWidth);
            drawable.setCornerRadius(corner);

            child.setButtonDrawable(null);
            child.setBackgroundDrawable(drawable.newStateListDrawable());
        }
    }
}
