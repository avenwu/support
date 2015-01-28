package com.avenwu.deepinandroid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import net.avenwu.support.widget.TagFlowLayout;

/**
 * Created by chaobin on 1/14/15.
 */
public class TagInputDemo extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(new TagInputLayout(this));
        setContentView(R.layout.test_tag_input_layout);
//        ((TagInputLayout) findViewById(R.id.tags)).setDecorator(new TagInputLayout.SimpleDecorator(this) {
//            Drawable[] mDrawable = new Drawable[]{
//                    getResources().getDrawable(R.drawable.b1),
//                    getResources().getDrawable(R.drawable.b2)
//            };
//
//            @Override
//            public Drawable[] getBackgroundDrawable() {
//                return mDrawable;
//            }
//        });
    }

    public void onGetTags(View view) {
        TextView tv = (TextView) findViewById(R.id.tv_tags);
        tv.setText("All tags:\n");
        for (CharSequence tag : ((TagFlowLayout) findViewById(R.id.tags)).getTagArray()) {
            tv.append(tag);
            tv.append("  ");
        }
    }
}
