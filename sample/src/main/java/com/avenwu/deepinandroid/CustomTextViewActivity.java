package com.avenwu.deepinandroid;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by chaobin on 11/18/15.
 */
public class CustomTextViewActivity extends AppCompatActivity {

    static final String HTML_IMG = "...<img src='icon'/><br>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textview_layout);

        Html.ImageGetter mImageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight
                        ());
                }
                return "icon".equals(source) ? drawable : null;
            }
        };
        Spanned text = Html.fromHtml(getResources().getString(R.string.sample_text_2) + HTML_IMG, mImageGetter, null);
        ((TextView) findViewById(R.id.tv_simple_text)).setText(text);
    }
}
