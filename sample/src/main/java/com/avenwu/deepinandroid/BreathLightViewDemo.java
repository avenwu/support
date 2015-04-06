package com.avenwu.deepinandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chaobin on 3/3/15.
 */
public class BreathLightViewDemo extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ripple_layout, null);
        view.setOnClickListener(null);
        return view;
    }
}
