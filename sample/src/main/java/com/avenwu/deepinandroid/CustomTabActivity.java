package com.avenwu.deepinandroid;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import net.avenwu.support.widget.SimpleTab;

/**
 * Created by aven on 5/3/15.
 */
public class CustomTabActivity extends FragmentActivity {
    SimpleTab mSimpleTab;
    FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_tab_activity);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.container);
        mSimpleTab = new SimpleTab.Builder(this)
                .newItem(new SimpleTab.Item().setLabelWithIcon(R.string.tab_1, R.drawable.ic_launcher))
                .newItem(new SimpleTab.Item().setLabelWithIcon(R.string.tab_2, R.drawable.ic_launcher))
                .newItem(new SimpleTab.Item().setLabelWithIcon(R.string.tab_3, R.drawable.ic_launcher))
                .newItem(new SimpleTab.Item().setLabelWithIcon(R.string.tab_4, R.drawable.ic_launcher))
                .setOnTabClickListener(new SimpleTab.OnTabClickListener() {
                    @Override
                    public void onItemClick(View view, SimpleTab.Item item, int position) {
                        Toast.makeText(CustomTabActivity.this, "position=" + position, Toast
                                .LENGTH_SHORT).show();
                    }
                })
                .create();
        mSimpleTab.injectInto(viewGroup);
//        TabWidget
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("Tab 1", null).setIndicator(getTabIndicator(this, R.string.tab_1, R.drawable.ic_launcher)),
                FragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator("Tab 2", null).setIndicator
                        (getTabIndicator(this, R.string.tab_2, R.drawable.ic_launcher)),
                FragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator("Tab 3", null).setIndicator
                        (getTabIndicator(this, R.string.tab_3, R.drawable.ic_launcher)),
                FragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab4").setIndicator("Tab 3", null).setIndicator
                        (getTabIndicator(this, R.string.tab_4, R.drawable.ic_launcher)),
                FragmentTab.class, null);
    }

    private View getTabIndicator(Context context, int title, int icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_tab_item, null);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.setImageResource(icon);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }
    public static class FragmentTab extends Fragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_tab_host, container, false);
            TextView tv = (TextView) v.findViewById(R.id.text);
            tv.setText(this.getTag() + " Content");
            return v;
        }
    }
}
