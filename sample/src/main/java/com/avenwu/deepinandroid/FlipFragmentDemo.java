package com.avenwu.deepinandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.avenwu.support.widget.FlipLayout;

/**
 * Created by aven on 1/19/16.
 */
public class FlipFragmentDemo extends Fragment {
    FlipLayout mFlipLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.flip_layout, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFlipLayout = (FlipLayout) view.findViewById(R.id.flip_layout);
        mFlipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlipLayout.flip();
            }
        });
        mFlipLayout.setViewOrder(view.findViewById(R.id.iv_image1), view.findViewById(R.id.iv_image2));
    }
}
