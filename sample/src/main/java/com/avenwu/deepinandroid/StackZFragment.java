package com.avenwu.deepinandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by aven on 1/27/16.
 */
public class StackZFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stack_z, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int count = ((ViewGroup) view).getChildCount();
        for (int i = 0; i < count; i++) {
            View card = ((ViewGroup) view).getChildAt(i);
            ViewCompat.setTranslationZ(card, (i + 1) * 8 * getResources().getDisplayMetrics().density);
        }
    }
}
