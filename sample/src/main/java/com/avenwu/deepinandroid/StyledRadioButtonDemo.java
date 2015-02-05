package com.avenwu.deepinandroid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.LinearLayout;

import net.avenwu.support.widget.FlatTabGroup;

import butterknife.InjectView;

/**
 * Created by chaobin on 2/4/15.
 */
public class StyledRadioButtonDemo extends ActionBarActivity {
    @InjectView(R.id.ll_container)
    LinearLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.styled_radio_button);
    }
}
