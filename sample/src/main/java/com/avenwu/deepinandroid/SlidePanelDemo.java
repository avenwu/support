package com.avenwu.deepinandroid;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by chaobin on 2/18/15.
 */
public class SlidePanelDemo extends ActionBarActivity {
    int[] mColors = {Color.RED, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW};
    ViewGroup mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slidepanel_layout);
        mContentView = (ViewGroup) findViewById(R.id.fl_container);
//        ListView menuList = (ListView) findViewById(R.id.lv_memu_list);
//        menuList.setAdapter(new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                android.R.id.text1,
//                new String[]{"RED", "BLUE", "CYAN", "GREEN", "YELLOW"}
//        ));
//        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mContentView.setBackgroundColor(mColors[position]);
//            }
//        });
        mContentView.setBackgroundColor(0xff03a9f4);

    }
}
