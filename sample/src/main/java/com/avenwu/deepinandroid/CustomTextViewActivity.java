package com.avenwu.deepinandroid;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

/**
 * Created by chaobin on 11/18/15.
 */
public class CustomTextViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textview_layout);

        ((TextView) findViewById(R.id.tv_test)).setText(Html.fromHtml("219473892740218937498127472349823178461982376，40123463218。，74632781964923817649237816498723164982371649782364897231...<img src='icon'>", new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable mIndicator = getResources().getDrawable(R.drawable.ic_arrow);
                if (mIndicator != null) {
                    mIndicator.setBounds(0, 0, mIndicator.getIntrinsicWidth(), mIndicator.getIntrinsicHeight
                            ());
                }
                return mIndicator;
            }
        }, null));
    }
}
