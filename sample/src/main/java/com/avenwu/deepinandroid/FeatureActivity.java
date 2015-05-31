package com.avenwu.deepinandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FeatureActivity extends AppCompatActivity {
    View mLeft, mRight;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feature_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new Adapter(getSupportFragmentManager()));
        mLeft = findViewById(R.id.iv_left);
        mRight = findViewById(R.id.iv_right);
        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mLeft.setVisibility(i == 0 ? View.GONE : View.VISIBLE);
                mRight.setVisibility(i == 2 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    class Adapter extends FragmentPagerAdapter {
        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new FragmentItem();
            Bundle bundle = new Bundle();
            switch (i) {
                case 0:
                    bundle.putString(KEY_LABEL, getString(R.string.page_1_text));
                    bundle.putInt(KEY_IMAGE_ID, R.drawable.page_1_image);
                    bundle.putInt(KEY_COLOR_ABOVE, getResources().getColor(R.color.page_1_dark));
                    bundle.putInt(KEY_COLOR_BELOW, getResources().getColor(R.color.page_1));
                    break;
                case 1:
                    bundle.putString(KEY_LABEL, getString(R.string.page_2_text));
                    bundle.putInt(KEY_IMAGE_ID, R.drawable.page_2_image);
                    bundle.putInt(KEY_COLOR_ABOVE, getResources().getColor(R.color.page_2_dark));
                    bundle.putInt(KEY_COLOR_BELOW, getResources().getColor(R.color.page_2));
                    break;
                case 2:
                    bundle.putString(KEY_LABEL, getString(R.string.page_3_text));
                    bundle.putInt(KEY_IMAGE_ID, R.drawable.page_3_image);
                    bundle.putInt(KEY_COLOR_ABOVE, getResources().getColor(R.color.page_3_dark));
                    bundle.putInt(KEY_COLOR_BELOW, getResources().getColor(R.color.page_3));
                    break;
            }
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static final String KEY_LABEL = "key_label";
    public static final String KEY_IMAGE_ID = "key_image_id";
    public static final String KEY_COLOR_ABOVE = "key_color_above";
    public static final String KEY_COLOR_BELOW = "key_color_below";

    public static class FragmentItem extends Fragment {
        public FragmentItem() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.feature_item_layout, null);
            String label = getArguments().getString(KEY_LABEL);
            ((TextView) view.findViewById(R.id.tv_label)).setText(label);
            int color1 = getArguments().getInt(KEY_COLOR_ABOVE);
            view.findViewById(R.id.ll_above).setBackgroundColor(color1);
            int color2 = getArguments().getInt(KEY_COLOR_BELOW);
            view.findViewById(R.id.ll_below).setBackgroundColor(color2);
            int image = getArguments().getInt(KEY_IMAGE_ID);
            ((ImageView) view.findViewById(R.id.iv_image)).setImageResource(image);
            return view;
        }
    }
}
