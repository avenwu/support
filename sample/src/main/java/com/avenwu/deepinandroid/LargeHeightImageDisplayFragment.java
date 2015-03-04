package com.avenwu.deepinandroid;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.IOException;

/**
 * Created by chaobin on 3/2/15.
 */
public class LargeHeightImageDisplayFragment extends Fragment {
    private static final String TAG = LargeHeightImageDisplayFragment.class.getSimpleName();
    private ViewGroup mContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScrollView scrollView = new ScrollView(getActivity());
        scrollView.setBackgroundColor(Color.WHITE);
        scrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mContainer = linearLayout;
        scrollView.addView(linearLayout);
        return scrollView;
    }

    AsyncTask<Context, Bitmap, Object> mDecodeTask;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mDecodeTask = new AsyncTask<Context, Bitmap, Object>() {
            @Override
            protected Object doInBackground(Context... params) {
                Log.d(TAG, "Start decode image...");
                BitmapRegionDecoder decoder = null;
                try {
                    //TODO According to api document both jpeg and png are supported, however jpeg image just failed to be decoded on this case
                    decoder = BitmapRegionDecoder.newInstance(params[0].getAssets().open("image.png", AssetManager.ACCESS_RANDOM), false);
                    final int screenWidth = params[0].getResources().getDisplayMetrics().widthPixels;
                    final int imageWidth = decoder.getWidth();
                    final int imageHeight = decoder.getHeight();
                    final int eachHeight = (int) (screenWidth * ((imageWidth + 0.5f) / screenWidth));
                    int heightRemained = imageHeight;
                    Rect corpRect = new Rect(0, 0, imageWidth, 0);
                    //TODO the while case is only for test, should only load specific bitmap data when scrolled to be visible
                    while (heightRemained > 0 && !isCancelled()) {
                        Log.d(TAG, "clip image");
                        if (heightRemained >= eachHeight) {
                            corpRect.set(corpRect.left, corpRect.bottom, corpRect.right, corpRect.bottom + eachHeight);
                            heightRemained -= eachHeight;
                        } else {
                            corpRect.set(corpRect.left, corpRect.bottom, corpRect.right, corpRect.bottom + heightRemained);
                            heightRemained = 0;
                        }
                        Log.d(TAG, "corptBitmap, " + corpRect.toString());
                        Bitmap corptBitmap = decoder.decodeRegion(corpRect, null);
                        publishProgress(corptBitmap);
                    }
                    Log.d(TAG, "Image decode finished");
                } catch (IOException e) {
                    Log.d(TAG, "Image decode failed");
                    e.printStackTrace();
                } finally {
                    if (decoder != null) {
                        decoder.recycle();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Bitmap... values) {
                if (getActivity() != null && !isRemoving() && values[0] != null) {
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    imageView.setImageBitmap(values[0]);
                    mContainer.addView(imageView);
                }
            }
        };
        AsyncTaskCompat.executeParallel(mDecodeTask, getActivity());
    }

    @Override
    public void onDestroyView() {
        if (mContainer != null) {
            mContainer.removeAllViews();
        }
        if (mDecodeTask != null) {
            mDecodeTask.cancel(true);
        }
        super.onDestroyView();
    }
}
