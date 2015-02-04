package net.avenwu.support.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;

import net.avenwu.support.R;

public class FlatTab extends RadioButton {
    public FlatTab(Context context) {
        this(context, null);
    }

    public FlatTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlatTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setButtonDrawable(null);
        setGravity(Gravity.CENTER);
    }

}