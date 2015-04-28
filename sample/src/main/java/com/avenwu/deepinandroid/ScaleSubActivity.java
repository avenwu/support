/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.avenwu.deepinandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * See WindowAnimations.java for comments on the overall application.
 * <p/>
 * This is a sub-activity which provides custom animation behavior. When this activity
 * is exited, the user will see the behavior specified in the overridePendingTransition() call.
 */
public class ScaleSubActivity extends Activity {
    int leftDeta, topDeta;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_scale_sub);
        final int left = getIntent().getIntExtra("left", 0);
        final int top = getIntent().getIntExtra("top", 0);
        final int width = getIntent().getIntExtra("width", 0);
        final int height = getIntent().getIntExtra("height", 0);
        final ImageView imageView = (ImageView) findViewById(R.id.image);
        ViewTreeObserver observer = imageView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int[] position = new int[2];
                imageView.getLocationOnScreen(position);
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                leftDeta = left - position[0];
                topDeta = top - position[1];
                float widthScale = (float) width / imageView.getWidth();
                float heightScale = (float) height / imageView.getHeight();
                imageView.setPivotX(0);
                imageView.setPivotY(0);
                imageView.setTranslationX(leftDeta);
                imageView.setTranslationY(topDeta);
                imageView.setScaleX(widthScale);
                imageView.setScaleY(heightScale);
                imageView.animate().setDuration(5000).setInterpolator(new DecelerateInterpolator())
                        .translationX(0).translationX(0).scaleX(1).scaleY(1).start();
                return true;
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
