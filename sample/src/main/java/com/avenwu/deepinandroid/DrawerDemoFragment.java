package com.avenwu.deepinandroid;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.avenwu.imageview.IndexImageView;

/**
 * Created by Chaobin Wu on 2014/10/10.
 */
public class DrawerDemoFragment extends Fragment {
	DrawerFrame drawerFrame;

	public DrawerDemoFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_my, container);
		drawerFrame = (DrawerFrame) view.findViewById(R.id.view);
		Switch s = (Switch) view.findViewById(R.id.switch1);
		s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					drawerFrame.showMenuSmoothly();
				} else {
					drawerFrame.dismissSmoothly();
				}
			}
		});
		TextView menu = new TextView(getActivity());
		menu.setText("Menu Layout");
		menu.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
		menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Menu clicked", Toast.LENGTH_SHORT).show();
			}
		});
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		menu.setLayoutParams(layoutParams);
		menu.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
		drawerFrame.setMenuView(menu);
		IndexImageView imageView = new IndexImageView(getActivity());
		imageView.setImageResource(R.drawable.ic_launcher);
		imageView.setIndexEnable(true);
		imageView.setText("121");
		imageView.setTextDimension(40);
		FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(200, 200);
		layoutParams2.gravity = Gravity.CENTER;
		imageView.setLayoutParams(layoutParams2);
		drawerFrame.setContentView(imageView);
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Image cliked", Toast.LENGTH_SHORT).show();
			}
		});
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
