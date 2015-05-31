package net.avenwu.support.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by aven on 5/3/15.
 */
public class SimpleTabLayout extends LinearLayout {
    SimpleTab mSimpleTab;

    public SimpleTabLayout(Context context) {
        this(context, null);
    }

    public SimpleTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSimpleTab = new SimpleTab.Builder(getContext())
                .newItem(new SimpleTab.Item().setLabelWithIcon(android.R.string.no,
                        android.R.drawable.ic_delete))
                .newItem(new SimpleTab.Item().setLabelWithIcon(android.R.string.no,
                        android.R.drawable.ic_delete))
                .newItem(new SimpleTab.Item().setLabelWithIcon(android.R.string.no,
                        android.R.drawable.ic_delete))
                .newItem(new SimpleTab.Item().setLabelWithIcon(android.R.string.no,
                        android.R.drawable.ic_delete))
                .setOnTabClickListener(new SimpleTab.OnTabClickListener() {
                    @Override
                    public void onItemClick(View view, SimpleTab.Item item, int position) {
                        Toast.makeText(getContext(), "Click " + position, Toast.LENGTH_SHORT).show();
                    }
                })
                .bind(this);
    }
}
