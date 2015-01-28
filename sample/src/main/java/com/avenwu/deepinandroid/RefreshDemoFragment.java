package com.avenwu.deepinandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SimpleAdapter;

import net.avenwu.support.widget.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chaobin Wu on 2014/10/10.
 */
public class RefreshDemoFragment extends Fragment {

	public RefreshDemoFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		RefreshLayout view = new RefreshLayout(getActivity());
		view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		view.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
		List<Map<String, String>> data = new ArrayList<Map<String, String>>(30);
		for (int i = 0; i < 20; i++) {
			Map<String, String> item = new HashMap<String, String>(2);
			item.put("index", i + "");
			item.put("text", "This is content " + i);
			data.add(item);
		}
		view.setAdapter(new SimpleAdapter(getActivity(), data, android.R.layout.simple_list_item_2,
				new String[]{"index", "text"}, new int[]{android.R.id.text1, android.R.id.text2}));
		return view;
	}
}
