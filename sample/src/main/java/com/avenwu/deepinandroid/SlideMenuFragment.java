package com.avenwu.deepinandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.avenwu.support.widget.DrawerFrameV2;

/**
 * Created by chaobin on 2/16/15.
 */
public class SlideMenuFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DrawerFrameV2 view =
                (DrawerFrameV2) inflater.inflate(R.layout.slide_layout, null);
        return view;
//        DrawerLayout
//        SlidingPaneLayout
    }
}
