package com.avenwu.deepinandroid;

import com.avenwu.deepinandroid.eventbus.Bus;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by chaobin on 1/29/15.
 */
public class BusEventDemo extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventbus_layout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bus.getDefault().register(this);
    }

    public void onSendClick(View view) {
        Bus.getDefault().post("Hello world");
    }

    public void onEvent(String event) {
        Log.d("BusEventDemo", "onEvent hit");
        Toast.makeText(this, "onEvent hit:" + event, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Bus.getDefault().unregister(this);
    }
}
