package com.avenwu.deepinandroid;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by aven on 4/26/15.
 */
public class WindowAnimationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.animation_layout, null);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    // Using the AnimatedSubActivity also allows us to animate exiting that
                    // activity - see that activity for details
                    Intent subActivity = new Intent(v.getContext(),
                            AnimatedSubActivity.class);
                    // The enter/exit animations for the two activities are specified by xml resources
                    Bundle translateBundle =
                            ActivityOptions.makeCustomAnimation(v.getContext(),
                                    R.anim.slide_in_left, R.anim.slide_out_left).toBundle();
                    getActivity().startActivity(subActivity, translateBundle);
                } else {
                    getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    getActivity().startActivity(new Intent(v.getContext(),
                            AnimatedSubActivity.class));
                }

            }
        });
        view.findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent subActivity = new Intent(v.getContext(),
                        AnimatedSubActivity.class);
//                Bundle scaleBundle = ActivityOptions.makeScaleUpAnimation(
//                        v, 0, 0, v.getWidth(), v.getHeight()).toBundle();
//                getActivity().startActivity(subActivity, scaleBundle);
                v.setDrawingCacheEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getActivity().startActivity(subActivity, ActivityOptions.makeThumbnailScaleUpAnimation(v, v.getDrawingCache(), 0, 0).toBundle());
                } else {
                    getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    getActivity().startActivity(new Intent(v.getContext(),
                            AnimatedSubActivity.class));
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ImageView imageView = (ImageView) view.findViewById(R.id.image2);
        BitmapDrawable[] bitmapDrawable = new BitmapDrawable[2];
        bitmapDrawable[0] = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.image1));
        bitmapDrawable[1] = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.image2));
        final TransitionDrawable drawable = new TransitionDrawable(bitmapDrawable);
        imageView.setImageDrawable(drawable);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDrawable == 0) {
                    drawable.startTransition(500);
                    currentDrawable = 1;
                } else {
                    drawable.reverseTransition(500);
                    currentDrawable = 0;
                }
            }
        });
    }

    int currentDrawable = 0;
}
