package com.avenwu.deepinandroid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.SeekBar;

import net.avenwu.support.widget.PolygonWithQuadraticBezirView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chaobin on 12/25/14.
 */
public class QQDraggingCircleDemo extends ActionBarActivity {
    @InjectView(R.id.shape2)
    PolygonWithQuadraticBezirView mShape2;
    @InjectView(R.id.shape1)
    PolygonWithQuadraticBezirView mShape1;
    @InjectView(R.id.seekBarX)
    SeekBar mSeekX;
    @InjectView(R.id.seekBarY)
    SeekBar mSeekY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qq_dragging_circle_layout);
        ButterKnife.inject(this);
        mShape2.setFilled(true);
        mSeekX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mShape1.moveHorizontal(progress / 100.0f);
                mShape2.moveHorizontal(progress / 100.0f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSeekY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mShape1.moveVertical(progress / 100.0f);
                mShape2.moveVertical(progress / 100.0f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
