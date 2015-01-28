package com.avenwu.deepinandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class ShortcutDemo extends ActionBarActivity {
	public static String INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
	public static String UNINSTALL_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shorcut_layout);
		findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AnimationDrawable animationDrawable = (AnimationDrawable) ((ImageView) v).getDrawable();
				animationDrawable.start();
			}
		});
		findViewById(R.id.btn_install).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shortcutAdd("测试", count++, ShortcutDemo.class);
			}
		});
		findViewById(R.id.btn_uninstall).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				shortcutDel("测试", ShortcutDemo.class);
			}
		});
	}

	public static int count = 0;

	private void shortcutAdd(String name, int number, Class<? extends Activity> cls) {
		// Intent to be send, when shortcut is pressed by user ("launched")
		Intent shortcutIntent = new Intent(getApplicationContext(), cls);
		shortcutIntent.setAction(Intent.ACTION_MAIN);
		shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		// Create bitmap with number in it -> very default. You probably want to give it a more stylish look
		Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
		Paint paint = new Paint();
		paint.setColor(0xFF808080); // gray
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTextSize(50);
		new Canvas(bitmap).drawText("" + number, 50, 50, paint);

		// Decorate the shortcut
		Intent addIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);

		// Inform launcher to create shortcut
		addIntent.setAction(INSTALL_SHORTCUT);
		getApplicationContext().sendBroadcast(addIntent);
	}

	private void shortcutDel(String name, Class<? extends Activity> cls) {
		// Intent to be send, when shortcut is pressed by user ("launched")
		Intent shortcutIntent = new Intent(getApplicationContext(), cls);
		shortcutIntent.setAction(Intent.ACTION_MAIN);
		shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		// Decorate the shortcut
		Intent delIntent = new Intent();
		delIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		delIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

		// Inform launcher to remove shortcut
		delIntent.setAction(UNINSTALL_SHORTCUT);
		getApplicationContext().sendBroadcast(delIntent);
	}
}
