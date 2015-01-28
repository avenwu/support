package com.avenwu.deepinandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

public class RefreshWidgetActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refresh_widget);
		try {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, (Fragment) (((Class) getIntent().getSerializableExtra("fragment")).newInstance()))
					.commitAllowingStateLoss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
