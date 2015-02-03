package com.avenwu.deepinandroid;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chaobin on 2/3/15.
 */

public class ColorFilterDemo extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    @InjectView(R.id.spinner)
    Spinner mPorterDuffSpinner;
    @InjectView(R.id.iv_preview)
    ImageView mPreviewView;
    @InjectView(R.id.iv_image)
    ImageView mImage;
    static final int MASK_HINT_COLOR = 0x99000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_layout);
        ButterKnife.inject(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.porter_duff_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mPorterDuffSpinner.setPrompt("Select the PorterDuff");
        mPorterDuffSpinner.setAdapter(adapter);
        mPorterDuffSpinner.setOnItemSelectedListener(this);
        int defaultSelection = 0;
        try {
            Field field = PorterDuff.Mode.class.getDeclaredField("nativeInt");
            field.setAccessible(true);
            defaultSelection = field.getInt(PorterDuff.Mode.DARKEN);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        mPorterDuffSpinner.setSelection(defaultSelection);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        PorterDuff.Mode mode = PorterDuff.Mode.class.getEnumConstants()[position];
        mPreviewView.setColorFilter(Color.GREEN, mode);
        mImage.setColorFilter(MASK_HINT_COLOR, mode);
        Toast.makeText(this, "Select " + mode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
