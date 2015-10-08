package com.avenwu.deepinandroid;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import net.avenwu.support.util.TypefaceContextWrapper;
import net.avenwu.support.util.TypefaceUtils;

public class TypefaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typeface);
        //1. 直接设置TextView setTypeface
        final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Stencbab" +
            ".ttf");
        TextView textView = (TextView) findViewById(R.id.tv_label_font);
        textView.setTypeface(typeface);

        //2. 缓存/复用Typeface，避免内存浪费
        TypefaceUtils.setTypeface(this, (TextView) findViewById(R.id.tv_label_font_2),
            "fonts/Roboto-Bold.ttf");

        //3. 自定义LayoutInflator


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypefaceContextWrapper.wrap(newBase));
    }
}
